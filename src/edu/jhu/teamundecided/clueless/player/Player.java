package edu.jhu.teamundecided.clueless.player;

import edu.jhu.teamundecided.clueless.gameboard.Room;
import edu.jhu.teamundecided.clueless.server.ClientHandler;

public class Player
{
    private String _characterName = "";
    private Room _currentLocation;
    private String _userName;
    private boolean _isActive;
    private Hand _playerHand;

    public Player()
    {
    }

    public Hand getPlayerHand()
    {
        return _playerHand;
    }

    public String getUserName()
    {
        return _userName;
    }
    public void setUserName(String name)
    {
        _userName = name;
    }

    public String getCharacterName()
    {
        return _characterName;
    }
    public void setCharacterName(String _characterName)
    {
        this._characterName = _characterName;
    }

    public Room getLocation() { return _currentLocation; }
    public void setLocation(Room room) { _currentLocation = room; }
}