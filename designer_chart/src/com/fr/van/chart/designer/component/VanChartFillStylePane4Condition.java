package com.fr.van.chart.designer.component;

import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * 配色 只有下拉框控件无配色title。 条件属性 配色中用到
 */
public class VanChartFillStylePane4Condition extends ChartFillStylePane {
    private static final long serialVersionUID = 2470094484790112401L;

    @Override
    protected void initLayout() {
        customPane.setPreferredSize(new Dimension(200, 130));
        colorGradient.setPreferredSize(new Dimension(120, 30));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = { p, p};
        Component[][] components = new Component[][]{
                new Component[]{styleSelectBox},
                new Component[]{customPane}
        } ;
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        if(styleSelectBox.getSelectedIndex() != styleSelectBox.getItemCount() - 1) {
            return new Dimension(styleSelectBox.getPreferredSize().width, 20);
        }
        return super.getPreferredSize();
    }
}