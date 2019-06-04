package com.fr.design.chartx.component;

import com.fr.design.gui.frpane.UICorrelationPane;

import java.awt.Component;

/**
 * Created by shine on 2019/6/4.
 */
public interface FieldEditorComponentWrapper<T extends Component> {

    String headerName();

    T createEditorComponent(UICorrelationPane parent);

    Object value(T t);

    void setValue(T t, Object o);
}
