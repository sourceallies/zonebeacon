package com.sourceallies.android.zonebeacon.fragment;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.CommandType;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CommandSetupFragmentTest extends ZoneBeaconRobolectricSuite {

    @Mock
    FragmentActivity activity;

    @Mock
    DataSource dataSource;

    CommandSetupFragment fragment;

    @Before
    public void setUp() {
        fragment = Mockito.spy(new CommandSetupFragment());
        Mockito.when(fragment.getActivity()).thenReturn(activity);

        List<CommandType> allItems = new ArrayList();

        for (int i = 0; i < 5; i++) {
            CommandType type = new CommandType();
            type.setActivateControllerSelection(i % 2 == 0);
            type.setShownInCommandList(true);
            type.setId(i);
            type.setName("comannd type " + i);

            allItems.add(type);
        }

        when(fragment.getDataSource()).thenReturn(dataSource);
        doReturn(allItems).when(fragment).findCommandTypes(any(DataSource.class));

        Gateway currentGateway = new Gateway();
        currentGateway.setName("test gateway");
        currentGateway.setId(1);
        doReturn(currentGateway).when(fragment).getCurrentGateway();

        startFragment(fragment);
    }

    @Test
    public void test_nullViews() {
        assertNotNull(fragment.getName());
        assertNotNull(fragment.getCommandTypeSpinner());
        assertNotNull(fragment.getLoadNumber());
        assertNotNull(fragment.getControllerNumber());
    }

    @Test
    public void test_fieldInvisible() {
        fragment.getName().setVisibility(View.INVISIBLE);
        assertFalse(fragment.isEmpty(fragment.getName()));
    }

    @Test
    public void test_fieldsComplete_1() {
        setText(fragment.getName(), "Test Command");
        setText(fragment.getLoadNumber(), "1");
        fragment.getControllerNumber().setVisibility(View.INVISIBLE);

        assertTrue(fragment.isComplete());
    }

    @Test
    public void test_fieldsComplete_2() {
        fragment.getControllerNumber().setVisibility(View.VISIBLE);

        setText(fragment.getName(), "Test Command");
        setText(fragment.getLoadNumber(), "1");
        setText(fragment.getControllerNumber(), "1");

        assertTrue(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_1() {
        fragment.getControllerNumber().setVisibility(View.VISIBLE);

        setText(fragment.getName(), "");
        setText(fragment.getLoadNumber(), "");
        setText(fragment.getControllerNumber(), "");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_2() {
        fragment.getControllerNumber().setVisibility(View.INVISIBLE);

        setText(fragment.getName(), "");
        setText(fragment.getLoadNumber(), "");
        setText(fragment.getControllerNumber(), "");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_3() {
        fragment.getControllerNumber().setVisibility(View.VISIBLE);

        setText(fragment.getName(), "");
        setText(fragment.getLoadNumber(), "1");
        setText(fragment.getControllerNumber(), "1");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_4() {
        fragment.getControllerNumber().setVisibility(View.VISIBLE);

        setText(fragment.getName(), "test command");
        setText(fragment.getLoadNumber(), "");
        setText(fragment.getControllerNumber(), "1");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_5() {
        fragment.getControllerNumber().setVisibility(View.VISIBLE);

        setText(fragment.getName(), "test command");
        setText(fragment.getLoadNumber(), "1");
        setText(fragment.getControllerNumber(), "");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_save() {
        fragment.getControllerNumber().setVisibility(View.INVISIBLE);

        setText(fragment.getName(), "test command");
        setText(fragment.getLoadNumber(), "1");

        fragment.save();
        verify(dataSource).insertNewCommand("test command", 1, 1, 0, null);
    }

    @Test
    public void test_save_withController() {
        fragment.getControllerNumber().setVisibility(View.VISIBLE);

        setText(fragment.getName(), "test command");
        setText(fragment.getLoadNumber(), "1");
        setText(fragment.getControllerNumber(), "1");

        fragment.save();
        verify(dataSource).insertNewCommand("test command", 1, 1, 0, 1);
    }

    @Test
    public void test_adapterClickListener_nothingSelected() {
        TextInputLayout spy = spy(fragment.getControllerNumber());
        fragment.getItemSelectedListener().onNothingSelected(null);
        verifyNoMoreInteractions(spy);
    }

    @Test
    public void test_adapterClickListener_itemSelected() {
        fragment.getItemSelectedListener().onItemSelected(null, null, 1, 1);
        assertEquals(View.INVISIBLE, fragment.getControllerNumber().getVisibility());

        fragment.getItemSelectedListener().onItemSelected(null, null, 2, 2);
        assertEquals(View.VISIBLE, fragment.getControllerNumber().getVisibility());
    }


    private void setText(TextInputLayout input, String text) {
        input.getEditText().setText(text);
    }
}