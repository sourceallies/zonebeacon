package com.sourceallies.android.zonebeacon.activity;

import android.view.View;

import com.sourceallies.android.zonebeacon.R;

import roboguice.inject.ContentView;

/**
 * A test activity to send plain text commands to the centralite briefcase
 */
@ContentView(R.layout.activity_send_tcp)
public class SendTCPActivity extends RoboAppCompatActivity {
    // simply holds the fragment, we don't need to do anything here
    // since the contentview is injected.
    public View getFragment() {
        return findViewById(R.id.tcp_fragment);
    }
}
