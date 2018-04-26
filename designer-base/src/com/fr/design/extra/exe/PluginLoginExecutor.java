package com.fr.design.extra.exe;

import com.fr.design.extra.LoginWebBridge;
import com.fr.design.extra.Process;

/**
 * @author vito
 * @date 2017/10/31
 */
public class PluginLoginExecutor implements Executor {
    private String result = "[]";

    private String username;
    private String password;

    public PluginLoginExecutor(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getTaskFinishMessage() {
        return result;
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return null;
                    }

                    @Override
                    public void run(Process<String> process) {
                        result = LoginWebBridge.getHelper().login(username, password);
                    }
                }
        };
    }
}
