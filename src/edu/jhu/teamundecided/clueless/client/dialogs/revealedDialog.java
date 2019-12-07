package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientAppController;

import javax.swing.*;
import java.awt.event.*;

public class revealedDialog extends JDialog
{
    private JPanel contentPane;
    private JButton _closeButton;
    private JPanel _revealedPanel;
    private JLabel _revealedCard;
    private JButton buttonCancel;

    public revealedDialog(ClientAppController cac)
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
        cac.getClientApp().getAccusationButton().setEnabled(true);
        cac.getClientApp().getEndTurnButton().setEnabled(true);

        dispose();
    }

    private void createUIComponents()
    {
        _revealedCard = new JLabel(new ImageIcon(DialogController.getInstance().getRevealedCard()));
    }
}
