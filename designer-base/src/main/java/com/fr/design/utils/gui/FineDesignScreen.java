package com.fr.design.utils.gui;

import com.fr.stable.os.OperatingSystem;

import java.awt.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/10/29
 */
public enum FineDesignScreen {

    /**
     * 一个临界dpi
     * 设计器(windows/linux)大于等于该dpi界面ui变得很小 (jdk bug 在jdk9修复: http://openjdk.java.net/jeps/263)
     */
    DPI_144(144);

    private int value;

    FineDesignScreen(int value) {
        this.value = value;
    }

    /**
     * 判断win/linux下的dpi macos不做处理
     * @return
     */
    public static boolean isHighDPI() {
        if (OperatingSystem.isMacos()) {
            return false;
        }
        return Toolkit.getDefaultToolkit().getScreenResolution() >= DPI_144.value;
    }
}
