package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteMember;

import javax.swing.DefaultListModel;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class AddedMemberList extends MemberList {

    public AddedMemberList() {
        super();
    }

    public AddedMemberList(DefaultListModel<RemoteMember> dataModel) {
        super(dataModel);
    }

    public AddedMemberList(RemoteMember[] listData) {
        super(listData);
    }

    public AddedMemberList(Vector<? extends RemoteMember> listData) {
        super(listData);
    }


    @Override
    protected boolean shouldDisplaySelected(MouseEvent e) {
        return true;
    }

    @Override
    protected void displaySelected() {
        RemoteMember member = getSelectedValue();
        member.setSelected(!member.isSelected());
        ((DefaultListModel<RemoteMember>) getModel()).removeElement(member);
        revalidate();
        repaint();
        fireSelectedChange();
    }
}
