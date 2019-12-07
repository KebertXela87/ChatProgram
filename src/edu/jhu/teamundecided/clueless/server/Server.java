package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.database.Database;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server extends Thread
{
    private ArrayList<ClientHandler> _clients = new ArrayList<>();
    private int _numConnections;

    private ServerSocket _serverSocket;
    private Socket _socket;

    private int _serverPort;

    private boolean _running = true;

    private GameController gc = GameController.getInstance(this);

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
        serverCloseOperation();
        _serverApp.writeToWindow("Welcome to the Server...");
    }

    public void serverCloseOperation()
    {
        _serverApp.getServerFrame().addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                shutdown();
            }
        });

        _serverApp.getServerFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void shutdown()
    {
        closeConnections();

        // Close window
        System.exit(0);
    }

    public void closeConnections()
    {
        try
        {
            System.out.println("Running the server close listener!");
            // Close connection to server
            if(_serverSocket != null)
            {
                _running = false;
                for (ClientHandler handler : _clients)
                {
                    System.out.println("Closing socket for " + handler.getPlayer().getUserName());
                    handler.writeToClient("serverclose");
                }
                _serverSocket.close();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void run()
    {
        try
        {
            _serverSocket = new ServerSocket(8818);

            while (_running)
            {
                if(_numConnections < 6)
                {
                    try
                    {
                        System.out.println("trying to accept connection");
                        _socket = _serverSocket.accept();
                        System.out.println("made connection");
                        ClientHandler client = new ClientHandler(_socket, this);

                        addHandler(client);
                    }
                    catch (SocketException e)
                    {
                        System.out.println("Server is closed. Accepting no more connections.");
                    }
                }
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

    public GameController getGameController() { return gc; }

    public void broadcastToAll(String msg){
        for (ClientHandler handler : _clients) {
            handler.writeToClient(msg);
        }
    }
}
