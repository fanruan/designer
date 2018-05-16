package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteMember;

import javax.swing.JList;
import javax.swing.ListModel;
import java.awt.Color;
import java.util.Vector;

public class MemberList extends JList<RemoteMember> {


    public MemberList() {
        super();
        init();
    }

    public MemberList(ListModel<RemoteMember> dataModel) {
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

    private void init() {
        setBackground(new Color(0xF5F5F7));
    }

}


