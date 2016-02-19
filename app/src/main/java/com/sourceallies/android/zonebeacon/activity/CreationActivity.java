package com.sourceallies.android.zonebeacon.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.sourceallies.android.zonebeacon.fragment.GatewaySetupFragment;

public class CreationActivity extends IntroActivity {

    public static void startCreation(Activity activity, int setupType) {
        Intent creation = new Intent(activity, CreationActivity.class);
        creation.putExtra(ARG_SETUP_TYPE, setupType);

        activity.startActivityForResult(creation, setupType);
    }

    public static final String ARG_SETUP_TYPE = "arg_setup_type";

    public static final int TYPE_GATEWAY = 101;
    public static final int TYPE_ZONE = 102;
    public static final int TYPE_BUTTON = 103;
    public static final int TYPE_COMMAND = 104;

    @VisibleForTesting
    protected int getFragmentType() {
        return getIntent().getIntExtra(ARG_SETUP_TYPE, TYPE_GATEWAY);
    }

    @Override
    public void addSlides() {
        switch (getFragmentType()) {
            // TODO: break these apart when more fragments are created.
            case TYPE_GATEWAY:
            case TYPE_ZONE:
            case TYPE_BUTTON:
            case TYPE_COMMAND:
                setupFragment = new GatewaySetupFragment();
                break;
        }

        addSlide(setupFragment);
    }
}
