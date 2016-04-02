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

package com.sourceallies.android.zonebeacon.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;
import com.sourceallies.android.zonebeacon.api.executor.Executor;
import com.sourceallies.android.zonebeacon.data.OnOffButton;
import com.sourceallies.android.zonebeacon.data.OnOffZone;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Zone;
import com.sourceallies.android.zonebeacon.util.OnOffStatusUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MainAdapterTest extends ZoneBeaconSuite {

    private static final String ZONES_TITLE = "Zones";
    private static final String BUTTONS_TITLE = "Buttons";

    @Mock
    Activity context;
    @Mock
    LayoutInflater inflater;
    @Mock
    MainAdapter.ViewHolder viewHolder;
    @Mock
    View view;
    @Mock
    TextView title;
    @Mock
    TextView fabSpacer;
    @Mock
    SwitchCompat buttonSwitch;
    @Mock
    ViewGroup parent;
    @Mock
    Gateway gateway;
    @Mock
    OnOffStatusUtil onOffStatusUtil;
    @Mock
    Executor executor;

    MainAdapter adapter;

    @Before
    public void setUp() {
        Mockito.when(context.getLayoutInflater()).thenReturn(inflater);
        Mockito.doReturn(view).when(inflater).inflate(Mockito.anyInt(), Mockito.any(ViewGroup.class), Mockito.eq(false));
        Mockito.doReturn(title).when(view).findViewById(R.id.title);
        Mockito.doReturn(fabSpacer).when(view).findViewById(R.id.fab_spacer);
        Mockito.doReturn(View.VISIBLE).when(fabSpacer).getVisibility();

        Mockito.when(context.getString(R.string.zones)).thenReturn(ZONES_TITLE);
        Mockito.when(context.getString(R.string.buttons)).thenReturn(BUTTONS_TITLE);

        Mockito.when(gateway.getIpAddress()).thenReturn("192.168.1.150");
        Mockito.when(gateway.getPortNumber()).thenReturn(11000);

        Mockito.doNothing().when(executor).execute(Mockito.any(Gateway.class));
        Mockito.doNothing().when(title).setText(Mockito.anyString());
        viewHolder.title = title;
        viewHolder.buttonSwitch = buttonSwitch;
        viewHolder.fabSpacer = fabSpacer;

        adapter = Mockito.spy(new MainAdapter(
                context,
                gateway
        ));

        Mockito.when(adapter.getOnOffStatusUtil(Mockito.anyList(), Mockito.anyList(), Mockito.anyMap()))
                .thenReturn(onOffStatusUtil);
    }

    @Test
    public void test_sections_two() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        assertEquals(2, adapter.getSectionCount());
    }

    @Test
    public void test_sections_one_zone() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(0)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(0),
                getMap()
        );

        assertEquals(1, adapter.getSectionCount());
    }

    @Test
    public void test_sections_one_button() {
        Mockito.doReturn(getOnOffZoneList(0)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(0),
                getButtonList(1),
                getMap()
        );

        assertEquals(1, adapter.getSectionCount());
    }

    @Test
    public void test_sections_zero() {
        Mockito.doReturn(getOnOffZoneList(0)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(0)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(0),
                getButtonList(0),
                getMap()
        );

        assertEquals(0, adapter.getSectionCount());
    }

    @Test
    public void test_items_sectionZero_zones() {
        Mockito.doReturn(getOnOffZoneList(2)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(2),
                getButtonList(1),
                getMap()
        );

        assertEquals(2, adapter.getItemCount(0));
    }

    @Test
    public void test_items_sectionZero_buttons() {
        Mockito.doReturn(getOnOffZoneList(0)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(2)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(0),
                getButtonList(2),
                getMap()
        );

        assertEquals(2, adapter.getItemCount(0));
    }

    @Test
    public void test_items_sectionOne() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        assertEquals(1, adapter.getItemCount(1));
    }

    @Test
    public void test_items_sectionOne_zeroButtons() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(0)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(0),
                getMap()
        );

        assertEquals(0, adapter.getItemCount(1));
    }

    @Test
    public void test_bindHeader_zone() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        adapter.onBindHeaderViewHolder(viewHolder, 0);
        Mockito.verify(title).setText(ZONES_TITLE);
    }

    @Test
    public void test_bindHeader_button() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        adapter.onBindHeaderViewHolder(viewHolder, 1);
        Mockito.verify(title).setText(BUTTONS_TITLE);
    }

    @Test
    public void test_bindView_zone() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        adapter.onBindViewHolder(viewHolder, 0, 0, -1);
        Mockito.verify(title).setText("Test Zone 0");
    }

    @Test
    public void test_bindView_button() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        adapter.onBindViewHolder(viewHolder, 1, 0, -1);
        Mockito.verify(title).setText("Test Button 0");
    }

    @Test
    public void test_create_header() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        adapter.onCreateViewHolder(parent, -2); // View type header
        Mockito.verify(inflater)
                .inflate(Mockito.eq(R.layout.adapter_item_button_zone_header),
                        Mockito.any(ViewGroup.class),
                        Mockito.anyBoolean());
    }

    @Test
    public void test_create_view() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        MainAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, -1); // View type item
        Mockito.verify(inflater)
                .inflate(Mockito.eq(R.layout.adapter_item_button_zone),
                        Mockito.any(ViewGroup.class),
                        Mockito.anyBoolean());
    }

    @Test
    public void test_getStatus() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        Mockito.when(buttonSwitch.isChecked()).thenReturn(true);
        assertEquals(Executor.LoadStatus.ON, adapter.getStatus(buttonSwitch));

        Mockito.when(buttonSwitch.isChecked()).thenReturn(false);
        assertEquals(Executor.LoadStatus.OFF, adapter.getStatus(buttonSwitch));
    }

    @Test
    public void test_clickListener() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        adapter.executor = executor;

        adapter.getClickListener(buttonSwitch, 0, 0).onClick(buttonSwitch);
        Mockito.verify(buttonSwitch).setChecked(true);

        Mockito.doReturn(true).when(buttonSwitch).isChecked();
        adapter.getClickListener(buttonSwitch, 1, 0).onClick(buttonSwitch);
        Mockito.verify(buttonSwitch).setChecked(false);
    }

    @Test
    public void test_itemClick() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        adapter.setItemClick(view, buttonSwitch, 0, 0);
        Mockito.verify(view).setOnClickListener(Mockito.any(View.OnClickListener.class));
    }

    @Test
    public void test_itemClick_header() {
        Mockito.doReturn(getOnOffZoneList(1)).when(onOffStatusUtil).getOnOffZones();
        Mockito.doReturn(getOnOffButtonList(1)).when(onOffStatusUtil).getOnOffButtons();

        adapter.loadOnOffStatuses(
                getZoneList(1),
                getButtonList(1),
                getMap()
        );

        adapter.setItemClick(view, null, 0, 0);
        Mockito.verifyZeroInteractions(view);
    }

    private List<Zone> getZoneList(int count) {
        List<Zone> zones = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Zone zone = new Zone();
            zone.setName("Test Zone " + i);
            zone.setButtons(getButtonList(1));
            zones.add(zone);
        }

        return zones;
    }

    private List<Button> getButtonList(int count) {
        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Button button = new Button();
            button.setName("Test Button " + i);
            buttons.add(button);
        }

        return buttons;
    }

    private List<OnOffZone> getOnOffZoneList(int count) {
        List<OnOffZone> zones = new ArrayList();

        for (int i = 0; i < count; i++) {
            Zone zone = new Zone();
            zone.setName("Test Zone " + i);
            zone.setButtons(getButtonList(1));
            zones.add(new OnOffZone(zone, Executor.LoadStatus.OFF));
        }

        return zones;
    }

    private List<OnOffButton> getOnOffButtonList(int count) {
        List<OnOffButton> buttons = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Button button = new Button();
            button.setName("Test Button " + i);
            buttons.add(new OnOffButton(button, Executor.LoadStatus.OFF));
        }

        return buttons;
    }

    private Map<Integer, Executor.LoadStatus> getMap() {
        return new HashMap();
    }
}