package com.fr.design.style.background;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Background;
import com.fr.general.Inter;

/**
 * Created by ibm on 2017/1/5.
 */
public class BackgroundButtonPane extends BackgroundPane {


    public BackgroundButtonPane() {
        super();
    }

    protected void initTabPane() {
        int index = 0;
        for (Class<? extends Background> key : BackgroundFactory.buttonKindsOfKey()) {
            BackgroundUIWrapper wrapper = BackgroundFactory.getButtonWrapper(key);
            wrapper.setIndex(index++);
            tabbedPane.addTab(Inter.getLocText(wrapper.getTitle()), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        }
    }

    protected BackgroundUIWrapper getBackgroundUIWrapper(Background background) {
        return BackgroundFactory.getButtonWrapper(background == null ? null : background.getClass());
    }

    protected BackgroundDetailPane getTabItemPane(Background background, int index) {
        BackgroundDetailPane quickPane = cacheMap.get(index);
        if (quickPane == null) {
            quickPane = BackgroundFactory.createButtonIfAbsent(background == null ? null : background.getClass());
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
            quickPane = BackgroundFactory.createButtonIfAbsent(index);
            tabbedPane.setComponentAt(index, quickPane);
            cacheMap.put(index, quickPane);
            quickPane.addChangeListener(backgroundChangeListener);
        }
        return quickPane;
    }

}
