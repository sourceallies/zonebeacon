/*
 * Copyright (C) 2016 Source Allies
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.inject.Inject;
import com.sourceallies.android.zonebeacon.R;

import lombok.Getter;
import lombok.Setter;

/**
 * Main activity that the user will be interacting with when using the app.
 */
public class MainActivity extends RoboAppCompatActivity {

    private static final int RESULT_INTRO = 1;

    @Inject @Setter
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startIntro();
    }

    /**
     * Start the intro activity if necessary
     * @return whether or not the activity was started
     */
    public boolean startIntro() {
        if (!sharedPrefs.getBoolean(getString(R.string.pref_intro), false)) {
            startActivityForResult(new Intent(this, IntroActivity.class), RESULT_INTRO);
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // we want to restart the main activity after setup to account for new information that gets entered.
        if (requestCode == RESULT_INTRO && resultCode == RESULT_OK) {
            sharedPrefs.edit().putBoolean(getString(R.string.pref_intro), true).commit();
            recreate();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
