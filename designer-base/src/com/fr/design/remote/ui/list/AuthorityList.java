package com.fr.design.remote.ui.list;

import com.fr.report.DesignAuthority;

import javax.swing.JList;
import javax.swing.ListModel;
import java.util.Vector;

public class AuthorityList extends JList<DesignAuthority> {


    public AuthorityList() {
        super();
    }

    public AuthorityList(ListModel<DesignAuthority> dataModel) {
        super(dataModel);
    }

    public AuthorityList(final DesignAuthority[] listData) {
        super(listData);
    }

    public AuthorityList(final Vector<? extends DesignAuthority> listData) {
        super(listData);
    }

}