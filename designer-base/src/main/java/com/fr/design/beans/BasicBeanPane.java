package com.fr.design.beans;

import com.fr.common.annotations.Open;
import com.fr.design.dialog.BasicPane;

@Open
public abstract class BasicBeanPane<T> extends BasicPane {

    /**
     * 展示数据
     * @param ob 待展示的对象
     */
    public abstract void populateBean(T ob);

    /**
     * 保存数据
     * @return 待保存的对象
     */
    public abstract T updateBean();

    /**
     * 保存数据
     * @param ob 待保存的对象
     */
    public void updateBean(T ob) {

    }

    /**
     * 更新权限工具栏面板
     */
    public void populateAuthority() {

    }
}