package com.fr.design.remote.ui.list;

import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.DefaultListModel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class AddingMemberList extends MemberList {


    public AddingMemberList() {
        super();
    }

    public AddingMemberList(DefaultListModel<RemoteDesignMember> dataModel) {
        super(dataModel);
    }

    public AddingMemberList(RemoteDesignMember[] listData) {
        super(listData);

    }

    public AddingMemberList(Vector<? extends RemoteDesignMember> listData) {
        super(listData);
    }


    @Override
    protected void displaySelected() {
        RemoteDesignMember member = getSelectedValue();
        member.setSelected(!member.isSelected());
        revalidate();
        repaint();
        fireSelectedChange();
    }

    @Override
    protected boolean shouldDisplaySelected(MouseEvent e) {
        Point point = e.getPoint();
        int rX = point.x;
        int rY = point.y;
        int index = this.getSelectedIndex();
        int x = 280;
        int y = 25 * index;
        int width = 25;
        int height = 25;
        return x <= rX && rX <= x + width && y <= rY && rY <= y + height;
    }
}


