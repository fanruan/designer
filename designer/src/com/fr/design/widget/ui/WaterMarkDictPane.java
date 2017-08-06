package com.fr.design.widget.ui;

import java.awt.*;
import java.awt.event.KeyListener;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.WaterMark;
import com.fr.general.Inter;

public class WaterMarkDictPane extends JPanel{
	
	private UITextField waterMarkTextField;
	
	public WaterMarkDictPane() {
		this.setLayout(new BorderLayout());

//		this.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
//		this.add(new UILabel(Inter.getLocText("WaterMark") + ":"));
		waterMarkTextField = new UITextField(13);

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark") + "      "), waterMarkTextField },
		};
		double[] rowSize = {p, p};
		double[] columnSize = {p,f};
		int[][] rowCount = {{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_MEDIUM);
		panel.setBorder(BorderFactory.createEmptyBorder(10,0,5,0));
		this.add(panel , BorderLayout.CENTER);
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