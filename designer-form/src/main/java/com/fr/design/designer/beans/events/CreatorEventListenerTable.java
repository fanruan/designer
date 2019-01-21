package com.fr.design.designer.beans.events;

import com.fr.design.designer.creator.XComponent;
import com.fr.general.ComparatorUtils;

import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class CreatorEventListenerTable {

    protected ArrayList<DesignerEditListener> listeners;

    public CreatorEventListenerTable() {
        listeners = new ArrayList<DesignerEditListener>();
    }

    public void addListener(DesignerEditListener listener) {
        if (listener == null) {
            return;
        }
        for (int i = 0; i < listeners.size(); i++) {
            if (ComparatorUtils.equals(listener, listeners.get(i))) {
                listeners.set(i, listener);
                return;
            }
        }
        listeners.add(listener);
    }

    private void fireCreatorModified(final DesignerEvent evt) {
        for (int i = 0; i < listeners.size(); i++) {
            final DesignerEditListener listener = listeners.get(i);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    listener.fireCreatorModified(evt);
                }
            });
        }
    }

    public void fireCreatorModified(XComponent creator, int eventID) {
        DesignerEvent evt = new DesignerEvent(eventID, creator);
        fireCreatorModified(evt);
    }

    public void fireCreatorModified(int eventID) {
        fireCreatorModified(null, eventID);
    }
}