package com.fr.design.gui.controlpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilist.ListModelElement;

/**
 * Created by plough on 2018/8/13.
 */
public interface ListControlPaneProvider {
    NameableCreator[] creators();
    ListModelElement getSelectedElement();
    BasicBeanPane createPaneByCreators(NameableCreator creator);
    BasicBeanPane createPaneByCreators(NameableCreator creator, String string);
}
