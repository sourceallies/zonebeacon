package com.sourceallies.android.zonebeacon.api;

import com.sourceallies.android.zonebeacon.data.model.Command;

/**
 * Provides an interface so that we can perform actions on the response string.
 */
public interface CommandCallback {
    void onResponse(Command command, String text);
}
