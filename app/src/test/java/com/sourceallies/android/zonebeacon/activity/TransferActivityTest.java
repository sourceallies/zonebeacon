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
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

// NOTE: a lot of these tests are garbage, google play services isn't easy to unit test correctly.
//       someone should probably come in an improve these at a later date.
public class TransferActivityTest extends ZoneBeaconRobolectricSuite {

    private TransferActivity activity;

    @Mock
    private MenuItem menuItem;
    @Mock
    private GoogleApiClient client;
    @Mock
    private PendingIntent pendingIntent;

    @Before
    public void setUp() {
        activity = getActivity();
    }

    private TransferActivity getActivity() {
        TransferActivity activity = spy(Robolectric.buildActivity(TransferActivity.class).create().get());
        when(activity.createClient()).thenReturn(client);
        activity.onStart();
        return activity;
    }

    @Test
    public void test_notNull() {
        assertNotNull(activity);
    }

    @Test
    public void test_apiClientNull() {
        assertNull(activity.getClient());
        activity.onClick(null, 0); // click the ok button on the dialog
        assertNotNull(activity.getClient());
    }

    @Test
    public void test_apiClientNotNullOnSecondLaunch() {
        activity = getActivity();
        assertNotNull(activity.getClient());
    }

    @Test
    public void test_messageNotNullOnSecondLaunch() {
        activity = getActivity();
        assertNotNull(activity.getMessage());
    }

    @Test
    public void test_messageListenerInvokesOnFound() {
        Message message = new Message("test".getBytes());
        activity.getMessageListener().onFound(message);
    }

    @Test
    public void test_changedSeenNearbySetting() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
        assertTrue(sharedPrefs.getBoolean("seen_nearby", false));
    }

    @Test
    public void test_onHomeSelected() {
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        activity.onOptionsItemSelected(menuItem);
        assertTrue(activity.isFinishing());
    }

    @Test
    public void test_otherOptionSelected() {
        when(menuItem.getItemId()).thenReturn(1);
        activity.onOptionsItemSelected(menuItem);
        assertFalse(activity.isFinishing());
    }

    @Test
    public void test_onConnected() {
        activity.onConnected(null);
    }

    @Test
    public void test_onStop() {
        activity.onStop();
    }

    @Test
    public void test_onConnectionSuspended() {
        activity.onConnectionSuspended(0);
    }

    @Test
    public void test_onConnectionFailed() {
        activity.onConnectionFailed(null);
    }

    @Test
    public void test_publish() {
        try {
            activity.publish(getRealClient(), new Message("test".getBytes()));
        } catch (IllegalStateException e) {
            // GoogleApiClient is not connected yet.
        }
    }

    @Test
    public void test_subscribe() {
        try {
            activity.subscribe(getRealClient(), new MessageListener() {
                @Override
                public void onFound(Message message) {

                }
            });
        } catch (IllegalStateException e) {
            // GoogleApiClient is not connected yet.
        }
    }

    @Test
    public void test_unpublish() {
        try {
            activity.unpublish(getRealClient(), new Message("test".getBytes()));
        } catch (IllegalStateException e) {
            // GoogleApiClient is not connected yet.
        }
    }

    @Test
    public void test_unsubscribe() {
        try {
            activity.unsubscribe(getRealClient(), new MessageListener() {
                @Override
                public void onFound(Message message) {

                }
            });
        } catch (IllegalStateException e) {
            // GoogleApiClient is not connected yet.
        }
    }

    @Test
    public void test_handleUnsuccessful_alreadyResolving() {
        Status status = new Status(0);
        activity.setResolvingError(true);
        activity.handleUnsuccessfulNearbyResult(status);
    }

    @Test
    public void test_handleUnsuccessful_hasResolution() {
        Status status = new Status(0, "test", pendingIntent);
        activity.setResolvingError(false);
        activity.handleUnsuccessfulNearbyResult(status);
    }

    @Test
    public void test_handleUnsuccessful_networkError() {
        Status status = new Status(CommonStatusCodes.NETWORK_ERROR);
        activity.setResolvingError(false);
        activity.handleUnsuccessfulNearbyResult(status);
    }

    @Test
    public void test_handleUnsuccessful_otherError() {
        Status status = new Status(0);
        activity.setResolvingError(false);
        activity.handleUnsuccessfulNearbyResult(status);
    }

    @Test
    public void test_onResult_successful() {
        Status status = new Status(0);
        activity.onResult(status);
    }

    @Test
    public void test_onResult_unsuccessful() {
        Status status = new Status(1);
        activity.onResult(status);
    }

    @Test
    public void test_onActivityResult_successful() {
        activity.setResolvingError(true);
        activity.onActivityResult(1, Activity.RESULT_OK, null);
        assertFalse(activity.isResolvingError());
    }

    @Test
    public void test_onActivityResult_cancelled() {
        activity.setResolvingError(true);
        activity.onActivityResult(1, Activity.RESULT_CANCELED, null);
        assertFalse(activity.isResolvingError());
        assertTrue(activity.isFinishing());
    }

    @Test
    public void test_onActivityResult_other() {
        activity.setResolvingError(true);
        activity.onActivityResult(1, 1, null);
        assertFalse(activity.isResolvingError());
    }

    @Test
    public void test_onActivityResult_otherRequestCode() {
        activity.setResolvingError(true);
        activity.onActivityResult(2, Activity.RESULT_OK, null);
        assertTrue(activity.isResolvingError());
    }

    private GoogleApiClient getRealClient() {
        return new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .addConnectionCallbacks(activity)
                .addOnConnectionFailedListener(activity)
                .build();
    }

}