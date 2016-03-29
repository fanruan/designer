package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.base.Utils;
import com.fr.chart.chartattr.Plot;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.gui.ilable.UILabel;
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
public class CombinedBubbleSeriesPane extends BubbleSeriesPane{
    private ChartFillStylePane fillColorPane;

    public CombinedBubbleSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    protected JPanel getContentInPlotType() {
        initCom();

        fillColorPane = new ChartFillStylePane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { p, f };
        double[] rowSize = {p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Chart_BubbleChart")), null},
                new Component[]{fillColorPane, null},
                new Component[]{bubbleMean,null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("Chart_Change_Bubble_Size")),zoomTime},
                new Component[]{isMinus,null}
        };
        JPanel pane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(pane,BorderLayout.CENTER);
        return pane;
    }

    protected ChartFillStylePane getFillStylePane(){
        return null;
    }

    public void populateBean(Plot plot) {
        super.populateBean(plot);
        fillColorPane.populateBean(plot.getPlotFillStyle());
    }

    public void updateBean(Plot plot) {
        super.updateBean(plot);
        plot.setCombinedSize(Utils.string2Number(zoomTime.getText()).doubleValue());
        plot.setPlotFillStyle(fillColorPane.updateBean());
    }
}