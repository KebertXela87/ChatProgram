package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.deck.DeckController;
import edu.jhu.teamundecided.clueless.gameboard.GameBoard;
import edu.jhu.teamundecided.clueless.gameboard.Room;
import edu.jhu.teamundecided.clueless.player.Player;

public class GameController
{
    private static GameController _gameController = null;

    private Server _server;
    private GameBoard _gameboard;
    private DeckController _deckController;
    private final int _turn;

    public GameController(Server server)
    {
        _server = server;
        _gameboard = new GameBoard();
        _deckController = new DeckController();
        _turn = 0;
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
}
