package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.color.NewColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.ui.Widget;
import com.fr.form.ui.mobile.MobileStyle;
import com.fr.general.FRFont;
import com.fr.invoke.Reflect;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class MobileStyleDefinePane extends BasicBeanPane<MobileStyle> {

    private Widget widget;
    private MobileStyleCustomDefinePane customBeanPane;
    private Class<? extends MobileStyle> mobileStyleClazz;
    private NewColorSelectBox colorSelectBox;

    MobileStyleDefinePane(Widget widget, Class<? extends MobileStyleCustomDefinePane> customBeanPaneClass,
                          Class<? extends MobileStyle> mobileStyleClazz) {
        this.widget = widget;
        this.customBeanPane = Reflect.on(customBeanPaneClass).create(widget).get();
        this.mobileStyleClazz = mobileStyleClazz;
        init();
    }

    @Override
    public void populateBean(MobileStyle ob) {
        this.customBeanPane.populateBean(ob);
        colorSelectBox.setSelectObject(ob.getBackground());
    }

    @Override
    public MobileStyle updateBean() {
        MobileStyle mobileStyle = Reflect.on(mobileStyleClazz).create().get();
        this.widget.setMobileStyle(mobileStyle);
        this.customBeanPane.updateBean();
        mobileStyle.setBackground(colorSelectBox.getBackground());
        return mobileStyle;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    private void init() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        createGeneralPane();
        createCustomPane();
    }

    private void createGeneralPane() {
        createPreviewPane();
        createBackgroundPane();
    }

    private void createPreviewPane() {
        JPanel mobileStylePreviewPane = this.customBeanPane.createPreviewPane();
        if(mobileStylePreviewPane != null) {
            JPanel previewPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            TitledBorder titledBorder = GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Basic_Widget_Style_Preview"), null);
            titledBorder.setTitleFont(FRFont.getInstance("PingFangSC-Regular", Font.PLAIN, 12, new Color(0x2f8ef100)));
            previewPane.setBorder(titledBorder);
            previewPane.setPreferredSize(new Dimension(500, 83));
            previewPane.add(mobileStylePreviewPane, BorderLayout.CENTER);
            this.add(previewPane, BorderLayout.NORTH);
        }
    }

    private void createBackgroundPane() {

        JPanel backgroundPane = new JPanel();
        backgroundPane.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        backgroundPane.setPreferredSize(new Dimension(500, 65));

        TitledBorder titledBorder = GUICoreUtils.createTitledBorder(Toolkit.i18nText("Fine-Design_Mobile_Common_Attribute"), null);
        titledBorder.setTitleFont(FRFont.getInstance("PingFangSC-Regular", Font.PLAIN, 12, Color.BLUE));
        backgroundPane.setBorder(titledBorder);

        UILabel colorSelectLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_Widget_Background"), UILabel.RIGHT);
        colorSelectLabel.setPreferredSize(new Dimension(65, 20));

        colorSelectBox = new NewColorSelectBox(152);
        colorSelectBox.addSelectChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            }
        });

        backgroundPane.add(colorSelectLabel);
        backgroundPane.add(colorSelectBox);

        this.add(backgroundPane, BorderLayout.NORTH);
    }

    private void createCustomPane() {
        JPanel configPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        TitledBorder titledBorder = GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set"), null);
        titledBorder.setTitleFont(FRFont.getInstance("PingFangSC-Regular", Font.PLAIN, 12, Color.BLUE));
        configPane.setBorder(titledBorder);

        configPane.add(this.customBeanPane, BorderLayout.CENTER);

        this.add(configPane, BorderLayout.CENTER);
    }
}
