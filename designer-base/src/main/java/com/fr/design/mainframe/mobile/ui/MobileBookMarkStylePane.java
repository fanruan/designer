package com.fr.design.mainframe.mobile.ui;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.MobileBookMarkStyleProvider;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.mobile.MobileBookMarkStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/23
 */
public class MobileBookMarkStylePane extends BasicBeanPane<MobileBookMarkStyle> {

    public static ListCellRenderer renderer = new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof MobileBookMarkStyle) {
                this.setText((value.toString()));
            }
            return this;
        }
    };

    private DefaultListModel<String> listModel;
    private JList bookMarkList;
    private JPanel rightPane;
    private CardLayout card;
    private Map<String, BasicBeanPane<MobileBookMarkStyle>> map = new HashMap<>();

    public MobileBookMarkStylePane() {
        initComponent();
    }

    private void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.listModel = new DefaultListModel<>();
        this.card = new CardLayout();
        this.rightPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        this.rightPane.setLayout(card);
        initDefaultAndExtraPanel();
        initLeftListPanel();
        initRightPanel();
    }

    private void initDefaultAndExtraPanel() {
        List<MobileBookMarkStyleProvider> list = getMobileBookMarkStyleProvider();
        for (MobileBookMarkStyleProvider bookMarkStyleProvider : list) {
            String displayName = bookMarkStyleProvider.displayName();
            MobileBookMarkStyleDefinePane mobileBookMarkStyleDefinePane = new MobileBookMarkStyleDefinePane(
                    bookMarkStyleProvider);
            listModel.addElement(displayName);
            rightPane.add(displayName, mobileBookMarkStyleDefinePane);
            map.put(displayName, mobileBookMarkStyleDefinePane);
        }
    }

    private void initLeftListPanel() {
        bookMarkList = new JList<>(listModel);
        bookMarkList.setCellRenderer(renderer);
        bookMarkList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                card.show(rightPane, (String) bookMarkList.getSelectedValue());
            }
        });
        JPanel leftPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
        leftPanel.add(bookMarkList, BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(100, 500));
        this.add(leftPanel, BorderLayout.WEST);
    }

    private void initRightPanel() {
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        centerPane.setPreferredSize(new Dimension(500, 500));
        centerPane.add(rightPane, BorderLayout.CENTER);
        this.add(centerPane, BorderLayout.CENTER);
    }


    private List<MobileBookMarkStyleProvider> getMobileBookMarkStyleProvider() {
        DefaultMobileBookMarkStyleProvider defaultMobileBookMarkStyleProvider = new DefaultMobileBookMarkStyleProvider();
        Set<MobileBookMarkStyleProvider> mobileBookMarkStyleProviders = ExtraDesignClassManager.getInstance().getArray(
                MobileBookMarkStyleProvider.XML_TAG);
        List<MobileBookMarkStyleProvider> list = new ArrayList<>();
        list.add(defaultMobileBookMarkStyleProvider);
        list.addAll(mobileBookMarkStyleProviders);
        return Collections.unmodifiableList(list);
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }


    @Override
    public void populateBean(MobileBookMarkStyle mobileBookMarkStyle) {
        if (mobileBookMarkStyle != null) {
            List<MobileBookMarkStyleProvider> bookMarkStyleProviders = getMobileBookMarkStyleProvider();
            int i = 0;
            for (MobileBookMarkStyleProvider bookMarkStyleProvider : bookMarkStyleProviders) {
                if (mobileBookMarkStyle.getClass() == bookMarkStyleProvider.classForMobileBookMarkStyle()) {
                    String displayName = bookMarkStyleProvider.displayName();
                    bookMarkList.setSelectedIndex(i);
                    map.get(displayName).populateBean(mobileBookMarkStyle);
                    card.show(rightPane, displayName);
                    return;
                }
                i++;
            }
        }
        bookMarkList.setSelectedIndex(0);
    }

    @Override
    public MobileBookMarkStyle updateBean() {
        return map.get(bookMarkList.getSelectedValue()).updateBean();
    }


}
