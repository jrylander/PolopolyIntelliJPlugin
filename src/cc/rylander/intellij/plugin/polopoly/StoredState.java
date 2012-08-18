package cc.rylander.intellij.plugin.polopoly;

import java.util.ArrayList;
import java.util.List;

public class StoredState {
    static final int VERSION = 1;

    public int version;

    // Version 0
    public String url;
    public String username;
    public String password;

    // Version 1
    public List<HostConfig> systems = new ArrayList<HostConfig>();
    public int systemChoice;

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
        if (version < 1) {
            init();
            if (url != null) systems.get(systemChoice).url = url;
            if (username != null) systems.get(systemChoice).username = username;
            if (password != null) systems.get(systemChoice).password = password;
        }
    }
}
