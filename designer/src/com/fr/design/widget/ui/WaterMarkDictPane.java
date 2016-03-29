package com.fr.design.widget.ui;

import java.awt.event.KeyListener;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.WaterMark;
import com.fr.general.Inter;

public class WaterMarkDictPane extends JPanel{
	
	private UITextField waterMarkTextField;
	
	public WaterMarkDictPane() {
		this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
		this.add(new UILabel(Inter.getLocText("WaterMark") + ":"));
		waterMarkTextField = new UITextField(16);
		this.add(waterMarkTextField);
	}
	
	public void populate(WaterMark waterMark) {
		this.waterMarkTextField.setText(waterMark.getWaterMark());
	}
	public void addInputKeyListener(KeyListener kl) {
		this.waterMarkTextField.addKeyListener(kl);
	}
	public void removeInputKeyListener(KeyListener kl) {
		this.waterMarkTextField.removeKeyListener(kl);
	}
	public void update(WaterMark waterMark) {
		waterMark.setWaterMark(this.waterMarkTextField.getText());
	}
	public void setWaterMark(String waterMark) {
		this.waterMarkTextField.setText(waterMark);
	}
	public String getWaterMark() {
		return this.waterMarkTextField.getText();
	}
	
}