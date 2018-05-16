package com.fr.design.remote.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.remote.RemoteDesignAuthority;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Label;

public class AuthorityEditorPane extends BasicBeanPane<RemoteDesignAuthority> {

    public AuthorityEditorPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder());
        this.add(new Label("editor"), BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return "编辑文件权限";
    }

    @Override
    public void populateBean(RemoteDesignAuthority ob) {

    }

    @Override
    public RemoteDesignAuthority updateBean() {
        return new RemoteDesignAuthority();
    }
}
