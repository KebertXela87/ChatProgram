package edu.jhu.teamundecided.clueless.client.startscreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Logo extends JPanel
{
    private Image _background;

    public Logo()
    {
        File filename = new File("./src/images/startscreen.png");
        try
        {
            this._background = ImageIO.read(filename);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (_background != null)
        {
            g.drawImage(_background,0,0,this);
        }
    }
}
