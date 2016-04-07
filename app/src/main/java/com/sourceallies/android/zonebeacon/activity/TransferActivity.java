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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.sourceallies.android.zonebeacon.R;

import lombok.Getter;
import lombok.Setter;
import roboguice.inject.ContentView;

/**
 * Activity for handing Google Nearby transaction. These transactions can be used to send your
 * database from one device to another so that you don't have to manually copy over settings to
 * a new device.
 */
@ContentView(R.layout.activity_transfer)
public class TransferActivity extends RoboAppCompatActivity
        implements DialogInterface.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private static final String TAG = "TransferActivity";
    private static final String SEEN_NEARBY_KEY = "seen_nearby";
    private static final int REQUEST_RESOLVE_ERROR = 1;

    @Getter
    @Setter
    private boolean resolvingError = false;

    @Getter
    private GoogleApiClient client;

    @Getter
    private Message message;

    @Getter
    private MessageListener messageListener = new MessageListener() {
        @Override
        public void onFound(Message message) {
            TransferActivity.this.onFound(message);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPrefs.getBoolean(SEEN_NEARBY_KEY, false)) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.initial_nearby_information)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, this)
                    .show();

            sharedPrefs.edit().putBoolean(SEEN_NEARBY_KEY, true).apply();
        } else {
            initializeClient();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        unpublish(client, message);
        unsubscribe(client, messageListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        initializeClient();
    }

    private void initializeClient() {
        message = createMessage();
        client = createClient();
        client.connect();
    }

    @VisibleForTesting
    protected GoogleApiClient createClient() {
        return new GoogleApiClient.Builder(this)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private Message createMessage() {
        String strMsg = "This is NearbyPubSub.";
        return new Message(strMsg.getBytes());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        publish(client, message);
        subscribe(client, messageListener);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Publishes a message for other nearby users to detect.
     */
    public void publish(GoogleApiClient client, Message message) {
        if (client != null) {
            Nearby.Messages.publish(client, message, PublishOptions.DEFAULT)
                    .setResultCallback(this);
        }
    }

    /**
     * Subscribes to find other nearby users who wish to send settings to me.
     */
    public void subscribe(GoogleApiClient client, MessageListener listener) {
        if (client != null) {
            Nearby.Messages.subscribe(client, listener, SubscribeOptions.DEFAULT)
                    .setResultCallback(this);
        }
    }

    /**
     * Unpublishes my message so that no one can detect it anymore.
     */
    public void unpublish(GoogleApiClient client, Message message) {
        if (client != null) {
            Nearby.Messages.unpublish(client, message);
        }
    }

    /**
     * Unsubscribes from listening for message.
     */
    public void unsubscribe(GoogleApiClient client, MessageListener listener) {
        if (client != null) {
            Nearby.Messages.unsubscribe(client, listener);
        }
    }

    /**
     * Handle the message when it is found from another device.
     *
     * @param message the message that was found.
     */
    public void onFound(Message message) {
        Toast.makeText(TransferActivity.this, "message found: " + new String(message.getContent()),
                Toast.LENGTH_SHORT).show();
    }

    @VisibleForTesting
    protected void handleUnsuccessfulNearbyResult(Status status) {
        Log.v(TAG, "Processing error, status = " + status);

        if (resolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (status.hasResolution()) {
            try {
                resolvingError = true;
                status.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (Exception e) {
                resolvingError = false;
                Log.v(TAG, "Failed to resolve error status.", e);
            }
        } else {
            if (status.getStatusCode() == CommonStatusCodes.NETWORK_ERROR) {
                Toast.makeText(this,
                        "No connectivity, cannot proceed. Fix in 'Settings' and try again.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Unsuccessful: " + status.getStatusMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.v(TAG, "successfully published or subscribed");
        } else {
            Log.v(TAG, "failed to publish or subscribe");
            // Check whether consent was given;
            // if not, prompt the user for consent.
            handleUnsuccessfulNearbyResult(status);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RESOLVE_ERROR) {
            // User was presented with the Nearby opt-in dialog and pressed "Allow".
            resolvingError = false;

            if (resultCode == Activity.RESULT_OK) {
                publish(client, message);
                subscribe(client, messageListener);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            } else {
                Toast.makeText(this, "Failed to resolve error with code " + resultCode,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
