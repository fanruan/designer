package com.fr.design.fun;

import com.fr.design.beans.BasicBeanPane;
import com.fr.form.ui.Widget;
import com.fr.stable.fun.Level;

/**
 * @author richie
 * @date 2015-01-29
 * @since 8.0
 * 格子中的自定义控件接口
 */
public interface CellWidgetOptionProvider extends ParameterWidgetOptionProvider, Level {

    String XML_TAG = "CellWidgetOptionProvider";

    /**
     * 自定义格子控件的设计界面类
     * @return 控件设计界面类
     */
    Class<? extends BasicBeanPane<? extends Widget>> appearanceForWidget();

}