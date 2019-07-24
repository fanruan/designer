package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.MultiPieColumnFieldCollection;
import com.fr.design.chartx.component.MultiComboBoxPaneWithUISpinner;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;

import javax.swing.JPanel;
import java.awt.Component;
import java.util.List;

/**
 * Created by shine on 2019/6/18.
 */
public class MultiPieDataSetFieldsPane extends AbstractDataSetFieldsPane<MultiPieColumnFieldCollection> {
    private UITextField nameField;

    private MultiComboBoxPaneWithUISpinner levelComboBoxPane;

    private UIComboBox value;

    private CalculateComboBox function;

    @Override
    protected void initComponents() {
        nameField = new UITextField();
        levelComboBoxPane = new MultiComboBoxPaneWithUISpinner();
        value = new UIComboBox();
        function = new CalculateComboBox();
        super.initComponents();
    }

    @Override
    protected JPanel createNorthPane() {
        return levelComboBoxPane;
    }


    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Use_Value"),
                Toolkit.i18nText("Fine-Design_Chart_Summary_Method")
        };
    }

    @Override
    protected Component[] fieldComponents() {
        return new UIComboBox[]{
                value,
                function
        };
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        List<UIComboBox> list = levelComboBoxPane.getComponentList();

        int len = list.size();
        UIComboBox[] result = new UIComboBox[len + 1];
        for (int i = 0; i < len; i++) {
            result[i] = list.get(i);
        }
        result[len] = value;

        return result;
    }

    @Override
    public void refreshBoxListWithSelectTableData(List columnNameList) {
        super.refreshBoxListWithSelectTableData(columnNameList);
        levelComboBoxPane.setCurrentBoxList(columnNameList);
    }

    @Override
    public void populateBean(MultiPieColumnFieldCollection ob) {
        levelComboBoxPane.populate(ob.getLevels());
        populateFunctionField(value, function, ob.getValue());
    }

    @Override
    public MultiPieColumnFieldCollection updateBean() {
        MultiPieColumnFieldCollection result = new MultiPieColumnFieldCollection();

        levelComboBoxPane.update(result.getLevels());

        updateFunctionField(value, function, result.getValue());

        return result;
    }
}
