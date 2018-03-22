package com.fr.extended.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by shine on 2018/3/12.
 */
public class ExtendedOtherPane extends AbstractChartAttrPane {

    private ExtendedChartHyperLinkPane hyperLinkPane;
    private UISpinner autoRefreshTime;

    @Override
    public void populate(ChartCollection collection) {
        if (collection != null) {
            Chart chart = collection.getSelectedChart();
            if (chart != null && chart instanceof AbstractChart) {
                hyperLinkPane.populateBean((AbstractChart) chart);
                autoRefreshTime.setValue(((AbstractChart) chart).getAutoRefreshTime());
            }
        }

    }

    @Override
    public void update(ChartCollection collection) {
        if (collection != null) {
            Chart chart = collection.getSelectedChart();
            if (chart != null && chart instanceof AbstractChart) {
                hyperLinkPane.updateBean((AbstractChart) chart);
                ((AbstractChart) chart).setAutoRefreshTime(autoRefreshTime.getValue());
            }
        }
    }

    @Override
    protected JPanel createContentPane() {
        autoRefreshTime = new UISpinner(0, Integer.MAX_VALUE, 1, 0);


        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p, p, p, p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{autoRefreshTime, null},
                new Component[]{createHyperlinkPane(), null}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    @Override
    public String getIconPath() {
        return null;
    }

    private JPanel createHyperlinkPane() {
        hyperLinkPane = new ExtendedChartHyperLinkPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("M_Insert-Hyperlink"), hyperLinkPane);
    }

    @Override
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_OTHER_TITLE;
    }

}
