package com.fr.design.chartx.component.correlation;

import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.frpane.UICorrelationPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.GeneralUtils;

import javax.swing.JTable;

/**
 * Created by shine on 2019/6/10.
 */
public class UITextFieldEditorComponent extends AbstractEditorComponent<UITextField> {
    public UITextFieldEditorComponent(String header) {
        super(header);
    }

    @Override
    public UITextField getTableCellEditorComponent(final UICorrelationPane parent, JTable table, boolean isSelected, int row, int column) {
        UITextField uiTextField = new UITextField();

        uiTextField.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                parent.fireTargetChanged();
            }
        });

        return uiTextField;
    }

    @Override
    public Object getValue(UITextField uiTextField) {
        return uiTextField.getText();
    }

    @Override
    public void setValue(UITextField uiTextField, Object o) {
        uiTextField.setText(GeneralUtils.objectToString(o));
    }
}
