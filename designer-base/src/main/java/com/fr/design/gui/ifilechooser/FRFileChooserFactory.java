package com.fr.design.gui.ifilechooser;

import com.fr.design.gui.ifilechooser.javafx.UINativeFileChooser;

import javax.swing.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/4/7
 */
public class FRFileChooserFactory {

    /**
     * 默认提供原生风格
     *
     * @return 文件选择器
     */
    public static JFileChooser createFileChooser() {
        if (UINativeFileChooser.isAvailable()) {
            return new UINativeFileChooser();
        } else {
            return new JFileChooser();
        }
    }

}
