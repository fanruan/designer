package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.chart.base.*;
import com.fr.chart.chartglyph.CustomAttr;
import com.fr.chart.chartglyph.MarkerFactory;
import com.fr.data.condition.AbstractCondition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.chart.series.SeriesCondition.ChartConditionPane;
import com.fr.design.condition.LiteConditionPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 组合图对单独某个系列设置的界面
 * Created by eason on 15/2/10.
 */
public class CustomTypeConditionSeriesPane extends BasicBeanPane<CustomAttr>{

    private static final int STYLE_WIDTH = 490;
    private static final int STYLE_HEIGHT = 50;

    private CustomAttr editing = new CustomAttr();

    protected UIRadioButton barRadioButton;
    protected UIRadioButton barStackButton;
    protected UIRadioButton bar3DRadioButton;
    protected UIRadioButton bar3DStackButton;
    protected UIRadioButton lineRadioButton;
    protected UIRadioButton areaStackButton;

    private CardLayout cardLayout;
    private JPanel cardPane;
    protected LiteConditionPane liteConditionPane;

    BasicBeanPane<CustomAttr> barPane = null;
    BasicBeanPane<CustomAttr> barStackPane = null;
    BasicBeanPane<CustomAttr> bar3DPane =null;
    BasicBeanPane<CustomAttr> bar3DStackPane = null;
    BasicBeanPane<CustomAttr> linePane = null;
    BasicBeanPane<CustomAttr> areaPane = null;

    public CustomTypeConditionSeriesPane(){

        this.setLayout(new BorderLayout());

        liteConditionPane = new ChartConditionPane();
        JPanel conditionPane = new JPanel();
        conditionPane.setLayout(new BoxLayout(conditionPane, BoxLayout.Y_AXIS));
        conditionPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Chart-Series_Config"), null));
        conditionPane.add(liteConditionPane);

        this.setLayout(new BorderLayout());
        this.add(this.getCustomAttrPane(), BorderLayout.NORTH);
        this.add(conditionPane, BorderLayout.CENTER);

        initListener();
    }

    private void initListener() {
        initBarListener();

        lineRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(lineRadioButton.isSelected()) {
                    cardLayout.show(cardPane, "Line");
                    linePane.populateBean(editing);
                }
            }
        });

        areaStackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(areaStackButton.isSelected()) {
                    cardLayout.show(cardPane, "Area");
                    areaPane.populateBean(editing);
                }
            }
        });
    }

    private void initBarListener(){
        barRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if(barRadioButton.isSelected()) {
                    cardLayout.show(cardPane, "Bar");
                    barPane.populateBean(editing);
                }
            }
        });

        barStackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(barStackButton.isSelected()) {
                    cardLayout.show(cardPane, "BarStack");
                    barStackPane.populateBean(editing);
                }
            }
        });

        bar3DRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(bar3DRadioButton.isSelected()){
                    cardLayout.show(cardPane, "Bar3D");
                    bar3DPane.populateBean(editing);
                }
            }
        });

        bar3DStackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(bar3DStackButton.isSelected()){
                    cardLayout.show(cardPane, "Bar3DStack");
                    bar3DStackPane.populateBean(editing);
                }
            }
        });
    }

    private JPanel getCustomAttrPane() {
        JPanel stylePane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane();

        stylePane.add(barRadioButton = new UIRadioButton(Inter.getLocText("ChartF-Column")));
        stylePane.add(barStackButton = new UIRadioButton(Inter.getLocText("I-BarStyle_NormalStack")));
        stylePane.add(bar3DRadioButton = new UIRadioButton(Inter.getLocText("FR-Chart-Bar3D_Chart")));
        stylePane.add(bar3DStackButton = new UIRadioButton(Inter.getLocText("FR-Chart-Bar3DStack_Chart")));
        stylePane.add(lineRadioButton = new UIRadioButton(Inter.getLocText("ChartF-Line")));
        stylePane.add(areaStackButton = new UIRadioButton(Inter.getLocText("I-AreaStyle_Stack")));

        ButtonGroup rendererButtonGroup = new ButtonGroup();
        rendererButtonGroup.add(barRadioButton);
        rendererButtonGroup.add(barStackButton);
        rendererButtonGroup.add(bar3DRadioButton);
        rendererButtonGroup.add(bar3DStackButton);
        rendererButtonGroup.add(lineRadioButton);
        rendererButtonGroup.add(areaStackButton);
        barRadioButton.setSelected(true);
        stylePane.setPreferredSize(new Dimension(STYLE_WIDTH, STYLE_HEIGHT));

        JPanel mainPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        mainPane.add(cardPane);
        cardLayout = new CardLayout();
        cardPane.setLayout(cardLayout);
        cardPane.add(barPane = new CustomTypeBarSeriesPane(), "Bar");
        cardPane.add(barStackPane = new CustomTypeBarSeriesPane(), "BarStack");
        cardPane.add(bar3DPane = new CustomTypeBar3DSeriesPane(), "Bar3D");
        cardPane.add(bar3DStackPane = new CustomTypeBar3DSeriesPane(), "Bar3DStack");
        cardPane.add(linePane = new CustomTypeLineSeriesPane(), "Line");
        cardPane.add(areaPane = new CustomTypeAreaSeriesPane(), "Area");
        cardLayout.show(cardPane, "Bar");

        JPanel styleChoosePane = new JPanel();
        styleChoosePane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Chart-Choose_Style"), null));
        styleChoosePane.setLayout(new BoxLayout(styleChoosePane, BoxLayout.Y_AXIS));
        styleChoosePane.add(stylePane);
        styleChoosePane.add(mainPane);

        return styleChoosePane;
    }

    public void populateBean(CustomAttr attr){
        switch (attr.getRenderer()){
            case BAR_RENDERER:
                this.barRadioButton.setSelected(true);
                cardLayout.show(cardPane, "Bar");
                barPane.populateBean(attr);
                break;
            case BAR_STACK:
                this.barStackButton.setSelected(true);
                cardLayout.show(cardPane, "BarStack");
                barStackPane.populateBean(attr);
                break;
            case BAR3D:
                this.bar3DRadioButton.setSelected(true);
                cardLayout.show(cardPane, "Bar3D");
                bar3DPane.populateBean(attr);
                break;
            case BAR3D_STACK:
                this.bar3DStackButton.setSelected(true);
                cardLayout.show(cardPane, "Bar3DStack");
                bar3DStackPane.populateBean(attr);
                break;
            case LINE_RENDERER:
                this.lineRadioButton.setSelected(true);
                cardLayout.show(cardPane, "Line");
                linePane.populateBean(attr);
                break;
            case AREA_STACK:
                this.areaStackButton.setSelected(true);
                cardLayout.show(cardPane, "Area");
                areaPane.populateBean(attr);
                break;
            default:
                this.barRadioButton.setSelected(true);
                cardLayout.show(cardPane, "Bar");
                barPane.populateBean(attr);
        }

        liteConditionPane.populateBean(attr.getCondition());
    }

    public CustomAttr updateBean(){
        return new CustomAttr();
    }

    public void updateBean(CustomAttr customAttr){
        customAttr.removeAll();
        if (this.barRadioButton.isSelected()) {
            customAttr.setRenderer(ChartCustomRendererType.BAR_RENDERER);
            barPane.updateBean(customAttr);
        } else if (this.lineRadioButton.isSelected()) {
            customAttr.setRenderer(ChartCustomRendererType.LINE_RENDERER);
            linePane.updateBean(customAttr);
        } else if (this.barStackButton.isSelected()) {
            customAttr.setRenderer(ChartCustomRendererType.BAR_STACK);
            barStackPane.updateBean(customAttr);
        } else if(this.areaStackButton.isSelected()) {
            customAttr.setRenderer(ChartCustomRendererType.AREA_STACK);
            areaPane.updateBean(customAttr);
        } else if(this.bar3DRadioButton.isSelected()){
            customAttr.setRenderer(ChartCustomRendererType.BAR3D);
            bar3DPane.updateBean(customAttr);
        } else if(this.bar3DStackButton.isSelected()){
            customAttr.setRenderer(ChartCustomRendererType.BAR3D_STACK);
            bar3DStackPane.updateBean(customAttr);
        }
        customAttr.setCondition((AbstractCondition)liteConditionPane.updateBean());
    }

    protected String title4PopupWindow(){
        return Inter.getLocText("FR-Chart-Series_Config");
    }

    private class CustomTypeBarSeriesPane extends BasicBeanPane<CustomAttr>{
        private static final double HUNDRED = 100.0;
        private static final double MAX_TIME = 5.0;
        private UIButtonGroup<String> positionGroup;
        private UINumberDragPane seriesGap;
        private UINumberDragPane categoryGap;
        public CustomTypeBarSeriesPane(){

            UILabel nameLabel = new UILabel(Inter.getLocText("FR-Chart-Axis_Choose"));
            String[] names = new String[]{Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")};
            String[] values = new String[]{ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()};
            positionGroup = new UIButtonGroup<String>(names, values);
            positionGroup.setAllToolTips(names);
            positionGroup.setSelectedItem(ChartAxisPosition.AXIS_LEFT.getAxisPosition());
            JPanel positionPane = new JPanel(FRGUIPaneFactory.createLabelFlowLayout());
            positionPane.add(nameLabel);
            positionPane.add(positionGroup);

            seriesGap = new UINumberDragPane(-HUNDRED, HUNDRED);
            categoryGap = new UINumberDragPane(0, MAX_TIME * HUNDRED);
            seriesGap.setPreferredSize(new Dimension(150,20));
            categoryGap.setPreferredSize(new Dimension(150,20));

            double p = TableLayout.PREFERRED;
            double[] columnSize = {p, p};
            double[] rowSize = { p, p};
            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(Inter.getLocText("FR-Chart-Series_Gap")), seriesGap},
                    new Component[]{new UILabel(Inter.getLocText("FR-Chart-Category_Gap")), categoryGap}

            };

            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(positionPane);
            this.add(TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize));
        }

        public void populateBean(CustomAttr attr){
            AttrBarSeries series = new AttrBarSeries();
            if(attr.getExisted(AttrBarSeries.class) != null){
                series = (AttrBarSeries)attr.getExisted(AttrBarSeries.class);
            }
            seriesGap.populateBean(series.getSeriesOverlapPercent() * HUNDRED);
            categoryGap.populateBean(series.getCategoryIntervalPercent() * HUNDRED);
            positionGroup.setSelectedItem(series.getAxisPosition().getAxisPosition());
            attr.removeAll();
            attr.addDataSeriesCondition(series);
        }

        public void updateBean(CustomAttr attr){
            attr.removeAll();
            AttrBarSeries barSeries = new AttrBarSeries();
            barSeries.setAxisPosition(ChartAxisPosition.parse(positionGroup.getSelectedItem()));
            barSeries.setSeriesOverlapPercent(seriesGap.updateBean()/HUNDRED);
            barSeries.setCategoryIntervalPercent(categoryGap.updateBean()/HUNDRED);
            attr.addDataSeriesCondition(barSeries);
        }

        public CustomAttr updateBean(){
            return new CustomAttr();
        }

        protected String title4PopupWindow(){
            return Inter.getLocText("FR-Chart-Series_Config");
        }
    }

    private class CustomTypeBar3DSeriesPane extends BasicBeanPane<CustomAttr>{
        private UIButtonGroup<String> positionGroup;

        public CustomTypeBar3DSeriesPane(){
            UILabel nameLabel = new UILabel(Inter.getLocText("FR-Chart-Axis_Choose"));
            String[] names = new String[]{Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")};
            String[] values = new String[]{ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()};
            positionGroup = new UIButtonGroup<String>(names, values);
            positionGroup.setAllToolTips(names);
            positionGroup.setSelectedItem(ChartAxisPosition.AXIS_RIGHT.getAxisPosition());
            this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
            this.add(nameLabel);
            this.add(positionGroup);
        }

        public void populateBean(CustomAttr attr){
            AttrAxisPosition series = new AttrAxisPosition(ChartAxisPosition.AXIS_RIGHT);
            if(attr.getExisted(AttrAxisPosition.class) != null){
                series = (AttrAxisPosition)attr.getExisted(AttrAxisPosition.class);
            }
            positionGroup.setSelectedItem(series.getAxisPosition().getAxisPosition());

            attr.removeAll();
            attr.addDataSeriesCondition(series);
        }

        public void updateBean(CustomAttr attr){
            attr.removeAll();
            AttrAxisPosition barSeries = new AttrAxisPosition();
            barSeries.setAxisPosition(ChartAxisPosition.parse(positionGroup.getSelectedItem()));
            attr.addDataSeriesCondition(barSeries);
        }

        public CustomAttr updateBean(){
            return new CustomAttr();
        }

        protected String title4PopupWindow(){
            return Inter.getLocText("FR-Chart-Series_Config");
        }
    }

    private class CustomTypeAreaSeriesPane extends BasicBeanPane<CustomAttr>{
        private UICheckBox isCurve;
        protected MarkerComboBox markerPane;
        private UIButtonGroup<String> positionGroup;

        public CustomTypeAreaSeriesPane(){
            UILabel nameLabel = new UILabel(Inter.getLocText("FR-Chart-Axis_Choose"));
            String[] names = new String[]{Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")};
            String[] values = new String[]{ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()};
            positionGroup = new UIButtonGroup<String>(names, values);
            positionGroup.setAllToolTips(names);
            positionGroup.setSelectedItem(ChartAxisPosition.AXIS_LEFT.getAxisPosition());
            JPanel positionPane = new JPanel(FRGUIPaneFactory.createLabelFlowLayout());
            positionPane.add(nameLabel);
            positionPane.add(positionGroup);

            isCurve = new UICheckBox(Inter.getLocText("FR-Chart-Curve_Line"));
            markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
            markerPane.setPreferredSize(new Dimension(150,20));

            double p = TableLayout.PREFERRED;
            double[] columnSize = { p,p};
            double[] rowSize = { p,p,p };
            Component[][] components = new Component[][]{
                    new Component[]{positionPane, null},
                    new Component[]{new UILabel(Inter.getLocText("FR-Chart-Line_Style")),isCurve},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane}
            };

            this.setLayout(new BorderLayout());
            this.add(TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize), BorderLayout.NORTH);
        }

        public void populateBean(CustomAttr attr){
            AttrAreaSeries areaSeries = new AttrAreaSeries();
            if(attr.getExisted(AttrAreaSeries.class) != null){
                areaSeries = (AttrAreaSeries)attr.getExisted(AttrAreaSeries.class);
            }

            isCurve.setSelected(areaSeries.isCurve());
            markerPane.setSelectedMarker(MarkerFactory.createMarker(areaSeries.getMarkerType()));
            positionGroup.setSelectedItem(areaSeries.getAxisPosition().getAxisPosition());

            attr.removeAll();
            attr.addDataSeriesCondition(areaSeries);
        }

        public void updateBean(CustomAttr attr){
            attr.removeAll();
            AttrAreaSeries areaSeries = new AttrAreaSeries();
            attr.addDataSeriesCondition(areaSeries);
            areaSeries.setCurve(isCurve.isSelected());
            areaSeries.setMarkerType(markerPane.getSelectedMarkder().getMarkerType());
            areaSeries.setAxisPosition(ChartAxisPosition.parse(positionGroup.getSelectedItem()));
        }

        public CustomAttr updateBean(){
            return new CustomAttr();
        }

        protected String title4PopupWindow(){
            return Inter.getLocText("FR-Chart-Series_Config");
        }
    }

    private class CustomTypeLineSeriesPane extends BasicBeanPane<CustomAttr>{

        private UIButtonGroup<String> positionGroup;
        protected UICheckBox isCurve;
        protected UIButtonGroup<Boolean> isNullValueBreak;
        protected LineComboBox lineStyle;
        protected MarkerComboBox markerPane;

        public CustomTypeLineSeriesPane(){
            UILabel nameLabel = new UILabel(Inter.getLocText("FR-Chart-Axis_Choose"));
            String[] names = new String[]{Inter.getLocText("ChartF-MainAxis"), Inter.getLocText("ChartF-SecondAxis")};
            String[] values = new String[]{ChartAxisPosition.AXIS_LEFT.getAxisPosition(), ChartAxisPosition.AXIS_RIGHT.getAxisPosition()};
            positionGroup = new UIButtonGroup<String>(names, values);
            positionGroup.setAllToolTips(names);
            positionGroup.setSelectedItem(ChartAxisPosition.AXIS_LEFT.getAxisPosition());
            JPanel positionPane = new JPanel(FRGUIPaneFactory.createLabelFlowLayout());
            positionPane.add(nameLabel);
            positionPane.add(positionGroup);

            isCurve = new UICheckBox(Inter.getLocText("Chart_Curve"));
            lineStyle = new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
            markerPane = new MarkerComboBox(MarkerFactory.getMarkerArray());
            isCurve.setPreferredSize(new Dimension(150,20));
            lineStyle.setPreferredSize(new Dimension(150,20));
            markerPane.setPreferredSize(new Dimension(150,20));

            String[] nameArray = {Inter.getLocText("Chart_Null_Value_Break"), Inter.getLocText("Chart_Null_Value_Continue")};
            Boolean[] valueArray = {true, false};
            isNullValueBreak = new UIButtonGroup<Boolean>(nameArray, valueArray);
            double p = TableLayout.PREFERRED;
            double[] columnSize = { p, p};
            double[] rowSize = { p,p,p,p,p};
            Component[][] components = new Component[][]{
                    new Component[]{positionPane, null},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart_Line_Style")),isCurve},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("Line-Style")),lineStyle},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Marker_Type")), markerPane},
                    new Component[]{new BoldFontTextLabel(Inter.getLocText("Null_Value_Show")), isNullValueBreak}
            };

            this.setLayout(new BorderLayout());
            this.add(TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize), BorderLayout.NORTH);
        }

        public void populateBean(CustomAttr attr){
            AttrLineSeries lineSeries = new AttrLineSeries();
            if(attr.getExisted(AttrLineSeries.class) != null){
                lineSeries = (AttrLineSeries)attr.getExisted(AttrLineSeries.class);
            }

            isCurve.setSelected(lineSeries.isCurve());
            isNullValueBreak.setSelectedIndex(lineSeries.isNullValueBreak() ? 0 : 1);
            lineStyle.setSelectedLineStyle(lineSeries.getLineStyle());
            markerPane.setSelectedMarker(MarkerFactory.createMarker(lineSeries.getMarkerType()));
            positionGroup.setSelectedItem(lineSeries.getAxisPosition().getAxisPosition());

            attr.removeAll();
            attr.addDataSeriesCondition(lineSeries);
        }

        public void updateBean(CustomAttr attr){
            attr.removeAll();
            AttrLineSeries lineSeries = new AttrLineSeries();
            attr.addDataSeriesCondition(lineSeries);
            lineSeries.setCurve(isCurve.isSelected());
            lineSeries.setNullValueBreak(isNullValueBreak.getSelectedIndex() == 0);
            lineSeries.setLineStyle(lineStyle.getSelectedLineStyle());
            lineSeries.setMarkerType(markerPane.getSelectedMarkder().getMarkerType());
            lineSeries.setAxisPosition(ChartAxisPosition.parse(positionGroup.getSelectedItem()));
        }

        public CustomAttr updateBean(){
            return new CustomAttr();
        }

        protected String title4PopupWindow(){
            return Inter.getLocText("FR-Chart-Series_Config");
        }
    }
}