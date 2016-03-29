package com.fr.design.extra.exe;

/**
 * Created by richie on 16/3/19.
 */
public class InstallFromDiskExecutor implements Executor {
    private String filePath;

    public InstallFromDiskExecutor(String filePath) {
        this.filePath = filePath;

    }

    @Override
    public String getTaskFinishMessage() {
        return "已成功安装";
    }

    @Override
    public Command[] getCommands() {
        return new Command[]{
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在解压文件" + filePath;
                    }

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Command() {
                    @Override
                    public String getExecuteMessage() {
                        return "正在安装";
                    }

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };
    }
}
