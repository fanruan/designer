package com.fr.van.chart.designer.component;

import com.fr.chart.base.ChartConstants;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;

import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by Mitisky on 15/9/8.
 */
//系列-风格
public class VanChartBeautyPane extends BasicBeanPane<Integer> {
    private UIButtonGroup styleBox;

    public VanChartBeautyPane() {
        styleBox = new UIButtonGroup(getNameArray());

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Gradient_Style")), styleBox},
        } ;
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);
    }

    protected String[] getNameArray(){
        return new String[]{Toolkit.i18nText("Fine-Design_Chart_On"),
                Toolkit.i18nText("Fine-Design_Chart_Off")
        };
    }
    @Override
    public void populateBean(Integer ob) {
        int finalIndex;
        switch (ob){
            case ChartConstants.STYLE_NONE: finalIndex = 1; break;
            case ChartConstants.STYLE_SHADE: finalIndex = 0; break;
            default: finalIndex = 1;
        }
        styleBox.setSelectedIndex(finalIndex);
    }

    @Override
    public Integer updateBean() {
        int index =  styleBox.getSelectedIndex();
        int style;
        switch (index){
            case 0: style = ChartConstants.STYLE_SHADE; break;
            case 1: style = ChartConstants.STYLE_NONE; break;
            default: style = ChartConstants.STYLE_NONE;
        }
        return style;
    }

    @Override
    protected String title4PopupWindow() {
        return "";
    }

}