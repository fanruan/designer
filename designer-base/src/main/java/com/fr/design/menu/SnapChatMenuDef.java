package com.fr.design.menu;

import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.gui.imenu.UIScrollMenu;
import com.fr.design.notification.SnapChat;
import com.fr.design.notification.SnapChatConfig;
import com.fr.design.notification.SnapChatKey;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * created by Harrison on 2020/03/16
 **/
public class SnapChatMenuDef extends MenuDef implements SnapChat {
    
    private SnapChatKey uniqueKey;
    private SnapChatMenuUI menuUI = new SnapChatMenuUI(this);
    
    public SnapChatMenuDef(String name, SnapChatKey uniqueKey) {
        super(name);
        this.uniqueKey = uniqueKey;
    }
    
    public SnapChatMenuDef(Boolean rePaint, SnapChatKey uniqueKey) {
        super(rePaint);
        this.uniqueKey = uniqueKey;
    }
    
    public SnapChatMenuDef(String name, char mnemonic, SnapChatKey uniqueKey) {
        super(name, mnemonic);
        this.uniqueKey = uniqueKey;
    }
    
    @Override
    public boolean hasRead() {
        
        String calcKey = calcKey();
        return SnapChatConfig.getInstance().hasRead(calcKey);
    }
    
    @Override
    public void markRead() {
        
        String calcKey = calcKey();
        SnapChatConfig.getInstance().markRead(calcKey);
    }
    
    @Override
    public SnapChatKey key() {
        
        return this.uniqueKey;
    }
    
    @Override
    protected MenuListener createMenuListener() {
        
        return new SnapChatMenuListener();
    }
    
    private String calcKey() {
        
        return key().calc();
    }
    
    @Override
    protected UIMenu createJMenu0() {
    
        UIMenu createdJMenu;
        if (hasScrollSubMenu) {
            createdJMenu = new SnapChatUIScrollMenu(this.getName());
        } else if (isHeadMenu){
            createdJMenu = new SnapChatUIHeadMenu(this.getName());
        } else {
            createdJMenu = new SnapChatUIMenu(this.getName());
        }
        return createdJMenu;
    }
    
    private class SnapChatMenuListener implements MenuListener {
        
        @Override
        public void menuSelected(MenuEvent e) {
            
            markRead();
            Object source = e.getSource();
            if (!(source instanceof JMenu)) {
                return;
            }
            updateMenu();
        }
        
        @Override
        public void menuDeselected(MenuEvent e) {
        
        }
        
        @Override
        public void menuCanceled(MenuEvent e) {
        
        }
    }
    
    private class SnapChatUIScrollMenu extends UIScrollMenu {
    
        public SnapChatUIScrollMenu(String s) {
            super(s);
        }
    
        @Override
        public void updateUI() {
            setUI(menuUI);
        }
    }
    
    private class SnapChatUIMenu extends UIMenu {
    
        public SnapChatUIMenu(String name) {
            
            super(name);
        }
    
        @Override
        public void updateUI() {
            
            setUI(menuUI);
        }
    }
    
    private class SnapChatUIHeadMenu extends UIMenu {
    
        public SnapChatUIHeadMenu(String name) {
            
            super(name);
        }
    
        @Override
        public void updateUI() {
            
            setUI(menuUI);
        }
    }
    
    public static void main(String[] args) {
        
        BufferedImage image = new BufferedImage(16, 16, Image.SCALE_DEFAULT);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.green);
        Ellipse2D.Double shape =
                new Ellipse2D.Double(2, 2, 1, 1);
        g2d.fill(shape);
        g2d.draw(shape);
        System.out.println();
    }
}
