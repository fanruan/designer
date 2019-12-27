package com.fr.design.mainframe.backgroundpane;

import com.fr.base.background.ColorBackground;
import com.fr.design.event.UIObserverListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.color.NewColorSelectPane;
import com.fr.general.Background;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * @author zhou
 * @since 2012-5-29下午1:12:14
 */
public class ColorBackgroundQuickPane extends BackgroundQuickPane {

    private NewColorSelectPane detailColorSelectPane;

    public ColorBackgroundQuickPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        detailColorSelectPane = new NewColorSelectPane();
        this.add(detailColorSelectPane, BorderLayout.NORTH);
    }

    public void populateBean(Background background) {
        ColorBackground colorBackgroud = (ColorBackground) background;
        populateColor(colorBackgroud.getColor());
    }

    public Background updateBean() {
        return ColorBackground.getInstance(updateColor());
    }

    public void populateColor(Color color) {
        this.detailColorSelectPane.setColor(color);
    }

    public Color updateColor() {
        this.detailColorSelectPane.updateUsedColor();
        return this.detailColorSelectPane.getNotNoneColor();
    }

    /**
     * 给组件登记一个观察者监听事件
     *
     * @param listener 观察者监听事件
     */
    @Override
    public void registerChangeListener(final UIObserverListener listener) {
        detailColorSelectPane.addChangeListener(new ChangeListenerImpl(listener));
    }

    @Override
    /**
     * 是否为ColorBackground 类型
     *
     * @param background 背景
     * @return 同上
     *
     */
    public boolean accept(Background background) {
        return background instanceof ColorBackground;
    }

    @Override
    /**
     * 窗口名称
     * @return 同上
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Color");
    }

    @Override
    public void reset() {
        this.detailColorSelectPane.setColor(null);
    }
}