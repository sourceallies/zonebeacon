package com.sourceallies.android.zonebeacon.fragment;

import android.support.design.widget.TextInputLayout;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.util.CommandExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class InitialGatewaySetupTest extends ZoneBeaconRobolectricSuite {

    InitialGatewaySetup fragment;

    @Before
    public void setUp() {
        fragment = new InitialGatewaySetup();
        startFragment(fragment);
    }

    @Test
    public void test_nullViews() {
        assertNotNull(fragment.getName());
        assertNotNull(fragment.getIpAddress());
        assertNotNull(fragment.getPort());
    }

    @Test
    public void test_fieldEmpty() {
        assertTrue(fragment.isEmpty(fragment.getName()));
    }

    @Test
    public void test_fieldFilled() {
        setText(fragment.getName(), "Test Gateway");
        assertFalse(fragment.isEmpty(fragment.getName()));
    }

    @Test
    public void test_fieldsComplete() {
        setText(fragment.getName(), "Test Gateway");
        setText(fragment.getIpAddress(), "192.169.1.100");
        setText(fragment.getPort(), "11000");

        assertTrue(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_1() {
        setText(fragment.getName(), "");
        setText(fragment.getIpAddress(), "");
        setText(fragment.getPort(), "");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_2() {
        setText(fragment.getName(), "");
        setText(fragment.getIpAddress(), "");
        setText(fragment.getPort(), "11000");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_3() {
        setText(fragment.getName(), "Test Gateway");
        setText(fragment.getIpAddress(), "");
        setText(fragment.getPort(), "");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_4() {
        setText(fragment.getName(), "");
        setText(fragment.getIpAddress(), "192.169.1.100");
        setText(fragment.getPort(), "");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_5() {
        setText(fragment.getName(), "");
        setText(fragment.getIpAddress(), "192.169.1.100");
        setText(fragment.getPort(), "11000");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_6() {
        setText(fragment.getName(), "Test Gateway");
        setText(fragment.getIpAddress(), "");
        setText(fragment.getPort(), "11000");

        assertFalse(fragment.isComplete());
    }

    @Test
    public void test_fieldsNotComplete_7() {
        setText(fragment.getName(), "Test Gateway");
        setText(fragment.getIpAddress(), "192.169.1.100");
        setText(fragment.getPort(), "");

        assertFalse(fragment.isComplete());
    }

    private void setText(TextInputLayout input, String text) {
        input.getEditText().setText(text);
    }
}