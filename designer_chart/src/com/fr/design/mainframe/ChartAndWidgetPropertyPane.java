/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itabpane.TitleChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.Inter;

public class ChartAndWidgetPropertyPane extends MiddleChartPropertyPane {

	public synchronized static ChartAndWidgetPropertyPane getInstance(BaseFormDesigner formEditor) {
		if(singleton == null) {
			singleton = new ChartAndWidgetPropertyPane(formEditor);
		} 
		
		singleton.setWidgetPropertyPane(DesignModuleFactory.getWidgetPropertyPane(formEditor));
		singleton.setSureProperty();
		return singleton;
	}

	public static ChartAndWidgetPropertyPane getInstance() {
		if(singleton == null) {
			singleton = new ChartAndWidgetPropertyPane();
		}
		return singleton;
	}

	private static ChartAndWidgetPropertyPane singleton;

	private BaseWidgetPropertyPane widgetpane = null;
	
	private UIToggleButton hisButton;

	public ChartAndWidgetPropertyPane() {

	}
	
	public ChartAndWidgetPropertyPane(BaseFormDesigner formEditor) {
		super();
		this.widgetpane = DesignModuleFactory.getWidgetPropertyPane(formEditor);
	}
	
	public void setWidgetPropertyPane(BaseWidgetPropertyPane pane) {
		this.widgetpane = pane;
	}
	
	@Override
	protected void createMainPane() {
		this.add(chartEditPane, BorderLayout.CENTER);
	}
	
	@Override
	protected void createNameLabel() {
		nameLabel = new UILabel();
		nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		nameLabel.setBorder(BorderFactory.createEmptyBorder(-2, 6, 2, 0));
	}

	@Override
	protected JComponent createNorthComponent() {
		JPanel toolPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

		JPanel hisPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		hisPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		hisButton = new UIToggleButton(Inter.getLocText(new String[]{"Widget", "Attribute"}), UIConstants.HISTORY_ICON);
		hisButton.setNormalPainted(false);
		hisButton.setBorderPaintedOnlyWhenPressed(true);
		hisPane.add(hisButton, BorderLayout.CENTER);

		hisButton.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				cardChange();
			}
		});

		toolPane.add(nameLabel, BorderLayout.CENTER);
		toolPane.add(hisPane, BorderLayout.EAST);

        titleListener = new TitleChangeListener() {

            @Override
            public void fireTitleChange(String addName) {
                if(hisButton.isSelected()) {
                    nameLabel.setText(Inter.getLocText(new String[]{"Widget", "Form-Widget_Property_Table"}));
                }else {
                    nameLabel.setText(Inter.getLocText("Chart-Property_Table") + '-' + addName);
                }
            }
        };
		
		return toolPane;
	}

    /**
     * 感觉ChartCollection加载图表属性界面.
     * @param collection  收集图表
     * @param ePane  面板
     */
    public void populateChartPropertyPane(ChartCollection collection, TargetComponent<?> ePane) {
        super.populateChartPropertyPane(collection, ePane);
        //表单中的图表切换界面上得更新
        resetChartEditPane();
    }

    protected void resetChartEditPane() {
       cardChange();
    }
	
	private void cardChange() {
		remove(chartEditPane);
		remove((Component)widgetpane);
		if(hisButton.isSelected()) {
			nameLabel.setText(Inter.getLocText(new String[]{"Widget", "Form-Widget_Property_Table"}));
			add((Component)widgetpane, BorderLayout.CENTER);
		} else {
			String tabname = chartEditPane.getSelectedTabName();
			nameLabel.setText(Inter.getLocText(new String[]{"Utils-The-Chart", "Form-Widget_Property_Table"}) + (tabname != null ? ('-' + chartEditPane.getSelectedTabName()) : ""));
			add(chartEditPane, BorderLayout.CENTER);
		}
		validate();
		repaint();
		revalidate();
	}
}