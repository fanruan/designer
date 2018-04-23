package com.fr.design.gui.style;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.itextfield.UITextField;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-16
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
public class FormatePaneNumField extends UITextField {
    private Timer timer;
    public FormatePaneNumField() {
        super();
    }

    protected void attributeChange() {
        if (!AbstractAttrNoScrollPane.isHasChangeListener()) {
            return;
        }
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runChange();
            }
        },100);

    }

    protected void runChange(){
        super.attributeChange();
        timer.cancel();

    }
}