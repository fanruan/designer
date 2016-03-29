package com.fr.design.fun;


import com.fr.stable.fun.Level;

import java.awt.event.AWTEventListener;

/**
 * Created by zack on 2015/8/17.
 */
public interface GlobalListenerProvider extends Level{

    String XML_TAG = "GlobalListenerProvider";

    int CURRENT_LEVEL = 1;


    AWTEventListener listener();
}