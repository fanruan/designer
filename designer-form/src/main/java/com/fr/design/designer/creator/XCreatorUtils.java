/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.creator.cardlayout.XCardAddButton;
import com.fr.design.designer.creator.cardlayout.XCardSwitchButton;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTagLayout;
import com.fr.design.designer.creator.cardlayout.XWCardTitleLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.fun.FormWidgetOptionProvider;
import com.fr.design.fun.ParameterWidgetOptionProvider;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.Button;
import com.fr.form.ui.CardAddButton;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.CheckBox;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.form.ui.ComboBox;
import com.fr.form.ui.ComboCheckBox;
import com.fr.form.ui.DateEditor;
import com.fr.form.ui.EditorHolder;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.FileEditor;
import com.fr.form.ui.FreeButton;
import com.fr.form.ui.IframeEditor;
import com.fr.form.ui.Label;
import com.fr.form.ui.MultiFileEditor;
import com.fr.form.ui.NameWidget;
import com.fr.form.ui.NumberEditor;
import com.fr.form.ui.Password;
import com.fr.form.ui.Radio;
import com.fr.form.ui.RadioGroup;
import com.fr.form.ui.TextArea;
import com.fr.form.ui.TextEditor;
import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetErrorMarker;
import com.fr.form.ui.container.WAbsoluteBodyLayout;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WHorizontalBoxLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.form.ui.container.WScaleLayout;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.form.ui.container.WVerticalBoxLayout;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.GeneralContext;
import com.fr.general.IOUtils;
import com.fr.invoke.Reflect;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.stable.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import java.awt.Container;
import java.awt.Dimension;

/**
 * XCreator的相关处理
 *
 * @author richer
 * @since 6.5.3
 */
public class XCreatorUtils {

    public static java.util.Map<Class<? extends Widget>, Class<?>> objectMap = new java.util.HashMap<Class<? extends Widget>, Class<?>>();

    private static java.util.Map<Class<? extends Widget>, Class<?>> extraObjectMap = new java.util.HashMap<Class<? extends Widget>, Class<?>>();

    public static java.util.Map<Class<? extends Widget>, Class<?>> xLayoutMap = new java.util.HashMap<Class<? extends Widget>, Class<?>>();

    static {
        init();
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {

            @Override
            public void on(PluginEvent event) {

                reInitExtra();
            }
        }, new PluginFilter() {

            @Override
            public boolean accept(PluginContext context) {

                return context.contain(PluginModule.ExtraDesign, ParameterWidgetOptionProvider.XML_TAG)
                        || context.contain(PluginModule.ExtraDesign, FormWidgetOptionProvider.XML_TAG);
            }
        });
    }

    private static void init() {

        putDefault();
        putExtraEditor();
        putDefaultLayouts();
        reInitExtra();
    }

    private static void putDefaultLayouts() {

        xLayoutMap.put(WAbsoluteLayout.class, XWAbsoluteLayout.class);
        xLayoutMap.put(WParameterLayout.class, XWParameterLayout.class);
        xLayoutMap.put(WAbsoluteBodyLayout.class, XWAbsoluteBodyLayout.class);
        xLayoutMap.put(WHorizontalBoxLayout.class, XWHorizontalBoxLayout.class);
        xLayoutMap.put(WBorderLayout.class, XWBorderLayout.class);
        xLayoutMap.put(WCardLayout.class, XWCardLayout.class);
        xLayoutMap.put(WVerticalBoxLayout.class, XWVerticalBoxLayout.class);

        xLayoutMap.put(WFitLayout.class, XWFitLayout.class);
        xLayoutMap.put(WScaleLayout.class, XWScaleLayout.class);
        xLayoutMap.put(WTitleLayout.class, XWTitleLayout.class);
        xLayoutMap.put(WCardTagLayout.class, XWCardTagLayout.class);
        xLayoutMap.put(WCardTitleLayout.class, XWCardTitleLayout.class);
        xLayoutMap.put(WTabFitLayout.class, XWTabFitLayout.class);
        xLayoutMap.put(WCardMainBorderLayout.class, XWCardMainBorderLayout.class);
    }

    private static void putDefault() {

        objectMap.put(TextEditor.class, XTextEditor.class);
        objectMap.put(TextArea.class, XTextArea.class);
        objectMap.put(NumberEditor.class, XNumberEditor.class);
        objectMap.put(FreeButton.class, XButton.class);
        objectMap.put(CheckBox.class, XCheckBox.class);
        objectMap.put(CheckBoxGroup.class, XCheckBoxGroup.class);
        objectMap.put(ComboBox.class, XComboBox.class);
        objectMap.put(ComboCheckBox.class, XComboCheckBox.class);
        objectMap.put(DateEditor.class, XDateEditor.class);
        objectMap.put(FileEditor.class, XFileUploader.class);
        objectMap.put(IframeEditor.class, XIframeEditor.class);
        objectMap.put(FormSubmitButton.class, XButton.class);
        objectMap.put(Button.class, XButton.class);
        objectMap.put(Label.class, XLabel.class);
        objectMap.put(MultiFileEditor.class, XMultiFileUploader.class);
        objectMap.put(Password.class, XPassword.class);
        objectMap.put(Radio.class, XRadio.class);
        objectMap.put(RadioGroup.class, XRadioGroup.class);
        objectMap.put(TreeEditor.class, XTreeEditor.class);
        objectMap.put(TreeComboBoxEditor.class, XTreeComboBoxEditor.class);
        objectMap.put(EditorHolder.class, XEditorHolder.class);
        objectMap.put(ElementCaseEditor.class, XElementCase.class);
        objectMap.put(NameWidget.class, XNameWidget.class);
        objectMap.put(CardSwitchButton.class, XCardSwitchButton.class);
        objectMap.put(CardAddButton.class, XCardAddButton.class);
        objectMap.put(WidgetErrorMarker.class, ErrorCreator.class);
    }

    private static void reInitExtra() {

        extraObjectMap.clear();
        extraObjectMap.putAll(ExtraDesignClassManager.getInstance().getParameterWidgetOptionsMap());
        extraObjectMap.putAll(ExtraDesignClassManager.getInstance().getFormWidgetOptionsMap());
    }

    private static void putExtraEditor() {
        if (DesignModuleFactory.getChartEditorClass() != null) {
            objectMap.put(DesignModuleFactory.getChartEditorClass(), XChartEditor.class);
        }
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends XCreator> searchXCreatorClass(Class<? extends Widget> clazz) {

        Class<? extends XCreator> xClazz = (Class<? extends XCreator>) objectMap.get(clazz);
        if (xClazz == null) {
            xClazz = (Class<? extends XCreator>) extraObjectMap.get(clazz);
        }
        if (xClazz == null) {
            xClazz = (Class<? extends XCreator>) xLayoutMap.get(clazz);
        }
        return xClazz;
    }

    /**
     * 创建creator
     *
     * @param widget 控件
     * @return 返回控件的creator
     */
    public static XCreator createXCreator(Widget widget) {
        return createXCreator(widget, new Dimension());
    }

    /**
     * 带初始大小的Widget转化为XCreator当然XCreator也需要把大小赋值上
     *
     * @param widget 控件
     * @param d      大小
     * @return 返回控件的xcreator
     */
    public static XCreator createXCreator(Widget widget, Dimension d) {
        Class<? extends Widget> widgetClass;
        Class<? extends XCreator> clazz;

        if (widget == null) {
            clazz = NullCreator.class;
        } else {
            widgetClass = widget.getClass();
            clazz = XCreatorUtils.searchXCreatorClass(widgetClass);
            if (clazz == null) {
                FineLoggerFactory.getLogger().error(widget + "'s" + " xcreator doesn't exsit!");
                clazz = NullCreator.class;
            }
        }
        XCreator creator = null;
        try {
            creator = Reflect.on(clazz).create(widget, d).get();
        } catch (Exception ignore) {

        }
        if (creator == null) {
            FineLoggerFactory.getLogger().error("Error to create xcreator!");
            creator = new NullCreator(widget, d);
        }
        creator.setXDescrption(widget);//设置描述信息
        return creator;
    }

    /**
     * 刷新所有名字控件
     *
     * @param container 布局容器
     */
    public static void refreshAllNameWidgets(XLayoutContainer container) {
        _refreshNameWidget(container);
        LayoutUtils.layoutRootContainer(container);
    }

    private static void _refreshNameWidget(XLayoutContainer container) {
        for (int i = 0, len = container.getXCreatorCount(); i < len; i++) {
            XCreator creator = container.getXCreator(i);
            if (creator instanceof XLayoutContainer) {
                _refreshNameWidget((XLayoutContainer) creator);
            } else if (creator instanceof XNameWidget) {
                ((XNameWidget) creator).rebuild();
            }
        }
    }

    /**
     * 获取焦点组件所在的顶层容器,不包括目标本身
     *
     * @param creator 组件
     * @return 返回父容器
     */
    public static XLayoutContainer getParentXLayoutContainer(XCreator creator) {
        Container c = creator.getParent();
        while (c != null) {
            XCreator crea = (XCreator) c;
            if (crea.acceptType(XWCardTitleLayout.class)) {
                return (XLayoutContainer) c.getParent();
            }
            if (crea.isDedicateContainer()) {
                return (XLayoutContainer) c.getParent();
            }
            if (c instanceof XLayoutContainer) {
                return (XLayoutContainer) c;
            }
            c = c.getParent();
        }

        return null;
    }

    /**
     * 获取焦点组件所在的顶层容器,可能是目标本身
     *
     * @param creator 组件
     * @return 返回顶层容器
     */
    public static XLayoutContainer getHotspotContainer(@NotNull XCreator creator) {
        if (creator.isDedicateContainer()) {
            return (XLayoutContainer) creator.getParent();
        }
        if (creator instanceof XLayoutContainer) {
            return (XLayoutContainer) creator;
        }
        return getParentXLayoutContainer(creator);
    }

    /**
     * 返回组件的图标
     *
     * @param creator 组件
     * @return 组件icon
     */
    public static Icon getCreatorIcon(XCreator creator) {
        String iconPath = creator.getIconPath();
        if (StringUtils.isEmpty(iconPath)) {
            return null;
        }
        return IOUtils.readIcon(iconPath);
    }
}