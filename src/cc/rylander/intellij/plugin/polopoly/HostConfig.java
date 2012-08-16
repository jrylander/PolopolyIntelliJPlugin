package cc.rylander.intellij.plugin.polopoly;

import java.io.Serializable;

public class HostConfig implements Serializable {
    public String url;
    public String username;
    public String password;

    public HostConfig() {
    }

    public HostConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return url;
    }
}
