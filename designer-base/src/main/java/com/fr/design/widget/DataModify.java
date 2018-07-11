package com.fr.design.widget;

import com.fr.design.data.DataCreatorUI;

import javax.swing.JComponent;


public interface DataModify<T> {

    void populateBean(T ob);

    T updateBean();

    void checkValid() throws Exception;

    DataCreatorUI dataUI();

    JComponent toSwingComponent();

    String getGlobalName();

    void setGlobalName(String globalName);

}