/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.data.datapane.TableDataNameObjectCreator;
import com.fr.design.data.tabledata.wrapper.TableDataFactory;
import com.fr.design.fun.*;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.core.WidgetOptionFactory;
import com.fr.design.mainframe.App;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.menu.ShortCut;
import com.fr.design.widget.Appearance;
import com.fr.file.XMLFileManager;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
import com.fr.plugin.PluginCollector;
import com.fr.plugin.PluginInvalidLevelException;
import com.fr.plugin.PluginLicenseManager;
import com.fr.plugin.PluginMessage;
import com.fr.stable.EnvChangedListener;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.Authorize;
import com.fr.stable.fun.Level;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;
import com.fr.stable.plugin.PluginSimplify;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.util.ArrayList;
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
public class ExtraDesignClassManager extends XMLFileManager implements ExtraDesignClassManagerProvider {

    private static final String XML_TAG = "ExtraDesignClassManager";
    private static final String TEMPLATE_TREE_TAG = "TemplateTreeShortCut";

    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();

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

    private List<TableDataNameObjectCreator> reportTDCreators;
    private List<TableDataNameObjectCreator> serverTDCreators;

    private List<WidgetOption> parameterWidgetOptions;
    private Map<Class<? extends Widget>, Class<?>> parameterWidgetOptionsMap;
    private List<WidgetOption> webWidgetOptions;

    private List<WidgetOption> formWidgetOptions;
    private List<WidgetOption> formWidgetContainerOptions;
    private Map<Class<? extends Widget>, Class<?>> formWidgetOptionsMap;

    private List<WidgetOption> cellWidgetOptions;
    private Map<Class<? extends Widget>, Appearance> cellWidgetOptionMap;
    private List<NameObjectCreator> connectionCreators;
    private Set<PreviewProvider> previewProviders;

    private Set<HighlightProvider> highlightProviders;

    private TableDataCreatorProvider tableDataCreatorProvider;

    private List<MenuHandler> menuHandlers;

    private UIFormulaProcessor uiFormulaProcessor;

    private List<PresentKindProvider> presentKindProviders;

    private List<ExportToolBarProvider> exportToolBarProviders;

    private Set<ShortCut> templateTreeShortCutProviders;

    private List<SubmitProvider> submitProviders;

    private List<GlobalListenerProvider> globalListenerProviders;

    private List<JavaScriptActionProvider> javaScriptActionProviders;

    private TitlePlaceProcessor titlePlaceProcessor;

    private FormElementCaseEditorProcessor formElementCaseEditorProcessor;

    private IndentationUnitProcessor indentationUnitProcessor;

    private CellAttributeProvider cellAttributeProvider;

    private Set<HyperlinkProvider> hyperlinkGroupProviders;

    public void addSupportDesignApps(Level level, PluginSimplify simplify) throws Exception {
        validAPILevel(level, App.CURRENT_LEVEL, simplify.getPluginName());
        App provider = (App) level;
        DesignerFrame.registApp(provider);
    }

    private void validAPILevel(Level level, int targetLevel, String pluginName) {
        if (PluginCollector.getCollector().isError(level, targetLevel, pluginName)) {
            throw new PluginInvalidLevelException(pluginName, level.currentAPILevel());
        }
    }

    public HyperlinkProvider[] getHyperlinkProvider() {
        if (hyperlinkGroupProviders == null) {
            return new HyperlinkProvider[0];
        }
        return hyperlinkGroupProviders.toArray(new HyperlinkProvider[hyperlinkGroupProviders.size()]);
    }

    public void addHyperlinkProvider(Level level, PluginSimplify simplify) throws Exception {
        if (hyperlinkGroupProviders == null) {
            hyperlinkGroupProviders = new HashSet<HyperlinkProvider>();
        }
        validAPILevel(level, HyperlinkProvider.CURRENT_LEVEL, simplify.getPluginName());
        HyperlinkProvider provider = (HyperlinkProvider) level;
        hyperlinkGroupProviders.add(provider);
    }

    public GlobalListenerProvider[] getGlobalListenerProvider() {
        if (globalListenerProviders == null) {
            return new GlobalListenerProvider[0];
        }
        return globalListenerProviders.toArray(new GlobalListenerProvider[globalListenerProviders.size()]);
    }

    /**
     * 获取javaScriptPane
     *
     * @return javaScriptPane集合
     */
    public List<JavaScriptActionProvider> getJavaScriptActionProvider() {
        return javaScriptActionProviders;
    }

    /**
     * 添加一个javaScriptPane
     */
    public void addJavaScriptActionProvider(Level level, PluginSimplify simplify) throws Exception {
        if (javaScriptActionProviders == null) {
            javaScriptActionProviders = new ArrayList<JavaScriptActionProvider>();
        }
        validAPILevel(level, JavaScriptActionProvider.CURRENT_LEVEL, simplify.getPluginName());
        JavaScriptActionProvider provider = (JavaScriptActionProvider) level;
        if (!javaScriptActionProviders.contains(provider)) {
            javaScriptActionProviders.add(provider);
        }
    }

    /**
     * 添加全局监听
     */
    public void addGlobalListenerProvider(Level level, PluginSimplify simplify) throws Exception {
        if (globalListenerProviders == null) {
            globalListenerProviders = new ArrayList<GlobalListenerProvider>();
        }
        validAPILevel(level, GlobalListenerProvider.CURRENT_LEVEL, simplify.getPluginName());
        GlobalListenerProvider provider = (GlobalListenerProvider) level;
        if (!globalListenerProviders.contains(provider)) {
            globalListenerProviders.add(provider);
        }
    }

    public TableDataCreatorProvider getTableDataCreatorProvider() {
        return tableDataCreatorProvider;
    }

    public void setTableDataCreatorProvider(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                tableDataCreatorProvider = (TableDataCreatorProvider) clazz.newInstance();
            } catch (Exception e) {
                FRLogger.getLogger().error(e.getMessage(), e);
            }
        }
    }

    public SubmitProvider[] getSubmitProviders() {
        if (submitProviders == null) {
            return new SubmitProvider[0];
        }
        return submitProviders.toArray(new SubmitProvider[submitProviders.size()]);
    }

    /**
     * 添加提交接口
     */
    public void addSubmitProvider(Level level, PluginSimplify simplify) throws Exception {
        if (submitProviders == null) {
            submitProviders = new ArrayList<SubmitProvider>();
        }
        validAPILevel(level, SubmitProvider.CURRENT_LEVEL, simplify.getPluginName());
        SubmitProvider provider = (SubmitProvider) level;
        if (!submitProviders.contains(provider)) {
            submitProviders.add(provider);
        }
    }

    public TableDataNameObjectCreator[] getReportTableDataCreators() {
        if (reportTDCreators == null) {
            return new TableDataNameObjectCreator[0];
        } else {
            return reportTDCreators.toArray(new TableDataNameObjectCreator[reportTDCreators.size()]);
        }
    }


    /**
     * 添加reportTDCreators
     *
     * @param className 类名
     */
    public void addTableDataNameObjectCreator(String className, PluginSimplify simplify) {
        if (StringUtils.isNotBlank(className)) {
            try {
                if (reportTDCreators == null) {
                    reportTDCreators = new ArrayList<TableDataNameObjectCreator>();
                }
                TableDataNameObjectCreator creator = createTableDataNameObjectCreator(className, simplify);
                if (!reportTDCreators.contains(creator)) {
                    reportTDCreators.add(creator);
                }
            } catch (Exception e) {
                PluginMessage.remindUpdate(className + e.getMessage());
            }
        }
    }


    /**
     * 添加serverTDCreators
     *
     * @return 类名
     */
    public TableDataNameObjectCreator[] getServerTableDataCreators() {
        if (serverTDCreators == null) {
            return new TableDataNameObjectCreator[0];
        } else {
            return serverTDCreators.toArray(new TableDataNameObjectCreator[serverTDCreators.size()]);
        }
    }

    /**
     * 添加serverTDCreators
     *
     * @param className 类名
     */
    public void addServerTableDataNameObjectCreator(String className, PluginSimplify simplify) {
        if (StringUtils.isNotBlank(className)) {
            try {
                if (serverTDCreators == null) {
                    serverTDCreators = new ArrayList<TableDataNameObjectCreator>();
                }
                TableDataNameObjectCreator creator = createTableDataNameObjectCreator(className, simplify);
                if (!serverTDCreators.contains(creator)) {
                    serverTDCreators.add(creator);
                }
            } catch (Exception e) {
                PluginMessage.remindUpdate(className + e.getMessage());
            }
        }
    }

    private TableDataNameObjectCreator createTableDataNameObjectCreator(String className, PluginSimplify simplify) throws Exception {
        Class clazz = loader.loadClass(className);
        TableDataDefineProvider provider = (TableDataDefineProvider) clazz.newInstance();
        validAPILevel(provider, TableDataDefineProvider.CURRENT_LEVEL, simplify.getPluginName());
        TableDataNameObjectCreator creator = new TableDataNameObjectCreator(
                provider.nameForTableData(),
                provider.prefixForTableData(),
                provider.iconPathForTableData(),
                provider.classForTableData(),
                provider.classForInitTableData(),
                provider.appearanceForTableData()
        );
        TableDataFactory.register(provider.classForTableData(), creator);
        return creator;
    }

    public Map<Class<? extends Widget>, Class<?>> getParameterWidgetOptionsMap() {
        if (parameterWidgetOptionsMap == null) {
            return new HashMap<Class<? extends Widget>, Class<?>>();
        } else {
            return parameterWidgetOptionsMap;
        }
    }

    public WidgetOption[] getParameterWidgetOptions() {
        if (parameterWidgetOptions == null) {
            return new WidgetOption[0];
        } else {
            return parameterWidgetOptions.toArray(new WidgetOption[parameterWidgetOptions.size()]);
        }
    }

    /**
     * 添加parameterWidgetOptionsMap
     */
    public void addParameterWidgetOption(Level level, PluginSimplify simplify) throws Exception {
        if (parameterWidgetOptions == null) {
            parameterWidgetOptions = new ArrayList<WidgetOption>();
        }
        if (parameterWidgetOptionsMap == null) {
            parameterWidgetOptionsMap = new HashMap<Class<? extends Widget>, Class<?>>();
        }
        validAPILevel(level, ParameterWidgetOptionProvider.CURRENT_LEVEL, simplify.getPluginName());

        ParameterWidgetOptionProvider provider = (ParameterWidgetOptionProvider) level;
        WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                provider.nameForWidget(),
                BaseUtils.readIcon(provider.iconPathForWidget()),
                provider.classForWidget()
        );
        parameterWidgetOptionsMap.put(provider.classForWidget(), provider.appearanceForWidget());
        parameterWidgetOptions.add(option);
    }

    /**
     * 添加 webWidgetOptions
     *
     * @return 返回 webWidgetOptions
     */
    public void addWebWidgetOption(Level level, PluginSimplify simplify) throws Exception {
        if (webWidgetOptions == null) {
            webWidgetOptions = new ArrayList<WidgetOption>();
        }
        validAPILevel(level, ToolbarItemProvider.CURRENT_LEVEL, simplify.getPluginName());

        ToolbarItemProvider provider = (ToolbarItemProvider) level;
        WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                provider.nameForWidget(),
                BaseUtils.readIcon(provider.iconPathForWidget()),
                provider.classForWidget()
        );
        if (!webWidgetOptions.contains(option)) {
            webWidgetOptions.add(option);
        }
    }

    public Map<Class<? extends Widget>, Class<?>> getFormWidgetOptionsMap() {
        if (formWidgetOptionsMap == null) {
            return new HashMap<Class<? extends Widget>, Class<?>>();
        } else {
            return formWidgetOptionsMap;
        }
    }

    public WidgetOption[] getFormWidgetOptions() {
        if (formWidgetOptions == null) {
            return new WidgetOption[0];
        } else {
            return formWidgetOptions.toArray(new WidgetOption[formWidgetOptions.size()]);
        }
    }

    public WidgetOption[] getWebWidgetOptions() {
        if (webWidgetOptions == null) {
            return new WidgetOption[0];
        } else {
            return webWidgetOptions.toArray(new WidgetOption[webWidgetOptions.size()]);
        }
    }

    public WidgetOption[] getFormWidgetContainerOptions() {
        if (formWidgetContainerOptions == null) {
            return new WidgetOption[0];
        } else {
            return formWidgetContainerOptions.toArray(new WidgetOption[formWidgetContainerOptions.size()]);
        }
    }

    /**
     * 添加 formWidgetContainerOptions
     */
    public void addFormWidgetOption(Level level, PluginSimplify simplify) throws Exception {
        if (formWidgetOptions == null) {
            formWidgetOptions = new ArrayList<WidgetOption>();
        }
        if (formWidgetContainerOptions == null) {
            formWidgetContainerOptions = new ArrayList<WidgetOption>();
        }
        if (formWidgetOptionsMap == null) {
            formWidgetOptionsMap = new HashMap<Class<? extends Widget>, Class<?>>();
        }
        validAPILevel(level, FormWidgetOptionProvider.CURRENT_LEVEL, simplify.getPluginName());

        FormWidgetOptionProvider provider = (FormWidgetOptionProvider) level;
        WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                provider.nameForWidget(),
                BaseUtils.readIcon(provider.iconPathForWidget()),
                provider.classForWidget()
        );
        formWidgetOptionsMap.put(provider.classForWidget(), provider.appearanceForWidget());
        if (provider.isContainer()) {
            formWidgetContainerOptions.add(option);
        } else {
            formWidgetOptions.add(option);
        }
    }

    public Map<Class<? extends Widget>, Appearance> getCellWidgetOptionsMap() {
        if (cellWidgetOptionMap == null) {
            return new HashMap<Class<? extends Widget>, Appearance>();
        } else {
            return cellWidgetOptionMap;
        }
    }

    public WidgetOption[] getCellWidgetOptions() {
        if (cellWidgetOptions == null) {
            return new WidgetOption[0];
        } else {
            return cellWidgetOptions.toArray(new WidgetOption[cellWidgetOptions.size()]);
        }
    }

    /**
     * 添加cellWidgetOptionMap
     */
    public void addCellWidgetOption(Level level, PluginSimplify simplify) throws Exception {
        if (cellWidgetOptions == null) {
            cellWidgetOptions = new ArrayList<WidgetOption>();
        }
        if (cellWidgetOptionMap == null) {
            cellWidgetOptionMap = new HashMap<Class<? extends Widget>, Appearance>();
        }
        validAPILevel(level, CellWidgetOptionProvider.CURRENT_LEVEL, simplify.getPluginName());

        CellWidgetOptionProvider provider = (CellWidgetOptionProvider) level;
        WidgetOption option = WidgetOptionFactory.createByWidgetClass(
                provider.nameForWidget(),
                BaseUtils.readIcon(provider.iconPathForWidget()),
                provider.classForWidget()
        );
        if (cellWidgetOptions.contains(option)) {
            return;
        }
        cellWidgetOptionMap.put(provider.classForWidget(), new Appearance(provider.appearanceForWidget(), Appearance.P_MARK + cellWidgetOptionMap.size()));
        cellWidgetOptions.add(option);
    }


    /**
     * 添加 connectionCreators
     *
     * @param className 类名
     */
    public void addConnection(String className, PluginSimplify simplify) {
        if (StringUtils.isNotBlank(className)) {
            try {
                Class clazz = Class.forName(className);
                if (connectionCreators == null) {
                    connectionCreators = new ArrayList<NameObjectCreator>();
                }
                ConnectionProvider provider = (ConnectionProvider) clazz.newInstance();
                validAPILevel(provider, ConnectionProvider.CURRENT_LEVEL, simplify.getPluginName());
                NameObjectCreator creator = new NameObjectCreator(
                        provider.nameForConnection(),
                        provider.iconPathForConnection(),
                        provider.classForConnection(),
                        provider.appearanceForConnection()
                );
                if (!connectionCreators.contains(creator)) {
                    connectionCreators.add(creator);
                }
            } catch (Exception e) {
                PluginMessage.remindUpdate(className + e.getMessage());
            }
        }
    }

    public NameObjectCreator[] getConnections() {
        if (connectionCreators == null) {
            return new NameObjectCreator[0];
        } else {
            return connectionCreators.toArray(new NameObjectCreator[connectionCreators.size()]);
        }
    }

    public PreviewProvider[] getPreviewProviders() {
        if (previewProviders == null) {
            return new PreviewProvider[0];
        }
        return previewProviders.toArray(new PreviewProvider[previewProviders.size()]);
    }

    /**
     * 添加previewProviders
     */
    public void addPreviewProvider(Level level, PluginSimplify simplify) throws Exception {
        if (previewProviders == null) {
            previewProviders = new HashSet<PreviewProvider>();
        }
        validAPILevel(level, PreviewProvider.CURRENT_LEVEL, simplify.getPluginName());

        PreviewProvider provider = (PreviewProvider) level;
        if (!previewProviders.contains(provider)) {
            previewProviders.add(provider);
        }
    }

    public HighlightProvider[] getHighlightProviders() {
        if (highlightProviders == null) {
            return new HighlightProvider[0];
        }
        return highlightProviders.toArray(new HighlightProvider[highlightProviders.size()]);
    }

    /**
     * 添加 highlightProviders
     */
    public void addTemplateTreeShortCutProvider(Level level, PluginSimplify simplify) throws Exception {
        if (templateTreeShortCutProviders == null) {
            templateTreeShortCutProviders = new HashSet<ShortCut>();
        }
        validAPILevel(level, ShortCut.CURRENT_LEVEL, simplify.getPluginName());
        ShortCut provider = (ShortCut) level;
        templateTreeShortCutProviders.add(provider);
    }

    public ShortCut[] getTemplateTreeShortCutProviders() {
        if (templateTreeShortCutProviders == null) {
            return new ShortCut[0];
        }
        return templateTreeShortCutProviders.toArray(new ShortCut[templateTreeShortCutProviders.size()]);
    }

    /**
     * 添加 highlightProviders
     */
    public void addConditionProvider(Level level, PluginSimplify simplify) throws Exception {
        if (highlightProviders == null) {
            highlightProviders = new HashSet<HighlightProvider>();
        }
        validAPILevel(level, HighlightProvider.CURRENT_LEVEL, simplify.getPluginName());
        HighlightProvider provider = (HighlightProvider) level;
        highlightProviders.add(provider);
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

    public MenuHandler[] getMenuHandlers(String category) {
        if (menuHandlers == null) {
            return new MenuHandler[0];
        }
        List<MenuHandler> handlers = new ArrayList<MenuHandler>();
        for (MenuHandler handler : menuHandlers) {
            if (ComparatorUtils.equals(category, handler.category())) {
                handlers.add(handler);
            }
        }
        return handlers.toArray(new MenuHandler[handlers.size()]);
    }

    /**
     * 添加menuHandlers
     */
    public void addMenuHandler(Level level, PluginSimplify simplify) throws Exception {
        if (menuHandlers == null) {
            menuHandlers = new ArrayList<MenuHandler>();
        }
        validAPILevel(level, MenuHandler.CURRENT_LEVEL, simplify.getPluginName());
        MenuHandler handler = (MenuHandler) level;
        if (!menuHandlers.contains(handler)) {
            menuHandlers.add(handler);
        }
    }

    public UIFormulaProcessor getUIFormulaProcessor() {
        return uiFormulaProcessor;
    }

    public void setUIFormulaProcessor(Level level, PluginSimplify simplify) throws Exception {
        validAPILevel(level, UIFormulaProcessor.CURRENT_LEVEL, simplify.getPluginName());
        uiFormulaProcessor = (UIFormulaProcessor) level;
    }

    public PresentKindProvider[] getPresentKindProviders() {
        if (presentKindProviders == null) {
            return new PresentKindProvider[0];
        }
        return presentKindProviders.toArray(new PresentKindProvider[presentKindProviders.size()]);
    }

    /**
     * 添加presentKindProviders
     */
    public void addPresentKindProvider(Level level, PluginSimplify simplify) throws Exception {
        if (presentKindProviders == null) {
            presentKindProviders = new ArrayList<PresentKindProvider>();
        }
        validAPILevel(level, PresentKindProvider.CURRENT_LEVEL, simplify.getPluginName());
        PresentKindProvider provider = (PresentKindProvider) level;
        presentKindProviders.add(provider);
    }

    public ExportToolBarProvider[] getExportToolBarProviders() {
        if (exportToolBarProviders == null) {
            return new ExportToolBarProvider[0];
        }
        return exportToolBarProviders.toArray(new ExportToolBarProvider[exportToolBarProviders.size()]);
    }

    /**
     * 添加exportToolBarProviders
     */
    public void addExportToolBarProvider(Level level, PluginSimplify simplify) throws Exception {
        if (exportToolBarProviders == null) {
            exportToolBarProviders = new ArrayList<ExportToolBarProvider>();
        }
        validAPILevel(level, ExportToolBarProvider.CURRENT_LEVEL, simplify.getPluginName());
        ExportToolBarProvider provider = (ExportToolBarProvider) level;
        if (!exportToolBarProviders.contains(provider)) {
            exportToolBarProviders.add(provider);
        }
    }

    public TitlePlaceProcessor getTitlePlaceProcessor() {
        return titlePlaceProcessor;
    }

    public void setTitlePlaceProcessor(Level level, PluginSimplify simplify) throws Exception {
        validAPILevel(level, TitlePlaceProcessor.CURRENT_LEVEL, simplify.getPluginName());
        titlePlaceProcessor = (TitlePlaceProcessor) level;
    }

    public FormElementCaseEditorProcessor getPropertyTableEditor() {
        return formElementCaseEditorProcessor;
    }

    public void setPropertyTableEditor(Level level, PluginSimplify simplify) throws Exception {
        validAPILevel(level, FormElementCaseEditorProcessor.CURRENT_LEVEL, simplify.getPluginName());
        formElementCaseEditorProcessor = (FormElementCaseEditorProcessor) level;
    }

    public IndentationUnitProcessor getIndentationUnitEditor() {
        return indentationUnitProcessor;
    }

    public void setIndentationUnitEditor(Level level, PluginSimplify simplify) throws Exception {
        validAPILevel(level, IndentationUnitProcessor.CURRENT_LEVEL, simplify.getPluginName());
        indentationUnitProcessor = (IndentationUnitProcessor) level;
    }

    public CellAttributeProvider getCelllAttributeProvider() {
        return cellAttributeProvider;
    }

    public void setCellAttributeProvider(Level level, PluginSimplify simplify) throws Exception {
        validAPILevel(level, CellAttributeProvider.CURRENT_LEVEL, simplify.getPluginName());
        cellAttributeProvider = (CellAttributeProvider) level;
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
     * 读xml
     *
     * @param reader                   xml对象
     * @param extraDesignInterfaceList 接口列表
     */
    @Override
    public void readXML(XMLableReader reader, List<String> extraDesignInterfaceList, PluginSimplify simplify) {
        if (reader.isChildNode()) {
            String tagName = reader.getTagName();
            if (extraDesignInterfaceList != null) {
                extraDesignInterfaceList.add(tagName);
            }
            String className = reader.getAttrAsString("class", "");
            if (StringUtils.isEmpty(className)) {
                return;
            }
            readLevelTag(tagName, className, simplify);
        }
    }

    private void readLevelTag(String tagName, String className, PluginSimplify simplify) {
        try {
            //实现了Level接口的, 可以直接newInstance子类的
            Class<?> clazz = loader.loadClass(className);
            Authorize authorize = clazz.getAnnotation(Authorize.class);
            if (authorize != null) {
                PluginLicenseManager.getInstance().registerPaid(authorize, simplify);
            }

            Level impl = (Level) clazz.newInstance();
            //控件
            readWidgetRelated(tagName, impl, simplify);
            //数据集, 数据连接
            readTableDataRelated(tagName, className, simplify);
            if (tagName.equals(ParameterWidgetOptionProvider.XML_TAG)) {
                addParameterWidgetOption(impl, simplify);
            } else if (tagName.equals(PreviewProvider.MARK_STRING)) {
                addPreviewProvider(impl, simplify);
            } else if (tagName.equals(HighlightProvider.MARK_STRING)) {
                addConditionProvider(impl, simplify);
            } else if (tagName.equals(MenuHandler.MARK_STRING)) {
                addMenuHandler(impl, simplify);
            } else if (tagName.equals(UIFormulaProcessor.MARK_STRING)) {
                setUIFormulaProcessor(impl, simplify);
            } else if (tagName.equals(PresentKindProvider.MARK_STRING)) {
                addPresentKindProvider(impl, simplify);
            } else if (tagName.equals(TEMPLATE_TREE_TAG)) {
                addTemplateTreeShortCutProvider(impl, simplify);
            } else if (tagName.equals(SubmitProvider.MARK_STRING)) {
                addSubmitProvider(impl, simplify);
            } else if (tagName.equals(GlobalListenerProvider.XML_TAG)) {
                addGlobalListenerProvider(impl, simplify);
            } else if (tagName.equals(JavaScriptActionProvider.XML_TAG)) {
                addJavaScriptActionProvider(impl, simplify);
            } else if (tagName.equals(TitlePlaceProcessor.MARK_STRING)) {
                setTitlePlaceProcessor(impl, simplify);
            } else if (tagName.equals(FormElementCaseEditorProcessor.MARK_STRING)) {
                setPropertyTableEditor(impl, simplify);
            } else if (tagName.equals(IndentationUnitProcessor.MARK_STRING)) {
                setIndentationUnitEditor(impl, simplify);
            } else if (tagName.equals(CellAttributeProvider.MARK_STRING)) {
                setCellAttributeProvider(impl, simplify);
            } else if (tagName.equals(HyperlinkProvider.XML_TAG)) {
                addHyperlinkProvider(impl, simplify);
            } else if (tagName.equals(App.MARK_STRING)) {
                addSupportDesignApps(impl, simplify);
            }
        } catch (PluginInvalidLevelException e) {
            PluginMessage.remindUpdate(e.getMessage());
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    private void readTableDataRelated(String tagName, String className, PluginSimplify simplify) {
        if (tagName.equals(TableDataCreatorProvider.XML_TAG)) {
            setTableDataCreatorProvider(className);
        } else if (tagName.equals(TableDataDefineProvider.XML_TAG)) {
            addTableDataNameObjectCreator(className, simplify);
        } else if (tagName.equals(ServerTableDataDefineProvider.XML_TAG)) {
            addServerTableDataNameObjectCreator(className, simplify);
        } else if (tagName.equals(ConnectionProvider.XML_TAG)) {
            addConnection(className, simplify);
        }
    }

    private void readWidgetRelated(String tagName, Level impl, PluginSimplify simplify) throws Exception {
        if (tagName.equals(FormWidgetOptionProvider.XML_TAG)) {
            addFormWidgetOption(impl, simplify);
        } else if (tagName.equals(ToolbarItemProvider.XML_TAG)) {
            addWebWidgetOption(impl, simplify);
        } else if (tagName.equals(ExportToolBarProvider.XML_TAG)) {
            addExportToolBarProvider(impl, simplify);
        } else if (tagName.equals(CellWidgetOptionProvider.XML_TAG)) {
            addCellWidgetOption(impl, simplify);
        }
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