package com.fr.design.foldablepane;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


/**
 * Created by MoMeak on 2017/7/5.
 */
public class UIExpandablePane  extends JPanel {
    private static final long serialVersionUID = 1L;
    private HeaderPane headerPanel;
    private JPanel contentPanel;
    private Color color = Color.black;
    private String title;
    private int headWidth;
    private int headHeight;


    public UIExpandablePane(String title,int headWidth,int headHeight,JPanel contentPanel)
    {
        super();
        this.title = title;
        this.headWidth = headWidth;
        this.headHeight = headHeight;
        this.contentPanel = contentPanel;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        headerPanel = new HeaderPane(color, title,headWidth,headHeight);
        headerPanel.addMouseListener(new PanelAction());
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        setOpaque(false);
    }

    class PanelAction extends MouseAdapter
    {
        public void mousePressed(MouseEvent e)
        {
            HeaderPane hp = (HeaderPane)e.getSource();
            if(contentPanel.isShowing())
            {
                contentPanel.setVisible(false);
                hp.setShow(false);
            }
            else
            {
                contentPanel.setVisible(true);
                hp.setShow(true);
            }
            hp.getParent().validate();
            hp.getParent().repaint();
        }
    }


    public static void main(String[] args)
    {
//        JFrame jf = new JFrame("test");
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel content = (JPanel) jf.getContentPane();
//        content.setLayout(new BorderLayout());
//
//        JPanel myPanel = new JPanel();
//        myPanel.setLayout(new BorderLayout());
//        JPanel Panel = new JPanel();
//        Panel.setBackground(Color.blue);
//        myPanel.add(new UIExpandablePane("基本",280,25,Panel),BorderLayout.CENTER);
////        myPanel.setLayout(new GridBagLayout());
////        myPanel.add(new JExpandablePanel());
////        GridBagConstraints gbc = new GridBagConstraints();
////        JPanel[] panels = new JPanel[4]; //
////        gbc.insets = new Insets(1,3,0,3);
////        gbc.weightx = 1.0;
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////        gbc.gridwidth = GridBagConstraints.REMAINDER;
////        for(int j = 0; j < panels.length; j++)
////        {
////            panels[j] = new JExpandablePanel();
////            myPanel.add(panels[j], gbc);
////        }
//        content.add(myPanel, BorderLayout.CENTER);
//        GUICoreUtils.centerWindow(jf);
//        jf.setSize(280, 400);
//        jf.setVisible(true);
    }

}
