package com.fr.start;

import com.fr.design.constants.DesignerLaunchStatus;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.ui.util.UIUtil;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;

/**
 * Created by juhaoyu on 2019-06-14.
 * 设计器上下文
 */
public class DesignerInitial {

    private static volatile Designer designer;

    public static void init(final String... args) {
        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                designer = new Designer(args);
            }
        });
    }

    public static void prepare() {
        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                if (designer != null) {
                    designer.show();
                }
            }
        });
        EventDispatcher.listen(DesignerLaunchStatus.OPEN_LAST_FILE_COMPLETE, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                EventDispatcher.stopListen(this);
                UIUtil.invokeLaterIfNeeded(new Runnable() {
                    @Override
                    public void run() {
                        DesignerContext.getDesignerFrame().setVisible(true);
                        DesignerContext.getDesignerFrame().resizeFrame();
                        //启动画面结束
                        SplashContext.getInstance().hide();
                    }
                });
                DesignerLaunchStatus.setStatus(DesignerLaunchStatus.STARTUP_COMPLETE);
            }
        });
    }
}
