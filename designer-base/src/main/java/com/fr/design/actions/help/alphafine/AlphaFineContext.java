package com.fr.design.actions.help.alphafine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaXiang on 2017/5/27.
 */
public class AlphaFineContext {
    private static List<AlphaFineListener> fireAlphaFineListener = new ArrayList<AlphaFineListener>();

    /**
     * 触发AlphaFine弹窗
     */
    public static void fireAlphaFineShowDialog() {
        for (AlphaFineListener l : fireAlphaFineListener) {
            l.showDialog();
        }
    }

    /**
     * 触发开启或关闭AlphaFine功能
     * @param isEnable
     */
    public static void fireAlphaFineEnable(boolean isEnable) {
        for (AlphaFineListener l : fireAlphaFineListener) {
            l.setEnable(isEnable);
        }
    }

    /**
     * 添加一个AlphaFine的监听事件
     *
     * @param l AlphaFine监听事件
     */
    public static void addAlphaFineListener(AlphaFineListener l) {
        fireAlphaFineListener.add(l);
    }

}
