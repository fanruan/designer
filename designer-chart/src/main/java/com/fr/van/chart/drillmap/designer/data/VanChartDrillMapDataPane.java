package com.fr.van.chart.drillmap.designer.data;

import com.fr.chart.chartattr.ChartCollection;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.mainframe.chart.gui.ChartDataPane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by Mitisky on 16/6/20.
 */
public class VanChartDrillMapDataPane extends ChartDataPane {

    public VanChartDrillMapDataPane(AttributeChangeListener listener) {
        super(listener);
    }

    @Override
    protected JPanel createContentPane() {
        contentsPane = new VanChartDrillMapContentsPane(listener, VanChartDrillMapDataPane.this);
        return contentsPane;
    }

    //编辑内置数据集会stateChange,会调用这里
    @Override
    protected void repeatLayout(ChartCollection collection) {
        if (contentsPane != null) {
            this.remove(contentsPane);
        }
        this.setLayout(new BorderLayout(0, 0));

        contentsPane = new VanChartDrillMapContentsPane(listener, VanChartDrillMapDataPane.this);
        contentsPane.setSupportCellData(isSupportCellData());

        this.add(contentsPane, BorderLayout.CENTER);
    }

}
