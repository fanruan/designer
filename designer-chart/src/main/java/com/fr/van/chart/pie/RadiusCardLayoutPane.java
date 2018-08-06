package com.fr.van.chart.pie;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.plugin.chart.attr.radius.VanChartRadiusPlot;
import com.fr.plugin.chart.base.VanChartRadius;
import com.fr.plugin.chart.type.RadiusType;
import com.fr.van.chart.layout.VanChartCardLayoutPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hufan on 2016/10/25.
 */
public class RadiusCardLayoutPane extends BasicBeanPane<Plot> {
    private static final double MIN_RADIUS = 0;
    private static final double MAX_RADIUS = Double.MAX_VALUE;

    private UIButtonGroup radiusType;//半径类型
    private JPanel radiusContent;//半径的布局界面
    private UISpinner radius;//半径值

    private JPanel nullPane;//自动布局界面

    private VanChartCardLayoutPane cardPane;//卡片布局界面

    public RadiusCardLayoutPane() {
        super();

        Map<String, Component> paneList = new HashMap<String, Component>();

        radiusType = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Auto"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Fixed")});
        radius = new UISpinner(MIN_RADIUS, MAX_RADIUS, 1, 100);

        radiusContent = new JPanel(new BorderLayout());
        radiusContent.add(radius, BorderLayout.CENTER);
        radiusContent.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        nullPane = new JPanel();

        paneList.put("auto", nullPane);
        paneList.put("fixed", radiusContent);

        radiusType.setSelectedIndex(0);

        cardPane = new VanChartCardLayoutPane(paneList, "auto"){
            @Override
            public Dimension getPreferredSize() {
                if (radiusType.getSelectedIndex() == 1) {
                    return radiusContent.getPreferredSize();
                }else {
                    return new Dimension(0, 0);
                }
            }
        };

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{radiusType,null},
                new Component[]{cardPane, null},
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

        this.setLayout(new BorderLayout(0,0));

        this.add(panel, BorderLayout.CENTER);

    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    @Override
    public void populateBean(Plot plot) {
        if (plot instanceof VanChartRadiusPlot){
            VanChartRadiusPlot radiusPlot = (VanChartRadiusPlot) plot;
            VanChartRadius vanChartRadius = radiusPlot.getRadius();
            radiusType.setSelectedIndex(vanChartRadius.getRadiusType() == RadiusType.AUTO ? 0 : 1);
            radius.setValue(vanChartRadius.getRadius());
            //设置card显示界面
            cardPane.updatePane(radiusType.getSelectedIndex() == 0 ? "auto" : "fixed");
        }
    }

    public void updateBean(Plot plot) {
        //更新半径
        if (plot instanceof VanChartRadiusPlot){
            VanChartRadiusPlot radiusPlot = (VanChartRadiusPlot) plot;
            VanChartRadius vanChartRadius = radiusPlot.getRadius();
            vanChartRadius.setRadiusType(radiusType.getSelectedIndex() == 0 ? RadiusType.AUTO : RadiusType.FIXED);
            vanChartRadius.setRadius((int) radius.getValue());
            cardPane.updatePane(radiusType.getSelectedIndex() == 0 ? "auto" : "fixed");
        }
    }

    @Override
    public Plot updateBean() {
        return null;
    }
}
