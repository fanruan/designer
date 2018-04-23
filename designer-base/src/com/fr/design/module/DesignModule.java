package com.fr.design.module;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.fun.ElementUIProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.EmailPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.design.mainframe.App;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;
import com.fr.js.EmailJavaScript;
import com.fr.js.JavaScriptImpl;
import com.fr.js.ParameterJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.module.TopModule;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-24
 * Time   : 下午2:52
 * 所有设计器模块的父类
 */
public abstract class DesignModule extends TopModule {
    public static final String LOCALE_FILE_PATH = "com/fr/design/i18n/main";

    public void start() {
        super.start();
        App<?>[] apps = apps4TemplateOpener();
        for (App<?> app : apps) {
            DesignerFrame.registApp(app);
        }
        ModuleContext.registerStartedModule(DesignModule.class.getName(), this);
        StableFactory.registerMarkedClass(ExtraDesignClassManagerProvider.XML_TAG, ExtraDesignClassManager.class);
        ActionFactory.registerCellInsertActionClass(actionsForInsertCellElement());
        ActionFactory.registerFloatInsertActionClass(actionsForInsertFloatElement());
        DesignModuleFactory.registerCreators4Hyperlink(hyperlinkTypes());
    }

    public boolean isStarted() {
        return ModuleContext.isModuleStarted(DesignModule.class.getName());
    }

    /**
     * 返回设计器能打开的模板类型的一个数组列表
     *
     * @return 可以打开的模板类型的数组
     */
    public abstract App<?>[] apps4TemplateOpener();

    /**
     * 国际化文件路径
     *
     * @return 国际化文件路径
     */
    public String[] getLocaleFile() {
        return new String[]{LOCALE_FILE_PATH};
    }

    public Class<?>[] actionsForInsertCellElement() {
        List<Class<?>> classes = new ArrayList<>();
        Set<ElementUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(ElementUIProvider.MARK_STRING);
        for (ElementUIProvider provider : providers) {
            classes.add(provider.actionForInsertCellElement());
        }
        return classes.toArray(new Class<?>[classes.size()]);
    }

    public Class<?>[] actionsForInsertFloatElement() {
        List<Class<?>> classes = new ArrayList<>();
        Set<ElementUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(ElementUIProvider.MARK_STRING);
        for (ElementUIProvider provider : providers) {
            classes.add(provider.actionForInsertFloatElement());
        }
        return classes.toArray(new Class<?>[classes.size()]);
    }

    public NameableCreator[] hyperlinkTypes() {
        return new NameableCreator[]{
                new NameObjectCreator(Inter.getLocText("FR-Hyperlink_Reportlet"), ReportletHyperlink.class, ReportletHyperlinkPane.ChartNoRename.class),
                new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), EmailJavaScript.class, EmailPane.class),
                new NameObjectCreator(Inter.getLocText("Hyperlink-Web_link"), WebHyperlink.class, WebHyperlinkPane.ChartNoRename.class),
                new NameObjectCreator(Inter.getLocText("JavaScript-Dynamic_Parameters"), ParameterJavaScript.class, ParameterJavaScriptPane.ChartNoRename.class),
                new NameObjectCreator(Inter.getLocText("FR-Designer_JavaScript"), JavaScriptImpl.class, JavaScriptImplPane.ChartNoRename.class)
        };
    }

}