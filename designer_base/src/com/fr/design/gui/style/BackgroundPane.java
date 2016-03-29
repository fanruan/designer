package com.fr.design.gui.style;

/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.Style;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.backgroundpane.BackgroundSettingPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;
import com.fr.design.mainframe.backgroundpane.PatternBackgroundPane;
import com.fr.design.mainframe.backgroundpane.TextureBackgroundPane;
import com.fr.general.Background;
import com.fr.general.Inter;

/**
 * 
 * @author zhou
 * @since 2012-5-28下午6:22:09
 */
public class BackgroundPane extends AbstractBasicStylePane {

	private UIComboBox typeComboBox;

	protected List<BackgroundSettingPane> paneList;

	public BackgroundPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(new BorderLayout(0, 6));
		typeComboBox = new UIComboBox();
		final CardLayout cardlayout = new CardLayout();
		this.add(typeComboBox, BorderLayout.NORTH);

        initPaneList();
		final JPanel centerPane = new JPanel(cardlayout) {
			@Override
			public Dimension getPreferredSize() {// AUGUST:使用当前面板的的高度
				int index = typeComboBox.getSelectedIndex();
				return new Dimension(super.getPreferredSize().width, paneList.get(index).getPreferredSize().height);
			}
		};
		for (int i = 0; i < paneList.size(); i++) {
			BackgroundSettingPane pane = paneList.get(i);
			typeComboBox.addItem(pane.title4PopupWindow());
			centerPane.add(pane, pane.title4PopupWindow());
		}
		this.add(centerPane, BorderLayout.CENTER);
		typeComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				cardlayout.show(centerPane, (String)typeComboBox.getSelectedItem());
				fireStateChanged();
			}
		});
	}

    protected void initPaneList(){
        paneList = new ArrayList<BackgroundSettingPane>();
        paneList.add(new NullBackgroundPane());
        paneList.add(new ColorBackgroundPane());
        paneList.add(new TextureBackgroundPane());
        paneList.add(new PatternBackgroundPane());
        paneList.add(new ImageBackgroundPane());
        paneList.add(new GradientPane());
    }

    /**
     * 事件监听
     * @param changeListener     事件
     */
	public void addChangeListener(ChangeListener changeListener) {
		listenerList.add(ChangeListener.class, changeListener);
	}

	/**
     */
	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		ChangeEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i + 1]).stateChanged(e);
			}
		}
	}

    /**
     * 名称
     * @return    名称
     */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Utils_Background");
	}

	/**
	 * Populate background.
	 */
	public void populateBean(Background background) {
		for (int i = 0; i < paneList.size(); i++) {
			BackgroundSettingPane pane = paneList.get(i);
			if (pane.accept(background)) {
				pane.populateBean(background);
				typeComboBox.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Update background.
	 */
	public Background update() {
		return paneList.get(typeComboBox.getSelectedIndex()).updateBean();
	}

	@Override
	public void populateBean(Style style) {
		this.populateBean(style.getBackground());
	}

	@Override
	public Style update(Style style) {
		return style.deriveBackground(this.update());
	}

}