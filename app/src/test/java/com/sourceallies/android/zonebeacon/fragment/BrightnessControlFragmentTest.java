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

package com.sourceallies.android.zonebeacon.fragment;

import android.widget.SeekBar;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.CommandType;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BrightnessControlFragmentTest extends ZoneBeaconRobolectricSuite {

    private BrightnessControlFragment fragment;

    @Mock
    private SeekBar seekBar;

    private Button button;
    private Zone zone;
    private List<CommandType> types;

    @Before
    public void setUp() {
        fragment = Mockito.spy(BrightnessControlFragment.newInstance(-1, true, -1));
        startDialogFragment(fragment);

        types = new ArrayList<>();
        CommandType singeMCP = new CommandType();
        singeMCP.setName("Single MCP");
        CommandType multiMCP = new CommandType();
        multiMCP.setName("Multi MCP");

        types.add(singeMCP);
        types.add(multiMCP);

        button = new Button();
        button.setName("test button");
        List<Command> commands = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Command command = new Command();
            command.setName("test " + i);

            if (i != 0)
                command.setControllerNumber(i);

            commands.add(command);
        }

        button.setCommands(commands);

        zone = new Zone();
        zone.setName("test zone");
        List<Button> buttons = new ArrayList<>();

        for (int j = 0; j < 3; j++) {
            Button button = new Button();
            button.setName("test button");
            commands = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                Command command = new Command();
                command.setName("test " + i);

                if (i != 0)
                    command.setControllerNumber(i);

                commands.add(command);
            }

            button.setCommands(commands);
            buttons.add(button);
        }

        zone.setButtons(buttons);

        fragment.setButton(button);
        fragment.setCommandTypes(types);
    }

    @Test
    public void test_createNonZone() {
        fragment = Mockito.spy(BrightnessControlFragment.newInstance(-1, false, -1));
        startDialogFragment(fragment);

        assertNotNull(fragment);
    }

    @Test
    public void test_findZone() {
        List<Zone> zones = new ArrayList<>();
        Zone z1 = new Zone();
        z1.setId(1);
        Zone z2 = new Zone();
        z2.setId(2);

        zones.add(z1);
        zones.add(z2);

        fragment.setZone(zones, 3);
        assertNull(fragment.getZone());

        fragment.setZone(zones, 1);
        assertEquals(z1, fragment.getZone());
    }

    @Test
    public void test_findButton() {
        fragment.setButton(null);

        List<Button> buttons = new ArrayList<>();
        Button b1 = new Button();
        b1.setId(1);
        Button b2 = new Button();
        b2.setId(2);

        buttons.add(b1);
        buttons.add(b2);

        fragment.setButton(buttons, 3);
        assertNull(fragment.getButton());

        fragment.setButton(buttons, 1);
        assertEquals(b1, fragment.getButton());
    }

    @Test
    public void test_percentChangeWithSlider() {
        setProgress(fragment.getDimmerBar(), 50);
        assertTrue(fragment.getPercent().getText().toString().equals("50%"));
    }

    @Test
    public void test_percentChangeWithSlider_lessThanOne() {
        setProgress(fragment.getDimmerBar(), 0);

        assertTrue(fragment.getPercent().getText().toString().equals("0%"));
        verify(fragment).addCommands(any(Button.class), eq(1));
    }

    @Test
    public void test_percentChangeWithSlider_moreThanNinetyNine() {
        fragment.setButton(null);
        fragment.setZone(zone);
        fragment.onProgressChanged(null, 100, false);

        assertTrue(fragment.getPercent().getText().toString().equals("100%"));
        verify(fragment, Mockito.times(3)).addCommands(any(Button.class), eq(99));
    }

    @Test
    public void test_alphaChangeWithSlider() {
        setProgress(fragment.getDimmerBar(), 50);
        assertTrue(fragment.getBulbImg().getAlpha() == 0.5f);
    }

    @Test
    public void test_startTrackingTouch() {
        fragment.onStartTrackingTouch(seekBar);
        verifyNoMoreInteractions(seekBar);
    }

    @Test
    public void test_stopTrackingTouch() {
        fragment.onStopTrackingTouch(seekBar);
        verifyNoMoreInteractions(seekBar);
    }

    private void setProgress(SeekBar slider, int progress) {
        slider.setProgress(progress);
    }
}