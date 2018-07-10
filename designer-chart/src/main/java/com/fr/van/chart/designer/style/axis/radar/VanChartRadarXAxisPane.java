package com.fr.van.chart.designer.style.axis.radar;

import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.stable.CoreConstants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.axis.VanChartBaseAxisPane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * 雷达图的x轴，即分类轴.
 */
public class VanChartRadarXAxisPane extends VanChartBaseAxisPane {

    private static final long serialVersionUID = -668494063365759428L;

    protected JPanel createContentPane(boolean isXAxis){

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] column = {f, s};
        double[] rowSize = {p,p,p,p,p,p};
        Component[][] components = new Component[][]{
                new Component[]{createLabelPane(new double[]{p, p}, column),null},
                new Component[]{createLineStylePane(new double[]{p, p, p, p}, columnSize),null},
                new Component[]{createValueStylePane(),null},
        };

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    protected Component[][] getLineStylePaneComponents() {
        return new Component[][]{
                new Component[]{null,null} ,
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_type")),axisLineStyle} ,
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")),axisLineColor},
        };
    }

    protected LineComboBox createLineComboBox() {
        return new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
    }
}