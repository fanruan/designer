package com.fr.design.gui.ibutton;

import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-3-27
 * Time: 下午5:04
 */
public class UIRadioButton extends JRadioButton implements UIObserver, GlobalNameObserver {
    private UIObserverListener uiObserverListener;
    private GlobalNameListener globalNameListener = null;
    private String radioButtonName = StringUtils.EMPTY;

    public UIRadioButton() {
        super();
        initListener();
    }

    public UIRadioButton(Icon icon) {
        super(icon);
        initListener();
    }

    public UIRadioButton(Action a) {
        super(a);
        initListener();
    }

    public UIRadioButton(Icon icon, boolean selected) {
        super(icon, selected);
        initListener();
    }

    public UIRadioButton(String text) {
        super(text);
        initListener();
    }

    public UIRadioButton(String text, boolean selected) {
        super(text, selected);
        initListener();
    }

    public UIRadioButton(String text, Icon icon) {
        super(text, icon);
        initListener();
    }


    public UIRadioButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        initListener();
    }

    private void initListener() {
        if (shouldResponseChangeListener()) {
            this.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (uiObserverListener == null || e.getStateChange() != ItemEvent.SELECTED) {
                        return;
                    }
                    if (globalNameListener != null && shouldResponseNameListener()) {
                        globalNameListener.setGlobalName(radioButtonName);
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    public void registerChangeListener(UIObserverListener listener) {
        this.uiObserverListener = listener;
    }


    public void setGlobalName(String name) {
        radioButtonName = name;
    }

    public String getGlobalName() {
        return radioButtonName;
    }

    /**
     * 组件是否需要响应添加的观察者事件
     *
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * 注册观察者监听事件
     * @param listener 观察者监听事件
     */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     *  组件是否需要响应观察者事件
     * @return 如果需要响应观察者事件则返回true，否则返回false
     */
    public boolean shouldResponseNameListener() {
        return true;
    }
}