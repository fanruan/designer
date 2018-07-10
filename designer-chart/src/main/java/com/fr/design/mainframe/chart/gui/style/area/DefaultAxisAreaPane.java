package com.fr.design.mainframe.chart.gui.style.area;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartattr.RectanglePlot;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.design.style.color.ColorSelectBox;

import javax.swing.*;
import java.awt.*;

public class DefaultAxisAreaPane extends ChartAxisAreaPane {
	
	private JPanel backgroundPane;
	private ColorSelectBox horizontalColorPane;
	private ColorSelectBox verticalColorPane;
	
	private JPanel gridlinePane;
	private UICheckBox isVerticleGridLine;
	private UICheckBox isHorizontalGridLine;
	private ColorSelectBox gridColorPane;

	public DefaultAxisAreaPane() {
		initBackgroundColorPane();
		initGridlinePane();
		
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f };
		double[] rowSize = {p,p,p,p};

        Component[][] components = new Component[][]{
                new Component[]{backgroundPane},
                new Component[]{new JSeparator()},
                new Component[]{gridlinePane},
                
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
	}

	//初始化间隔背景
	private void initBackgroundColorPane(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] rowSize = {p,p};
		double[] columnSize = { p, f };
		
		horizontalColorPane = new ColorSelectBox(100);
		verticalColorPane = new ColorSelectBox(100);
		
		Component[][] components = new Component[][]{
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Utils-Left_to_Right") + ":"),horizontalColorPane},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Utils-Top_to_Bottom") + ":"),verticalColorPane},
        };
		
		backgroundPane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Chart_Interval_Back"}, components, rowSize, columnSize);
	}
	
	//初始化网格线
	private void initGridlinePane(){
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] rowSize = {p,p};
		double[] columnSize = { p, f };
		
		isVerticleGridLine = new UICheckBox(Inter.getLocText("Utils-Left_to_Right"));
		isHorizontalGridLine = new UICheckBox(Inter.getLocText("Utils-Top_to_Bottom"));
		gridColorPane = new ColorSelectBox(100);
		
		JPanel container = TableLayoutHelper.createTableLayoutPane(new Component[][]{
				 new Component[]{new BoldFontTextLabel(Inter.getLocText("Color")),gridColorPane},}
																					, rowSize, columnSize);
		
		Component[][] components = new Component[][]{
                new Component[]{isVerticleGridLine,isHorizontalGridLine},
                new Component[]{container,null}
        };
		
		gridlinePane = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"ChartF-Grid_Line"}, components, rowSize, columnSize);
		
	}
	
	/**
	 * 更新间隔背景界面.
	 */
	public void populateBean(Plot plot) {
		isVerticleGridLine.setSelected(plot.getyAxis().getMainGridStyle() != Constants.LINE_NONE);
		isHorizontalGridLine.setSelected(plot.getxAxis().getMainGridStyle() != Constants.LINE_NONE);
		gridColorPane.setSelectObject(isVerticleGridLine.isSelected() ? plot.getyAxis().getMainGridColor() : plot.getxAxis().getMainGridColor());
		horizontalColorPane.setSelectObject(((RectanglePlot)plot).getHorizontalIntervalBackgroundColor());
		verticalColorPane.setSelectObject(((RectanglePlot)plot).getVerticalIntervalBackgroundColor());
	}

	/**
	 * 保存间隔背景的界面属性.
	 */
	public void updateBean(Plot plot) {
		if(isVerticleGridLine.isSelected()) {
			plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
			plot.getyAxis().setMainGridColor(gridColorPane.getSelectObject());
		} else {
			plot.getyAxis().setMainGridStyle(Constants.LINE_NONE);
		}
		if(isHorizontalGridLine.isSelected()) {
			plot.getxAxis().setMainGridStyle(Constants.LINE_THIN);
			plot.getxAxis().setMainGridColor(gridColorPane.getSelectObject());
		} else {
			plot.getxAxis().setMainGridStyle(Constants.LINE_NONE);
		}
		((RectanglePlot)plot).setHorizontalIntervalBackgroundColor(horizontalColorPane.getSelectObject());
		((RectanglePlot)plot).setVerticalIntervalBackgroundColor(verticalColorPane.getSelectObject());
	}
}