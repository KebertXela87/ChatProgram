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

      return list.toString();
   }

   public String updateLocations(Player player, String newRoomName)
   {
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
      } while (nextPlayer.isNPC() || !nextPlayer.getIsActive());

      return nextPlayer;
   }

   public void handleEndTurn()
   {
      if (!isGameOver())
      {
         startTurn(getNextPlayer());
      }
      else
      {
         Player winner = getActivePlayers().get(0);
         broadcast(winner.getUserName() + " is the last remaining player.");
         broadcast(winner.getUserName() + " wins!");
         endGame();
      }
   }

   public void handleAccusationCommand(Suggestion accusation)
   {
      Player currentPlayer = _players.get(_turn);
      String accuser = currentPlayer.getUserName();

      broadcast(accuser + " has made an accusation that " + accusation.toString());

      StringBuilder response = new StringBuilder("accuseresponse");

      _gameOver = _deckController.checkAccusation(accusation);

      // Build accusation response message
      response.append(":").append(_gameOver);
      response.append(":").append(accuser);

      for(Card card : _deckController.getCaseFile())
      {
         if(card.getType().equals(Card.CardType.Suspect))
         {
            response.append(":").append(card.getCardName());
         }
         if(card.getType().equals(Card.CardType.Weapon))
         {
            response.append(":").append(card.getCardName());
         }
         if(card.getType().equals(Card.CardType.Room))
         {
            response.append(":").append(card.getCardName());
         }
      }

      if(_gameOver)
      {
         // Game Over, Send Response to everyone
         broadcast(response.toString());
         broadcast("The game is over!");
         endGame();
      }
      else
      {
         // Player guessed wrong. Send response just to Player
         currentPlayer.sendToClient(response.toString());
         // Let everyone else know of incorrect response through the chat system.
         broadcast(accuser + "'s accusation was incorrect!");
         broadcast(accuser + " is now inactive.");

         // Set Player to inactive
         currentPlayer.setIsActive(false);
      }

      handleEndTurn();
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
      ArrayList<Player> activePlayers = new ArrayList<>();
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
      }
      else
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

      broadcast("gamestarted");

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
       boolean move = false;
       boolean suggest = false;
       boolean accuse = true; // Accuse is always going to be TRUE
       boolean endturn = false;

       StringBuilder startTurnMessage = new StringBuilder("startturn");

       // Get Player's Character name
       startTurnMessage.append(":").append(currentPlayer.getCharacterName());

       // Check Position

       // If in starting location
       if (currentPlayer.getLocation().getRoomName().contains("StartLoc"))
       {
           move = true;
       }
       else
       {
           // If in a Hallway
           if (currentPlayer.getLocation().getIsHall())
           {
               move = true;
           }
           else // In a room
           {
               // was moved by another player
               if(currentPlayer.getMoved())
               {
                  suggest = true;
               }

               // Check for possible moves
               if(currentPlayer.getLocation().getPossibleMoves().size() <= 0)
               {
                  endturn = true;
               }
               else
               {
                  move = true;
               }
           }
       }

       startTurnMessage.append(":").append(Boolean.toString(move));
       startTurnMessage.append(":").append(Boolean.toString(suggest));
       startTurnMessage.append(":").append(Boolean.toString(accuse));
       startTurnMessage.append(":").append(Boolean.toString(endturn));

       currentPlayer.sendToClient(startTurnMessage.toString());
   }

   public boolean disproveSequence(Suggestion suggestion)
   {
      // Move suspect to suggested room
      Player suspect = getPlayerFromList(suggestion.getSuspect());
      broadcast(updateLocations(suspect, _players.get(_turn).getLocation().getRoomName()));
      suspect.setMoved(true); // Will allow suspect player to make a suggestion on their next turn without moving first.

      int mark = _turn;

      Player playerToCheck;

      while ((mark = (++mark % _players.size())) != _turn)
      {
         playerToCheck = _players.get(mark);

         if (playerToCheck.isNPC())
         {
            continue;
         }
         else
         {
            ArrayList<Card> matchingCards = playerToCheck.getPlayerHand().getMatchingCards(suggestion);

            if (matchingCards.size() > 0)
            {
               broadcast(Database.getInstance().getCharacterName(playerToCheck.getCharacterName()) + " can disprove the suggestion...");
               sendDisproveRequest(playerToCheck, matchingCards);
               return true;
            }
            else
            {
               broadcast(Database.getInstance().getCharacterName(playerToCheck.getCharacterName()) + " has no matching cards to show...");
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
      _players.get(_turn).sendToClient("revealedCard:" + card);
   }

   public void addPlayer(Player player)
   {
      _players.add(player);
   }

   public void endGame()
   {
      _server.broadcastToAll("Closing connection to server...");
      _server.closeConnections();
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

