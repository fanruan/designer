package com.fr.exit;

import com.fr.design.env.DesignerWorkspaceGenerator;
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

    public static DesignerExiter getInstance() {
        return INSTANCE;
    }

    public void execute() {
        beforeExit();
        if (FineProcessContext.getParentPipe() != null) {
            FineProcessContext.getParentPipe().syncFire(FineProcessEngineEvent.DESTROY);
        }
        System.exit(0);
    }

    private void beforeExit() {
        DesignerWorkspaceGenerator.stop();
    }
}
