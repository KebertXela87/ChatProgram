package edu.jhu.teamundecided.clueless.client.gameboard;

import edu.jhu.teamundecided.clueless.database.Database;

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
        Database.Point loc = Database.getInstance().getStartingLocation(name);
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
                    //TODO update this demo code
                    if (sprite.getX() != sprite.getDestX())
                    {
                        sprite.move();
                    }
                }
                repaint();
            }
        }
    }
}
