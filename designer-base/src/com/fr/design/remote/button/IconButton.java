package com.fr.design.remote.button;

import com.fr.base.BaseUtils;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

public final class IconButton extends JButton {
    public IconButton() {
        super(StringUtils.EMPTY);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setIcon(BaseUtils.readIcon("com/fr/design/remote/images/icon_Remove_x.png"));
        setBorder(null);
    }

    @Override
    protected void paintBorder(Graphics g) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(
                new GradientPaint(
                        new Point(0, 0),
                        new Color(0xF5F5F7),
                        new Point(0, getPreferredSize().height),
                        new Color(0xF5F5F7)
                )
        );
        g2.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
        g2.dispose();

        super.paintComponent(g);
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(null);
        IconButton a = new IconButton();
        a.setBounds(0, 0, a.getPreferredSize().width, a.getPreferredSize().height);
        content.add(a);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }
}
