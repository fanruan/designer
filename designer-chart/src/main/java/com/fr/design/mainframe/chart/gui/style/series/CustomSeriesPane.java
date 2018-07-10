package com.fr.design.mainframe.chart.gui.style.series;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.fr.chart.chartattr.CustomPlot;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.chart.chartglyph.ConditionCollection;
import com.fr.chart.chartglyph.CustomAttr;
import com.fr.design.gui.frpane.UICorrelationComboBoxPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.imenutable.UIMenuNameableCreator;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import com.fr.general.Inter;

/**
 * 组合图, 属性表, 图表样式-系列.
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-6 上午10:37:22
 */
public class CustomSeriesPane extends AbstractPlotSeriesPane {

    private ChartBeautyPane stylePane;
	private CustomDefaultSeriesPane defaultSeriesStyle;
    private UICorrelationComboBoxPane conditionPane;

	public CustomSeriesPane(ChartStylePane parent, Plot plot) {
		super(parent, plot, false);
	}

	protected JPanel getContentInPlotType() {
        stylePane = new ChartBeautyPane();
		defaultSeriesStyle = new CustomDefaultSeriesPane();
        conditionPane = new UICorrelationComboBoxPane();
		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		double[] columnSize = { f};
		double[] rowSize = { p, p, p, p, p, p, p};
        Component[][] components = new Component[][]{
        		new Component[]{stylePane},
        	    new Component[]{new JSeparator()},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Series_Use_Default"))},
                new Component[]{defaultSeriesStyle},
                new Component[]{new JSeparator()},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Add_Series_Setting"), SwingConstants.LEFT)},
                new Component[]{conditionPane}
        } ;

		return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}
	
	public void populateBean(Plot plot) {
		super.populateBean(plot);
		if(plot instanceof CustomPlot) {
			CustomPlot custom = (CustomPlot)plot;
			if(stylePane != null) {
				stylePane.populateBean(custom.getPlotStyle());
			}

            if(defaultSeriesStyle != null){
                defaultSeriesStyle.populateBean((CustomAttr)custom.getCustomTypeCondition().getDefaultAttr());
            }

            List<UIMenuNameableCreator> list = new ArrayList<UIMenuNameableCreator>();
            list.add(new UIMenuNameableCreator(Inter.getLocText("FR-Chart-Series_Setting"), new CustomAttr(), CustomTypeConditionSeriesPane.class));

            conditionPane.refreshMenuAndAddMenuAction(list);

            ConditionCollection collection = custom.getCustomTypeCondition();
            List<UIMenuNameableCreator> valueList = new ArrayList<UIMenuNameableCreator>();

            for(int i = 0; i < collection.getConditionAttrSize(); i++) {
                valueList.add(new UIMenuNameableCreator(collection.getConditionAttr(i).getName(), collection.getConditionAttr(i), CustomTypeConditionSeriesPane.class));
            }

            conditionPane.populateBean(valueList);
            conditionPane.doLayout();
		}
	}
	
	public void updateBean(Plot plot) {
		super.updateBean(plot);
		if(plot instanceof CustomPlot) {
			CustomPlot custom = (CustomPlot)plot;
			if(stylePane != null) {
				custom.setPlotStyle(stylePane.updateBean());
			}

            if(defaultSeriesStyle != null){
                defaultSeriesStyle.updateBean((CustomAttr)custom.getCustomTypeCondition().getDefaultAttr());
            }

            List<UIMenuNameableCreator> list = conditionPane.updateBean();
            ConditionCollection cc = custom.getCustomTypeCondition();
            cc.clearConditionAttr();
            for(int i = 0; i < list.size(); i++) {
                UIMenuNameableCreator nameMenu = list.get(i);
                ConditionAttr ca = (ConditionAttr)nameMenu.getObj();
                ca.setName(nameMenu.getName());
                cc.addConditionAttr(ca);
            }
		}
	}
}