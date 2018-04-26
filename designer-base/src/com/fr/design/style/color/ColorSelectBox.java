package com.fr.design.style.color;

import com.fr.base.background.ColorBackground;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.style.AbstractSelectBox;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Color select pane.
 */
public class ColorSelectBox extends AbstractSelectBox<Color> implements UIObserver {
    private static final long serialVersionUID = 2782150678943960557L;

    private Color color;
    private ColorSelectPane colorPane;
    private UIObserverListener uiObserverListener;

    public ColorSelectBox(int preferredWidth) {
        colorPane = getColorSelectPane();
        initBox(preferredWidth);
        iniListener();
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addSelectChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    protected ColorSelectPane getColorSelectPane(){
        return new ColorSelectPane() {
            public void setVisible(boolean b) {
                super.setVisible(b);
            }
        };
    }

    /**
     * 初始化弹出框的面板
     * @param preferredWidth 宽度
     * @return 弹出面板
     */
    public JPanel initWindowPane(double preferredWidth) {
    	// 下拉的时候重新生成面板，刷新最近使用颜色
    	colorPane = getColorSelectPane();
        colorPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                hidePopupMenu();
                color = ((ColorSelectPane) e.getSource()).getColor();
                fireDisplayComponent(ColorBackground.getInstance(color));
            }
        });
        colorPane.setColor(color);
        return colorPane;
    }

    /**
     * 获取当前选中的颜色
     * @return 当前选中的颜色
     */
    public Color getSelectObject() {
        return this.color;
    }

    /**
     *设置选中的颜色
     * @param color 颜色
     */
    public void setSelectObject(Color color) {
        this.color = color;
        colorPane.setColor(color);

        fireDisplayComponent(ColorBackground.getInstance(color));
    }

    /**
     * 注册监听器
     * @param listener 监听器
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    /**
     * 是否相应事件
     * @return 需要相应
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}