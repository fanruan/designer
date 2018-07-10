package com.fr.design.mainframe.chart.gui.other;

import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.CustomPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.CustomAttr;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.general.Inter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ChartConditionAttrPane extends BasicScrollPane<Chart> {
	private static final long serialVersionUID = 5725969986029470291L;
	private UICorrelationComboBoxPane conditionPane;
	
	public ChartConditionAttrPane() {
		super();
	}
	
	@Override
	protected JPanel createContentPane() {
		if (conditionPane == null) {
			conditionPane = new UICorrelationComboBoxPane();
		}
		
		return conditionPane;
	}

    /**
     * 面板标题
     * @return 标题
     */
	public String title4PopupWindow() {
		return Inter.getLocText("Chart-Condition_Display");
	}
	
	@Override
	public void populateBean(Chart chart) {
		Plot plot = chart.getPlot();
		Class<? extends ConditionAttributesPane> showPane = ChartTypeInterfaceManager.getInstance().getPlotConditionPane(chart.getPlot()).getClass();
		List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();
		
		if(plot instanceof CustomPlot) {
			list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Condition_Attributes"), new CustomAttr(), showPane));
		} else {
			list.add(new UIMenuNameableCreator(Inter.getLocText("Chart-Condition_Attributes"), new ConditionAttr(), showPane));
		}
		
		conditionPane.refreshMenuAndAddMenuAction(list);
		
		ConditionCollection collection = chart.getPlot().getConditionCollection();
		List<UIMenuNameableCreator> valueList = new ArrayList<UIMenuNameableCreator>();
		
		for(int i = 0; i < collection.getConditionAttrSize(); i++) {
			valueList.add(new UIMenuNameableCreator(collection.getConditionAttr(i).getName(), collection.getConditionAttr(i), showPane));
		}
		
		conditionPane.populateBean(valueList);
		conditionPane.doLayout();
	}

	@Override
	public void updateBean(Chart chart) {
		List<UIMenuNameableCreator> list = conditionPane.updateBean();
		
		ConditionCollection cc = chart.getPlot().getConditionCollection();
		
		cc.clearConditionAttr();
		for(int i = 0; i < list.size(); i++) {
			UIMenuNameableCreator nameMenu = list.get(i);
			ConditionAttr ca = (ConditionAttr)nameMenu.getObj();
			ca.setName(nameMenu.getName());
			cc.addConditionAttr(ca);
		}
	}
}