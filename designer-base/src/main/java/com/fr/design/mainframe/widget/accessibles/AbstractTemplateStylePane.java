package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.dialog.BasicPane;

public abstract class AbstractTemplateStylePane<T> extends BasicPane {

    public abstract void populate(T ob);

    public abstract T update();
}
