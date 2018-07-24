package com.fr.design.remote.ui.list;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.remote.button.IconButton;
import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class AddedMemberListCellRender extends JPanel implements ListCellRenderer<RemoteDesignMember> {


    private UILabel label;

    public AddedMemberListCellRender() {
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        label = new UILabel();
        label.setIcon(BaseUtils.readIcon("com/fr/design/remote/images/icon_Member_normal@1x.png"));
        label.setPreferredSize(new Dimension(260, 20));
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 25));

        this.add(label);
        this.add(new IconButton());
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends RemoteDesignMember> list, RemoteDesignMember member, int index, boolean isSelected, boolean cellHasFocus) {
        this.setLabelText(member.getRealName() + "(" + member.getUsername() + ")");
        return this;
    }

    private void setLabelText(String name) {
        label.setText(name);
    }

}
