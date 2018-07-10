package com.fr.design.style.background;

import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.gradient.GradientBackgroundPane;
import com.fr.design.style.background.impl.ColorBackgroundPane;
import com.fr.design.style.background.impl.NullBackgroundPane;
import com.fr.general.Background;
import com.fr.general.Inter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author kerry
 * @date 2018/1/17
 */
public class BackgroundTabPane extends BackgroundPane {

    private static Map<Class<? extends Background>, BackgroundUIWrapper> tabpane = new LinkedHashMap<>();

    static {
        registerTabpaneBackground(tabpane);
    }


    private static void registerTabpaneBackground(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        map.put(null, BackgroundUIWrapper.create()
                .setType(NullBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Null")));
        map.put(ColorBackground.class, BackgroundUIWrapper.create()
                .setType(ColorBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Color")));
        map.put(GradientBackground.class, BackgroundUIWrapper.create()
                .setType(GradientBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Gradient_Color")));

    }

    public BackgroundTabPane() {
        super();
    }

    protected void initTabPane() {
        int index = 0;
        for (Class<? extends Background> key : tabpane.keySet()) {
            BackgroundUIWrapper wrapper = tabpane.get(key);
            wrapper.setIndex(index++);
            tabbedPane.addTab(Inter.getLocText(wrapper.getTitle()), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        }
    }

    protected BackgroundUIWrapper getBackgroundUIWrapper(Background background) {
        return tabpane.get(background == null ? null : background.getClass());
    }

    protected BackgroundDetailPane getTabItemPane(Background background, int index) {
        BackgroundDetailPane quickPane = cacheMap.get(index);
        if (quickPane == null) {
            BackgroundUIWrapper uiWrapper = getBackgroundUIWrapper(background);
            quickPane = BackgroundFactory.createByWrapper(uiWrapper);
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
            quickPane = createDetailPaneByIndex(index);
            tabbedPane.setComponentAt(index, quickPane);
            cacheMap.put(index, quickPane);
            quickPane.addChangeListener(backgroundChangeListener);
        }
        return quickPane;
    }

    public BackgroundDetailPane createDetailPaneByIndex(int index) {
        for (BackgroundUIWrapper wrapper : tabpane.values()) {
            if (wrapper.getIndex() == index) {
                return BackgroundFactory.createByWrapper(wrapper);
            }
        }
        return new NullBackgroundPane();
    }

}
