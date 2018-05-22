package com.fr.design.remote.action;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.remote.ui.AuthorityManagerPane;
import com.fr.env.RemoteEnv;
import com.fr.report.DesignAuthority;

import java.awt.event.ActionEvent;

/**
 * @author yaohwu
 */
public class RemoteDesignAuthorityManagerAction extends UpdateAction {


    public RemoteDesignAuthorityManagerAction() {
        this.setName("远程设计权限管理");
        this.setSmallIcon(BaseUtils.readIcon("com/fr/design/remote/images/icon_Remote_Design_Permission_Manager_normal@1x.png"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        final AuthorityManagerPane managerPane = new AuthorityManagerPane();

        BasicDialog dialog = managerPane.showWindow(DesignerContext.getDesignerFrame());


        //todo read contents from database by hibernate to show

        dialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                DesignAuthority[] authorities = managerPane.update();
                if (!FRContext.getCurrentEnv().isLocalEnv()) {
                    try {
                        ((RemoteEnv) FRContext.getCurrentEnv()).updateAuthorities(authorities);
                    } catch (Exception exception) {
                        FRContext.getLogger().error(exception.getMessage());
                    }
                }
            }

            @Override
            public void doCancel() {
                super.doCancel();
            }
        });
        dialog.setModal(true);
        dialog.setVisible(true);
    }
}
