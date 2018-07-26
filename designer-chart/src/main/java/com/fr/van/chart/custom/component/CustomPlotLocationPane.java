package com.fr.van.chart.custom.component;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.plugin.chart.PiePlot4VanChart;
import com.fr.plugin.chart.attr.plot.VanChartPositionPlot;
import com.fr.plugin.chart.base.Position;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by Fangjie on 2016/4/26.
 */
public class CustomPlotLocationPane extends BasicBeanPane<Plot>{
    private UISpinner radius;
    private UISpinner xDirection;
    private UISpinner yDirection;
    private static final double MIN_ANGLE = PiePlot4VanChart.START_ANGLE;
    private static final double MAX_ANGLE = PiePlot4VanChart.END_ANGLE;

    public CustomPlotLocationPane(){
        init();
    }

    private void init() {
        radius = new UISpinner(MIN_ANGLE, MAX_ANGLE, 1, 50);

        xDirection = new UISpinner(0, 100, 1, 20);
        yDirection = new UISpinner(0, 100, 1, 20);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Position") + "(%):  " + com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_X_Direction"), SwingConstants.LEFT),xDirection},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Y_Direction"), SwingConstants.RIGHT),yDirection},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Radius") + "(px):   ",SwingConstants.LEFT),radius}

        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, new double[]{-2, -2, -2}, new double[]{-2, -1});

        this.setLayout(new BorderLayout(0,0));

        this.add(TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Location"), panel), BorderLayout.CENTER);
    }

    @Override
    public void populateBean(Plot plot) {
        if (plot instanceof VanChartPositionPlot) {
            Position position = ((VanChartPositionPlot) plot).getPosition();

            if (position != null) {
                radius.setValue(position.getRadius());
                xDirection.setValue(position.getX());
                yDirection.setValue(position.getY());
            }
        }
    }

    @Override
    public void updateBean(Plot plot) {
        if (plot instanceof VanChartPositionPlot) {
            Position position = new Position();
            position.setRadius(radius.getValue());
            position.setX(xDirection.getValue());
            position.setY(yDirection.getValue());

            ((VanChartPositionPlot) plot).setPosition(position);
        }
    }

    @Override
    public Plot updateBean() {
        return null;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
