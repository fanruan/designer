/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.style.datalabel;

import com.fr.chart.base.TextAttr;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrNoColorPane;
import com.fr.design.dialog.BasicScrollPane;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 14-9-10
 * Time: 下午6:00
 */
public class ChartLabelFontPane extends BasicScrollPane<Chart> {
    private ChartTextAttrNoColorPane valueTextAttrPane;
    private ChartTextAttrNoColorPane unitTextAttrPane;
    private ChartTextAttrNoColorPane cateTextAttrPane;
    private UICheckBox categoryName;

    @Override
    protected JPanel createContentPane() {
        return new ContentPane();
    }

    @Override
    public void populateBean(Chart ob) {
        Plot meterPlot = ob.getPlot();
        TextAttr valueTextAttr = meterPlot.getValueTextAttr();
        TextAttr unitTextAttr = meterPlot.getUnitTextAttr();
        TextAttr cateTextAttr = meterPlot.getCategoryNameTextAttr();
        if (unitTextAttr == null) {
            unitTextAttr = new TextAttr();
        }
        unitTextAttrPane.populate(unitTextAttr);
        if (valueTextAttr == null) {
            valueTextAttr = new TextAttr();
        }
        valueTextAttrPane.populate(valueTextAttr);
        if (cateTextAttr == null) {
            cateTextAttr = new TextAttr();
        }
        cateTextAttrPane.populate(cateTextAttr);
        categoryName.setSelected(meterPlot.isShowCateName());
        cateTextAttrPane.setEnabled(meterPlot.isShowCateName());
    }


    public void updateBean(Chart chart) {
        if (chart == null) {
            chart = new Chart();
        }
        Plot meterPlot = chart.getPlot();
        TextAttr valueTextAttr = meterPlot.getValueTextAttr();
        TextAttr unitTextAttr = meterPlot.getUnitTextAttr();
        TextAttr cateTextAttr = meterPlot.getCategoryNameTextAttr();
        if (unitTextAttr == null) {
            unitTextAttr = new TextAttr();
        }
        unitTextAttrPane.update(unitTextAttr);
        if (valueTextAttr == null) {
            valueTextAttr = new TextAttr();
        }
        valueTextAttrPane.update(valueTextAttr);
        if (cateTextAttr == null) {
            cateTextAttr = new TextAttr();
        }
        cateTextAttrPane.update(cateTextAttr);
        meterPlot.setShowCateName(categoryName.isSelected());
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Label");
    }

    private class ContentPane extends JPanel {
        private UILabel value = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Value"));
        private UILabel unit = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Units"));

        public ContentPane() {
            this.initComponents();
        }

        private void initComponents() {
            this.setLayout(new BorderLayout());
            this.add(createValuePane(), BorderLayout.CENTER);
        }


        private JPanel createValuePane() {
            valueTextAttrPane = new ChartTextAttrNoColorPane();
            unitTextAttrPane = new ChartTextAttrNoColorPane();
            cateTextAttrPane = new ChartTextAttrNoColorPane();
            categoryName = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Category_Name"));
            categoryName.setSelected(true);
            categoryName.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cateTextAttrPane.setEnabled(categoryName.isSelected());
                }
            });

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] columnSize = {LayoutConstants.CHART_ATTR_TOMARGIN, f};
            double[] rowSize = {p, p, p, p, p, p, p, p, p};
            Component[][] components = new Component[][]{
                    new Component[]{value, null},
                    new Component[]{null, valueTextAttrPane},
                    new Component[]{new JSeparator(), null},
                    new Component[]{unit, null},
                    new Component[]{null, unitTextAttrPane},
                    new Component[]{new JSeparator(), null},
                    new Component[]{categoryName, null},
                    new Component[]{null, cateTextAttrPane},
                    new Component[]{new JSeparator(), null}
            };

            return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        }
    }
}