package com.fr.van.chart.map;

import com.fr.base.chart.chartdata.model.DataProcessor;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrBorderWithAlpha;
import com.fr.plugin.chart.base.AttrEffect;
import com.fr.plugin.chart.base.AttrMarkerAlpha;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.bubble.attr.VanChartAttrBubble;
import com.fr.plugin.chart.drillmap.VanChartDrillMapPlot;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.map.attr.AttrMapLabel;
import com.fr.plugin.chart.map.line.condition.AttrCurve;
import com.fr.plugin.chart.map.line.condition.AttrLineEffect;
import com.fr.plugin.chart.type.MapMarkerType;
import com.fr.plugin.chart.type.MapType;
import com.fr.van.chart.bubble.component.VanChartBubblePane;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.border.VanChartBorderWithAlphaPane;
import com.fr.van.chart.designer.component.marker.VanChartImageMarkerPane;
import com.fr.van.chart.designer.other.VanChartInteractivePane;
import com.fr.van.chart.designer.style.series.VanChartAbstractPlotSeriesPane;
import com.fr.van.chart.designer.style.series.VanChartEffectPane;
import com.fr.van.chart.map.designer.style.series.VanChartMapScatterMarkerPane;
import com.fr.van.chart.map.line.VanChartCurvePane;
import com.fr.van.chart.map.line.VanChartLineMapEffectPane;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Mitisky on 16/5/4.
 * 地图-系列界面
 */
public class VanChartMapSeriesPane extends VanChartAbstractPlotSeriesPane {
    private static final String AREA_STRING = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Region");
    private static final String POINT_STRING = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Point");
    private static final String LINE_STRING = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Flow");

    private static final String[] MARKER_TYPES = new String[]{MapMarkerType.DEFAULT.toLocalString(),
            MapMarkerType.COMMON.toLocalString(), MapMarkerType.BUBBLE.toLocalString(), MapMarkerType.IMAGE.toLocalString()};

    //custom
    private UIButtonGroup<Integer> areaPointAndLineGroup;

    //drill custom
    private UIButtonGroup<Integer> areaAndPointGroup;

    //area
    private VanChartBorderWithAlphaPane borderWithAlphaPane;
    private ColorSelectBox nullValueColorBox;

    //point
    private UIComboBox markerTypeCom;
    private VanChartMapScatterMarkerPane commonMarkerPane;
    private VanChartBubblePane bubblePane;
    private VanChartImageMarkerPane imageMarkerPane;
    private UINumberDragPane pointAlphaPane;
    private VanChartEffectPane pointEffectPane;

    //line
    private VanChartCurvePane curvePane;
    private VanChartLineMapEffectPane lineMapEffectPane;

    //大数据模式 恢复用注释。下面1行删除。
    private UIButtonGroup<DataProcessor> lineMapLargeDataModelGroup;//大数据模式

    private MapType mapType = MapType.AREA;

    public VanChartMapSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    //大数据模式 恢复用注释。删除下面4个方法 checkLarge lineMapLargeModel checkLineMapLarge createLineMapLargeDataModelPane。
    @Override
    protected void checkLarge() {
        if (largeModel(plot)) {
            if (plot instanceof VanChartMapPlot) {
                ConditionAttr defaultAttr = plot.getConditionCollection().getDefaultAttr();
                AttrMapLabel attrMapLabel = defaultAttr.getExisted(AttrMapLabel.class);
                if (attrMapLabel == null) {
                    attrMapLabel = new AttrMapLabel();
                    defaultAttr.addDataSeriesCondition(attrMapLabel);
                }
                attrMapLabel.getPointLabel().setEnable(false);

                VanChartInteractivePane.resetCustomCondition(((VanChartMapPlot) plot).getPointConditionCollection());
            }
        }

        checkPointCompsEnabledWithLarge(plot);
    }


    private boolean lineMapLargeModel() {
        return lineMapLargeDataModelGroup != null && lineMapLargeDataModelGroup.getSelectedIndex() == 0;
    }

    private void checkLineMapLarge() {
        if (lineMapLargeModel()) {
            if (plot instanceof VanChartMapPlot) {
                VanChartInteractivePane.resetCustomCondition(((VanChartMapPlot) plot).getLineConditionCollection());
            }
        }

        checkLineCompsEnabledWithLarge(plot);
    }

    private JPanel createLineMapLargeDataModelPane() {
        lineMapLargeDataModelGroup = createLargeDataModelGroup();
        lineMapLargeDataModelGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkLineMapLarge();
            }
        });
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Large_Model"), lineMapLargeDataModelGroup);
        return createLargeDataModelPane(panel);
    }


    protected void checkCompsEnabledWithLarge(Plot plot) {
        checkPointCompsEnabledWithLarge(plot);
        checkLineCompsEnabledWithLarge(plot);
    }

    private void checkPointCompsEnabledWithLarge(Plot plot) {
        if (pointEffectPane != null) {
            GUICoreUtils.setEnabled(pointEffectPane, !largeModel(plot));
        }
    }

    private void checkLineCompsEnabledWithLarge(Plot plot) {
        if (lineMapEffectPane != null) {
            //大数据模式 恢复用注释。下面1行删除。
            GUICoreUtils.setEnabled(lineMapEffectPane, !lineMapLargeModel());
            //大数据模式 恢复用注释。取消注释。
            //GUICoreUtils.setEnabled(lineMapEffectPane, !largeModel(plot));
        }
    }

    /**
     * 在每个不同类型Plot, 得到不同类型的属性. 比如: 柱形的风格, 折线的线型曲线.
     */
    @Override
    protected JPanel getContentInPlotType() {
        mapType = ((VanChartMapPlot) plot).getAllLayersMapType();
        switch (mapType) {
            case AREA:
                return createAreaPane();
            case POINT:
                return createPointPane();
            case LINE:
                return createLinePane();
            default:
                return createCustomPane(plot);
        }
    }

    //设置色彩面板内容
    protected void setColorPaneContent(JPanel panel) {
        panel.add(createNullValueColorPane(), BorderLayout.CENTER);
        panel.add(createAlphaPane(), BorderLayout.SOUTH);

    }


    protected JPanel createAreaPane() {
        borderWithAlphaPane = new VanChartBorderWithAlphaPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p, p, p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{getColorPane()},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Border"), borderWithAlphaPane)},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    protected JPanel createNullValueColorPane() {
        nullValueColorBox = new ColorSelectBox(80);

        return TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_NULL_Value_Color"), nullValueColorBox);
    }

    private JPanel createPointPane() {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p, p, p, p, p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color")), createPointAlphaPane())},
                new Component[]{createMarkerComPane()},
                //大数据模式 恢复用注释。下面1行删除。
                new Component[]{createLargeDataModelPane()},
                new Component[]{createPointEffectPane()},
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    private JPanel createLinePane() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p, p, p};
        double[] col = {f};

        curvePane = new VanChartCurvePane();

        Component[][] components = new Component[][]{
                new Component[]{createCurvePane()},
                //大数据模式 恢复用注释。下面1行删除。
                new Component[]{createLineMapLargeDataModelPane()},
                new Component[]{createAnimationPane()}
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    private Component createCurvePane() {
        curvePane = new VanChartCurvePane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Curve"), curvePane);
    }

    private Component createAnimationPane() {
        lineMapEffectPane = new VanChartLineMapEffectPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Animation"), lineMapEffectPane);
    }

    //不透明度
    private JPanel createPointAlphaPane() {
        pointAlphaPane = new UINumberDragPane(0, 100);
        return TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alpha"), pointAlphaPane);
    }

    private JPanel createPointEffectPane() {
        pointEffectPane = new VanChartEffectPane();
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Animation"), pointEffectPane);
    }

    private JPanel createMarkerComPane() {
        markerTypeCom = new UIComboBox(MARKER_TYPES);

        commonMarkerPane = new VanChartMapScatterMarkerPane();
        commonMarkerPane.setBorder(TableLayout4VanChartHelper.SECOND_EDIT_AREA_BORDER);
        bubblePane = new VanChartBubblePane() {
            protected JPanel getContentPane() {
                double p = TableLayout.PREFERRED;
                double f = TableLayout.FILL;
                double e = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
                double[] row = {p, p, p, p, p};
                double[] col = {f, e};

                JPanel panel = TableLayoutHelper.createTableLayoutPane(getComponent(), row, col);
                panel.setBorder(TableLayout4VanChartHelper.SECOND_EDIT_AREA_BORDER);
                return panel;
            }
        };
        imageMarkerPane = new VanChartImageMarkerPane();

        final JPanel[] panes = new JPanel[]{new JPanel(), commonMarkerPane, bubblePane, imageMarkerPane};
        final CardLayout cardLayout = new CardLayout();
        final JPanel cardPane = new JPanel(cardLayout) {
            @Override
            public Dimension getPreferredSize() {
                return panes[markerTypeCom.getSelectedIndex()].getPreferredSize();
            }
        };

        for (int i = 0, len = MARKER_TYPES.length; i < len; i++) {
            cardPane.add(panes[i], MARKER_TYPES[i]);
        }

        markerTypeCom.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                cardLayout.show(cardPane, MARKER_TYPES[markerTypeCom.getSelectedIndex()]);
            }
        });

        JPanel northPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Point_Style"), markerTypeCom);
        JPanel markerPane = new JPanel(new BorderLayout(0, 6));
        markerPane.add(northPane, BorderLayout.NORTH);
        markerPane.add(cardPane, BorderLayout.CENTER);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Marker"), markerPane);
    }

    private JPanel createCustomPane(Plot plot) {
        JPanel areaPane = createAreaPane();
        JPanel pointPane = createPointPane();
        JPanel linePane = createLinePane();

        JPanel panel = createGroupPane(plot, areaPane, pointPane, linePane);

        return panel;
    }

    private JPanel createGroupPane(Plot plot, JPanel areaPane, JPanel pointPane, JPanel linePane) {
        JPanel panel;
        if (ComparatorUtils.equals(plot.getClass(), VanChartDrillMapPlot.class)) {
            panel = createDrillMapCustomGroupPane(areaPane, pointPane);
        } else {
            panel = createMapCustomGroupPane(areaPane, pointPane, linePane);
        }
        return panel;
    }

    private JPanel createMapCustomGroupPane(JPanel areaPane, JPanel pointPane, JPanel linePane) {
        areaPointAndLineGroup = new UIButtonGroup<Integer>(new String[]{AREA_STRING, POINT_STRING, LINE_STRING});
        areaPointAndLineGroup.setSelectedIndex(0);

        final CardLayout cardLayout = new CardLayout();
        final JPanel centerPane = new JPanel(cardLayout);
        centerPane.add(areaPane, AREA_STRING);
        centerPane.add(pointPane, POINT_STRING);
        centerPane.add(linePane, LINE_STRING);

        areaPointAndLineGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (areaPointAndLineGroup.getSelectedIndex() == 0) {
                    cardLayout.show(centerPane, AREA_STRING);
                } else if (areaPointAndLineGroup.getSelectedIndex() == 1) {
                    cardLayout.show(centerPane, POINT_STRING);
                } else {
                    cardLayout.show(centerPane, LINE_STRING);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(areaPointAndLineGroup, BorderLayout.NORTH);
        panel.add(centerPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDrillMapCustomGroupPane(JPanel areaPane, JPanel pointPane) {
        areaAndPointGroup = new UIButtonGroup<Integer>(new String[]{AREA_STRING, POINT_STRING});
        areaAndPointGroup.setSelectedIndex(0);

        final CardLayout cardLayout = new CardLayout();
        final JPanel centerPane = new JPanel(cardLayout);
        centerPane.add(areaPane, AREA_STRING);
        centerPane.add(pointPane, POINT_STRING);

        areaAndPointGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (areaAndPointGroup.getSelectedIndex() == 0) {
                    cardLayout.show(centerPane, AREA_STRING);
                } else {
                    cardLayout.show(centerPane, POINT_STRING);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(areaAndPointGroup, BorderLayout.NORTH);
        panel.add(centerPane, BorderLayout.CENTER);
        return panel;
    }

    public void populateBean(Plot plot) {
        if (plot != null && plot instanceof VanChartMapPlot) {
            if (markerTypeCom != null) {
                markerTypeCom.setSelectedItem(((VanChartMapPlot) plot).getMapMarkerType().toLocalString());
            }
            if (nullValueColorBox != null) {
                nullValueColorBox.setSelectObject(((VanChartMapPlot) plot).getNullValueColor());
            }
            //大数据模式 恢复用注释。下面3行删除。
            if (lineMapLargeDataModelGroup != null) {
                lineMapLargeDataModelGroup.setSelectedItem(((VanChartMapPlot) plot).getLineMapDataProcessor());
            }
        }
        super.populateBean(plot);
    }

    public void updateBean(Plot plot) {
        if (plot != null && plot instanceof VanChartMapPlot) {
            if (markerTypeCom != null) {
                ((VanChartMapPlot) plot).setMapMarkerType(MapMarkerType.parseInt(markerTypeCom.getSelectedIndex()));
            }
            if (nullValueColorBox != null) {
                ((VanChartMapPlot) plot).setNullValueColor(nullValueColorBox.getSelectObject());
            }
            //大数据模式 恢复用注释。下面3行删除。
            if (lineMapLargeDataModelGroup != null) {
                ((VanChartMapPlot) plot).setLineMapDataProcessor(lineMapLargeDataModelGroup.getSelectedItem());
            }
        }
        super.updateBean(plot);
    }

    @Override
    protected void populateCondition(ConditionAttr defaultAttr) {
        switch (mapType) {
            case AREA:
                populateArea(defaultAttr);
                break;
            case POINT:
                populatePoint(defaultAttr);
                break;
            case LINE:
                populateLine(defaultAttr);
                break;
            default:
                populatePoint(defaultAttr);
                populateArea(defaultAttr);
                populateLine(defaultAttr);
                break;
        }
    }

    protected void populateArea(ConditionAttr defaultAttr) {
        populateAlpha(defaultAttr);
        if (borderWithAlphaPane != null) {
            AttrBorderWithAlpha attrBorderWithAlpha = defaultAttr.getExisted(AttrBorderWithAlpha.class);
            borderWithAlphaPane.populate(attrBorderWithAlpha);
        }
    }


    private void populatePoint(ConditionAttr defaultAttr) {
        if (pointAlphaPane != null) {
            AttrMarkerAlpha attrAlpha = defaultAttr.getExisted(AttrMarkerAlpha.class);
            double alpha = VanChartAttrHelper.PERCENT * (attrAlpha == null ? 1 : attrAlpha.getAlpha());
            pointAlphaPane.populateBean(alpha);
        }

        if (pointEffectPane != null) {
            AttrEffect attrEffect = defaultAttr.getExisted(AttrEffect.class);
            if (attrEffect == null) {//老的模板做界面上的兼容
                attrEffect = new AttrEffect(3.2);
                attrEffect.setEnabled(false);
            }
            pointEffectPane.populateBean(attrEffect);
        }

        VanChartAttrMarker attrMarker = defaultAttr.getExisted(VanChartAttrMarker.class);
        if (commonMarkerPane != null) {
            commonMarkerPane.populateBean(attrMarker);
        }
        if (imageMarkerPane != null) {
            imageMarkerPane.populateBean(attrMarker);
        }
        if (bubblePane != null) {
            VanChartAttrBubble attrBubble = defaultAttr.getExisted(VanChartAttrBubble.class);
            bubblePane.populateBean(attrBubble);
        }
    }

    private void populateLine(ConditionAttr defaultAttr) {
        if (curvePane != null) {
            if (defaultAttr.getExisted(AttrCurve.class) == null) {
                defaultAttr.addDataSeriesCondition(new AttrCurve());
            }
            curvePane.populateBean(defaultAttr.getExisted(AttrCurve.class));
        }
        if (lineMapEffectPane != null) {
            if (defaultAttr.getExisted(AttrLineEffect.class) == null) {
                defaultAttr.addDataSeriesCondition(new AttrLineEffect());
            }
            AttrLineEffect attrLineEffect = defaultAttr.getExisted(AttrLineEffect.class);
            lineMapEffectPane.populateBean(attrLineEffect);
        }
    }

    @Override
    protected void updateCondition(ConditionAttr defaultAttr) {
        switch (mapType) {
            case AREA:
                updateArea(defaultAttr);
                break;
            case POINT:
                updatePoint(defaultAttr);
                break;
            case LINE:
                updateLine(defaultAttr);
                break;
            default:
                updateArea(defaultAttr);
                updatePoint(defaultAttr);
                updateLine(defaultAttr);
                break;
        }
    }

    protected void checkoutMapType(Plot plot) {
        this.mapType = ((VanChartMapPlot) plot).getMapType();
    }

    protected void updateArea(ConditionAttr defaultAttr) {
        updateAlpha(defaultAttr);
        if (borderWithAlphaPane != null) {
            AttrBorderWithAlpha attrBorderWithAlpha = defaultAttr.getExisted(AttrBorderWithAlpha.class);
            defaultAttr.remove(attrBorderWithAlpha);
            defaultAttr.addDataSeriesCondition(borderWithAlphaPane.update());
        }
    }

    private void updatePoint(ConditionAttr defaultAttr) {
        if (pointAlphaPane != null) {
            AttrMarkerAlpha attrAlpha = defaultAttr.getExisted(AttrMarkerAlpha.class);
            if (attrAlpha == null) {
                attrAlpha = new AttrMarkerAlpha();
                defaultAttr.addDataSeriesCondition(attrAlpha);
            }
            attrAlpha.setAlpha((float) (pointAlphaPane.updateBean() / VanChartAttrHelper.PERCENT));
        }

        if (pointEffectPane != null) {
            AttrEffect attrEffect = defaultAttr.getExisted(AttrEffect.class);
            defaultAttr.remove(attrEffect);
            defaultAttr.addDataSeriesCondition(pointEffectPane.updateBean());
        }

        VanChartAttrMarker attrMarker = defaultAttr.getExisted(VanChartAttrMarker.class);
        defaultAttr.remove(attrMarker);
        VanChartAttrBubble attrBubble = defaultAttr.getExisted(VanChartAttrBubble.class);
        defaultAttr.remove(attrBubble);
        if (markerTypeCom != null) {
            if (markerTypeCom.getSelectedIndex() == 1) {
                defaultAttr.addDataSeriesCondition(commonMarkerPane.updateBean());
            } else if (markerTypeCom.getSelectedIndex() == 2) {
                defaultAttr.addDataSeriesCondition(bubblePane.updateBean());
            } else if (markerTypeCom.getSelectedIndex() == 3) {
                defaultAttr.addDataSeriesCondition(imageMarkerPane.updateBean());
            }
        }
    }

    private void updateLine(ConditionAttr defaultAttr) {
        if (curvePane != null) {
            AttrCurve attrCurve = defaultAttr.getExisted(AttrCurve.class);
            if (attrCurve != null) {
                defaultAttr.remove(AttrCurve.class);
            }
            attrCurve = curvePane.updateBean();
            defaultAttr.addDataSeriesCondition(attrCurve);
        }
        if (lineMapEffectPane != null) {
            AttrLineEffect attrLineEffect = defaultAttr.getExisted(AttrLineEffect.class);
            if (attrLineEffect != null) {
                defaultAttr.remove(AttrLineEffect.class);
            }
            attrLineEffect = (AttrLineEffect) lineMapEffectPane.updateBean();
            defaultAttr.addDataSeriesCondition(attrLineEffect);
        }
    }
}
