package com.fr.van.chart.gantt.designer.data.data.component;

import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

/**
 * Created by hufan on 2017/1/10.
 */
public abstract class ComboBoxWithButtonPane extends JPanel {
    private UIComboBox comboBoxName;
    private UIButton button;
    private int index = 0;
    private static final int H_GAP = 5;
    private static final int LEFT_GAP = 7;
    private UIObserverListener listener;

    public ComboBoxWithButtonPane() {
        this(0);
    }

    public ComboBoxWithButtonPane(int index) {
        this.index = index;
        comboBoxName = new UIComboBox();
        comboBoxName.setPreferredSize(new Dimension(80,20));

        button = new UIButton(getButtonIcon());
        button.setPreferredSize(new Dimension(20, 20));
        button.addActionListener(getButtonListener());

        UILabel title = new UILabel(getTitleText());
        title.setPreferredSize(new Dimension(80, 20));

        this.setLayout(new BorderLayout(H_GAP, 0));
        this.add(comboBoxName, BorderLayout.CENTER);
        this.add(button, BorderLayout.EAST);
        this.add(title, BorderLayout.WEST);
        this.setBorder(BorderFactory.createEmptyBorder(0, LEFT_GAP, 0, 0));
    }

    protected abstract String getTitleText();

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    protected abstract Icon getButtonIcon();

    /**
     * 刷新Box的选项.
     */
    public void refreshBoxItems(java.util.List list) {
        if(comboBoxName == null) {
            return;
        }

        Object ob = comboBoxName.getSelectedItem();
        comboBoxName.removeAllItems();

        int length = list.size();
        for(int i = 0; i < length; i++) {
            comboBoxName.addItem(list.get(i));
        }

        comboBoxName.getModel().setSelectedItem(ob);
    }

    /**
     * 清空box里所有东西
     */
    public void clearBoxItems(){
        if(comboBoxName == null){
            return;
        }
        comboBoxName.removeAllItems();

    }

    public void setComboBoxName(String name){
        //清空监听，防止触发改变
        comboBoxName.registerChangeListener(null);

        comboBoxName.setSelectedItem(name);

        comboBoxName.registerChangeListener(listener);
    }

    public Object getComboBoxName(){
        return comboBoxName.getSelectedItem();
    }

    public void checkBoxUse(boolean hasUse) {
        comboBoxName.setEnabled(hasUse);
        button.setEnabled(hasUse);
    }

    protected abstract ActionListener getButtonListener();

    public void registerUIObserverListener(UIObserverListener listener){
        this.listener = listener;
        comboBoxName.registerChangeListener(listener);
    }
}
