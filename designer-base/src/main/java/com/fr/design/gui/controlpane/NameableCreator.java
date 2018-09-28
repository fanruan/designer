package com.fr.design.gui.controlpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.js.JavaScript;
import com.fr.stable.Nameable;

import javax.swing.Icon;

public interface NameableCreator {
    String menuName();

    Icon menuIcon();

    String createTooltip();

    Nameable createNameable(UnrepeatedNameHelper helper);

    Class<? extends BasicBeanPane> getUpdatePane();

    Object acceptObject2Populate(Object ob);

    void saveUpdatedBean(ListModelElement wrapper, Object bean);

    Class<? extends JavaScript> getHyperlink();


    boolean isNeedParameterWhenPopulateJControlPane();
}