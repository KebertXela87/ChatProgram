package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientAppController;

import javax.swing.*;
import java.awt.event.*;

public class accusationResponseDialog extends JDialog
{
    private JPanel contentPane;
    private JButton _closeButton;
    private JLabel _suspectIcon;
    private JLabel _weaponIcon;
    private JLabel _roomIcon;
    private JPanel _responsePanel;
    private JLabel _responseField;

    public accusationResponseDialog(ClientAppController cac)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(_closeButton);

        _closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onClose(cac);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onClose(cac);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onClose(cac);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onClose(ClientAppController cac)
    {
        cac.getClientApp().getEndTurnButton().setEnabled(false);

        dispose();
    }

    private void createUIComponents()
    {
        String filepath = "./src/images/cards/";
        _suspectIcon = new JLabel(new ImageIcon(filepath + "suspects/" + DialogController.getInstance().getAccusationResponseParts("suspect") + ".jpg"));
        _weaponIcon = new JLabel(new ImageIcon(filepath + "weapons/" + DialogController.getInstance().getAccusationResponseParts("weapon") + ".jpg"));
        _roomIcon = new JLabel(new ImageIcon(filepath + "rooms/" + DialogController.getInstance().getAccusationResponseParts("room") + ".jpg"));
        _responseField = new JLabel(DialogController.getInstance().getAccusationResponseParts("response"));
    }
}
