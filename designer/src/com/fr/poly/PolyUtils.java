/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.fr.base.FRContext;
import com.fr.base.chart.BaseChart;
import com.fr.base.chart.BaseChartCollection;
import com.fr.base.chart.BasePlot;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.creator.ChartBlockCreator;
import com.fr.poly.creator.ECBlockCreator;
import com.fr.poly.model.AddedData;
import com.fr.report.poly.PolyChartBlock;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.poly.TemplateBlock;
import com.fr.stable.bridge.StableFactory;

/**
 * @author richer
 * @since 6.5.3 聚合报表设计界面工具类
 */
public class PolyUtils {
	public static Map<Class, Class> blockMapCls = new HashMap<Class, Class>();

	static {
		blockMapCls.put(PolyECBlock.class, ECBlockCreator.class);
		blockMapCls.put(PolyChartBlock.class, ChartBlockCreator.class);
	}

	public static BlockCreator createCreator(TemplateBlock block) {
		Class cls = block.getClass();
		Class clazz = blockMapCls.get(cls);
		if (clazz == null) {
			return null;
		}
		Constructor c = null;
		try {
			c = clazz.getConstructor(cls);
			return (BlockCreator) c.newInstance(block);
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}
		return null;
	}

	public static BlockCreator createCreator(Class clazz) {
		return createCreator(blockGenerate(clazz));
	}
	
	public static BlockCreator createCreator(BaseChart chart) {
		BaseChartCollection cc = (BaseChartCollection)StableFactory.createXmlObject(BaseChartCollection.XML_TAG);
		cc.addChart(chart);
		TemplateBlock block = new PolyChartBlock(cc);
		return createCreator(block);
	}
	
	private static TemplateBlock blockGenerate(Class clazz) {
		TemplateBlock block = null;
		try {
			block = (TemplateBlock) clazz.newInstance();
		} catch (Exception e) {
			try {
				BasePlot plot = (BasePlot)clazz.newInstance();
				BaseChartCollection  cc = (BaseChartCollection)StableFactory.createXmlObject(BaseChartCollection.XML_TAG);
				
				BaseChart chart = (BaseChart)StableFactory.createXmlObject(BaseChart.XML_TAG);
				chart.initChart(plot);
				cc.addChart(chart);
				
				block = new PolyChartBlock(cc);
			} catch (InstantiationException e1) {
				FRContext.getLogger().error(e1.getMessage(), e1);
			} catch (IllegalAccessException e1) {
				FRContext.getLogger().error(e1.getMessage(), e1);
			}
		}
		return block;
	}

	public static Point convertPoint2Designer(PolyDesigner designer, Point p) {
		int hh = designer.getHorizontalValue();
		int vv = designer.getVerticalValue();
		p.x += hh;
		p.y += vv;
		return p;
	}

	public static Point convertPoint2Designer(PolyDesigner designer, int x, int y) {
		return convertPoint2Designer(designer, new Point(x, y));
	}

	public static int convertx2Designer(PolyDesigner designer, int x) {
		return x += designer.getHorizontalValue();
	}

	public static int converty2Designer(PolyDesigner designer, int y) {
		return y += designer.getVerticalValue();
	}
	
	/**
	 * 获取所处(x,y)位置的聚合块，如果没有，就返回null
	 * @param designer
	 * @param x
	 * @param y
	 * @return
	 */
	public static BlockCreator searchAt(PolyDesigner designer,int x, int y) {
		AddedData addedData = designer.getAddedData();
		for (int count = addedData.getAddedCount() - 1; count >= 0; count--) {
			BlockCreator creator = addedData.getAddedAt(count);
			int cx = creator.getX();
			int cy = creator.getY();
			int cw = creator.getWidth();
			int ch = creator.getHeight();
			if (x >= cx && x <= (cx + cw)) {
				if(y >= cy && y <= (cy + ch)) {
					return creator;
				}
			}
		}
		return null;
	}
}