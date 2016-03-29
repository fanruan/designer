package com.fr.design.extra.exe;

/**
 * Created by richie on 16/3/19.
 */
public class UninstallExecutor implements Executor {

    private String[] pluginIDs;

    public UninstallExecutor(String[] pluginIDs) {
        this.pluginIDs = pluginIDs;
    }

    @Override
    public String getTaskFinishMessage() {
        return "插件已卸载完毕,重启后生效";
    }

    @Override
    public Command[] getCommands() {
        return new Command[0];
    }
}
