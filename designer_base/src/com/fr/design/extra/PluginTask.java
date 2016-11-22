package com.fr.design.extra;

import com.fr.design.extra.exe.Executor;
import com.fr.design.extra.exe.Command;
import com.fr.stable.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

/**
 * 插件安装,卸载,更新等任务
 *
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
                String fun = "(" + callback + ")(\"" + newValue + "\")";
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
            if (StringUtils.isNotBlank(message)) {
                updateMessage(message);
            }
            command.run(new Process<String>() {
                @Override
                public void process(String s) {
                    if (StringUtils.isNotBlank(s)) {
                        updateMessage(trimText(s));
                    }
                }
            });
        }
        return null;
    }

    @Override
    protected void done() {
        updateMessage(trimText(executor.getTaskFinishMessage()));
    }

    /**
     * vito:由于使用webEngine.executeScript("(" + callback + ")(\"" + newValue + "\")")
     * 执行脚本，所以原来规范的json格式也会在拼接字符串后可能抛出参数异常，需要转换掉一些会造成错误的特殊字符，
     * 选择在java端替换的原因是异常抛出自executeScript方法的参数,现在规范和拼接的json都能通过了，
     * 但是强烈建议不要使用拼接json
     * <p>
     * 1.""中的""必须转义
     * 2.js字符串中的\n会导致js字符串变成多行,而js字符串不支持多行拼接
     * 3.由JSONObject.toString()得到的字符串中html标签的属性会自动加上\造成替换难度加大，
     * 这边建议去除所有的html标签
     *
     * @param old 原始字符串
     * @return 处理之后的字符串
     */
    private String trimText(String old) {
        if (StringUtils.isNotBlank(old)) {
            String a = old.replaceAll("\n", "").replaceAll("\"", "\\\\\"").replaceAll("\'", "\\\\\'");
            System.out.println(a);
            return a;
        }
        return StringUtils.EMPTY;
    }
}
