package com.fr.design.beans;

import com.fr.common.annotations.Open;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019/9/24
 * 数据存取视图界面
 */
@Open
public abstract class BasicStorePane<T> extends BasicBeanPane<T> {

    @Override
    public T updateBean() {
        return null;
    }

    public abstract void populateBean(T t);

    @Override
    public abstract void updateBean(T t);
}
