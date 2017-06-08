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

    public static void fireAlphaFineEnable(boolean isEnable) {
        for (AlphaFineListener l : fireAlphaFineListener) {
            l.setEnable(isEnable);
        }
    }

    /**
     * 添加一个弹出AlphaFine的监听事件
     *
     * @param l AlphaFine框弹出监听事件
     */
    public static void addAlphaFineListener(AlphaFineListener l) {
        fireAlphaFineListener.add(l);
    }

}
