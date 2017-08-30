package com.fr.plugin.chart.designer.component.format;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/8/14.
 * 没有字体设置
 * 类型下拉框中只有常规、数字、百分比、货币、科学计数选项，没有时间型、日期型、文本型选项。
 */
public class FormatPaneWithNormalType extends FormatPaneWithOutFont {

    public FormatPaneWithNormalType() {
        super();
        setForDataSheet();
    }

    protected Component[][] getComponent(JPanel fontPane, JPanel centerPane, JPanel typePane) {
        return new Component[][]{
                new Component[]{null, centerPane},
        };
    }

    protected void setTypeComboBoxPane(UIComboBox typeComboBox) {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(getTypeComboBoxComponent(typeComboBox), rowSize, columnSize);
        this.add(panel, BorderLayout.NORTH);
    }

    protected Component[][] getTypeComboBoxComponent (UIComboBox typeComboBox) {
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_DataType"), SwingConstants.LEFT), typeComboBox},

        };
    }
}
