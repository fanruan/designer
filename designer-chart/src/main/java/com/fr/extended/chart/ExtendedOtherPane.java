package com.fr.extended.chart;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.AbstractChartAttrPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by shine on 2018/3/12.
 */
public class ExtendedOtherPane extends AbstractChartAttrPane {

    private ExtendedChartHyperLinkPane hyperLinkPane;
    private UIButtonGroup refreshEnabled;
    private UISpinner autoRefreshTime;
    private JPanel contentPane;

    @Override
    public void populate(ChartCollection collection) {
        if (collection != null) {
            Chart chart = collection.getSelectedChart();
            if (chart != null && chart instanceof AbstractChart) {
                hyperLinkPane.populateBean((AbstractChart) chart);
                autoRefreshTime.setValue(((AbstractChart) chart).getAutoRefreshTime());
                refreshEnabled.setSelectedIndex(((AbstractChart) chart).isRefreshEnabled() ? 0 : 1);
                checkRefreshEnable();
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
                ((AbstractChart) chart).setRefreshEnabled(refreshEnabled.getSelectedIndex() == 0);
            }
        }
    }

    private void checkRefreshEnable() {
        contentPane.setVisible(refreshEnabled.getSelectedIndex() == 0);
    }

    @Override
    protected JPanel createContentPane() {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p, p, p, p, p, p};

        Component[][] components = new Component[][]{
                new Component[]{createRefreshPane(), null},
                new Component[]{createHyperlinkPane(), null}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    @Override
    public String getIconPath() {
        return null;
    }

    private JPanel createRefreshPane() {

        refreshEnabled = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});
        refreshEnabled.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkRefreshEnable();
            }
        });

        autoRefreshTime = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f, 20};
        double[] rowSize = {p};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Time_Interval")), autoRefreshTime, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Time_Seconds"))},
        };
        contentPane = TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);

        JPanel panel = new JPanel(new BorderLayout(0,4));
        panel.add(refreshEnabled, BorderLayout.NORTH);
        panel.add(contentPane, BorderLayout.CENTER);

        JPanel gapPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Auto_Refresh"), panel);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Refresh"), gapPane);
    }

    private JPanel createHyperlinkPane() {
        hyperLinkPane = new ExtendedChartHyperLinkPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Insert_Hyperlink"), hyperLinkPane);
    }

    @Override
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_OTHER_TITLE;
    }

}
