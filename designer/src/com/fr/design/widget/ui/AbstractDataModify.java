package com.fr.design.widget.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.widget.DataModify;

import javax.swing.*;

/**
 * Created by richie on 15/11/16.
 */
public abstract class AbstractDataModify<T> extends BasicBeanPane<T> implements DataModify<T> {

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }

    @Override
    public JComponent toSwingComponent() {
        return this;
    }
}