package com.fr.design.gui.ilable;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import com.fr.design.gui.ilable.UILabel;

public class FRExplainLabel extends UILabel {

	private static final long serialVersionUID = 1L;
	
	private Icon icon = new ImageIcon("/com/fr/design/images/warnings/warning3.png");
	
	public FRExplainLabel(String string){
		super(string, UILabel.CENTER);
		init();
	}
	
	private void init(){
		setIcon(icon);
		setForeground(new Color(255, 0, 0));
	}
}