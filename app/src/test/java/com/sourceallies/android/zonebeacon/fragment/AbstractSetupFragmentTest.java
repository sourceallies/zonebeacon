package com.sourceallies.android.zonebeacon.fragment;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractSetupFragmentTest extends ZoneBeaconRobolectricSuite {

    private AbstractSetupFragment fragment = new AbstractSetupFragment() {
        @Override
        public void save() {

        }

        @Override
        public boolean isComplete() {
            return false;
        }
    };

    @Before
    public void setUp() {
        startFragment(fragment);
    }

    @Test
    public void test_isAdded() {
        assertTrue(fragment.isAdded());
    }

}