package com.fr.design.gui.imenu;

import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: richie
 * Date: 13-12-5
 * Time: 上午11:12
 */
public class UIScrollMenuDemo extends JFrame {
    public UIScrollMenuDemo() {
        UIMenuBar menuBar = new UIMenuBar();

        // File Menu, F - Mnemonic
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        // File->New, N - Mnemonic
        UIMenuItem newMenuItem = new UIMenuItem("New", KeyEvent.VK_N);
        fileMenu.add(newMenuItem);

        // Edit->Options Submenu, O - Mnemonic, at.gif - Icon Image File
        UIMenu findOptionsMenu = new UIScrollMenu("Options");
        Icon atIcon = new ImageIcon("at.gif");
        findOptionsMenu.setIcon(atIcon);
        findOptionsMenu.setMnemonic(KeyEvent.VK_O);
        fileMenu.add(findOptionsMenu);

        // ButtonGroup for radio buttons
        ButtonGroup directionGroup = new ButtonGroup();

        // Edit->Options->Forward, F - Mnemonic, in group
        JRadioButtonMenuItem forwardMenuItem = new JRadioButtonMenuItem("Forward", true);
        forwardMenuItem.setMnemonic(KeyEvent.VK_F);
        findOptionsMenu.add(forwardMenuItem);
        directionGroup.add(forwardMenuItem);

        // Edit->Options->Backward, B - Mnemonic, in group
        JRadioButtonMenuItem backwardMenuItem = new JRadioButtonMenuItem("Backward");
        backwardMenuItem.setMnemonic(KeyEvent.VK_B);
        findOptionsMenu.add(backwardMenuItem);
        directionGroup.add(backwardMenuItem);

        for (int i = 0; i < 100; i ++) {
            JRadioButtonMenuItem m = new JRadioButtonMenuItem("Backward" + i);
            m.setMnemonic(KeyEvent.VK_B);
            findOptionsMenu.add(m);
            directionGroup.add(m);
        }


        setJMenuBar(menuBar);

        setSize(400, 400);
        GUICoreUtils.centerWindow(this);
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UIScrollMenuDemo().setVisible(true);
            }
        });
    }
}