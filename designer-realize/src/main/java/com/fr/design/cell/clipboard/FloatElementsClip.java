/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.clipboard;

import java.util.Iterator;

import com.fr.base.FRContext;
import com.fr.design.cell.FloatElementsProvider;
import com.fr.general.ComparatorUtils;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.OLDPIX;

/**
 * The clip of Float Element.
 */
public class FloatElementsClip implements Cloneable, java.io.Serializable,FloatElementsProvider {
	private FloatElement floatEl;

    public FloatElementsClip(FloatElement floatEl) {
    	this.floatEl = floatEl;
    }
    
    /**
     * 悬浮元素的粘贴
     * 
     * @param ec 单元格
     * @return 粘贴的悬浮元素
     */
    public FloatSelection pasteAt(TemplateElementCase ec) {
    	if (floatEl == null) {
    		return null;
    	}
    	FloatElement ret;
		try {
			ret = (FloatElement)this.floatEl.clone();
		} catch (CloneNotSupportedException e) {
			FRContext.getLogger().error(e.getMessage(), e);
			return null;
		}
		
    	while (ec.getFloatElement(ret.getName()) != null) {
    		ret.setName(ret.getName() + "-Copy");
    	}
		
		while (true) {
			if (isContainSameBoundFloatElement(ec, ret)) {
				ret.setTopDistance(FU.getInstance(ret.getTopDistance().toFU() + new OLDPIX(50).toFU()));
				ret.setLeftDistance(FU.getInstance(ret.getLeftDistance().toFU() + new OLDPIX(50).toFU()));
			} else {
				break;
			}
		}
		
		ec.addFloatElement(ret);
		
		return new FloatSelection(ret.getName());
    }

	/**
	 * Contain same location and bounds FloatElement.
	 */
	private static boolean isContainSameBoundFloatElement(ElementCase report, FloatElement oFloatElement) {
		if (oFloatElement == null) {
			return false;
		}

		Iterator flotIt = report.floatIterator();
		while (flotIt.hasNext()) {
			FloatElement tmpFloatElement = (FloatElement) flotIt.next();
			if (hasSameDistance(tmpFloatElement,oFloatElement)) {
				return true;
			}
		}

		return false;
	}
	
	private static boolean hasSameDistance(FloatElement tmpFloatElement,FloatElement oFloatElement){
		return (ComparatorUtils.equals(tmpFloatElement.getTopDistance(), oFloatElement.getTopDistance())
				&& ComparatorUtils.equals(tmpFloatElement.getLeftDistance(), oFloatElement.getLeftDistance())
				&& ComparatorUtils.equals(tmpFloatElement.getWidth(), oFloatElement.getWidth())
				&& ComparatorUtils.equals(tmpFloatElement.getHeight(), oFloatElement.getHeight()));
	}

    /**
     * Clone.
     */
    @Override
	public Object clone() throws CloneNotSupportedException {
        FloatElementsClip cloned = (FloatElementsClip) super.clone();
        
        if (floatEl != null) {
        	cloned.floatEl = (FloatElement)this.floatEl.clone();
        }

        return cloned;
    }
}