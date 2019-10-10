package com.fr.design.chartx.component.correlation;

import com.fr.design.gui.frpane.UICorrelationPane;

import javax.swing.JTable;
import java.awt.Component;

/**
 * Created by shine on 2019/6/4.
 */
public interface FieldEditorComponentWrapper<T extends Component> {

    String headerName();

    T getTableCellEditorComponent(UICorrelationPane parent, JTable table, boolean isSelected, final int row, int column);

    Object getValue(T t);

    void setValue(T t, Object o);
}
