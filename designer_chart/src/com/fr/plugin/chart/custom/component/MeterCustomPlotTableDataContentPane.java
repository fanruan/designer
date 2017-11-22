package com.fr.plugin.chart.custom.component;

import com.fr.base.Utils;
import com.fr.chart.chartdata.MeterTableDefinition;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.mainframe.chart.gui.ChartDataPane;
import com.fr.design.mainframe.chart.gui.data.table.MeterPlotTableDataContentPane;
import com.fr.plugin.chart.data.VanChartMeterCustomTableDefinition;

import java.awt.*;

/**
 * Created by Fangjie on 2016/5/18.
 */
public class MeterCustomPlotTableDataContentPane extends MeterPlotTableDataContentPane {
    private static final int TEXT_HT = 20;
    private static final int TEXT_WD = 80;
    private UITextField nameField;
    public MeterCustomPlotTableDataContentPane(ChartDataPane parent) {
        super(parent);
    }

    @Override
    protected Component getNameComponent() {
        nameField = new UITextField();
        nameField.setPreferredSize(new Dimension(TEXT_WD, TEXT_HT));
        return nameField;
    }

    @Override
    protected void populateNameComponent(MeterTableDefinition meter) {
        nameField.setText(meter.getName());
    }

    @Override
    protected void updateNameComponent(MeterTableDefinition meter) {
        meter.setName(Utils.objectToString(nameField.getText()));
    }

    @Override
    protected MeterTableDefinition getMeterTableDefinition(){
        return new VanChartMeterCustomTableDefinition();
    }
}
