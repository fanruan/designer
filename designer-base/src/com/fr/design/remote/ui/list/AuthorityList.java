package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteDesignAuthority;

import javax.swing.JList;
import javax.swing.ListModel;
import java.util.Vector;

public class AuthorityList extends JList<RemoteDesignAuthority> {


    public AuthorityList() {
        super();
    }

    public AuthorityList(ListModel<RemoteDesignAuthority> dataModel) {
        super(dataModel);
    }

    public AuthorityList(final RemoteDesignAuthority[] listData) {
        super(listData);
    }

    public AuthorityList(final Vector<? extends RemoteDesignAuthority> listData) {
        super(listData);
    }

}