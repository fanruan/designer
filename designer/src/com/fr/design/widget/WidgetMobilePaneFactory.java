package com.fr.design.widget;

import com.fr.base.FRContext;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.widget.mobile.*;
import com.fr.design.widget.ui.NumberEditorDefinePane;
import com.fr.form.ui.NumberEditor;
import com.fr.form.ui.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by plough on 2018/4/25.
 */
public class WidgetMobilePaneFactory {
    private static Map<Class<? extends Widget>, Class<? extends WidgetMobilePane>> mobilePaneMap = new HashMap<>();

    static {
//        mobilePaneMap.put(NumberEditor.class, NumberEditorDefinePane.class);

//        mobileMap.putAll(ExtraDesignClassManager.getInstance().getCellWidgetOptionsMap());
    }

    private WidgetMobilePaneFactory() {
    }

    public static WidgetMobilePane createWidgetMobilePane(Widget widget) {
        WidgetMobilePane mobilePane = WidgetMobilePane.DEFAULT_PANE;
        try {
            if (mobilePaneMap.containsKey(widget.getClass())) {
                mobilePane = mobilePaneMap.get(widget.getClass()).newInstance();
                mobilePane.populate(widget);
            }
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return mobilePane;
    }

//    public static class RN {
//        private WidgetMobilePane mobilePane;
//        private String cardName;
//
//        public RN(WidgetMobilePane mobilePane, String cardName) {
//            this.mobilePane = mobilePane;
//            this.cardName = cardName;
//        }
//
//        public WidgetMobilePane getMobilePane() {
//            return mobilePane;
//        }
//        public String getCardName() {
//            return cardName;
//        }
//    }
}
