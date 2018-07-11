package com.fr.design.fun;


import com.fr.stable.fun.mark.Mutable;

import java.awt.event.AWTEventListener;

/**
 * Created by zack on 2015/8/17.
 * 全局事件监听
 */
public interface GlobalListenerProvider extends Mutable{

    String XML_TAG = "GlobalListenerProvider";

    int CURRENT_LEVEL = 1;

    /**
     * 返回一个AWT监听事件给系统注册
     * @return AWT监听事件
     */
    AWTEventListener listener();
}