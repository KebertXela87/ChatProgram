package edu.jhu.teamundecided.clueless.client;

import edu.jhu.teamundecided.clueless.client.startscreen.StartScreen;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClueLess
{
    public static void main(String[] args)
    {
        ClientAppController controller = new ClientAppController();

        controller.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controller.getFrame().addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                controller.logout();
            }
        });

        // Set Start Screen
        controller.getFrame().setContentPane(new StartScreen(controller).getMainPanel());

        controller.getFrame().setResizable(false);

        controller.getFrame().pack();
        controller.getFrame().setLocationRelativeTo(null);
        controller.getFrame().setVisible(true);
    }
}
