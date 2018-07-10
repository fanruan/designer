package com.fr.design.style.background.gradient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.background.GradientBackground;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.design.style.CustomSelectBox;
import com.fr.design.style.background.BackgroundJComponent;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-10 下午02:52:40
 * 类说明: 渐变色 选择Box
 */
public class GradientSelectBox extends BasicPane {
	private static final long serialVersionUID = -3133347878480990266L;
	private CustomSelectBox startBox;
	private CustomSelectBox endBox;
	private BackgroundJComponent displayComponent;
	
	private Color startColor = Color.black;
	private Color endColor = Color.WHITE;
	
	public GradientSelectBox() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		this.add(startBox = new CustomSelectBox(Constants.LEFT));
		startBox.setSelectObject(Color.black);
		
		this.add(displayComponent = new BackgroundJComponent());
		displayComponent.setSelfBackground(new GradientBackground(Color.black, Color.WHITE, 0));
		displayComponent.setPreferredSize(new Dimension(80, 15));
		
		this.add(endBox = new CustomSelectBox(Constants.RIGHT));
		endBox.setSelectObject(Color.WHITE);
		
		startBox.addSelectChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				startColor = startBox.getSelectObject();
				refreshBackground();
			}
		});
		
		endBox.addSelectChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				endColor = endBox.getSelectObject();
				
				refreshBackground();
			}
		});
	}
	
	private void refreshBackground() {
		displayComponent.setSelfBackground(new GradientBackground(startColor, endColor, 0));
		displayComponent.repaint();
	}

	protected String title4PopupWindow() {
		return Inter.getLocText("Gradient-Color");
	}
	
	public void populate(GradientBackground background) {
		startColor = background.getStartColor();
		endColor = background.getEndColor();
		
		startBox.setSelectObject(startColor);
		endBox.setSelectObject(endColor);
		
		displayComponent.setSelfBackground(new GradientBackground(startColor, endColor, 0));
		displayComponent.repaint();
	}
	
	public void update(GradientBackground background) {
		background.setStartColor(startColor);
		background.setEndColor(endColor);
	}
}