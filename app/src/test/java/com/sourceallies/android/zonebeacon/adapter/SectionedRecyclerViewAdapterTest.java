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

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class SectionedRecyclerViewAdapterTest extends ZoneBeaconSuite {

    private SectionedRecyclerViewAdapter adapter = new SectionedRecyclerViewAdapter() {
        @Override
        public int getSectionCount() {
            return 1;
        }

        @Override
        public int getItemCount(int section) {
            return 1;
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }
    };

    @Mock
    GridLayoutManager layoutManager;

    @Before
    public void setUp() {
        Mockito.when(layoutManager.getSpanCount()).thenReturn(2);
        adapter = Mockito.spy(adapter);
    }

    @Test
    public void test_spanSize() {
        Mockito.when(adapter.isHeader(0)).thenReturn(true);
        adapter.setLayoutManager(layoutManager);

        assertEquals(2, adapter.getSizeLookup().getSpanSize(0));
        assertEquals(1, adapter.getSizeLookup().getSpanSize(1));
    }

    @Test
    public void test_itemCount() {
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void test_sectionIndexAndRelativePosition() {
        test_itemCount();

        SectionedRecyclerViewAdapter.ItemPosition positions = adapter.getSectionIndexAndRelativePosition(1);
        assertEquals(0, positions.sectionIndex);
        assertEquals(0, positions.relativeIndex);
    }

    @Test
    public void test_header() {
        test_itemCount();

        assertTrue(adapter.isHeader(0));
        assertFalse(adapter.isHeader(1));
    }

    @Test
    public void test_itemType() {
        Mockito.when(adapter.isHeader(0)).thenReturn(true);

        assertEquals(SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER, adapter.getItemViewType(0));
        assertEquals(SectionedRecyclerViewAdapter.VIEW_TYPE_ITEM, adapter.getItemViewType(1));
    }

    @Test
    public void test_setLayoutParams() {
        View mock = Mockito.mock(View.class);
        ViewGroup.LayoutParams params =
                new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

        adapter.setLayoutParameters(mock, params);

        Mockito.verify(mock).setLayoutParams(Mockito.eq(params));
    }

    @Test
    public void test_bindHeaderView() {
        test_itemCount();

        MainAdapter.ViewHolder holder = Mockito.mock(MainAdapter.ViewHolder.class);

        Mockito.doNothing().when(adapter).onBindHeaderViewHolder(Mockito.any(MainAdapter.ViewHolder.class), Mockito.anyInt());

        adapter.onBindViewHolder(holder, 0);
        Mockito.verify(adapter).onBindHeaderViewHolder(holder, 0);
    }

    @Test
    public void test_bindItemView() {
        test_itemCount();

        MainAdapter.ViewHolder holder = Mockito.mock(MainAdapter.ViewHolder.class);

        Mockito.doNothing().when(adapter).onBindViewHolder(Mockito.any(MainAdapter.ViewHolder.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());

        adapter.onBindViewHolder(holder, 1);
        Mockito.verify(adapter).onBindViewHolder(Mockito.eq(holder), Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(0));
    }
}