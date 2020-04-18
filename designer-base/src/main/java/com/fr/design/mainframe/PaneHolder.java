package com.fr.design.mainframe;

import javax.swing.JPanel;

/**
 * created by Harrison on 2020/03/23
 **/
public interface PaneHolder<T> {
    
    /**
     *  得到 Pane
     *
     * @param arg 参数
     * @return 面板
     */
    JPanel getInstance(T arg);
}
