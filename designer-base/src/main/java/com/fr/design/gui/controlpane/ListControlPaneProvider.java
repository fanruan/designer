package com.fr.design.gui.controlpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ilist.JNameEdList;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.stable.Nameable;

import javax.swing.DefaultListModel;

/**
 * Created by plough on 2018/8/13.
 */
public interface ListControlPaneProvider extends UnrepeatedNameHelper {
    NameableCreator[] creators();
    BasicBeanPane createPaneByCreators(NameableCreator creator);
    BasicBeanPane createPaneByCreators(NameableCreator creator, String string);
    DefaultListModel getModel();
    boolean hasInvalid(boolean isAdd);
    void addNameable(Nameable nameable, int index);
    JNameEdList getNameableList();
    int getSelectedIndex();
    void setSelectedIndex(int idx);
    ListModelElement getSelectedValue();
    void checkButtonEnabled();
    JControlUpdatePane getControlUpdatePane();
//    BasicBeanPane[] getUpdatePanes();
    /**
     * 检查是否符合规范
     * @throws Exception
     */
    void checkValid() throws Exception;
    void showSelectPane();
    void showEditPane();
    ShortCut4JControlPane[] getShorts();
}
