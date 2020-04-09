package com.fr.design.mainframe.mobile.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.cardtag.mobile.DefaultMobileTemplateStyle;
import com.fr.general.cardtag.mobile.DownMenuStyle;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;
import com.fr.general.cardtag.mobile.SliderStyle;
import com.fr.general.cardtag.mobile.UpMenuStyle;
import com.fr.general.cardtag.mobile.UniteStyle;
import com.fr.invoke.Reflect;
import com.fr.log.FineLoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class TemplateStyleDefinePaneFactory {
    private static Map<String, StyleDefinePaneUI> defineMap = new HashMap<String, StyleDefinePaneUI>();

    static {
        defineMap.put(DefaultMobileTemplateStyle.STYLE_NAME, new StyleDefinePaneUI(DefaultMobileStyleDefinePane.class));
        defineMap.put(UpMenuStyle.STYLE_NAME, new StyleDefinePaneUI(UpMenuStyleDefinePane.class));
        defineMap.put(DownMenuStyle.STYLE_NAME, new StyleDefinePaneUI(DownMenuStyleDefinePane.class));
        defineMap.put(SliderStyle.STYLE_NAME, new StyleDefinePaneUI(SliderStyleDefinePane.class));
        defineMap.put(UniteStyle.STYLE_NAME, new StyleDefinePaneUI(UniteStyleDefinePane.class));
    }

    public static BasicBeanPane<MobileTemplateStyle> createDefinePane(String style, WCardTagLayout tagLayout) {
        StyleDefinePaneUI styleDefinePaneUI = defineMap.get(style);
        if(styleDefinePaneUI == null){
            styleDefinePaneUI = defineMap.get(DefaultMobileTemplateStyle.STYLE_NAME);
        }
        Class<? extends BasicBeanPane<MobileTemplateStyle>> clazz = styleDefinePaneUI.getaClass();
        BasicBeanPane<MobileTemplateStyle> quickPane = null;
        try {
            quickPane = Reflect.on(clazz).create(tagLayout).get();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return quickPane;
    }

    private static class StyleDefinePaneUI {
        private Class<? extends BasicBeanPane<MobileTemplateStyle>> aClass;

        public StyleDefinePaneUI(Class<? extends BasicBeanPane<MobileTemplateStyle>> aClass) {
            this.aClass = aClass;
        }

        public Class<? extends BasicBeanPane<MobileTemplateStyle>> getaClass() {
            return aClass;
        }

        public void setaClass(Class<? extends BasicBeanPane<MobileTemplateStyle>> aClass) {
            this.aClass = aClass;
        }
    }
}
