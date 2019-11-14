package com.fr.design.actions.help.alphafine;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;

import java.awt.*;

/**
 * Created by XiaXiang on 2017/5/26.
 * 提示弹窗
 */
public class RemindDialog extends UIDialog {
    private RemindPane remindPane;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    public RemindDialog(Frame parent) {
        super(parent);
        setUndecorated(true);
        setSize(WIDTH, HEIGHT);
        initComponent();
        GUICoreUtils.centerWindow(this);
    }

    private void initComponent() {
        final AlphaFineConfigManager manager = DesignerEnvManager.getEnvManager().getAlphaFineConfigManager();
        remindPane = new RemindPane(manager, this);
        this.add(remindPane);

    }

    @Override
    public void checkValid() throws Exception {
        // Do nothing
    }

    public RemindPane getRemindPane() {
        return remindPane;
    }

    public void setRemindPane(RemindPane remindPane) {
        this.remindPane = remindPane;
    }
}
