package com.fr.design.remote.action;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.remote.ui.AuthorityManagerPane;
import com.fr.log.FineLoggerFactory;
import com.fr.report.DesignAuthority;
import com.fr.stable.ArrayUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.authority.AuthorityOperator;

import java.awt.event.ActionEvent;

/**
 * @author yaohwu
 */
public class RemoteDesignAuthManagerAction extends UpdateAction {


    public RemoteDesignAuthManagerAction() {
        this.setName(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Authority_Manager"));
        // 远程设计权限管理
        this.setSmallIcon(BaseUtils.readIcon("com/fr/design/remote/images/icon_Remote_Design_Auth_Manager_normal@1x.png"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        final AuthorityManagerPane managerPane = new AuthorityManagerPane();

        BasicDialog dialog = managerPane.showWindow(DesignerContext.getDesignerFrame());

        if (!WorkContext.getCurrent().isLocal()) {
            try {
                // 远程设计获取全部设计成员的权限列表
                DesignAuthority[] userAuthorities = WorkContext.getCurrent().get(AuthorityOperator.class).getUserAuthorities();
                DesignAuthority[] customAuthorities = WorkContext.getCurrent().get(AuthorityOperator.class).getCustomRoleAuthorities();
                if (userAuthorities != null && userAuthorities.length != 0) {
                    managerPane.populateByUser(userAuthorities);
                }
                if (customAuthorities != null && customAuthorities.length != 0) {
                    managerPane.populateByCustom(customAuthorities);
                }
            } catch (Exception exception) {
                FineLoggerFactory.getLogger().error(exception.getMessage(), exception);
            }
        }

        dialog.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                DesignAuthority[] userAuthorities = managerPane.updateByUser();
                DesignAuthority[] customRoleAuthorities = managerPane.updateByCustom();
                DesignAuthority[] authorities = ArrayUtils.addAll(userAuthorities, customRoleAuthorities);
                if (!WorkContext.getCurrent().isLocal()) {
                    boolean success = false;
                    try {
                        success = WorkContext.getCurrent().get(AuthorityOperator.class).updateAuthorities(authorities);
                    } catch (Exception e) {
                        FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    }
                    FineLoggerFactory.getLogger().debug("update remote design authority: " + success);
                }
            }
        });
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}
