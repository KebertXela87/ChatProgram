package edu.jhu.teamundecided.clueless.database;

import java.util.HashMap;
import java.util.Set;

public class Database
{
    private HashMap<String, String> _characterNames;
    private HashMap<String, Point> _startingLocations;

    private static Database _database = null;

    private boolean _running = true;

    public Database()
    {
        setupCharacterNames();
        setupStartingLocations();
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

    public Set getCharacterKeys()
    {
        return _characterNames.keySet();
    }

    private void setupStartingLocations()
    {
        _startingLocations = new HashMap<>();
        _startingLocations.put("mustard", new Point(50,50));
        _startingLocations.put("scarlett", new Point(150,50));
        _startingLocations.put("green", new Point(250,50));
        _startingLocations.put("white", new Point(50,150));
        _startingLocations.put("peacock", new Point(150,150));
        _startingLocations.put("plum", new Point(250,150));
    }

    public Point getStartingLocation(String key)
    {
        return _startingLocations.get(key);
    }

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
