package com.fr.design.gui.frpane;

import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.module.DesignModuleFactory;
import com.fr.js.JavaScript;
import com.fr.stable.Filter;
import org.jetbrains.annotations.NotNull;

/**
 * 基础的超级链接类型, 不包括图表相关超链.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-26 下午04:46:02
 */
public class BaseHyperlinkGroup implements HyperlinkGroupType {

    /**
     * 返回支持的超级链接类型
     *
     * @return NameableCreator[]
     */
    @NotNull
    @Override
    public NameableCreator[] getHyperlinkCreators() {
        return DesignModuleFactory.getCreators4Hyperlink();
    }

    @Override
    public Filter<Class<? extends JavaScript>> getFilter() {
        return new Filter<Class<? extends JavaScript>>() {
            @Override
            public boolean accept(Class<? extends JavaScript> aClass) {
                return true;
            }
        };
    }

    @Override
    public Filter<Object> getOldFilter() {
        return new Filter<Object>() {
            @Override
            public boolean accept(Object object) {
                return true;
            }
        };
    }


}