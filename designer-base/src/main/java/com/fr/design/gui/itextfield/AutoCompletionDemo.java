package com.fr.design.gui.itextfield;

import java.io.File;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.UIManager;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.stable.ArrayUtils;

public class AutoCompletionDemo extends javax.swing.JFrame {
	private UIAutoCompletionField autoCompletionField1;
	private UIButton jButton1;
	private UILabel jLabel1;

	public AutoCompletionDemo() {
		initComponents();
		Object[] v = new String[0];
		File file = new File(System.getProperty("user.home"));
		String[] files = file.list();
		for (String filename : files)
			v= ArrayUtils.add(v, filename);
		autoCompletionField1.setFilter(new DefaultCompletionFilter(v));
	}

	private void initComponents() {
		autoCompletionField1 = new UIAutoCompletionField();
		autoCompletionField1.setText(".F");
		autoCompletionField1.addDocumentListener();
		jButton1 = new UIButton();
		jLabel1 = new UILabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jButton1.setText("Exit");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});

		jLabel1.setText("<html> <body> <h3>fuck</h3>   </body> </html>  ");
		jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
										.addComponent(autoCompletionField1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
										.addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(autoCompletionField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1).addContainerGap()));
		pack();
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new AutoCompletionDemo().setVisible(true);
			}
		});
	}

}