package com.sourceallies.android.zonebeacon.util;

import android.app.Activity;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.ZoneBeaconSuite;
import com.sourceallies.android.zonebeacon.data.Command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CommandExecutorTest extends ZoneBeaconRobolectricSuite {

    @Mock
    Activity activity;
    @Mock
    List<Command> commands;
    @Mock
    CommandExecutor.SocketConnection socketConnection;
    @Mock
    PrintWriter printWriter;
    @Mock
    InputStream inputStream;
    @Mock
    OutputStream outputStream;
    @Mock
    Socket socket;

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

    @Test
    public void test_executeCommands_multiple() throws Exception {
        doReturn(socketConnection).when(commandExecutor)
                .getOrCreateSocketConnection(any(Command.class));
        doReturn(printWriter).when(commandExecutor).createPrintWriter(socketConnection);
        doReturn(inputStream).when(socketConnection).getInputStream();

        Command command1 = new Command("test.ip", 100)
                .setCommand("test 1");
        Command command2 = new Command("test.ip", 100)
                .setCommand("test 2");
        Command command3 = new Command("test.ip", 100)
                .setCommand("test 3");

        commandExecutor.setCommands(new ArrayList<Command>());
        commandExecutor.addCommand(command1);
        commandExecutor.addCommand(command2);
        commandExecutor.addCommand(command3);

        commandExecutor.execute();

        Thread.sleep(200);

        InOrder inOrder = inOrder(printWriter);

        inOrder.verify(printWriter).print("test 1\r\n");
        inOrder.verify(printWriter).print("test 2\r\n");
        inOrder.verify(printWriter).print("test 3\r\n");
    }

    @Test
    public void test_closeSocket() throws Exception {
        commandExecutor.closeSocket(socket);
        verify(socket).close();
    }

    @Test
    public void test_closeSocket_exception() throws Exception {
        doThrow(IOException.class).when(socket).close();
        commandExecutor.closeSocket(socket);
        verify(socket).close();
        verifyNoMoreInteractions(socket);
    }

    @Test
    public void test_getOrCreateSocketConnection() {
        Command command = new Command("192.168.1.150", 11000);

        CommandExecutor.SocketConnection connection =
                commandExecutor.getOrCreateSocketConnection(command);
        assertNotNull(connection);

        CommandExecutor.SocketConnection connection2 =
                commandExecutor.getOrCreateSocketConnection(command);
        assertEquals(connection, connection2);
    }

    @Test
    public void test_shutdownConnection() throws Exception {
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        CommandExecutor.SocketConnection connection = new CommandExecutor.SocketConnection(socket);

        commandExecutor.shutdownConnection(connection);

        verify(socket).close();
        verify(inputStream).close();
        verify(outputStream).close();
    }

    @Test
    public void test_shutdownConnection_withExceptions() throws Exception {
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        CommandExecutor.SocketConnection connection = new CommandExecutor.SocketConnection(socket);

        doThrow(IOException.class).when(socket).close();
        doThrow(IOException.class).when(inputStream).close();
        doThrow(IOException.class).when(outputStream).close();

        commandExecutor.shutdownConnection(connection);

        verify(socket).close();
        verify(inputStream).close();
        verify(outputStream).close();
    }

    @Test
    public void test_socketConnection() throws Exception {
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        CommandExecutor.SocketConnection connection = new CommandExecutor.SocketConnection(socket);

        assertNotNull(connection.getSocket());
        assertNotNull(connection.getInputStream());
        assertNotNull(connection.getOutputStream());
    }
}