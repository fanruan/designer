package com.fr.design.style.color;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;

public class RecentUseColorPane extends BasicPane implements ColorSelectable{
	
	JColorChooser chooser;
	
	@Override
	protected String title4PopupWindow() {
		return null;
	}
	
	public RecentUseColorPane() {
	}
	
	public RecentUseColorPane(JColorChooser chooser) {
		this.chooser = chooser;
		
		// center
		JPanel centerPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		this.add(centerPane, BorderLayout.CENTER);
		// 最近使用
		UsedColorPane pane = new UsedColorPane(2, 10, ColorSelectConfigManager.getInstance().getColors(),this);
		centerPane.add(pane.getPane());
	}

	/**
	 * 选中颜色
	 * 
	 * @param 颜色
	 */
	@Override
	public void colorSetted(ColorCell color) {
		
	}

	@Override
	public Color getColor() {
		return null;
	}

	@Override
	public void setColor(Color color) {
		chooser.getSelectionModel().setSelectedColor(color);
	}
	
}