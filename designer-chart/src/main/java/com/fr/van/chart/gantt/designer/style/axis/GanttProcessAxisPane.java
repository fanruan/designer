package com.fr.van.chart.gantt.designer.style.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.plugin.chart.gantt.attr.GanttProcessAxis;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.AbstractVanChartScrollPane;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by hufan on 2017/1/12.
 */
public class GanttProcessAxisPane extends AbstractVanChartScrollPane<VanChart> {
    private UIButtonGroup typeButton;
    private UISpinner horizontalProportion;

    private GanttAxisStylePane horizontalHeaderPane;
    private GanttAxisStylePane verticalHeaderPane;
    private GanttAxisStylePane bodyPane;

    @Override
    protected JPanel createContentPane() {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p,p,p,p,p,p,p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{createHorizontalProportionPane()},
                new Component[]{createHorizontalHeaderPane()},
                new Component[]{createVerticalHeaderPane()},
                new Component[]{createBodyPane()}
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        this.add(leftcontentPane);
    }

    private Component createBodyPane() {
        bodyPane = new GanttAxisStylePane();

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Content"), bodyPane);
    }

    private Component createHorizontalHeaderPane() {
        horizontalHeaderPane = new GanttAxisStylePane();

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Horizontal_Table"), horizontalHeaderPane);
    }

    private Component createVerticalHeaderPane() {
        verticalHeaderPane = new GanttAxisStylePane();

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Vertical_Table"), verticalHeaderPane);
    }

    private Component createHorizontalProportionPane() {
        typeButton = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Auto"), com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Custom")});
        horizontalProportion = new UISpinner(0, 100, 1, 30);

        JPanel proportionPane = new JPanel(new BorderLayout(5, 0));
        proportionPane.add(horizontalProportion, BorderLayout.CENTER);
        proportionPane.add(new UILabel("%"), BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout(0, 5));

        panel.add(typeButton, BorderLayout.NORTH);
        panel.add(horizontalProportion, BorderLayout.CENTER);

        typeButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkoutPaneVisible();
            }
        });

        JPanel jPanel = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Horizontal_Proportion"), panel);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Chart-DataFunction_Percent"), jPanel);
    }

    private void checkoutPaneVisible() {
        horizontalProportion.setVisible(typeButton.getSelectedIndex() == 1);
    }

    @Override
    public void populateBean(VanChart chart) {
        Plot plot = chart.getPlot();
        if (plot instanceof VanChartGanttPlot){
            VanChartGanttPlot ganttPlot = (VanChartGanttPlot) plot;
            GanttProcessAxis processAxis = ganttPlot.getProcessAxis();

            typeButton.setSelectedIndex(processAxis.isCustomProportion() ? 1 : 0);
            horizontalProportion.setValue(processAxis.getProportion());

            horizontalHeaderPane.populateBean(processAxis.getHorizontalHeaderStyle());
            verticalHeaderPane.populateBean(processAxis.getVerticalHeaderStyle());
            bodyPane.populateBean(processAxis.getBodyStyle());
        }
        checkoutPaneVisible();
    }

    @Override
    public void updateBean(VanChart vanChart){
        VanChartGanttPlot ganttPlot = (VanChartGanttPlot) vanChart.getPlot();
        GanttProcessAxis processAxis = new GanttProcessAxis();

        processAxis.setCustomProportion(typeButton.getSelectedIndex() == 1);
        processAxis.setProportion(horizontalProportion.getValue());

        horizontalHeaderPane.updateBean(processAxis.getHorizontalHeaderStyle());
        verticalHeaderPane.updateBean(processAxis.getVerticalHeaderStyle());
        bodyPane.updateBean(processAxis.getBodyStyle());

        ganttPlot.setProcessAxis(processAxis);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Project_Axis");
    }
}
