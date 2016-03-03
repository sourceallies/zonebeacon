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
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Gateway;
import com.sourceallies.android.zonebeacon.data.model.Zone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

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
    SwitchCompat buttonSwitch;
    @Mock
    ViewGroup parent;
    @Mock
    Gateway gateway;
    @Mock
    Executor executor;

    @Before
    public void setUp() {
        Mockito.when(context.getLayoutInflater()).thenReturn(inflater);
        Mockito.doReturn(view).when(inflater).inflate(Mockito.anyInt(), Mockito.any(ViewGroup.class), Mockito.eq(false));
        Mockito.doReturn(title).when(view).findViewById(R.id.title);

        Mockito.when(context.getString(R.string.zones)).thenReturn(ZONES_TITLE);
        Mockito.when(context.getString(R.string.buttons)).thenReturn(BUTTONS_TITLE);

        Mockito.when(gateway.getIpAddress()).thenReturn("192.168.1.150");
        Mockito.when(gateway.getPortNumber()).thenReturn(11000);

        Mockito.doNothing().when(executor).execute(Mockito.any(Gateway.class));
        Mockito.doNothing().when(title).setText(Mockito.anyString());
        viewHolder.title = title;
    }

    @Test
    public void test_sections_two() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        assertEquals(2, adapter.getSectionCount());
    }

    @Test
    public void test_sections_one_zone() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(0));
        assertEquals(1, adapter.getSectionCount());
    }

    @Test
    public void test_sections_one_button() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(0), getButtonList(1));
        assertEquals(1, adapter.getSectionCount());
    }

    @Test
    public void test_sections_zero() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(0), getButtonList(0));
        assertEquals(0, adapter.getSectionCount());
    }

    @Test
    public void test_items_sectionZero_zones() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(2), getButtonList(1));
        assertEquals(2, adapter.getItemCount(0));
    }

    @Test
    public void test_items_sectionZero_buttons() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(0), getButtonList(2));
        assertEquals(2, adapter.getItemCount(0));
    }

    @Test
    public void test_items_sectionOne() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        assertEquals(1, adapter.getItemCount(1));
    }

    @Test
    public void test_items_sectionOne_zeroButtons() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(0));
        assertEquals(0, adapter.getItemCount(1));
    }

    @Test
    public void test_bindHeader_zone() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        adapter.onBindHeaderViewHolder(viewHolder, 0);

        Mockito.verify(title).setText(ZONES_TITLE);
    }

    @Test
    public void test_bindHeader_button() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        adapter.onBindHeaderViewHolder(viewHolder, 1);

        Mockito.verify(title).setText(BUTTONS_TITLE);
    }

    @Test
    public void test_bindView_zone() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        adapter.onBindViewHolder(viewHolder, 0, 0, -1);

        Mockito.verify(title).setText("Test Zone 0");
    }

    @Test
    public void test_bindView_button() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        adapter.onBindViewHolder(viewHolder, 1, 0, -1);

        Mockito.verify(title).setText("Test Button 0");
    }

    @Test
    public void test_create_header() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        adapter.onCreateViewHolder(parent, -2); // View type header

        Mockito.verify(inflater)
                .inflate(Mockito.eq(R.layout.adapter_item_button_zone_header),
                        Mockito.any(ViewGroup.class),
                        Mockito.anyBoolean());
    }

    @Test
    public void test_create_view() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        MainAdapter.ViewHolder holder = adapter.onCreateViewHolder(parent, -1); // View type item

        Mockito.verify(inflater)
                .inflate(Mockito.eq(R.layout.adapter_item_button_zone),
                        Mockito.any(ViewGroup.class),
                        Mockito.anyBoolean());
    }

    @Test
    public void test_getStatus() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));

        Mockito.when(buttonSwitch.isChecked()).thenReturn(true);
        assertEquals(Executor.LoadStatus.ON, adapter.getStatus(buttonSwitch));

        Mockito.when(buttonSwitch.isChecked()).thenReturn(false);
        assertEquals(Executor.LoadStatus.OFF, adapter.getStatus(buttonSwitch));
    }

    @Test
    public void test_clickListener() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        adapter.executor = executor;

        adapter.getClickListener(buttonSwitch, 0, 0).onClick(buttonSwitch);
        Mockito.verify(buttonSwitch).setChecked(true);

        Mockito.doReturn(true).when(buttonSwitch).isChecked();
        adapter.getClickListener(buttonSwitch, 1, 0).onClick(buttonSwitch);
        Mockito.verify(buttonSwitch).setChecked(false);
    }

    @Test
    public void test_itemClick() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
        adapter.setItemClick(view, buttonSwitch, 0, 0);

        Mockito.verify(view).setOnClickListener(Mockito.any(View.OnClickListener.class));
    }

    @Test
    public void test_itemClick_header() {
        MainAdapter adapter = new MainAdapter(context, gateway, getZoneList(1), getButtonList(1));
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
}