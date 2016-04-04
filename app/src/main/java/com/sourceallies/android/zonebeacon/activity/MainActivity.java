/*
 * Copyright (C) 2016 Source Allies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sourceallies.android.zonebeacon.activity;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.adapter.GatewaySpinnerAdapter;
import com.sourceallies.android.zonebeacon.adapter.MainAdapter;
import com.sourceallies.android.zonebeacon.api.QueryLoadsCallback;
import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Zone;
import com.sourceallies.android.zonebeacon.fragment.BrightnessControlFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import roboguice.inject.ContentView;

/**
 * Main activity that the user will be interacting with when using the app.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboAppCompatActivity {

    public static final int RESULT_INTRO = 1;

    @Getter
    private CoordinatorLayout rootLayout;
    @Setter @Getter
    private RecyclerView recycler;
    @Getter
    private SwipeRefreshLayout swipeRefreshLayout;
    @Getter
    private Toolbar toolbar;
    @Getter
    private Spinner spinner;
    @Getter
    private View dim;
    @Getter
    private FloatingActionsMenu fabMenu;

    @Getter
    private FloatingActionButton getHelp;
    @Getter
    private FloatingActionButton addZone;
    @Getter
    private FloatingActionButton addButton;
    @Getter
    private FloatingActionButton addCommand;

    @Getter
    private MainAdapter mainAdapter;

    @Setter
    private GatewaySpinnerAdapter spinnerAdapter;

    @Getter
    private int currentSpinnerSelection = 0;

    @Getter
    private boolean startedIntro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find the layout information
        rootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner = (Spinner) findViewById(R.id.toolbar_spinner);
        dim = findViewById(R.id.dim);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        getHelp = (FloatingActionButton) findViewById(R.id.get_help);
        addZone = (FloatingActionButton) findViewById(R.id.add_zone);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addCommand = (FloatingActionButton) findViewById(R.id.add_command);

        // set up the app bar with a spinner, toolbar, and no title
        setSpinnerAdapter();
        setSupportActionBar(toolbar);
        setTitle("");

        // put the labels on the floating action buttons.
        setFabButtons();

        swipeRefreshLayout.setOnRefreshListener(getRefreshListener(this));
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
    }

    @VisibleForTesting
    protected SwipeRefreshLayout.OnRefreshListener getRefreshListener(final MainActivity activity) {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activity.setRecycler();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    /**
     * Set the FAB labels to their defaults (containing %s placeholder)
     */
    private void resetFabButtons() {
        addZone.setTitle(getString(R.string.add_zone));
        addButton.setTitle(getString(R.string.add_button));
        addCommand.setTitle(getString(R.string.add_command));
    }

    /**
     * Resets the FAB labels and replaces them with the current gateway information
     */
    private void setFabButtons() {
        resetFabButtons();

        setFabTitle(addZone);
        setFabTitle(addButton);
        setFabTitle(addCommand);

        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuCollapsed() {
                dim.setVisibility(View.GONE);
            }

            @Override
            public void onMenuExpanded() {
                dim.setVisibility(View.VISIBLE);
            }
        });

        dim.setOnTouchListener(getDimListener());

        getHelp.setOnClickListener(getHelpListener());
    }

    @VisibleForTesting
    protected View.OnClickListener getHelpListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseFab();
                openOption(GetHelpActivity.class);
            }
        };
    }

    @VisibleForTesting
    protected View.OnTouchListener getDimListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fabMenu.collapse();
                return true;
            }
        };
    }

    /**
     * Replaces the %s placeholder in the Floating Action Buttons with the current gateway
     *
     * @param fab floating action button that we are setting the text for
     */
    private void setFabTitle(final FloatingActionButton fab) {
        fab.setTitle(fab.getTitle().replace("%s", getGatewayName()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseFab();

                if (fab == addZone){
                    addZone();
                } else if (fab == addButton) {
                    addButton();
                } else if (fab == addCommand) {
                    addCommand();
                }
            }
        });
    }

    /**
     * Displays the the brightness control fragment.
     */
    public void showBrightnessDialog() {
        FragmentManager fm = getSupportFragmentManager();
        BrightnessControlFragment brightnessControl = BrightnessControlFragment.newInstance();
        brightnessControl.show(fm, "fragment_brightness_control");
    }


    /**
     * Creates the adapter for the toolbar spinner that displays the gateways
     *
     * @param dataSource The database for the adapter
     * @return Spinner adapter holding the gateway information
     */
    @VisibleForTesting
    protected GatewaySpinnerAdapter createSpinnerAdapter(DataSource dataSource) {
        return new GatewaySpinnerAdapter(this, dataSource.findGateways());
    }

    /**
     * creates the recycler view to display all the information about zones and buttons
     * on the current gateway
     */
    @VisibleForTesting
    protected void setRecycler() {
        final Gateway currentGateway = spinnerAdapter.getItem(getCurrentSpinnerSelection());
        DataSource source = getDataSource();
        source.open();

        GridLayoutManager manager = getLayoutManager();
        recycler.setLayoutManager(manager);

        if (currentGateway != null) {
            final List<Zone> zones = source.findZones(currentGateway);
            final List<Button> buttons = source.findButtons(currentGateway);

            Executor executor = getExecutor();
            executor.queryActiveLoads(currentGateway, getQueryCallback(this, currentGateway, zones, buttons));

            mainAdapter = new MainAdapter(
                    MainActivity.this,
                    currentGateway
            );

            loadOnOffStatusesToAdapter(
                    source.findZones(currentGateway),
                    source.findButtons(currentGateway),
                    null
            );

            mainAdapter.setLayoutManager(manager);
        } else {
            mainAdapter = null;
        }

        recycler.setAdapter(mainAdapter);
        source.close();
    }

    /**
     * Apply the load status map to the adapter
     *
     * @param zones List of zones in the current gateway
     * @param buttons list of buttons in the current gateway
     * @param map map of the load statuses. Load number is key, status is the value
     */
    @VisibleForTesting
    protected void loadOnOffStatusesToAdapter(List<Zone> zones, List<Button> buttons, Map<Integer, Executor.LoadStatus> map) {
        mainAdapter.loadOnOffStatuses(
                zones,
                buttons,
                map
        );
    }

    /**
     * Get the instance of the DataSource.
     *
     * @return singleton of the DataSource
     */
    @VisibleForTesting
    protected DataSource getDataSource() {
        return DataSource.getInstance(this);
    }

    /**
     * Get the instance of the Executor
     *
     * @return singleton of the Executor
     */
    @VisibleForTesting
    protected Executor getExecutor() {
        return Executor.createForGateway(getCurrentGateway());
    }

    /**
     * Get the callback that used for querying the gateway for which loads are on and off
     *
     * @param currentGateway the currently selected gateway
     * @param zones list of zones on the gateway
     * @param buttons list of buttons on the gateway
     * @return callback that will reset the RecyclerView to have a list of load statuses
     */
    @VisibleForTesting
    protected QueryLoadsCallback getQueryCallback(final Activity activity, final Gateway currentGateway, final List<Zone> zones, final List<Button> buttons) {
        return new QueryLoadsCallback() {
            @Override
            public void onResponse(final Map<Integer, Executor.LoadStatus> loadStatusMap) {
                activity.runOnUiThread(setLoadStatusRunnable(currentGateway, zones, buttons, loadStatusMap));
            }
        };
    }

    /**
     * Get the callback that used for querying the gateway for which loads are on and off
     *
     * @param currentGateway the currently selected gateway
     * @param zones list of zones on the gateway
     * @param buttons list of buttons on the gateway
     * @param map the map of load statuses with the load number as the key, and the status as the value
     * @return Runnable that will reset the RecyclerView to have a list of load statuses
     */
    @VisibleForTesting
    protected Runnable setLoadStatusRunnable(final Gateway currentGateway, final List<Zone> zones,
                                             final List<Button> buttons, final Map<Integer, Executor.LoadStatus> map) {
        return new Runnable() {
            @Override
            public void run() {
                mainAdapter = new MainAdapter(
                        MainActivity.this,
                        currentGateway
                );

                loadOnOffStatusesToAdapter(zones, buttons, map);
                recycler.setAdapter(mainAdapter);
            }
        };
    }

    /**
     * Grab the layout manager for the list and adapter;
     *
     * @return GridLayoutManager for the view
     */
    @VisibleForTesting
    protected GridLayoutManager getLayoutManager() {
        return new GridLayoutManager(this, getColumnCount());
    }

    /**
     * Get the number of columns in the grid.
     *
     * @return integer for the number of columns used in the grid
     */
    @VisibleForTesting
    protected int getColumnCount() {
        Resources res = getResources();

        if (res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) return 2;
        if (res.getBoolean(R.bool.tablet)) return 2;

        return 1;
    }

    /**
     * Adds the current gateways in the database to the toolbar spinner.
     */
    @VisibleForTesting
    protected void setSpinnerAdapter() {
        DataSource dataSource = getDataSource();
        dataSource.open();

        spinnerAdapter = createSpinnerAdapter(dataSource);
        spinner.setAdapter(spinnerAdapter);

        dataSource.close();

        startIntro();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == spinner.getCount() - 1 && spinner.getCount() > 1) {
                    createNewGateway();
                    spinner.setSelection(currentSpinnerSelection);
                } else {
                    currentSpinnerSelection = position;
                    setFabButtons();
                    setRecycler();
                }
            }
        });
    }

    /**
     * Use the creation activity to create a new gateway
     */
    public void createNewGateway() {
        CreationActivity.startCreation(this, CreationActivity.TYPE_GATEWAY);
    }


    /**
     * Use the creation activity to add a zone.
     */
    public void addZone() {
        startCreationActivity(CreationActivity.TYPE_ZONE);
    }

    /**
     * Use the creation activity to add a button.
     */
    public void addButton() {
        startCreationActivity(CreationActivity.TYPE_BUTTON);
    }

    /**
     * Use the creation activity to add a command.
     */
    public void addCommand() {
        startCreationActivity(CreationActivity.TYPE_COMMAND);
    }

    private void startCreationActivity(int type) {
        CreationActivity.startCreation(this, type, getCurrentGatewayId());
    }

    /**
     * Get the name of whatever gateway the user has currently selected
     *
     * @return String of the gateway name
     */
    private String getGatewayName() {
        return spinnerAdapter.getTitle(getCurrentSpinnerSelection());
    }

    /**
     * Get the name of whatever gateway the user has currently selected
     *
     * @return String of the gateway name
     */
    private Gateway getCurrentGateway() {
        return spinnerAdapter.getItem(getCurrentSpinnerSelection());
    }

    protected long getCurrentGatewayId() {
        return getCurrentGateway().getId();
    }

    /**
     * Start the intro activity if necessary
     */
    public boolean startIntro() {
        if (spinnerAdapter.getCount() == 1) {
            startActivityForResult(new Intent(this, IntroActivity.class), RESULT_INTRO);
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_INTRO &&
                (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            // if the user is just returning from the intro, then we will recreate everything
            recreate();
        } else if (requestCode == CreationActivity.TYPE_GATEWAY && resultCode == RESULT_OK) {
            // if the user just added a gateway, it should be put into the spinner
            setSpinnerAdapter();
        } else if ((requestCode == CreationActivity.TYPE_BUTTON || requestCode == CreationActivity.TYPE_ZONE) &&
                resultCode == RESULT_OK) {
            // if the user added a button or zone, we want to update the list
            setRecycler();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Add the data to the spinnerAdapter and display it in the recycler
        setRecycler();
    }

    @Override
    public void onBackPressed() {
        if (fabMenu.isExpanded()) {
            collapseFab();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Collapse the fab menu.
     */
    @VisibleForTesting
    protected void collapseFab() {
        fabMenu.collapse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                openOption(IntroActivity.class);
                return true;
            case R.id.diagnosis:
                openOption(null);
                return true;
            case R.id.transfer_settings:
                openOption(TransferActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Start the activity for the class you want to open
     *
     * @param toOpen the class that should be opened.
     */
    public void openOption(Class toOpen) {
        if (toOpen != null) {
            Intent option = new Intent(this, toOpen);
            startActivity(option);
        }
    }

}
