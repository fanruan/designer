package com.fr.design.gui.style;

import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.module.ModuleContext;
import com.fr.value.ClearableLazyValue;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: pony
 * Date: 13-4-16
 * Time: 下午4:40
 * To change this template use File | Settings | File Templates.
 */
public class FormatePaneNumField extends UITextField {
    private ClearableLazyValue<ScheduledExecutorService> ses = new ClearableLazyValue<ScheduledExecutorService>() {
        @NotNull
        @Override
        protected ScheduledExecutorService compute() {
            return ModuleContext.getExecutor()
                    .newSingleThreadScheduledExecutor(new NamedThreadFactory("FormatePaneNumFieldRunChange"));
        }
    };

    public FormatePaneNumField() {
        super();
    }

    @Override
    protected void attributeChange() {
        if (!AbstractAttrNoScrollPane.isHasChangeListener()) {
            return;
        }
        ses.getValue().schedule(new Runnable() {
            @Override
            public void run() {
                runChange();
            }
        }, 100, TimeUnit.MILLISECONDS);
    }

    protected void runChange() {
        super.attributeChange();
        ses.getValue().shutdown();
        ses.drop();
    }
}