package com.fr.plugin.chart.custom.other;

import com.fr.chart.chartattr.Chart;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.plugin.chart.custom.VanChartCustomPlot;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Fangjie on 2016/4/28.
 */
public class VanChartCustomPlotHyperlinkPane extends BasicScrollPane<Chart> {
    private VanChartCustomPlotHyperlinkTabPane linkPane;
    protected Chart chart;
    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(chart == null) {
            return contentPane;
        }
        initLinkPane((VanChartCustomPlot) chart.getPlot());
        if(linkPane != null) {
            contentPane.add(linkPane, BorderLayout.CENTER);
        }
        return contentPane;
    }

    private void initLinkPane(VanChartCustomPlot plot) {
        linkPane = new VanChartCustomPlotHyperlinkTabPane(plot, null);
    }

    @Override
    public void populateBean(Chart chart) {
        this.chart = chart;
        if(linkPane == null) {
            this.remove(leftcontentPane);
            layoutContentPane();
        }
        if(linkPane != null) {
            linkPane.populateBean((VanChartCustomPlot)chart.getPlot());
        }
    }

    @Override
    public void updateBean(Chart chart) {
        this.chart = chart;

        if(linkPane != null) {
            linkPane.updateBean((VanChartCustomPlot) chart.getPlot());
        }
    }

    @Override
    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        this.setLayout(new BorderLayout());
        this.add(leftcontentPane, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
