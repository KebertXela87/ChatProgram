package edu.jhu.teamundecided.clueless.client.dialogs;

import edu.jhu.teamundecided.clueless.client.ClientAppController;
import edu.jhu.teamundecided.clueless.database.Database;

import javax.swing.*;
import java.awt.event.*;

public class suggestDialog extends JDialog
{
    private JPanel contentPane;
    private JButton _suggestButton;
    private JButton _cancelButton;
    private JComboBox<String> _suspectComboBox;
    private JComboBox<String> _weaponComboBox;
    private JLabel _suspectIcon;
    private JLabel _weaponIcon;
    private JLabel _roomIcon;
    private JLabel _roomLabel;

    private String _cardFilepath = "./src/images/cards/";

    private String _suspectSelected = "";
    private String _weaponSelected = "";

    public suggestDialog(ClientAppController cac)
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(_suggestButton);
        _suggestButton.setEnabled(false);

        // Set Room Label
        _roomLabel.setText(Database.getInstance().getRoomName(DialogController.getInstance().getSuggestionRoom()));
        _roomLabel.setHorizontalAlignment(SwingConstants.CENTER);

        _suggestButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onSuggest(cac);
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

    private void onSuggest(ClientAppController cac)
    {
        StringBuilder suggestion = new StringBuilder("suggestion:");
        suggestion.append(_suspectSelected + ":");
        suggestion.append(_weaponSelected + ":");
        suggestion.append(DialogController.getInstance().getSuggestionRoom());

//        cac.writeToServer(suggestion.toString());
        System.out.println(suggestion);

        cac.getClientApp().getSuggestButton().setEnabled(false);
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
        for(String suspect : Database.getInstance().getCharacterNames().values())
        {
            _suspectComboBox.addItem(suspect);
        }
        for(String weapon : Database.getInstance().getWeaponNames().values())
        {
            _weaponComboBox.addItem(weapon);
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

                _suspectIcon.setIcon(new ImageIcon(_cardFilepath + name));

                setSuggestButtonState();
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

                _weaponIcon.setIcon(new ImageIcon(_cardFilepath + name));

                setSuggestButtonState();
            }
        });
    }

    private void setSuggestButtonState()
    {
        if(_suspectComboBox.getSelectedIndex() == 0 || _weaponComboBox.getSelectedIndex() == 0)
        {
            _suggestButton.setEnabled(false);
        }
        else
        {
            _suggestButton.setEnabled(true);
        }
    }

    private void createUIComponents()
    {
        String suggestionRoom = DialogController.getInstance().getSuggestionRoom();
        _roomIcon = new JLabel(new ImageIcon(_cardFilepath + "rooms/" + suggestionRoom + ".jpg"));
    }
}
