package edu.jhu.teamundecided.clueless.database;

import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.util.HashMap;
import java.util.Set;

public class Database
{
    private HashMap<String, String> _characterNames;
    private HashMap<String, Point> _startingLocations;
    private HashMap<String, String> _roomNames;
    private HashMap<String, String> _weaponNames;

    private static Database _database = null;

    private boolean _running = true;

    public Database()
    {
        setupCharacterNames();
        setupStartingLocations();
        setupRoomNames();
        setupWeaponNames();
    }

    public static Database getInstance()
    {
        if (_database == null)
        {
            _database = new Database();
        }

        return _database;
    }

    public boolean getRunning()
    {
        return _running;
    }

    public void setRunning(boolean value)
    {
        _running = value;
    }

    private void  setupCharacterNames()
    {
        _characterNames = new HashMap<>();
        _characterNames.put("mustard", "Col. Mustard");
        _characterNames.put("scarlett", "Ms. Scarlett");
        _characterNames.put("green", "Mr. Green");
        _characterNames.put("white", "Mr. White");
        _characterNames.put("peacock", "Mrs. Peacock");
        _characterNames.put("plum", "Prof. Plum");
    }

    public String getCharacterName(String key)
    {
        return _characterNames.get(key);
    }

    public HashMap<String, String> getCharacterNames() { return _characterNames; }

    private void setupStartingLocations()
    {
        _startingLocations = new HashMap<>();
        int awayFromEdge = 5;
        int board = 800;
        int spriteSize = 50;
        _startingLocations.put("mustard", new Point(board - awayFromEdge - spriteSize,225));
        _startingLocations.put("scarlett", new Point(525, awayFromEdge));
        _startingLocations.put("green", new Point(225,board - awayFromEdge - spriteSize));
        _startingLocations.put("white", new Point(525,board - awayFromEdge- spriteSize));
        _startingLocations.put("peacock", new Point(awayFromEdge,525));
        _startingLocations.put("plum", new Point(awayFromEdge,225));
    }

    public Point getStartingLocation(String key)
    {
        return _startingLocations.get(key);
    }

    private void setupRoomNames()
    {
        _roomNames = new HashMap<>();
        _roomNames.put("ballroom", "Ballroom");
        _roomNames.put("billiardroom", "Billiard Room");
        _roomNames.put("conservatory", "Conservatory");
        _roomNames.put("diningroom", "Dining Room");
        _roomNames.put("hall", "Hall");
        _roomNames.put("kitchen", "Kitchen");
        _roomNames.put("library", "Library");
        _roomNames.put("lounge", "Lounge");
        _roomNames.put("study", "Study");
    }

    public HashMap<String, String> getRoomNames() { return _roomNames; }

    public String getRoomName(String key) { return _roomNames.get(key); }

    private void setupWeaponNames()
    {
        _weaponNames = new HashMap<>();
        _weaponNames.put("candlestick", "Candlestick");
        _weaponNames.put("knife", "Knife");
        _weaponNames.put("leadpipe", "Lead Pipe");
        _weaponNames.put("revolver", "Revolver");
        _weaponNames.put("rope", "Rope");
        _weaponNames.put("wrench", "Wrench");
    }

    public HashMap<String, String> getWeaponNames() { return _weaponNames; }

    public String getWeaponName(String key) { return _weaponNames.get(key); }

    public class Point
    {
        private int _x;
        private int _y;

        Point(int x, int y)
        {
            _x = x;
            _y = y;
        }

        public int getX()
        {
            return _x;
        }

        public void setX(int _x)
        {
            this._x = _x;
        }

        public int getY()
        {
            return _y;
        }

        public void setY(int _y)
        {
            this._y = _y;
        }
    }
}
