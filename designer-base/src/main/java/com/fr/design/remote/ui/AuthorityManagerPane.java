package com.fr.design.remote.ui;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.report.DesignAuthority;

import java.awt.BorderLayout;

public class AuthorityManagerPane extends BasicPane {

    private AuthorityListUserPane userList = new AuthorityListUserPane();
    private AuthorityListCustomRolePane roleList = new AuthorityListCustomRolePane();

    public AuthorityManagerPane() {

        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        //Tabbed Pane
        UITabbedPane tabbedPane = new UITabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addTab(Toolkit.i18nText("Fine-Design_Basic_User"), userList);
        tabbedPane.addTab(Toolkit.i18nText("Fine-Design_Basic_Role"), roleList);

    }

    @Override
    protected String title4PopupWindow() {
        // 远程设计权限管理
        return Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Authority_Manager");
    }

    public void populateByUser(DesignAuthority[] authorities) {
        userList.populate(authorities);
    }

    public void populateByCustom(DesignAuthority[] authorities) {
        roleList.populate(authorities);
    }

    public DesignAuthority[] updateByUser() {
        return userList.update();
    }

    public DesignAuthority[] updateByCustom() {
        return roleList.update();
    }

}
