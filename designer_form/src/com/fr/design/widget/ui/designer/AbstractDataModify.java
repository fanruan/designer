package com.fr.design.widget.ui.designer;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.widget.DataModify;

import javax.swing.JComponent;


/**
 * Created by kerry on 17/07/28.
 */
public abstract class AbstractDataModify<T> extends BasicBeanPane<T> implements DataModify<T> {
    protected XCreator creator;
    protected String globalName;


    public AbstractDataModify(XCreator xCreator){
        this.creator = xCreator;
    }

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }

    @Override
    public JComponent toSwingComponent() {
        return this;
    }

    public void setGlobalName(String globalName){
        this.globalName = globalName;
    }

    public String getGlobalName(){
        return globalName;
    }
}