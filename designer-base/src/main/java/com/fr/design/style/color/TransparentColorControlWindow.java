package com.fr.design.style.color;

import com.fr.design.gui.ipoppane.PopupHider;

/**
 * 支持透明的colorControlWindow
 * 
 * @author focus
 * @time 2014年11月13日下午7:44:11
 * @version
 */
public abstract class TransparentColorControlWindow extends  ColorControlWindow{
	protected abstract void colorChanged();

	/**
	 * constructor
	 * 
	 * @param popupHider
	 */
	public TransparentColorControlWindow(PopupHider popupHider) {
		super(true, popupHider);
	}

}