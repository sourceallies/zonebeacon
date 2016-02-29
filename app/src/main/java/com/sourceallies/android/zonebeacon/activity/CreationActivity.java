package com.sourceallies.android.zonebeacon.activity;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.sourceallies.android.zonebeacon.fragment.GatewaySetupFragment;

/**
 * Base activity for creating new data/configurations, including:
 * - Gateway
 * - Zone
 * - Button
 * - Command
 */
public class CreationActivity extends IntroActivity {

    /**
     * Static factory method to start the activity
     *
     * @param activity  The current context
     * @param setupType TYPE_GATEWAY, TYPE_ZONE, TYPE_BUTTON, or TYPE_COMMAND
     */
    public static void startCreation(Activity activity, int setupType) {
        Intent creation = new Intent(activity, CreationActivity.class);
        creation.putExtra(ARG_SETUP_TYPE, setupType);

        activity.startActivityForResult(creation, setupType);
    }

    // argument to use with the intent
    public static final String ARG_SETUP_TYPE = "arg_setup_type";

    public static final int TYPE_GATEWAY = 101;
    public static final int TYPE_ZONE = 102;
    public static final int TYPE_BUTTON = 103;
    public static final int TYPE_COMMAND = 104;

    /**
     * Get the fragment type we want to use to create data
     *
     * @return TYPE_GATEWAY, TYPE_ZONE, TYPE_BUTTON, or TYPE_COMMAND
     */
    @VisibleForTesting
    protected int getFragmentType() {
        return getIntent().getIntExtra(ARG_SETUP_TYPE, TYPE_GATEWAY);
    }

    /**
     * Add the setup slide based on the ARG_SETUP_TYPE
     */
    @Override
    public void addSlides() {
        switch (getFragmentType()) {
            // TODO: break these apart when more fragments are created.
            case TYPE_ZONE:
            case TYPE_BUTTON:
            case TYPE_COMMAND:
            case TYPE_GATEWAY:
                setupFragment = new GatewaySetupFragment();
                break;
        }

        addSlide(setupFragment);
    }
}
