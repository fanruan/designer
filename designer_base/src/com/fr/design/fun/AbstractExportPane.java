package com.fr.design.fun;

import com.fr.design.dialog.BasicPane;

/**
 * Created by vito on 16/5/5.
 */
public abstract class AbstractExportPane extends BasicPane {

    public abstract void populate(Object t);

    public abstract Object update(Object t);

    public <T> T update(Object t,Class<? extends T> clazz){
        Object object = this.update(t);
        if(object == null){
            return null;
        }
        if(object.getClass().isAssignableFrom(clazz)){
            return clazz.cast(object);
        }
        return null;
    }
}
