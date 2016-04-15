package com.fr.design.extra.exe;

import com.fr.design.extra.PluginWebBridge;

import java.io.File;

/**
 * Created by richie on 16/3/19.
 */
public class UpdateFromDiskExecutor implements Executor {

    private String filePath;

    public UpdateFromDiskExecutor(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getTaskFinishMessage() {
        return "插件更新操作结束";
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
                    public void run() {
                        PluginWebBridge.getHelper().updateFileFromDisk(new File(filePath));
                    }
                }
        };
    }
}
