package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientAppController;
import edu.jhu.teamundecided.clueless.database.Database;

import javax.swing.*;
import java.awt.event.*;

public class accuseDialog extends JDialog
{
    private JPanel contentPane;
    private JButton _accuseButton;
    private JButton _cancelButton;
    private JComboBox _suspectComboBox;
    private JComboBox _weaponComboBox;
    private JComboBox _roomComboBox;
    private JLabel _suspectIcon;
    private JLabel _weaponIcon;
    private JLabel _roomIcon;

    private String _filename = "./src/images/cards/";

    private String _suspectSelected = "";
    private String _weaponSelected = "";
    private String _roomSelected = "";

    public accuseDialog(ClientAppController cac)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(_accuseButton);
        _accuseButton.setEnabled(false);

        _accuseButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onAccuse(cac);
            }
        });

        _cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
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

        setupComboBoxes();
        setupComboBoxListeners();
    }

    private void onAccuse(ClientAppController cac)
    {
        StringBuilder accusation = new StringBuilder("accusation:");
        accusation.append(_suspectSelected + ":");
        accusation.append(_weaponSelected + ":");
        accusation.append(_roomSelected);

//        cac.writeToServer(accusation.toString());
        System.out.println(accusation);

        cac.getClientApp().getAccusationButton().setEnabled(false);
        cac.getClientApp().getEndTurnButton().setEnabled(true);

        dispose();
    }

    private void onCancel()
    {
        // add your code here if necessary
        dispose();
    }

    private void setupComboBoxes()
    {
        _suspectComboBox.addItem("Select Suspect...");
        _weaponComboBox.addItem("Select Weapon...");
        _roomComboBox.addItem("Select Room...");
        for(String suspect : Database.getInstance().getCharacterNames().values())
        {
            _suspectComboBox.addItem(suspect);
        }
        for(String weapon : Database.getInstance().getWeaponNames().values())
        {
            _weaponComboBox.addItem(weapon);
        }
        for(String room : Database.getInstance().getRoomNames().values())
        {
            _roomComboBox.addItem(room);
        }
    }

    private void setupComboBoxListeners()
    {
        _suspectComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String name = "unknown.png";
                for(String key : Database.getInstance().getCharacterNames().keySet())
                {
                    if(_suspectComboBox.getSelectedItem().toString().equals(Database.getInstance().getCharacterName(key)))
                    {
                        name = "suspects/" + key + ".jpg";
                        _suspectSelected = key;
                    }
                }

                _suspectIcon.setIcon(new ImageIcon(_filename + name));

                setAccuseButtonState();
            }
        });

        _weaponComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String name = "unknown.png";
                for(String key : Database.getInstance().getWeaponNames().keySet())
                {
                    if(_weaponComboBox.getSelectedItem().toString().equals(Database.getInstance().getWeaponName(key)))
                    {
                        name = "weapons/" + key + ".jpg";
                        _weaponSelected = key;
                    }
                }

                _weaponIcon.setIcon(new ImageIcon(_filename + name));

                setAccuseButtonState();
            }
        });

        _roomComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String name = "unknown.png";
                for(String key : Database.getInstance().getRoomNames().keySet())
                {
                    if(_roomComboBox.getSelectedItem().toString().equals(Database.getInstance().getRoomName(key)))
                    {
                        name = "rooms/" + key + ".jpg";
                        _roomSelected = key;
                    }
                }

                _roomIcon.setIcon(new ImageIcon(_filename + name));

                setAccuseButtonState();
            }
        });
    }

    private void setAccuseButtonState()
    {
        if(_suspectComboBox.getSelectedIndex() == 0 || _weaponComboBox.getSelectedIndex() == 0 || _roomComboBox.getSelectedIndex() == 0)
        {
            _accuseButton.setEnabled(false);
        }
        else
        {
            _accuseButton.setEnabled(true);
        }
    }
}
