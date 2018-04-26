package com.fr.design.module;

import com.fr.base.ChartPreStyleManagerProvider;
import com.fr.base.ChartPreStyleServerManager;
import com.fr.chart.base.ChartPreStyle;
import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.chart.gui.style.ChartPreFillStylePane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * 图表预定义风格界面,  服务器--图表风格.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-8-20 下午05:02:00
 */
public class ChartPreStylePane extends BasicBeanPane<ChartPreStyle>{
	
	private ChartPreFillStylePane fillStylePane;
	private ChartComponent chartComponent;
	
	public ChartPreStylePane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		fillStylePane = new ChartPreFillStylePane();

		JPanel pane = new JPanel();
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));

		pane.add(new BoldFontTextLabel(Inter.getLocText("FR-Designer_Preview")));

        JPanel boxPane = new JPanel();
        boxPane.setLayout(new BorderLayout());
        boxPane.add(pane, BorderLayout.SOUTH);

        boxPane.add(fillStylePane, BorderLayout.CENTER);
        
        this.add(boxPane, BorderLayout.CENTER);
		
		ChartCollection cc = new ChartCollection();
		cc.addChart(new Chart(new Bar2DPlot()));
		
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
	
	private void refreshWhenStyleChange(ChartPreStyle preStyle) {
		ChartPreStyleManagerProvider manager = ChartPreStyleServerManager.getProviderInstance();
		manager.setStyleEditing(preStyle);
		if(chartComponent != null) {
			chartComponent.reset();
		}
	}
	
	@Override
	public void populateBean(ChartPreStyle preStyle) {

		fillStylePane.populateBean(preStyle.getAttrFillStyle());
		
		refreshWhenStyleChange(preStyle);
	}

	@Override
	public ChartPreStyle updateBean() {
		ChartPreStyle preStyle = new ChartPreStyle();
		preStyle.setAttrFillStyle(fillStylePane.updateBean());
		return preStyle;
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("ServerM-Predefined_Styles");
	}

}