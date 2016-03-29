/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.insert.flot;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.actions.ElementCaseAction;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.module.DesignModuleFactory;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.stable.Constants;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.unit.OLDPIX;

import javax.swing.*;
import java.awt.*;

/**
 * 图表插入悬浮元素的操作.
 */
public class ChartFloatAction extends ElementCaseAction {

    /**
     * 构造函数 图表插入悬浮元素
     */
	public ChartFloatAction(ElementCasePane t) {
		super(t);
        this.setMenuKeySet(FLOAT_INSERT_CHART);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_insert/chart.png"));
    }

	public static final MenuKeySet FLOAT_INSERT_CHART = new MenuKeySet() {
		@Override
		public char getMnemonic() {
			return 'C';
		}

		@Override
		public String getMenuName() {
			return Inter.getLocText("M_Insert-Chart");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};

    /**
     * 执行插入悬浮元素操作, 并返回true, 需要记录撤销.
     * @return 是则返回true
     */
	public boolean executeActionReturnUndoRecordNeeded() {
		final ElementCasePane reportPane = getEditingComponent();
		if (reportPane == null) {
			return false;
		}
		
		reportPane.stopEditing();
		
		final BaseChartCollection cc = (BaseChartCollection)StableFactory.createXmlObject(BaseChartCollection.XML_TAG);
		cc.removeAllNameObject();
		final MiddleChartDialog chartDialog = DesignModuleFactory.getChartDialog(DesignerContext.getDesignerFrame());
		chartDialog.populate(cc);
		chartDialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				
				FloatElement newFloatElement;
				try {
					newFloatElement = new FloatElement(chartDialog.getChartCollection().clone());
					newFloatElement.setLeftDistance(new OLDPIX(20));
					newFloatElement.setTopDistance(new OLDPIX(20));
					newFloatElement.setWidth(new OLDPIX(BaseChartCollection.CHART_DEFAULT_WIDTH));
					newFloatElement.setHeight(new OLDPIX(BaseChartCollection.CHART_DEFAULT_HEIGHT));
					
					Style style = newFloatElement.getStyle();
					if(style != null) {
						newFloatElement.setStyle(style.deriveBorder(Constants.LINE_NONE, Color.black, 
								Constants.LINE_NONE, Color.black, 
								Constants.LINE_NONE, Color.black, 
								Constants.LINE_NONE, Color.black));
					}
					reportPane.getEditingElementCase().addFloatElement(newFloatElement);
					reportPane.setSelection(new FloatSelection(newFloatElement.getName()));
					reportPane.fireTargetModified();
					reportPane.fireSelectionChangeListener();
				} catch (CloneNotSupportedException e) {
					FRLogger.getLogger().error("Error in Float");
				}
			}
		});
		
		chartDialog.setVisible(true);
		return true;
	}
}