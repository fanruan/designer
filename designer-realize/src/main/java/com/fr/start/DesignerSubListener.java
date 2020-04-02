package com.fr.start;

import com.fr.design.mainframe.DesignerContext;
import com.fr.event.Event;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.process.engine.core.CarryMessageEvent;
import com.fr.process.engine.core.FineProcessContext;
import com.fr.process.engine.core.FineProcessEngineEvent;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/21
 */
public class DesignerSubListener {

    public static DesignerSubListener INSTANCE = new DesignerSubListener();

    public static DesignerSubListener getInstance() {
        return INSTANCE;
    }

    private DesignerSubListener() {

    }

    public void start() {
        if (FineProcessContext.getParentPipe() != null) {
            FineProcessContext.getParentPipe().listen(FineProcessEngineEvent.READY, new Listener<Null>() {
                @Override
                public void on(Event event, Null param) {
                    if (DesignerContext.getDesignerFrame() == null || !DesignerContext.getDesignerFrame().isShowing()) {
                        FineProcessContext.getParentPipe().fire(new CarryMessageEvent(DesignerProcessType.INSTANCE.obtain()));
                    }
                }
            });
        }
    }
}
