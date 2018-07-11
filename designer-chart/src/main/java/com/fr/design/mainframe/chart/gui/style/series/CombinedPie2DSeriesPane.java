package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by eason on 15/1/14.
 */
public class CombinedPie2DSeriesPane extends Pie2DSeriesPane{
    private ChartFillStylePane fillColorPane;
    private UITextField zoomTime;

    public CombinedPie2DSeriesPane(ChartStylePane parent, Plot plot){
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        initCom();
        fillColorPane = new ChartFillStylePane();
        zoomTime = new UITextField();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { p, f };
        double[] rowSize = { p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Type_Pie")),null},
                new Component[]{fillColorPane, null},
                new Component[]{stylePane,null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart-Change_Pie_Size")),zoomTime},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected ChartFillStylePane getFillStylePane(){
        return null;
    }

    public void updateBean(Plot plot) {
        super.updateBean(plot);
        plot.setCombinedSize(Utils.string2Number(zoomTime.getText()).doubleValue());
        plot.setPlotFillStyle(fillColorPane.updateBean());
    }

    public void populateBean(Plot plot) {
        super.populateBean(plot);
        zoomTime.setText("" + plot.getCombinedSize());
        fillColorPane.populateBean(plot.getPlotFillStyle());
    }
}