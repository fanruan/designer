package com.fr.design.bridge.exec;

/**
 * Created by ibm on 2017/5/27.
 */
public class JSCallback {

    private JSExecutor executeScript;

    public JSCallback(JSExecutor jsExecutor) {
        this.executeScript = jsExecutor;
    }

    public void execute(String newValue) {
        executeScript.executor(newValue);
    }
}

