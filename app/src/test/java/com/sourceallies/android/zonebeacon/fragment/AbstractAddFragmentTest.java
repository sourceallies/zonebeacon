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

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractAddFragmentTest extends ZoneBeaconRobolectricSuite {

    public AbstractAddFragment fragment;

    @Mock
    private TextInputLayout textInputLayout;
    @Mock
    private EditText editText;
    @Mock
    private Editable editable;
    @Mock
    private DataSource dataSource;

    @Before
    public void setUp() {
        fragment = getAddFragment();
        startFragment(fragment);
    }

    @Test
    public void test_isAdded() {
        assertTrue(fragment.isAdded());
    }

    @Test
    public void test_pageTitleSet() {
        assertNotNull(fragment.getPageTitle());
        assertNotNull(((TextView) fragment.getView().findViewById(R.id.page_title)).getText());
    }

    @Test
    public void test_listTitleSet() {
        assertNotNull(fragment.getListTitle());
        assertNotNull(((TextView) fragment.getView().findViewById(R.id.list_title)).getText());
    }

    @Test
    public void test_nameHintSet() {
        assertNotNull(fragment.getNameHint());
        assertNotNull(((TextInputLayout) fragment.getView().findViewById(R.id.name)).getHint());
    }

    @Test
    public void test_nameInitialized() {
        assertNotNull(fragment.getName());
    }

    @Test
    public void test_listInitialized() {
        assertNotNull(fragment.getList());
    }

    @Test
    public void test_getText() {
        when(textInputLayout.getEditText()).thenReturn(editText);
        when(editText.getText()).thenReturn(editable);
        when(editable.toString()).thenReturn("Test String");

        assertEquals("Test String", fragment.getText(textInputLayout));
    }

    @Test
    public void test_populateCommandList() {
        List<String> allItems = new ArrayList<String>();
        allItems.add("1");
        allItems.add("2");
        allItems.add("3");

        fragment = spy(fragment);
        doReturn(allItems).when(fragment).findItems(any(DataSource.class), any(Gateway.class));

        fragment.populateCommandList();
        assertEquals(3, fragment.getList().getAdapter().getCount());
    }

    @Test
    public void test_getCheckedItems() {
        test_populateCommandList();
        fragment.getList().setItemChecked(0, true);
        fragment.getList().setItemChecked(1, false);
        fragment.getList().setItemChecked(2, true);

        List<String> checkedItems = fragment.getCheckedItems();
        assertEquals(2, checkedItems.size());
        assertEquals("1", checkedItems.get(0));
        assertEquals("3", checkedItems.get(1));
    }

    @Test
    public void test_multiSelectEnabled() {
        assertEquals(ListView.CHOICE_MODE_MULTIPLE, fragment.getList().getChoiceMode());
    }

    @Test
    public void test_isComplete() {
        assertFalse(fragment.isComplete());
        fragment.getName().getEditText().setText("apartment");
        assertTrue(fragment.isComplete());
    }

    @Test
    public void test_save() {
        test_getCheckedItems();
        test_isComplete();

        List<String> items = new ArrayList<String>();
        items.add("1");
        items.add("3");

        doNothing().when(fragment).insertItems(any(DataSource.class), eq("apartment"), eq(items));
        fragment.save();

        verify(fragment).insertItems(any(DataSource.class), eq("apartment"), eq(items));
    }

    public AbstractAddFragment getAddFragment() {
        return (AbstractAddFragment) AbstractSetupFragment.getInstance(new AbstractAddFragment() {
            @Override
            public String getPageTitle() {
                return "Test";
            }

            @Override
            public String getListTitle() {
                return "Test list";
            }

            @Override
            public String getNameHint() {
                return "Name";
            }

            @Override
            public void insertItems(DataSource dataSource, String name, List items) {

            }

            @Override
            public List findItems(DataSource dataSource, Gateway currentGateway) {
                return new ArrayList();
            }
        }, 1);
    }

}