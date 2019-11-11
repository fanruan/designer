package com.fr.start;

import com.fr.design.os.impl.DemoAction;
import com.fr.stable.os.support.OSBasedAction;
import com.fr.stable.os.support.OSSupportCenter;

public class Demo {
    public static void main(String[] args) {
        OSBasedAction osBasedAction = OSSupportCenter.getAction(DemoAction.class);
        osBasedAction.execute();
        System.exit(0);
    }
}