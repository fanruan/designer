package com.fr.design.chart.gui.active.action;

import java.awt.event.ActionEvent;

import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.mainframe.chart.ChartEditPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.general.Inter;

public class SetDataLabelStyleAction extends ChartComponentAction{

	public SetDataLabelStyleAction(ChartComponent chartComponent) {
		super(chartComponent);
		this.setName(Inter.getLocText(new String[]{"Set", "Data-Label"}));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		showDataLabelStylePane();
	}
	
	public void showDataLabelStylePane() {
    	ChartEditPane.getInstance().GoToPane(PaneTitleConstants.CHART_STYLE_TITLE, PaneTitleConstants.CHART_STYLE_LABEL_TITLE);
    }

}