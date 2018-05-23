package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteMember;

import javax.swing.DefaultListModel;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class AddingMemberList extends MemberList {


    public AddingMemberList() {
        super();
    }

    public AddingMemberList(DefaultListModel<RemoteMember> dataModel) {
        super(dataModel);
    }

    public AddingMemberList(RemoteMember[] listData) {
        super(listData);

    }

    public AddingMemberList(Vector<? extends RemoteMember> listData) {
        super(listData);
    }


    @Override
    protected void displaySelected() {
        RemoteMember member = getSelectedValue();
        member.setSelected(!member.isSelected());
        revalidate();
        repaint();
        fireSelectedChange();
    }

    @Override
    protected boolean shouldDisplaySelected(MouseEvent e) {
        return true;
    }
}


