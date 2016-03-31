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

package com.sourceallies.android.zonebeacon.api.executor;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;
import com.sourceallies.android.zonebeacon.api.QueryLoadsCallback;
import com.sourceallies.android.zonebeacon.api.interpreter.CentraLiteInterpreter;
import com.sourceallies.android.zonebeacon.api.interpreter.Interpreter;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExecutorTest extends ZoneBeaconSuite {

    @Test
    public void test_getElegance() {
        Executor e = Executor.createForGateway(getGatewayForSystem(1));
        assertTrue(e instanceof SerialExecutor);
        assertTrue(e.getInterpreter() instanceof CentraLiteInterpreter);
    }

    @Test
    public void test_getDefault() {
        Executor e = Executor.createForGateway(getGatewayForSystem(-1));
        assertTrue(e instanceof SerialExecutor);
        assertTrue(e.getInterpreter() instanceof CentraLiteInterpreter);
    }

    @Test
    public void test_queryActiveLoads() {
        Gateway gateway = getGatewayForSystem(1);
        Executor e = Mockito.spy(Executor.createForGateway(gateway));
        Mockito.doNothing().when(e).execute(gateway);

        QueryLoadsCallback callback = Mockito.mock(QueryLoadsCallback.class);
        e.queryActiveLoads(gateway, callback);

        Mockito.verify(e).addCommand(Mockito.any(Command.class), Mockito.eq(Executor.LoadStatus.OFF));
        Mockito.verify(e).execute(gateway);
    }

    @Test
    public void test_queryCommandCallback() {
        QueryLoadsCallback callback = Mockito.mock(QueryLoadsCallback.class);
        Interpreter interpreter = Mockito.mock(Interpreter.class);

        Mockito.when(interpreter.processActiveLoadsResponse(Mockito.anyString()))
                .thenReturn(new ArrayList<Integer>());

        Gateway gateway = getGatewayForSystem(1);
        Executor e = Mockito.spy(Executor.createForGateway(gateway));

        e.getCommandCallbackForQueryingLoads(interpreter, callback)
                .onResponse(null, "test text");

        Mockito.verify(callback).onResponse(Mockito.anyList());
    }

    private Gateway getGatewayForSystem(int systemTypeId) {
        Gateway gateway = new Gateway();
        gateway.setSystemTypeId(systemTypeId);

        return gateway;
    }

}