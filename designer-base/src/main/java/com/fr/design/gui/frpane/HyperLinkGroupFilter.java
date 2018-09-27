package com.fr.design.gui.frpane;

import com.fr.js.JavaScript;

public interface HyperLinkGroupFilter {

    /**
     * @param clazz clazz
     * @return 是否可用，返回true表示可用，返回false表示不可用
     */
    boolean filter(Class<? extends JavaScript> clazz);

    /**
     * 兼容老图表
     *
     * @param object object
     * @return 是否可用，返回true表示可用，返回false表示不可用
     * @see HyperLinkGroupFilter#filter(Class)
     */
    boolean filter(Object object);

}
