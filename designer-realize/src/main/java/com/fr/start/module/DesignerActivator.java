package com.fr.start.module;

import com.fr.base.BaseFormula;
import com.fr.base.Formula;
import com.fr.base.MultiFieldParameter;
import com.fr.base.passport.FinePassportListenerAdapter;
import com.fr.base.passport.FinePassportManager;
import com.fr.base.process.ProcessOperator;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.NewFormAction;
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
import com.fr.design.bridge.DesignToolbarProvider;
import com.fr.design.constants.DesignerLaunchStatus;
import com.fr.design.form.parameter.FormParaDesigner;
import com.fr.design.fun.ElementUIProvider;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.i18n.DesignI18nImpl;
import com.fr.design.javascript.EmailPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.design.javascript.ProcessTransitionAdapter;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.ElementCaseThumbnail;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.mainframe.InformationCollector;
import com.fr.design.mainframe.JForm;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.WidgetToolBarPane;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.bbs.BBSGuestPane;
import com.fr.design.mainframe.bbs.UserInfoPane;
import com.fr.design.mainframe.form.FormECCompositeProvider;
import com.fr.design.mainframe.form.FormECDesignerProvider;
import com.fr.design.mainframe.form.FormElementCaseDesigner;
import com.fr.design.mainframe.form.FormReportComponentComposite;
import com.fr.design.mainframe.loghandler.DesignerLogAppender;
import com.fr.design.mainframe.socketio.DesignerSocketIO;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.os.impl.SupportOSImpl;
import com.fr.design.parameter.FormParameterReader;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.parameter.WorkBookParameterReader;
import com.fr.design.widget.ui.btn.FormSubmitButtonDetailPane;
import com.fr.form.stable.ElementCaseThumbnailProcessor;
import com.fr.general.xml.GeneralXMLTools;
import com.fr.js.EmailJavaScript;
import com.fr.js.JavaScriptImpl;
import com.fr.js.ParameterJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.locale.InterMutableKey;
import com.fr.locale.LocaleMarker;
import com.fr.locale.LocaleScope;
import com.fr.log.FineLoggerFactory;
import com.fr.log.LogHandler;
import com.fr.module.Activator;
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
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.os.support.OSBasedAction;
import com.fr.stable.os.support.OSSupportCenter;
import com.fr.stable.plugin.ExtraDesignClassManagerProvider;
import com.fr.stable.script.CalculatorProviderContext;
import com.fr.stable.script.ValueConverter;
import com.fr.stable.xml.ObjectTokenizer;
import com.fr.stable.xml.ObjectXMLWriterFinder;
import com.fr.start.BBSGuestPaneProvider;
import com.fr.xml.ReportXMLUtils;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by juhaoyu on 2018/1/31.
 * 触发原来的DesignerModule的启动
 * 之后慢慢将DesignerModule拆成Activator
 */
public class DesignerActivator extends Activator {

    private LogHandler<DesignerLogAppender> logHandler = null;

    @Override
    public void start() {
        List<LocaleMarker> markers = findMutable(InterMutableKey.Path);
        for (LocaleMarker marker : markers) {
            if (marker.match(LocaleScope.DESIGN)) {
                DesignI18nImpl.getInstance().addResource(marker.getPath());
            }
        }
        designerModuleStart();
        loadLogAppender();
        DesignerSocketIO.update();
        OSSupportCenter.buildAction(new OSBasedAction() {
            @Override
            public void execute() {
                UserInfoPane.getInstance().updateBBSUserInfo();
            }
        }, SupportOSImpl.USERINFOPANE);

        storePassport();
        AlphaFineHelper.switchConfig4Locale();
    }

    @Override
    public void afterAllStart() {
        DesignerLaunchStatus.setStatus(DesignerLaunchStatus.DESIGNER_INIT_COMPLETE);
    }

    private void loadLogAppender() {
        logHandler = new LogHandler<DesignerLogAppender>() {
            final DesignerLogAppender logAppender = new DesignerLogAppender();

            @Override
            public DesignerLogAppender getHandler() {
                return logAppender;
            }
        };
        FineLoggerFactory.getLogger().addLogAppender(logHandler);
    }

    private void unloadLogAppender() {
        if (logHandler != null) {
            FineLoggerFactory.getLogger().removeLogAppender(logHandler);
        }
    }

    private void designerModuleStart() {

        StableFactory.registerMarkedClass(ExtraDesignClassManagerProvider.XML_TAG, ExtraDesignClassManager.class);
        ActionFactory.registerCellInsertActionClass(actionsForInsertCellElement());
        ActionFactory.registerFloatInsertActionClass(actionsForInsertFloatElement());
        DesignModuleFactory.registerCreators4Hyperlink(hyperlinkTypes());

        justStartModules4Designer();

        CalculatorProviderContext.setValueConverter(valueConverter());
        GeneralXMLTools.Object_Tokenizer = startXMLReadObjectTokenizer();
        GeneralXMLTools.Object_XML_Writer_Finder = startObjectXMLWriterFinder();
        addAdapterForPlate();

        designerRegister();

        InformationCollector.getInstance().collectStartTime();
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
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Reportlet"), ReportletHyperlink.class, ReportletHyperlinkPane.ChartNoRename.class),
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Email"), EmailJavaScript.class, EmailPane.class),
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Web_Link"), WebHyperlink.class, WebHyperlinkPane.ChartNoRename.class),
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_JavaScript_Dynamic_Parameters"), ParameterJavaScript.class, ParameterJavaScriptPane.ChartNoRename.class),
                new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_JavaScript"), JavaScriptImpl.class, JavaScriptImplPane.ChartNoRename.class)
        };
    }


    private static void justStartModules4Designer() {
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

    private static void storePassport() {
        FinePassportManager.getInstance().storePassport(MarketConfig.getInstance().getBbsUsername(), DesignerEnvManager.getEnvManager().getActivationKey());
        FinePassportManager.getInstance().addPassportListener(new FinePassportListenerAdapter() {
            @Override
            public void onLoginSuccess() {
                FinePassportManager.getInstance().storePassport(MarketConfig.getInstance().getBbsUsername(), DesignerEnvManager.getEnvManager().getActivationKey());
            }
        });
    }

    @Override
    public void stop() {
        unloadLogAppender();
        DesignerSocketIO.close();
    }
}
