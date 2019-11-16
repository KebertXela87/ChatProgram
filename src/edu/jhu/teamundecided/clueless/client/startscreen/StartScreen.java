package edu.jhu.teamundecided.clueless.client.startscreen;

import edu.jhu.teamundecided.clueless.client.ClientAppController;
import edu.jhu.teamundecided.clueless.server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class StartScreen
{
    private JTextField _ipAddress;
    private JButton _hostButton;
    private JButton _joinButton;
    private JTextField _port;
    private JPanel logo;
    private JPanel _mainPanel;


    public StartScreen(ClientAppController cac)
    {
        _hostButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Start Server
                cac.startServer();

                if(!cac.connect("localhost", 8818))
                {
                    // connection failed
                    System.exit(0);
                }

                cac.askForUserName();
            }
        });

        _joinButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!cac.connect(_ipAddress.getText(), Integer.parseInt(_port.getText())))
                {
                    // connection failed
                    System.exit(0);
                }

                cac.askForUserName();
            }
        });
    }

    public JPanel getMainPanel()
    {
        return _mainPanel;
    }

    private void createUIComponents()
    {
        logo = new Logo();
    }
}
