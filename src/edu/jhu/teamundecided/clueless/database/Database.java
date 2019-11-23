package edu.jhu.teamundecided.clueless.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Database
{
    private LinkedHashMap<String, String> _characterNames;
    private HashMap<String, Point> _startingLocations;
    private LinkedHashMap<String, String> _roomNames;
    private LinkedHashMap<String, String> _weaponNames;
    private HashMap<String, String> _hallwayDirections;

    private ArrayList<String> _disabledCharacters;

    private static Database _database = null;

    private ReentrantLock lock = new ReentrantLock();

    public Database()
    {
        setupCharacterNames();
        setupStartingLocations();
        setupRoomNames();
        setupWeaponNames();
        setupHallwayDirections();
        _disabledCharacters = new ArrayList<>();
    }

    public ReentrantLock getLock() { return lock; }

    public static Database getInstance()
    {
        if (_database == null)
        {
            _database = new Database();
        }

        return _database;
    }

    private void  setupCharacterNames()
    {
        _characterNames = new LinkedHashMap<>();
        _characterNames.put("green", "Mr. Green");
        _characterNames.put("mustard", "Col. Mustard");
        _characterNames.put("peacock", "Mrs. Peacock");
        _characterNames.put("plum", "Prof. Plum");
        _characterNames.put("scarlett", "Ms. Scarlett");
        _characterNames.put("white", "Mr. White");
    }

    public String getCharacterName(String key)
    {
        return _characterNames.get(key);
    }

    public LinkedHashMap<String, String> getCharacterNames() { return _characterNames; }

    private void setupStartingLocations()
    {
        _startingLocations = new LinkedHashMap<>();
        int awayFromEdge = 5;
        int board = 800;
        int spriteSize = 50;
        _startingLocations.put("green", new Point(225,board - awayFromEdge - spriteSize));
        _startingLocations.put("mustard", new Point(board - awayFromEdge - spriteSize,225));
        _startingLocations.put("peacock", new Point(awayFromEdge,525));
        _startingLocations.put("plum", new Point(awayFromEdge,225));
        _startingLocations.put("scarlett", new Point(525, awayFromEdge));
        _startingLocations.put("white", new Point(525,board - awayFromEdge- spriteSize));
    }

    public Point getStartingLocation(String key)
    {
        return _startingLocations.get(key);
    }

    private void setupRoomNames()
    {
        _roomNames = new LinkedHashMap<>();
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

    public LinkedHashMap<String, String> getRoomNames() { return _roomNames; }

    public String getRoomName(String key) { return _roomNames.get(key); }

    private void setupWeaponNames()
    {
        _weaponNames = new LinkedHashMap<>();
        _weaponNames.put("candlestick", "Candlestick");
        _weaponNames.put("knife", "Knife");
        _weaponNames.put("leadpipe", "Lead Pipe");
        _weaponNames.put("revolver", "Revolver");
        _weaponNames.put("rope", "Rope");
        _weaponNames.put("wrench", "Wrench");
    }

    public LinkedHashMap<String, String> getWeaponNames() { return _weaponNames; }

    public String getWeaponName(String key) { return _weaponNames.get(key); }

    public synchronized ArrayList<String> getDisabledCharacters()
    {
        return _disabledCharacters;
    }

    private void setupHallwayDirections()
    {
        _hallwayDirections = new HashMap<>();
        _hallwayDirections.put("ballroom", "hallway_9,north:hallway_11,west:hallway_12,east");
        _hallwayDirections.put("billiardroom", "hallway_4,north:hallway_6,west:hallway_7,east:hallway_9,south");
        _hallwayDirections.put("conservatory", "hallway_8,north:hallway_11,east");
        _hallwayDirections.put("diningroom", "hallway_5,north:hallway_7,west:hallway_10,south");
        _hallwayDirections.put("hall", "hallway_1,west:hallway_2,east:hallway_4,south");
        _hallwayDirections.put("kitchen", "hallway_10,north:hallway_12,west");
        _hallwayDirections.put("library", "hallway_3,north:hallway_6,east:hallway_8,south");
        _hallwayDirections.put("lounge", "hallway_2,west:hallway_5,south");
        _hallwayDirections.put("study", "hallway_1,east:hallway_3,south");
        _hallwayDirections.put("ScarlettStartLoc", "hallway_2,south");
        _hallwayDirections.put("MustardStartLoc", "hallway_5,west");
        _hallwayDirections.put("GreenStartLoc", "hallway_11,north");
        _hallwayDirections.put("PeacockStartLoc", "hallway_8,east");
        _hallwayDirections.put("WhiteStartLoc", "hallway_12,north");
        _hallwayDirections.put("PlumStartLoc", "hallway_3,east");
    }

    public String getHallwayDirections(String key)
    {
        return _hallwayDirections.get(key);
    }
}
