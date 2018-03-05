package com.fr.design.actions.server;

import com.fr.config.Configuration;
import com.fr.config.ServerConfig;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * StyleList Action
 */
public class StyleListAction extends UpdateAction {
	public StyleListAction() {
        this.setMenuKeySet(PREDEFINED_STYLES);
        this.setName(getMenuKeySet().getMenuKeySetName()+ "...");
        this.setMnemonic(getMenuKeySet().getMnemonic());
		this.setSmallIcon(IOUtils.readIcon("/com/fr/design/images/m_web/style.png"));
		this.setSearchText(new StyleManagerPane());
	}

    /**
     * 动作
     * @param evt 事件
     */
	public void actionPerformed(ActionEvent evt) {
		DesignerFrame designerFrame = DesignerContext.getDesignerFrame();
		final StyleManagerPane styleListPane = new StyleManagerPane();
		BasicDialog styleListDialog = styleListPane.showWindow(designerFrame);
		styleListDialog.addDialogActionListener(new DialogActionAdapter() {
			@Override
			public void doOk() {
				Configurations.update(new Worker() {
					@Override
					public void run() {
						styleListPane.update(ServerConfig.getInstance());
					}

					@Override
					public Class<? extends Configuration>[] targets() {
						return new Class[]{ServerConfig.class};
					}
				});

			}                
        });

        styleListPane.populate(ServerConfig.getInstance().copy());
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
			return Inter.getLocText("ServerM-Predefined_Styles");
		}

		@Override
		public KeyStroke getKeyStroke() {
			return null;
		}
	};
}