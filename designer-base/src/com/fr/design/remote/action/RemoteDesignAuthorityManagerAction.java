package com.fr.design.remote.action;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.remote.ui.AuthorityManagerPane;
import com.fr.env.RemoteEnv;
import com.fr.general.Inter;
import com.fr.report.DesignAuthority;

import java.awt.event.ActionEvent;

/**
 * @author yaohwu
 */
public class RemoteDesignAuthorityManagerAction extends UpdateAction {


    public RemoteDesignAuthorityManagerAction() {
        this.setName(Inter.getLocText("FR-Designer_Remote_Design_Authority_Manager"));
        this.setSmallIcon(BaseUtils.readIcon("com/fr/design/remote/images/icon_Remote_Design_Permission_Manager_normal@1x.png"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        final AuthorityManagerPane managerPane = new AuthorityManagerPane();

        BasicDialog dialog = managerPane.showWindow(DesignerContext.getDesignerFrame());

        if (!FRContext.getCurrentEnv().isLocalEnv()) {
            try {
                DesignAuthority[] authorities = ((RemoteEnv) FRContext.getCurrentEnv()).getAuthorities();
                if (authorities != null && authorities.length != 0) {
                    managerPane.populate(authorities);
                }
            } catch (Exception exception) {
                FRContext.getLogger().error(exception.getMessage());
            }
        }

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
