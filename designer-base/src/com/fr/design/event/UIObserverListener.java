package com.fr.design.event;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-3-22
 * Time: 下午5:22
 */
public interface UIObserverListener extends EventListener {
    public void doChange();
}