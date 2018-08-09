package com.fr.design.style.background;


import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.base.background.ImageFileBackground;
import com.fr.base.background.PatternBackground;
import com.fr.base.background.TextureBackground;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.BackgroundUIProvider;
import com.fr.design.style.background.gradient.GradientBackgroundPane;
import com.fr.design.style.background.impl.ColorBackgroundPane;
import com.fr.design.style.background.impl.ImageBackgroundPane;
import com.fr.design.style.background.impl.ImageBackgroundPane4Browser;
import com.fr.design.style.background.impl.ImageButtonBackgroundPane;
import com.fr.design.style.background.impl.NullBackgroundPane;
import com.fr.design.style.background.impl.PatternBackgroundPane;
import com.fr.design.style.background.impl.TextureBackgroundPane;
import com.fr.general.Background;
import com.fr.general.GeneralContext;

import com.fr.plugin.context.PluginContext;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.plugin.observer.PluginEventType;

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
        listenPlugin();
        registerButtonBackground(button);
    }
    
    private static void listenPlugin() {
        
        PluginFilter filter = new PluginFilter() {
            
            @Override
            public boolean accept(PluginContext context) {
                
                return context.contain(BackgroundUIProvider.MARK_STRING);
            }
        };
        GeneralContext.listenPlugin(PluginEventType.BeforeStop, new PluginEventListener() {
            
            @Override
            public void on(PluginEvent event) {
                
                Set<BackgroundUIProvider> set = event.getContext().getRuntime().get(BackgroundUIProvider.MARK_STRING);
                for (BackgroundUIProvider provider : set) {
                    map.remove(provider.targetClass());
                    browser.remove(provider.targetClass());
                }
            }
        }, filter);
        GeneralContext.listenPlugin(PluginEventType.AfterRun, new PluginEventListener() {
            
            @Override
            public void on(PluginEvent event) {
                
                Set<BackgroundUIProvider> set = event.getContext().getRuntime().get(BackgroundUIProvider.MARK_STRING);
                Class<? extends Background> clazz;
                BackgroundUIWrapper wrapper;
                for (BackgroundUIProvider provider : set) {
                    clazz = provider.targetClass();
                    wrapper = BackgroundUIWrapper.create().setType(provider.targetUIClass()).setTitle(provider.targetTitle());
                    map.put(clazz, wrapper);
                    browser.put(clazz, wrapper);
                }
            }
        });
        
    }
    
    private static void registerUniversal(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        map.put(null, BackgroundUIWrapper.create()
                .setType(NullBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Null")));
        map.put(ColorBackground.class, BackgroundUIWrapper.create()
                .setType(ColorBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Color")));
        map.put(TextureBackground.class, BackgroundUIWrapper.create()
                .setType(TextureBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Texture")));
        map.put(PatternBackground.class, BackgroundUIWrapper.create()
                .setType(PatternBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Pattern")));
        map.put(GradientBackground.class, BackgroundUIWrapper.create()
                .setType(GradientBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Gradient_Color")));
    }

    private static void registerImageBackground(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        map.put(ImageFileBackground.class, BackgroundUIWrapper.create()
                .setType(ImageBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Image")));
    }

    private static void registerBrowserImageBackground(Map<Class<? extends Background>, BackgroundUIWrapper> map) {
        map.put(ImageFileBackground.class, BackgroundUIWrapper.create()
                .setType(ImageBackgroundPane4Browser.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Image")));
    }

    private static void registerButtonBackground(Map<Class<? extends Background>, BackgroundUIWrapper> map){
        map.put(ColorBackground.class, BackgroundUIWrapper.create()
                .setType(ColorBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Color")));
        map.put(ImageFileBackground.class, BackgroundUIWrapper.create()
                .setType(ImageButtonBackgroundPane.class).setTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Background_Image")));

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

    public static BackgroundDetailPane createByWrapper(BackgroundUIWrapper wrapper) {
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
