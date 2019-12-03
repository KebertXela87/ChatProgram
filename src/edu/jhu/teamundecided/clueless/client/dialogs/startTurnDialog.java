package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientAppController;

import javax.swing.*;
import java.awt.event.*;

public class startTurnDialog extends JDialog
{
    private JPanel contentPane;
    private JButton _startButton;
    private JLabel _characterIcon;

    private boolean _moveState;
    private boolean _suggestState;
    private boolean _accuseState;
    private boolean _endTurnState;

    public startTurnDialog(ClientAppController cac, String startTurnButtonStates)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(_startButton);

        parseButtonStates(startTurnButtonStates);

        _startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onStart(cac);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onStart(cac);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onStart(cac);
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onStart(ClientAppController cac)
    {
        cac.getClientApp().getMoveButton().setEnabled(_moveState);
        cac.getClientApp().getSuggestButton().setEnabled(_suggestState);
        cac.getClientApp().getAccusationButton().setEnabled(_accuseState);
        cac.getClientApp().getEndTurnButton().setEnabled(_endTurnState);

        dispose();
    }

    private void parseButtonStates(String msg)
    {
        String[] booleans = msg.split(":");
        _moveState = Boolean.parseBoolean(booleans[0]);
        _suggestState = Boolean.parseBoolean(booleans[1]);
        _accuseState = Boolean.parseBoolean(booleans[2]);
        _endTurnState = Boolean.parseBoolean(booleans[3]);
    }

    private void createUIComponents()
    {
        _characterIcon = new JLabel(new ImageIcon(DialogController.getInstance().getCharacterIcon()));
    }
}
