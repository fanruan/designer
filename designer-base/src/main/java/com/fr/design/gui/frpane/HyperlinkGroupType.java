package com.fr.design.gui.frpane;

import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.js.JavaScript;
import com.fr.stable.Filter;
import org.jetbrains.annotations.NotNull;

/**
 * 超级链接 支持的类型 种类.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-26 下午04:41:55
 */
public interface HyperlinkGroupType {

    /**
     * 返回支持的超级链接类型
     *
     * @return NameableCreator[]
     */
    @NotNull
    NameableCreator[] getHyperlinkCreators();


    /**
     * 图表超链过滤
     *
     * @return 图表超链过滤
     */
    Filter<Class<? extends JavaScript>> getFilter();

    /**
     * 老图表超链的过滤
     *
     * @return 老图表超链的过滤
     */
    Filter<Object> getOldFilter();

}