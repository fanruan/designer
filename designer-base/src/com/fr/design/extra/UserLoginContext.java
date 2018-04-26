package com.fr.design.extra;

import java.util.ArrayList;

/**
 *  Created by lp on 2016/8/16.
 */
public class UserLoginContext {
    private static ArrayList<LoginContextListener> fireLoginContextListener = new ArrayList<LoginContextListener>();

    /**
     * 触发登录框弹出的监听器
     */
    public static void fireLoginContextListener() {
        for (LoginContextListener l : fireLoginContextListener) {
            l.showLoginContext();
        }
    }

    /**
     * 添加一个弹出登录框的监听事件
     *
     * @param l 登录框弹出监听事件
     */
    public static void addLoginContextListener(LoginContextListener l) {
        fireLoginContextListener.add(l);
    }
}
