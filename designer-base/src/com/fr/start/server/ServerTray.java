package com.fr.start.server;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.general.Inter;
import com.fr.start.StartServer;

/**
 * Create server tray.
 */
public class ServerTray {
	public static boolean JVM_EXIT_ON_TRAY_EXIT = false;

	private MenuItem manangeMenu, startMenu, stopMenu, exitMenu;
	private Image trayStartedImage = BaseUtils.readImage(
				"/com/fr/base/images/oem/trayStarted.png");
	private Image trayStoppedImage = BaseUtils.readImage(
				"/com/fr/base/images/oem/trayStopped.png");

	private ServerManageFrame serverManageFrame;

	private TrayIcon trayIcon;

	private TomcatHost hostTomcatServer;


	public ServerTray(TomcatHost hostTomcatServer) {

		this.hostTomcatServer = hostTomcatServer;

		//p:首先构建右键菜单
		PopupMenu popup = new PopupMenu();
		manangeMenu = new MenuItem(Inter.getLocText("Server-Open_Service_Manager"));
		manangeMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		    	serverManageFrame = ServerManageFrame.getServerManageFrame(ServerTray.this.hostTomcatServer);
		    	if(!serverManageFrame.isVisible()) {
		    		serverManageFrame.setVisible(true);
		    	}
		    	serverManageFrame.toFront();//p:到第一个.
			}
		});
        startMenu = new MenuItem(Inter.getLocText("FR-Server_Embedded_Server_Start"));
        stopMenu = new MenuItem(Inter.getLocText("FR-Server_Embedded_Server_Stop"));
		exitMenu = new MenuItem(Inter.getLocText("Exit"));
		//创建打开监听器
		ActionListener startListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TomcatHost tomcatServer = StartServer.getInstance();
				try {
					if(!tomcatServer.isStarted()) {
						tomcatServer.start();
						tomcatServer.addAndStartLocalEnvHomeWebApp();//暂停后再打开Tomcat,需要addApp
					}
				} catch(Exception exp) {
                    FRContext.getLogger().error(exp.getMessage(), exp);
				}
			}
		};
		ActionListener stopListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TomcatHost tomcatServer = StartServer.getInstance();
				try {
					if(tomcatServer.isStarted()) {
						tomcatServer.stop();
					}
				} catch(Exception exp) {
                    FRContext.getLogger().error(exp.getMessage(), exp);
				}
			}
		};
		startMenu.addActionListener(startListener);
		stopMenu.addActionListener(stopListener);
		//创建退出菜单监听器
		ActionListener exitListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		};

		exitMenu.addActionListener(exitListener);
		popup.add(manangeMenu);
		popup.addSeparator();
		popup.add(startMenu);
		popup.add(stopMenu);
		popup.addSeparator();
		popup.add(exitMenu);

		//p:开始创建托盘.
		trayIcon = new TrayIcon(trayStartedImage, Inter.getLocText("Server-Embedded_Server"), popup);
		trayIcon.setImageAutoSize(true);
		trayIcon.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		    	if(e.getClickCount() < 2) {
		    		return;
		    	}

		    	ServerManageFrame serverManageFrame = ServerManageFrame.getServerManageFrame(ServerTray.this.hostTomcatServer);
		    	if(!serverManageFrame.isVisible()) {
		    		serverManageFrame.setVisible(true);
		    	}
		    	serverManageFrame.toFront();//p:到第一个.
		    }
		});

		TrayIcon[] ti = SystemTray.getSystemTray().getTrayIcons();
		for(int i = 0;i <ti.length; i++){
			SystemTray.getSystemTray().remove(ti[i]);
		}

		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e) {
			System.err.println("Can not create the System Tray:" + e);
		}

		//p:先check
		checkPopupMenuItemEnabled(this.hostTomcatServer);

		// TODOJ
		this.hostTomcatServer.addListener(new MyTomcatListner());
		try {
			if (!this.hostTomcatServer.isStarted()) {
				this.hostTomcatServer.start();
			}
		} catch (Exception e){
			FRContext.getLogger().error(e.getMessage(), e);
		}
	}

	private void exit() {
		if (hostTomcatServer != null) {
			try {
				if(hostTomcatServer.isStarted()) {
					hostTomcatServer.exit();
				}
			} catch(Exception exp) {
                FRContext.getLogger().error(exp.getMessage(), exp);
			}

			hostTomcatServer = null;
		}


		SystemTray.getSystemTray().remove(trayIcon);

		if (JVM_EXIT_ON_TRAY_EXIT) {
			System.exit(0);
		}
	}

	class MyTomcatListner implements TomcatServerListener {
		/**
		 * Started
		 */
		public void started(TomcatHost tomcatServer) {
			checkPopupMenuItemEnabled(tomcatServer);
		}

		/**
		 * Stopped
		 */
		public void stopped(TomcatHost tomcatServer) {
			checkPopupMenuItemEnabled(tomcatServer);
		}

		@Override
		public void exited(TomcatHost tomcatServer) {
			exit();
		}
	}

	private void checkPopupMenuItemEnabled(TomcatHost tomcatServer) {
		try {
			if(tomcatServer.isStarted()) {
				startMenu.setEnabled(false);
				stopMenu.setEnabled(true);

				trayIcon.setImage(trayStartedImage);
			} else {
				startMenu.setEnabled(true);
				stopMenu.setEnabled(false);

				trayIcon.setImage(trayStoppedImage);
			}

			if (serverManageFrame != null) {
				serverManageFrame.checkButtonEnabled();
				serverManageFrame.repaint();
			}
		} catch(Exception exp) {
            FRContext.getLogger().error(exp.getMessage(), exp);
		}
	}

}