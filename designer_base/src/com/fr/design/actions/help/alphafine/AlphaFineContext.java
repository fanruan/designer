package com.fr.design.actions.help.alphafine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaXiang on 2017/5/27.
 */
public class AlphaFineContext {
    private static List<AlphaFineListener> fireLoginContextListener = new ArrayList<AlphaFineListener>();

    /**
     * 触发AlphaFine弹窗
     */
    public static void fireAlphaFineContextListener() {
        for (AlphaFineListener l : fireLoginContextListener) {
            l.showDialog();
        }
    }

    /**
     * 添加一个弹出AlphaFine的监听事件
     *
     * @param l AlphaFine框弹出监听事件
     */
    public static void addAlphafineContextListener(AlphaFineListener l) {
        fireLoginContextListener.add(l);
    }
}
