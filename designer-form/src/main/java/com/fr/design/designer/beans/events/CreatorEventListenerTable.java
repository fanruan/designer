package com.fr.design.designer.beans.events;

import java.util.ArrayList;

import com.fr.design.designer.creator.XComponent;
import com.fr.general.ComparatorUtils;

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
            if (ComparatorUtils.equals(listener,listeners.get(i))) {
                listeners.set(i, listener);
                return;
            }
        }
        listeners.add(listener);
    }

    private void fireCreatorModified(DesignerEvent evt) {
        for (int i = 0; i < listeners.size(); i++) {
            DesignerEditListener listener = listeners.get(i);
            listener.fireCreatorModified(evt);
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