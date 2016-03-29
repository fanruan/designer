package com.fr.design.extra;

import com.fr.design.extra.exe.Executor;
import com.fr.design.extra.exe.Command;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

/**
 * 插件安装,卸载,更新等任务
 * @param <T>
 */
public class PluginTask<T> extends Task<T> {

    private Executor executor;

    public PluginTask(final WebEngine webEngine, final JSObject callback, final Executor executor) {
        init(webEngine, callback);
        this.executor = executor;
    }

    private void init(final WebEngine webEngine, final JSObject callback) {
        messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String fun = "(" + callback + ")('" + newValue + "')";
                try {
                    webEngine.executeScript(fun);
                } catch (Exception e) {
                    webEngine.executeScript("alert(\"" + e.getMessage() + "\")");
                }
            }
        });
    }

    @Override
    protected T call() throws Exception {
        Command[] commands = executor.getCommands();
        for (Command command : commands) {
            String message = command.getExecuteMessage();
            updateMessage(message);
            command.run();
        }
        return null;
    }

    @Override
    protected void done() {
        updateMessage(executor.getTaskFinishMessage());
    }
}
