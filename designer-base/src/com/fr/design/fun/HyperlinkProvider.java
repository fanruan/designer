package com.fr.design.fun;

import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.stable.fun.Level;
import com.fr.stable.fun.mark.Mutable;

/**
 * Created by zack on 2016/1/20.
 */
public interface HyperlinkProvider extends Mutable {
    String XML_TAG = "HyperlinkProvider";

    int CURRENT_LEVEL = 2;


    /**
     * 创建一个超级连接类型
     * @return  NameableCreator
     */
    NameableCreator createHyperlinkCreator();
}