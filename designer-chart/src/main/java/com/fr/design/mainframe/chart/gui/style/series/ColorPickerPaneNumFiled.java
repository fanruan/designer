package com.fr.design.mainframe.chart.gui.style.series;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.module.ModuleContext;
import com.fr.value.ClearableLazyValue;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-8
 * Time: 上午9:18
 * To change this template use File | Settings | File Templates.
 */
public class ColorPickerPaneNumFiled extends UINumberField {
    private ClearableLazyValue<ScheduledExecutorService> ses = new ClearableLazyValue<ScheduledExecutorService>() {
        @NotNull
        @Override
        protected ScheduledExecutorService compute() {
            return ModuleContext.getExecutor()
                    .newSingleThreadScheduledExecutor(new NamedThreadFactory("FormatePaneNumFieldRunChange"));
        }
    };

    public ColorPickerPaneNumFiled() {
        super();
    }

    @Override
    protected void attributeChange() {
        ses.getValue().schedule(new Runnable() {
            @Override
            public void run() {
                // kuns: 默认修改500, 在地图修改系列颜色text时, 快速响应.
                runChange();
            }
        }, 500, TimeUnit.MILLISECONDS);
    }

    protected void runChange() {
        super.attributeChange();
        ses.getValue().shutdown();
        ses.drop();
    }


}