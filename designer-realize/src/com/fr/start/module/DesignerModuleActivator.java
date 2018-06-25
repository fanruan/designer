package com.fr.start.module;

import com.fr.base.BaseFormula;
import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.base.MultiFieldParameter;
import com.fr.base.Style;
import com.fr.base.TempNameStyle;
import com.fr.base.extension.FileExtension;
import com.fr.base.frpx.exception.FRPackageRunTimeException;
import com.fr.base.frpx.exception.InvalidWorkBookException;
import com.fr.base.io.XMLEncryptUtils;
import com.fr.base.process.ProcessOperator;
import com.fr.base.remote.RemoteDeziConstants;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.insert.cell.BiasCellAction;
import com.fr.design.actions.insert.cell.ChartCellAction;
import com.fr.design.actions.insert.cell.DSColumnCellAction;
import com.fr.design.actions.insert.cell.FormulaCellAction;
import com.fr.design.actions.insert.cell.GeneralCellAction;
import com.fr.design.actions.insert.cell.ImageCellAction;
import com.fr.design.actions.insert.cell.RichTextCellAction;
import com.fr.design.actions.insert.cell.SubReportCellAction;
import com.fr.design.actions.insert.flot.ChartFloatAction;
import com.fr.design.actions.insert.flot.FormulaFloatAction;
import com.fr.design.actions.insert.flot.ImageFloatAction;
import com.fr.design.actions.insert.flot.TextBoxFloatAction;
import com.fr.design.actions.server.StyleListAction;
import com.fr.design.bridge.DesignToolbarProvider;
import com.fr.design.chart.ChartDialog;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.form.parameter.FormParaDesigner;
import com.fr.design.fun.ElementUIProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.EmailPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.design.javascript.ProcessTransitionAdapter;
import com.fr.design.mainframe.App;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.ChartPropertyPane;
import com.fr.design.mainframe.DecodeDialog;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.ElementCaseThumbnail;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.mainframe.InformationCollector;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.WidgetToolBarPane;
import com.fr.design.mainframe.actions.NewFormAction;
import com.fr.design.mainframe.bbs.BBSGuestPane;
import com.fr.design.mainframe.form.FormECCompositeProvider;
import com.fr.design.mainframe.form.FormECDesignerProvider;
import com.fr.design.mainframe.form.FormElementCaseDesigner;
import com.fr.design.mainframe.form.FormReportComponentComposite;
import com.fr.design.mainframe.loghandler.DesignerLogImpl;
import com.fr.design.module.ChartHyperlinkGroup;
import com.fr.design.module.ChartPreStyleAction;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.parameter.FormParameterReader;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.parameter.WorkBookParameterReader;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.ui.btn.FormSubmitButtonDetailPane;
import com.fr.file.FILE;
import com.fr.form.stable.ElementCaseThumbnailProcessor;
import com.fr.form.ui.ChartEditor;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.general.ModuleContext;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.io.importer.Excel2007ReportImporter;
import com.fr.io.importer.ExcelReportImporter;
import com.fr.io.utils.ResourceIOUtils;
import com.fr.js.EmailJavaScript;
import com.fr.js.JavaScriptImpl;
import com.fr.js.ParameterJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.locale.InterMutableKey;
import com.fr.log.FineLoggerFactory;
import com.fr.main.impl.WorkBook;
import com.fr.main.impl.WorkBookAdapter;
import com.fr.main.impl.WorkBookX;
import com.fr.module.Activator;
import com.fr.module.extension.Prepare;
import com.fr.plugin.chart.vanchart.imgevent.design.DesignImageEvent;
import com.fr.quickeditor.cellquick.CellBiasTextPainterEditor;
import com.fr.quickeditor.cellquick.CellDSColumnEditor;
import com.fr.quickeditor.cellquick.CellFormulaQuickEditor;
import com.fr.quickeditor.cellquick.CellImageQuickEditor;
import com.fr.quickeditor.cellquick.CellRichTextEditor;
import com.fr.quickeditor.cellquick.CellStringQuickEditor;
import com.fr.quickeditor.cellquick.CellSubReportEditor;
import com.fr.quickeditor.chartquick.BasicChartQuickEditor;
import com.fr.quickeditor.chartquick.FloatChartQuickEditor;
import com.fr.quickeditor.floatquick.FloatImageQuickEditor;
import com.fr.quickeditor.floatquick.FloatStringQuickEditor;
import com.fr.report.cell.CellElementValueConverter;
import com.fr.report.cell.cellattr.core.RichText;
import com.fr.report.cell.cellattr.core.SubReport;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.report.cell.painter.BiasTextPainter;
import com.fr.report.cell.painter.CellImagePainter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.fun.LogProvider;
import com.fr.stable.plugin.ExtraChartDesignClassManagerProvider;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;
import com.fr.stable.script.CalculatorProviderContext;
import com.fr.stable.script.ValueConverter;
import com.fr.stable.web.ServletContext;
import com.fr.stable.web.ServletContextAdapter;
import com.fr.stable.xml.ObjectTokenizer;
import com.fr.stable.xml.ObjectXMLWriterFinder;
import com.fr.start.BBSGuestPaneProvider;
import com.fr.van.chart.DownloadOnlineSourcesHelper;
import com.fr.van.chart.map.server.ChartMapEditorAction;
import com.fr.xml.ReportXMLUtils;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.fr.stable.module.Module.ENGINE_MODULE;

/**
 * Created by juhaoyu on 2018/1/31.
 * 触发原来的DesignerModule的启动
 * 之后慢慢将DesignerModule拆成Activator
 */
public class DesignerModuleActivator extends Activator implements Prepare {

    static {
        ServletContext.addServletContextListener(new ServletContextAdapter() {

            @Override
            public void onServletStart() {
                designerModuleStart();
            }
        });
    }

    @Override
    public void start() {
        designerModuleStart();
    }

    private static void designerModuleStart() {

        if (com.fr.module.ModuleContext.getModule(DesignerModuleActivator.class).isRunning()) {
            return;
        }

        App<?>[] apps = apps4TemplateOpener();
        for (App<?> app : apps) {
            DesignerFrame.registApp(app);
        }

        StableFactory.registerMarkedClass(ExtraDesignClassManagerProvider.XML_TAG, ExtraDesignClassManager.class);
        ActionFactory.registerCellInsertActionClass(actionsForInsertCellElement());
        ActionFactory.registerFloatInsertActionClass(actionsForInsertFloatElement());
        DesignModuleFactory.registerCreators4Hyperlink(hyperlinkTypes());

        justStartModules4Engine();
        justStartModules4Designer();

        CalculatorProviderContext.setValueConverter(valueConverter());
        GeneralXMLTools.Object_Tokenizer = startXMLReadObjectTokenizer();
        GeneralXMLTools.Object_XML_Writer_Finder = startObjectXMLWriterFinder();
        addAdapterForPlate();

        designerRegister();

        InformationCollector.getInstance().collectStartTime();

        ExtraDesignClassManager.getInstance().getFeedback().didFeedback();
        StableFactory.registerMarkedObject(LogProvider.MARK_STRING, DesignerLogImpl.getInstance());

    }

    private static Class<?>[] actionsForInsertCellElement() {
        List<Class<?>> classes = new ArrayList<>();
        Set<ElementUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(ElementUIProvider.MARK_STRING);
        for (ElementUIProvider provider : providers) {
            classes.add(provider.actionForInsertCellElement());
        }

        return ArrayUtils.addAll(new Class<?>[]{
                DSColumnCellAction.class,
                GeneralCellAction.class,
                RichTextCellAction.class,
                FormulaCellAction.class,
                ChartCellAction.class,
                ImageCellAction.class,
                BiasCellAction.class,
                SubReportCellAction.class
        }, classes.toArray(new Class<?>[classes.size()]));
    }

    private static Class<?>[] actionsForInsertFloatElement() {
        List<Class<?>> classes = new ArrayList<>();
        Set<ElementUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(ElementUIProvider.MARK_STRING);
        for (ElementUIProvider provider : providers) {
            classes.add(provider.actionForInsertFloatElement());
        }
        return ArrayUtils.addAll(new Class<?>[]{
                TextBoxFloatAction.class,
                FormulaFloatAction.class,
                ChartFloatAction.class,
                ImageFloatAction.class
        }, classes.toArray(new Class<?>[classes.size()]));
    }

    private static NameableCreator[] hyperlinkTypes() {
        return new NameableCreator[]{
                new NameObjectCreator(Inter.getLocText("FR-Hyperlink_Reportlet"), ReportletHyperlink.class, ReportletHyperlinkPane.ChartNoRename.class),
                new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), EmailJavaScript.class, EmailPane.class),
                new NameObjectCreator(Inter.getLocText("Hyperlink-Web_link"), WebHyperlink.class, WebHyperlinkPane.ChartNoRename.class),
                new NameObjectCreator(Inter.getLocText("JavaScript-Dynamic_Parameters"), ParameterJavaScript.class, ParameterJavaScriptPane.ChartNoRename.class),
                new NameObjectCreator(Inter.getLocText("FR-Designer_JavaScript"), JavaScriptImpl.class, JavaScriptImplPane.ChartNoRename.class)
        };
    }

    /**
     * kunsnat: 一些模块信息 必须跟随设计器启动,
     * 比如 读取CC.XML, 设计器启动之后, 马上会读取XML, 需要Chart_Module中的注册信息
     */
    private static void justStartModules4Engine() {
        ModuleContext.startModule(ENGINE_MODULE);
    }

    private static void justStartModules4Designer() {
        chartDesignerRegister();
        formDesignerRegister();
    }

    /**
     * CellElementValueConverter用来处理设计器格子里的值，将公式/数组/其他元素转换成对应的值。
     *
     * @return 返回处理格子值的转换器
     */
    private static ValueConverter valueConverter() {
        return new CellElementValueConverter();
    }

    /*
     * 针对不同的对象，在读取Object对象的xml的时候需要使用不同的对象生成器
     * @return 返回对象生成器
     */
    private static ObjectTokenizer startXMLReadObjectTokenizer() {
        return new ReportXMLUtils.ReportObjectTokenizer();
    }

    /**
     * 针对不同的对象，在写对象的XML时需要使用不同的XML生成器
     *
     * @return 返回xml生成器
     */
    private static ObjectXMLWriterFinder startObjectXMLWriterFinder() {
        return new ReportXMLUtils.ReportObjectXMLWriterFinder();
    }


    //wei:fs的模块中可能有需要设计器界面做设置的地方，在这边添加
    private static void addAdapterForPlate() {

        ProcessTransitionAdapter.setProcessTransitionAdapter(new ProcessTransitionAdapter() {

            @Override
            protected String[] getTransitionNamesByBook(String book) {
                return StableFactory.getMarkedObject(ProcessOperator.MARK_STRING, ProcessOperator.class, ProcessOperator.EMPTY).getTransitionNamesByBook(book);
            }

            @Override
            protected String[] getParaNames(String book) {
                return StableFactory.getMarkedObject(ProcessOperator.MARK_STRING, ProcessOperator.class, ProcessOperator.EMPTY).getParaNames(book);
            }

            @Override
            protected ParameterProvider[] getParas(String book) {
                return StableFactory.getMarkedObject(ProcessOperator.MARK_STRING, ProcessOperator.class, ProcessOperator.EMPTY).getParas(book);
            }

            @Override
            protected MultiFieldParameter[] getAllMultiFieldParas(String book) {
                return StableFactory.getMarkedObject(ProcessOperator.MARK_STRING, ProcessOperator.class, ProcessOperator.EMPTY).getAllMultiFieldParas(book);
            }
        });
    }

    private static abstract class AbstractWorkBookApp implements App<WorkBook> {

        @Override
        public int currentAPILevel() {
            return CURRENT_LEVEL;
        }

        @Override
        public JTemplate<WorkBook, ?> openTemplate(FILE tplFile) {
            return new JWorkBook(asIOFile(tplFile), tplFile);
        }

        @Override
        public String mark4Provider() {
            return getClass().getName();
        }

        @Override
        public void process() {

        }

        @Override
        public void undo() {

        }
    }

    /*
 * 返回设计器能打开的模板类型的一个数组列表
 * @return 可以打开的模板类型的数组
 */
    private static App[] apps4TemplateOpener() {
        return new App[]{getCptxApp(), getCptApp(), getXlsApp(), getXlsxApp()};
    }

    private static AbstractWorkBookApp getXlsxApp() {
        return new AbstractWorkBookApp() {
            @Override
            public String[] defaultExtensions() {
                return new String[]{FileExtension.XLSX.getExtension()};
            }

            @Override
            public WorkBook asIOFile(FILE tplFile) {
                WorkBook workbook = null;
                try {
                    workbook = new Excel2007ReportImporter().generateWorkBookByStream(tplFile.asInputStream());
                } catch (Exception exp) {
                    FRContext.getLogger().error("Failed to generate xlsx from " + tplFile, exp);
                }
                return workbook;
            }
        };
    }

    private static AbstractWorkBookApp getXlsApp() {
        return new AbstractWorkBookApp() {
            @Override
            public String[] defaultExtensions() {
                return new String[]{FileExtension.XLS.getExtension()};
            }

            @Override
            public WorkBook asIOFile(FILE tplFile) {
                WorkBook workbook = null;
                try {
                    workbook = new ExcelReportImporter().generateWorkBookByStream(tplFile.asInputStream());
                } catch (Exception exp) {
                    FRContext.getLogger().error("Failed to generate xls from " + tplFile, exp);
                }
                return workbook;
            }
        };
    }

    private static AbstractWorkBookApp getCptApp() {
        return new AbstractWorkBookApp() {
            @Override
            public String[] defaultExtensions() {
                return new String[]{FileExtension.CPT.getExtension()};
            }

            @Override
            public WorkBook asIOFile(FILE file) {
                if (XMLEncryptUtils.isCptEncoded() &&
                        !XMLEncryptUtils.checkVaild(DesignerEnvManager.getEnvManager().getEncryptionKey())) {
                    if (!new DecodeDialog(file).isPwdRight()) {
                        FRContext.getLogger().error(Inter.getLocText("ECP-error_pwd"));
                        return new WorkBook();
                    }
                }

                WorkBook tpl = new WorkBook();
                // richer:打开报表通知
                FRContext.getLogger().info(Inter.getLocText(new String[]{"LOG-Is_Being_Openned", "LOG-Please_Wait"}, new String[]{"\"" + file.getName() + "\"" + ",", "..."}));
                TempNameStyle namestyle = TempNameStyle.getInstance();
                namestyle.clear();
                String checkStr = StringUtils.EMPTY;
                try {
                    checkStr = ResourceIOUtils.inputStream2String(file.asInputStream());
                    tpl.readStream(file.asInputStream());
                } catch (Exception exp) {
                    String errorMessage = StringUtils.EMPTY;
                    errorMessage = ComparatorUtils.equals(RemoteDeziConstants.INVALID_USER, checkStr) ? Inter.getLocText("FR-Designer_No-Privilege")
                            : Inter.getLocText("NS-exception_readError");
                    FRContext.getLogger().error(errorMessage + file, exp);
                }
                checkNameStyle(namestyle);
                return tpl;
            }
        };
    }

    private static AbstractWorkBookApp getCptxApp() {
        return new AbstractWorkBookApp() {

            @Override
            public String[] defaultExtensions() {
                return new String[]{FileExtension.CPTX.getExtension()};
            }

            @Override
            public WorkBook asIOFile(FILE file) {
                FRContext.getLogger().info(Inter.getLocText(new String[]{"LOG-Is_Being_Openned", "LOG-Please_Wait"}, new String[]{"\"" + file.getName() + "\"" + ",", "..."}));
                WorkBookX tpl;
                InputStream inputStream;
                try {
                    inputStream = file.asInputStream();
                    long time = System.currentTimeMillis();
                    tpl = new WorkBookX(inputStream);
                    FRContext.getLogger().error("cost: " + (System.currentTimeMillis() - time) + " ms");
                } catch (Exception exp) {
                    if (exp instanceof FRPackageRunTimeException) {
                        throw (FRPackageRunTimeException) exp;
                    }
                    throw new InvalidWorkBookException(file + ":" + exp.getMessage(), exp);
                }


                return new WorkBookAdapter(tpl);
            }
        };
    }


    private static void checkNameStyle(TempNameStyle namestyle) {
        Iterator it = namestyle.getIterator();
        ArrayList<String> al = new ArrayList<String>();
        while (it.hasNext()) {
            al.add((String) it.next());
        }
        if (!al.isEmpty()) {
            showConfirmDialog(al);
        }
    }

    private static void showConfirmDialog(final ArrayList<String> namelist) {

        final JDialog jd = new JDialog();
        // 模态一下，因为可能会多个样式丢失
        // jd.setModal(true);
        jd.setAlwaysOnTop(true);
        jd.setSize(450, 150);
        jd.setResizable(false);
        jd.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
        String message = namelist.toString().replaceAll("\\[", "").replaceAll("\\]", "");
        UILabel jl = new UILabel(Inter.getLocText(new String[]{"Current_custom_global", "Has_been_gone"}, new String[]{message}));
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        jd.add(jl, BorderLayout.CENTER);
        JPanel jp = new JPanel();

        // ”是“按钮，点击之后将生成一个全局样式，并写入xml
        UIButton confirmButton = new UIButton(Inter.getLocText("FR-Designer_Yes"));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    for (int i = 0; i < namelist.size(); i++) {
                        ServerPreferenceConfig.getInstance().putStyle(namelist.get(i), Style.DEFAULT_STYLE);
                    }
                } catch (Exception ex) {
                    FineLoggerFactory.getLogger().error(ex.getMessage());
                }
                jd.dispose();
                new StyleListAction().actionPerformed(e);// 弹窗
            }
        });

        UIButton noButton = new UIButton(Inter.getLocText("FR-Designer_No"));
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jd.dispose();
            }
        });

        jp.add(confirmButton);
        jp.add(noButton);
        jd.setTitle(Inter.getLocText("FR-Custom_styles_lost"));
        jd.add(jp, BorderLayout.SOUTH);
        GUICoreUtils.centerWindow(jd);
        jd.setVisible(true);
    }

    private static void designerRegister() {
        registerCellEditor();
        registerFloatEditor();
        registerData4Form();
        registerOtherPane();
    }

    private static void registerOtherPane() {
        StableFactory.registerMarkedClass(BBSGuestPaneProvider.XML_TAG, BBSGuestPane.class);
    }

    /**
     * kunsnat:注册单元格选中Editor
     */
    private static void registerCellEditor() {

        ActionFactory.registerCellEditor(String.class, new CellStringQuickEditor());
        ActionFactory.registerCellEditor(Number.class, new CellStringQuickEditor());
        ActionFactory.registerCellEditor(BaseFormula.class, new CellFormulaQuickEditor());
        ActionFactory.registerCellEditor(SubReport.class, new CellSubReportEditor());
        ActionFactory.registerCellEditor(RichText.class, new CellRichTextEditor());
        ActionFactory.registerCellEditor(DSColumn.class, new CellDSColumnEditor());
        ActionFactory.registerCellEditor(Image.class, new CellImageQuickEditor());
        ActionFactory.registerCellEditor(BiasTextPainter.class, new CellBiasTextPainterEditor());
        ActionFactory.registerCellEditor(BufferedImage.class, new CellImageQuickEditor());
        ActionFactory.registerCellEditor(CellImagePainter.class, new CellImageQuickEditor());
        //todo 图表编辑器populate没能实现刷新面板显示
        ActionFactory.registerCellEditorClass(ChartCollection.class, BasicChartQuickEditor.class);

        Set<ElementUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(ElementUIProvider.MARK_STRING);
        for (ElementUIProvider provider : providers) {
            try {
                ActionFactory.registerCellEditor(provider.targetObjectClass(), provider.quickEditor().newInstance());
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }


    /**
     * kunnat: 注册悬浮选中Editor
     */
    private static void registerFloatEditor() {

        ActionFactory.registerFloatEditor(String.class, new FloatStringQuickEditor());
        ActionFactory.registerFloatEditor(Formula.class, new FloatStringQuickEditor());
        ActionFactory.registerFloatEditor(Image.class, new FloatImageQuickEditor());
        ActionFactory.registerFloatEditor(BufferedImage.class, new FloatImageQuickEditor());
        ActionFactory.registerFloatEditor(CellImagePainter.class, new FloatImageQuickEditor());
        //todo 图表编辑器populate没能实现刷新面板显示
        ActionFactory.registerFloatEditorClass(ChartCollection.class, FloatChartQuickEditor.class);
    }


    private static void registerData4Form() {
        StableFactory.registerMarkedClass(FormECDesignerProvider.XML_TAG, FormElementCaseDesigner.class);
        StableFactory.registerMarkedClass(FormECCompositeProvider.XML_TAG, FormReportComponentComposite.class);
        DesignModuleFactory.registerParameterReader(new WorkBookParameterReader());
    }

    private static void chartDesignerRegister() {

        StableFactory.registerMarkedClass(ExtraChartDesignClassManagerProvider.XML_TAG, ChartTypeInterfaceManager.class);
        StableFactory.getStaticMarkedInstanceObjectFromClass(ExtraChartDesignClassManagerProvider.XML_TAG, ExtraChartDesignClassManagerProvider.class);

        DesignModuleFactory.registerHyperlinkGroupType(new ChartHyperlinkGroup());

        DesignModuleFactory.registerChartEditorClass(ChartEditor.class);
        DesignModuleFactory.registerChartComponentClass(ChartComponent.class);

        DesignModuleFactory.registerChartDialogClass(ChartDialog.class);

        DesignModuleFactory.registerChartPropertyPaneClass(ChartPropertyPane.class);

        ActionFactory.registerChartPreStyleAction(new ChartPreStyleAction());
        ActionFactory.registerChartMapEditorAction(new ChartMapEditorAction());

        ActionFactory.registerChartCollection(ChartCollection.class);

        DesignModuleFactory.registerExtraWidgetOptions(ChartTypeInterfaceManager.initWidgetOption());

        DesignImageEvent.registerDefaultCallbackEvent(HistoryTemplateListPane.getInstance());
        DesignImageEvent.registerDownloadSourcesEvent(new DownloadOnlineSourcesHelper());
    }

    private static void formDesignerRegister() {

        StableFactory.registerMarkedObject(DesignToolbarProvider.STRING_MARKED, WidgetToolBarPane.getInstance());

        DesignModuleFactory.registerNewFormActionClass(NewFormAction.class);
        DesignModuleFactory.registerFormParaDesignerClass(FormParaDesigner.class);
        DesignModuleFactory.registerParaPropertyPaneClass(ParameterPropertyPane.class);
        DesignModuleFactory.registerFormHierarchyPaneClass(FormHierarchyTreePane.class);
        DesignModuleFactory.registerWidgetPropertyPaneClass(WidgetPropertyPane.class);
        DesignModuleFactory.registerButtonDetailPaneClass(FormSubmitButtonDetailPane.class);
        DesignModuleFactory.registerParameterReader(new FormParameterReader());

        StableFactory.registerMarkedClass(BaseJForm.XML_TAG, JForm.class);

        StableFactory.registerMarkedObject(ElementCaseThumbnailProcessor.MARK_STRING, new ElementCaseThumbnail());
    }

    @Override
    public void stop() {
    }

    @Override
    public void prepare() {

        addMutable(InterMutableKey.Path, "com/fr/design/i18n/main", "com/fr/design/i18n/chart");
    }
}
