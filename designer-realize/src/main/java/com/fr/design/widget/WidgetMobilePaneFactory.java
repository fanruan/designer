package com.fr.design.widget;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.widget.mobile.WidgetMobilePane;
import com.fr.design.widget.ui.mobile.MultiFileEditorMobilePane;
import com.fr.design.widget.ui.mobile.ScanCodeMobilePane;
import com.fr.form.ui.MultiFileEditor;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.Widget;
import com.fr.log.FineLoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by plough on 2018/4/25.
 */
public class WidgetMobilePaneFactory {
    private static Map<Class<? extends Widget>, Class<? extends WidgetMobilePane>> mobilePaneMap = new HashMap<>();

    static {
        mobilePaneMap.put(MultiFileEditor.class, MultiFileEditorMobilePane.class);
        mobilePaneMap.put(TextEditor.class, ScanCodeMobilePane.class);
        mobilePaneMap.putAll(ExtraDesignClassManager.getInstance().getCellWidgetMobileOptionsMap());
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
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return mobilePane;
    }
}
