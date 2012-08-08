package cc.rylander.intellij.plugin.polopoly;

import javax.swing.*;

public class PolopolyInstallationConfigGUI {
    public JPanel rootComponent;
    public JLabel urlLabel;
    public JTextField url;
    public JLabel usernameLabel;
    public JTextField username;
    public JLabel passwordLabel;
    public JTextField password;
    private JLabel nameLabel;
    private JTextField name;
    private JComboBox systemChoice;
    private JLabel systemChoiceLabel;

    public PolopolyInstallationConfigGUI() {
        urlLabel.setLabelFor(url);
        usernameLabel.setLabelFor(username);
        passwordLabel.setLabelFor(password);
        nameLabel.setLabelFor(name);
        systemChoiceLabel.setLabelFor(systemChoice);
    }

    public boolean isModified(String _url, String _username, String _password, String _name, int _systemChoice) {
        return (systemChoice.getSelectedIndex() != _systemChoice) ||
                (name.getText() != null ? ! name.getText().equals(_name) : _name != null) ||
                (url.getText() != null ? ! url.getText().equals(_url) : _url != null) ||
                (username.getText() != null ? ! username.getText().equals(_username) : _username != null) ||
                (password.getText() != null ? ! password.getText().equals(_password) : _password != null);
    }
}
