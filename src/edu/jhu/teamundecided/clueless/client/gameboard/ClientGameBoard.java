package edu.jhu.teamundecided.clueless.client.gameboard;

import edu.jhu.teamundecided.clueless.database.Database;
import edu.jhu.teamundecided.clueless.database.Point;
import edu.jhu.teamundecided.clueless.database.RoomPoints;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ClientGameBoard extends JPanel
{
    private Image _background;
    private ArrayList<PlayerSprite> sprites;

    public ClientGameBoard()
    {
        File filename = new File("./src/images/gameboard/gameboard.png");
        try
        {
            this._background = ImageIO.read(filename);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        sprites = new ArrayList<>();

        setupSprites();

        // Start Sprite Runnable
        Thread updateSprites = new Thread(new SpriteUpdate());
        updateSprites.start();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (_background != null)
        {
            g.drawImage(_background,0,0,this);
        }

        for (PlayerSprite sprite : sprites)
        {
            sprite.paintSprite(g);
        }
    }

    public Dimension getBackgroundSize()
    {
        Dimension dim = super.getPreferredSize();
        if (_background != null)
        {
            dim = new Dimension(_background.getWidth(this), _background.getHeight(this));
        }
        return dim;
    }

    public void setupSprites()
    {
        for (Object name : Database.getInstance().getCharacterNames().keySet())
        {
            addSprite((String) name);
        }
    }

    public void addSprite(String name)
    {
        PlayerSprite sprite = new PlayerSprite(name);
        Point loc = Database.getInstance().getStartingLocation(name);
        sprite.setX(loc.getX());
        sprite.setY(loc.getY());
        sprite.setInitDest();
        sprites.add(sprite);
        repaint();
    }

    public ArrayList<PlayerSprite> getSprites()
    {
        return sprites;
    }

    private class SpriteUpdate implements Runnable
    {
        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    Thread.sleep(80);
                }
                catch (InterruptedException e) {}

//                Dimension gameboardSize = getBackgroundSize();

                for (PlayerSprite sprite : sprites)
                {
                    sprite.moveSprite();
                }
                repaint();
            }
        }
    }

    public void handleSpriteMovement(String message)
    {
        String[] RoomOccupantLists = message.split("#");

        for(String occupantList : RoomOccupantLists)
        {
            processOccupants(occupantList);
        }
    }

    public void processOccupants(String occupantList)
    {
        String[] list = occupantList.split(":");

        String roomName = list[0];

        int occupantNum = list.length - 1;

        if (occupantNum > 0)
        {
            ArrayList<Point> locPoints = RoomPoints.getInstance().getLocationPoints(roomName, occupantNum);

            int i = 1;
            for (Point point : locPoints)
            {
                for (PlayerSprite sprite : sprites)
                {
                    if (sprite.getName().equals(list[i]))
                    {
                        sprite.setDestX(point.getX());
                        sprite.setDestY(point.getY());
                    }
                }

                i++;
            }
        }
    }
}
