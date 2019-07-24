package com.fr.design.module;

import com.fr.base.ChartColorMatching;
import com.fr.chart.base.ChartUtils;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * 图表预定义风格界面,  服务器--图表风格.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-8-20 下午05:02:00
 */
public class ChartPreStylePane extends BasicBeanPane<ChartColorMatching> {

	private ChartPreFillStylePane fillStylePane;
	private ChartComponent chartComponent;
	private Bar2DPlot demoPlot;

	public ChartPreStylePane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		fillStylePane = new ChartPreFillStylePane();

		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));

		pane.add(new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview")));

        JPanel boxPane = new JPanel();
        boxPane.setLayout(new BorderLayout());
        boxPane.add(pane, BorderLayout.SOUTH);

        boxPane.add(fillStylePane, BorderLayout.CENTER);
        
        this.add(boxPane, BorderLayout.CENTER);
		
		ChartCollection cc = new ChartCollection();
		demoPlot = new Bar2DPlot();
		cc.addChart(new Chart(demoPlot));

		chartComponent = new ChartComponent();
		chartComponent.populate(cc);
		chartComponent.setPreferredSize(new Dimension(400, 300));
		chartComponent.setSupportEdit(false);
		
		this.add(chartComponent, BorderLayout.SOUTH);
		
		initListener(ChartPreStylePane.this);
	}
	
	private void initListener(Container parentComponent) {
		for (int i = 0; i < parentComponent.getComponentCount(); i++) {
			Component tmpComp = parentComponent.getComponent(i);

			if (tmpComp instanceof Container) {
				initListener((Container) tmpComp);
			}
			if (tmpComp instanceof UIObserver) {
				((UIObserver) tmpComp).registerChangeListener(new UIObserverListener() {
					@Override
					public void doChange() {
						refreshWhenStyleChange(updateBean());
					}
				});
			}
		}
	}

	private void refreshWhenStyleChange(ChartColorMatching preStyle) {
		if(chartComponent != null) {
			demoPlot.setPlotFillStyle(ChartUtils.chartColorMatching2AttrFillStyle(preStyle));
			chartComponent.reset();
		}
	}
	
	@Override
	public void populateBean(ChartColorMatching preStyle) {

		fillStylePane.populateBean(preStyle);

		refreshWhenStyleChange(preStyle);
	}

	@Override
	public ChartColorMatching updateBean() {
		return fillStylePane.updateBean();
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ServerM_Predefined_Styles");
	}

}
