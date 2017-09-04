package com.fr.design.mainframe.loghandler;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LogHandlerBar extends JPanel implements ItemSelectable {

	private static final long serialVersionUID = 1L;
	private ItemListener listeners;
	private UIButton clear;
	private UIButton selectedall;
	private UIButton set;

	private String text;
	private int INFONUM = 0;
	private int ERRORNUM = 0;
	private int SERVERNUM = 0;

	private boolean isWithSerious;

	public LogHandlerBar() {
		this(null);
	}

	public LogHandlerBar(String text) {
		this.setLayout(new CaptionLayout());
		this.setUI(new LogHandlerBarUI());
		this.text = text;
		clear = new UIButton(BaseUtils.readIcon("com/fr/design/images/log/clear.png"));
		clear.setMargin(null);
		clear.setOpaque(false);
		clear.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		clear.setToolTipText(Inter.getLocText("Clear_All"));
		selectedall = new UIButton(BaseUtils.readIcon("com/fr/design/images/log/selectedall.png"));
		selectedall.setMargin(null);
		selectedall.setOpaque(false);
		selectedall.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		selectedall.setToolTipText(Inter.getLocText("Select_All"));
		set = new UIButton(BaseUtils.readIcon("com/fr/design/images/log/setting.png"));
		set.setMargin(null);
		set.setOpaque(false);
		set.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		set.setToolTipText(Inter.getLocText("Set"));

		this.add(clear);
		this.add(selectedall);
		this.add(set);
	}

	public void clearMessage() {
		INFONUM = ERRORNUM = SERVERNUM = 0;
		repaint();
	}

	public boolean IsWithSerious() {
		return isWithSerious;
	}

	public void setWithSerious(boolean b) {
		this.isWithSerious = b;
	}

	public void infoAdd() {
		INFONUM++;
		repaint();
	}

	public void errorAdd() {
		ERRORNUM++;
		timerPaint();

	}

	public void serverAdd() {
		SERVERNUM++;
		timerPaint();
	}

	public synchronized void timerPaint() {
		repaint();
	}

	public int getInfo() {
		return INFONUM;
	}

	public int getError() {
		return ERRORNUM;
	}

	public int getServer() {
		return SERVERNUM;
	}

	public void addItemListener(ItemListener l) {
		listeners = l;
	}

	public void removeItemListener(ItemListener l) {
		listeners = null;
	}

	protected void fireItemStateChanged(ItemEvent e) {
		listeners.itemStateChanged(e);
	}

	public Object[] getSelectedObjects() {
		return new Object[] { text };
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		repaint();
	}

	public void addClearListener(ActionListener l) {
		clear.addActionListener(l);
	}

	public void addSelectedListener(ActionListener l) {
		selectedall.addActionListener(l);
	}

	public void addSetListener(ActionListener l) {
		set.addActionListener(l);
	}

	private class CaptionLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {

		}

		@Override
		public void removeLayoutComponent(Component comp) {

		}

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(120, 24);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(0, 0);
		}

		@Override
		public void layoutContainer(Container target) {
			Insets insets = target.getInsets();
			int top = insets.top;
			int right = target.getWidth() - insets.right;
			clear.setBounds(right - 130, top + 4, clear.getPreferredSize().width, clear.getPreferredSize().height);
			selectedall.setBounds(right - 100, top + 4, selectedall.getPreferredSize().width, selectedall.getPreferredSize().height);
			set.setBounds(right - 70, top + 4, set.getPreferredSize().width, set.getPreferredSize().height);

		}
	}

}