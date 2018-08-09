package com.fr.design.style.background;

import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.base.background.ImageFileBackground;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.gradient.GradientBackgroundPane;
import com.fr.design.style.background.impl.ColorBackgroundPane;
import com.fr.design.style.background.impl.ImageBackgroundPane;
import com.fr.design.style.background.impl.NullBackgroundPane;
import com.fr.general.Background;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author kerry
 * @date 2018/1/29
 */
public class BackgroundCardSwitchButtonPane extends BackgroundButtonPane {

    private static Map<Class<? extends Background>, BackgroundUIWrapper> cardSwitchButton = new LinkedHashMap<>();

    static {
        registerCardSwitchBtnBackground(cardSwitchButton);
    }


    private static void registerCardSwitchBtnBackground(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        map.put(ColorBackground.class, BackgroundUIWrapper.create()
                .setType(ColorBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Color")));
        map.put(ImageFileBackground.class, BackgroundUIWrapper.create()
                .setType(ImageBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Image")));
        map.put(GradientBackground.class, BackgroundUIWrapper.create()
                .setType(GradientBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Gradient_Color")));

    }

    public BackgroundCardSwitchButtonPane() {
        super();
    }

    @Override
    protected void initTabPane() {
        int index = 0;
        for (Class<? extends Background> key : cardSwitchButton.keySet()) {
            BackgroundUIWrapper wrapper = cardSwitchButton.get(key);
            wrapper.setIndex(index++);
            tabbedPane.addTab(com.fr.design.i18n.Toolkit.i18nText(wrapper.getTitle()), FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane());
        }
    }

    @Override
    protected BackgroundUIWrapper getBackgroundUIWrapper(Background background) {
        return cardSwitchButton.get(background == null ? null : background.getClass());
    }


    @Override
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

    @Override
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
        for (BackgroundUIWrapper wrapper : cardSwitchButton.values()) {
            if (wrapper.getIndex() == index) {
                return BackgroundFactory.createByWrapper(wrapper);
            }
        }
        return new NullBackgroundPane();
    }

}
