package com.fr.design.extra.exe;

import com.fr.design.extra.LoginWebBridge;
import com.fr.design.extra.Process;
import com.fr.design.gui.ilable.UILabel;

/**
 * Created by Slpire on 2016/11/7.
 */
public class PluginLoginExecutor implements Executor {

    private String result = "[]";

    private String username;
    private String password;
    private UILabel uiLabel;

    public PluginLoginExecutor(String username, String password, UILabel uiLabel) {
        this.username = username;
        this.password = password;
        this.uiLabel = uiLabel;
    }

    @Override
    public String getTaskFinishMessage() {
        return result;
    }

    @Override
    public Command[] getCommands() {
        return new Command[] {
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return null;
                    }

                    @Override
                    public void run(Process<String> process) {
                        String loginResult = LoginWebBridge.getHelper().login(username, password, uiLabel);
                        if (Integer.valueOf(loginResult) == 0) {
                            LoginWebBridge.getHelper().updateMessageCount();
                        }
                        result = loginResult;
                    }
                }
        };
    }
}
