package cc.rylander.intellij.plugin.polopoly;

import com.intellij.openapi.components.*;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
    static final int VERSION = 1;

    @Transient
    PolopolyInstallationConfigGUI configGUI;

    // Old config
    public String url;
    public String username;
    public String password;

    public int version;
    public HostConfig[] storedSystems;
    public int systemChoice;

    @Transient
    private List<HostConfig> systems = new ArrayList<HostConfig>();

    public PolopolyPlugin() {
    }

    void init() {
        systems = new ArrayList<HostConfig>();
        systems.add(new HostConfig("http://local:8090/polopoly/import", "sysadmin", "password"));
        systems.add(new HostConfig("http://test/polopoly/import", "sysadmin", "password"));
        systems.add(new HostConfig("http://stage/polopoly/import", "sysadmin", "password"));
        systems.add(new HostConfig("http://prod/polopoly/import", "sysadmin", "password"));
        systemChoice = 0;
        version = VERSION;
    }

    void migrate() {
        if (url != null) systems.get(systemChoice).url = url;
        if (username != null) systems.get(systemChoice).username = username;
        if (password != null) systems.get(systemChoice).password = password;
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    HostConfig getChosenSystem() {
        return systems.get(systemChoice);
    }

    @NotNull
    public String getComponentName() {
        return "PolopolyPlugin";
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
            configGUI = new PolopolyInstallationConfigGUI(this);
        }
        return configGUI.rootComponent;
    }

    public boolean isModified() {
        return configGUI != null && configGUI.isModified(systems, systemChoice);
    }

    public void apply() throws ConfigurationException {
        if (configGUI != null) {
            systemChoice = configGUI.getChosenSystem();
            systems = configGUI.getSystems();
        }
    }

    public void reset() {
        if (configGUI != null && systems != null) {
            configGUI.set(systems, systemChoice);
        }
    }

    public void disposeUIResources() {
        configGUI = null;
    }

    public PolopolyPlugin getState() {
        storedSystems = new HostConfig[systems.size()];
        int idx=0;
        for (HostConfig system : systems) {
            storedSystems[idx++] = system;
        }
        return this;
    }

    public void loadState(PolopolyPlugin state) {
        XmlSerializerUtil.copyBean(state, this);
        if (version != VERSION) {
            init();
            migrate();
        } else {
            systems = new ArrayList<HostConfig>();
            for (HostConfig system : state.storedSystems) {
                systems.add(system);
            }
        }
    }
}
