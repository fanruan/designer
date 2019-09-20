package com.fr.start.server;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.log.FineLoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 内置Tomcat服务器管理界面 
 */
public class ServerManageFrame extends JFrame {
	// 由于实际情况，只需要一个当前对象的Instance.
	private static ServerManageFrame serverManageFrame = null;
	
	static ServerManageFrame getServerManageFrame() {
		
		if (serverManageFrame == null) {
			serverManageFrame = new ServerManageFrame();
		}

		//p:每次启动之前都需要检查按钮的Enabled属性.
		try {
			serverManageFrame.checkButtonEnabled();
		} catch(Exception exp) {
            FineLoggerFactory.getLogger().error(exp.getMessage());
		}
		
		return serverManageFrame;
	}
	
	private JPanel startPane;
	private JPanel stopPane;
	
	private ServerManageFrame() {
		
		this.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/trayStarted.png"));

		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());
		contentPane.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));

		//p:control JPanel
		JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		contentPane.add(controlPane, BorderLayout.NORTH);
//		controlPane.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		UILabel iconLabel = new UILabel();
		controlPane.add(iconLabel, BorderLayout.WEST);
		iconLabel.setPreferredSize(new Dimension(100, 80));
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setIcon(BaseUtils.readIcon("/com/fr/design/images/server/manage.png"));		

		JPanel controlPane2 = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
		controlPane.add(controlPane2, BorderLayout.CENTER);
		controlPane2.setBorder(BorderFactory.createEmptyBorder((80 - 26) / 2, 0, 0, 0));
		
		startPane =FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		controlPane2.add(startPane);		
		UIButton startButton = new UIButton();
		startPane.add(startButton);
		startButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/server/start.png"));
		startPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Start")));
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FineEmbedServer.start();
					checkButtonEnabled();
				} catch(Exception exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage());
				}
			}
		});
		
		stopPane =FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		controlPane2.add(stopPane);
		UIButton stopButton = new UIButton();
		stopPane.add(stopButton);
		stopButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/server/stop.png"));
		stopPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Stop")));
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FineEmbedServer.stop();
					checkButtonEnabled();
				} catch(Exception exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage());
				}
			}
		});

		//p:info pane
		JPanel infoPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		contentPane.add(infoPane, BorderLayout.SOUTH);
		infoPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

		infoPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Log") + ":"), BorderLayout.WEST);
		UITextField logPathTextField = new UITextField();
		infoPane.add(logPathTextField, BorderLayout.CENTER);
		logPathTextField.setEditable(false);
		
		UIButton openButton = new UIButton();
		infoPane.add(openButton, BorderLayout.EAST);
		openButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/server/view.png"));
		openButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Open"));
//		openButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent evt) {
//
//				Desktop desktop = Desktop.getDesktop();
//				try {
//					desktop.open();
//				} catch(Exception exp) {
//                    FineLoggerFactory.getLogger().error(exp.getMessage());
//				}
//			}
//		});
		
		this.setSize(420, 160);
		this.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Embedded_Server"));
		GUICoreUtils.centerWindow(this);
	}

    /**
     * 检验button是否可用
     * @throws Exception 异常
     */
	public void checkButtonEnabled() throws Exception  {
		
		if (FineEmbedServer.isRunning()) {
			GUICoreUtils.setEnabled(startPane, false);
			GUICoreUtils.setEnabled(stopPane, true);
		} else {
			GUICoreUtils.setEnabled(startPane, true);
			GUICoreUtils.setEnabled(stopPane, false);
		}
		
		this.validate();
	}	
}
