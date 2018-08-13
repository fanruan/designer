package com.fr.design.gui.ilist;

import com.fr.base.Utils;
import com.fr.design.gui.itextfield.UITextField;

import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by plough on 2017/7/23.
 */
public class UINameEdList extends JNameEdList {
//    private static final int TEST_LIST_LENTH = 20;
    private static final int BUTTON_WIDTH = 25;
//    private boolean editable = true;
//
//    // kunsnat: 是否强制ListName是数字 (int型)
//    private boolean isNameShouldNumber = false;
//
//    transient protected ListCellEditor cellEditor;
//    transient protected Component editorComp;
//    transient protected int editingIndex;
//    private PropertyChangeAdapter editingListner;
//    private java.util.List<ModNameActionListener> ll = new ArrayList<ModNameActionListener>();

    public UINameEdList(ListModel dataModel) {
        super(dataModel);
    }

    public UINameEdList(final Object[] listData) {
        super(listData);
    }

    public UINameEdList(final Vector<?> listData) {
        super(listData);
    }

    public UINameEdList() {
        super();
    }

    public Object getType(int index) {
        Nameable nameable = ((ListModelElement) getModel().getElementAt(index)).wrapper;
        if (nameable != null && nameable instanceof NameObject) {
            return ((NameObject) nameable).getObject();
        }
        return null;
    }

    public int getIconWidth() {
        return BUTTON_WIDTH;
    }

    @Override
    public int locationToIndex(Point location) {
        int index = super.locationToIndex(location);
        if (index != -1 && !getCellBounds(index, index).contains(location)) {
            return -1;
        }
        else {
            return index;
        }
    }

    /**
     * 主函数
     *
     * @param args 参数
     */

}