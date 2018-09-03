package com.fr.van.chart.designer.component.marker;

import com.fr.chart.chartglyph.Marker;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.xcombox.MarkerComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.plugin.chart.base.VanChartAttrMarker;
import com.fr.plugin.chart.marker.type.MarkerType;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.background.VanChartMarkerBackgroundPane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Mitisky on 16/5/19.
 * 通用标记点,标记点类型/颜色/半径
 */
public class VanChartCommonMarkerPane extends BasicBeanPane<VanChartAttrMarker> {
    private MarkerComboBox markerPane;
    private VanChartMarkerBackgroundPane markerFillColor;
    private UISpinner radius;

    private static final MarkerType[] NORMAL_TYPES = {
            MarkerType.MARKER_NULL,
            MarkerType.MARKER_CIRCLE,
            MarkerType.MARKER_SQUARE,
            MarkerType.MARKER_DIAMOND,
            MarkerType.MARKER_TRIANGLE,
            MarkerType.MARKER_CIRCLE_HOLLOW,
            MarkerType.MARKER_SQUARE_HOLLOW,
            MarkerType.MARKER_DIAMOND_HOLLOW,
            MarkerType.MARKER_TRIANGLE_HOLLOW
    };
    private static Marker[] normalMarkers = null;
    protected static Marker[] getNormalMarkers() {
        if(normalMarkers == null){
            normalMarkers = new Marker[NORMAL_TYPES.length];
            int i = 0;
            for(MarkerType markerType : NORMAL_TYPES){
                normalMarkers[i++] = Marker.createMarker(markerType);
            }
        }
        return normalMarkers;
    }

    //没有无这个选项
    private static final MarkerType[] MAP_TYPES = {
            MarkerType.MARKER_CIRCLE,
            MarkerType.MARKER_SQUARE,
            MarkerType.MARKER_DIAMOND,
            MarkerType.MARKER_TRIANGLE,
            MarkerType.MARKER_CIRCLE_HOLLOW,
            MarkerType.MARKER_SQUARE_HOLLOW,
            MarkerType.MARKER_DIAMOND_HOLLOW,
            MarkerType.MARKER_TRIANGLE_HOLLOW
    };
    private static Marker[] mapMarkers = null;
    protected static Marker[] getMapScatterMarkers() {
        if(mapMarkers == null){
            mapMarkers = new Marker[MAP_TYPES.length];
            int i = 0;
            for(MarkerType markerType : MAP_TYPES){
                mapMarkers[i++] = Marker.createMarker(markerType);
            }
        }
        return mapMarkers;
    }

    //甘特图的菱形是实心的，之前写的空心。
    //兼容：模板属性不做兼容。只是之前空心做界面兼容。前台展现实心空心一样的效果，所以不用做什么
    private static final MarkerType[] GANTT_TYPES = {
            MarkerType.MARKER_TRIANGLE,
            MarkerType.MARKER_DIAMOND,
            MarkerType.MARKER_STAR
    };
    private static Marker[] ganttMarkers = null;
    protected static Marker[] getGanttMarkers() {
        if(ganttMarkers == null){
            ganttMarkers = new Marker[GANTT_TYPES.length];
            int i = 0;
            for(MarkerType markerType : GANTT_TYPES){
                ganttMarkers[i++] = Marker.createMarker(markerType);
            }
        }
        return ganttMarkers;
    }


    protected MarkerComboBox getMarkerPane(){
        return markerPane;
    }

    public VanChartCommonMarkerPane() {
        markerPane = new MarkerComboBox(getMarkers());
        markerFillColor = new VanChartMarkerBackgroundPane(){
            protected Component[][] getPaneComponents() {
                return  new Component[][]{
                        new Component[]{typeComboBox, null},
                        new Component[]{centerPane, null},
                };
            }
        };
        radius = new UISpinner(0, 100, 0.5, 0);

        double p = TableLayout.PREFERRED;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double d = TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH;
        double[] row = {p, p, p};

        Component[][] components = getUseComponent();

        JPanel jPanel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, getcolumnSize());

        this.add(jPanel);
    }

    protected double[] getcolumnSize () {
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double d = TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH;
        return new double[] {d, e};
    }

    protected Marker[] getMarkers() {
        return getNormalMarkers();
    }

    protected Component[][] getUseComponent() {
        return  new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Type")), markerPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Fill_Color")), markerFillColor},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Radius")), radius},
        };
    }

    protected Component[][] getUseComponentWithOutFillColor() {
        return  new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Type")), markerPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Radius")), radius},
        };
    }


    /**
     * title应该是一个属性，不只是对话框的标题时用到，与其他组件结合时，也会用得到
     *
     * @return 绥化狂标题
     */
    @Override
    public String title4PopupWindow() {
        return "commonMarker";
    }

    public void setDefaultValue() {
    }

    @Override
    public void populateBean(VanChartAttrMarker marker) {
        if(marker == null){
            marker = new VanChartAttrMarker();
            marker.setCommon(true);
        }
        markerPane.setSelectedMarker(Marker.createMarker(populateMarkType(marker)));
        populateColor(marker);
        radius.setValue(marker.getRadius());
    }

    protected void populateColor(VanChartAttrMarker marker) {
        markerFillColor.populate(marker.getColorBackground());
    }

    protected MarkerType populateMarkType(VanChartAttrMarker marker){
        return marker.getMarkerType();
    }
    /**
     * Update.
     */
    @Override
    public VanChartAttrMarker updateBean() {
        VanChartAttrMarker marker = new VanChartAttrMarker();
        updateBean(marker);
        return marker;
    }

    public void updateBean(VanChartAttrMarker marker){
        marker.setCommon(true);
        updateColor(marker);
        marker.setRadius(radius.getValue());
        marker.setMarkerType(MarkerType.parse(markerPane.getSelectedMarkder().getMarkerType()));
    }

    protected void updateColor(VanChartAttrMarker marker){
        marker.setColorBackground(markerFillColor.update());
    }
}
