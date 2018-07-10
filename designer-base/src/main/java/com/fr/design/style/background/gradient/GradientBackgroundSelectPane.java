package com.fr.design.style.background.gradient;

import java.awt.BorderLayout;
import java.awt.Dimension;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.base.background.GradientBackground;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.AlphaPane;
import com.fr.design.style.background.BackgroundPane4BoxChange;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-2 下午04:07:22
 * 类说明: 渐变色背景的选择界面  bug原型图@5471
 */
// TODO 改成泛型 (Background)
public class GradientBackgroundSelectPane extends BackgroundPane4BoxChange {
	private static final long serialVersionUID = -2762854865690293473L;
	
	private static final String LEFT_2_RIGHT = Inter.getLocText("PageSetup-Landscape");
	private static final String TOP_2_BOTTOM = Inter.getLocText("PageSetup-Portrait");
	
	private static final String[] COLOR_DIRECTIONS = {
		LEFT_2_RIGHT, TOP_2_BOTTOM
	};
	
	
	private AlphaPane alphaPane;
	private UIComboBox colorDirctionBox;
	
	private GradientSelectBox gradientBox;
	
	private GradientChangeBoxPane changeBox;
	private UICheckBox repeatShow;
	
	public GradientBackgroundSelectPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel mainPane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
		this.add(mainPane, BorderLayout.NORTH);
		
		JPanel firstFloorPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		mainPane.add(firstFloorPane);
		
		firstFloorPane.add(gradientBox = new GradientSelectBox());
		firstFloorPane.add(colorDirctionBox = new UIComboBox(COLOR_DIRECTIONS));
		firstFloorPane.add(alphaPane = new AlphaPane());
		
		colorDirctionBox.setPreferredSize(new Dimension(60, 20));
		
		JPanel secondFloorPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		mainPane.add(secondFloorPane);
		
		secondFloorPane.add(new UILabel(Inter.getLocText(new String[]{"Gradient", "Filed"}) + ":"));
		
		changeBox = new GradientChangeBoxPane();
		secondFloorPane.add(changeBox);
		
		secondFloorPane.add(repeatShow = new UICheckBox(Inter.getLocText(new String[]{"Repeat", "Show"})));
	}
	
	public void populate(Background background) {
		if(!(background instanceof GradientBackground)) {
			return ;
		}
		GradientBackground bg = (GradientBackground)background;
		
		gradientBox.populate(bg);
		if(bg.getDirection() == GradientBackground.LEFT2RIGHT) {
			colorDirctionBox.setSelectedItem(LEFT_2_RIGHT);
		} else {
			colorDirctionBox.setSelectedItem(TOP_2_BOTTOM);
		}
		
		changeBox.populate(bg);

		repeatShow.setSelected(bg.isCycle());
	}
	
	public void populateAlpha(int alpha) {
		alphaPane.populate(alpha);
	}
	
	public float updateAlpha() {
		return alphaPane.update();
	}
	
	public GradientBackground update() {
		GradientBackground bg = new GradientBackground();
		
		gradientBox.update(bg);
		
		if(colorDirctionBox.getSelectedItem().equals(LEFT_2_RIGHT)) {
			bg.setDirection(GradientBackground.LEFT2RIGHT);
		} else {
			bg.setDirection(GradientBackground.TOP2BOTTOM);
		}

		changeBox.update(bg);
		
		bg.setCycle(repeatShow.isSelected());
		
		return bg;
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Choose", "Gradient-Color"});
	}

}