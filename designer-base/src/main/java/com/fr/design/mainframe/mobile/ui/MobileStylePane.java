package com.fr.design.mainframe.mobile.ui;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.fun.MobileWidgetStyleProvider;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WScaleLayout;
import com.fr.form.ui.mobile.MobileStyle;
import com.fr.form.ui.widget.CRBoundsWidget;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class MobileStylePane extends BasicPane {

    private Widget widget;
    private DefaultListModel<String> listModel;
    private JPanel right;
    private CardLayout card;
    private JList styleList;
    private Map<String, BasicBeanPane<MobileStyle>> map = new HashMap<>();

    public MobileStylePane(Widget widget) {
        if(widget instanceof WScaleLayout) {
            this.widget = ((CRBoundsWidget)((WScaleLayout) widget).getBoundsWidget()).getWidget();
        } else {
            this.widget = widget;
        }
        init();
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    public void populate(MobileStyle mobileStyle) {
        if(mobileStyle != null) {
            MobileWidgetStyleProvider[] styleProviders = getMobileWidgetStyleProviders();
            for(int i = 0; i < styleProviders.length; i ++) {
                if(mobileStyle.getClass() == styleProviders[i].classForMobileStyle()) {
                    String displayName = styleProviders[i].displayName();
                    styleList.setSelectedIndex(i);
                    map.get(displayName).populateBean(mobileStyle);
                    card.show(right, displayName);
                    return;
                }
            }
        }
        styleList.setSelectedIndex(0);
    }

    public MobileStyle update() {
        return map.get(styleList.getSelectedValue()).updateBean();
    }

    private void init() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        listModel = new DefaultListModel<>();
        card = new CardLayout();
        right = FRGUIPaneFactory.createCardLayout_S_Pane();
        right.setLayout(card);
        MobileWidgetStyleProvider[] styleProviders = getMobileWidgetStyleProviders();
        for(MobileWidgetStyleProvider styleProvider: styleProviders) {
            this.addProvider2View(styleProvider);
        }
        this.addWestList();
        this.addCenterConfig();
    }

    private void addWestList() {
        styleList = new JList<>(listModel);
        styleList.setCellRenderer(render);
        styleList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                String selectedValue = (String)styleList.getSelectedValue();
                card.show(right, selectedValue);
            }
        });
        JPanel westPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        westPane.add(styleList, BorderLayout.CENTER);
        westPane.setPreferredSize(new Dimension(100, 500));
        this.add(westPane, BorderLayout.WEST);
    }

    private void addCenterConfig() {
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        JPanel attrConfPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.setPreferredSize(new Dimension(500, 500));
        attrConfPane.add(right, BorderLayout.CENTER);
        centerPane.add(attrConfPane, BorderLayout.CENTER);
        this.add(centerPane, BorderLayout.CENTER);
    }

    private void addProvider2View(MobileWidgetStyleProvider styleProvider) {
        String displayName = styleProvider.displayName();
        Class<? extends MobileStyleCustomDefinePane> appearanceClazz = styleProvider.classForWidgetAppearance();
        Class<? extends MobileStyle> mobileStyleClazz = styleProvider.classForMobileStyle();

        listModel.addElement(displayName);
        try {
            BasicBeanPane<MobileStyle> mobileStyleBasicBeanPane = new MobileStyleDefinePane(widget, appearanceClazz, mobileStyleClazz);
            right.add(displayName, mobileStyleBasicBeanPane);
            map.put(displayName, mobileStyleBasicBeanPane);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    private static ListCellRenderer render = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof MobileStyle) {
                MobileStyle l = (MobileStyle) value;
                this.setText(l.toString());
            }
            return this;
        }
    };

    private MobileWidgetStyleProvider[] getMobileWidgetStyleProviders() {
        DefaultMobileWidgetStyleProvider defaultMobileWidgetStyleProvider = new DefaultMobileWidgetStyleProvider();
        MobileWidgetStyleProvider[] styleProviders = ExtraDesignClassManager.getInstance().getMobileStyleOfWidget(widget.getXType());
        styleProviders = ArrayUtils.insert(0, styleProviders, defaultMobileWidgetStyleProvider);
        return styleProviders;
    }
}