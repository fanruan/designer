package com.fr.design.style.preference;

import com.fr.base.Style;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * Created by kerry on 2019-11-04
 */
public interface PreferenceTabConfig {
    /**
     * @return tab的标题
     */
    String tabName();

    /**
     * @param changeListener 需要添加的listener
     * @return tab对应的component
     */
    JComponent tabComponent(ChangeListener changeListener);

    /**
     * @return 更新后的样式
     */
    Style updateTabConfig();

    /**
     * @param style 待渲染的样式
     */
    void populateTabConfig(Style style);
}
