package com.fr.design.data;

import com.fr.design.data.datapane.TableDataTree;
import com.fr.design.data.tabledata.tabledatapane.AbstractTableDataPane;
import com.fr.design.mainframe.DockingView;

import javax.swing.*;

/**
 * Coder: zack
 * Date: 2016/4/22
 * Time: 16:23
 */
public class BasicTableDataTreePane extends DockingView{
    @Override
    public void refreshDockingView() {

    }

    @Override
    public String getViewTitle() {
        return null;
    }

    @Override
    public Icon getViewIcon() {
        return null;
    }

    @Override
    public Location preferredLocation() {
        return null;
    }

    public void dgEdit(final AbstractTableDataPane<?> uPanel, String originalName) {
    }

    public TableDataTree getDataTree() {
        return null;
    }
}
