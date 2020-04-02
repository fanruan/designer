package com.fr.van.chart.funnel.designer.style;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import com.fr.plugin.chart.funnel.VanChartFunnelPlot;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by Mitisky on 16/10/10.
 */
public class VanChartFunnelSeriesPane extends VanChartAbstractPlotSeriesPane {
    private UIButtonGroup useSameSlantAngle;
    private UIButtonGroup sort;
    public VanChartFunnelSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p,p,p,p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createFunnelStylePane()},
                new Component[]{createBorderPane()},
        };

        contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        return contentPane;
    }

    //设置色彩面板内容
    protected void setColorPaneContent (JPanel panel) {
        if (stylePane != null) {
            panel.add(stylePane, BorderLayout.CENTER);
        }
        panel.add(createAlphaPane(), BorderLayout.SOUTH);
    }

    private JPanel createFunnelStylePane() {
        useSameSlantAngle = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Same_Slant_Angle"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Diff_Slant_Angle")});
        sort = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Auto_Sort"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Origin")});

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Continuity")), useSameSlantAngle},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Sort")), sort},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Name"), panel);
    }

    public void populateBean(Plot plot) {
        if(plot == null) {
            return;
        }

        super.populateBean(plot);

        if(plot instanceof VanChartFunnelPlot){
            useSameSlantAngle.setSelectedIndex(((VanChartFunnelPlot) plot).isUseSameSlantAngle() ? 0 : 1);
            sort.setSelectedIndex(((VanChartFunnelPlot) plot).isSort() ? 0 : 1);
        }

    }



    public void updateBean(Plot plot) {
        if(plot == null) {
            return;
        }

        super.updateBean(plot);

        if(plot instanceof VanChartFunnelPlot){
            ((VanChartFunnelPlot) plot).setUseSameSlantAngle(useSameSlantAngle.getSelectedIndex() == 0);
            ((VanChartFunnelPlot) plot).setSort(sort.getSelectedIndex() == 0);
        }
    }
}
