package com.fr.design.remote.ui.list;

import com.fr.base.BaseUtils;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.remote.RemoteMember;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.FlowLayout;

public class MemberListCellRender extends JPanel implements ListCellRenderer<RemoteMember> {

    private UILabel label;
    private UICheckBox check;


    public MemberListCellRender() {
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        label = new UILabel();
        label.setIcon(BaseUtils.readIcon("com/fr/design/remote/images/icon_Member_normal@1x.png"));
        check = new UICheckBox();
        check.setSelected(false);
        this.add(label);
        this.add(check);
    }


    @Override
    public Component getListCellRendererComponent(JList list, RemoteMember member, int index, boolean isSelected, boolean cellHasFocus) {
        this.setLabelName(member.getName());
        check.setSelected(false);
        return this;
    }

    private void setLabelName(String name) {
        label.setName(name);
    }


}
