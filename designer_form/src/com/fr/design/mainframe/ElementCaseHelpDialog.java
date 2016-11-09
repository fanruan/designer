package com.fr.design.mainframe;

import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * @author zack
 * @date 2016-10-14
 * @since 8.0
 */
public class ElementCaseHelpDialog extends UIDialog {

    private static final int OUTER_WIDTH = 190;
    private static final int OUTER_HEIGHT = 280;


    private String helpMsg;
    private UIScrollPane helpArea;


    public ElementCaseHelpDialog(Frame parent, String helpMsg) {
        super(parent);
        this.helpMsg = helpMsg;
        initHelpArea();
        JPanel panel = (JPanel) getContentPane();
        initComponents(panel);
        setSize(new Dimension(OUTER_WIDTH, OUTER_HEIGHT));
    }

    private void initHelpArea() {
        UITextArea textArea = new UITextArea(helpMsg);
        textArea.setEditable(false);
        textArea.setBorder(null);
        helpArea = new UIScrollPane(textArea);
        helpArea.setBounds(0, 0, OUTER_WIDTH, OUTER_HEIGHT);
        helpArea.setBorder(null);
    }

    private void initComponents(JPanel contentPane) {
        contentPane.setLayout(new BorderLayout());
        add(helpArea, BorderLayout.CENTER);
        this.applyClosingAction();
        this.setTitle(Inter.getLocText("FR-Designer_Help"));
    }

    /**
     * 打开帮助框
     */
    public void showWindow() {
        this.setResizable(false);
        setVisible(true);
    }

    /**
     * 略
     */
    @Override
    public void checkValid() throws Exception {

    }

    public void setLocationRelativeTo(JFrame c, int x, int y) {
        int dx = 0, dy = 0;
        Point compLocation = c.getLocationOnScreen();//获取设计器Jframe坐标作为相对位置原点
        setLocation(dx + x, dy + y);
        dx = compLocation.x;
        dy = compLocation.y + c.getRootPane().getY();//加上底层容器的y坐标(其实就是设计器最上方图标栏的高度)
        setLocation(dx + x, dy + y);
    }
}