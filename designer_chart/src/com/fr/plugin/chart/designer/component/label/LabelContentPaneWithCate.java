package com.fr.plugin.chart.designer.component.label;

import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.VanChartStylePane;

import javax.swing.*;
import java.awt.*;

/**
 * 只有分类（仪表盘的分类标签）
 */
public class LabelContentPaneWithCate extends GaugeLabelContentPane {

    private static final long serialVersionUID = 1238530475858471304L;

    public LabelContentPaneWithCate(VanChartStylePane parent, JPanel showOnPane){
        super(parent, showOnPane);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, JPanel panel) {
        return TableLayout4VanChartHelper.createTableLayoutPaneWithSmallTitle(title, panel);
    }

    protected double[] getRowSize(double p){
        return new double[]{p};
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{categoryNameFormatPane,null},
        };
    }
}