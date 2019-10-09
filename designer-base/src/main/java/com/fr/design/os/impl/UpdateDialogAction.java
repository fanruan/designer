package com.fr.design.os.impl;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.os.OSBasedAction;
import com.fr.design.update.ui.dialog.UpdateMainDialog;
import com.fr.design.utils.DesignUtils;
import com.fr.stable.os.OperatingSystem;

//更新升级窗口
public class UpdateDialogAction implements OSBasedAction {
    private static String UPDATE_ROUTE = "#management/backup";
    @Override
    public void execute() {
        if(!OperatingSystem.isLinux()) {
            UpdateMainDialog dialog = new UpdateMainDialog(DesignerContext.getDesignerFrame());
            dialog.showDialog();
        }else{
            DesignUtils.visitEnvServerByParameters( UPDATE_ROUTE,null,null);
        }
    }

}
