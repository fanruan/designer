package com.fr.design.report;

import com.fr.design.dialog.BasicPane;

/**
 * Created by vito on 16/5/5.
 */
public abstract class AbstractExportPane extends BasicPane {

    public abstract void populate(Object t);

    public abstract void update(Object t);
}
