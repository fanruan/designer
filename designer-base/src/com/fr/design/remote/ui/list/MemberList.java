package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteMember;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public abstract class MemberList extends JList<RemoteMember> {

    public MemberList(DefaultListModel<RemoteMember> dataModel) {
        super(dataModel);
        init();
    }

    public MemberList(RemoteMember[] listData) {
        super(listData);
        init();
    }

    public MemberList(Vector<? extends RemoteMember> listData) {
        super(listData);
        init();
    }

    public MemberList() {
        super();
        init();
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

    private void init() {
        setBackground(new Color(0xF5F5F7));
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (shouldDisplaySelected(e)) {
                    displaySelected();
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    displaySelected();
                }
            }
        });
    }

    abstract protected void displaySelected();

    abstract protected boolean shouldDisplaySelected(MouseEvent e);


}
