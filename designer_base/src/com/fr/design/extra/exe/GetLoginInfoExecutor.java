package com.fr.design.extra.exe;

import com.fr.design.DesignerEnvManager;
import com.fr.design.extra.Process;
import com.fr.stable.StringUtils;

/**
 * Created by lp on 2016/8/16.
 */
public class GetLoginInfoExecutor implements Executor {
    private String result = "[]";

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
                        String username = DesignerEnvManager.getEnvManager().getBBSName();
                        if (StringUtils.isEmpty(username)) {
                        }else {
                            result = username;
                        }
                    }
                }
        };
    }
}