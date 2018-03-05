/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.server;


import com.fr.config.Configuration;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FunctionManagerPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.MenuKeySet;
import com.fr.file.FunctionConfig;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * FunctionManager.
 */
public class FunctionManagerAction extends UpdateAction {
    public FunctionManagerAction() {
        this.setMenuKeySet(FUNCTION_MANAGER);
        this.setName(getMenuKeySet().getMenuKeySetName()+"...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_web/function.png"));
        this.setSearchText(new FunctionManagerPane());
    }

    /**
     * 动作
     * @param evt 事件
     */
    public void actionPerformed(ActionEvent evt) {
    	final FunctionManagerPane functionManagerPane = new FunctionManagerPane();
        BasicDialog functionManagerDialog =
        	functionManagerPane.showWindow(
        			DesignerContext.getDesignerFrame(),null);
        final FunctionConfig functionManager = FunctionConfig.getInstance();
        functionManagerDialog.addDialogActionListener(new DialogActionAdapter() {
			public void doOk() {
                Configurations.update(new Worker() {
                    @Override
                    public void run() {
                        functionManagerPane.update(functionManager);
                    }

                    @Override
                    public Class<? extends Configuration>[] targets() {
                        return new Class[]{FunctionConfig.class};
                    }
                });

			}
        });
        functionManagerPane.populate(functionManager.copy());
        functionManagerDialog.setVisible(true);
    }
    
    public void update() {
		this.setEnabled(true);
	}

    public static final MenuKeySet FUNCTION_MANAGER = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'F';
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("M_Server-Function_Manager");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}