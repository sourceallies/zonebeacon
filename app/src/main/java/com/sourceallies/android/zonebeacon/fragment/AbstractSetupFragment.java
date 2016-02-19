package com.sourceallies.android.zonebeacon.fragment;

import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class AbstractSetupFragment extends Fragment {

    private static final String TAG = "AbstractSetupFragment";

    public AbstractSetupFragment() {
        super();
        Log.v(TAG, "starting setup fragment");
    }

    public abstract void save();
    public abstract boolean isComplete();
}
