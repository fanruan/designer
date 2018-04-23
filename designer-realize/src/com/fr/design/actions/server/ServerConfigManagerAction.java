/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;

import com.fr.config.Configuration;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.webattr.EditReportServerParameterPane;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Server Config Manager
 */
public class ServerConfigManagerAction extends UpdateAction {
    public ServerConfigManagerAction() {
        this.setMenuKeySet(SERVER_CONFIG_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_web/edit.png"));
        this.setSearchText(new EditReportServerParameterPane().getAllComponents());
    }

    /**
     * 动作
     *
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        final ServerPreferenceConfig config = ServerPreferenceConfig.getInstance();
        final EditReportServerParameterPane editReportServerParameterPane = new EditReportServerParameterPane() {
            @Override
            public void complete() {
                populate(config.mirror());
            }
        };

        final BasicDialog editReportServerParameterDialog = editReportServerParameterPane.showWindow(
                DesignerContext.getDesignerFrame()
        );

        editReportServerParameterDialog.addDialogActionListener(new DialogActionAdapter() {
            @Override
            public void doOk() {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        editReportServerParameterPane.update(config);
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{ReportWebAttr.class, ServerPreferenceConfig.class};
                    }
                });

            }
        });
        editReportServerParameterDialog.setVisible(true);
    }

    @Override
    public void update() {
        this.setEnabled(true);
    }

    public static final MenuKeySet SERVER_CONFIG_MANAGER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'M';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Server-Server_Config_Manager");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}