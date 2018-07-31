package com.fr.design.style.background;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;


/**
 * Created by richie on 16/5/18.
 */
public class BackgroundPane4Browser extends BackgroundPane {

    public BackgroundPane4Browser() {
        super();
    }

    protected void initTabPane() {
        int index = 0;
        for (Class<? extends Background> key : BackgroundFactory.browserKindsOfKey()) {
            BackgroundUIWrapper wrapper = BackgroundFactory.getBrowserWrapper(key);
            wrapper.setIndex(index++);
            tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText(wrapper.getTitle()), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        }
    }

    protected BackgroundUIWrapper getBackgroundUIWrapper(Background background) {
        return BackgroundFactory.getBrowserWrapper(background == null ? null : background.getClass());
    }

    protected BackgroundDetailPane getTabItemPane(Background background, int index) {
        BackgroundDetailPane quickPane = cacheMap.get(index);
        if (quickPane == null) {
            quickPane = BackgroundFactory.createBrowserIfAbsent(background == null ? null : background.getClass());
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
            quickPane = BackgroundFactory.createBrowserIfAbsent(index);
            tabbedPane.setComponentAt(index, quickPane);
            cacheMap.put(index, quickPane);
            quickPane.addChangeListener(backgroundChangeListener);
        }
        return quickPane;
    }
}
