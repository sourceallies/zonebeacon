package com.sourceallies.android.zonebeacon.api.interpreter;

import com.sourceallies.android.zonebeacon.data.model.Command;

/**
 * Interpreter for handling creating commands for a CentraLite control unit and receiving and
 * processing the results.
 */
public class CentraLiteInterpreter extends Interpreter {

    @Override
    public String getExecutable(Command command) {
        String zeros = "";

        if (command.getNumber() < 100) {
            zeros += "0";
        }

        if (command.getNumber() < 10) {
            zeros += "0";
        }

        return "^a1" + zeros + command.getNumber();
    }

    @Override
    public String processResponse(String response) {
        // TODO
        return response;
    }

}
