package com.fr.design.extra.exe;

/**
 * Created by richie on 16/3/19.
 */
public interface Executor {

    String getTaskFinishMessage();

    Command[] getCommands();
}
