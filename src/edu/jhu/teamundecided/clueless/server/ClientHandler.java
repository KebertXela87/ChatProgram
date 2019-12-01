package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.database.Database;
import edu.jhu.teamundecided.clueless.deck.Suggestion;
import edu.jhu.teamundecided.clueless.player.Player;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread
{
    private BufferedReader _reader; // Input from Client App
    private BufferedWriter _writer; // Output to Client App

    private Socket _clientSocket;

    private ServerApp _serverApp;

    private Database database = Database.getInstance();
    private GameController gameController;

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
            gameController = server.getGameController(); // Get the Server GameController

            _serverApp = server.getServerApp();

            _player = new Player(this);

           gameController.addPlayer(_player);
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
                    _serverApp.writeToWindow("Client Message [" + _player.getUserName() + "]: " + received);

                    handleMessage(received);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Database getClientDatabase() { return database; }

    public void handleMessage(String message)
    {
        String[] tokens = message.split(":", 2);

        switch(tokens[0])
        {
            case "chat":
                broadcast(tokens[1]); // ClientHandler Method
                break;
            case "user":
                setUserName(tokens[1]); // ClientHandler Method
                break;
            case "getDisabledCharacters":
                getDisableCharacterList(); // ClientHandler Method
                break;
            case "getmoves":
                getPossibleMoves(tokens[1]); // ClientHandler Method
                break;
            case "move":
                updateLocations(tokens[1]);
                break;
            case "getSuggestRoom":
                getSuggestionRoom(tokens[1]); // ClientHandler Method
                break;
            case "getPlayerHand":
                getPlayerHand(tokens[1]); // ClientHandler Method
                break;
            case "character":
                setPlayerCharacter(tokens[1]); // ClientHandler Method
                break;
            case "logoff":
                handleLogoff(tokens[1]); // ClientHandler Method
                break;
            case "shutdownServer":
                gameController.getGameServer().shutdown();
                break;
           case "suggestion":
              handleSuggestionFromClient(tokens[1]);
              break;
        }
    }


   private void handleSuggestionFromClient(String tokens)
   {
      // TODO - unpack tokens and use it to create suggestion object - Sean
      // TODO - broadcast that the player has made a suggestion

      Suggestion suggestion = new Suggestion(null, null, null);

      boolean wasDisproven = gameController.disproveSequence(suggestion);
   }


   public void broadcast(String message)
    {
        for (ClientHandler client : gameController.getGameServer().getCients())
        {
            client.writeToClient(message);
        }
    }

    public void setUserName(String name)
    {
        _player.setUserName(name);
    }

    private void getDisableCharacterList()
    {
        broadcast("disableCharacter" + gameController.getSelectedCharacters());
    }

    private void getPlayerHand(String token)
    {
        //TODO UNCOMMENT THIS CODE ONCE GAME START IS IMPLEMENTED
//        writeToClient("playerHandDialog" + _player.getPlayerHand().toString());

        // DEMO CODE
        writeToClient("playerHandDialog:hall:mustard:scarlett:rope:knife:ballroom");
    }

    /**
     * Sets the Client's Player Character and their Starting Location
     * @param name - the character name to set this client player to
     */
    public void setPlayerCharacter(String name)
    {
        broadcast("disableCharacter:" + name);
        _player.setCharacterName(name);
        _player.setLocation(gameController.getGameBoard().findRoom(name + "startloc"));

        // TODO this may not go here, but it goes somewhere in this class
        setIsReady(true);
    }

    private void getSuggestionRoom(String token)
    {
        //TODO UNCOMMENT THIS CODE ONCE ROOM PLACEMENT IS IMPLEMENTED
//        writeToClient("suggestDialog:" + _player.getLocation().getRoomName());

        // DEMO CODE
        writeToClient("suggestDialog:hall");
    }

    private void getPossibleMoves(String name)
    {
        //TODO UNCOMMENT THIS CODE ONCE ROOM PLACEMENT IS IMPLEMENTED
//        StringBuilder moves = new StringBuilder("moveDialog");
//        for (String roomname : _player.getLocation().getPossibleMoves())
//        {
//            moves.append(":" + roomname);
//        }
//
//        if(_player.getLocation().getIsHall())
//        {
//            moves.append("#").append(database.getHallwayDirections(_player.getLocation().getRoomName()));
//        }
//        writeToClient(moves.toString());

        // Hallway Directions

        // DEMO CODE
        writeToClient("moveDialog:hallway_1:hallway_2:hallway_4#" + database.getHallwayDirections("hall"));
    }

    public void updateLocations(String roomName)
    {
        broadcast(gameController.updateLocations(_player, roomName));
    }

    public void handleLogoff(String name)
    {
        gameController.getGameServer().removeHandler(this);
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
            _serverApp.writeToWindow("Server Message [" + _player.getUserName() + "]: " + message);
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


    private void setIsReady(boolean isReady)
    {
        _player.setIsReady(true);
        gameController.attemptToStart();
    }
}
