package com.fr.van.chart.designer.component.background;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundQuickPane;
import com.fr.general.Inter;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Mitisky on 16/6/29.
 * 默认+颜色.+透明度
 */
public class VanChartBackgroundPaneWithOutImageAndShadow extends VanChartBackgroundPane {

    @Override
    protected JPanel initContentPanel() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        double[] columnSize = {p, f};
        double[] rowSize = { p,p,p};

        return TableLayoutHelper.createTableLayoutPane(getPaneComponents(),rowSize,columnSize);
    }

    @Override
    protected void initList() {
        paneList.add(new NullBackgroundQuickPane() {
            /**
             * 名称
             *
             * @return 名称
             */
            @Override
            public String title4PopupWindow() {
                return Inter.getLocText("Chart-Default_Name");
            }
        });
        paneList.add(new ColorBackgroundQuickPane());
    }


    @Override
    protected Component[][] getPaneComponents() {
        return new Component[][]{
                new Component[]{typeComboBox, null},
                new Component[]{centerPane, null},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Alpha")), transparent},
        };
    }

}

