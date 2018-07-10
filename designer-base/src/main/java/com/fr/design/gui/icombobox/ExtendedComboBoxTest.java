package com.fr.design.gui.icombobox;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.fr.base.FRContext;

public class ExtendedComboBoxTest {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage(), e);
		}

		final ExtendedComboBox cb = new ExtendedComboBox(
				new String[] {
						"Hello   world,   alksdfjlaskdjflaskjdflaksdf",
						"Hello   world,   alksdfjlaskdjflaskjdflaksdfasdfklajsdflkasjdflkasdf",
						"Hello   world,   alksdfjlaskdjflaskjdflaksdfasdfklajsdflkasjdflkasdfaaaaaaaaaaaaaaaaaadfsdf",
						"Hello   world,   alksdfjlaskdjflaskjdflaksdfasdfklajsdflkasjdflkasdfsdfgklsdjfgklsdfjgklsjdfgkljsdflkgjsdlfk" });


		JFrame f = new JFrame();
		f.getContentPane().add(cb, BorderLayout.CENTER);
		f.pack();
		f.setSize(300, f.getHeight());
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}