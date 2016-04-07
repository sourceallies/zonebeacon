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

package com.sourceallies.android.zonebeacon.api.interpreter;

import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.model.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interpreter for handling creating commands for a CentraLite control unit and receiving and
 * processing the results.
 */
public class CentraLiteInterpreter extends Interpreter {

    @Override
    public String getExecutable(Command command, Executor.LoadStatus status) {
        return getExecutable(command, null, status);
    }

    @Override
    public String getExecutable(Command command, Integer brightnessLevel, Executor.LoadStatus status) {
        String base;
        if (status == Executor.LoadStatus.OFF) { // we want to turn the light on
            base = command.getCommandType().getBaseSerialOnCode();
        } else { // we want to turn the lights off
            base = command.getCommandType().getBaseSerialOffCode();
        }

        return base.replace("%nnn", addZeros(command.getNumber() + "", 3))
                .replace("%s", addZeros(command.getControllerNumber() + "", 1))
                .replace("%ll", brightnessLevel + ""); // TODO: apply brightness level here
    }

    @Override
    public String processResponse(String response) {
        // TODO
        return response;
    }

    @Override
    public String getQueryActiveLoadsCommandString(int controllerNumber) {
        if (controllerNumber == 0) {
            return "^G";
        } else {
            return "^g" + controllerNumber;
        }
    }

    @Override
    public Map<Integer, Executor.LoadStatus> processActiveLoadsResponse(String statusString) {
        int length = statusString.length();
        List<String> bitList = new ArrayList();
        Map<Integer, Executor.LoadStatus> loadStatusList = new HashMap();
        int loadIndex = 0;
        int currentIndex = 0;

        if (length % 48 != 0) {
            throw new RuntimeException("Length of the status string is incorrect. Must be length % 48 = 0");
        }

        if (!statusString.matches("[0-9a-fA-F]+")) {
            throw new RuntimeException("Status string has more than just hex values.");
        }

        // get the binary of the each character and make a list
        while (currentIndex != length) {
            String binary = convertToBinary(statusString.substring(currentIndex, currentIndex + 1));
            bitList.add(binary);
            currentIndex++;
        }

        // Loop through the list and get items in two chunks and identify them with right device number
        int bitListIndex = 0;
        while (bitListIndex < bitList.size()) {
            String combinedString = bitList.get(bitListIndex) + bitList.get(bitListIndex + 1);
            char[] charArray = combinedString.toCharArray();
            int charArrayIndex = charArray.length - 1;
            while (charArrayIndex >= 0) {
                loadStatusList.put(
                        loadIndex + 1,                          // load number
                        charArray[charArrayIndex] == '1' ?      // status
                                Executor.LoadStatus.ON :
                                Executor.LoadStatus.OFF
                );
                loadIndex++;
                charArrayIndex--;
            }

            bitListIndex = bitListIndex + 2;
        }

        return loadStatusList;
    }

    protected String convertToBinary(String s) {
        int num = (Integer.parseInt(s, 16));
        return addZeros(Integer.toBinaryString(num), 4);
    }

    protected String addZeros(String val, int numDigits) {
        while (val.length() < numDigits) {
            val = "0" + val;
        }

        return val;
    }
}
