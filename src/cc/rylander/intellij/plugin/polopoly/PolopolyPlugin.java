package cc.rylander.intellij.plugin.polopoly;

import com.intellij.openapi.components.*;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
public class PolopolyPlugin implements ProjectComponent, Configurable, PersistentStateComponent<StoredState> {
    PolopolyInstallationConfigGUI configGUI;
    StoredState settings = new StoredState();
    private boolean settingsIsLoaded;


    public void initComponent() {
        if (!settingsIsLoaded) {
            settings.init();
        }
    }

    public void disposeComponent() { }

    HostConfig getChosenSystem() {
        return settings.systems.get(settings.systemChoice);
    }

    @NotNull
    public String getComponentName() {
        return "PolopolyPlugin";
    }

    public void projectClosed() { }

    public void projectOpened() { }

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
            configGUI = new PolopolyInstallationConfigGUI(this);
        }
        return configGUI.rootComponent;
    }

    public boolean isModified() {
        return configGUI != null && configGUI.isModified(settings.systems, settings.systemChoice);
    }

    public void apply() throws ConfigurationException {
        settings.systemChoice = configGUI.getChosenSystem();
        settings.systems = configGUI.getSystems();
    }

    public void reset() {
        configGUI.set(settings.systems, settings.systemChoice);
    }

    public void disposeUIResources() {
        configGUI = null;
    }

    public StoredState getState() {
        return settings;
    }

    public void loadState(StoredState state) {
        XmlSerializerUtil.copyBean(state, settings);
        settings.migrate();
        settingsIsLoaded = true;
    }
}
