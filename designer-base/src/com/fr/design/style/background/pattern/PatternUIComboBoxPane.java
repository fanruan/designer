package com.fr.design.style.background.pattern;

import java.awt.BorderLayout;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.base.background.PatternBackground;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.background.BackgroundPane4BoxChange;
import com.fr.design.style.color.ColorSelectBox;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-3 上午10:42:05
 * 类说明: 图案的UIComboBox切换分支pane, 原型图@bug 5471
 */
public class PatternUIComboBoxPane extends BackgroundPane4BoxChange {
	private static final long serialVersionUID = -6341773082454804759L;
	
	private PatternSelectBox patternBox;
	private ColorSelectBox foreColor;
	private ColorSelectBox backColor;
	
	public PatternUIComboBoxPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel pane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		this.add(pane, BorderLayout.NORTH);
		
		pane.add(patternBox = new PatternSelectBox(80));
		pane.add(new UILabel(Inter.getLocText("Foreground") + ":"));
		pane.add(foreColor = new ColorSelectBox(80));
		pane.add(new UILabel(Inter.getLocText("Background") + ":"));
		pane.add(backColor = new ColorSelectBox(80));
	}
	
	public void populate(Background background) {
		if(background instanceof PatternBackground) {
			PatternBackground pb = (PatternBackground)background;
			
			foreColor.setSelectObject(pb.getForeground());
			backColor.setSelectObject(pb.getBackground());
		}
	}
	
	public Background update() {
		
		PatternBackground background = new PatternBackground();
		
		background.setForeground(foreColor.getSelectObject());
		background.setBackground(backColor.getSelectObject());
		
		return null;
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}
}