/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.editor.editor;

import com.fr.base.BaseUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The baics abstract CellEditor. 子类必须确定类型T
 *
 * @editor zhou
 * @since 2012-3-29下午5:08:21
 */
public abstract class Editor<T> extends JComponent {
	private static final long serialVersionUID = 1L;
	private String editorName;

	/**
	 * Return the value of CellEditor.
	 */
	public abstract T getValue();

	/**
	 * Set the value to the CellEditor.
	 */
	public abstract void setValue(T value);

	public abstract boolean accept(Object object);

	// 约定图片的文件名为this.getName(),处理起来方便些
	// b:这里国际化没有考虑
	public Icon getIcon() {
		String iconName = "com/fr/design/images/buttonicon/" + this.getIconName() + ".png";
		try {
			return BaseUtils.readIcon(iconName);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getName() {
		return this.editorName;
	}

    public String getIconName(){
        return this.editorName;
    }

	public void reset() {

	}

    public void clearData() {

    }

	@Override
	public void setName(String name) {
		this.editorName = name;
	}

	/**
	 * Calls <code>fireEditingStopped</code>.
	 */
	public void startEditing() {
		fireEditingStarted();
	}


	/**
	 * Calls <code>fireEditingStopped</code> and returns true.
	 *
	 * @return true
	 */
	public boolean stopEditing() {
		fireEditingStopped();
		return true;
	}

	/**
	 * Calls <code>fireEditingCanceled</code>.
	 */
	public void cancelEditing() {
		fireEditingCanceled();
	}

	public void selected() {

	}

	/**
	 * Adds a <code>CellEditorListener</code> to the listener list.
	 */
	public void addCellEditorListener(EditorListener cellEditorListener) {
		listenerList.add(EditorListener.class, cellEditorListener);
	}

	/**
	 * Removes a <code>CellEditorListener</code> from the listener list.
	 */
	public void removeCellEditorListener(EditorListener cellEditorListener) {
		listenerList.remove(EditorListener.class, cellEditorListener);
	}

	/**
	 * Return all of the <code>CellEditorListener</code>s added or an empty
	 * array if no listeners have been added
	 */
	public EditorListener[] getCellEditorListeners() {
		return listenerList.getListeners(EditorListener.class);
	}

	/**
	 * Fire editing started listeners.
	 */
	protected void fireEditingStarted() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == EditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}

				((EditorListener) listeners[i + 1]).editingStarted(changeEvent);
			}
		}
	}

	/**
	 * Fire editing stopped listeners.
	 */
	protected void fireEditingStopped() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == EditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}

				((EditorListener) listeners[i + 1]).editingStopped(changeEvent);
			}
		}
	}

	/**
	 * Fire editing canceled listeners.
	 */
	protected void fireEditingCanceled() {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == EditorListener.class) {
				// Lazily create the event:
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}

				((EditorListener) listeners[i + 1]).editingCanceled(changeEvent);
			}
		}
	}

	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				// Lazily create the event:
				if (changeEvent == null) {
					changeEvent = new ChangeEvent(this);
				}

				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	transient private ChangeEvent changeEvent = null;

	public void addChangeListener(ChangeListener changeListener) {
		listenerList.add(ChangeListener.class, changeListener);

	}
}