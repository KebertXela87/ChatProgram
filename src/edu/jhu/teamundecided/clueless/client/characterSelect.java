package edu.jhu.teamundecided.clueless.client;

import edu.jhu.teamundecided.clueless.database.Database;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class characterSelect
{
    private JPanel _selectionPanel;
    private JButton _green;
    private JButton _peacock;
    private JButton _white;
    private JButton _plum;
    private JButton _scarlett;
    private JButton _mustard;
    private JLabel _selectLabel;
    private JPanel _buttonPanel;

    private HashMap<String, JButton> _buttons;

    public characterSelect(ClientAppController cac)
    {
        for (String key : Database.getInstance().getCharacterNames().keySet())
        {
            _buttons.get(key).addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    cac.writeToServer("character:" + key);
                    cac.startClient();
                }
            });
        }

        Thread selecting = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    Database.getInstance().getLock().lock();
                    if(Database.getInstance().getDisabledCharacters().size() > 0)
                    {
                        for (String key : Database.getInstance().getDisabledCharacters())
                        {
                            if (_buttons.get(key).isEnabled())
                            {
                                System.out.println("Disabling button = " + key);
                                _buttons.get(key).setEnabled(false);
                            }
                        }
                    }
                    Database.getInstance().getLock().unlock();
                }
            }
        });

        selecting.start();
    }

    private void createUIComponents()
    {
        BufferedImage scarlett = null;
        BufferedImage mustard = null;
        BufferedImage green = null;
        BufferedImage peacock = null;
        BufferedImage white = null;
        BufferedImage plum = null;

        try
        {
            scarlett = ImageIO.read(new File("./src/images/gameboard/sprites/characterSelect/scarlett_cs.png"));
            mustard = ImageIO.read(new File("./src/images/gameboard/sprites/characterSelect/mustard_cs.png"));
            green = ImageIO.read(new File("./src/images/gameboard/sprites/characterSelect/green_cs.png"));
            peacock = ImageIO.read(new File("./src/images/gameboard/sprites/characterSelect/peacock_cs.png"));
            white = ImageIO.read(new File("./src/images/gameboard/sprites/characterSelect/white_cs.png"));
            plum = ImageIO.read(new File("./src/images/gameboard/sprites/characterSelect/plum_cs.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        _green = new JButton(new ImageIcon(green));
        _peacock = new JButton(new ImageIcon(peacock));
        _white = new JButton(new ImageIcon(white));
        _plum = new JButton(new ImageIcon(plum));
        _scarlett = new JButton(new ImageIcon(scarlett));
        _mustard = new JButton(new ImageIcon(mustard));

        _buttons = new HashMap<>();
        _buttons.put("mustard", _mustard);
        _buttons.put("scarlett", _scarlett);
        _buttons.put("plum", _plum);
        _buttons.put("white", _white);
        _buttons.put("peacock", _peacock);
        _buttons.put("green", _green);

    }

    public JPanel getSelectionPanel()
    {
        return _selectionPanel;
    }
}
