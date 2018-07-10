package com.fr.design.report.mobile;

import com.fr.base.mobile.MobileFitAttrState;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.report.mobile.ElementCaseMobileAttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 夏翔 on 2016/5/28.
 */
public class AppFitBrowserPane extends BasicBeanPane<ElementCaseMobileAttr> {
    //横屏设置面板
    private MobileRadioGroupPane horizionPane;
    //竖屏设置面板
    private MobileRadioGroupPane verticalPane;
    //效果预览面板
    private AppFitPreviewPane appFitPreviewPane;


    public AppFitBrowserPane(){
        initComponents();

    }

    private void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel borderPane = FRGUIPaneFactory.createTitledBorderPane(this.title4PopupWindow());
        JPanel fitOpsPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        horizionPane = new MobileRadioGroupPane(Inter.getLocText("FR-Designer_Mobile-Horizontal"));
        verticalPane = new MobileRadioGroupPane(Inter.getLocText("FR-Designer_Mobile-Vertical"));
        ActionListener actionListener = getAppPreviewActionListener();
        horizionPane.addActionListener(actionListener);
        verticalPane.addActionListener(actionListener);
        fitOpsPane.add(horizionPane, BorderLayout.NORTH);
        fitOpsPane.add(verticalPane, BorderLayout.SOUTH);
        borderPane.add(fitOpsPane);
        this.add(borderPane);
    }

    public void setAppFitPreviewPane(AppFitPreviewPane appFitPreviewPane) {
        this.appFitPreviewPane = appFitPreviewPane;
    }

    //纵向和横向独立设置
    public int[] getCurrentFitOptions() {
        return new int[]{horizionPane.getSelectRadioIndex(), verticalPane.getSelectRadioIndex()};
    }

    @Override
    public void populateBean(ElementCaseMobileAttr ob) {
        if (ob == null) {
            ob = new ElementCaseMobileAttr();
        }
        horizionPane.populateBean(ob.getHorziontalAttr());
        verticalPane.populateBean(ob.getVerticalAttr());
//        radioCheckPane.populateBean(ob.isZoom());
        appFitPreviewPane.refreshPreview(getCurrentFitOptions());

    }

    @Override
    public ElementCaseMobileAttr updateBean() {
        MobileFitAttrState horizonState = horizionPane.updateBean();
        MobileFitAttrState verticalState = verticalPane.updateBean();
//        boolean isZoom = radioCheckPane.updateBean();
//        return new ElementCaseMobileAttr(horizonState, verticalState, isZoom);
        return new ElementCaseMobileAttr(horizonState, verticalState);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer_Fit");
    }

    private ActionListener getAppPreviewActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] fitOptions = getCurrentFitOptions();
                appFitPreviewPane.refreshPreview(fitOptions);
            }
        };
    }
}
