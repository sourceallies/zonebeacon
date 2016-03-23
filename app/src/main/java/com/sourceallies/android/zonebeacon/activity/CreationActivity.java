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
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.fragment.AddButtonFragment;
import com.sourceallies.android.zonebeacon.fragment.AddZoneFragment;
import com.sourceallies.android.zonebeacon.fragment.CommandSetupFragment;
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
        startCreation(activity, setupType, -1L);
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
            case TYPE_ZONE:
                setupFragment = AddZoneFragment.
                        getInstance(new AddZoneFragment(), getGatewayId());
                break;
            case TYPE_BUTTON:
                setupFragment = AddButtonFragment.
                        getInstance(new AddButtonFragment(), getGatewayId());
                break;
            case TYPE_COMMAND:
                setupFragment = CommandSetupFragment.
                        getInstance(new CommandSetupFragment(), getGatewayId());
            case TYPE_GATEWAY:
                setupFragment = GatewaySetupFragment.
                        getInstance(new GatewaySetupFragment(), getGatewayId());
                break;
        }

        addSlide(setupFragment);
    }
}
