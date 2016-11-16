package com.fr.design.dialog.mobile;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fanglei on 2016/11/14.
 * 这个toolbar类需要被design和design_form两个模块复用，所以为了结构精简，抽出共同方法作为抽象类供它们使用
 */
public abstract class MobileToolBarBeanPane<T> extends BasicBeanPane<T> {
    //缩放选项面板
    private MobileRadioCheckPane zoomCheckPane;

    //刷新选项面板
    private MobileRadioCheckPane refreshCheckPane;

    public MobileRadioCheckPane getZoomCheckPane() {
        return zoomCheckPane;
    }

    public MobileRadioCheckPane getRefreshCheckPane() {
        return refreshCheckPane;
    }

    public MobileToolBarBeanPane() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());
        JPanel toobarsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        UILabel uiLabel = new UILabel("html5");
        uiLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        zoomCheckPane = new MobileRadioCheckPane(Inter.getLocText("FR-Designer_Mobile-Zoom"));
        refreshCheckPane = new MobileRadioCheckPane("刷新");

        toobarsPane.add(uiLabel, BorderLayout.WEST);
        toobarsPane.add(zoomCheckPane, BorderLayout.CENTER);
        toobarsPane.add(refreshCheckPane, BorderLayout.EAST);
        borderPane.add(toobarsPane);
        this.add(borderPane);
    }

    @Override
    protected String title4PopupWindow() {
        return "工具栏";
    }

}
