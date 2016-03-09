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