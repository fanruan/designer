package com.fr.design.mainframe.chart.gui.item;

import com.fr.design.gui.icombobox.UIComboBox;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by hufan on 2016/10/11.
 */
public class FlexibleComboBox extends UIComboBox {
    //新旧分界线
    private int partition = 0;
    //分界线上还是分界线下
    private boolean bottom = true;
    //图表是否开启切换模式
    private boolean multiMode = false;
    //当前下拉框处理的事件类型
    private ItemEvenType itemEvenType = ItemEvenType.DEFAULT;

    public boolean isReactor(){
        return itemEvenType == ItemEvenType.REACTOR;
    }

    public int getPartition() {
        return partition;
    }

    public void setPartition(int partition) {
        this.partition = partition;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public boolean isMultiMode() {
        return multiMode;
    }

    public void setMultiMode(boolean multiMode) {
        this.multiMode = multiMode;
    }

    public ItemEvenType getItemEvenType() {
        return itemEvenType;
    }

    public void setItemEvenType(ItemEvenType itemEvenType) {
        this.itemEvenType = itemEvenType;
    }

    public int getRelatedSelectedIndex(){
        return isMultiMode() && isBottom() ? (super.getSelectedIndex() + partition) : super.getSelectedIndex();
    }

    public void setRelatedSelectedIndex(int index){
        if (isMultiMode() && isBottom()){
            super.setSelectedIndex(index - partition);
        }else {
            super.setSelectedIndex(index);
        }
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
