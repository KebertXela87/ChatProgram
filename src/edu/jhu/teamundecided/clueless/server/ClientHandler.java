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
            case "getmoves":
                getPossibleMoves(tokens[1]);
                break;
            case "getSuggestRoom":
                getSuggestionRoom(tokens[1]);
                break;
            case "getPlayerHand":
                getPlayerHand(tokens[1]);
                break;
            case "character":
                setPlayerCharacter(tokens[1]);
                break;
            case "getDisabledCharacters":
                getDisableCharacterList();
                break;
            case "logoff":
                handleLoggoff(tokens[1]);
                break;
        }
    }

    private void getPlayerHand(String token)
    {
        //TODO UNCOMMENT THIS CODE ONCE ROOM PLACEMENT IS IMPLEMENTED
//        writeToClient("playerHandDialog" + _player.getPlayerHand().toString());

        // DEMO CODE
        writeToClient("playerHandDialog:hall:mustard:scarlett:rope:knife:ballroom");
    }

    private void getSuggestionRoom(String token)
    {
        //TODO UNCOMMENT THIS CODE ONCE ROOM PLACEMENT IS IMPLEMENTED
//        writeToClient("suggestDialog:" + _player.getLocation().getRoomName());

        // DEMO CODE
        writeToClient("suggestDialog:hall");
    }

    private void getDisableCharacterList()
    {
        StringBuilder list = new StringBuilder("disableCharacter");
        for (ClientHandler client : _server.getCients())
        {
            list.append(":").append(client.getPlayer().getCharacterName());
        }
        broadcast(list.toString());
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

    private void getPossibleMoves(String name)
    {
        //TODO UNCOMMENT THIS CODE ONCE ROOM PLACEMENT IS IMPLEMENTED
//        StringBuilder moves = new StringBuilder("moveDialog");
//        for (String roomname : _player.getLocation().getPossibleMoves())
//        {
//            moves.append(":" + roomname);
//        }
//        writeToClient(moves.toString());

        // DEMO CODE
        writeToClient("moveDialog:study:study:study");
    }

    private void setPlayerCharacter(String name)
    {
        System.out.println("setting player " + _player.getUserName() + " to " + name);
        _player.setCharacterName(name);
        for (ClientHandler client : _server.getCients())
        {
            client.writeToClient("disableCharacter:" + name);
        }
    }

    public void handleLoggoff(String name)
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
        broadcast(name + " has logged off.");
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
