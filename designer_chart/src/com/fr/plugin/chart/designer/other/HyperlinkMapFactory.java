package com.fr.plugin.chart.designer.other;

import com.fr.base.Formula;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chart.web.ChartHyperRelateCellLink;
import com.fr.chart.web.ChartHyperRelateFloatLink;
import com.fr.design.chart.javascript.ChartEmailPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperPoplinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateCellLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.ChartHyperRelateFloatLinkPane;
import com.fr.design.chart.series.SeriesCondition.impl.FormHyperlinkPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.BooleanEditor;
import com.fr.design.editor.editor.ConstantsEditor;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.DoubleEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.design.hyperlink.ReportletHyperlinkPane;
import com.fr.design.hyperlink.WebHyperlinkPane;
import com.fr.design.javascript.JavaScriptImplPane;
import com.fr.design.javascript.ParameterJavaScriptPane;
import com.fr.general.Inter;
import com.fr.js.EmailJavaScript;
import com.fr.js.FormHyperlinkProvider;
import com.fr.js.JavaScriptImpl;
import com.fr.js.ParameterJavaScript;
import com.fr.js.ReportletHyperlink;
import com.fr.js.WebHyperlink;
import com.fr.plugin.chart.area.VanChartAreaPlot;
import com.fr.plugin.chart.bubble.BubblePlotType;
import com.fr.plugin.chart.bubble.VanChartBubblePlot;
import com.fr.plugin.chart.column.VanChartColumnPlot;
import com.fr.plugin.chart.custom.VanChartCustomPlot;
import com.fr.plugin.chart.custom.type.CustomStyle;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.funnel.VanChartFunnelPlot;
import com.fr.plugin.chart.gantt.VanChartGanttPlot;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.heatmap.VanChartHeatMapPlot;
import com.fr.plugin.chart.line.VanChartLinePlot;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.multilayer.VanChartMultiPiePlot;
import com.fr.plugin.chart.scatter.VanChartScatterPlot;
import com.fr.plugin.chart.structure.VanChartStructurePlot;
import com.fr.plugin.chart.treemap.VanChartTreeMapPlot;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.plugin.chart.type.MapType;
import com.fr.plugin.chart.wordcloud.VanChartWordCloudPlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 根据plot返回不同的超链的map
 */

public class HyperlinkMapFactory {
    //注意啦：下面超链面板那些类名如VAN_CHART_SCATTER要和SCATTER = "$VAN_CHART_SCATTER"完全一致。
    private static final String NORMAL = "$VAN_CHART";
    private static final String GAUGE = "$VAN_CHART_GAUGE";
    private static final String SCATTER = "$VAN_CHART_SCATTER";
    private static final String GANNT = "$VAN_CHART_GANNT";
    private static final String MULTIPIE = "$VAN_CHART_MULTIPIE";
    private static final String MAP = "$VAN_CHART_MAP";
    private static final String LINEMAP = "$VAN_CHART_LINE_MAP";
    private static final String DIRLLMAPCATALOG = "$VAN_CHART_DRILLMAPCATALOG";
    private static final String FUNNEL = "$VAN_CHART_FUNNEL";
    private static final String WORDCLOUD = "$VAN_CHART_WORDCLOUD";
    private static final String STRUCTURE = "$VAN_CHART_STRUCTURE";
    private static final String MULTICATEGORY = "$VAN_CHART_MULTICATEGORY";

    private static final String KEY_GAUGE_SLOT = VanChartGaugePlot.class.getName() + GaugeStyle.SLOT.ordinal();
    private static final String KEY_GAUGE_RING = VanChartGaugePlot.class.getName() + GaugeStyle.RING.ordinal();
    private static final String KEY_GAUGE_THERMOMETER = VanChartGaugePlot.class.getName() + GaugeStyle.THERMOMETER.ordinal();
    private static final String KEY_LINE_MAP = VanChartMapPlot.class.getName() + MapType.LINE.ordinal();

    private static final String KEY_CUSTOM_COLUMN_LINE = VanChartCustomPlot.class.getName() + CustomStyle.COMMON_COLUMN_AREA.ordinal();
    private static final String KEY_CUSTOM_COLUMN_AREA = VanChartCustomPlot.class.getName() + CustomStyle.COMMON_COLUMN_AREA.ordinal();
    private static final String KEY_CUSTOM_STACK_COLUMN_LINE = VanChartCustomPlot.class.getName() + CustomStyle.COMMON_COLUMN_AREA.ordinal();

    private static final String KEY_BUBBLE_FORCE = VanChartBubblePlot.class.getName() + BubblePlotType.FORCE.ordinal();

    private static HashMap<String, String> plotTypeMap = new HashMap<String, String>();

    static {
        plotTypeMap.put(KEY_GAUGE_SLOT, GAUGE);
        plotTypeMap.put(KEY_GAUGE_RING, GAUGE);
        plotTypeMap.put(KEY_GAUGE_THERMOMETER, GAUGE);
        plotTypeMap.put(VanChartScatterPlot.class.getName(), SCATTER);
        plotTypeMap.put(VanChartBubblePlot.class.getName(), SCATTER);
        plotTypeMap.put(VanChartMultiPiePlot.class.getName(), MULTIPIE);
        plotTypeMap.put(VanChartTreeMapPlot.class.getName(), MULTIPIE);
        plotTypeMap.put(KEY_BUBBLE_FORCE, NORMAL);
        plotTypeMap.put(VanChartMapPlot.class.getName(), MAP);
        plotTypeMap.put(KEY_LINE_MAP, LINEMAP);
        plotTypeMap.put(VanChartDrillMapPlot.class.getName(), MAP);
        plotTypeMap.put(VanChartFunnelPlot.class.getName(), FUNNEL);
        plotTypeMap.put(VanChartHeatMapPlot.class.getName(), MAP);
        plotTypeMap.put(VanChartWordCloudPlot.class.getName(), WORDCLOUD);
        plotTypeMap.put(VanChartGanttPlot.class.getName(), GANNT);
        plotTypeMap.put(VanChartStructurePlot.class.getName(), STRUCTURE);
        plotTypeMap.put(VanChartColumnPlot.class.getName(), MULTICATEGORY);
        plotTypeMap.put(VanChartLinePlot.class.getName(), MULTICATEGORY);
        plotTypeMap.put(VanChartAreaPlot.class.getName(), MULTICATEGORY);
        plotTypeMap.put(KEY_CUSTOM_COLUMN_LINE, MULTICATEGORY);
        plotTypeMap.put(KEY_CUSTOM_COLUMN_AREA, MULTICATEGORY);
        plotTypeMap.put(KEY_CUSTOM_STACK_COLUMN_LINE, MULTICATEGORY);
    }

    public static HashMap getHyperlinkMap(Plot plot) {
        String plotType = plotTypeMap.get(plot.getClass().getName() + plot.getDetailType());
        if (plotType == null) {
            plotType = plotTypeMap.get(plot.getClass().getName());
            if (plotType == null) {
                plotType = NORMAL;
            }
        }
        return getHyperlinkMapWithType(plotType);
    }

    public static HashMap getDrillUpLinkMap() {
        return getHyperlinkMapWithType(DIRLLMAPCATALOG);
    }

    public static HashMap getLineMapHyperLinkMap() {
        return getHyperlinkMapWithType(LINEMAP);
    }

    private static HashMap getHyperlinkMapWithType(String plotType) {
        HashMap<Class, Class> map = new HashMap<Class, Class>();

        map.put(ReportletHyperlink.class, getClassWithPrefix(HyperlinkMapFactory.Report.class, plotType));
        map.put(EmailJavaScript.class, VanChartEmailPane.class);
        map.put(WebHyperlink.class, getClassWithPrefix(HyperlinkMapFactory.Web.class, plotType));
        map.put(ParameterJavaScript.class, getClassWithPrefix(HyperlinkMapFactory.Para.class, plotType));

        map.put(JavaScriptImpl.class, getClassWithPrefix(HyperlinkMapFactory.Js.class, plotType));
        map.put(ChartHyperPoplink.class, getClassWithPrefix(HyperlinkMapFactory.Chart_Chart.class, plotType));
        map.put(ChartHyperRelateCellLink.class, getClassWithPrefix(HyperlinkMapFactory.Chart_Cell.class, plotType));
        map.put(ChartHyperRelateFloatLink.class, getClassWithPrefix(HyperlinkMapFactory.Chart_Float.class, plotType));

        map.put(FormHyperlinkProvider.class, getClassWithPrefix(HyperlinkMapFactory.Form.class, plotType));
        return map;
    }

    private static Class getClassWithPrefix(Class superClass, String plotType) {
        String wholeClassString = superClass.getName() + plotType;
        try {
            return Class.forName(wholeClassString);
        } catch (Exception e) {
            return superClass;
        }
    }

    private static Editor[] addBasicEditors(List<Editor> list) {
        list.add(new TextEditor());
        list.add(new IntegerEditor());
        list.add(new DoubleEditor());
        list.add(new DateEditor(true, Inter.getLocText("FR-Base_Sche_Day")));
        list.add(new BooleanEditor());

        FormulaEditor formulaEditor = new FormulaEditor(Inter.getLocText("Plugin-ChartF_Formula"));
        formulaEditor.setEnabled(true);
        list.add(formulaEditor);

        return list.toArray(new Editor[list.size()]);
    }

    private static List<Editor> getVanMultiPieEditor() {
        ConstantsEditor seriesName = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_MultiPie_Series_Name"), new Formula("SERIES"));
        seriesName.setEnabled(false);
        ConstantsEditor levelName = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Level_Name"), new Formula("NAME"));
        levelName.setEnabled(false);
        ConstantsEditor levelOrder = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Level_Order"), new Formula("LEVEL"));
        levelOrder.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Use_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(seriesName);
        lists.add(levelName);
        lists.add(levelOrder);
        lists.add(value);
        return lists;
    }

    private static List<Editor> getVanMultiCategoryEditor() {
        ConstantsEditor category = new ConstantsEditor(Inter.getLocText("Chart-Category_Name"), new Formula("CATEGORY"));
        category.setEnabled(false);
        ConstantsEditor categoryArray = new ConstantsEditor(Inter.getLocText("Plugin-Chart_Category_Array"), new Formula("CATEGORYARRAY"));
        categoryArray.setEnabled(false);
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Use_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(category);
        lists.add(categoryArray);
        lists.add(series);
        lists.add(value);
        return lists;
    }

    private static List<Editor> getVanScatterEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor size = new ConstantsEditor("SIZE", new Formula("SIZE"));
        size.setEnabled(false);
        ConstantsEditor x = new ConstantsEditor("X", new Formula("X"));
        x.setEnabled(false);
        ConstantsEditor y = new ConstantsEditor("Y", new Formula("Y"));
        y.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(x);
        lists.add(y);
        lists.add(size);
        return lists;
    }

    private static List<Editor> getVanGanntEditor() {
        ConstantsEditor project = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Project_Name"), new Formula("PROJECT"));
        project.setEnabled(false);
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor startTime = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Start_Time"), new Formula("START_TIME"));
        startTime.setEnabled(false);
        ConstantsEditor endTime = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_End_Time"), new Formula("END_TIME"));
        endTime.setEnabled(false);
        ConstantsEditor progress = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Process"), new Formula("PROGRESS"));
        progress.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(project);
        lists.add(series);
        lists.add(startTime);
        lists.add(endTime);
        lists.add(progress);

        return lists;
    }

    private static List<Editor> getVanMapEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor areaName = new ConstantsEditor(Inter.getLocText("FR-Chart-Area_Name"), new Formula("AREA_NAME"));
        areaName.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Use_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(areaName);
        lists.add(value);
        return lists;
    }

    private static List<Editor> getVanLineMapEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor startAreaName = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Start_Point"), new Formula("START_AREA_NAME"));
        startAreaName.setEnabled(false);
        ConstantsEditor endAreaName = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_End_Point"), new Formula("END_AREA_NAME"));
        endAreaName.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Use_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(startAreaName);
        lists.add(endAreaName);
        lists.add(value);
        return lists;
    }

    private static List<Editor> getDrillMapCatalogEditor() {
        ConstantsEditor areaName = new ConstantsEditor(Inter.getLocText("FR-Chart-Area_Name"), new Formula("AREA_NAME"));
        areaName.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(areaName);
        return lists;
    }

    private static List<Editor> getVanFunnelEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("ChartF-Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Use_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(value);
        return lists;
    }

    private static List<Editor> getVanWordCloudEditor() {
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_MultiPie_Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor name = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Word_Name"), new Formula("WORD_NAME"));
        name.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Word_Value"), new Formula("WORD_VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(series);
        lists.add(name);
        lists.add(value);
        return lists;
    }

    private static List<Editor> getVanStructureEditor() {
        ConstantsEditor name = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_Node_Name"), new Formula("NAME"));
        name.setEnabled(false);
        ConstantsEditor series = new ConstantsEditor(Inter.getLocText("Plugin-ChartF_MultiPie_Series_Name"), new Formula("SERIES"));
        series.setEnabled(false);
        ConstantsEditor value = new ConstantsEditor(Inter.getLocText("Chart-Use_Value"), new Formula("VALUE"));
        value.setEnabled(false);

        List<Editor> lists = new ArrayList<Editor>();
        lists.add(name);
        lists.add(series);
        lists.add(value);
        return lists;
    }

    private static ValueEditorPane getMultiCategoryEditorPane() {
        List<Editor> list = getVanMultiCategoryEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getScatterValueEditorPane() {
        List<Editor> list = getVanScatterEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getGanntValueEditorPane() {
        List<Editor> list = getVanGanntEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getMultiPieValueEditorPane() {
        List<Editor> list = getVanMultiPieEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getMapValueEditorPane() {
        List<Editor> list = getVanMapEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getLineMapValueEditorPane() {
        List<Editor> list = getVanLineMapEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getDrillMapCatalogValueEditorPane() {
        List<Editor> list = getDrillMapCatalogEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getFunnelValueEditorPane() {
        List<Editor> list = getVanFunnelEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getWordCloudValueEditorPane() {
        List<Editor> list = getVanWordCloudEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }

    private static ValueEditorPane getStructureValueEditorPane() {
        List<Editor> list = getVanStructureEditor();
        return ValueEditorPaneFactory.createValueEditorPane(addBasicEditors(list));
    }


    //网络报表
    public static class Report {
        public static class VAN_CHART extends ReportletHyperlinkPane.CHART {

            @Override
            protected boolean needRenamePane() {
                return false;
            }

        }

        public static class VAN_CHART_MULTICATEGORY extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiCategoryEditorPane();
            }
        }

        public static class VAN_CHART_SCATTER extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getScatterValueEditorPane();
            }
        }

        public static class VAN_CHART_GANNT extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getGanntValueEditorPane();
            }
        }

        public static class VAN_CHART_MULTIPIE extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiPieValueEditorPane();
            }
        }

        public static class VAN_CHART_GAUGE extends ReportletHyperlinkPane.CHART_METER {

            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MAP extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMapValueEditorPane();
            }
        }

        public static class VAN_CHART_LINE_MAP extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getLineMapValueEditorPane();
            }
        }

        public static class VAN_CHART_DRILLMAPCATALOG extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getDrillMapCatalogValueEditorPane();
            }
        }

        public static class VAN_CHART_FUNNEL extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getFunnelValueEditorPane();
            }
        }

        public static class VAN_CHART_WORDCLOUD extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getWordCloudValueEditorPane();
            }
        }

        public static class VAN_CHART_STRUCTURE extends Report.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getStructureValueEditorPane();
            }
        }
    }

    //邮箱
    public static class VanChartEmailPane extends ChartEmailPane {
        @Override
        protected boolean needRenamePane() {
            return false;
        }
    }

    //网页链接
    public static class Web {
        public static class VAN_CHART extends WebHyperlinkPane.CHART {

            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MULTICATEGORY extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiCategoryEditorPane();
            }
        }

        public static class VAN_CHART_SCATTER extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getScatterValueEditorPane();
            }
        }

        public static class VAN_CHART_GANNT extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getGanntValueEditorPane();
            }
        }

        public static class VAN_CHART_GAUGE extends WebHyperlinkPane.CHART_METER {

            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MAP extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMapValueEditorPane();
            }
        }

        public static class VAN_CHART_LINE_MAP extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getLineMapValueEditorPane();
            }
        }


        public static class VAN_CHART_DRILLMAPCATALOG extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getDrillMapCatalogValueEditorPane();
            }
        }

        public static class VAN_CHART_MULTIPIE extends Web.VAN_CHART {
            protected ValueEditorPane getValueEditorPane() {
                return getMultiPieValueEditorPane();
            }
        }

        public static class VAN_CHART_FUNNEL extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getFunnelValueEditorPane();
            }
        }

        public static class VAN_CHART_WORDCLOUD extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getWordCloudValueEditorPane();
            }
        }

        public static class VAN_CHART_STRUCTURE extends Web.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getStructureValueEditorPane();
            }
        }
    }

    //动态参数
    public static class Para {
        public static class VAN_CHART extends ParameterJavaScriptPane.CHART {

            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MULTICATEGORY extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiCategoryEditorPane();
            }
        }

        public static class VAN_CHART_SCATTER extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getScatterValueEditorPane();
            }
        }

        public static class VAN_CHART_GANNT extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getGanntValueEditorPane();
            }
        }

        public static class VAN_CHART_GAUGE extends ParameterJavaScriptPane.CHART_METER {

            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MAP extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMapValueEditorPane();
            }
        }

        public static class VAN_CHART_LINE_MAP extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getLineMapValueEditorPane();
            }
        }


        public static class VAN_CHART_DRILLMAPCATALOG extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getDrillMapCatalogValueEditorPane();
            }
        }

        public static class VAN_CHART_MULTIPIE extends Para.VAN_CHART {
            protected ValueEditorPane getValueEditorPane() {
                return getMultiPieValueEditorPane();
            }
        }

        public static class VAN_CHART_FUNNEL extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getFunnelValueEditorPane();
            }
        }

        public static class VAN_CHART_WORDCLOUD extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getWordCloudValueEditorPane();
            }
        }

        public static class VAN_CHART_STRUCTURE extends Para.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getStructureValueEditorPane();
            }
        }
    }

    //js超链
    public static class Js {
        public static class VAN_CHART extends JavaScriptImplPane.CHART {

            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MULTICATEGORY extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiCategoryEditorPane();
            }
        }

        public static class VAN_CHART_SCATTER extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getScatterValueEditorPane();
            }
        }

        public static class VAN_CHART_GANNT extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getGanntValueEditorPane();
            }
        }

        public static class VAN_CHART_GAUGE extends Js.VAN_CHART {

            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MAP extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMapValueEditorPane();
            }
        }

        public static class VAN_CHART_LINE_MAP extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getLineMapValueEditorPane();
            }
        }

        public static class VAN_CHART_DRILLMAPCATALOG extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getDrillMapCatalogValueEditorPane();
            }
        }

        public static class VAN_CHART_MULTIPIE extends Js.VAN_CHART {
            protected ValueEditorPane getValueEditorPane() {
                return getMultiPieValueEditorPane();
            }
        }

        public static class VAN_CHART_FUNNEL extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getFunnelValueEditorPane();
            }
        }

        public static class VAN_CHART_WORDCLOUD extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getWordCloudValueEditorPane();
            }
        }

        public static class VAN_CHART_STRUCTURE extends Js.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getStructureValueEditorPane();
            }
        }
    }

    //图表超链-悬浮窗图表
    public static class Chart_Chart {
        public static class VAN_CHART extends ChartHyperPoplinkPane {
            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MULTICATEGORY extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiCategoryEditorPane();
            }
        }

        public static class VAN_CHART_SCATTER extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getScatterValueEditorPane();
            }
        }

        public static class VAN_CHART_GANNT extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getGanntValueEditorPane();
            }
        }

        public static class VAN_CHART_GAUGE extends ChartHyperPoplinkPane.CHART_METER {
            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MAP extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMapValueEditorPane();
            }
        }

        public static class VAN_CHART_LINE_MAP extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getLineMapValueEditorPane();
            }
        }

        public static class VAN_CHART_DRILLMAPCATALOG extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getDrillMapCatalogValueEditorPane();
            }
        }

        public static class VAN_CHART_MULTIPIE extends Chart_Chart.VAN_CHART {
            protected ValueEditorPane getValueEditorPane() {
                return getMultiPieValueEditorPane();
            }
        }

        public static class VAN_CHART_FUNNEL extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getFunnelValueEditorPane();
            }
        }

        public static class VAN_CHART_WORDCLOUD extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getWordCloudValueEditorPane();
            }
        }

        public static class VAN_CHART_STRUCTURE extends Chart_Chart.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getStructureValueEditorPane();
            }
        }
    }

    //图表超链-联动单元格
    public static class Chart_Cell {
        public static class VAN_CHART extends ChartHyperRelateCellLinkPane {
            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MULTICATEGORY extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiCategoryEditorPane();
            }
        }

        public static class VAN_CHART_SCATTER extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getScatterValueEditorPane();
            }
        }

        public static class VAN_CHART_GANNT extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getGanntValueEditorPane();
            }
        }

        public static class VAN_CHART_GAUGE extends ChartHyperRelateCellLinkPane.CHART_METER {
            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MAP extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMapValueEditorPane();
            }
        }

        public static class VAN_CHART_LINE_MAP extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getLineMapValueEditorPane();
            }
        }

        public static class VAN_CHART_DRILLMAPCATALOG extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getDrillMapCatalogValueEditorPane();
            }
        }

        public static class VAN_CHART_MULTIPIE extends Chart_Cell.VAN_CHART {
            protected ValueEditorPane getValueEditorPane() {
                return getMultiPieValueEditorPane();
            }
        }

        public static class VAN_CHART_FUNNEL extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getFunnelValueEditorPane();
            }
        }

        public static class VAN_CHART_WORDCLOUD extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getWordCloudValueEditorPane();
            }
        }

        public static class VAN_CHART_STRUCTURE extends Chart_Cell.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getStructureValueEditorPane();
            }
        }
    }

    //图表超链-悬浮元素
    public static class Chart_Float {
        public static class VAN_CHART extends ChartHyperRelateFloatLinkPane {
            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MULTICATEGORY extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiCategoryEditorPane();
            }
        }

        public static class VAN_CHART_SCATTER extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getScatterValueEditorPane();
            }
        }

        public static class VAN_CHART_GANNT extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getGanntValueEditorPane();
            }
        }

        public static class VAN_CHART_GAUGE extends ChartHyperRelateFloatLinkPane.CHART_METER {
            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MAP extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMapValueEditorPane();
            }
        }

        public static class VAN_CHART_LINE_MAP extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getLineMapValueEditorPane();
            }
        }

        public static class VAN_CHART_DRILLMAPCATALOG extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getDrillMapCatalogValueEditorPane();
            }
        }

        public static class VAN_CHART_MULTIPIE extends Chart_Float.VAN_CHART {
            protected ValueEditorPane getValueEditorPane() {
                return getMultiPieValueEditorPane();
            }
        }

        public static class VAN_CHART_FUNNEL extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getFunnelValueEditorPane();
            }
        }

        public static class VAN_CHART_WORDCLOUD extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getWordCloudValueEditorPane();
            }
        }

        public static class VAN_CHART_STRUCTURE extends Chart_Float.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getStructureValueEditorPane();
            }
        }
    }

    //当前表单对象
    public static class Form {
        public static class VAN_CHART extends FormHyperlinkPane.CHART {
            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MULTICATEGORY extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMultiCategoryEditorPane();
            }
        }

        public static class VAN_CHART_SCATTER extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getScatterValueEditorPane();
            }
        }

        public static class VAN_CHART_GANNT extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getGanntValueEditorPane();
            }
        }

        public static class VAN_CHART_GAUGE extends FormHyperlinkPane.CHART_METER {
            @Override
            protected boolean needRenamePane() {
                return false;
            }
        }

        public static class VAN_CHART_MAP extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getMapValueEditorPane();
            }
        }

        public static class VAN_CHART_LINE_MAP extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getLineMapValueEditorPane();
            }
        }


        public static class VAN_CHART_DRILLMAPCATALOG extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getDrillMapCatalogValueEditorPane();
            }
        }

        public static class VAN_CHART_MULTIPIE extends Form.VAN_CHART {
            protected ValueEditorPane getValueEditorPane() {
                return getMultiPieValueEditorPane();
            }
        }

        public static class VAN_CHART_FUNNEL extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getFunnelValueEditorPane();
            }
        }

        public static class VAN_CHART_WORDCLOUD extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getWordCloudValueEditorPane();
            }
        }

        public static class VAN_CHART_STRUCTURE extends Form.VAN_CHART {
            @Override
            protected ValueEditorPane getValueEditorPane() {
                return getStructureValueEditorPane();
            }
        }
    }

}