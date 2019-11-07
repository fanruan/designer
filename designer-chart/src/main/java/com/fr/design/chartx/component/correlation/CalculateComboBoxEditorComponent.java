package com.fr.design.chartx.component.correlation;

import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;

import javax.swing.JTable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by shine on 2019/6/10.
 */
public class CalculateComboBoxEditorComponent extends AbstractEditorComponent<CalculateComboBox> {

    public CalculateComboBoxEditorComponent(String header) {
        super(header);
    }

    @Override
    public CalculateComboBox getTableCellEditorComponent(final UICorrelationPane parent, JTable table, boolean isSelected, int row, int column) {

        CalculateComboBox calculateComboBox = new CalculateComboBox();

        calculateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                parent.stopCellEditing();
                parent.fireTargetChanged();
            }
        });
        return calculateComboBox;
    }

    @Override
    public Object getValue(CalculateComboBox calculateComboBox) {
        return calculateComboBox.getSelectedItem();
    }

    @Override
    public void setValue(CalculateComboBox calculateComboBox, Object o) {

        if (o != null) {
            calculateComboBox.setSelectedItem(o);
        }
    }
}
