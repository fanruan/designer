package com.fr.design.foldablepane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Created by MoMeak on 2017/7/5.
 */
public class UIExpandablePane extends JPanel {
    private static final long serialVersionUID = 1L;
    private HeaderPane headerPanel;
    private JPanel contentPanel;
    private Color color = Color.black;
    private String title;
    private int headWidth;
    private int headHeight;


    public UIExpandablePane(String title, int headWidth, int headHeight, JPanel contentPanel) {
        super();
        this.title = title;
        this.headWidth = headWidth;
        this.headHeight = headHeight;
        this.contentPanel = contentPanel;
        initComponents();
    }

    public UIExpandablePane(String title, int headHeight, JPanel contentPanel) {
        super();
        this.title = title;
        this.headHeight = headHeight;
        this.contentPanel = contentPanel;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        headerPanel = new HeaderPane(color, title, headHeight);
        headerPanel.addMouseListener(new PanelAction());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0 ,5, 0, 0));
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        setOpaque(false);
    }

    class PanelAction extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            HeaderPane hp = (HeaderPane) e.getSource();
            if (contentPanel.isShowing()) {
                contentPanel.setVisible(false);
                hp.setShow(false);
            } else {
                contentPanel.setVisible(true);
                hp.setShow(true);
            }
            hp.getParent().validate();
            hp.getParent().repaint();
        }
    }


    public static void main(String[] args) {
//        JFrame jf = new JFrame("test");
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel content = (JPanel) jf.getContentPane();
//        content.setLayout(new BorderLayout());
//        JPanel myPanel = new JPanel();
//        myPanel.setLayout(new BorderLayout());
//        JPanel Panel = new JPanel();
//        Panel.setBackground(Color.blue);
//        myPanel.add(new UIExpandablePane("基本", 223, 24, Panel), BorderLayout.CENTER);
//        content.add(myPanel, BorderLayout.CENTER);
//        GUICoreUtils.centerWindow(jf);
//        jf.setSize(439, 400);
//        jf.setVisible(true);
    }

}
