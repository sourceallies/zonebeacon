package com.sourceallies.android.zonebeacon.fragment;

import android.support.v4.app.Fragment;

public abstract class AbstractSetupFragment extends Fragment {
    public abstract void save();
    public abstract boolean isComplete();
}
