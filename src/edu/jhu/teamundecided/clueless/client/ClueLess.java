package edu.jhu.teamundecided.clueless.client;

import edu.jhu.teamundecided.clueless.client.startscreen.StartScreen;

public class ClueLess
{
    public static void main(String[] args)
    {
        ClientAppController controller = new ClientAppController();

        controller.clientCloseOperation(controller.getFrame());

        // Set Start Screen
        controller.getFrame().setContentPane(new StartScreen(controller).getMainPanel());

        controller.getFrame().setResizable(false);

        controller.getFrame().pack();
        controller.getFrame().setLocationRelativeTo(null);
        controller.getFrame().setVisible(true);
    }
}
