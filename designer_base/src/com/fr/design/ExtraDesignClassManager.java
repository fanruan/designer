/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design;

import com.fr.base.BaseUtils;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.fun.*;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.design.menu.ShortCut;
import com.fr.design.widget.Appearance;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.AbstractExtraClassManager;
import com.fr.plugin.solution.container.CompatibleInjectionContainer;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.injectable.PluginSingleInjection;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;

import java.util.*;

/**
 * @author : richie
 * @since : 8.0
 * 用于设计器扩展的管理类
 */
public class ExtraDesignClassManager extends AbstractExtraClassManager implements ExtraDesignClassManagerProvider {
    
    private static ExtraDesignClassManager classManager = new ExtraDesignClassManager();
    
    private CompatibleInjectionContainer<ShortCut> shortCuts = new CompatibleInjectionContainer<>();
    
    public synchronized static ExtraDesignClassManager getInstance() {
        return classManager;
    }
    
    static {
        StableFactory.registerMarkedObject(PluginModule.ExtraDesign.getAgentName(), classManager);
    }
    
    public TableDataNameObjectCreator[] getReportTableDataCreators() {
        return getKindsOfTableDataCreators(TableDataDefineProvider.XML_TAG);
    }
    
    
    /**
     * 添加serverTDCreators
     *
     * @return 类名
     */
    public TableDataNameObjectCreator[] getServerTableDataCreators() {
        return getKindsOfTableDataCreators(ServerTableDataDefineProvider.XML_TAG);
    }
    
    private TableDataNameObjectCreator[] getKindsOfTableDataCreators(String tag) {
        Set<TableDataDefineProvider> set = getArray(tag);
        if (set.isEmpty()) {
            return new TableDataNameObjectCreator[0];
        }
        List<TableDataNameObjectCreator> creators = new ArrayList<>();
        for (TableDataDefineProvider provider : set) {
            TableDataNameObjectCreator creator = new TableDataNameObjectCreator(
                provider.nameForTableData(),
                provider.prefixForTableData(),
                provider.iconPathForTableData(),
                provider.classForTableData(),
                provider.classForInitTableData(),
                provider.appearanceForTableData()
            );
            creators.add(creator);
        }
        return creators.toArray(new TableDataNameObjectCreator[creators.size()]);
    }
    
    
    public Map<Class<? extends Widget>, Class<?>> getParameterWidgetOptionsMap() {
        Map<Class<? extends Widget>, Class<?>> map = new HashMap<>();
        Set<ParameterWidgetOptionProvider> set = getArray(ParameterWidgetOptionProvider.XML_TAG);
        for (ParameterWidgetOptionProvider provider : set) {
            map.put(provider.classForWidget(), provider.appearanceForWidget());
        }
        return map;
    }
    
    public WidgetOption[] getParameterWidgetOptions() {
        Set<ParameterWidgetOptionProvider> set = getArray(ParameterWidgetOptionProvider.XML_TAG);
        if (set.isEmpty()) {
            return new WidgetOption[0];
        }
        Set<WidgetOption> result = new HashSet<>();
        for (ParameterWidgetOptionProvider provider : set) {
            WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                provider.nameForWidget(),
                IOUtils.readIcon(provider.iconPathForWidget()),
                provider.classForWidget()
            );
            result.add(option);
        }
        return result.toArray(new WidgetOption[result.size()]);
    }
    
    
    public WidgetOption[] getWebWidgetOptions() {
        Set<ToolbarItemProvider> set = getArray(ToolbarItemProvider.XML_TAG);
        if (set.isEmpty()) {
            return new WidgetOption[0];
        }
        List<WidgetOption> list = new ArrayList<>();
        for (ToolbarItemProvider provider : set) {
            WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                provider.nameForWidget(),
                IOUtils.readIcon(provider.iconPathForWidget()),
                provider.classForWidget()
            );
            list.add(option);
        }
        return list.toArray(new WidgetOption[list.size()]);
    }
    
    
    
    public Map<Class<? extends Widget>, Class<?>> getFormWidgetOptionsMap() {
        Set<FormWidgetOptionProvider> set = getArray(FormWidgetOptionProvider.XML_TAG);
        Map<Class<? extends Widget>, Class<?>> map = new HashMap<>();
        for (FormWidgetOptionProvider provider : set) {
            map.put(provider.classForWidget(), provider.appearanceForWidget());
        }
        return map;
    }
    
    public WidgetOption[] getFormWidgetOptions() {
        return getFormUnits(false);
    }
    
    public WidgetOption[] getFormWidgetContainerOptions() {
        return getFormUnits(true);
    }
    
    private WidgetOption[] getFormUnits(boolean isContainer) {
        Set<FormWidgetOptionProvider> set = getArray(FormWidgetOptionProvider.XML_TAG);
        if (set.isEmpty()) {
            return new WidgetOption[0];
        }
        Set<WidgetOption> result = new HashSet<>();
        for (FormWidgetOptionProvider provider : set) {
            if (provider.isContainer() == isContainer) {
                WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                    provider.nameForWidget(),
                    BaseUtils.readIcon(provider.iconPathForWidget()),
                    provider.classForWidget()
                );
                result.add(option);
            }
        }
        return result.toArray(new WidgetOption[result.size()]);
    }
    
    
    
    public WidgetOption[] getCellWidgetOptions() {
        Set<CellWidgetOptionProvider> set = getArray(CellWidgetOptionProvider.XML_TAG);
        if (set.isEmpty()) {
            return new WidgetOption[0];
        }
        Set<WidgetOption> result = new HashSet<>();
        for (CellWidgetOptionProvider provider : set) {
            WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                provider.nameForWidget(),
                IOUtils.readIcon(provider.iconPathForWidget()),
                provider.classForWidget()
            );
            result.add(option);
        }
        return result.toArray(new WidgetOption[result.size()]);
        
    }
    
    public Map<Class<? extends Widget>, Appearance> getCellWidgetOptionsMap() {
        Set<CellWidgetOptionProvider> set = getArray(CellWidgetOptionProvider.XML_TAG);
        Map<Class<? extends Widget>, Appearance> map = new HashMap<>();
        for (CellWidgetOptionProvider provider : set) {
            map.put(provider.classForWidget(), new Appearance(provider.appearanceForWidget(), Appearance.P_MARK + map.size()));
        }
        return map;
    }
    
    
    public Feedback getFeedback() {
        try {
            Class clazz = GeneralUtils.classForName("com.fr.design.feedback.CurrentFeedback");
            if (clazz != null) {
                return (Feedback) clazz.newInstance();
            }
        } catch (Exception e) {
            FRLogger.getLogger().info("no feed back support");
        }
        return Feedback.EMPTY;
    }
    
    @Override
    protected boolean demountSpecific(PluginSingleInjection injection) {
        
        if (ShortCut.TEMPLATE_TREE.equals(injection.getName()) && injection.getOriginalObject() instanceof ShortCut) {
            shortCuts.remove(injection.getObject());
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean mountSpecific(PluginSingleInjection injection) {
        
        if (ShortCut.TEMPLATE_TREE.equals(injection.getName()) && injection.getOriginalObject() instanceof ShortCut) {
            shortCuts.put(injection.getObject(), (ShortCut) injection.getOriginalObject());
            return true;
        }
        return false;
    }
    
    public Set<ShortCut> getExtraShortCuts() {
        
        return shortCuts.getSet();
    }
}