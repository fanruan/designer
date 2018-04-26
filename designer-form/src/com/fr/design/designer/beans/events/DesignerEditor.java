package com.fr.design.designer.beans.events;

import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.stable.core.PropertyChangeListener;
import com.fr.design.utils.gui.LayoutUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DesignerEditor<T extends JComponent> implements PropertyChangeListener<T> {

	private ArrayList<PropertyChangeAdapter> propertyChangeListenerList = new ArrayList<PropertyChangeAdapter>();
	private ArrayList<PropertyChangeAdapter> stopEditListenerList = new ArrayList<PropertyChangeAdapter>();
	private T comp;
	private boolean changed;

	public DesignerEditor(T comp) {
		this.comp = comp;
	}

	public void addStopEditingListener(PropertyChangeAdapter l) {
		int index = stopEditListenerList.indexOf(l);
		if (index == -1) {
			stopEditListenerList.add(l);
		} else {
			stopEditListenerList.set(index, l);
		}
	}

	public void fireEditStoped() {
		if (changed) {
			for (PropertyChangeAdapter l : stopEditListenerList) {
				l.propertyChange();
			}
			changed = false;
		}
	}
	
	public void addPropertyChangeListener(PropertyChangeAdapter l) {
		int index = propertyChangeListenerList.indexOf(l);
		if (index == -1) {
			propertyChangeListenerList.add(l);
		} else {
			propertyChangeListenerList.set(index, l);
		}
	}

	public void propertyChange() {
		for (PropertyChangeAdapter l : propertyChangeListenerList) {
			l.propertyChange();
		}
		changed = true;
	}

    @Override
    public void propertyChange(T mark) {

    }

	@Override
	public void propertyChange(T... marks) {

	}

	public void reset() {
		changed = false;
	}

	public void paintEditor(Graphics g, Dimension size) {
		if (this.comp != null) {
			comp.setSize(new Dimension(size.width - 2, size.height - 2));
			LayoutUtils.layoutContainer(comp);
			Graphics clipg = g.create(1, 1, size.width, size.height);
			this.comp.paint(clipg);
		}
	}

	public T getEditorTarget() {
		return comp;
	}
}