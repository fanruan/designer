package com.fr.design.style.background.pattern;

import com.fr.general.Inter;
import com.fr.design.style.background.BackgroundDetailPane;
import com.fr.design.style.background.impl.PatternBackgroundPaneNoFore;
import com.fr.design.style.background.BackgroundSelectPane;



/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-1 下午03:12:49
 * 类说明: 图案的背景选择 弹出界面
 */
public class PatternSelectPane extends BackgroundSelectPane {
	private static final long serialVersionUID = 6504634749254957415L;
	
	public PatternSelectPane(double preWidth) {
		initBackgroundShowPane(getShowPane(preWidth));
	}

	@Override
	public BackgroundDetailPane getShowPane(double preWidth) {
		// 最少6个. 因为项目太多了. 会拉的很长
		int column = Math.max((int)preWidth / 25, 6);
		return new PatternBackgroundPaneNoFore(column); 
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Background-Pattern");
	}

}