package com.fr.design.mainframe.chart.gui.style.area;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.AbstractChartTabPane;
import com.fr.design.mainframe.chart.gui.style.ChartBackgroundNoImagePane;
import com.fr.design.mainframe.chart.gui.style.ChartBackgroundPane;
import com.fr.design.mainframe.chart.gui.style.ChartBorderPane;

import javax.swing.*;
import java.awt.*;

public class ChartPlotAreaPane extends AbstractChartTabPane<Chart>{
	private static final long serialVersionUID = -2589129242557082314L;
	private static final int WIDTH = 220;
	private ChartBackgroundPane plotBackgroundPane;
	private ChartBackgroundNoImagePane plotNoImagePane;
	private ChartAxisAreaPane axisAreaPane;

    private ChartBorderPane plotBorderPane;

	private JPanel contentPane;
	private ChartStylePane parent;
	
	public ChartPlotAreaPane() {
	}

    /**
     * 标题
     * @return 标题
     */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_AREA_PLOT_TITLE;
	}
	
	public void setParentPane(ChartStylePane parent) {
		this.parent = parent;
	}
	
	protected JPanel createContentPane() {
		contentPane = new JPanel();
		
		plotBackgroundPane = new ChartBackgroundPane();
		plotNoImagePane = new ChartBackgroundNoImagePane();
        plotBorderPane = new ChartBorderPane();

        contentPane.setLayout(new BorderLayout());

        contentPane.add(plotBackgroundPane, BorderLayout.CENTER);

		return contentPane;
	}

	@Override
	public void populateBean(Chart chart) {
		if(chart == null) {
			return;
		}
		Plot plot = chart.getPlot();
		if(plot == null) {
			return;
		}
		refreshContentPane(plot);
		
		if(plot.is3D()) {
			plotNoImagePane.populate(plot);
		} else {
			plotBackgroundPane.populate(plot);
		}
		
		if(axisAreaPane != null) {
			axisAreaPane.populateBean(plot);
		}

        if(plotBorderPane != null) {
            plotBorderPane.populate(plot);
        }
	}

    private JPanel initIntervalBackPane(Plot plot) {
        if(plot.isSupportIntervalBackground()) {
            if (plot.isOnlyIntervalBackground()) {
                axisAreaPane = new RadarAxisAreaPane();
            } else if(plot.is3D()){
                axisAreaPane = new Plot3DAxisAreaPane();
            } else {
                axisAreaPane = new DefaultAxisAreaPane();
            }
        } else {
            if(axisAreaPane != null) {
                axisAreaPane = null;
            }
        }

        return axisAreaPane;
    }

    private JPanel initPlotBackgroundPane(Plot plot) {
        return plot.is3D() ? plotNoImagePane : plotBackgroundPane;
    }

    private JPanel initPlotBorderPane(Plot plot) {
        return plot.isSupportBorder() ? plotBorderPane : null;
    }
	
	private void refreshContentPane(Plot plot) {
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = {p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{initPlotBorderPane(plot)},
                new Component[]{initPlotBackgroundPane(plot)},
                new Component[]{initIntervalBackPane(plot)}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        contentPane.add(panel, BorderLayout.CENTER);
		
		parent.initAllListeners();
	}
	
	@Override
	public void updateBean(Chart chart) {
		Plot plot = chart.getPlot();
		
		if(plot.is3D()) {
			plotNoImagePane.update(plot);
		} else {
			plotBackgroundPane.update(plot);
		}
		if(axisAreaPane != null) {
			axisAreaPane.updateBean(plot);
		} 
        if(plotBorderPane != null) {
            plotBorderPane.update(plot);
        }
	}

	@Override
	public Chart updateBean() {
		return null;
	}
}