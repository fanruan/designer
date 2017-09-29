/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;

import com.fr.general.Inter;

/**
 * @author richer
 * @since 6.5.5 创建于2011-6-15 延迟加载的下拉框
 */
public abstract class LazyComboBox extends UIComboBox implements PopupMenuListener {
	protected boolean loaded = false;
	private List<EventListener> ls = new ArrayList<EventListener>();
	private Object initialSelected = null;
	private static final int NUM=80;

	public static final Object PENDING = new Object() {

		@Override
		public String toString() {
			return Inter.getLocText("Loading") + "...";
		}
	};

	public LazyComboBox() {
		super();
		this.setEditor(new FilterComboBoxEditor());
		addPopupMenuListener(this);
//		updateUI();
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public abstract Object[] load();

	public void setSelectedItem(Object anObject) {
		initialSelected = anObject;
		if (loaded) {
			super.setSelectedItem(anObject);
		} else {

			setModel(new DefaultComboBoxModel(new Object[] { anObject }));
			super.setSelectedItem(anObject);
		}
	}

	/**
	 * 通过调用该方法，在点击下拉框按钮之前就加载好数据
	 */
	public void loadInstant() {
		if (loaded) {
			return;
		}
		setModel(new DefaultComboBoxModel(load()));
		loaded = true;
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if (loaded) {
            return;
        }
        DefaultComboBoxModel loadingModel = new DefaultComboBoxModel(new String[]{"", Inter.getLocText("Loading") + "..."});
        LazyComboBox.this.setModel(loadingModel);
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                final Object selectedObj = getSelectedItem();
				loadList();
                return null;
            }

            @Override
            public void done() {
                LazyComboBox.this.updateUI();
                LazyComboBox.this.fireEvent();
                LazyComboBox.this.showPopup();
            }

        }.execute();


    }

	/**
	 * 计算加载下拉列表
	 */
	public void loadList() {
		DefaultComboBoxModel model = new DefaultComboBoxModel(load());
        model.setSelectedItem(initialSelected);
		LazyComboBox.this.setModel(model);
		LazyComboBox.this.selectedItemReminder = initialSelected ;
		loaded = true;
	}

	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

	}

	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {

	}

	public void addClickListener(EventListener l) {
		if (ls == null) {
			ls = new ArrayList<LazyComboBox.EventListener>();
		}
		ls.add(l);
	}

	public void fireEvent() {
		for (int i = 0, n = ls.size(); i < n; i++) {
			ls.get(i).fireEvent();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		dim.width = NUM;
		return dim;
	}

	private static class LazyPopMenu extends BasicComboPopup {

		public LazyPopMenu(final JComboBox combo) {
			super(combo);
			LazyComboBox comboc = (LazyComboBox) combo;
			comboc.addClickListener(new EventListener() {

				@Override
				public void fireEvent() {
					LazyPopMenu.this.show();
					combo.showPopup();
				}
			});
		}
	}

	private interface EventListener {
		void fireEvent();
	}

	class FilterComboBoxEditor extends UIComboBoxEditor implements DocumentListener {
		private Object item;
		private volatile boolean filtering = false;
		private volatile boolean setting = false;

		public FilterComboBoxEditor() {
			super();
			textField.getDocument().addDocumentListener(this);
		}

		public void setItem(Object item) {
			if (filtering) {
				return;
			}
			this.item = item;

			this.setting = true;
			textField.setSetting(true);
			String newText = (item == null) ? "" : item.toString();
			textField.setText(newText);
			textField.setSetting(false);
			this.setting = false;
		}

		public Object getItem() {
			return this.item;
		}

		public void insertUpdate(DocumentEvent e) {
			handleChange();
		}

		public void removeUpdate(DocumentEvent e) {
			handleChange();
		}

		public void changedUpdate(DocumentEvent e) {
			handleChange();
		}

		protected void handleChange() {
			if (setting) {
				return;
			}
			filtering = true;
			String xx = textField.getText();
			LazyComboBox.this.setSelectedItem(xx);
			this.item = textField.getText();

			setPopupVisible(true);
			filtering = false;
		}
	}
}