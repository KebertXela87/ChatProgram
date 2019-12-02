package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.database.Database;
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
      broadcast(player.getCharacterName() + " moved to the " + newRoomName);

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

      Player nextPlayer;

      do
      {
         _turn = ++_turn % _players.size();
         nextPlayer = _players.get(_turn);
      } while (nextPlayer.isNPC());

      return nextPlayer;
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
         Player winner = getActivePlayers().get(0);
         broadcast(winner.getUserName() + " is the last remaining player.");
         broadcast(winner.getUserName() + " wins!");
         endGame();
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
         endGame();
      } else
      {
         broadcast(_players.get(_turn).getUserName() + "'s accusation was incorrect!");
         broadcast(_players.get(_turn).getUserName() + " is now inactive.");
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

      int activePlayerCount = getActivePlayers().size();

      return activePlayerCount <= 1;
   }


   private ArrayList<Player> getActivePlayers()
   {

      ArrayList<Player> activePlayers = new ArrayList<Player>();
      for (Player player : _players)
      {
         if (player.getIsActive())
         {
            activePlayers.add(player);
         }
      }
      return activePlayers;
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

      rearrangePlayers(); // includes create npcs

      startTurn(getNextPlayer());
   }


   private void rearrangePlayers()
   {

      ArrayList<Player> newPlayers = new ArrayList<>();
      boolean characterFound;

      for (String character : Database.getInstance().getCharacterNames().keySet())
      {
         characterFound = false;

         // search the players for character key
         for (Player player : _players)
         {
            if (player.getCharacterName().equalsIgnoreCase(character))
            {
               newPlayers.add(player);
               characterFound = true;
               break;
            }
         }

         if (!characterFound)
         {
            newPlayers.add(createNPC(character));
         }
      }

      _players = newPlayers;
   }


   private void startTurn(Player currentPlayer)
   {
      // TODO build appropriate message to be received in client app - Sean

      StringBuilder msg = new StringBuilder("startTurn");
      currentPlayer.sendToClient(msg.toString());
   }


   public boolean disproveSequence(Suggestion suggestion)
   {
      // Move suspect to suggested room
      updateLocations(getPlayerFromList(suggestion.getSuspect()), _players.get(_turn).getLocation().getRoomName());

      int mark = _turn + 1;

      Player playerToCheck;

      while ((mark % _players.size()) != _turn)
      {
         playerToCheck = _players.get(mark);

         if (playerToCheck.isNPC())
         {
            continue;
         } else
         {
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


   public void revealCard(String card)
   {
      // TEST CODE
      _players.get(0).sendToClient("revealedCard:" + card);

//       _players.get(_turn).sendToClient("revealedCard:" + card);
   }


   public void addPlayer(Player player)
   {

      _players.add(player);
   }


   public void endGame()
   {

      _server.broadcastToAll("Shutting down now");
      _server.shutdown();
   }


   private Player getPlayerFromList(String name)
   {

      for (Player player : _players)
      {
         if (player.getCharacterName().equalsIgnoreCase(name))
         {
            return player;
         }
      }

      // No player found, this means that the character is an NPC
      return createNPC(name);
   }


   private Player createNPC(String name)
   {

      Player npc = new Player(name, getGameBoard().findRoom(name + "startloc"));
      _players.add(npc);
      return npc;
   }


}

