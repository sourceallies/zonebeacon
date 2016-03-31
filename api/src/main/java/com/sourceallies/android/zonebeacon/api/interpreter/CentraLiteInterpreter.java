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

import java.util.List;

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
    public String getQueryActiveLoadsCommandString() {
        return "^G";
    }

    @Override
    public List<Integer> processActiveLoadsResponse(String systemResponse) {
        // TODO
        return null;
    }

    protected String addZeros(String val, int numDigits) {
        while (val.length() < numDigits) {
            val = "0" + val;
        }

        return val;
    }
}
