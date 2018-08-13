package com.fr.design.chart.series.SeriesCondition;


import com.fr.chart.chartglyph.CustomAttr;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;

import java.awt.*;

public class DataSeriesAttrCustomPane extends DataSeriesAttrPane {
	private static final long serialVersionUID = -9046019835977910412L;

	public DataSeriesAttrCustomPane() {
		super();

		// 重新设定大小. JControlPane中的(450, 450) 稍小  @ChartSize
		this.setPreferredSize(new Dimension(640,450));
	}
	
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] {
				new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Condition_Attributes"), CustomAttr.class, DataSeriesCustomConditionPane.class)
		};
	}
}