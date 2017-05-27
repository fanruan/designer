package com.fr.design.actions.help.alphafine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiaXiang on 2017/5/27.
 */
public class AlphafineContext {
    private static List<AlphafineListener> fireLoginContextListener = new ArrayList<AlphafineListener>();

    /**
     * 触发AlphaFine弹窗
     */
    public static void fireAlphaFineContextListener() {
        for (AlphafineListener l : fireLoginContextListener) {
            l.showDialog();
        }
    }

    /**
     * 添加一个弹出AlphaFine的监听事件
     *
     * @param l AlphaFine框弹出监听事件
     */
    public static void addAlphafineContextListener(AlphafineListener l) {
        fireLoginContextListener.add(l);
    }
}
