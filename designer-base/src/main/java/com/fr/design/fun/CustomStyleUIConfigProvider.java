package com.fr.design.fun;

import com.fr.base.Style;
import com.fr.common.annotations.Open;
import com.fr.stable.fun.mark.Mutable;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;

/**
 * Created by kerry on 2019-11-11
 */
@Open
public interface CustomStyleUIConfigProvider extends Mutable {
    String XML_TAG = "CustomStyleUIConfigProvider";

    int CURRENT_LEVEL = 1;

    /**
     * @return 配置名
     */
    String configName();

    /**
     * @param changeListener 需要添加的listener
     * @return 对应的component
     */
    JComponent uiComponent(ChangeListener changeListener);

    /**
     * @return 更新后的样式
     */
    Style updateConfig();

    /**
     * @param style 待渲染的样式
     */
    void populateConfig(Style style);
}
