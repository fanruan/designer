package com.fr.design.gui.ilist;

import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import javax.swing.ListModel;
import java.awt.Point;

/**
 * Created by plough on 2017/7/23.
 */
public class UINameEdList extends JNameEdList {
    private static final int BUTTON_WIDTH = 25;

    protected UINameEdList(ListModel dataModel) {
        super(dataModel);
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
}