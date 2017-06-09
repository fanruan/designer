package com.fr.design.actions.help.alphafine;

/**
 * Created by XiaXiang on 2017/5/27.
 * AlphaFine监听器
 */
public interface AlphaFineListener {
    /**
     * 显示dialog
     */
    void showDialog();

    /**
     * 设置可用性
     * @param isEnable
     */
    void setEnable(boolean isEnable);
}
