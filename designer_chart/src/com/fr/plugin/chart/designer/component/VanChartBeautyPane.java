package com.fr.plugin.chart.designer.component;

import com.fr.chart.base.ChartConstants;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Mitisky on 15/9/8.
 */
//系列-风格
public class VanChartBeautyPane extends BasicBeanPane<Integer> {
    private UIComboBox styleBox;

    public VanChartBeautyPane() {
        styleBox = new UIComboBox(getNameArray());

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { f };
        double[] rowSize = { p};
        Component[][] components = new Component[][]{
                new Component[]{styleBox},
        } ;
        JPanel panel = TableLayoutHelper.createTableLayoutPane4Chart(new String[]{"Plugin-Chart_Style"}, components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    protected String[] getNameArray(){
        return new String[]{Inter.getLocText("Chart-Default_Name"),
                Inter.getLocText("Plugin-Chart_TopDownShade")
        };
    }
    @Override
    public void populateBean(Integer ob) {
        int finalIndex;
        switch (ob){
            case ChartConstants.STYLE_NONE: finalIndex = 0; break;
            case ChartConstants.STYLE_SHADE: finalIndex = 1; break;
            default: finalIndex = 0;
        }
        styleBox.setSelectedIndex(finalIndex);
    }

    @Override
    public Integer updateBean() {
        int index =  styleBox.getSelectedIndex();
        int style;
        switch (index){
            case 0: style = ChartConstants.STYLE_NONE; break;
            case 1: style = ChartConstants.STYLE_SHADE; break;
            default: style = ChartConstants.STYLE_NONE;
        }
        return style;
    }

    @Override
    protected String title4PopupWindow() {
        return "";
    }

}