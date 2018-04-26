package com.fr.design.dialog;

import javax.swing.JPanel;

/**
 * Created by kerry on 2017/10/25.
 */
public abstract class AttrScrollPane extends BasicScrollPane {
    private static final int OVER_WIDTH = 10;

    @Override
    public void populateBean(Object ob) {

    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    @Override
    protected int getOverWidth() {
        return OVER_WIDTH;
    }

    @Override
    protected boolean hideBarWidth() {
        return true;
    }
}
