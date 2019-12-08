package edu.jhu.teamundecided.clueless.gameboard;

import edu.jhu.teamundecided.clueless.player.Player;

import java.util.*;

public class GameBoard
{
    // Starting Rooms
    public Room Scarlett;
    public Room Mustard;
    public Room Green;
    public Room Peacock;
    public Room White;
    public Room Plum;
    public Room blank;
    public Room study;
    public Room hall;
    public Room lounge;
    public Room library;
    public Room billiardroom;
    public Room diningroom;
    public Room conservatory;
    public Room ballroom;
    public Room kitchen;
    public Room hallway_1;
    public Room hallway_2;
    public Room hallway_3;
    public Room hallway_4;
    public Room hallway_5;
    public Room hallway_6;
    public Room hallway_7;
    public Room hallway_8;
    public Room hallway_9;
    public Room hallway_10;
    public Room hallway_11;
    public Room hallway_12;

    public ArrayList<Room> rooms;

    public GameBoard()
    {
        // Starting Rooms
        Scarlett = new Room("ScarlettStartLoc", false);
        Mustard = new Room("MustardStartLoc", false);
        Green = new Room("GreenStartLoc", false);
        Peacock = new Room("PeacockStartLoc", false);
        White = new Room("WhiteStartLoc", false);
        Plum = new Room("PlumStartLoc", false);

        blank = new Room("", false);

        // Rooms
        study = new Room("study", false);
        hall = new Room("hall", false);
        lounge = new Room("lounge", false);
        library = new Room("library", false);
        billiardroom = new Room("billiardroom", false);
        diningroom = new Room("diningroom", false);
        conservatory = new Room("conservatory", false);
        ballroom = new Room("ballroom", false);
        kitchen = new Room("kitchen", false);

        // Hallways
        hallway_1 = new Room("hallway_1", true);
        hallway_2 = new Room("hallway_2", true);
        hallway_3 = new Room("hallway_3", true);
        hallway_4 = new Room("hallway_4", true);
        hallway_5 = new Room("hallway_5", true);
        hallway_6 = new Room("hallway_6", true);
        hallway_7 = new Room("hallway_7", true);
        hallway_8 = new Room("hallway_8", true);
        hallway_9 = new Room("hallway_9", true);
        hallway_10 = new Room("hallway_10", true);
        hallway_11 = new Room("hallway_11", true);
        hallway_12 = new Room("hallway_12", true);

        // Set Adjacent Rooms
        Scarlett.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_2)));
        Mustard.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_5)));
        Green.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_11)));
        Peacock.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_8)));
        White.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_12)));
        Plum.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_3)));

        study.setAdjacentRooms(new ArrayList<>(Arrays.asList(kitchen, hallway_1, hallway_3)));
        hall.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_1, hallway_2, hallway_4)));
        lounge.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_2, hallway_5, conservatory)));
        library.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_3, hallway_6, hallway_8)));
        billiardroom.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_4, hallway_6, hallway_7, hallway_9)));
        diningroom.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_5, hallway_7, hallway_10)));
        conservatory.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_8, hallway_11, lounge)));
        ballroom.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_9, hallway_11, hallway_12)));
        kitchen.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_10, hallway_12, study)));
        hallway_1.setAdjacentRooms(new ArrayList<>(Arrays.asList(study, hall)));
        hallway_2.setAdjacentRooms(new ArrayList<>(Arrays.asList(hall, lounge)));
        hallway_3.setAdjacentRooms(new ArrayList<>(Arrays.asList(study, library)));
        hallway_4.setAdjacentRooms(new ArrayList<>(Arrays.asList(hall, billiardroom)));
        hallway_5.setAdjacentRooms(new ArrayList<>(Arrays.asList(lounge, diningroom)));
        hallway_6.setAdjacentRooms(new ArrayList<>(Arrays.asList(library, billiardroom)));
        hallway_7.setAdjacentRooms(new ArrayList<>(Arrays.asList(billiardroom, diningroom)));
        hallway_8.setAdjacentRooms(new ArrayList<>(Arrays.asList(library, conservatory)));
        hallway_9.setAdjacentRooms(new ArrayList<>(Arrays.asList(billiardroom, ballroom)));
        hallway_10.setAdjacentRooms(new ArrayList<>(Arrays.asList(diningroom, kitchen)));
        hallway_11.setAdjacentRooms(new ArrayList<>(Arrays.asList(conservatory, ballroom)));
        hallway_12.setAdjacentRooms(new ArrayList<>(Arrays.asList(ballroom, kitchen)));

        rooms = new ArrayList<>(Arrays.asList(Scarlett, Mustard, Green, Peacock, White, Plum,
                study, hall, lounge, library, billiardroom, diningroom, conservatory, ballroom,
                kitchen, hallway_1, hallway_2, hallway_3, hallway_4, hallway_5, hallway_6, hallway_7,
                hallway_8, hallway_9, hallway_10, hallway_11, hallway_12));
    }

    public boolean movePlayer(Player player, Room room)
    {
        // remove player from room only if player already exists in a room
        if (player.getLocation() != null)
        {
            player.getLocation().removeOccupant(player);
        }

        room.addOccupant(player);

        player.setLocation(room);

        System.out.println(player.getCharacterName() + " moved to " + room.getRoomName());

        return true;
    }


    public Room findRoom(String roomName)
    {

        for (Room room : rooms)
        {
            if (room.getRoomName().equalsIgnoreCase(roomName))
            {
                System.out.println("Found room : " + roomName);
                return room;
            }
        }
        System.out.println("Did not find room : " + roomName);
        return null;
    }
}