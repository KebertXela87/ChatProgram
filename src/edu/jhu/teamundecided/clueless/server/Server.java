package edu.jhu.teamundecided.clueless.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread
{
    private ArrayList<ClientHandler> _clients = new ArrayList<>();
    private int _numConnections;

    private ServerSocket _serverSocket;
    private Socket _socket;

    private int _serverPort;

    public ServerApp getServerApp()
    {
        return _serverApp;
    }

    private ServerApp _serverApp;

    public Server(int port)
    {
        _serverPort = port;
        _numConnections = 0;

        _serverApp = new ServerApp();
        _serverApp.writeToWindow("Welcome to the Server...");
    }

    public void run()
    {
        try
        {
            _serverSocket = new ServerSocket(8818);

            while (_numConnections < 6)
            {
                _socket = _serverSocket.accept();

                ClientHandler client = new ClientHandler(_socket, this);

                addHandler(client);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addHandler(ClientHandler client)
    {
        _clients.add(client);
        _numConnections++;
        client.start();
    }

    public void removeHandler(ClientHandler client)
    {
        _clients.remove(client);
        _numConnections--;
    }

    public ArrayList<ClientHandler> getCients()
    {
        return _clients;
    }

    public static void main(String[] args)
    {
        Server server = new Server(8818);

        server.start();
    }
}
