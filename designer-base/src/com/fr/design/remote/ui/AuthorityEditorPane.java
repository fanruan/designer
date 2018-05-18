package com.fr.design.remote.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.remote.RemoteDesignAuthority;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;

public class AuthorityEditorPane extends BasicBeanPane<RemoteDesignAuthority> {

    private UILabel label = new UILabel();

    public AuthorityEditorPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder());
        this.add(label, BorderLayout.CENTER);
    }

    @Override
    protected String title4PopupWindow() {
        return "编辑文件权限";
    }

    @Override
    public void populateBean(RemoteDesignAuthority ob) {
        label.setText(ob.getName());
    }

    @Override
    public RemoteDesignAuthority updateBean() {
        return new RemoteDesignAuthority();
    }
}
