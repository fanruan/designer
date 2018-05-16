package com.fr.design.remote.action;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.remote.ui.AuthorityManagerPane;

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

        AuthorityManagerPane managerPane = new AuthorityManagerPane();

        BasicDialog dialog = managerPane.showWindow(DesignerContext.getDesignerFrame());


        //todo read contents from database by hibernate to show

        dialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                //todo save contents into database by hibernate
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
