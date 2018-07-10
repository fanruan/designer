package com.fr.design.chart.demo;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.*;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.*;
import java.awt.*;

public class GlyphDemo extends JFrame {
	private static final long serialVersionUID = -3174315376448330927L;

	/**
	 * 主函数
	 * @param args 参数
	 */
	public static void main(String[] args) {
		new GlyphDemo();
	}

	public GlyphDemo() {
		JPanel contentPane = (JPanel)this.getContentPane();
		contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());
		UITabbedPane tab = new UITabbedPane();
		contentPane.add(tab, BorderLayout.CENTER);

		tab.add("Line", createGlyphLinePanel());
		tab.add("Bar", createBarPanel());
		tab.add("Area", createAreaPanel());
		tab.add("Meter", createMeterPanel());
		tab.add("Pie", createPiePanel());
		tab.add("Radar", createRadarPanel());
		tab.add("Stock", createStockPanel());
		tab.add("XYScatter", createXYScatterPanel());
		tab.add("Range", createRangePanel());
		tab.add("Mix", createCustomPanel());
		tab.add("Gantt", createGanttPanel());
		tab.add("Bubble", createBubblePane());

		setSize(1200, 600);
		// 关闭时退出
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setTitle("Line Glyph Demo");
		setVisible(true);
	}

	private JPanel createGlyphLinePanel() {
		Chart[] chr = LineIndependentChart.lineChartTypes;

		return  contentPane(chr,2,4);
	}

	private JPanel createBarPanel() {
		Chart[] chr= ColumnIndependentChart.columnChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel createAreaPanel() {
		Chart[] chr= AreaIndependentChart.areaChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel createMeterPanel() {
		Chart[] chr= MeterIndependentChart.meterChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel createPiePanel() {
		Chart[] chr = PieIndependentChart.pieChartTypes;
		JPanel contentPane = contentPane(chr,2,4);

		return contentPane;
	}

	private JPanel createRadarPanel() {
		Chart[] chr = RadarIndependentChart.radarChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel createStockPanel() {
		Chart[] chr = StockIndependentChart.stockChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel createXYScatterPanel() {
		Chart[] chr = XYScatterIndependentChart.XYScatterChartTypes;
		return  contentPane(chr,2,4);
	}

	private JPanel createRangePanel() {
		Chart[] chr = RangeIndependentChart.rangeChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel createCustomPanel() {
		Chart[] chr = CustomIndependentChart.combChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel createGanttPanel() {
		Chart[] chr = GanttIndependentChart.ganttChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel createBubblePane() {
		Chart[] chr = BubbleIndependentChart.bubbleChartTypes;
		return contentPane(chr,2,4);
	}

	private JPanel contentPane(Chart[] chr,int x,int y){
		JPanel contentPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		contentPane.setLayout(new /**/ GridLayout(x, y));
		for(int i=0;i<chr.length;i++){
			contentPane.add(new ChartComponent(new ChartCollection(chr[i])));
		}

		return contentPane;
	}
}