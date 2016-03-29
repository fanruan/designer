package com.fr.design.extra.exe;

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
        return "插件已更新完毕";
    }

    @Override
    public Command[] getCommands() {
        return new Command[0];
    }
}
