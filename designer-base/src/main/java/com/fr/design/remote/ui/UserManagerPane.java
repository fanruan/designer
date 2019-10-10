package com.fr.design.remote.ui;

import com.fr.design.border.UITitledBorder;
import com.fr.design.i18n.Toolkit;
import com.fr.design.remote.ui.list.AddedMemberListCellRender;
import com.fr.design.remote.ui.list.AddingMemberListCellRender;
import com.fr.design.remote.ui.list.cell.AddedUserListCellRender;
import com.fr.design.remote.ui.list.cell.AddingUserListCellRender;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.authority.RemoteDesignMember;
import com.fr.workspace.server.authority.decision.DecisionOperator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.util.Collection;


/**
 * 选择设计用户
 */
public class UserManagerPane extends AbstractManagerPane {

    @Override
    protected String title4PopupWindow() {
        // 选择设计用户
        return Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Choose_User");
    }

    @Override
    protected JPanel rightPanel(){
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 0, 0),
                        UITitledBorder.createBorderWithTitle(
                                // 已选择的设计用户
                                Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Selected_User"),
                                4
                        )
                )
        );
        return content;
    }

    @Override
    protected JPanel leftPanel(){
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 0, 0),
                        UITitledBorder.createBorderWithTitle(
                                // 决策系统用户
                                Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Decision_User"),
                                4)
                )
        );
        return content;
    }

    @Override
    protected AddingMemberListCellRender getAddingMemberListCellRender() {
        return new AddingUserListCellRender();
    }

    @Override
    protected AddedMemberListCellRender getAddedMemberListCellRender() {
        return new AddedUserListCellRender();
    }

    @Override
    protected Collection<RemoteDesignMember> getMembers(String userName, String keyWord){
        return WorkContext.getCurrent().get(DecisionOperator.class).getMembers(userName, keyWord);
    }

    @Override
    protected Collection<RemoteDesignMember> getMembers(String userName, String keyWord, int pageNum, int count){
        return WorkContext.getCurrent().get(DecisionOperator.class).getMembers(userName, keyWord, pageNum, count);
    }
}