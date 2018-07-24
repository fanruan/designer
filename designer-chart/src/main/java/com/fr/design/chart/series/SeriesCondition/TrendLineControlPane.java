package com.fr.design.chart.series.SeriesCondition;


import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.chart.base.AttrTrendLine;
import com.fr.chart.base.ConditionTrendLine;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;

import java.util.ArrayList;
import java.util.List;

public class TrendLineControlPane extends JListControlPane {

	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] {
        		new NameObjectCreator(
    				com.fr.design.i18n.Toolkit.i18nText("Chart_TrendLine"),
    				ConditionTrendLine.class, 
    				ConditionTrendLinePane.class
        		)
		};
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Edit", "Chart_TrendLine"});
	}
	
	public void populate(AttrTrendLine trendLineList) {
		List<NameObject> nameObjectList = new ArrayList<NameObject>();
		
		for(int i = 0; i < trendLineList.size(); i++) {
			ConditionTrendLine value = trendLineList.get(i);
			nameObjectList.add(new NameObject(value.getPaneName(), value));
		}
		
		if(nameObjectList.size() > 0) { 
			populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
		}
	}
	
	public void update(AttrTrendLine trendLineList) {
		Nameable[] res = update();
		NameObject[] res_array = new NameObject[res.length];
		java.util.Arrays.asList(res).toArray(res_array);
		
		trendLineList.clear();
		
		if (res_array.length < 1) {
			return;
		}
		
		for (int i = 0; i < res_array.length; i++) {
			NameObject nameObject = res_array[i];
			
			ConditionTrendLine trendLine = (ConditionTrendLine)nameObject.getObject();
			trendLine.setPaneName(nameObject.getName());
			trendLineList.add(trendLine);
		}
	}
}