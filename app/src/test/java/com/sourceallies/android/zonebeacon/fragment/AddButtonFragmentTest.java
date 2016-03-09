package com.sourceallies.android.zonebeacon.fragment;

import com.sourceallies.android.zonebeacon.data.DataSource;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddButtonFragmentTest extends AbstractAddFragmentTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Gateway gateway;
    @Mock
    private List<Command> items;

    @Override
    public AbstractAddFragment getAddFragment() {
        return (AbstractAddFragment) AbstractSetupFragment.getInstance(new AddButtonFragment(), 1);
    }

    @Test
    public void test_getPageTitle() {
        assertEquals("Add Button:", fragment.getPageTitle());
    }

    @Test
    public void test_getListTitle() {
        assertEquals("Include Commands:", fragment.getListTitle());
    }

    @Test
    public void test_getNameHint() {
        assertEquals("Button Name", fragment.getNameHint());
    }

    @Test
    public void test_insertItems() {
        fragment.insertItems(dataSource, "Test Name", items);
        verify(dataSource).insertNewButton("Test Name", items);
    }

    @Test
    public void test_findItems() {
        when(dataSource.findCommands(gateway)).thenReturn(items);
        assertEquals(items, fragment.findItems(dataSource, gateway));
    }

}