package com.fr.start.server;

import com.fr.base.BaseUtils;
import com.fr.design.ui.util.UIUtil;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.ListenerAdaptor;
import com.fr.log.FineLoggerFactory;

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

/**
 * Create server tray.
 */
public class ServerTray {

    private static ServerTray INSTANCE;

    private MenuItem startMenu;

    private MenuItem stopMenu;

    private Image trayStartedImage = BaseUtils.readImage("/com/fr/base/images/oem/trayStarted.png");

    private Image trayStoppedImage = BaseUtils.readImage("/com/fr/base/images/oem/trayStopped.png");

    private ServerManageFrame serverManageFrame;

    private TrayIcon trayIcon;


    private ServerTray() {

        listen();
        //p:首先构建右键菜单
        PopupMenu popup = new PopupMenu();
        MenuItem manangeMenu = new MenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Open_Service_Manager"));
        manangeMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                serverManageFrame = ServerManageFrame.getServerManageFrame();
                if (!serverManageFrame.isVisible()) {
                    serverManageFrame.setVisible(true);
                }
                serverManageFrame.toFront();//p:到第一个.
            }
        });
        startMenu = new MenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Embedded_Server_Start"));
        stopMenu = new MenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Embedded_Server_Stop"));
        MenuItem exitMenu = new MenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Exit"));
        //创建打开监听器
        ActionListener startListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    FineEmbedServer.start();
                } catch (Exception exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                }
            }
        };
        ActionListener stopListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    FineEmbedServer.stop();
                } catch (Throwable exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                }
            }
        };
        startMenu.addActionListener(startListener);
        stopMenu.addActionListener(stopListener);
        //创建退出菜单监听器
        ActionListener exitListener = new ActionListener() {

            @Override
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
        trayIcon = new TrayIcon(trayStartedImage, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Server_Embedded_Server"), popup);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() < 2) {
                    return;
                }

                ServerManageFrame serverManageFrame = ServerManageFrame.getServerManageFrame();
                if (!serverManageFrame.isVisible()) {
                    serverManageFrame.setVisible(true);
                }
                serverManageFrame.toFront();//p:到第一个.
            }
        });

        TrayIcon[] icons = SystemTray.getSystemTray().getTrayIcons();
        for (TrayIcon icon : icons) {
            SystemTray.getSystemTray().remove(icon);
        }

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Can not create the System Tray:" + e);
        }

        checkPopupMenuItemEnabled();
    }

    private void listen() {

        ListenerAdaptor listenerAdaptor = new ListenerAdaptor() {

            @Override
            protected void on(Event event) {

                checkPopupMenuItemEnabled();
            }
        };
        EventDispatcher.listen(EmbedServerEvent.AfterStart, listenerAdaptor);
        EventDispatcher.listen(EmbedServerEvent.AfterStop, listenerAdaptor);
    }

    private void exit() {

        FineEmbedServer.stop();
        SystemTray.getSystemTray().remove(trayIcon);
    }

    private void checkPopupMenuItemEnabled() {

        try {
            if (FineEmbedServer.isRunning()) {
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
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
        }
    }

    public static void init() {
        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                if (INSTANCE == null) {
                    INSTANCE = new ServerTray();
                }
            }
        });
    }
}