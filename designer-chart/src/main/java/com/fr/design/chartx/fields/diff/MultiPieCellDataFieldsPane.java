package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.MultiPieColumnFieldCollection;
import com.fr.design.chartx.component.MultiTinyFormulaPaneWithUISpinner;
import com.fr.design.chartx.fields.AbstractCellDataFieldsPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;

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
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_MultiPie_Series_Name"), SwingConstants.LEFT), nameField}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, new double[]{p}, new double[]{ChartDataPane.LABEL_WIDTH, 122}, 0, 6);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 15));

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(panel, BorderLayout.NORTH);
        northPane.add(new JSeparator(), BorderLayout.CENTER);
        northPane.add(levelPane, BorderLayout.SOUTH);
        return northPane;
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
