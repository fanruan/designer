/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design;

import com.fr.base.BaseUtils;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.fun.*;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.design.widget.Appearance;
import com.fr.form.ui.Widget;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.plugin.ExtraXMLFileManager;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;
import com.fr.stable.plugin.PluginSimplify;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.*;

/**
 * @author : richie
 * @since : 8.0
 * 用于设计器扩展的管理类
 */
public class ExtraDesignClassManager extends ExtraXMLFileManager implements ExtraDesignClassManagerProvider {

    private static final String XML_TAG = "ExtraDesignClassManager";

    private static ExtraDesignClassManager classManager;

    public synchronized static ExtraDesignClassManager getInstance() {
        if (classManager == null) {
            classManager = new ExtraDesignClassManager();
            classManager.readXMLFile();
        }

        return classManager;
    }

    static {
        GeneralContext.addEnvChangedListener(new EnvChangedListener() {
            public void envChanged() {
                ExtraDesignClassManager.envChanged();
            }
        });
    }


    private synchronized static void envChanged() {
        classManager = null;
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

    /**
     * 文件名
     *
     * @return 文件名
     */
    @Override
    public String fileName() {
        return "designer.xml";
    }

    /**
     * 读xml
     *
     * @param reader xml对象
     */
    public void readXML(XMLableReader reader) {
        readXML(reader, null, PluginSimplify.NULL);
    }


    /**
     * 写xml
     *
     * @param writer xml对象
     */
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(XML_TAG);
        writer.end();
    }
}