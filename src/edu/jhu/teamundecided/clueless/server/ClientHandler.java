package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.player.Player;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread
{
    private BufferedReader _reader; // Input from Client App
    private BufferedWriter _writer; // Output to Client App

    private Socket _clientSocket;
    private Server _server;

    private ServerApp _serverApp;

    // User Variables
    private Player _player;

    private boolean _handlerRunning = true;

    public ClientHandler(Socket socket, Server server)
    {
        try
        {
            _reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            _writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            _clientSocket = socket;
            _server = server;
            _serverApp = server.getServerApp();

            _player = new Player();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        String received;
        while (_handlerRunning)
        {
            try
            {
                received = _reader.readLine();

                if (received != null)
                {
                    _serverApp.writeToWindow(received);

                    handleMessage(received);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void handleMessage(String message)
    {
        System.out.println("Message: " + message);
        String[] tokens = message.split(":", 2);

        switch(tokens[0])
        {
            case "chat":
                broadcast(tokens[1]);
                break;
            case "user":
                setUserName(tokens[1]);
                break;
            case "logoff":
                handleLoggoff();
                break;
        }
    }

    public void broadcast(String message)
    {
        for (ClientHandler client : _server.getCients())
        {
            client.writeToClient(message);
        }
    }

    public void setUserName(String name)
    {
        _player.setUserName(name);
    }

    public void handleLoggoff()
    {
        _server.removeHandler(this);
        try
        {
            _handlerRunning = false;
            closeSocket(_clientSocket);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public BufferedWriter getWriter()
    {
        return _writer;
    }

    public void writeToClient(String message)
    {
        try
        {
            _writer.write(message);
            _writer.newLine();
            _writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Socket getClientSocket()
    {
        return _clientSocket;
    }

    public Player getPlayer()
    {
        return _player;
    }

    public void closeSocket(Socket socket) throws IOException
    {
        _reader.close();
        _writer.close();
        socket.close();
    }
}
