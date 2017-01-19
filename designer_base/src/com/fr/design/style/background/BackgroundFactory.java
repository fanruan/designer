package com.fr.design.style.background;


import com.fr.base.background.*;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.BackgroundUIProvider;
import com.fr.design.style.background.gradient.GradientBackgroundPane;
import com.fr.design.style.background.impl.*;
import com.fr.general.Background;
import com.fr.general.Inter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by richie on 16/5/18.
 */
public class BackgroundFactory {

    private static Map<Class<? extends Background>, BackgroundUIWrapper> map = new LinkedHashMap<>();
    private static Map<Class<? extends Background>, BackgroundUIWrapper> browser = new LinkedHashMap<>();
    private static Map<Class<? extends Background>, BackgroundUIWrapper> button = new LinkedHashMap<>();

    static {
        registerUniversal(map);
        registerImageBackground(map);
        registerUniversal(browser);
        registerBrowserImageBackground(browser);
        registerExtra(map);
        registerExtra(browser);
        registerButtonBackground(button);
    }

    private static void registerUniversal(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        map.put(null, BackgroundUIWrapper.create()
                .setType(NullBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Null")));
        map.put(ColorBackground.class, BackgroundUIWrapper.create()
                .setType(ColorBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Color")));
        map.put(TextureBackground.class, BackgroundUIWrapper.create()
                .setType(TextureBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Texture")));
        map.put(PatternBackground.class, BackgroundUIWrapper.create()
                .setType(PatternBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Pattern")));
        map.put(GradientBackground.class, BackgroundUIWrapper.create()
                .setType(GradientBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Gradient_Color")));
    }

    private static void registerImageBackground(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        map.put(ImageBackground.class, BackgroundUIWrapper.create()
                .setType(ImageBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Image")));
    }

    private static void registerBrowserImageBackground(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        map.put(ImageBackground.class, BackgroundUIWrapper.create()
                .setType(ImageBackgroundPane4Browser.class).setTitle(Inter.getLocText("FR-Designer_Background_Image")));
    }

    private static void registerButtonBackground(Map<Class<? extends Background>, BackgroundUIWrapper> map){
        map.put(ColorBackground.class, BackgroundUIWrapper.create()
                .setType(ColorBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Color")));
        map.put(ImageBackground.class, BackgroundUIWrapper.create()
                .setType(ImageButtonBackgroundPane.class).setTitle(Inter.getLocText("FR-Designer_Background_Image")));

    }

    private static void registerExtra(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        Set<BackgroundUIProvider> set = ExtraDesignClassManager.getInstance().getArray(BackgroundUIProvider.MARK_STRING);
        for (BackgroundUIProvider provider : set) {
            map.put(provider.targetClass(), BackgroundUIWrapper.create()
                    .setType(provider.targetUIClass()).setTitle(provider.targetTitle()));
        }
    }


    public static Set<Class<? extends Background>> kindsOfKey() {
        return map.keySet();
    }

    public static BackgroundUIWrapper getWrapper(Class<? extends Background> category) {
        return map.get(category);
    }

    public static BackgroundDetailPane createIfAbsent(Class<? extends Background> category) {
        BackgroundUIWrapper wrapper = map.get(category);
        return createByWrapper(wrapper);
    }

    public static BackgroundDetailPane createIfAbsent(int index) {
        for (BackgroundUIWrapper wrapper : map.values()) {
            if (wrapper.getIndex() == index) {
                return createByWrapper(wrapper);
            }
        }
        return new NullBackgroundPane();
    }

    public static Set<Class<? extends Background>> buttonKindsOfKey() {
        return button.keySet();
    }

    public static BackgroundUIWrapper getButtonWrapper(Class<? extends Background> category) {
        return button.get(category);
    }

    public static BackgroundDetailPane createButtonIfAbsent(Class<? extends Background> category) {
        BackgroundUIWrapper wrapper = button.get(category);
        return createByWrapper(wrapper);
    }

    public static BackgroundDetailPane createButtonIfAbsent(int index) {
        for (BackgroundUIWrapper wrapper : button.values()) {
            if (wrapper.getIndex() == index) {
                return createByWrapper(wrapper);
            }
        }
        return new NullBackgroundPane();
    }


    public static Set<Class<? extends Background>> browserKindsOfKey() {
        return browser.keySet();
    }

    public static BackgroundUIWrapper getBrowserWrapper(Class<? extends Background> category) {
        return browser.get(category);
    }

    public static BackgroundDetailPane createBrowserIfAbsent(Class<? extends Background> category) {
        BackgroundUIWrapper wrapper = browser.get(category);
        return createByWrapper(wrapper);
    }

    public static BackgroundDetailPane createBrowserIfAbsent(int index) {
        for (BackgroundUIWrapper wrapper : browser.values()) {
            if (wrapper.getIndex() == index) {
                return createByWrapper(wrapper);
            }
        }
        return new NullBackgroundPane();
    }

    private static BackgroundDetailPane createByWrapper(BackgroundUIWrapper wrapper) {
        Class<? extends BackgroundDetailPane> clazz = wrapper.getType();
        if (clazz == null) {
            clazz = NullBackgroundPane.class;
        }
        BackgroundDetailPane quickPane;
        try {
            quickPane = clazz.newInstance();
        } catch (Exception e) {
            quickPane = new NullBackgroundPane();
        }
        return quickPane;
    }
}
