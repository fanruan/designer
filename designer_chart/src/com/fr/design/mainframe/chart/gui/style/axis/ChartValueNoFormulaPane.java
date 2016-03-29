package com.fr.design.mainframe.chart.gui.style.axis;

import com.fr.chart.chartattr.Axis;

import javax.swing.*;

/**
 * Created by eason on 14-10-23.
 */
public class ChartValueNoFormulaPane extends ChartValuePane{

    protected JPanel getAxisTitlePane(){
        return this.axisTitleNoFormulaPane;
    }

    protected void updateAxisTitle(Axis axis){
        this.axisTitleNoFormulaPane.update(axis);
    }

    protected void populateAxisTitle(Axis axis){
        this.axisTitleNoFormulaPane.populate(axis);
    }

}