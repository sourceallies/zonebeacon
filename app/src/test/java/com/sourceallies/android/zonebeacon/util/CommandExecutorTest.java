package com.sourceallies.android.zonebeacon.util;

import android.app.Activity;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.data.Command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CommandExecutorTest extends ZoneBeaconRobolectricSuite {

    @Mock
    Activity activity;
    @Mock
    List<Command> commands;

    private CommandExecutor commandExecutor;

    @Before
    public void setUp() {
        commandExecutor = new CommandExecutor();
        commandExecutor.setActivity(activity);
        commandExecutor.setCommands(commands);

        commandExecutor = Mockito.spy(commandExecutor);
    }

    @Test(expected = RuntimeException.class)
    public void test_addNullCommand() {
        Command command = new Command("test.ip", 100);
        commandExecutor.sendCommand(command);
    }

    @Test
    public void test_addElementsToQueue() {
        // stub out the execute method since we are just checking they get added to the queue.
        doNothing().when(commandExecutor).execute();

        Command command = new Command("test.ip", 100)
                .setCommand("testing");
        commandExecutor.addCommand(command);
        commandExecutor.addCommand(command);
        commandExecutor.addCommand(command);

        verify(commands, Mockito.times(3)).add(any(Command.class));
    }

    @Test
    public void test_executeCommands() {
        // stub out the execute method since we are just checking they get added to the queue.
        doNothing().when(commandExecutor).execute();

        Command command = new Command("test.ip", 100)
                .setCommand("testing");
        commandExecutor.sendCommand(command);
        commandExecutor.sendCommand(command);
        commandExecutor.sendCommand(command);

        verify(commandExecutor, times(3)).execute();
    }
}