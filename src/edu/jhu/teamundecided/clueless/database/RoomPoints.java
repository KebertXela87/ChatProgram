package edu.jhu.teamundecided.clueless.database;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomPoints
{
    private Point roomCP; // Room Center Point
    private Point spriteCP; // Sprite Center Point

    private static RoomPoints _roomPoints = null;

    private HashMap<String, Point> _roomOffsets;

    private ArrayList<Point> oneOccupant;
    private ArrayList<Point> twoOccupant;
    private ArrayList<Point> threeOccupant;
    private ArrayList<Point> fourOccupant;
    private ArrayList<Point> fiveOccupant;
    private ArrayList<Point> sixOccupant;

    public RoomPoints()
    {
        roomCP = new Point(100,100);
        spriteCP = new Point(25,25);

        setupRoomOffsets();
        setupOccupantPositions();
    }

    public static RoomPoints getInstance()
    {
        if(_roomPoints == null)
        {
            _roomPoints = new RoomPoints();
        }
        return _roomPoints;
    }

    private void setupRoomOffsets()
    {
        _roomOffsets = new HashMap<>();

        _roomOffsets.put("study", new Point(0,0));
        _roomOffsets.put("hall", new Point(300, 0));
        _roomOffsets.put("lounge", new Point(600,0));

        _roomOffsets.put("library", new Point(0,300));
        _roomOffsets.put("billiardroom", new Point(300,300));
        _roomOffsets.put("diningroom", new Point(600,300));

        _roomOffsets.put("conservatory", new Point(0,600));
        _roomOffsets.put("ballroom", new Point(300,600));
        _roomOffsets.put("kitchen", new Point(600,600));
    }

    private void setupOccupantPositions()
    {
        //one
        oneOccupant = new ArrayList<>();
        oneOccupant.add(new Point(roomCP.getX() - spriteCP.getX(), roomCP.getY() - spriteCP.getY()));

        //two
        twoOccupant = new ArrayList<>();
        twoOccupant.add(new Point(roomCP.getX() - 53, roomCP.getY() - 25));
        twoOccupant.add(new Point(roomCP.getX() + 3, roomCP.getY() - 25));

        //three
        threeOccupant = new ArrayList<>();
        threeOccupant.add(new Point(roomCP.getX() - 25, roomCP.getY() - 53));
        threeOccupant.add(new Point(roomCP.getX() - 53, roomCP.getY() + 3));
        threeOccupant.add(new Point(roomCP.getX() + 3, roomCP.getY() + 3));

        //four
        fourOccupant = new ArrayList<>();
        fourOccupant.add(new Point(roomCP.getX() - 53, roomCP.getY() - 53));
        fourOccupant.add(new Point(roomCP.getX() + 3, roomCP.getY() - 53));
        fourOccupant.add(new Point(roomCP.getX() - 53, roomCP.getY() + 3));
        fourOccupant.add(new Point(roomCP.getX() + 3, roomCP.getY() + 3));

        //five
        fiveOccupant = new ArrayList<>();
        fiveOccupant.add(new Point(roomCP.getX() - 53, roomCP.getY() - 53));
        fiveOccupant.add(new Point(roomCP.getX() + 53, roomCP.getY() - 53));
        fiveOccupant.add(new Point(roomCP.getX() - 80, roomCP.getY() + 3));
        fiveOccupant.add(new Point(roomCP.getX() - 25, roomCP.getY() + 3));
        fiveOccupant.add(new Point(roomCP.getX() + 30, roomCP.getY() + 3));

        //six
        sixOccupant = new ArrayList<>();
        sixOccupant.add(new Point(roomCP.getX() - 80, roomCP.getY() - 53));
        sixOccupant.add(new Point(roomCP.getX() - 25, roomCP.getY() - 53));
        sixOccupant.add(new Point(roomCP.getX() + 30, roomCP.getY() - 53));
        sixOccupant.add(new Point(roomCP.getX() - 80, roomCP.getY() + 3));
        sixOccupant.add(new Point(roomCP.getX() - 25, roomCP.getY() + 3));
        sixOccupant.add(new Point(roomCP.getX() + 30, roomCP.getY() + 3));
    }

    private ArrayList<Point> getOccupantPositionList(int number)
    {
        switch(number)
        {
            case 1:
                return oneOccupant;
            case 2:
                return twoOccupant;
            case 3:
                return threeOccupant;
            case 4:
                return fourOccupant;
            case 5:
                return fiveOccupant;
            case 6:
                return sixOccupant;
        }
        return null;
    }

    public ArrayList<Point> getLocationPoints(String roomName, int occupantNum)
    {
        ArrayList<Point> updateLocations = new ArrayList<>();

        Point roomOffset = _roomOffsets.get(roomName);

        System.out.println("Occupant Number: " + occupantNum);

        for(Point point : getOccupantPositionList(occupantNum))
        {
            updateLocations.add(new Point(point.getX() + roomOffset.getX(), point.getY() + roomOffset.getY()));
        }

        return updateLocations;
    }
}
