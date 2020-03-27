package com.fr.design.bridge.exec;

/**
 * Created by ibm on 2017/6/21.
 */
public interface JSExecutor {

    String CALLBACK_FUNCTION_NAME = "action";

    void executor(String newValue);
}
