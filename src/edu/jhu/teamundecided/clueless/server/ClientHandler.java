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
            case "revealCard":
                handleRevealedCard(tokens[1]);
                break;
            case "accusation":
                handleAccusstionFromClient(tokens[1]);
                break;
            case "readytoplay":
                setIsReady(true);
                break;
            case "endturn":
                gameController.handleEndTurn();
                break;
        }
    }


   private void handleSuggestionFromClient(String message)
   {
       String[] tokens = message.split(":");
       String suspect = tokens[0];
       String weapon = tokens[1];
       String room = tokens[2];

       Suggestion suggestion = new Suggestion(suspect, weapon, room);

       StringBuilder suggestmsg = new StringBuilder(_player.getUserName());
       suggestmsg.append(" has made a suggestion that ").append(suggestion.toString());
       broadcast(suggestmsg.toString());

       if(!gameController.disproveSequence(suggestion))
       {
           // No one could disprove this client's suggestion
           writeToClient("noreveal");
           broadcast("No one could disprove the suggestion made by " + _player.getUserName());
       }
   }

   private void handleAccusstionFromClient(String message)
   {
       String[] tokens = message.split(":");
       Suggestion accusation = new Suggestion(tokens[0], tokens[1], tokens[2]);

       gameController.handleAccusationCommand(accusation);
   }

   private void handleRevealedCard(String revealedCard)
   {
       gameController.revealCard(revealedCard);
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
        writeToClient("playerHandDialog" + _player.getPlayerHand().toString());
    }

    public void setPlayerCharacter(String name)
    {
        broadcast("disableCharacter:" + name);
        _player.setCharacterName(name);
        _player.setLocation(gameController.getGameBoard().findRoom(name + "startloc"));
        _player.setIsActive(true);
    }

    private void getSuggestionRoom(String token)
    {
        writeToClient("suggestDialog:" + _player.getLocation().getRoomName());
    }

    private void getPossibleMoves(String name)
    {
        StringBuilder moves = new StringBuilder("moveDialog");
        for (String roomname : _player.getLocation().getPossibleMoves())
        {
            moves.append(":" + roomname);
        }

        // Check if in hallway, if not - append hallway directions to message
        if(!_player.getLocation().getIsHall())
        {
            moves.append("#").append(database.getHallwayDirections(_player.getLocation().getRoomName()));
        }
        writeToClient(moves.toString());
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
       broadcast(_player.getUserName() + " is ready to play!");
       gameController.attemptToStart();
    }
}
