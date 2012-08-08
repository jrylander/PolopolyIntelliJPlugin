package cc.rylander.intellij.plugin.polopoly;

import com.intellij.openapi.components.*;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;

/**
 * Created by Johan Rylander <johan@rylander.cc>
 * on 2011-09-13
 */
@State(
        name = "PolopolyConfiguration",
        storages = {
                @Storage(id = "other", file = "$PROJECT_FILE$"),
                @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/other.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)

public class PolopolyPlugin implements ProjectComponent, Configurable, PersistentStateComponent<PolopolyPlugin> {
    public static final String NAME = "PolopolyPlugin";

    public class HostConfig {
        public String url;
        public String username;
        public String password;
        public String name;
    }
    
    PolopolyInstallationConfigGUI configGUI;
    public Collection<HostConfig> systems;
    public HostConfig systemChoice;

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return NAME;
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    @Nls
    public String getDisplayName() {
        return "Atex Polopoly";
    }

    public Icon getIcon() {
        return null;
    }

    public String getHelpTopic() {
        return null;
    }

    public JComponent createComponent() {
        if (configGUI == null) {
            configGUI = new PolopolyInstallationConfigGUI();
        }
        return configGUI.rootComponent;
    }

    public boolean isModified() {
        return configGUI != null && configGUI.isModified(url, username, password, name, systemChoice);
    }

    public void apply() throws ConfigurationException {
        if (configGUI != null) {
            url = configGUI.url.getText();
            username = configGUI.username.getText();
            password = configGUI.password.getText();
        }
    }

    public void reset() {
        if (configGUI != null) {
            configGUI.url.setText(url);
            configGUI.username.setText(username);
            configGUI.password.setText(password);
        }
    }

    public void disposeUIResources() {
        configGUI = null;
    }

    public PolopolyPlugin getState() {
        return this;
    }

    public void loadState(PolopolyPlugin state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
