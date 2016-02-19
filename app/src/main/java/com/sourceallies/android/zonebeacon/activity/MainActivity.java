/*
 * Copyright (C) 2016 Source Allies
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.inject.Inject;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.adapter.GatewaySpinnerAdapter;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Main activity that the user will be interacting with when using the app.
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends RoboAppCompatActivity {

    private static final int RESULT_INTRO = 1;

    @Inject @Setter
    private SharedPreferences sharedPrefs;

    @Getter private CoordinatorLayout rootLayout;
    @Getter private Toolbar toolbar;
    @Getter private Spinner spinner;
    @Getter private FloatingActionsMenu fabMenu;

    @Getter private FloatingActionButton addZone;
    @Getter private FloatingActionButton addButton;
    @Getter private FloatingActionButton addCommand;

    @Getter @Setter
    private GatewaySpinnerAdapter adapter;
    @Getter private int currentSpinnerSelection = 0;

    @Getter private boolean startedIntro = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinner = (Spinner) findViewById(R.id.toolbar_spinner);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);

        addZone = (FloatingActionButton) findViewById(R.id.add_zone);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);
        addCommand = (FloatingActionButton) findViewById(R.id.add_command);

        setSpinnerAdapter();
        setSupportActionBar(toolbar);
        setTitle("");

        setFabTitle(addZone);
        setFabTitle(addButton);
        setFabTitle(addCommand);
    }

    private void setFabTitle(final FloatingActionButton fab) {
        fab.setTitle(fab.getTitle().replace("%s", getGatewayName()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                makeSnackbar(fab.getTitle());
            }
        });
    }

    @VisibleForTesting
    protected GatewaySpinnerAdapter createAdapter(DataSource dataSource) {
        return new GatewaySpinnerAdapter(this, dataSource.findGateways());
    }

    private void setSpinnerAdapter() {
        DataSource dataSource = DataSource.getInstance(this);
        dataSource.open();

        adapter = createAdapter(dataSource);
        spinner.setAdapter(adapter);

        dataSource.close();

        startIntro();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onNothingSelected(AdapterView<?> parent) { }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == spinner.getCount() - 1 && spinner.getCount() > 1) {
                    createNewGateway();
                    spinner.setSelection(currentSpinnerSelection);
                } else {
                    currentSpinnerSelection = position;
                }
            }
        });
    }

    public void createNewGateway() {
        // TODO: create a new gateway here
        makeSnackbar("Create New Gateway");
    }

    private String getGatewayName() {
        return adapter.getTitle(spinner.getSelectedItemPosition());
    }

    /**
     * Start the intro activity if necessary
     */
    public boolean startIntro() {
        if (adapter.getCount() == 1) {
            startActivityForResult(new Intent(this, IntroActivity.class), RESULT_INTRO);
            return true;
        }

        return false;
    }

    private void makeSnackbar(String text) {
        Snackbar.make(rootLayout, text, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_INTRO &&
                (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            recreate();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
            case R.id.settings: openOption(IntroActivity.class);
                return true;
            case R.id.diagnosis: openOption(null);
                return true;
            case R.id.get_help: openOption(null);
                return true;
            case R.id.transfer_settings: openOption(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openOption(Class toOpen) {
        if (toOpen != null) {
            Intent option = new Intent(this, toOpen);
            startActivity(option);
        } else {
            makeSnackbar("No class to open.");
        }
    }

}
