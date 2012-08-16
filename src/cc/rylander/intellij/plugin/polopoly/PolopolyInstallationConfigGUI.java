package cc.rylander.intellij.plugin.polopoly;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class PolopolyInstallationConfigGUI implements ItemListener {
    JPanel rootComponent;
    JLabel urlLabel;
    JTextField url;
    JLabel usernameLabel;
    JTextField username;
    JLabel passwordLabel;
    JTextField password;
    JLabel activeLabel;
    JComboBox active;
    PolopolyPlugin plugin;
    boolean resetting;
    List<HostConfig> systems;
    int chosenSystem;


    PolopolyInstallationConfigGUI(PolopolyPlugin plugin) {
        active.addItemListener(this);
        this.plugin = plugin;
    }

    boolean isModified(List<HostConfig> systems, int chosenSystem) {
        if (systems.size() != active.getItemCount()) return true;

        if (chosenSystem != active.getSelectedIndex()) return true;

        int guiSel = active.getSelectedIndex();
        if (!systems.get(guiSel).url.equals(url.getText())) return true;
        if (!systems.get(guiSel).username.equals(username.getText())) return true;
        if (!systems.get(guiSel).password.equals(password.getText())) return true;

        int idx = 0;
        for (HostConfig config : systems) {
            if (config == active.getSelectedItem()) continue;
            if (!systems.get(idx).url.equals(config.url)) return true;
            if (!systems.get(idx).username.equals(config.username)) return true;
            if (!systems.get(idx).password.equals(config.password)) return true;
            idx++;
        }
        return false;
    }

    List<HostConfig> getSystems() {
        chosenSystem = active.getSelectedIndex();
        systems.get(active.getSelectedIndex()).url = url.getText();
        systems.get(active.getSelectedIndex()).username = username.getText();
        systems.get(active.getSelectedIndex()).password = password.getText();
        return copy(systems);
    }

    int getChosenSystem() {
        return active.getSelectedIndex();
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getSource() == active && itemEvent.getStateChange() == ItemEvent.SELECTED && !resetting) {
            systems.get(chosenSystem).url = url.getText();
            systems.get(chosenSystem).username = username.getText();
            systems.get(chosenSystem).password = password.getText();
            chosenSystem = active.getSelectedIndex();
            setFields();
        }
    }

    void set(List<HostConfig> systemsToSet, int chosenSystemToSet) {
        try {
            resetting = true;

            this.systems = copy(systemsToSet);
            this.chosenSystem = chosenSystemToSet;

            active.removeAllItems();
            for (HostConfig config : this.systems) {
                active.addItem(config);
            }
            active.setSelectedIndex(chosenSystemToSet);
            setFields();
        } finally {
            resetting = false;
        }
    }

    void setFields() {
        url.setText(systems.get(chosenSystem).url);
        username.setText(systems.get(chosenSystem).username);
        password.setText(systems.get(chosenSystem).password);
    }

    List<HostConfig> copy(List<HostConfig> in) {
        List<HostConfig> copy = new ArrayList<HostConfig>();
        for (HostConfig inSystem : in) {
            HostConfig sysCopy = new HostConfig(inSystem.url, inSystem.username, inSystem.password);
            copy.add(sysCopy);
        }
        return copy;
    }
}
