package com.fr.poly;

import javax.swing.JComponent;

public class PolyArea extends JComponent {
	private PolyDesigner polyDesigner;

	public PolyArea(PolyDesigner polyDesigner) {
		this.polyDesigner = polyDesigner;
		setUI(new PolyDesignUI());
	}

	public PolyDesigner getPolyDesigner() {
		return polyDesigner;
	}
}