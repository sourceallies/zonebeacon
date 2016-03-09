package com.sourceallies.android.zonebeacon.fragment;

import android.support.design.widget.TextInputLayout;
import android.widget.TextView;

import com.sourceallies.android.zonebeacon.R;
import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractAddFragmentTest extends ZoneBeaconRobolectricSuite {

    public AbstractAddFragment fragment;

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

    // TODO add more tests for isEmpty, isComplete, save, etc here.

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