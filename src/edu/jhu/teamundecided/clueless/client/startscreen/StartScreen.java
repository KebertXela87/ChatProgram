package edu.jhu.teamundecided.clueless.client.startscreen;

import edu.jhu.teamundecided.clueless.client.ClientAppController;

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
                System.out.println("Clicked the HOST button...");
                cac.getFrame().setContentPane(cac.getClientApp().returnMainPanel());
                cac.getFrame().pack();
                cac.getFrame().setVisible(true);
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

    private class Logo extends JPanel
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
}
