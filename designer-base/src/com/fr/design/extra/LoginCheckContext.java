package com.fr.design.extra;

import java.util.ArrayList;

/**
 * Created by 夏翔 on 2016/3/14 0014.
 */


public class LoginCheckContext {
    private static ArrayList<LoginCheckListener> loginCheckListenerList = new ArrayList<LoginCheckListener>();

    /**
     * 触发登录框弹出的监听器
     */
    public static void fireLoginCheckListener() {
        for (LoginCheckListener l : loginCheckListenerList) {
            l.loginChecked();
        }
    }

    /**
     * 添加一个弹出登录框的监听事件
     *
     * @param l 登录框弹出监听事件
     */
    public static void addLoginCheckListener(LoginCheckListener l) {
        loginCheckListenerList.add(l);
    }
}
