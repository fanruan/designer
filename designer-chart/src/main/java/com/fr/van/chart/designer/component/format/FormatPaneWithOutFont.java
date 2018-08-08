package com.fr.van.chart.designer.component.format;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FormatPane;
import com.fr.design.layout.TableLayout;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by mengao on 2017/8/28.
 * 只有文本格式设置，没有字体设置
 */
public class FormatPaneWithOutFont extends FormatPane {
    private static final int HEIGHT = 30;

    protected JPanel createContentPane(Component[][] components) {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] rowSize = {p, p, p};
        double[] columnSize = {f, e};
        return TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);
    }

    protected Component[][] getComponent (JPanel fontPane, JPanel centerPane, JPanel typePane) {
        typePane.setBorder(BorderFactory.createEmptyBorder());
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Format"), SwingConstants.LEFT), typePane},
                new Component[]{null, centerPane},
        };
    }

    public Dimension getPreferredSize() {
        //todo @mango
        if (getTypeComboBox().getSelectedIndex() == 0) {
            return new Dimension((int)getTypeComboBox().getPreferredSize().getWidth(), HEIGHT);
        }
        return new Dimension((int)super.getPreferredSize().getWidth(), (int)super.getPreferredSize().getHeight());
    }
}
