package com.fr.design.widget.ui.designer;

import com.fr.design.gui.itextfield.UIPropertyTextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.WaterMark;

import javax.swing.*;

public class WaterMarkDictPane extends JPanel{
	
	private UIPropertyTextField waterMarkTextField;
	
	public WaterMarkDictPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		waterMarkTextField = new UIPropertyTextField();
		this.add(waterMarkTextField);
	}
	
	public void populate(WaterMark waterMark) {
		this.waterMarkTextField.setText(waterMark.getWaterMark());
	}

	public void update(WaterMark waterMark) {
		waterMark.setWaterMark(this.waterMarkTextField.getText());
	}

	
}