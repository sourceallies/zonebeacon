package com.sourceallies.android.zonebeacon.api.interpreter;

import com.sourceallies.android.zonebeacon.data.model.Command;

/**
 * Adheres to the strategy pattern. This interface can be implemented in various different ways.
 * Each control unit will need to interpret commands differently, so that logic is handled here.
 * Once implemented, that interpreter can be loaded into the app so that we can support different
 * control unit types easily.
 */
public interface Interpreter {

    /**
     * Receives a command that is stored in the database and turns it into an executable string
     * that can be sent to the gateway/control unit.
     *
     * @param command the command to execute.
     * @return a string representing the command for the control unit.
     */
    String getExecutable(Command command);

    /**
     * Processes the response provided by the control unit and hands it off back to the app.
     *
     * TODO I'm not sure what to return for this method, that will need to be decided.
     *
     * @param response the response from the control unit.
     * @return a processed response.
     */
    String processResponse(String response);

}
