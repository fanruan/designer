package com.fr.design.module;

import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.gui.chart.MiddleChartDialog;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.frpane.BaseHyperlinkGroup;
import com.fr.design.gui.frpane.HyperlinkGroupType;
import com.fr.design.mainframe.BaseFormDesigner;
import com.fr.design.mainframe.BaseWidgetPropertyPane;
import com.fr.design.parameter.HierarchyTreePane;
import com.fr.design.parameter.ParameterDesignerProvider;
import com.fr.design.parameter.ParameterReader;
import com.fr.form.ui.Widget;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.solution.sandbox.collection.PluginSandboxCollections;
import com.fr.stable.StableUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 7.0.3
 * Date: 13-7-8
 * Time: 下午1:57
 */
public class DesignModuleFactory {
    private static DesignModuleFactory instance = new DesignModuleFactory();

    private DesignModuleFactory() {

    }

    private HyperlinkGroupType hyperlinkGroupType = new BaseHyperlinkGroup();
    private NameableCreator[] creators4Hyperlink;
    private WidgetOption[] extraOptions;
    private Class<Widget> chartEditorClass;
    private Class<MiddleChartComponent> chartComponentClass;
    private Class<MiddleChartDialog> chartDialogClass;
    private Class<? extends BaseChartPropertyPane> chartPropertyPaneClass;
    private Class newFormAction;
    private Class formParaDesigner;
    private Class paraPropertyPane;
    private Class<? extends HierarchyTreePane> formHierarchyPaneCls;
    private Class<? extends BaseWidgetPropertyPane> widgetPropertyPane;
    private Class buttonDetailPaneClass;
    private java.util.List<ParameterReader> parameterReaderList;


    public static void registerHyperlinkGroupType(HyperlinkGroupType hyperlinkGroupType) {
        instance.hyperlinkGroupType = hyperlinkGroupType;
    }

    public static HyperlinkGroupType getHyperlinkGroupType() {
        return instance.hyperlinkGroupType;
    }

    public static void registerCreators4Hyperlink(NameableCreator[] nameableCreators) {
        instance.creators4Hyperlink = nameableCreators;
    }

    @NotNull
    public static NameableCreator[] getCreators4Hyperlink() {
        return instance.creators4Hyperlink == null? new NameableCreator[0]:instance.creators4Hyperlink;
    }

    public static void registerExtraWidgetOptions(WidgetOption[] options) {
        instance.extraOptions = options;
    }

    public static WidgetOption[] getExtraWidgetOptions() {
        if (instance.extraOptions == null) {
            instance.extraOptions = new WidgetOption[0];
        }

        return instance.extraOptions;
    }

    public static void registerChartEditorClass(Class cls) {
        instance.chartEditorClass = cls;
    }

    public static Class<Widget> getChartEditorClass() {
        return instance.chartEditorClass;
    }

    public static void registerChartComponentClass(Class bcc) {
        instance.chartComponentClass = bcc;
    }

    public static void registerChartDialogClass(Class cd) {
        instance.chartDialogClass = cd;
    }

    public static void registerChartPropertyPaneClass(Class<? extends BaseChartPropertyPane> p) {
        instance.chartPropertyPaneClass = p;
    }


    public static void registerNewFormActionClass(Class f) {
        instance.newFormAction = f;
    }

    public static Class getNewFormAction() {
        return instance.newFormAction;
    }

    public static void registerParaPropertyPaneClass(Class p) {
        instance.paraPropertyPane = p;
    }

    /**
     * 获取参数属性界面
     *
     * @return 参数属性界面.
     */
    public static Object getParaPropertyPane() {
        if (instance.paraPropertyPane != null) {
            try {
                return instance.paraPropertyPane.newInstance();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error("Error in Para PropertyPane");
            }
        }
        return null;
    }

    public static void registerFormParaDesignerClass(Class f) {
        instance.formParaDesigner = f;
    }

    public static ParameterDesignerProvider getFormParaDesigner() {
        if (instance.formParaDesigner != null) {
            try {
                return (ParameterDesignerProvider) instance.formParaDesigner.newInstance();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error("error in form para designer");
            }
        }
        return null;
    }

    public static void registerFormHierarchyPaneClass(Class<? extends HierarchyTreePane> fClass) {
        instance.formHierarchyPaneCls = fClass;
    }

    public static HierarchyTreePane getFormHierarchyPane() {
        return StableUtils.getInstance(instance.formHierarchyPaneCls);
    }

    public static void registerWidgetPropertyPaneClass(Class<? extends BaseWidgetPropertyPane> wp) {
        instance.widgetPropertyPane = wp;
    }

    public static BaseWidgetPropertyPane getWidgetPropertyPane(BaseFormDesigner fd) {
        BaseWidgetPropertyPane wp = null;
        if (instance.widgetPropertyPane != null) {
            wp = StableUtils.getInstance(instance.widgetPropertyPane);
            wp.setEditingFormDesigner(fd);
            wp.refreshDockingView();
        }
        return wp;
    }

    public static MiddleChartComponent getChartComponent(BaseChartCollection collection) {
        MiddleChartComponent bcc = null;
        if (instance.chartComponentClass != null) {
            try {
                bcc = instance.chartComponentClass.newInstance();
                bcc.populate(collection);
            } catch (InstantiationException e) {
                FineLoggerFactory.getLogger().error("Error in ChartComponent instant", e);
            } catch (IllegalAccessException e) {
                FineLoggerFactory.getLogger().error("Error in Access", e);
            }
        }
        return bcc;
    }

    /**
     * kunsnat: 初始化图表向导对话框, 调用静态方法showWindow, 参数window.
     *
     * @return 返回调出的ChartDailog
     */
    public static MiddleChartDialog getChartDialog(Window window) {
        try {
            Constructor<MiddleChartDialog> c;
            if (window instanceof Frame) {
                c = instance.chartDialogClass.getConstructor(Frame.class);
            } else {
                c = instance.chartDialogClass.getConstructor(Dialog.class);
            }
            return c.newInstance(window);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * kunsnat: 获取图表属性界面
     *
     * @return 返回界面.
     */
    public static BaseChartPropertyPane getChartPropertyPane() {
        BaseChartPropertyPane bp = null;
        if (instance.chartPropertyPaneClass != null) {
            bp = StableUtils.getInstance(instance.chartPropertyPaneClass);
        }
        return bp;
    }


    public static void clearChartPropertyPane() {
        if (instance.chartPropertyPaneClass != null) {
            StableUtils.clearInstance(instance.chartPropertyPaneClass);
        }
    }


    public static void registerButtonDetailPaneClass(Class clazz) {
        instance.buttonDetailPaneClass = clazz;
    }

    public static Class getButtonDetailPaneClass() {
        return instance.buttonDetailPaneClass;
    }

    public static void registerParameterReader(ParameterReader reader) {
        if (instance.parameterReaderList == null) {
            instance.parameterReaderList = PluginSandboxCollections.newSandboxList();
        }
        instance.parameterReaderList.add(reader);
    }

    public static ParameterReader[] getParameterReaders() {
        if (instance.parameterReaderList == null) {
            return new ParameterReader[0];
        }
        return instance.parameterReaderList.toArray(new ParameterReader[instance.parameterReaderList.size()]);
    }
}
