package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Plot;
import com.fr.design.chart.ChartPlotFactory;
import com.fr.design.chart.axis.AxisStyleObject;
import com.fr.design.chart.axis.ChartStyleAxisPane;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.mainframe.chart.gui.ChartStylePane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eason on 14-10-23.
 */
public class ChartAxisNoFormulaPane extends ChartAxisPane{
    public ChartAxisNoFormulaPane(Plot plot, ChartStylePane parent){
        super(plot, parent);
    }

    protected List<NamePane> initPaneList(Plot plot, AbstractAttrNoScrollPane parent) {
        List<NamePane> paneList = new ArrayList<NamePane>();

        ChartStyleAxisPane axisStylePane = ChartPlotFactory.createChartStyleAxisPaneByPlot(plot);
        AxisStyleObject[] objs = axisStylePane.createAxisStyleObjects(plot);
        for(int i = 0; i < objs.length; i++) {
            ChartAxisUsePane usePane = objs[i].getAxisStylePane();
            usePane = ChartPlotFactory.getNoFormulaPane(usePane);

            if(i == 0) {
                first = usePane;
            } else if(i == 1) {
                second = usePane;
            } else if(i == 2) {
                third = usePane;
            }
            paneList.add(new NamePane(objs[i].getName(), usePane));
        }

        return paneList;
    }

}