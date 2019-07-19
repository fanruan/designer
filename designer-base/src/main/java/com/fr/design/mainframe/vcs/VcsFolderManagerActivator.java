package com.fr.design.mainframe.vcs;

import com.fr.module.Activator;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;

public class VcsFolderManagerActivator extends Activator {
    @Override
    public void start() {
        WorkContext.getCurrent().get(VcsOperator.class).moveVscFolder();
    }

    @Override
    public void stop() {

    }
}
