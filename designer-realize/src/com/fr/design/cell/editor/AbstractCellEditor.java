/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.Point;

import javax.swing.event.EventListenerList;

import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.event.CellEditorEvent;
import com.fr.grid.event.CellEditorListener;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * The baics abstract editor.
 */
public abstract class AbstractCellEditor implements CellEditor {
	private EventListenerList listenerList = new EventListenerList();
	private CellEditorEvent cellEditEvent = null;
	private Point locationOnCellElement = null;
	private ElementCasePane<? extends TemplateElementCase> ePane;

	public AbstractCellEditor() {

	}

	public AbstractCellEditor(ElementCasePane<? extends TemplateElementCase> ePane) {
		this.ePane = ePane;
	}

	/**
	 * Gets the location of this component in the form of a point
	 * specifying the component's top-left corner in the CellElement's
	 * coordinate space. The default value is (0, 0).
	 * @return an instance of <code>Point</code> representing
	 * 		the top-left corner of the component's bounds in the
	 * 		coordinate space of the CellElement
	 */
	public Point getLocationOnCellElement() {
		return this.locationOnCellElement;
	}

	/**
	 * Sets the location of this component in the form of a point
	 * specifying the component's top-left corner in the CellElement's
	 * coordinate space.
	 * @param loc an instance of <code>Point</code> representing
	 * 		the top-left corner of the component's bounds in the
	 * 		coordinate space of the CellElement
	 */
	public void setLocationOnCellElement(Point loc) {
		this.locationOnCellElement = loc;
	}

	/**
	 * Tells the editor to stop editing and accept any partially edited
	 * value as the value of the editor.  The editor returns false if
	 * editing was not stopped; this is useful for editors that validate
	 * and can not accept invalid entries.
	 *
	 * @return true if editing was stopped; false otherwise
	 */
	public boolean stopCellEditing() {
		fireEditingStopped();
		return true;
	}

	/**
	 * Tells the editor to cancel editing and not accept any partially
	 * edited value.
	 */
	public void cancelCellEditing() {
		fireEditingCanceled();
	}

	/**
	 * Adds a listener to the list that's notified when the editor
	 * stops, or cancels editing.
	 *
	 * @param cellEditorListener the CellEditorListener
	 */
	public void addCellEditorListener(CellEditorListener cellEditorListener) {
		listenerList.add(CellEditorListener.class, cellEditorListener);
	}

	/**
	 * Removes a listener from the list that's notified
	 *
	 * @param cellEditorListener the CellEditorListener
	 */
	public void removeCellEditorListener(CellEditorListener cellEditorListener) {
		listenerList.remove(CellEditorListener.class, cellEditorListener);
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
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (cellEditEvent == null) {
					cellEditEvent = new CellEditorEvent(this);
				}

				((CellEditorListener) listeners[i + 1]).editingStopped(cellEditEvent);
			}
		}
		if(ePane != null) {
			ePane.fireSelectionChangeListener();
            EastRegionContainerPane.getInstance().switchTabTo(EastRegionContainerPane.KEY_CELL_ELEMENT);
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
			if (listeners[i] == CellEditorListener.class) {
				// Lazily create the event:
				if (cellEditEvent == null) {
					cellEditEvent = new CellEditorEvent(this);
				}

				((CellEditorListener) listeners[i + 1]).editingCanceled(cellEditEvent);
			}
		}
	}
}