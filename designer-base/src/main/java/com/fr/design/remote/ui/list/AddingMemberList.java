package com.fr.design.remote.ui.list;

import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.report.constant.RoleType;
import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.util.Vector;

public class AddingMemberList extends MemberList {

    public AddingMemberList() {
        super();
    }

    public AddingMemberList(DefaultListModel<RemoteDesignMember> dataModel) {
        super(dataModel);
    }

    public AddingMemberList(RemoteDesignMember[] listData) {
        super(listData);

    }

    public AddingMemberList(Vector<? extends RemoteDesignMember> listData) {
        super(listData);
    }


    @Override
    protected void displaySelected() {
        RemoteDesignMember member = getSelectedValue();
        if (member != null) {
            String keyTitle = member.getRoleType() == RoleType.CUSTOM ?
                    "Fine-Design_Basic_Utils_Are_You_Sure_To_Delete_The_Role_And_Its_Design_Authorities" :
                    "Fine-Design_Basic_Utils_Are_You_Sure_To_Delete_The_User_And_Its_Design_Authorities";
            if (member.isSelected() && member.hasAuthority()){
                int val = FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Toolkit.i18nText(keyTitle),
                        Toolkit.i18nText("Fine-Design_Basic_Remove"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (val == JOptionPane.OK_OPTION) {
                    member.setSelected(!member.isSelected());
                }
            }
            else {
                member.setSelected(!member.isSelected());
            }
        }
        revalidate();
        repaint();
        fireSelectedChange();
    }
}


