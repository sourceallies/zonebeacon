package com.sourceallies.android.zonebeacon.util;

import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.activity.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;

public class FloatingActionButtonBehaviorTest extends ZoneBeaconRobolectricSuite {

    FloatingActionButtonBehavior behavior;

    @Mock
    private Snackbar.SnackbarLayout snackbarLayout;
    @Mock
    private LinearLayout linearLayout;

    @Before
    public void setUp() {
        behavior = new FloatingActionButtonBehavior(null, null);
    }

    @Test
    public void test_dependency() {
        assertTrue(behavior.layoutDependsOn(null, null, snackbarLayout));
        assertFalse(behavior.layoutDependsOn(null, null, linearLayout));
    }

    @Test
    public void test_translation() {
        Mockito.when(snackbarLayout.getTranslationY()).thenReturn(0f);
        Mockito.when(snackbarLayout.getHeight()).thenReturn(0);

        behavior.onDependentViewChanged(null, linearLayout, snackbarLayout);

        assertEquals(0, linearLayout.getTranslationY(), .1f);
    }
}
