package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.deck.Card;
import edu.jhu.teamundecided.clueless.deck.DeckController;
import edu.jhu.teamundecided.clueless.deck.Suggestion;
import edu.jhu.teamundecided.clueless.gameboard.GameBoard;
import edu.jhu.teamundecided.clueless.gameboard.Room;
import edu.jhu.teamundecided.clueless.gameboard.Weapon;
import edu.jhu.teamundecided.clueless.player.Hand;
import edu.jhu.teamundecided.clueless.player.Player;

import java.util.ArrayList;

public class GameController
{
    private static GameController _gameController = null;

    private Server _server;
    private GameBoard _gameboard;
    private DeckController _deckController;
    private int _turn;
    private int _mark;
    private boolean _gameOver;
    private ArrayList<Player> _players;

    public GameController(Server server)
    {
        _server = server;
        _gameboard = new GameBoard();
        _deckController = new DeckController();
        _turn = 0;
        _mark = 1;
        _gameOver = false;
        _players = getPlayers();
    }

    public static GameController getInstance(Server server)
    {
        if (_gameController == null)
        {
            _gameController = new GameController(server);
        }

        return _gameController;
    }

    public Server getGameServer() { return _server; }
    public GameBoard getGameBoard() { return _gameboard; }

    public String getSelectedCharacters()
    {
        StringBuilder list = new StringBuilder();
        for(ClientHandler client : _server.getCients())
        {
            list.append(":").append(client.getPlayer().getCharacterName());
        }
        return list.toString();
    }

    public String updateLocations(Player player, String newRoomName)
    {
        System.out.println("Creating moveSprites message...");
        StringBuilder moveSprites = new StringBuilder("moveSprites:");

        Room oldRoom = player.getLocation();
        Room newRoom = _gameboard.findRoom(newRoomName);

        // Update Player Object Location
        _gameboard.movePlayer(player, newRoom); // GameBoard Method

        moveSprites.append(oldRoom.getRoomName());

        for(Player occupant : oldRoom.getOccupants())
        {
            moveSprites.append(":").append(occupant.getCharacterName());
        }

        moveSprites.append("#").append(newRoom.getRoomName());

        for(Player occupant : newRoom.getOccupants())
        {
            moveSprites.append(":").append(occupant.getCharacterName());
        }

        return moveSprites.toString();
    }

    public ArrayList<Player> getPlayers(){
        ArrayList<Player> players = new ArrayList<Player>();
        ArrayList<ClientHandler> handlers = _server.getCients();
        for (ClientHandler handler : handlers) {
            players.add(handler.getPlayer());
        }
        return players;
    }

    public Player getNextTurn(){
        _players = getPlayers();
        int playerCount = _players.size();
        _turn++;
        _turn %= playerCount;
        return _players.get(_turn);
    }

    public void handleEndTurn(){
        Player next = getNextTurn();
        //TODO next.executeTurn();
    }

    public void handleSuggestionCommand(Suggestion sug){
        _server.broadcastToAll(_players.get(_turn).getUserName() + " suggested " + sug.toString());

        ArrayList<Card> suggestedCards = sug.getSuggestedCards();
        _mark = _turn + 1; //start checking with next player
        ArrayList<Card> possibleCards = new ArrayList<Card>();
        while (_mark != _turn){
            Player possible = _players.get(_mark);
            ArrayList<Card> hand = possible.getPlayerHand().getCards();
            for (Card card : suggestedCards) {
                for (Card check : hand) {
                    if(check.getCardName().equalsIgnoreCase(card.getCardName())){
                        possibleCards.add(check);
                    }
                }
            }

            if(possibleCards.isEmpty()){
                _mark ++;
                _mark %= _players.size();
            }
            else break;
        }

        if(!possibleCards.isEmpty()){
            _server.broadcastToAll(_players.get(_mark).getUserName() + " can disprove the suggestion.");
            //TODO _players.get(mark). disproveSuggestion(possibleCards)
        }
        else{
            _server.broadcastToAll("Nobody disproved the suggestion.");
        }
    }

    public void handleDisproven(Card card){
        _server.broadcastToAll(_players.get(_mark).getUserName() + " has shown a card to " + _players.get(_turn).getUserName());
        ClientHandler currentHandler = getClientHandler(_players.get(_turn));
        currentHandler.writeToClient(_players.get(_mark).getUserName() + " has shown you the " + card.getCardName());
    }

    public void handleAccusationCommand(Suggestion accusation){
        _server.broadcastToAll(_players.get(_turn).getUserName() + " has made an accusation that " + accusation.toString());

        if(_deckController.checkAccusation(accusation)){
            _gameOver = true;
            _server.broadcastToAll(_players.get(_turn).getUserName() + "'s accusation was correct!");
            _server.broadcastToAll("The game is over!");
        }
        else{
            _server.broadcastToAll(_players.get(_turn).getUserName() + "'s accusation was incorrect!");
        }
    }

    public boolean isGameOver(){
        return _gameOver;
    }

    private ClientHandler getClientHandler(Player player){
        for (ClientHandler handler : _server.getCients()) {
            if(handler.getPlayer().getCharacterName().equalsIgnoreCase(player.getCharacterName())){
                return handler;
            }
        }
        return null;
    }
}
