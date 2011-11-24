package cc.rylander.intellij.plugin.polopoly;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Import extends AnAction {
    @Override
    public void update(AnActionEvent e) {
        super.update(e);
        VirtualFile[] files = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(e.getDataContext());
        e.getPresentation().setEnabled(
                files != null &&
                files.length == 1 &&
                "xml".equalsIgnoreCase(files[0].getExtension()));
    }

    public void actionPerformed(AnActionEvent event) {
        final Project project = event.getData(PlatformDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        final PolopolyPlugin settings = project.getComponent(PolopolyPlugin.class);
        if (null == settings.username || null == settings.password || null == settings.url) {
            Messages.showMessageDialog(project,
                    "Please configure url, user and password for importing to Atex Polopoly", "Error",
                    Messages.getErrorIcon());
            return;
        }

        final VirtualFile[] files = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(event.getDataContext());
        if (files == null || files.length != 1) {
            return;
        }

        final StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
        new Thread(new Runnable() {
            public void run() {
                try {
                    setStatus(statusBar, "Starting import to Atex Polopoly");

                    byte[] fileContents = files[0].contentsToByteArray();
                    String contentType = "text/xml;charset=" + files[0].getCharset();
                    URL url = new URL(settings.url +
                                      "?username=" + settings.username +
                                      "&password=" + settings.password +
                                      "&result=true");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Content-Type", contentType);
                    connection.connect();
                    connection.getOutputStream().write(fileContents);

                    final int result = connection.getResponseCode();
                    if (result < 200 || result > 299) {
                        setStatus(statusBar,
                                "Error when importing file, see server log for details. Http error was: " + result);
                    } else {
                        setStatus(statusBar, "Finished import to Atex Polopoly");
                    }

                } catch (IOException e) {
                    setStatus(statusBar, "Error when importing file: " + e.getMessage());
                }
            }
        }).start();
    }

    private static void setStatus(final StatusBar statusBar, final String msg) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                if (null != statusBar) {
                    statusBar.setInfo(msg);
                }
            }
        });
    }
}
