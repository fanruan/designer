package com.fr.design.style.color;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.fr.base.background.ColorBackground;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.AlphaPane;
import com.fr.design.style.background.BackgroundPane4BoxChange;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-3 上午10:32:15
 * 类说明: 颜色选择的UIComboBox切换分支pane 原型图bug@5471
 */
public class ColorUIComboBoxPane extends BackgroundPane4BoxChange {
	private static final long serialVersionUID = -4613725388689229514L;
	
	private ColorSelectBox colorBox;
	private AlphaPane alphaPane;
	
	public ColorUIComboBoxPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel pane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		this.add(pane, BorderLayout.CENTER);
		
		pane.add(colorBox = new ColorSelectBox(80));
		pane.add(alphaPane = new AlphaPane());
	}
	
	public void populate(Background background) {
		if(background instanceof ColorBackground) {
			ColorBackground colorBackground = (ColorBackground)background;
			colorBox.setSelectObject(colorBackground.getColor());
		}
	}
	
	public void populateAlpha(int alpha) {
		alphaPane.populate(alpha);
	}
	
	public float updateAlpha() {
		return alphaPane.update();
	}
	
	public Background update() {
		return ColorBackground.getInstance(colorBox.getSelectObject());
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Color");
	}

}