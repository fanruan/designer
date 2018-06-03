package com.fr.design.remote.ui.list;

import com.fr.env.RemoteDesignMember;

import javax.swing.DefaultListModel;
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
        return true;
    }
}


