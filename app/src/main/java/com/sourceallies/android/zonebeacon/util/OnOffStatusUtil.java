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

package com.sourceallies.android.zonebeacon.util;

import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.StatefulButton;
import com.sourceallies.android.zonebeacon.data.StatefulZone;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to convert the map of load statuses into lists of individual buttons
 * and zones that should be turned on.
 * </p>
 * Buttons are turned on if all of the loads (commands) attached to them are turned on
 * </p>
 * Zones are turned on if all the buttons attached to them are turned on
 */
public class OnOffStatusUtil {

    private List<Button> buttons;
    private List<Zone> zones;
    private Map<Integer, Executor.LoadStatus> loadStatusMap;

    protected List<StatefulButton> statefulButtons = null;
    protected List<StatefulZone> statefulZones = null;

    /**
     * Create an object that can get the on off status of the individual buttons and zones
     *
     * @param buttons list of buttons we are looking to get the load status of
     * @param zones list of zones that we are looking to get the load status of
     * @param loadStatusMap map that contains the load number as the key and its On Off status as the value
     */
    public OnOffStatusUtil(List<Button> buttons, List<Zone> zones, Map<Integer, Executor.LoadStatus> loadStatusMap) {
        this.buttons = buttons;
        this.zones = zones;
        this.loadStatusMap = loadStatusMap == null ? new HashMap() : loadStatusMap;
    }

    /**
     * Invalidate the list of buttons and zones so that it is re-queried when the OnOffStatusUtil#getStatefulButtons()
     * and OnOffStatusUtil#getStatefulZones()
     */
    public void invalidate() {
        this.statefulButtons = null;
        this.statefulZones = null;
    }

    /**
     * Converts the list of buttons to a list that contains the buttons as well as their statuses
     *
     * @return list of buttons along with their current load statuses
     */
    public List<StatefulButton> getStatefulButtons() {
        if (statefulButtons != null) {
            return statefulButtons;
        } else {
            statefulButtons = new ArrayList();

            for (Button button : buttons) {
                // store whether or not one of the loads corresponding to a button is turned off
                // if even one load is turned off, then the button is turned off.

                boolean loadOff = false;
                for (Command command : button.getCommands()) {
                    Executor.LoadStatus status = loadStatusMap.get(command.getNumber());
                    if (status == null || status == Executor.LoadStatus.OFF) {
                        loadOff = true;
                        break;
                    }
                }

                statefulButtons.add(
                        new StatefulButton(
                                button,
                                loadOff ? Executor.LoadStatus.OFF : Executor.LoadStatus.ON
                ));
            }

            return statefulButtons;
        }
    }

    /**
     * Converts the list of zones to a list that contains the zones as well as their statuses
     *
     * @return list of zones along with their current load statuses
     */
    public List<StatefulZone> getStatefulZones() {
        if (statefulZones != null) {
            return statefulZones;
        } else {
            statefulZones = new ArrayList();

            for (Zone zone : zones) {
                // store whether or not one of the loads corresponding to a button is turned off
                // if even one load is turned off, then the button is turned off.

                boolean loadOff = false;
                for (Button button : zone.getButtons()) {
                    for (Command command : button.getCommands()) {
                        Executor.LoadStatus status = loadStatusMap.get(command.getNumber());
                        if (status == null || status == Executor.LoadStatus.OFF) {
                            loadOff = true;
                            break;
                        }
                    }

                    if (loadOff) {
                        break;
                    }
                }

                statefulZones.add(
                        new StatefulZone(
                                zone,
                                loadOff ? Executor.LoadStatus.OFF : Executor.LoadStatus.ON
                        ));
            }

            return statefulZones;
        }
    }
}
