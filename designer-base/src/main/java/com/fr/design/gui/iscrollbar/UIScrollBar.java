package com.fr.design.gui.iscrollbar;

import javax.swing.*;
import java.awt.*;

/**
 * UIScrollBar是没有下上的按钮的,宽为8像素
 *
 * @author zhou
 * @since 2012-5-9下午4:32:59
 */
public class UIScrollBar extends JScrollBar {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
    private int temp = 10;

    public UIScrollBar(){
    }

	public UIScrollBar(int orientation) {
		super(orientation);
		setUI(new UIScrollBarUI());
	}

	@Override
	public Dimension getPreferredSize() {
		return getOrientation() == UIScrollBar.VERTICAL ?
				new Dimension(10, super.getPreferredSize().height)
				: new Dimension(super.getPreferredSize().width, 10);
	}

	@Override
	/**
	 * 取得宽度
	 */
	public int getWidth() {
		return getOrientation() == UIScrollBar.VERTICAL ? temp : super.getWidth();
	}

	@Override
	/**
	 * 取得高度
	 */
	public int getHeight() {
		return getOrientation() == UIScrollBar.HORIZONTAL ? temp : super.getHeight();
	}
}