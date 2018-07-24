package com.fr.design.chart.series.SeriesCondition.dlp;



import javax.swing.*;
import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-29
 * Time   : 上午10:34
 * 地图系列标签界面
 */
public class MapDataLabelPane extends DataLabelPane {

    @Override
    protected Component[] createComponents4ShowSeriesName() {
        return new Component[0];
    }

    @Override
    protected String getCategoryString() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Area_Name");
    }

    @Override
    protected String getValueString() {
        return com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Area_Value");
    }

    protected JPanel createJPanel4Position() {
        return null;
    }
}