/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.poly;

import com.fr.base.ScreenResolution;
import com.fr.base.chart.BaseChartCollection;
import com.fr.log.FineLoggerFactory;
import com.fr.poly.creator.BlockCreator;
import com.fr.poly.creator.ChartBlockCreator;
import com.fr.poly.creator.ECBlockCreator;
import com.fr.poly.model.AddedData;
import com.fr.report.poly.PolyChartBlock;
import com.fr.report.poly.PolyECBlock;
import com.fr.report.poly.TemplateBlock;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

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
		if (block == null) {
			return null;
		}
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
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
		}
		return null;
	}

	public static BlockCreator createCreator(Class clazz) {
		return createCreator(blockGenerate(clazz));
	}

	public static BlockCreator createCreator(BaseChartCollection cc) {
		TemplateBlock block = new PolyChartBlock(cc);
		return createCreator(block);
	}

	private static TemplateBlock blockGenerate(Class clazz) {
		TemplateBlock block = null;
		try {
			block = (TemplateBlock) clazz.newInstance();
		} catch (Exception e) {
			FineLoggerFactory.getLogger().error(e.getMessage(), e);
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
			float times = (float) designer.getResolution()/ScreenResolution.getScreenResolution();
			int cx = (int) (creator.getX() * times);
			int cy = (int) (creator.getY() * times);
			int cw = (int) (creator.getWidth() * times);
			int ch = (int) (creator.getHeight() * times);
			if (x >= cx && x <= (cx + cw)) {
				if(y >= cy && y <= (cy + ch)) {
					return creator;
				}
			}
		}
		return null;
	}
}
