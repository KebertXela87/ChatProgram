package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.deck.Card;
import edu.jhu.teamundecided.clueless.deck.DeckController;
import edu.jhu.teamundecided.clueless.deck.Suggestion;
import edu.jhu.teamundecided.clueless.gameboard.GameBoard;
import edu.jhu.teamundecided.clueless.gameboard.Room;
import edu.jhu.teamundecided.clueless.player.Player;

import java.util.ArrayList;

public class GameController
{

   private static GameController _gameController = null;

   private Server _server;
   private GameBoard _gameboard;
   private DeckController _deckController;
   private int _turn;
   private boolean _gameOver;
   private ArrayList<Player> _players;


   public GameController(Server server)
   {

      _server = server;
      _gameboard = new GameBoard();
      _deckController = new DeckController();
      _turn = -1;
      _gameOver = false;
      _players = new ArrayList<>();
   }


   public static GameController getInstance(Server server)
   {

      if (_gameController == null)
      {
         _gameController = new GameController(server);
      }

      return _gameController;
   }


   public Server getGameServer()
   {

      return _server;
   }


   public GameBoard getGameBoard()
   {

      return _gameboard;
   }


   public String getSelectedCharacters()
   {

      StringBuilder list = new StringBuilder();

      for (Player player : _players)
      {
         list.append(":").append(player.getCharacterName());
      }

/*      for (ClientHandler client : _server.getCients())
      {
         list.append(":").append(client.getPlayer().getCharacterName());
      }*/

      return list.toString();



   }


   public String updateLocations(Player player, String newRoomName)
   {

      System.out.println("Creating moveSprites message...");
      StringBuilder moveSpritesMsg = new StringBuilder("moveSprites:");

      Room oldRoom = player.getLocation();
      Room newRoom = _gameboard.findRoom(newRoomName);

      // Update Player Object Location
      _gameboard.movePlayer(player, newRoom); // GameBoard Method

      moveSpritesMsg.append(oldRoom.getRoomName());

      for (Player occupant : oldRoom.getOccupants())
      {
         moveSpritesMsg.append(":").append(occupant.getCharacterName());
      }

      moveSpritesMsg.append("#").append(newRoom.getRoomName());

      for (Player occupant : newRoom.getOccupants())
      {
         moveSpritesMsg.append(":").append(occupant.getCharacterName());
      }

      return moveSpritesMsg.toString();
   }


   public ArrayList<Player> getPlayers()
   {

      return _players;
   }


   public Player getNextPlayer()
   {

      return _players.get(++_turn % _players.size());
   }


   public void handleEndTurn()
   {

      // TODO end turn messaging if necessary - Sean
      // TODO should this be called by the client handler? - Sean

      if (!isGameOver())
      {
         startTurn(getNextPlayer());
      } else
      {
         // TODO end of game procedures - Sean
      }
   }


   public void handleMoveRequest(Player player, String room)
   {
      // TODO - is this a duplicate function of updateLocation?
      Room destination = _gameboard.findRoom(room);
      _gameboard.movePlayer(player, destination);
      //TODO broadcast move to all players
   }


   public void handleAccusationCommand(Suggestion accusation)
   {

      broadcast(_players.get(_turn).getUserName() + " has made an accusation that " + accusation.toString());

      if (_deckController.checkAccusation(accusation))
      {
         _gameOver = true;
         broadcast(_players.get(_turn).getUserName() + "'s accusation was correct!");
         broadcast("The game is over!");

         // TODO - end game sequence
      } else
      {
         broadcast(_players.get(_turn).getUserName() + "'s accusation was incorrect!");
         _players.get(_turn).setIsActive(false);
      }

      // TODO - trigger end of turn?
   }


   public boolean isGameOver()
   {

      if (_gameOver)
      {
         return true;
      }

      int activePlayerCount = 0;

      for (Player player : _players)
      {
         if (player.getIsActive())
         {
            activePlayerCount++;
         }
      }

      return activePlayerCount <= 1;
   }


   public void attemptToStart()
   {

      if (_players.size() < 3)
      {
         broadcast("Not enough players to start a game (currently have " + _players.size() + ", need at least 3)");
         return;
      } else
      {
         for (Player player : _players)
         {
            if (!player.getIsReady())
            {
               broadcast("Not all players are ready to start the game...");
               return;
            }
         }

         startGame();
      }
   }


   public void broadcast(String message)
   {

      for (Player player : _players)
      {
         player.sendToClient(message);
      }
   }

   private void startGame()
   {
       _deckController.dealCards(_players);
       startTurn(getNextPlayer());
   }


   private void startTurn(Player currentPlayer)
   {
      // TODO build appropriate message to be received in client app - Sean

      StringBuilder msg = new StringBuilder("startTurn");
      currentPlayer.sendToClient(msg.toString());
   }


   public boolean disproveSequence(Suggestion suggestion)
   {

      int mark = _turn + 1;

      Player playerToCheck;

      while ((mark % _players.size()) != (_turn % _players.size()))
      {
         playerToCheck = _players.get(mark);
         ArrayList<Card> matchingCards = playerToCheck.getPlayerHand().getMatchingCards(suggestion);

         if (matchingCards.size() > 0)
         {
            broadcast(playerToCheck.getCharacterName() + " can disprove the suggestion...");
            sendDisproveRequest(playerToCheck, matchingCards);
            return true;
         } else
         {
            broadcast(playerToCheck.getCharacterName() + " has no matching cards to show...");
            mark++;
         }
      }
      return false;
   }

   private void sendDisproveRequest(Player disprovingPlayer, ArrayList<Card> matchingCards)
   {
      StringBuilder message = new StringBuilder("disproveSuggestion");
      for (Card card : matchingCards)
      {
         message.append(":").append(card.getCardName());
      }
      disprovingPlayer.sendToClient(message.toString());
   }

   public void addPlayer(Player player)
   {

      _players.add(player);
   }


}
