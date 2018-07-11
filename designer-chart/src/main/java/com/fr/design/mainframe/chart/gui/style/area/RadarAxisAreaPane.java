package com.fr.design.mainframe.chart.gui.style.area;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RadarPlot;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.design.style.color.ColorSelectBox;

public class RadarAxisAreaPane extends ChartAxisAreaPane {

	private ColorSelectBox horizontalColorPane;
	private UICheckBox isHorizontalGridLine;
	private ColorSelectBox gridColorPane;

	public RadarAxisAreaPane() {
		horizontalColorPane = new ColorSelectBox(100);
		isHorizontalGridLine = new UICheckBox(Inter.getLocText("Chart_Main_Grid"));
		gridColorPane = new ColorSelectBox(100);

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = {p,p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart_Interval_Back") + ":"),horizontalColorPane},
                new Component[]{new JSeparator(),null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart_Main_Grid")),null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Color")),gridColorPane,}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
	}

	/**
	 * 更新间隔背景界面.
	 */
	public void populateBean(Plot p) {
		RadarPlot plot = (RadarPlot)p;
		isHorizontalGridLine.setSelected(plot.getxAxis().getMainGridStyle() != Constants.LINE_NONE);
		gridColorPane.setSelectObject(plot.getxAxis().getMainGridColor());
		horizontalColorPane.setSelectObject(plot.getIntervalColor());
	}

	/**
	 * 保存间隔背景的界面属性.
	 */
	public void updateBean(Plot p) {
		RadarPlot plot = (RadarPlot)p;
		if(isHorizontalGridLine.isSelected()) {
			plot.getxAxis().setMainGridStyle(Constants.LINE_THIN);
			plot.getxAxis().setMainGridColor(gridColorPane.getSelectObject());
		} else {
			plot.getxAxis().setMainGridStyle(Constants.LINE_NONE);
		}
		plot.setIntervalColor(horizontalColorPane.getSelectObject());
	}
}