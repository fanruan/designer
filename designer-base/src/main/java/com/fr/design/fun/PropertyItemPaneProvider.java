package com.fr.design.fun;

import com.fr.design.mainframe.PaneHolder;
import com.fr.design.mainframe.PropertyItemBean;
import com.fr.stable.fun.mark.Mutable;
import org.jetbrains.annotations.Nullable;

/**
 * created by Harrison on 2020/03/23
 **/
public interface PropertyItemPaneProvider<T> extends Mutable {
    
    int CURRENT_LEVEL = 1;
    
    String XML_TAG = "PropertyItemPaneProvider";
    
    int FIRST = 100;
    
    int LAST = -100;
    
    /**
     * 独一无二的标志
     *
     * @return 标志
     */
    String key();
    
    /**
     * 配置属性
     *
     * @return 熟悉
     */
    PropertyItemBean getItem();
    
    /**
     * 面板持有者
     *
     * @param clazz 类型
     * @return 持有者
     */
    @Nullable
    PaneHolder<T> getPaneHolder(Class<?> clazz);
    
    /**
     * 想要替代的类型
     *
     * @return 替代类型
     */
    String replaceKey();
}
