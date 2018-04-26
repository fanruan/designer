package com.fr.design.gui.controlpane;

import com.fr.design.menu.ShortCut;

public abstract class ShortCut4JControlPane {
	protected ShortCut shortCut;
	
	public ShortCut getShortCut() {
		return shortCut;
	}

    /**
     * 检查是否可用
     */
	public abstract void checkEnable();
}