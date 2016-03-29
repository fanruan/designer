package com.fr.design.extra.exe;

/**
 * Created by richie on 16/3/19.
 */
public class ModifyStatusExecutor implements Executor {

    private String pluginID;
    private boolean active;

    public ModifyStatusExecutor(String pluginID, boolean active) {
        this.pluginID = pluginID;
        this.active = active;
    }

    @Override
    public String getTaskFinishMessage() {
        return "插件" + pluginID + "已更改为可用状态:" + active;
    }

    @Override
    public Command[] getCommands() {
        return new Command[0];
    }
}
