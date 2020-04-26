package com.fr.design.actions.server;

import com.fr.config.Configuration;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.IOUtils;

import com.fr.transaction.CallBackAdaptor;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;
import com.fr.transaction.WorkerCallBack;
import com.fr.transaction.WorkerFacade;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * StyleList Action
 */
public class StyleListAction extends UpdateAction {
    public StyleListAction() {
        this.setMenuKeySet(PREDEFINED_STYLES);
        this.setName(getMenuKeySet().getMenuKeySetName() + "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_web/style.png"));
        this.generateAndSetSearchText(StyleManagerPane.class.getName());
    }

    /**
     * 动作
     *
     * @param evt 事件
     */
	public void actionPerformed(ActionEvent evt) {
		DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
		final StyleManagerPane styleListPane = new StyleManagerPane();
		final BasicDialog styleListDialog = styleListPane.showWindow(designerFrame);
		styleListDialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
			    if (!styleListPane.isNamePermitted()) {
			        styleListDialog.setDoOKSucceed(false);
                }
				Configurations.modify(new WorkerFacade(ServerPreferenceConfig.class) {
					@Override
					public void run() {
						styleListPane.update(ServerPreferenceConfig.getInstance());
					}
				}.addCallBack(new CallBackAdaptor(){
				    @Override
                    public void afterCommit() {
                        DesignerContext.getDesignerBean("predefinedStyle").refreshBeanElement();
                    }
                }));

            }
        });
        ServerPreferenceConfig mirror = ServerPreferenceConfig.getInstance().mirror();
        styleListPane.populate(mirror);
        styleListDialog.setVisible(true);
    }

    @Override
    public void update() {
        this.setEnabled(true);
    }

    public static final MenuKeySet PREDEFINED_STYLES = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 'K';
        }

        @Override
        public String getMenuName() {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ServerM_Predefined_Styles");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}
