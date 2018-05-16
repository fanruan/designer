package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteMember;

import javax.swing.JList;
import javax.swing.ListModel;
import java.awt.Color;
import java.util.Vector;

public class AddedMemberList extends JList<RemoteMember> {

    public AddedMemberList() {
        super();
        init(this.getModel().getSize());
    }

    public AddedMemberList(ListModel<RemoteMember> dataModel) {
        super(dataModel);
        init(dataModel.getSize());
    }

    public AddedMemberList(RemoteMember[] listData) {
        super(listData);
        init(listData.length);
    }

    public AddedMemberList(Vector<? extends RemoteMember> listData) {
        super(listData);
        init(listData.size());
    }

    private void init(int size) {
        setBackground(new Color(0xF5F5F7));
    }
}
