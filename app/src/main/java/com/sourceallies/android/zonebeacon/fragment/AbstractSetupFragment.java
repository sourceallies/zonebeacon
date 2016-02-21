package com.sourceallies.android.zonebeacon.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Abstract fragment to use with the CreationActivity to create new data
 */
public abstract class AbstractSetupFragment extends Fragment {
    private static final String TAG = "AbstractSetupFragment";

    /**
     * Default constructor for the fragments. Logs that we are starting a setup.
     */
    public AbstractSetupFragment() {
        super();
        Log.v(TAG, "starting setup fragment");
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
