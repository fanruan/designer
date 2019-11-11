/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design;

import com.fr.base.BaseUtils;
import com.fr.common.annotations.Open;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.fun.CellWidgetOptionProvider;
import com.fr.design.fun.FormWidgetOptionProvider;
import com.fr.design.fun.MobileWidgetStyleProvider;
import com.fr.design.fun.ParameterWidgetOptionProvider;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.fun.ServerTableDataDefineProvider;
import com.fr.design.fun.TableDataDefineProvider;
import com.fr.design.fun.ToolbarItemProvider;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.design.menu.ShortCut;
import com.fr.design.widget.Appearance;
import com.fr.design.widget.mobile.WidgetMobilePane;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.AbstractExtraClassManager;
import com.fr.plugin.injectable.PluginModule;
import com.fr.plugin.injectable.PluginSingleInjection;
import com.fr.plugin.solution.closeable.CloseableContainedSet;
import com.fr.stable.Filter;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : richie
 * @since : 8.0
 * 用于设计器扩展的管理类
 */
@Open
public class ExtraDesignClassManager extends AbstractExtraClassManager implements ExtraDesignClassManagerProvider {

    private static ExtraDesignClassManager classManager = new ExtraDesignClassManager();

    private Set<ShortCut> shortCuts = new CloseableContainedSet<>(HashSet.class);

    public static ExtraDesignClassManager getInstance() {
        return classManager;
    }

    static {
        PluginModule.registerAgent(PluginModule.ExtraDesign, classManager);
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
        return getWebWidgetOptions(new Filter<ToolbarItemProvider>() {
            @Override
            public boolean accept(ToolbarItemProvider toolbarItemProvider) {
                return true;
            }
        });
    }

    public WidgetOption[] getWebWidgetOptions(Filter<ToolbarItemProvider> filter) {
        Set<ToolbarItemProvider> set = getArray(ToolbarItemProvider.XML_TAG);
        return getWebWidgetOptions(set, filter);
    }

    public WidgetOption[] getWebWidgetOptions(Set<ToolbarItemProvider> set, Filter<ToolbarItemProvider> filter) {
        return new DesignExtraBridge<WidgetOption, ToolbarItemProvider>() {

            @Override
            WidgetOption createT(ToolbarItemProvider provider) {
                return WidgetOptionFactory.createByWidgetClass(
                        provider.nameForWidget(),
                        IOUtils.readIcon(provider.iconPathForWidget()),
                        provider.classForWidget()
                );
            }

            @Override
            WidgetOption[] toArray(Collection<?> sCollection) {
                if (sCollection == null) {
                    return new WidgetOption[0];
                }
                return sCollection.toArray(new WidgetOption[sCollection.size()]);
            }
        }.filterSAndTransformT(set, filter);
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

    public Map<Class<? extends Widget>, Class<? extends WidgetMobilePane>> getCellWidgetMobileOptionsMap() {
        Set<CellWidgetOptionProvider> set = getArray(CellWidgetOptionProvider.XML_TAG);
        Map<Class<? extends Widget>, Class<? extends WidgetMobilePane>> map = new HashMap<>();
        for (CellWidgetOptionProvider provider : set) {
            map.put(provider.classForWidget(), provider.classForMobilePane());
        }
        return map;
    }

    public MobileWidgetStyleProvider[] getMobileStyleOfWidget(String xType) {
        Set<MobileWidgetStyleProvider> set = getArray(MobileWidgetStyleProvider.XML_TAG);
        if (set.isEmpty()) {
            return new MobileWidgetStyleProvider[0];
        }
        List<MobileWidgetStyleProvider> providers = new ArrayList<>();
        for (MobileWidgetStyleProvider provider : set) {
            if (ComparatorUtils.equalsIgnoreCase(provider.xTypeForWidget(), xType)) {
                providers.add(provider);
            }
        }
        return providers.toArray(new MobileWidgetStyleProvider[providers.size()]);
    }

    @Override
    protected boolean demountSpecific(PluginSingleInjection injection) {

        if (ShortCut.TEMPLATE_TREE.equals(injection.getName()) && injection.getObject() instanceof ShortCut) {
            shortCuts.remove(injection.getObject());
            return true;
        }
        return false;
    }

    @Override
    protected boolean mountSpecific(PluginSingleInjection injection) {

        if (ShortCut.TEMPLATE_TREE.equals(injection.getName()) && injection.getObject() instanceof ShortCut) {
            shortCuts.add((ShortCut) injection.getObject());
            return true;
        }
        return false;
    }

    public Set<ShortCut> getExtraShortCuts() {

        return Collections.unmodifiableSet(shortCuts);
    }

    public PreviewProvider[] getTemplatePreviews(Filter<PreviewProvider> filter) {
        Set<PreviewProvider> set = ExtraDesignClassManager.getInstance().getArray(PreviewProvider.MARK_STRING);
        return new DesignExtraBridge<PreviewProvider, PreviewProvider>() {

            @Override
            PreviewProvider createT(PreviewProvider previewProvider) {
                return previewProvider;
            }

            @Override
            PreviewProvider[] toArray(Collection<?> sCollection) {
                if (sCollection == null) {
                    return new PreviewProvider[0];
                }
                return sCollection.toArray(new PreviewProvider[sCollection.size()]);
            }
        }.filterSAndTransformT(set, filter);
    }

    /**
     * 抽了一个可能用到的公用逻辑出来：通过filter过滤接口实现（Set<s>）并将对外接口实例转成内部实例S转成T,比如ToolItemProvider转成WidgetOption,当然也可以不转
     *
     * @param <T> 你想要得到的类型，可以跟S相同
     * @param <S> 待转换的目标类型
     */
    abstract class DesignExtraBridge<T, S> {
        T[] filterSAndTransformT(Set<S> set, Filter<S> filter) {
            if (set == null || set.isEmpty()) {
                return toArray(set);
            }
            List<T> list = new ArrayList<>();
            for (S provider : set) {
                if (filter == null || filter.accept(provider)) {
                    list.add(createT(provider));
                }
            }

            return toArray(list);
        }

        /**
         * S转T
         * @param s
         * @return
         */
        abstract T createT(S s);

        abstract T[] toArray(Collection<?> sCollection);

    }
}