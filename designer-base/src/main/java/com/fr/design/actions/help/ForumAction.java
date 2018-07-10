/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.actions.help;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.MenuKeySet;
import com.fr.general.Inter;
import com.fr.general.CloudCenter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-18
 * Time: 上午9:21
 */
public class ForumAction extends UpdateAction {
    public ForumAction() {
        this.setMenuKeySet(FORUM);
        this.setName(getMenuKeySet().getMenuName());
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_help/product_forum.png"));
    }


    /**
     * 动作
     * @param e 事件
     */
    public void actionPerformed(ActionEvent e) {
        String url = CloudCenter.getInstance().acquireUrlByKind("bbs");
        if (StringUtils.isEmpty(url)) {
            FRContext.getLogger().info("The URL is empty!");
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, Inter.getLocText("Set_default_browser"));
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
        } catch (URISyntaxException exp) {
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
        } catch (Exception exp) {
            FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
            FineLoggerFactory.getLogger().error("Can not open the browser for URL:  " + url);
        }
    }

    public static final MenuKeySet FORUM = new MenuKeySet() {
        @Override
        public char getMnemonic() {
            return 0;
        }

        @Override
        public String getMenuName() {
            return Inter.getLocText("Forum");
        }

        @Override
        public KeyStroke getKeyStroke() {
            return null;
        }
    };
}