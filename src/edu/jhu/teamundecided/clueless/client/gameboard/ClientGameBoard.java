package edu.jhu.teamundecided.clueless.client.gameboard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

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

    public void setupSprites()
    {
        addSprite("colmustard");
    }

    public void addSprite(String name)
    {
        PlayerSprite sprite = new PlayerSprite(name);
        sprite.setX(100);
        sprite.setY(100);
        sprites.add(sprite);
        repaint();
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(800, 800);
        frame.setContentPane(new ClientGameBoard());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
