package com.fr.design.chart.mobile;

import com.fr.base.mobile.ChartMobileAttrProvider;
import com.fr.base.mobile.ChartMobileFitAttrState;
import com.fr.base.mobile.ChartMobileFitAttrStateProvider;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

/**
 * 当前图表块在移动端的一些属性
 * Created by plough on 2018/1/23.
 */
public class ChartMobileAttr implements ChartMobileAttrProvider {

    public static final String XML_TAG = "ChartMobileAttr";
    // 缩小逻辑属性
    private ChartMobileFitAttrState zoomOutAttr;
    // 放大逻辑属性
    private ChartMobileFitAttrState zoomInAttr;

    @Override
    public ChartMobileFitAttrStateProvider getZoomOutAttr() {
        return zoomOutAttr;
    }

    @Override
    public void setZoomOutAttr(ChartMobileFitAttrStateProvider zoomOutAttr) {
        this.zoomOutAttr = (ChartMobileFitAttrState) zoomOutAttr;
    }

    @Override
    public ChartMobileFitAttrStateProvider getZoomInAttr() {
        return zoomInAttr;
    }

    @Override
    public void setZoomInAttr(ChartMobileFitAttrStateProvider zoomInAttr) {
        this.zoomInAttr = (ChartMobileFitAttrState) zoomInAttr;
    }

    @Override
    public boolean isAdaptive() {
        // TODO: body 是否开启手机重布局
        return true;
    }

    @Override
    public void createJSONConfig(JSONObject jo) throws JSONException {
        jo.put("zoomOutFit", this.zoomOutAttr.getState());
        jo.put("zoomInFit", this.zoomInAttr.getState());
    }

    @Override
    public void readXML(XMLableReader reader) {
        int defaultIndex = ChartMobileFitAttrState.AUTO.getState();
        int zoomOutIndex = reader.getAttrAsInt("zoomOut", defaultIndex);
        int zoomInIndex = reader.getAttrAsInt("zoomIn", defaultIndex);
        this.zoomOutAttr = ChartMobileFitAttrState.parse(zoomOutIndex);
        this.zoomInAttr = ChartMobileFitAttrState.parse(zoomInIndex);
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
        writer.startTAG(ChartMobileAttrProvider.XML_TAG)
                .attr("zoomOut", this.zoomOutAttr.getState())
                .attr("zoomIn", this.zoomInAttr.getState())
                .end();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
