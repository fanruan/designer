package com.fr.design.os;

import com.fr.invoke.Reflect;
import java.util.HashMap;
import java.util.Map;

public class OSSupportCenter {

    private static Map<Class<? extends OSBasedAction>,OSBasedAction> osBasedActionMap = new HashMap<Class<? extends OSBasedAction>,OSBasedAction>();
    public static void buildAction(OSBasedAction action, SupportOS supportOS){
        if(supportOS.support()){
            action.execute();
        }
    }

    public static <T extends OSBasedAction> T getAction(Class<T> clazz) {
        OSBasedAction action = osBasedActionMap.get(clazz);
        if(action == null){
            action = Reflect.on(clazz).create().get();
            osBasedActionMap.put(clazz,action);
        }
        return (T) action;
    }
}
