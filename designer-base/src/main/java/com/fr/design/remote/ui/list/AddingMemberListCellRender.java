package com.fr.design.remote.ui.list;

import com.fr.base.BaseUtils;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class AddingMemberListCellRender extends JPanel implements ListCellRenderer<RemoteDesignMember> {

    private UILabel label;
    private UICheckBox check;


    public AddingMemberListCellRender() {
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        label = new UILabel();
        label.setPreferredSize(new Dimension(260, 20));
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 25));
        label.setIcon(BaseUtils.readIcon("com/fr/design/remote/images/icon_Member_normal@1x.png"));

        check = new UICheckBox();
        check.setSelected(false);
        check.setEnabled(true);

        this.add(label);
        this.add(check);
    }


    @Override
    public Component getListCellRendererComponent(JList list, RemoteDesignMember member, int index, boolean isSelected, boolean cellHasFocus) {

        if (member.equals(RemoteDesignMember.DEFAULT_MEMBER)) {
            this.setLabelText(member.getUsername());
            check.setVisible(false);
            fixLoadingDisplay();
        } else {
            this.setLabelText(member.getRealName() + "(" + member.getUsername() + ")");
            check.setVisible(true);
            check.setSelected(member.isSelected());
            recoveryCommonDisplay();
        }
        return this;
    }

    private void setLabelText(String name) {
        label.setText(name);
    }

    private void fixLoadingDisplay() {
        label.setIcon(null);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void recoveryCommonDisplay() {
        label.setIcon(BaseUtils.readIcon("com/fr/design/remote/images/icon_Member_normal@1x.png"));
        label.setHorizontalAlignment(SwingConstants.LEFT);
    }
}
