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

import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Button;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddZoneFragmentTest extends AbstractAddFragmentTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Gateway gateway;
    @Mock
    private List<Button> items;

    @Override
    public AbstractAddFragment getAddFragment() {
        return (AbstractAddFragment) AbstractSetupFragment.getInstance(new AddZoneFragment(), 1);
    }

    @Test
    public void test_getPageTitle() {
        assertEquals("Add Zone:", fragment.getPageTitle());
    }

    @Test
    public void test_getListTitle() {
        assertEquals("Include Buttons:", fragment.getListTitle());
    }

    @Test
    public void test_getNameHint() {
        assertEquals("Zone Name", fragment.getNameHint());
    }

    @Test
    public void test_insertItems() {
        fragment.insertItems(dataSource, "Test Name", items);
        verify(dataSource).insertNewZone("Test Name", items);
    }

    @Test
    public void test_findItems() {
        when(dataSource.findButtons(gateway)).thenReturn(items);
        assertEquals(items, fragment.findItems(dataSource, gateway));
    }

}