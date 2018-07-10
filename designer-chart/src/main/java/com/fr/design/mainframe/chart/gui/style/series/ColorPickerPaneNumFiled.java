package com.fr.design.mainframe.chart.gui.style.series;

import java.util.Timer;
import java.util.TimerTask;

import com.fr.design.gui.itextfield.UINumberField;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-8
 * Time: 上午9:18
 * To change this template use File | Settings | File Templates.
 */
public class ColorPickerPaneNumFiled extends UINumberField {
    private Timer timer;
    public ColorPickerPaneNumFiled() {
        super();
    }

    protected void attributeChange() {
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runChange();
            }
        },500);// kuns: 默认修改500, 在地图修改系列颜色text时, 快速响应.

    }

    protected void runChange(){
        super.attributeChange();
        timer.cancel();
    }


}