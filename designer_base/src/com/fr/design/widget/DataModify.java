package com.fr.design.widget;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.Widget;

import javax.swing.*;

public interface DataModify<T> {

    void populateBean(T ob);

    T updateBean();

    void checkValid() throws Exception;

    DataCreatorUI dataUI();

    JComponent toSwingComponent();
}