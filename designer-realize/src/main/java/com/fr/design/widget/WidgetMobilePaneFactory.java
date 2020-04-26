package com.fr.design.widget;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.CellWidgetOptionProvider;
import com.fr.design.widget.mobile.WidgetMobilePane;
import com.fr.design.widget.ui.mobile.MultiFileEditorMobilePane;
import com.fr.design.widget.ui.mobile.ScanCodeMobilePane;
import com.fr.form.ui.MultiFileEditor;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.Widget;
import com.fr.general.GeneralContext;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by plough on 2018/4/25.
 */
public class WidgetMobilePaneFactory {
    private static Map<Class<? extends Widget>, Class<? extends WidgetMobilePane>> mobilePaneMap = new HashMap<>();
    private static Map<Class<? extends Widget>, Class<? extends WidgetMobilePane>> mobilePluginPaneMap = new HashMap<>();

    static {
        mobilePaneMap.put(MultiFileEditor.class, MultiFileEditorMobilePane.class);
        mobilePaneMap.put(TextEditor.class, ScanCodeMobilePane.class);
        mobilePluginPaneMap.putAll(ExtraDesignClassManager.getInstance().getCellWidgetMobileOptionsMap());

        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            @Override
            public void on(PluginEvent event) {
                refreshPluginMap();
            }
        }, new PluginFilter() {
            @Override
            public boolean accept(PluginContext context) {
                return context.contain(PluginModule.ExtraDesign, CellWidgetOptionProvider.XML_TAG);
            }
        });
    }

    private WidgetMobilePaneFactory() {
    }

    private static void refreshPluginMap() {
        mobilePluginPaneMap.clear();
        mobilePluginPaneMap.putAll(ExtraDesignClassManager.getInstance().getCellWidgetMobileOptionsMap());
    }

    public static WidgetMobilePane createWidgetMobilePane(Widget widget) {
        WidgetMobilePane mobilePane = WidgetMobilePane.DEFAULT_PANE;
        try {
            if (mobilePaneMap.containsKey(widget.getClass())) {
                mobilePane = mobilePaneMap.get(widget.getClass()).newInstance();
                mobilePane.populate(widget);
            } else if (mobilePluginPaneMap.containsKey(widget.getClass())){
                mobilePane = mobilePluginPaneMap.get(widget.getClass()).newInstance();
                mobilePane.populate(widget);
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return mobilePane;
    }
}
