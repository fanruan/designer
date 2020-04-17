/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */
package com.fr.design.style.background;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import com.fr.design.gui.frpane.UITabbedPane;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Background;
import com.fr.log.FineLoggerFactory;


public class BackgroundPane extends BasicPane {

    protected UITabbedPane tabbedPane = null;

    private EventListenerList eventChangeList = new EventListenerList();
    protected Map<Integer, BackgroundDetailPane> cacheMap = new HashMap<>();

    
    //需求说: 如果是浏览器背景, 隐藏掉几个button
    public BackgroundPane() {
        this.initComponents();
        this.setPreferredSize(new Dimension(400, 300));
    }


    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        previewPane = new BackgroundPreviewLabel();
        previewPane.addBackgroundChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                fireStateChanged();
            }
        });
        tabbedPane = new UITabbedPane();
        this.add(tabbedPane, BorderLayout.CENTER);

        initTabPane();

        tabbedPane.addChangeListener(backgroundChangeListener);
        tabbedPane.setPreferredSize(new Dimension(200, 210));
    }

    protected void initTabPane() {
        int index = 0;
        for (Class<? extends Background> key : BackgroundFactory.kindsOfKey()) {
            BackgroundUIWrapper wrapper = BackgroundFactory.getWrapper(key);
            wrapper.setIndex(index++);
            tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText(wrapper.getTitle()), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        }
    }

    public void addChangeListener(ChangeListener changeListener) {
        eventChangeList.add(ChangeListener.class, changeListener);
    }

    /**
     */
    public void fireStateChanged() {
        Object[] listeners = eventChangeList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }
    
    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background");
    }


    protected BackgroundDetailPane getTabItemPane(Background background, int index) {
        BackgroundDetailPane quickPane = cacheMap.get(index);
        if (quickPane == null) {
            quickPane = BackgroundFactory.createIfAbsent(background == null ? null : background.getClass());
            quickPane.addChangeListener(backgroundChangeListener);
            cacheMap.put(index, quickPane);
        }
        tabbedPane.setComponentAt(index, quickPane);
        tabbedPane.setSelectedIndex(index);
        return quickPane;
    }

    protected BackgroundDetailPane getTabItemPaneByIndex(int index) {
        BackgroundDetailPane quickPane = cacheMap.get(index);
        if (quickPane == null) {
            quickPane = BackgroundFactory.createIfAbsent(index);
            tabbedPane.setComponentAt(index, quickPane);
            cacheMap.put(index, quickPane);
            quickPane.addChangeListener(backgroundChangeListener);
        }
        return quickPane;
    }


    /**
     * Populate background.
     */
    public void populate(Background background) {
        BackgroundUIWrapper wrapper = getBackgroundUIWrapper(background);
        if (wrapper == null) {
            return;
        }
        int index = wrapper.getIndex();
        BackgroundDetailPane quickPane = getTabItemPane(background, index);
        quickPane.populate(background);

        tabbedPane.doLayout();
        tabbedPane.validate();
    }

    protected BackgroundUIWrapper getBackgroundUIWrapper(Background background) {
        return BackgroundFactory.getWrapper(background == null ? null : background.getClass());
    }

    /**
     * Update background.
     */
    public Background update() {
        int index = tabbedPane.getSelectedIndex();
        BackgroundDetailPane quickPane = getTabItemPaneByIndex(index);
        try {
            return quickPane.update();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }
    /**
     * Change listener.
     */
    protected ChangeListener backgroundChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent evt) {
            previewPane.setBackgroundObject(update());
            previewPane.repaint();
        }
    };
    private BackgroundPreviewLabel previewPane = null;

}