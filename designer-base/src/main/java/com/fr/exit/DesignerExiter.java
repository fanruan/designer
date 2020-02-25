package com.fr.exit;

import com.fr.process.engine.core.FineProcessContext;
import com.fr.process.engine.core.FineProcessEngineEvent;
import com.fr.stable.StableUtils;


/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/12
 */
public class DesignerExiter {

    public static final DesignerExiter INSTANCE = new DesignerExiter();

    private static final String DOT = ".";

    public static DesignerExiter getInstance() {
        return INSTANCE;
    }

    public void execute() {
        if (FineProcessContext.getParentPipe() != null || !DOT.equals(StableUtils.getInstallHome())) {
            FineProcessContext.getParentPipe().fire(FineProcessEngineEvent.DESTROY);
        }
        System.exit(0);
    }
}
