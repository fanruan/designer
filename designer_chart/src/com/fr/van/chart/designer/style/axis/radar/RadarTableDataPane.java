package com.fr.van.chart.designer.style.axis.radar;

import com.fr.design.mainframe.chart.gui.data.TableDataPane;
import com.fr.plugin.chart.radar.data.RadarYAxisTableDefinition;


/**
 * Created by mengao on 2017/4/14.
 */
public class RadarTableDataPane extends TableDataPane {

    public RadarTableDataPane() {
        super(null);
        refreshContentPane(new RadarTableContentPane());
    }

    public void updateBean(RadarYAxisTableDefinition dataDefinition) {
        updateDSName(dataDefinition);
        ((RadarTableContentPane)getDataContentPane()).updateBean(dataDefinition);
    }
    public void populateBean(RadarYAxisTableDefinition dataDefinition){
        populateDSName(dataDefinition);
        ((RadarTableContentPane)getDataContentPane()).populateBean(dataDefinition);
    }

}
