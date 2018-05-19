package com.fr.design.remote.ui;

import com.fr.design.dialog.BasicPane;
import com.fr.design.remote.RemoteDesignAuthority;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;

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
        return Inter.getLocText("远程设计权限管理");
    }

    public void populate(RemoteDesignAuthority[] authorities) {
        list.populate(authorities);
    }

    public RemoteDesignAuthority[] update() {

        return list.update();
    }
}
