package com.sourceallies.android.zonebeacon.activity;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.sourceallies.android.zonebeacon.fragment.AddButtonFragment;
import com.sourceallies.android.zonebeacon.fragment.AddZoneFragment;
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
        startCreation(activity, setupType, -1l);
    }

    /**
     * Static factory method to start the activity
     *
     * @param activity  The current context
     * @param setupType TYPE_GATEWAY, TYPE_ZONE, TYPE_BUTTON, or TYPE_COMMAND
     * @param gatewayId the id value for the current gateway
     */
    public static void startCreation(Activity activity, int setupType, long gatewayId) {
        Intent creation = new Intent(activity, CreationActivity.class);
        creation.putExtra(ARG_SETUP_TYPE, setupType);
        creation.putExtra(ARG_GATEWAY_ID, gatewayId);

        activity.startActivityForResult(creation, setupType);
    }

    // argument to use with the intent
    public static final String ARG_SETUP_TYPE = "arg_setup_type";
    public static final String ARG_GATEWAY_ID = "arg_gateway_id";

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

    @VisibleForTesting
    protected long getGatewayId() {
        return getIntent().getLongExtra(ARG_GATEWAY_ID, -1);
    }

    /**
     * Add the setup slide based on the ARG_SETUP_TYPE
     */
    @Override
    public void addSlides() {
        switch (getFragmentType()) {
            // TODO: break these apart when more fragments are created.
            case TYPE_ZONE:
                setupFragment = AddZoneFragment.getInstance(new AddZoneFragment(), getGatewayId());
                break;
            case TYPE_BUTTON:
                setupFragment = AddButtonFragment.getInstance(new AddButtonFragment(), getGatewayId());
                break;
            case TYPE_COMMAND:
            case TYPE_GATEWAY:
                setupFragment = GatewaySetupFragment.getInstance(new GatewaySetupFragment(), getGatewayId());
                break;
        }

        addSlide(setupFragment);
    }
}
