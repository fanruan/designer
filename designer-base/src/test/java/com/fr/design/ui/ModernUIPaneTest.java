package com.fr.design.ui;

import com.fr.design.DesignerEnvManager;
import com.fr.general.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-03-05
 */
public class ModernUIPaneTest {

    public static void main(String... args) {
        final JFrame frame = new JFrame();
        frame.setSize(1200, 800);
        JPanel contentPane = (JPanel) frame.getContentPane();
        // 是否需要开启调试窗口
        DesignerEnvManager.getEnvManager().setOpenDebug(true);
        final ModernUIPane<Model> pane = new ModernUIPane.Builder<Model>()
                .withEMB("/com/fr/design/ui/demo.html").namespace("Pool").build();
        contentPane.add(pane, BorderLayout.CENTER);

        Model model = new Model();
        model.setAge(20);
        model.setName("Pick");
        pane.populate(model);

        JPanel panel = new JPanel(new FlowLayout());
        contentPane.add(panel, BorderLayout.SOUTH);
        JButton button = new JButton("点击我可以看到Swing的弹框，输出填写的信息");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Model returnValue = pane.update();
                if (returnValue != null) {
                    JOptionPane.showMessageDialog(frame, String.format("姓名为:%s,年龄为:%d", returnValue.getName(), returnValue.getAge()));
                }
            }
        });
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static class Model {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public void print(String message) {
            System.out.println(message);
        }

    }

}