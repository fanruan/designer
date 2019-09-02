package com.fr.design.event;

import java.util.EventListener;

/**
 * Created by plough on 2018/1/19.
 */
public interface DesignerOpenedListener extends EventListener {
    /**
     * Invoked when the target of the listener has changed the rpt content.
     */
    void designerOpened();
}