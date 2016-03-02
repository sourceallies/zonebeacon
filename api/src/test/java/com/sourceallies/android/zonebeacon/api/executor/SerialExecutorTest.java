package com.sourceallies.android.zonebeacon.api.executor;

import com.sourceallies.android.zonebeacon.ZoneBeaconRobolectricSuite;
import com.sourceallies.android.zonebeacon.api.CommandCallback;
import com.sourceallies.android.zonebeacon.api.interpreter.Interpreter;
import com.sourceallies.android.zonebeacon.data.model.Command;
import com.sourceallies.android.zonebeacon.data.model.Gateway;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.robolectric.Robolectric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class SerialExecutorTest extends ZoneBeaconRobolectricSuite {

    private SerialExecutor executor;

    @Mock
    private Gateway gateway;
    @Mock
    private Command command;
    @Mock
    private List<Command> commands;
    @Mock
    private SerialExecutor.SocketConnection socketConnection;
    @Mock
    private PrintWriter printWriter;
    @Mock
    private InputStream inputStream;
    @Mock
    private OutputStream outputStream;
    @Mock
    private Socket socket;
    @Mock
    private Interpreter interpreter;
    @Mock
    private CommandCallback commandCallback;
    @Mock
    private BufferedReader bufferedReader;

    @Before
    public void setUp() {
        doReturn("192.168.86.150").when(gateway).getIpAddress();
        doReturn(11000).when(gateway).getPortNumber();

        executor = spy(new SerialExecutor(interpreter));
    }

    @Test
    public void test_notNull() {
        assertNotNull(executor);
    }

    @Test(expected = RuntimeException.class)
    public void test_addNullCommand() {
        executor.addCommand(null);
    }

    @Test
    public void test_addCommand() {
        executor.addCommand(command);
        assertEquals(1, executor.getCommands().size());
    }

    @Test
    public void test_addCommands() {
        List<Command> commands = new ArrayList<>();
        commands.add(command);
        commands.add(command);
        commands.add(command);
        commands.add(command);
        executor.addCommands(commands);

        assertEquals(4, executor.getCommands().size());
    }

    @Test(expected = RuntimeException.class)
    public void test_addCommandsWithNull() {
        List<Command> commands = new ArrayList<>();
        commands.add(command);
        commands.add(null);
        commands.add(command);
        commands.add(command);
        executor.addCommands(commands);
    }

    @Test
    public void test_executeCommands_multiple() throws Exception {
        doReturn(socketConnection).when(executor).createSocketConnection(any(Gateway.class));
        doReturn(printWriter).when(executor).createPrintWriter(socketConnection);
        doReturn(bufferedReader).when(executor).createBufferedReader(socketConnection);
        doReturn(inputStream).when(socketConnection).getInputStream();
        when(bufferedReader.read()).thenReturn(1);
        when(bufferedReader.ready()).thenReturn(true);

        Command command1 = mock(Command.class);
        Command command2 = mock(Command.class);
        Command command3 = mock(Command.class);

        when(interpreter.getExecutable(command1)).thenReturn("test 1");
        when(interpreter.getExecutable(command2)).thenReturn("test 2");
        when(interpreter.getExecutable(command3)).thenReturn("test 3");

        executor.addCommand(command1);
        executor.addCommand(command2);
        executor.addCommand(command3);

        executor.setCommandCallback(commandCallback);
        executor.connect(gateway);
        executor.execute();

        Thread.sleep(10);
        when(bufferedReader.ready()).thenReturn(false);

        InOrder inOrder = inOrder(printWriter);

        inOrder.verify(printWriter).print("test 1\r\n");
        inOrder.verify(printWriter).print("test 2\r\n");
        inOrder.verify(printWriter).print("test 3\r\n");

        verify(commandCallback).onResponse(eq(command1), anyString());
        verify(commandCallback).onResponse(eq(command2), anyString());
        verify(commandCallback).onResponse(eq(command3), anyString());
    }

    @Test
    public void test_ioExceptionOnExecute() throws Exception {
        doReturn(socketConnection).when(executor).createSocketConnection(any(Gateway.class));
        doThrow(IOException.class).when(executor).send("test 1");

        Command command1 = mock(Command.class);
        when(interpreter.getExecutable(command1)).thenReturn("test 1");
        executor.addCommand(command1);

        executor.connect(gateway);
        executor.execute();
    }

    @Test
    public void test_closeSocket() throws Exception {
        executor.closeSocket(socket);
        verify(socket).close();
    }

    @Test
    public void test_closeSocket_exception() throws Exception {
        doThrow(IOException.class).when(socket).close();
        executor.closeSocket(socket);
        verify(socket).close();
        verifyNoMoreInteractions(socket);
    }

    @Test
    public void test_shutdownConnection() throws Exception {
        when(socketConnection.getSocket()).thenReturn(socket);
        when(socketConnection.getInputStream()).thenReturn(inputStream);
        when(socketConnection.getOutputStream()).thenReturn(outputStream);
        doReturn(socketConnection).when(executor).createSocketConnection(any(Gateway.class));

        executor.connect(gateway);
        executor.disconnect();

        verify(socket).close();
        verify(inputStream).close();
        verify(outputStream).close();
    }

    @Test
    public void test_shutdownConnection_withExceptions() throws Exception {
        when(socketConnection.getSocket()).thenReturn(socket);
        when(socketConnection.getInputStream()).thenReturn(inputStream);
        when(socketConnection.getOutputStream()).thenReturn(outputStream);
        doReturn(socketConnection).when(executor).createSocketConnection(any(Gateway.class));

        doThrow(IOException.class).when(socket).close();
        doThrow(IOException.class).when(inputStream).close();
        doThrow(IOException.class).when(outputStream).close();

        executor.connect(gateway);
        executor.disconnect();

        verify(socket).close();
        verify(inputStream).close();
        verify(outputStream).close();
    }

    @Test
    public void test_socketConnection() throws Exception {
        when(socket.getInputStream()).thenReturn(inputStream);
        when(socket.getOutputStream()).thenReturn(outputStream);
        SerialExecutor.SocketConnection connection = new SerialExecutor.SocketConnection(socket);

        assertNotNull(connection.getSocket());
        assertNotNull(connection.getInputStream());
        assertNotNull(connection.getOutputStream());
    }

    @Test
    public void test_socketConnection_createSocket_null() {
        assertNull(SerialExecutor.SocketConnection.createSocket(null));
    }

    @Test
    public void test_createSocket_nullGateway() {
        SerialExecutor.SocketConnection connection = executor.createSocketConnection(null);
        assertNull(connection.getSocket());
        assertNull(connection.getInputStream());
        assertNull(connection.getOutputStream());
    }

    @Test
    public void test_socketTimeout() throws Exception {
        executor.setCommandCallback(commandCallback);
        executor.setTimeoutOnSocket(socket);
        Robolectric.flushForegroundThreadScheduler();
        verify(executor).closeSocket(socket);
        verify(commandCallback).onResponse(null, "No Response");
    }

    @Test
    public void test_pingGateway() {
        assertTrue(executor.pingGateway(gateway));
    }

    @Test
    public void test_getBufferedReader() {
        when(socketConnection.getInputStream()).thenReturn(inputStream);
        assertNotNull(executor.createBufferedReader(socketConnection));
    }

    @Test
    public void test_getPrintWriter() {
        when(socketConnection.getOutputStream()).thenReturn(outputStream);
        assertNotNull(executor.createPrintWriter(socketConnection));
    }

}