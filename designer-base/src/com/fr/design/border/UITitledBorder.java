package com.fr.design.border;

import com.fr.design.constants.UIConstants;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class UITitledBorder extends TitledBorder {

	private static final long serialVersionUID = 1L;

	public static UITitledBorder createBorderWithTitle(String title) {
		return new UITitledBorder(title);
	}

	private UITitledBorder(String title) {
		super(null, title, 4, 2, null, new Color(1, 159, 222));
		init(title);
	}
	
	private void init(String title){
		UIRoundedBorder roundedborder = new UIRoundedBorder(UIConstants.TITLED_BORDER_COLOR, 1, 10);
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0), roundedborder));
		
		this.setTitle(title);
	}
	
}