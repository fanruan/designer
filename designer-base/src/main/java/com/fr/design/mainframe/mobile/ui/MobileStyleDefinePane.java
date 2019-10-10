package com.fr.design.mainframe.mobile.ui;

import com.fr.base.background.ColorBackground;
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
    private Color titleColor = new Color(47, 142, 241);

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
        if(ob.getBackground() != null) {
            colorSelectBox.setSelectObject(((ColorBackground)ob.getBackground()).getColor());
        }
    }

    @Override
    public MobileStyle updateBean() {
        MobileStyle mobileStyle = Reflect.on(mobileStyleClazz).create().get();
        this.widget.setMobileStyle(mobileStyle);
        this.customBeanPane.updateBean();
        mobileStyle.setBackground(ColorBackground.getInstance(colorSelectBox.getSelectObject()));
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
            TitledBorder titledBorder = createTitledBorder(Toolkit.i18nText("Fine-Design_Basic_Widget_Style_Preview"));
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

        TitledBorder titledBorder = createTitledBorder(Toolkit.i18nText("Fine-Design_Mobile_Common_Attribute"));
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
        TitledBorder titledBorder = createTitledBorder(Toolkit.i18nText("Fine-Design_Report_Set"));
        configPane.setBorder(titledBorder);

        configPane.add(this.customBeanPane, BorderLayout.CENTER);

        this.add(configPane, BorderLayout.CENTER);
    }

    private TitledBorder createTitledBorder(String title) {
        TitledBorder titledBorder = GUICoreUtils.createTitledBorder(title, titleColor);
        titledBorder.setTitleFont(FRFont.getInstance("PingFangSC-Regular", Font.PLAIN, 12));
        return  titledBorder;
    }
}
