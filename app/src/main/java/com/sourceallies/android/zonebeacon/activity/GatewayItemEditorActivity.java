package com.sourceallies.android.zonebeacon.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.adapter.EditAdapter;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import lombok.Getter;
import lombok.Setter;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_gateway_item_editor)
public class GatewayItemEditorActivity extends RoboAppCompatActivity {


    @Getter
    private CoordinatorLayout rootLayout;
    @Setter
    @Getter
    private RecyclerView recycler;
    @Getter
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Find the layout information
        rootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.edit_gateway_information));


    }

    private void addAdapter() {
        DataSource source = DataSource.getInstance(this);
        source.open();
        Gateway gateway = source.findGateway(1);
        source.close();

        EditAdapter editAdapter = new EditAdapter(this, gateway);

        GridLayoutManager manager = getLayoutManager();
        recycler.setLayoutManager(manager);
        editAdapter.setLayoutManager(manager);
        recycler.setAdapter(editAdapter);
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
}
