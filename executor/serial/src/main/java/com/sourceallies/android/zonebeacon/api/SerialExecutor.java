package com.sourceallies.android.zonebeacon.api;

import com.sourceallies.android.zonebeacon.data.model.Gateway;

/**
 * Executor that sends commands serially to a control unit. This is required for CentraLite systems
 * and perhaps others as well.
 */
public class SerialExecutor extends Executor {

    public SerialExecutor(Interpreter interpreter) {
        super(interpreter);
    }

    @Override
    boolean pingGateway(Gateway gateway) {
        return false;
    }

    @Override
    void connect(Gateway gateway) {

    }

    @Override
    boolean isConnected() {
        return false;
    }

    @Override
    void disconnect() {

    }

    @Override
    String send(String command) {
        return null;
    }

}
