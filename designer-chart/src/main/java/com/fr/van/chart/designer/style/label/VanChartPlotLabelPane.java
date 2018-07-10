package com.fr.van.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.AttrLabel;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VanChartPlotLabelPane extends BasicPane {
    private static final long serialVersionUID = -1701936672446232396L;
    protected UICheckBox isLabelShow;

    protected VanChartPlotLabelDetailPane labelDetailPane;

    protected VanChartStylePane parent;
    protected Plot plot;

    protected JPanel labelPane;

    public VanChartPlotLabelPane(Plot plot, VanChartStylePane parent) {
        this.parent = parent;
        this.plot = plot;
        isLabelShow = new UICheckBox(Inter.getLocText("Plugin-ChartF_UseLabel"));
        labelPane = new JPanel(new BorderLayout(0, 4));
        createLabelPane();
        addComponents();
    }

    protected void createLabelPane() {
        labelDetailPane = new VanChartPlotLabelDetailPane(this.plot, this.parent);
        labelPane.add(labelDetailPane, BorderLayout.CENTER);
    }

    protected void addComponents() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{isLabelShow},
                new Component[]{labelPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);

        isLabelShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxUse();
            }
        });
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    private void checkBoxUse() {
        labelPane.setVisible(isLabelShow.isSelected());
        if(checkEnabled4Large()) {
            isLabelShow.setEnabled(!PlotFactory.largeDataModel(plot));
        }
    }

    protected boolean checkEnabled4Large() {
        return true;
    }

    public void populate(AttrLabel attr) {
        if(attr == null) {
            attr = ((VanChartPlot)this.plot).getDefaultAttrLabel();
        }
        isLabelShow.setSelected(attr.isEnable());

        labelDetailPane.populate(attr.getAttrLabelDetail());

        checkBoxUse();
    }

    public AttrLabel update() {
        //刪除返回null,否則無法保存不顯示標籤的屬性
        AttrLabel attrLabel = ((VanChartPlot)this.plot).getDefaultAttrLabel();
        attrLabel.setEnable(isLabelShow.isSelected());

        labelDetailPane.update(attrLabel.getAttrLabelDetail());

        return attrLabel;
    }

}