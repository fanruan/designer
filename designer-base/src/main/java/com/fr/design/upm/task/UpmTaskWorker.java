package com.fr.design.upm.task;

import com.fr.design.bridge.exec.JSCallback;
import com.fr.design.bridge.exec.JSUtils;
import com.fr.design.extra.Process;
import com.fr.design.extra.exe.Command;
import com.fr.design.extra.exe.Executor;
import com.fr.stable.StringUtils;

import javax.swing.*;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-17
 */
public class UpmTaskWorker<V> extends SwingWorker<V, String> {

    private Executor executor;
    private JSCallback callback;

    public UpmTaskWorker(final JSCallback callback, final Executor executor) {
        this.executor = executor;
        this.callback = callback;
    }

    @Override
    protected V doInBackground() throws Exception {
        Command[] commands = executor.getCommands();
        for (Command command : commands) {
            String message = command.getExecuteMessage();
            if (StringUtils.isNotBlank(message)) {
                publish(message);
            }
            command.run(new Process<String>() {
                @Override
                public void process(String s) {
                    if (StringUtils.isNotBlank(s)) {
                        publish(JSUtils.trimText(s));
                    }
                }
            });
        }
        return null;
    }

    @Override
    protected void done() {
        String result = executor.getTaskFinishMessage();
        callback.execute(result);
    }
}
