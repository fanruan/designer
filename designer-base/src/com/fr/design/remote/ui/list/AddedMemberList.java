package com.fr.design.remote.ui.list;

import com.fr.env.RemoteDesignMember;

import javax.swing.DefaultListModel;
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
        return true;
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
