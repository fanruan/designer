package com.fr.design.chartx.component.correlation;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UICorrelationPane;

import javax.swing.JTable;

/**
 * Created by shine on 2019/6/4.
 */
public class TinyFormulaPaneEditorComponent extends AbstractEditorComponent<TinyFormulaPane> {

    public TinyFormulaPaneEditorComponent(String header) {
        super(header);
    }

    @Override
    public TinyFormulaPane getTableCellEditorComponent(final UICorrelationPane parent, JTable table, boolean isSelected, final int row, int column) {
        TinyFormulaPane editorComponent = new TinyFormulaPane() {
            @Override
            public void okEvent() {
                parent.stopCellEditing();
                parent.fireTargetChanged();
            }

            @Override
            protected void populateTextField(BaseFormula fm) {
                formulaTextField.setText(fm.getContent());
            }
        };
        editorComponent.setBackground(UIConstants.FLESH_BLUE);

        editorComponent.getUITextField().registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                parent.fireTargetChanged();
            }
        });

        return editorComponent;
    }

    @Override
    public Object getValue(TinyFormulaPane formulaPane) {
        return formulaPane.getUITextField().getText();
    }

    @Override
    public void setValue(TinyFormulaPane formulaPane, Object o) {
        formulaPane.getUITextField().setText(Utils.objectToString(o));
    }
}
