/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;

import com.fr.base.BaseUtils;
import com.fr.config.Configuration;
import com.fr.config.ServerConfig;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.parameter.ParameterManagerPane;
import com.fr.general.Inter;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Parameter dialog
 */
public class GlobalParameterAction extends UpdateAction {
    public GlobalParameterAction() {
        this.setMenuKeySet(GLOBAL_PARAMETER);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_report/p.png"));
    }

    /**
     * 动作
     *
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        final DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
        final ParameterManagerPane parameterManagerPane = new ParameterManagerPane();

        final BasicDialog parameterManagerDialog = parameterManagerPane.showWindow(designerFrame);

        //marks:读取服务器配置信息
        final ServerConfig configManager = ServerConfig.getInstance();

        parameterManagerPane.populate(configManager.mirror());
        parameterManagerDialog.addDialogActionListener(new DialogActionAdapter() {
            public void doOk() {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        //apply new parameter list.
                        parameterManagerPane.update(configManager);
                        DesignModelAdapter<?, ?> model = DesignModelAdapter.getCurrentModelAdapter();
                        if (model != null) {
                            model.parameterChanged();
                        }
                        parameterManagerDialog.setDoOKSucceed(!parameterManagerPane.isContainsRename());
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{ServerConfig.class};
                    }
                });

            }
        });
        parameterManagerDialog.setModal(true);
        parameterManagerDialog.setVisible(true);
    }

    public void update() {
        this.setEnabled(true);
    }

    public static final MenuKeySet GLOBAL_PARAMETER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'G';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Server-Global_Parameters");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}