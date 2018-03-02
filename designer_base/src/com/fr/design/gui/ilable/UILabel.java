package com.fr.design.gui.ilable;

import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.stable.StringUtils;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-1-23
 * Time: 下午3:15
 */
public class UILabel extends JLabel {
	private static final int HTML_SHIFT_HEIGHT = 3;

	public UILabel(String text, Icon image, int horizontalAlignment) {
		super(text, image, horizontalAlignment);
		if (image != null && text != null) {
			setIconTextGap(4);
		}
	}

	public UILabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
	}

	public UILabel(String text) {
		super(text);
	}

	public UILabel(String text, boolean enable) {
		super(text);
		this.setEnabled(enable);
	}

	public UILabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
	}

	public UILabel(Icon image) {
		super(image);
	}

	public UILabel() {
		super();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension preferredSize = super.getPreferredSize();
		// （Windows 下）使用 html 时，文字内容会略微向下偏移，导致文字底部被截断，所以适当增加 UILabel 的高度
		if (StringUtils.isNotEmpty(getText()) && getText().startsWith("<html>")) {
			return new Dimension(preferredSize.width, preferredSize.height + HTML_SHIFT_HEIGHT);
		}
		return preferredSize;
	}


	public static void main(String[] args) {
//        UILabel label =  new UILabel("shishi",SwingConstants.LEFT);
		JFrame frame = new JFrame("Test");
		UILabel label = new UILabel("HELLO");
		frame.setSize(new Dimension(300, 400));
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setLayout(new BorderLayout());
		label.setBackground(Color.GREEN);
		panel.add(label, BorderLayout.CENTER);
		GUICoreUtils.centerWindow(frame);
		frame.setVisible(true);


	}


}