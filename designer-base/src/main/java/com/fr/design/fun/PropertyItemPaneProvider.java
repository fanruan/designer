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
    
    String key();
    
    PropertyItemBean getItem();
    
    @Nullable
    PaneHolder<T> getPaneHolder(Class<?> clazz);
    
    String replaceKey();
}
