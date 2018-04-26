package com.fr.design.menu;

import javax.swing.*;
import javax.swing.JToolBar.Separator;
import java.awt.*;

/**
 * Separator.
 */
public class SeparatorDef extends ShortCut  {
	
	public static SeparatorDef DEFAULT = new SeparatorDef();

	private static final Dimension SEPARATOR_DIMENSION = new Dimension(2, 16);// Separator分隔线
	
	private SeparatorDef() {
		
	}

	/*
	 * Add this ShortCut into JPopupMenu
	 */
	public void intoJPopupMenu(JPopupMenu menu) {
		menu.addSeparator();
	}

	/*
	 * Add this ShortCut into JToolBar
	 */
	public void intoJToolBar(JToolBar toolBar) {
		Separator separator = new Separator(SEPARATOR_DIMENSION);
		toolBar.add(separator);
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setEnabled(boolean b) {
		// do nothing
	}

}