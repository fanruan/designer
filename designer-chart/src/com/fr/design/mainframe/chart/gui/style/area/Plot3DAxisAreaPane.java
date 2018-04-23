package com.fr.design.mainframe.chart.gui.style.area;

import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.design.style.color.ColorSelectBox;

import javax.swing.*;
import java.awt.*;

public class Plot3DAxisAreaPane extends ChartAxisAreaPane {

	private UICheckBox gridLine;
	private ColorSelectBox gridColorPane;

	public Plot3DAxisAreaPane() {
        gridLine = new UICheckBox(Inter.getLocText("ChartF-Grid_Line"));
		gridColorPane = new ColorSelectBox(100);

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { p, f };
		double[] rowSize = {p,p};

        Component[][] components = new Component[][]{
                new Component[]{gridLine,null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Color")),gridColorPane,}
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
	}

	/**
	 * 更新间隔背景界面.
	 */
	public void populateBean(Plot plot) {
		gridLine.setSelected(plot.getyAxis().getMainGridStyle() != Constants.LINE_NONE);
		gridColorPane.setSelectObject(plot.getyAxis().getMainGridColor());
	}

	/**
	 * 保存间隔背景的界面属性.
	 */
	public void updateBean(Plot plot) {
		if(gridLine.isSelected()) {
			plot.getyAxis().setMainGridStyle(Constants.LINE_THIN);
			plot.getyAxis().setMainGridColor(gridColorPane.getSelectObject());
		} else {
			plot.getyAxis().setMainGridStyle(Constants.LINE_NONE);
		}
	}
}