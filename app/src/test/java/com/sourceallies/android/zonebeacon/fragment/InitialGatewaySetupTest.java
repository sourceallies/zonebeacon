package com.sourceallies.android.zonebeacon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.util.CommandExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class InitialGatewaySetupTest extends ZoneBeaconRobolectricSuite {

    @Mock
    FragmentActivity activity;

    InitialGatewaySetup fragment;

    @Before
    public void setUp() {
        fragment = Mockito.spy(new InitialGatewaySetup());
        Mockito.when(fragment.getActivity()).thenReturn(activity);

        startFragment(fragment);
    }

    @Test
    public void test_nullViews() {
        assertNotNull(fragment.getName());
        assertNotNull(fragment.getIpAddress());
        assertNotNull(fragment.getPort());
    }

    @Test
    public void test_videoLink() {
        fragment.getVideoLink().getClickListener().onClick("this YouTube channel");

        Mockito.verify(activity).startActivity(Mockito.any(Intent.class));
    }

    @Test
    public void test_textInputErrors() {
        fragment.isEmpty(fragment.getName());
        assertFalse(TextUtils.isEmpty(fragment.getName().getError()));

        fragment.getName().getEditText().append("Test Name");
        fragment.isEmpty(fragment.getName());
        assertTrue(TextUtils.isEmpty(fragment.getName().getError()));
    }

    @Test
    public void test_noTextInputErrors() {
        fragment.getName().getEditText().setText("Test Name");
        fragment.isEmpty(fragment.getName());

        assertTrue(TextUtils.isEmpty(fragment.getName().getError()));
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