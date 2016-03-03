package com.sourceallies.android.zonebeacon.api.executor;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;
import com.sourceallies.android.zonebeacon.api.interpreter.CentraLiteInterpreter;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExecutorTest extends ZoneBeaconSuite {

    @Test
    public void test_getElegance() {
        Executor e = Executor.createForGateway(getGatewayForSystem(1));
        assertTrue(e instanceof SerialExecutor);
        assertTrue(e.getInterpreter() instanceof CentraLiteInterpreter);
    }

    private Gateway getGatewayForSystem(int systemTypeId) {
        Gateway gateway = new Gateway();
        gateway.setSystemTypeId(systemTypeId);

        return gateway;
    }

}