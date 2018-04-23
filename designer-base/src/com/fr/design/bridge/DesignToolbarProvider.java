package com.fr.design.bridge;

/**
 * @author richie
 * @date 14/11/10
 * @since 8.0
 * 工具栏界面接口
 */
public interface DesignToolbarProvider {

    public static final String STRING_MARKED = "DesignToolbarProvider";

    /**
     * 刷新工具栏
     */
    public void refreshToolbar();
}