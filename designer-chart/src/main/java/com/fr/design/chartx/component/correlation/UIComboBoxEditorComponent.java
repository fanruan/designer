package com.fr.design.chartx.component.correlation;

import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itable.UITable;

import javax.swing.JTable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2019/6/10.
 */
public class UIComboBoxEditorComponent extends AbstractEditorComponent<UIComboBox> {

    public UIComboBoxEditorComponent(String header) {
        super(header);
    }

    protected List<String> items() {
        return new ArrayList<String>();
    }

    @Override
    public UIComboBox getTableCellEditorComponent(final UICorrelationPane parent, JTable table, boolean isSelected, final int row, int column) {
        UIComboBox uiComboBox = new UIComboBox(items().toArray());

        uiComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                parent.stopCellEditing();
                parent.fireTargetChanged();
                UITable table = parent.getTable();
                Object object = table.getValueAt(row, 0);
                if (object != null) {
                    table.setValueAt(object, row, 1);
                }
            }
        });

        return uiComboBox;
    }


    @Override
    public Object getValue(UIComboBox uiComboBox) {
        return uiComboBox.getSelectedItem();
    }

    @Override
    public void setValue(UIComboBox uiComboBox, Object o) {
        uiComboBox.getModel().setSelectedItem(o);
    }
}
