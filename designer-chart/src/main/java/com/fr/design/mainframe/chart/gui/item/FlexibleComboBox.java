package com.fr.design.mainframe.chart.gui.item;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by hufan on 2016/10/11.
 */
public class FlexibleComboBox extends UIComboBox {
    //当前下拉框处理的事件类型
    private ItemEventType itemEvenType = ItemEventType.DEFAULT;

    public boolean isReactor(){
        return itemEvenType == ItemEventType.REACTOR;
    }

    public ItemEventType getItemEvenType() {
        return itemEvenType;
    }

    public void setItemEvenType(ItemEventType itemEvenType) {
        this.itemEvenType = itemEvenType;
    }

    @Override
    protected void initListener() {
        if (shouldResponseChangeListener()) {
            this.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    fireSetGlobalName();
                }
            });
            this.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    fireSetGlobalName();
                    //只有不在重构状态才会触发下拉框选择时的改变事件
                    if (e.getStateChange() == ItemEvent.SELECTED && !isReactor()) {
                        uiObserverListener.doChange();
                    }
                }
            });
        }
    }
}