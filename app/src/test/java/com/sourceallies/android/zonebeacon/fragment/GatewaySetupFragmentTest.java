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

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class GatewaySetupFragmentTest extends ZoneBeaconRobolectricSuite {

    @Mock
    FragmentActivity activity;

    GatewaySetupFragment fragment;

    @Before
    public void setUp() {
        fragment = Mockito.spy(new GatewaySetupFragment());
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

        verify(activity).startActivity(any(Intent.class));
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

    @Test
    public void test_clickTransferButton() {
        fragment.getView().findViewById(R.id.transfer_button).performClick();
        verify(activity).startActivity(any(Intent.class));
    }

    private void setText(TextInputLayout input, String text) {
        input.getEditText().setText(text);
    }
}