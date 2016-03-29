package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.*;
import com.fr.chart.chartglyph.CustomAttr;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by eason on 15/2/11.
 */
public class CustomDefaultSeriesPane extends BasicPane{

    private static final String BAR = Inter.getLocText("ChartF-Column");
    private static final String BAR_STACK = Inter.getLocText("I-BarStyle_NormalStack");
    private static final String BAR3D = Inter.getLocText("FR-Chart-Bar3D_Chart");
    private static final String BAR3D_STACK = Inter.getLocText("FR-Chart-Bar3DStack_Chart");
    private static final String LINE = Inter.getLocText("I-LineStyle_Line");
    private static final String AREA_STACK = Inter.getLocText("I-AreaStyle_Stack");

    private UIComboBoxPane boxPane;

    CustomBarDefaultSeriesPane barSeriesPane = new CustomBarDefaultSeriesPane(BAR);
    CustomBarDefaultSeriesPane barStackSeriesPane = new CustomBarDefaultSeriesPane(BAR_STACK);

    CustomBar3DDefaultSeriesPane bar3DSeriesPane = new CustomBar3DDefaultSeriesPane(BAR3D);
    CustomBar3DDefaultSeriesPane bar3DStackSeriesPane = new CustomBar3DDefaultSeriesPane(BAR3D_STACK);

    CustomLineDefaultSeriesPane lineSeriesPane = new CustomLineDefaultSeriesPane();
    CustomAreaDefaultSeriesPane areaSeriesPane = new CustomAreaDefaultSeriesPane();

    private static final HashMap<ChartCustomRendererType, Integer> COM_MAP = new HashMap<ChartCustomRendererType, Integer>(){
        {
            put(ChartCustomRendererType.BAR_RENDERER, 0);
            put(ChartCustomRendererType.BAR_STACK, 1);
            put(ChartCustomRendererType.BAR3D,2);
            put(ChartCustomRendererType.BAR3D_STACK,3);
            put(ChartCustomRendererType.LINE_RENDERER,4);
            put(ChartCustomRendererType.AREA_STACK, 5);
        }
    };

    public CustomDefaultSeriesPane(){
        this.setLayout(new BorderLayout());

        boxPane = new UIComboBoxPane() {
            protected List<FurtherBasicBeanPane> initPaneList() {
                List list = new ArrayList<FurtherBasicBeanPane>();

                list.add(barSeriesPane);
                list.add(barStackSeriesPane);

                list.add(bar3DSeriesPane);
                list.add(bar3DStackSeriesPane);

                list.add(lineSeriesPane);
                list.add(areaSeriesPane);

                return list;
            }

            protected String title4PopupWindow() {
                return "";
            }
        };

        this.add(boxPane, BorderLayout.NORTH);
    }

    /**
     * 判断类型, 更新界面属性
     */
    public void populateBean(CustomAttr attr) {
        boxPane.setSelectedIndex(COM_MAP.get(attr.getRenderer()));
        FurtherBasicBeanPane pane = (FurtherBasicBeanPane)boxPane.getCards().get(COM_MAP.get(attr.getRenderer()));
        pane.populateBean(attr);
    }

    /**
     * 保存界面属性.
     */
    public void updateBean(CustomAttr attr) {
        int selectedIndex = boxPane.getSelectedIndex();
        FurtherBasicBeanPane<CustomAttr> pane = (FurtherBasicBeanPane<CustomAttr>)boxPane.getCards().get(selectedIndex);

        if(COM_MAP.get(attr.getRenderer()) == boxPane.getSelectedIndex()){
            pane.updateBean(attr);
        }else{
            attr.removeAll();
            attr.setRenderer(indexToRender(boxPane.getSelectedIndex()));
            pane.populateBean(attr);
        }

        //恢复下原来的形状
        attr.setUseRenderer(attr.getRenderer());
    }

    private ChartCustomRendererType indexToRender(int index){
        Iterator<ChartCustomRendererType> iterator = COM_MAP.keySet().iterator();
        while (iterator.hasNext()){
            ChartCustomRendererType key = iterator.next();
            int tmpIndex = COM_MAP.get(key).intValue();
            if(tmpIndex == index){
                return key;
            }
        }
        return ChartCustomRendererType.BAR_RENDERER;
    }

    protected String title4PopupWindow(){
        return "";
    }

    private class CustomBarDefaultSeriesPane extends FurtherBasicBeanPane<CustomAttr> {
        private static final double HUNDRED = 100.0;
        private static final double MAX_TIME = 5.0;

        private String title = "";
        private UINumberDragPane seriesGap;
        private UINumberDragPane categoryGap;

        public CustomBarDefaultSeriesPane(String title){
            this.title = title;

            this.setLayout(new BorderLayout());
            seriesGap = new UINumberDragPane(-HUNDRED, HUNDRED);
            categoryGap = new UINumberDragPane(0, MAX_TIME * HUNDRED);

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] columnSize = {p, f};
            double[] rowSize = { p, p};
            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(Inter.getLocText("FR-Chart-Series_Gap")), seriesGap},
                    new Component[]{new UILabel(Inter.getLocText("FR-Chart-Category_Gap")), categoryGap}
            };

            this.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize), BorderLayout.NORTH);
        }

        public CustomAttr updateBean(){
            return new CustomAttr();
        }

        public void updateBean(CustomAttr attr){
            attr.removeAll();
            AttrBarSeries barSeries = new AttrBarSeries();
            barSeries.setSeriesOverlapPercent(seriesGap.updateBean()/HUNDRED);
            barSeries.setCategoryIntervalPercent(categoryGap.updateBean()/HUNDRED);
            barSeries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
            attr.addDataSeriesCondition(barSeries);
        }

        public void populateBean(CustomAttr attr){
            AttrBarSeries series = new AttrBarSeries();
            series.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
            if(attr.getExisted(AttrBarSeries.class) != null){
                series = (AttrBarSeries)attr.getExisted(AttrBarSeries.class);
            }
            seriesGap.populateBean(series.getSeriesOverlapPercent() * HUNDRED);
            categoryGap.populateBean(series.getCategoryIntervalPercent() * HUNDRED);

            attr.removeAll();
            attr.addDataSeriesCondition(series);
        }

        public String title4PopupWindow(){
            return title;
        }

        public void reset(){

        }

        public boolean accept(Object object){
            return object instanceof AttrBarSeries;
        }
    }

    private class CustomBar3DDefaultSeriesPane extends FurtherBasicBeanPane<CustomAttr>{
        private String title = "";

        public CustomBar3DDefaultSeriesPane(String title){
            this.title = title;
        }

        public CustomAttr updateBean(){
            return new CustomAttr();
        }

        public void updateBean(CustomAttr attr){
            attr.removeAll();
            attr.addDataSeriesCondition(new AttrAxisPosition(ChartAxisPosition.AXIS_LEFT));
        }

        public void populateBean(CustomAttr attr){
            attr.removeAll();
            attr.addDataSeriesCondition(new AttrAxisPosition(ChartAxisPosition.AXIS_LEFT));
        }

        public String title4PopupWindow(){
            return title;
        }

        public void reset(){

        }

        public boolean accept(Object object){
            return object instanceof AttrAxisPosition;
        }
    }

    private class CustomLineDefaultSeriesPane extends FurtherBasicBeanPane<CustomAttr>{

        protected UICheckBox isCurve;
        protected UIButtonGroup<Boolean> isNullValueBreak;
        protected LineComboBox lineStyle;
        protected MarkerComboBox markerPane;

        public CustomLineDefaultSeriesPane(){
            this.setLayout(new BorderLayout());

            isCurve = new UICheckBox(Inter.getLocText("Chart_Curve"));
            lineStyle = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
            markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
            String[] nameArray = {Inter.getLocText("Chart_Null_Value_Break"), Inter.getLocText("Chart_Null_Value_Continue")};
            Boolean[] valueArray = {true, false};
            isNullValueBreak = new UIButtonGroup<Boolean>(nameArray, valueArray);
            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] columnSize = { p, f };
            double[] rowSize = { p,p,p,p};
            Component[][] components = new Component[][]{
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart_Line_Style")),isCurve},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("Line-Style")),lineStyle},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("Null_Value_Show")), isNullValueBreak}
            };

            this.add(TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize), BorderLayout.NORTH);
        }

        public CustomAttr updateBean(){
            return new CustomAttr();
        }

        public void updateBean(CustomAttr attr){
            attr.removeAll();
            AttrLineSeries lineSeries = new AttrLineSeries();
            attr.addDataSeriesCondition(lineSeries);
            lineSeries.setCurve(isCurve.isSelected());
            lineSeries.setNullValueBreak(isNullValueBreak.getSelectedIndex() == 0);
            lineSeries.setLineStyle(lineStyle.getSelectedLineStyle());
            lineSeries.setMarkerType(markerPane.getSelectedMarkder().getMarkerType());
            lineSeries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
        }

        public void populateBean(CustomAttr attr){
            AttrLineSeries lineSeries = new AttrLineSeries();
            lineSeries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
            if(attr.getExisted(AttrLineSeries.class) != null){
                lineSeries = (AttrLineSeries)attr.getExisted(AttrLineSeries.class);
            }

            isCurve.setSelected(lineSeries.isCurve());
            isNullValueBreak.setSelectedIndex(lineSeries.isNullValueBreak() ? 0 : 1);
            lineStyle.setSelectedLineStyle(lineSeries.getLineStyle());
            markerPane.setSelectedMarker(MarkerFactory.createMarker(lineSeries.getMarkerType()));

            attr.removeAll();
            attr.addDataSeriesCondition(lineSeries);
        }

        public String title4PopupWindow(){
            return LINE;
        }

        public void reset(){

        }

        public boolean accept(Object object){
            return object instanceof AttrLineSeries;
        }
    }

    private class CustomAreaDefaultSeriesPane extends FurtherBasicBeanPane<CustomAttr>{
        private UICheckBox isCurve;
        protected MarkerComboBox markerPane;

        public CustomAreaDefaultSeriesPane(){

            this.setLayout(new BorderLayout());

            isCurve = new UICheckBox(Inter.getLocText("FR-Chart-Curve_Line"));
            markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());

            double p = TableLayout.PREFERRED;
            double f = TableLayout.FILL;
            double[] columnSize = { p,f };
            double[] rowSize = { p,p };
            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(Inter.getLocText("FR-Chart-Line_Style")),isCurve},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane}
            };

            this.add(TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize), BorderLayout.NORTH);
        }

        public CustomAttr updateBean(){
            return new CustomAttr();
        }

        public void updateBean(CustomAttr attr){
            attr.removeAll();
            AttrAreaSeries areaSeries = new AttrAreaSeries();
            attr.addDataSeriesCondition(areaSeries);
            areaSeries.setCurve(isCurve.isSelected());
            areaSeries.setMarkerType(markerPane.getSelectedMarkder().getMarkerType());
            areaSeries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
        }

        public void populateBean(CustomAttr attr){
            AttrAreaSeries areaSeries = new AttrAreaSeries();
            areaSeries.setAxisPosition(ChartAxisPosition.AXIS_LEFT);
            if(attr.getExisted(AttrAreaSeries.class) != null){
                areaSeries = (AttrAreaSeries)attr.getExisted(AttrAreaSeries.class);
            }

            isCurve.setSelected(areaSeries.isCurve());
            markerPane.setSelectedMarker(MarkerFactory.createMarker(areaSeries.getMarkerType()));

            attr.removeAll();
            attr.addDataSeriesCondition(areaSeries);
        }

        public String title4PopupWindow(){
            return AREA_STACK;
        }

        public void reset(){

        }

        public boolean accept(Object object){
            return object instanceof AttrAreaSeries;
        }

    }
}