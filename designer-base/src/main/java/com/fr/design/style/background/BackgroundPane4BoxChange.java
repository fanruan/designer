package com.fr.design.style.background;

import java.awt.event.ItemListener;

import com.fr.design.dialog.BasicPane;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-3 下午03:22:48
 * 类说明: 抽象的, 背景切换的界面
 */
public abstract class BackgroundPane4BoxChange extends BasicPane {
	private static final long serialVersionUID = -4014704617562056964L;
	
	// TODO 继承UIComboBox 值不改变.
	
	private void initBox() {
//		selectBox = new UIComboBox(BackgroundUIComboBoxPane.selectTypes);
//		selectBox.setPreferredSize(new Dimension(70, 20));
	}
	
	public void addItemListener(ItemListener listener) {
//		selectBox.addItemEditListener(listener);
	}

}