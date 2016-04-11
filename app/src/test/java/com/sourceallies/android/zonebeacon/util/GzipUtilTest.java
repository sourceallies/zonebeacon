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

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GzipUtilTest extends ZoneBeaconSuite {

    @Before
    public void setUp() {
        GzipUtil util = new GzipUtil();
    }

    @Test
    public void test_compress_decompress() throws Exception {
        assertEquals(TEST_JSON, GzipUtil.ungzip(GzipUtil.gzip(TEST_JSON)));
    }

    @Test
    public void test_compressesSmallerThanOriginal() throws Exception {
        byte[] gzip = GzipUtil.gzip(TEST_JSON);
        System.out.println(gzip.length);
        assertTrue(gzip.length < TEST_JSON_SIZE);
    }

    private static final int TEST_JSON_SIZE = 1463;
    public static final String TEST_JSON =
            "{\n" +
                    "   \"system_type\":[\n" +
                    "      \"1,CentraLite Elegance,1.0\"\n" +
                    "   ],\n" +
                    "   \"command_type\":[\n" +
                    "      \"1,Single MCP - Load\\/Relay,^A%nnn,^B%nnn,1,0,1\",\n" +
                    "      \"2,Single MCP - Switch,^S%nnn,^S%nnn,1,0,1\",\n" +
                    "      \"3,Single MCP - Scene,^C%nnn,^D%nnn,1,0,1\",\n" +
                    "      \"4,Multi MCP - Load\\/Relay,^a%s%nnn,^b%s%nnn,1,1,1\",\n" +
                    "      \"5,Multi MCP - Switch,^s%s%nnn,^s%s%nnn,1,1,1\",\n" +
                    "      \"6,Multi MCP - Scene,^c%s%nnn,^d%s%nnn,1,1,1\",\n" +
                    "      \"7,Single MCP - Brightness,^E%nnn%ll00,^E%nnn0000,1,0,0\",\n" +
                    "      \"8,Multi MCP - Brightness,^e%s%nnn%ll00,^e%s%nnn0000,1,1,0\"\n" +
                    "   ],\n" +
                    "   \"gateway\":[\n" +
                    "      \"1,Gateway 1,192.168.1.150,11000,101\",\n" +
                    "      \"2,Gateway 2,192.168.1.100,11000,101\"\n" +
                    "   ],\n" +
                    "   \"command\":[\n" +
                    "      \"1,command 1,1,1,1,1\",\n" +
                    "      \"2,command 2,1,1,1,1\",\n" +
                    "      \"3,command 3,1,1,1,1\",\n" +
                    "      \"4,command 4,1,1,1,null\",\n" +
                    "      \"5,command 5,1,1,1,1\",\n" +
                    "      \"6,command 6,1,1,1,1\",\n" +
                    "      \"7,command 7,1,1,1,1\",\n" +
                    "      \"8,command 8,1,1,1,1\",\n" +
                    "      \"9,command 9,2,1,1,1\",\n" +
                    "      \"10,command 10,2,1,1,1\",\n" +
                    "      \"11,command 11,2,1,1,1\",\n" +
                    "      \"12,command 12,2,1,1,1\",\n" +
                    "      \"13,command 13,2,1,1,1\",\n" +
                    "      \"14,command 14,2,1,1,1\",\n" +
                    "      \"15,command 15,2,1,1,1\",\n" +
                    "      \"16,command 16,2,1,1,1\"\n" +
                    "   ],\n" +
                    "   \"button\":[\n" +
                    "      \"1,button 1\",\n" +
                    "      \"2,button 2\",\n" +
                    "      \"3,button 3\",\n" +
                    "      \"4,button 4\",\n" +
                    "      \"5,button 5\",\n" +
                    "      \"6,button 6\",\n" +
                    "      \"7,button 7\",\n" +
                    "      \"8,button 8\",\n" +
                    "      \"9,button 9\",\n" +
                    "      \"10,button 10\"\n" +
                    "   ],\n" +
                    "   \"button_command_link\":[\n" +
                    "      \"1,1,1\",\n" +
                    "      \"2,1,2\",\n" +
                    "      \"3,2,3\",\n" +
                    "      \"4,3,4\",\n" +
                    "      \"5,3,5\",\n" +
                    "      \"6,3,6\",\n" +
                    "      \"7,4,7\",\n" +
                    "      \"8,5,8\",\n" +
                    "      \"9,5,2\",\n" +
                    "      \"10,6,9\",\n" +
                    "      \"11,6,10\",\n" +
                    "      \"12,6,11\",\n" +
                    "      \"13,7,12\",\n" +
                    "      \"14,8,12\",\n" +
                    "      \"15,8,13\",\n" +
                    "      \"16,9,14\",\n" +
                    "      \"17,10,15\",\n" +
                    "      \"18,10,16\"\n" +
                    "   ],\n" +
                    "   \"zone\":[\n" +
                    "      \"1,zone 1\",\n" +
                    "      \"2,zone 2\",\n" +
                    "      \"3,zone 3\",\n" +
                    "      \"4,zone 4\",\n" +
                    "      \"5,zone 5\"\n" +
                    "   ],\n" +
                    "   \"zone_button_link\":[\n" +
                    "      \"1,1,1\",\n" +
                    "      \"2,1,2\",\n" +
                    "      \"3,1,3\",\n" +
                    "      \"4,2,3\",\n" +
                    "      \"5,2,4\",\n" +
                    "      \"6,3,6\",\n" +
                    "      \"7,3,7\",\n" +
                    "      \"8,3,8\",\n" +
                    "      \"9,3,9\",\n" +
                    "      \"10,4,10\",\n" +
                    "      \"11,4,9\",\n" +
                    "      \"12,5,6\",\n" +
                    "      \"13,5,7\",\n" +
                    "      \"14,5,10\"\n" +
                    "   ]\n" +
                    "}";

}