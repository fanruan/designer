package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
/**
 * 属性表, 图表样式 - 系列 界面, 通过重载继承每个不同的方法, 得到内容的组合.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-5 上午11:07:51
 */
public abstract class AbstractPlotSeriesPane extends BasicBeanPane<Plot>{
	protected ChartFillStylePane fillStylePane;
	protected Plot plot;
	protected ChartStylePane parentPane;
	protected Chart chart;//地图有用,需要数据定义

    public AbstractPlotSeriesPane(ChartStylePane parent, Plot plot) {
        this(parent, plot, false);
    }

    public AbstractPlotSeriesPane(ChartStylePane parent, Plot plot, boolean custom) {
        this.plot = plot;
        this.parentPane = parent;
        fillStylePane = getFillStylePane();

        this.setLayout(new BorderLayout());
        this.add(getContentPane(custom), BorderLayout.CENTER);
    }

    protected JPanel getContentPane(boolean custom) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p, p, p};
        Component[][] components = new Component[3][1];

        JPanel panel;

        if (custom) {
            if (!(plot instanceof Bar2DPlot)) {
                components[0] = new Component[]{getContentInPlotType()};
                components[1] = new Component[]{new JSeparator()};
            }

            panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(panel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        } else {
            if (fillStylePane != null) {
                components[0] = new Component[]{fillStylePane};
                components[1] = new Component[]{new JSeparator()};
            }

            JPanel contentPane = getContentInPlotType();
            if (contentPane != null) {
                components[2] = new Component[]{contentPane};
            }

            panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
        return panel;

    }

    /**
	 * 在每个不同类型Plot, 得到不同类型的属性. 比如: 柱形的风格, 折线的线型曲线.
	 */
	protected abstract JPanel getContentInPlotType();

	/**
	 * 返回 填充界面.
	 */
	protected ChartFillStylePane getFillStylePane() {
		return new ChartFillStylePane();
	}

	/**
	 * 界面标题.
	 */
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Chart-Data_Series");
	}

	/**
	 * 重载 donothing
	 */
	public Plot updateBean() {
		return null;
	}


	/**
	 * 更新Plot的属性到系列界面
	 */
	public void populateBean(Plot plot) {
		if(plot == null) {
			return;
		}
		if(fillStylePane != null) {
			fillStylePane.populateBean(plot.getPlotFillStyle());
		}
	}

	/**
	 * 保存 系列界面的属性到Plot
	 */
	public void updateBean(Plot plot) {
		if(plot == null) {
			return;
		}
		if(fillStylePane != null) {
			plot.setPlotFillStyle(fillStylePane.updateBean());
		}
	}

	/**
	 * 设置当前的chart，只有地图有用
	 * @param chart 当前的chart
	 */
	public void setCurrentChart(Chart chart){
		this.chart = chart;
	}
}