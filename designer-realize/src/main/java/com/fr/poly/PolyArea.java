package com.fr.poly;


import javax.swing.JComponent;

public class PolyArea extends JComponent {
	private PolyDesigner polyDesigner;
	private int resolution;

	public PolyArea(PolyDesigner polyDesigner, int resolution) {
		this.polyDesigner = polyDesigner;
		this.resolution = resolution;
		this.setUI(new PolyDesignUI(resolution));
//		setUI(new PolyDesignUI());
	}


	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

	public int getResolution() {
		return this.resolution;
	}


	public PolyDesigner getPolyDesigner() {
		return polyDesigner;
	}
}