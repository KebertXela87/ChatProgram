package edu.jhu.teamundecided.clueless.client.gameboard;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PlayerSprite
{
    private final String _name;
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int _destX;
    private int _destY;

    private Image image;
    private int _width;
    private int _height;

    private String _spritesFolder = "./src/images/gameboard/sprites/small/";

    public PlayerSprite(String name)
    {
        this._name = name;

        try
        {
            image = ImageIO.read(new File(_spritesFolder + name + "_small.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        _width = image.getWidth(null);
        _height = image.getHeight(null);

        // set speed
        dx = 1;
        dy = 1;
    }

    public String getName()
    {
        return _name;
    }

    public void paintSprite( Graphics g )
    {
        if (image != null)
        {
            g.drawImage(image, x, y, null);
        }
    }

    public void moveSprite()
    {
        if (x != _destX || y != _destY)
        {
            if(x < _destX)
            {
                x += dx;
            }
            if(x > _destX)
            {
                x -= dx;
            }
            if(y < _destY)
            {
                y += dy;
            }
            if(y > _destY)
            {
                y -= dy;
            }
        }
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getDx()
    {
        return dx;
    }

    public void setDx(int dx)
    {
        this.dx = dx;
    }

    public int getDy()
    {
        return dy;
    }

    public void setDy(int dy)
    {
        this.dy = dy;
    }

    public int getDestX()
    {
        return _destX;
    }

    public void setDestX(int _destX)
    {
        this._destX = _destX;
    }

    public int getDestY()
    {
        return _destY;
    }

    public void setDestY(int _destY)
    {
        this._destY = _destY;
    }

    public void setInitDest()
    {
        this._destX = x;
        this._destY = y;
    }
}
