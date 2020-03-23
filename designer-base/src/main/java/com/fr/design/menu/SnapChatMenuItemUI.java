package com.fr.design.menu;

import com.fr.design.gui.imenu.UIMenuItemUI;

import javax.swing.JMenuItem;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * created by Harrison on 2020/03/22
 **/
class SnapChatMenuItemUI extends UIMenuItemUI {
    
    private final SnapChatUpdateAction snapChatUpdateAction;
    
    public SnapChatMenuItemUI(SnapChatUpdateAction snapChatUpdateAction) {
        
        this.snapChatUpdateAction = snapChatUpdateAction;
    }
    
    @Override
    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
        
        super.paintText(g, menuItem, textRect, text);
        
        if (!snapChatUpdateAction.hasRead()) {
            SnapChatUtil.paintSnapChat(g, textRect);
        }
    }
    
}
