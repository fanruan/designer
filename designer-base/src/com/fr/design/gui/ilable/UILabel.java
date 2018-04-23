package com.fr.design.gui.ilable;

import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-1-23
 * Time: 下午3:15
 */
public class UILabel extends JLabel {

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