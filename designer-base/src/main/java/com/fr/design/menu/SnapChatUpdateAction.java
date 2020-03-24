package com.fr.design.menu;

import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.notification.SnapChat;
import com.fr.design.notification.SnapChatConfig;
import com.fr.design.notification.SnapChatKey;

import java.awt.event.ActionEvent;

/**
 * created by Harrison on 2020/03/22
 **/
public abstract class SnapChatUpdateAction extends UpdateAction implements SnapChat {
    
    private SnapChatKey uniqueKey;
    
    public SnapChatUpdateAction(SnapChatKey uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
    
    @Override
    public final void actionPerformed(ActionEvent e) {
        
        markRead();
        actionPerformed0(e);
    }
    
    protected abstract void actionPerformed0(ActionEvent e);
    
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
    
    private String calcKey() {
        
        return key().calc();
    }
    
    @Override
    public UIMenuItem createMenuItem() {
        
        Object object = this.getValue(UIMenuItem.class.getName());
        if (object == null && !(object instanceof UIMenuItem)) {
            UIMenuItem menuItem = new UIMenuItem(this);
            // 设置名字用作单元测
            menuItem.setName(getName());
            menuItem.setUI(new SnapChatMenuItemUI(this));
            object = menuItem;
            
            this.putValue(UIMenuItem.class.getName(), object);
        }
        return (UIMenuItem) object;
    }
    
}
