package com.sourceallies.android.zonebeacon.fragment;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.Command;
import com.sourceallies.android.zonebeacon.util.CommandExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class SendTCPFragmentTest extends ZoneBeaconRobolectricSuite {

    @Mock
    CommandExecutor commandExecutor;
    SendTCPFragment fragment;

    @Before
    public void setUp() {
        fragment = new SendTCPFragment();
        fragment.setCommandExecuter(commandExecutor);

        startFragment(fragment);
    }

    @Test
    public void test_nullViews() {
        assertNotNull(fragment.getCommandText());
        assertNotNull(fragment.getResponseText());
        assertNotNull(fragment.getSendButton());
    }

    @Test
    public void test_sendClick() {
        // stub out the send method so we aren't actually sending anything
        when(commandExecutor.sendCommand(any(Command.class))).thenReturn(1);

        fragment.getSendButton().callOnClick();

        // since the button writes to our text view, we can assert that it did write.
        assertTrue(fragment.getResponseText().getText().length() > 0);
    }

    @Test
    public void test_onResponse() {
        fragment.onResponse("Test");

        assertTrue(fragment.getResponseText().getText().toString().endsWith(
                "\nResponse: Test"
        ));
        assertTrue(fragment.getSendButton().isEnabled());
    }
}