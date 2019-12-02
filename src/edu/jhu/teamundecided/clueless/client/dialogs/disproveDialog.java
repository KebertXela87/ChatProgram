package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientAppController;

import javax.swing.*;
import java.awt.event.*;
import java.util.Enumeration;

public class disproveDialog extends JDialog
{
    private JPanel contentPane;
    private JPanel _disprovePanel;
    private JButton _disproveButton;

    private String _disproveSelection = "";

    public disproveDialog(ClientAppController cac)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(_disproveButton);
        _disproveButton.setEnabled(false);

        _disproveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                onDisprove(cac);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        for (Enumeration<AbstractButton> buttons = DialogController.getInstance().getDisproveButtonGroup().getElements(); buttons.hasMoreElements();)
        {
            AbstractButton button = buttons.nextElement();

            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _disproveSelection = button.getName();
                    _disproveButton.setEnabled(true);
                }
            });
        }
    }

    private void onDisprove(ClientAppController cac)
    {
        StringBuilder reveal = new StringBuilder("revealCard:");
        reveal.append(_disproveSelection);

        cac.writeToServer(reveal.toString());
        System.out.println(reveal.toString());

        cac.getClientApp().getSuggestButton().setEnabled(false);
        cac.getClientApp().getAccusationButton().setEnabled(true);
        cac.getClientApp().getEndTurnButton().setEnabled(true);

        dispose();
    }

    private void onCancel()
    {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents()
    {
        _disprovePanel = DialogController.getInstance().getDisprovePanel();
    }
}
