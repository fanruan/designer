package com.fr.design.extra.exe;

/**
 * Created by richie on 16/3/19.
 */
public class InstallOnlineExecutor implements Executor {

    private String pluginID;

    public InstallOnlineExecutor(String pluginID) {
        this.pluginID = pluginID;
    }

    @Override
    public String getTaskFinishMessage() {
        return "已成功安裝";
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在下载插件:" + pluginID;
                    }

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在安装插件:" + pluginID;
                    }

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };
    }
}
