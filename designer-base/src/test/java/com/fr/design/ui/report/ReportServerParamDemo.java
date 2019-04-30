package com.fr.design.ui.report;

import com.fr.design.DesignerEnvManager;
import com.fr.design.ui.ModernUIPane;
import com.fr.design.ui.ModernUIPaneTest;

import javax.swing.*;
import java.awt.*;

/**
 * Created by windy on 2019/3/25.
 * 报表服务器参数demo
 */
public class ReportServerParamDemo {

    public static void main(String... args) {
        final JFrame frame = new JFrame();
        frame.setSize(660, 600);
        JPanel contentPane = (JPanel) frame.getContentPane();
        // 是否需要开启调试窗口
        DesignerEnvManager.getEnvManager().setOpenDebug(true);

        final ModernUIPane<ModernUIPaneTest.Model> pane = new ModernUIPane.Builder<ModernUIPaneTest.Model>()
                .withComponent(ReportServerParamComponent.KEY).build();
        contentPane.add(pane, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
