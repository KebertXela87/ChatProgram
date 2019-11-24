package edu.jhu.teamundecided.clueless.client.dialogs;

import javax.swing.*;
import java.awt.event.*;

public class notebookDialog extends JDialog
{
    private JPanel contentPane;
    private JButton _closeButton;
    private JPanel _contentPanel;
    private JCheckBox checkBox1;
    private JScrollPane _scrollPane;

    public notebookDialog()
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(_closeButton);

        _closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onClose();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onClose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onClose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onClose()
    {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents()
    {
        _scrollPane = new JScrollPane();
        _scrollPane.setBorder(BorderFactory.createEmptyBorder());
    }

}
