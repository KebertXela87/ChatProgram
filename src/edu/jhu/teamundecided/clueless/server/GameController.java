package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.deck.DeckController;
import edu.jhu.teamundecided.clueless.gameboard.GameBoard;

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
}
