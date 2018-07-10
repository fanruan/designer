package com.fr.design.layout;

import java.awt.GridLayout;

public class FRGridLayout extends GridLayout {

	private static final long serialVersionUID = 1L;
	
	protected FRGridLayout(int n){
		super(0, n, 0, 8);
	}
	
	protected FRGridLayout(int n, int h, int v) {
		super(0, n, h, v);
	}
}