package com.fr.design.remote.ui.list;


import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public abstract class MemberList extends JList<RemoteDesignMember> {
    static final int TRIGGER_AREA_X = 270;
    static final int TRIGGER_AREA_Y_EACH = 25;

    static final int TRIGGER_AREA_WIDTH = 25;
    static final int TRIGGER_AREA_HEIGHT = 25;


    public MemberList(DefaultListModel<RemoteDesignMember> dataModel) {
        super(dataModel);
        init();
    }

    public MemberList(RemoteDesignMember[] listData) {
        super(listData);
        init();
    }

    public MemberList(Vector<? extends RemoteDesignMember> listData) {
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

    protected boolean shouldDisplaySelected(MouseEvent e) {
        Point point = e.getPoint();
        int rX = point.x;
        int rY = point.y;
        int index = this.getSelectedIndex();
        int x = TRIGGER_AREA_X;
        int y = TRIGGER_AREA_Y_EACH * index;
        return x <= rX && rX <= x + TRIGGER_AREA_WIDTH && y <= rY && rY <= y + TRIGGER_AREA_HEIGHT;
    }
}
