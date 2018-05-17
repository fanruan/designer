package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteMember;

import javax.swing.JList;
import javax.swing.ListModel;
import java.util.Vector;

public class MemberList extends JList<RemoteMember> {

    public MemberList(ListModel<RemoteMember> dataModel) {
        super(dataModel);
    }

    public MemberList(RemoteMember[] listData) {
        super(listData);
    }

    public MemberList(Vector<? extends RemoteMember> listData) {
        super(listData);
    }

    public MemberList() {
    }


    public void addSelectedChangeListener(MemberListSelectedChangeListener l) {
        this.listenerList.add(MemberListSelectedChangeListener.class, l);
    }

    public void removeSelectedChangeListener(MemberListSelectedChangeListener l) {
        this.listenerList.remove(MemberListSelectedChangeListener.class, l);
    }

    public void fireSelectedChange() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MemberListSelectedChangeListener.class) {
                ((MemberListSelectedChangeListener) listeners[i + 1]).selectedChange();
            }
        }
    }

    protected void displaySelected() {
        RemoteMember member = getSelectedValue();
        member.setSelected(!member.isSelected());
        revalidate();
        repaint();
        fireSelectedChange();
    }
}
