package com.fr.van.chart.designer.other.zoom;

import com.fr.chartx.attr.ZoomAttribute;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;

import java.awt.Component;

/**
 * Created by shine on 2019/08/28.
 */
public class MapZoomPane extends ZoomPane {

    private UIButtonGroup<Boolean> mapZoomWidget;//地图缩放控件
    private UIButtonGroup<Boolean> gestureZoomGroup;//地图手势缩放

    @Override
    protected Component[][] getSouthComps() {
        mapZoomWidget = new UIButtonGroup<Boolean>(new String[]{Toolkit.i18nText("Fine-Design_Chart_Open")
                , Toolkit.i18nText("Fine-Design_Chart_Close")}, new Boolean[]{true, false});

        gestureZoomGroup = new UIButtonGroup<Boolean>(new String[]{Toolkit.i18nText("Fine-Design_Chart_Open")
                , Toolkit.i18nText("Fine-Design_Chart_Close")}, new Boolean[]{true, false});


        return new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Zoom_Widget")), mapZoomWidget},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_ZoomGesture")), gestureZoomGroup}
        };
    }

    @Override
    public void populateBean(ZoomAttribute ob) {
        super.populateBean(ob);
        mapZoomWidget.setSelectedItem(ob.isMapZoomWidget());
        gestureZoomGroup.setSelectedItem(ob.isGestureZoom());
    }

    @Override
    public ZoomAttribute updateBean() {
        ZoomAttribute zoomAttribute = super.updateBean();
        zoomAttribute.setMapZoomWidget(mapZoomWidget.getSelectedItem());
        zoomAttribute.setGestureZoom(gestureZoomGroup.getSelectedItem());
        return zoomAttribute;
    }
}
