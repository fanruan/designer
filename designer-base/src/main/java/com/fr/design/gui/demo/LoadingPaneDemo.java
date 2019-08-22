/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.demo;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.gui.frpane.LoadingBasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.log.FineLoggerFactory;

import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * user   : Richer
 * version: 6.5.5
 * Date   : 11-7-1
 * Time   : 下午3:32
 */
public class LoadingPaneDemo extends JPanel {
    public LoadingPaneDemo() {
        setLayout(FRGUIPaneFactory.createCenterFlowLayout());
        UIButton btn = new UIButton("点我看效果");
        add(btn);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadingBasicPane lb = new LoadingBasicPane() {
                    protected void initComponents(JPanel container) {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                FineLoggerFactory.getLogger().error(e.getMessage(), e);
                                Thread.currentThread().interrupt();
                            }
                            container.add(new UIButton(i + "adfadwdadawdwad"));
                        }
                    }
                    
                    @Override
                    protected String title4PopupWindow() {
                    	return "测试";
                    }
                };

                BasicDialog dlg = lb.showWindow(null);
                dlg.setVisible(true);
            }
        });
    }
}