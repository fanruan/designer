package com.fr.design.extra.exe;

/**
 * Created by richie on 16/3/19.
 */
public class UpdateOnlineExecutor implements Executor {

    private String pluginID;

    public UpdateOnlineExecutor(String pluginID) {
        this.pluginID = pluginID;

    }
    @Override
    public String getTaskFinishMessage() {
        return "插件已更新完毕:" + pluginID;
    }

    @Override
    public Command[] getCommands() {
        return new Command[0];
    }
}
