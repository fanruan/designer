package com.fr.design.mainframe.chart.gui.type;

import com.fr.chart.chartattr.Chart;

/**
 * Created by mengao on 2017/8/30.
 * 不能删掉这个类，echarts插件中用到
 */
public abstract class UserDefinedChartTypePane extends AbstractChartTypePane {
    protected String[] getTypeLayoutPath() {
        return new String[0];
    }

    protected String[] getTypeLayoutTipName(){
        return new String[0];
    }

    protected String[] getTypeIconPath(){
        return new String[]{"/com/fr/design/images/chart/default.png"};
    }

    protected  String[] getTypeTipName() {
        return new String[]{title4PopupWindow()};
    }

    public void updateBean(Chart chart) {

    }

    public void populateBean(Chart chart){
        typeDemo.get(0).isPressing = true;
        checkDemosBackground();
    }

    /**
     * 弹出界面的标题
     * @return 标题
     */
    public String title4PopupWindow(){
        return "";
    }
}
