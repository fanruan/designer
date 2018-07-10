/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.fr.base.FRContext;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 * @author richer
 * @since 6.5.5 创建于2011-6-15
 */
public class LazyComboBoxTest {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new WindowsLookAndFeel());
		} catch (UnsupportedLookAndFeelException e1) {
			FRContext.getLogger().error(e1.getMessage(), e1);
		}
		JFrame f = new JFrame();
		JPanel p = (JPanel) f.getContentPane();
		p.setLayout(FRGUIPaneFactory.createBorderLayout());

		LazyComboBox lc = new LazyComboBox() {

			@Override
			public Object[] load() {
				Object[] m = new String[] { "aa", "bb", "cc" };
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					FRContext.getLogger().error(e.getMessage(), e);
				}
				return m;
			}
		};
		lc.setEditable(true);
		final UITextField jT = new UITextField();
		p.add(jT, BorderLayout.SOUTH);
		lc.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				jT.setText(e.getItem().toString());
			}
		});
		p.add(lc, BorderLayout.NORTH);
		f.setSize(300, 400);
		f.setVisible(true);
	}
}