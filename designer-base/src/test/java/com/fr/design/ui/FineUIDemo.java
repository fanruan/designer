package com.fr.design.ui;

import com.fr.design.DesignerEnvManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-07
 */
public class FineUIDemo {

    public static void main(String... args) {
        final JFrame frame = new JFrame();
        frame.setSize(1200, 800);
        JPanel contentPane = (JPanel) frame.getContentPane();
        // 是否需要开启调试窗口
        DesignerEnvManager.getEnvManager().setOpenDebug(true);

        final ModernUIPane<ModernUIPaneTest.Model> pane = new ModernUIPane.Builder<ModernUIPaneTest.Model>()
                .withComponent(StartComponent.KEY).namespace("Pool").build();
        contentPane.add(pane, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
