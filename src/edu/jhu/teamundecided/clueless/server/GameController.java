package edu.jhu.teamundecided.clueless.server;

import edu.jhu.teamundecided.clueless.database.Database;
import edu.jhu.teamundecided.clueless.deck.DeckController;
import edu.jhu.teamundecided.clueless.gameboard.GameBoard;

public class GameController
{
    private static GameController _gameController = null;

    private Database db = Database.getInstance();

    private GameBoard _gameboard;
    private DeckController _deckController;
    private final int _turn;

    public GameController()
    {
        _gameboard = new GameBoard();
        _deckController = new DeckController();
        _turn = 0;
    }

    public static GameController getInstance()
    {
        if (_gameController == null)
        {
            _gameController = new GameController();
        }

        return _gameController;
    }

    public void broadcast(String message)
    {
        for (ClientHandler client : db.getGameServer().getCients())
        {
            client.writeToClient(message);
        }
    }

    public void placePlayers()
    {
        for (ClientHandler client : db.getGameServer().getCients())
        {
            //TODO
        }
    }
}
