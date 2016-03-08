package com.sourceallies.android.zonebeacon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.sourceallies.android.zonebeacon.activity.CreationActivity;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import java.util.List;

/**
 * Abstract fragment to use with the CreationActivity to create new data
 */
public abstract class AbstractSetupFragment extends Fragment {
    private static final String TAG = "AbstractSetupFragment";

    public static AbstractSetupFragment getInstance(AbstractSetupFragment fragment, long gatewayId) {
        Bundle args = new Bundle();
        args.putLong(CreationActivity.ARG_GATEWAY_ID, gatewayId);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Default constructor for the fragments. Logs that we are starting a setup.
     */
    public AbstractSetupFragment() {
        super();
        Log.v(TAG, "starting setup fragment");
    }

    /**
     * Grab the gateway that was sent into the fragment
     * @return
     */
    protected Gateway getCurrentGateway() {
        long gatewayId = getArguments().getLong(CreationActivity.ARG_GATEWAY_ID);

        DataSource source = DataSource.getInstance(getActivity());
        source.open();

        Gateway gateway = source.findGateway(gatewayId);

        source.close();

        return gateway;
    }

    /**
     * Save the information from the setup fragment to the database
     */
    public abstract void save();

    /**
     * Check whether or not the user should be done filling in information from the UI.
     *
     * @return true if user input is complete, false otherwise
     */
    public abstract boolean isComplete();
}
