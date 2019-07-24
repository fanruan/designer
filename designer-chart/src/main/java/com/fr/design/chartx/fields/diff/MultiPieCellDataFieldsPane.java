package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.MultiPieColumnFieldCollection;
import com.fr.design.chartx.component.MultiTinyFormulaPaneWithUISpinner;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;

import javax.swing.JPanel;

/**
 * Created by shine on 2019/6/18.
 */
public class MultiPieCellDataFieldsPane extends AbstractCellDataFieldsPane<MultiPieColumnFieldCollection> {

    private UITextField nameField;//指标名称

    private MultiTinyFormulaPaneWithUISpinner levelPane;

    private TinyFormulaPane value;

    @Override
    protected void initComponents() {
        nameField = new UITextField();
        levelPane = new MultiTinyFormulaPaneWithUISpinner();
        value = new TinyFormulaPane();
        super.initComponents();
    }

    @Override
    protected JPanel createNorthPane() {
        return levelPane;
    }

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Use_Value"),
        };
    }

    @Override
    protected TinyFormulaPane[] formulaPanes() {
        return new TinyFormulaPane[]{
                value
        };
    }

    @Override
    public void populateBean(MultiPieColumnFieldCollection ob) {
        nameField.setText(ob.getTargetName());
        levelPane.populate(ob.getLevels());
        populateField(value, ob.getValue());
    }

    @Override
    public MultiPieColumnFieldCollection updateBean() {
        MultiPieColumnFieldCollection result = new MultiPieColumnFieldCollection();

        result.setTargetName(nameField.getText());
        levelPane.update(result.getLevels());
        updateField(value, result.getValue());

        return result;
    }
}
