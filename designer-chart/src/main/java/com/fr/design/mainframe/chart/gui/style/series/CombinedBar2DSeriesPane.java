package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.design.mainframe.chart.gui.style.ChartBeautyPane;
import com.fr.design.mainframe.chart.gui.style.ChartFillStylePane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by eason on 15/1/14.
 */
public class CombinedBar2DSeriesPane extends Bar2DSeriesPane{
    ChartFillStylePane fillColorPane;
    private UITextField zoomTime;
    public CombinedBar2DSeriesPane(ChartStylePane parent, Plot plot){
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        stylePane = new ChartBeautyPane();
        fillColorPane = new ChartFillStylePane();
        zoomTime = new UITextField();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] columnSize = {p, f};
        double[] rowSize = { p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Type_Column"))},
                new Component[]{fillColorPane, null},
                new Component[]{stylePane, null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart-Change_Bar_Size")),zoomTime}
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected ChartFillStylePane getFillStylePane(){
        return null;
    }

    public void populateBean(Plot plot) {
        super.populateBean(plot);
        fillColorPane.populateBean(plot.getPlotFillStyle());
        zoomTime.setText("" + plot.getCombinedSize());
    }

    public void updateBean(Plot plot) {
        super.updateBean(plot);
        plot.setPlotFillStyle(fillColorPane.updateBean());
        plot.setCombinedSize(Utils.string2Number(zoomTime.getText()).doubleValue());
    }
}