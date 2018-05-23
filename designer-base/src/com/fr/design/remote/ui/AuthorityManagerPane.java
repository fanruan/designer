package com.fr.design.remote.ui;

import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.report.DesignAuthority;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.util.Arrays;

public class AuthorityManagerPane extends BasicPane {

    private AuthorityListControlPane list;


    public AuthorityManagerPane() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder());

        list = new AuthorityListControlPane();
        this.add(list, BorderLayout.CENTER);
    }


    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Remote_Design_Authority_Manager");
    }

    public void populate(DesignAuthority[] authorities) {
        list.populate(authorities);
    }

    public DesignAuthority[] update() {
        return list.update();
    }
}
