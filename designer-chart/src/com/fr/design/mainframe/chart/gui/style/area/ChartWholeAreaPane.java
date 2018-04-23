package com.fr.design.mainframe.chart.gui.style.area;


import com.fr.chart.chartattr.Chart;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.AbstractChartTabPane;
import com.fr.design.mainframe.chart.gui.style.ChartBackgroundPane;
import com.fr.design.mainframe.chart.gui.style.ChartBorderPane;

import javax.swing.*;
import java.awt.*;

public class ChartWholeAreaPane extends AbstractChartTabPane<Chart>{
	private static final long serialVersionUID = -9034253033977926565L;
	private ChartBorderPane chartBorderPane;
	private ChartBackgroundPane chargBackgroundPane;
	
	private class ContentPane extends JPanel {
		private static final long serialVersionUID = -9062394733909781995L;

		public ContentPane() {
			initComponents();
		}

		private void initComponents() {
			chartBorderPane = new ChartBorderPane();
			chargBackgroundPane = new ChartBackgroundPane();

			double p = TableLayout.PREFERRED;
			double f = TableLayout.FILL;
			double[] columnSize = { f };
			double[] rowSize = {p,p,p};
            Component[][] components = new Component[][]{
                    new Component[]{chartBorderPane},
                    new Component[]{chargBackgroundPane}
            };
            JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
            this.setLayout(new BorderLayout());
            this.add(panel,BorderLayout.CENTER);



		}
	}
	
	@Override
	protected JPanel createContentPane() {
		return new ContentPane();
	}

    /**
     *       标题
     *    @return 标题
     */
	public String title4PopupWindow() {
		return PaneTitleConstants.CHART_STYLE_AREA_AREA_TITLE;
	}

	@Override
	public void updateBean(Chart chart) {
		if (chart == null) {
			chart = new Chart();
		}
		chartBorderPane.update(chart);
		chargBackgroundPane.update(chart);
	}

	@Override
	public void populateBean(Chart chart) {
		if(chart == null) {
			return;
		}
		chartBorderPane.populate(chart);
		chargBackgroundPane.populate(chart);
	}

	@Override
	public Chart updateBean() {
		return null;
	}
}