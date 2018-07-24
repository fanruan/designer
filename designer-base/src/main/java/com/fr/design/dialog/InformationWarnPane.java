package com.fr.design.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.design.utils.gui.GUICoreUtils;

public class InformationWarnPane extends JPanel{
	private UITextArea moreText;
	private JPanel controlPane;
	private AlertDialog dg;
	private String title;
	private UIToggleButton arrow;
	
	private boolean isShow = false;
	public void show() {
		showWindow(SwingUtilities.getWindowAncestor(this)).setVisible(true);
	}
	
	public InformationWarnPane(String infor, String moreInfo, String title) {
		this.title = title;
		this.setLayout(null);
		this.setBounds(5,5,410,130);
		UILabel image = new UILabel(new ImageIcon(getClass().getResource("/com/fr/design/images/buttonicon/warn.png")));
		image.setBounds(10, 25, 80, 80);
		UITextArea warnLabel = new UITextArea(infor);
		warnLabel.setLineWrap(true);  
		warnLabel.setWrapStyleWord(true);  
		warnLabel.setHighlighter(null);  
		warnLabel. setEditable(false);
		warnLabel.setBackground(this.getBackground());
		warnLabel.setBounds(100, 20, 300, 80);
		arrow = new UIToggleButton(BaseUtils.readIcon("com/fr/design/images/buttonicon/more.png"));
		arrow.setRolloverIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/more3.png"));
		arrow.setBorderPainted(false);
		arrow.setExtraPainted(false);
		
		arrow.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				arrow.setSelected(arrow.isSelected());
				boolean isSelected = arrow.isSelected();
				moreText.setVisible(isSelected);
				if(isSelected) {
					arrow.setIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/more2.png"));
					arrow.setRolloverIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/more4.png"));
					InformationWarnPane.this.setBounds(5,5,410,265);
					controlPane.setBounds(175, 270, 80, 25);
					dg.setBounds(dg.getX(), dg.getY(), 430, 330);
				} else {
					arrow.setIcon(BaseUtils.readIcon("com/fr/design/images/buttonicon/more.png"));
					arrow.setRolloverIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/more3.png"));
					InformationWarnPane.this.setBounds(5,5,410,130);
					controlPane.setBounds(175, 135, 80, 25);
					dg.setBounds(dg.getX(), dg.getY(), 430, 195);
				}
			}
		});

		arrow.setBounds(90,100,30,30);
		UILabel more = new UILabel("<html><font color='blue'FACE='MicroSoft YaHei'>"+com.fr.design.i18n.Toolkit.i18nText("More-information")+"</font></html>");
		more.setBounds(125,102,55,25);
		
		moreText = new UITextArea(moreInfo);
		moreText.setForeground(Color.blue);
		moreText.setBounds(100,130,300,130);
		moreText.setLineWrap(true);  
		moreText.setWrapStyleWord(true);  
		moreText.setHighlighter(null);  
		moreText. setEditable(false);
		moreText.setBackground(this.getBackground());

		moreText.setVisible(arrow.isSelected());
		this.add(image);
		this.add(warnLabel);
		this.add(arrow);
		this.add(more);
		this.add(moreText);
	}
	
	public AlertDialog showWindow(Window window) {
		isShow = true;
		if (window instanceof Frame) {
			dg = new AlertDialog((Frame) window);
		} else {
			dg = new AlertDialog((Dialog) window);
		}
		dg.setBounds(0, 0, 430, 195);
		dg.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
		GUICoreUtils.centerWindow(dg);
		dg.setResizable(false);
		return dg;
	}
	
	public boolean isShow() {
		return isShow;
	}

	private class AlertDialog extends JDialog {
		protected UIButton okButton;

		public AlertDialog(Frame parent) {
			super(parent);
			this.setTitle(InformationWarnPane.this.title);
			this.initComponents();
		}

		public AlertDialog(Dialog parent) {
			super(parent);
			this.setTitle(InformationWarnPane.this.title);
			this.initComponents();
		}

		/**
		 * init Components
		 */
		protected void initComponents() {
			JPanel contentPane = (JPanel) this.getContentPane();
			contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			contentPane.setLayout(null);
			contentPane.add(InformationWarnPane.this);
			contentPane.add(this.createControlButtonPane());
			this.setModal(true);
			GUICoreUtils.centerWindow(this);
		}

		private JPanel createControlButtonPane() {
			controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

			okButton = new UIButton("<html><font FACE='MicroSoft YaHei'>" + com.fr.design.i18n.Toolkit.i18nText("OK") + "</font></html>") {
				@Override
				public Dimension getPreferredSize() {
					// TODO Auto-generated method stub
					return new Dimension(30, 20);
				}
			};
			controlPane.add(okButton, BorderLayout.CENTER);
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					isShow = false;
					AlertDialog.this.setVisible(false);
				}
			});
			controlPane.setBounds(175, 135, 80, 25);
			return controlPane;
		}
	}

}