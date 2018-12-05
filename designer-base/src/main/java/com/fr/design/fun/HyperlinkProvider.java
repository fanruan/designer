package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.js.Hyperlink;
import com.fr.stable.fun.mark.Mutable;

/**
 * Created by zack on 2016/1/20.
 */
public interface HyperlinkProvider<T extends Hyperlink> extends Mutable {
    String XML_TAG = "HyperlinkProvider";

    int CURRENT_LEVEL = 2;


    /**
     * 超级链接的描述信息，如果是实现类中重载了这个方法，就不需要再实现下面的三个方法：
     * @see HyperlinkProvider#text()
     * @see HyperlinkProvider#target()
     * @see HyperlinkProvider#appearance()
     * 如果并不重载，就需要分别实现上面的三个方法，不推荐重载这个方法
     * @return 描述信息
     */
    NameableCreator createHyperlinkCreator();

    /**
     * 超级链接的名字
     * @return 名字
     */
    String text();

    /**
     * 超级链接的实现类
     * @return 实现类
     */
    Class<T> target();

    /**
     * 超级链接的界面配置类
     * @return 配置类
     */
    Class<? extends BasicBeanPane<T>> appearance();
}