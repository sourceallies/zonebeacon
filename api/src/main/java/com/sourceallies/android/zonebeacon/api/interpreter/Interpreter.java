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
    String getExecutable(Command command, Executor.LoadStatus status);

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
