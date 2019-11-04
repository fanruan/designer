package com.fr.design.chartx.fields.diff;

import com.fr.chartx.data.field.diff.BubbleColumnField;
import com.fr.chartx.data.field.diff.BubbleColumnFieldCollection;
import com.fr.design.chartx.component.AbstractSingleFilterPane;
import com.fr.design.chartx.fields.AbstractDataSetFieldsPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.i18n.Toolkit;
import com.fr.extended.chart.UIComboBoxWithNone;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;

/**
 * Created by Wim on 2019/10/29.
 */
public class ScatterDataSetFieldsPane extends AbstractDataSetFieldsPane<BubbleColumnFieldCollection> {

    private UIComboBoxWithNone series;
    private UIComboBox xField;
    private UIComboBox yField;
    private UIComboBoxWithNone value;

    //todo 数据筛选
    private AbstractSingleFilterPane filterPane;


    @Override
    protected void initComponents() {

        series = new UIComboBoxWithNone();
        xField = new UIComboBox();
        yField = new UIComboBox();
        value = new UIComboBoxWithNone();

        filterPane = new AbstractSingleFilterPane() {
            @Override
            public String title4PopupWindow() {
                return Toolkit.i18nText("Fine-Design_Chart_Series");
            }
        };

        JPanel northPane = new JPanel(new BorderLayout(0, 6));
        northPane.add(new JSeparator(), BorderLayout.CENTER);
        northPane.add(createCenterPane(), BorderLayout.SOUTH);
        northPane.setBorder(BorderFactory.createEmptyBorder(4, 24, 0, 15));

        this.setLayout(new BorderLayout(0, 6));
        this.add(northPane, BorderLayout.NORTH);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new JPanel(), BorderLayout.NORTH);
        contentPane.add(filterPane, BorderLayout.CENTER);
        this.add(TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Data_Filter"), contentPane), BorderLayout.CENTER);
    }

    @Override
    protected UIComboBox[] filedComboBoxes() {
        return new UIComboBox[]{
                series, xField, yField, value
        };
    }

    @Override
    protected String[] fieldLabels() {
        return new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Series_Name"),
                "x",
                "y",
                Toolkit.i18nText("Fine-Design_Chart_Use_Value")
        };
    }

    @Override
    public BubbleColumnFieldCollection updateBean() {
        BubbleColumnFieldCollection collection = new BubbleColumnFieldCollection();
        BubbleColumnField field = collection.getBubbleColumnField(0);
        updateField(series, field.getSeriesName());
        updateField(xField, field.getXField());
        updateField(yField, field.getYField());
        updateField(value, field.getValueField());
        return collection;
    }

    @Override
    public void populateBean(BubbleColumnFieldCollection ob) {
        BubbleColumnField field = ob.getBubbleColumnField(0);
        populateField(series, field.getSeriesName());
        populateField(xField, field.getXField());
        populateField(yField, field.getYField());
        populateField(value, field.getValueField());
    }
}
