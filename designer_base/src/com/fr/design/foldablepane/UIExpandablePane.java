package com.fr.design.foldablepane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Created by MoMeak on 2017/7/5.
 */
public class UIExpandablePane extends JPanel {

    private static final int LEFT_BORDER = 5;
    private static final long serialVersionUID = 1L;
    private HeaderPane headerPanel;
    private JPanel contentPanel;
    private Color color = Color.black;
    private String title;
    private int headWidth;
    private int headHeight;

    public JPanel getContentPanel() {
        return contentPanel;
    }

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
        setcontentPanelontentPanelBorder ();
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        setOpaque(false);
    }

    protected void setcontentPanelontentPanelBorder (){
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0 ,LEFT_BORDER, 0, 0));

    }

    class PanelAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            HeaderPane hp = (HeaderPane) e.getSource();
            if (contentPanel.isShowing()) {
                contentPanel.setVisible(false);
                hp.setShow(false);
            } else {
                contentPanel.setVisible(true);
                hp.setShow(true);
            }
            hp.setPressed(false);
            hp.getParent().validate();
            hp.getParent().repaint();
        }

        public void mousePressed(MouseEvent e) {
            HeaderPane hp = (HeaderPane) e.getSource();
            hp.setPressed(true);
            hp.getParent().repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            HeaderPane hp = (HeaderPane) e.getSource();
            hp.setPressed(false);
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
