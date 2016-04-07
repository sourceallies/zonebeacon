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


import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to used to get the different controller numbers used in multiple mcp systems
 */
public class ControllerUtil {

    /**
     * Get a list of the different controller numbers that are used in commands on a multiple MCP system
     *
     * @param commands commands on the current gateway
     * @return A list of controller numbers used on the multiple MCP system
     */
    private List<Integer> getControllerNumbersByCommands(List<Command> commands) {
        List<Integer> controllerNumbers = new ArrayList();

        for (Command command : commands) {
            Integer controllerNumber = command.getControllerNumber();

            if (command.getCommandType().isActivateControllerSelection()) {
                if (!controllerNumbers.contains(controllerNumber)) {
                    controllerNumbers.add(controllerNumber);
                }
            }
        }

        if (controllerNumbers.size() == 0) {
            // we will add the zero to designate that we want it to query without multi-mcp support.
            controllerNumbers.add(0);
        }

        return controllerNumbers;
    }

    /**
     * Get a list of the different controller numbers that are used in commands on a multiple MCP system
     *
     * @param buttons commands on the current gateway
     * @return A list of controller numbers used on the multiple MCP system
     */
    public List<Integer> getControllerNumbersByButtons(List<Button> buttons) {
        List<Command> commands = new ArrayList();

        for (Button button : buttons) {
            commands.addAll(button.getCommands());
        }

        return getControllerNumbersByCommands(commands);
    }

    /**
     * Get a list of the different controller numbers that are used in commands on a multiple MCP system
     *
     * @param zones commands on the current gateway
     * @return A list of controller numbers used on the multiple MCP system
     */
    public List<Integer> getControllerNumbersByZones(List<Zone> zones) {
        List<Command> commands = new ArrayList();

        for (Zone zone : zones) {
            for (Button button : zone.getButtons()) {
                commands.addAll(button.getCommands());
            }
        }

        return getControllerNumbersByCommands(commands);
    }
}
