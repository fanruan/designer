package com.fr.design.remote.ui.list;

import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.DefaultListModel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class AddedMemberList extends MemberList {

    public AddedMemberList() {
        super();
    }

    public AddedMemberList(DefaultListModel<RemoteDesignMember> dataModel) {
        super(dataModel);
    }

    public AddedMemberList(RemoteDesignMember[] listData) {
        super(listData);
    }

    public AddedMemberList(Vector<? extends RemoteDesignMember> listData) {
        super(listData);
    }


    @Override
    protected boolean shouldDisplaySelected(MouseEvent e) {
        Point point = e.getPoint();
        int rX = point.x;
        int rY = point.y;
        int index = this.getSelectedIndex();
        int x = 280;
        int y = 25 * index;
        int width = 20;
        int height = 25;
        return x <= rX && rX <= x + width && y <= rY && rY <= y + height;
    }

    @Override
    protected void displaySelected() {
        RemoteDesignMember member = getSelectedValue();
        if (member != null) {
            member.setSelected(!member.isSelected());
            ((DefaultListModel<RemoteDesignMember>) getModel()).removeElement(member);
        }
        revalidate();
        repaint();
        fireSelectedChange();
    }
}
