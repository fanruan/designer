package com.fr.design.actions.help.AlphaFine;

import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.UIDialog;
import com.fr.design.utils.gui.GUICoreUtils;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by XiaXiang on 2017/5/26.
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
        final AlphafineConfigManager manager = DesignerEnvManager.getEnvManager().getAlphafineConfigManager();
        remindPane = new RemindPane(manager);
        remindPane.navigateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                manager.setOperateCount(0);
            }
        });
        this.add(remindPane);

    }

    @Override
    public void checkValid() throws Exception {

    }

    public RemindPane getRemindPane() {
        return remindPane;
    }

    public void setRemindPane(RemindPane remindPane) {
        this.remindPane = remindPane;
    }
}
