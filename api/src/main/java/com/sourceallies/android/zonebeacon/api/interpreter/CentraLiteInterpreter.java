package com.sourceallies.android.zonebeacon.api.interpreter;

import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.model.Command;

/**
 * Interpreter for handling creating commands for a CentraLite control unit and receiving and
 * processing the results.
 */
public class CentraLiteInterpreter implements Interpreter {

    @Override
    public String getExecutable(Command command, Executor.LoadStatus loadStatus) {
        String zeros = "";

        if (command.getNumber() < 100) {
            zeros += "0";
        }

        if (command.getNumber() < 10) {
            zeros += "0";
        }

        String base;
        if (loadStatus == Executor.LoadStatus.OFF) { // we want to turn the light on
            base = command.getCommandType().getBaseSerialOnCode();
        } else { // we want to turn the lights off
            base = command.getCommandType().getBaseSerialOffCode();
        }

        String commandString;
        if (command.getControllerNumber() != null) {
            commandString = base + command.getControllerNumber() + zeros + command.getNumber();
        } else {
            commandString =  base + zeros + command.getNumber();
        }

        return commandString;
    }

    @Override
    public String processResponse(String response) {
        // TODO
        return response;
    }

}
