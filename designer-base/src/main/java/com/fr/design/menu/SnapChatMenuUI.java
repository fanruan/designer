package com.fr.design.menu;

import com.fr.design.gui.imenu.UIMenuUI;

import javax.swing.JMenuItem;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * created by Harrison on 2020/03/22
 **/
public class SnapChatMenuUI extends UIMenuUI {
    
    private SnapChatMenuDef menuDef;
    
    public SnapChatMenuUI(SnapChatMenuDef menuDef) {
        this.menuDef = menuDef;
    }
    
    @Override
    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
        
        super.paintText(g, menuItem, textRect, text);
        
        if (!menuDef.hasRead()) {
            SnapChatUtil.paintSnapChat(g, textRect);
        }
    }
}
