package com.fr.design.mainframe.alphafine;//package com.fr.design.mainframe.alphafine;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.image.ImageObserver;
//import java.util.Vector;
//
///**
// * Created by XiaXiang on 2017/5/10.
// */
//public class Main {
//    public static void main(String args[]) {
//        JFrame frame = new JFrame("JList Background Demonstration");
//
//        JList list = new JList();
//
//        String [] imageIcon = new String[] {
//                "female.gif", "male.gif"
//        };
//
//        // create model
//        Vector v = new Vector();
//        for (int i=0; i<10; i++) {
//            ImageIcon ii = new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/loading.gif"));
//            ii.setImageObserver(new AnimatedObserver(list, i));
//            v.addElement(ii);
//        }
//
//        list.setListData(v);
//
//        frame.getContentPane().add(BorderLayout.CENTER, list); //new JScrollPane(list));
//        frame.setDefaultCloseOperation(3);
//        frame.pack();
//        frame.setVisible(true);
//    }
//}
//
//class AnimatedObserver implements ImageObserver
//{
//    JList list;
//    int index;
//
//    public AnimatedObserver(JList list, int index) {
//        this.list = list;
//        this.index = index;
//    }
//
//    public boolean imageUpdate (Image img, int infoflags, int x, int y, int width, int height) {
//        if ((infoflags & (FRAMEBITS|ALLBITS)) != 0) {
//            Rectangle rect = list.getCellBounds(index, index);
//            list.repaint(rect);
//        }
//
//        return (infoflags & (ALLBITS|ABORT)) == 0;
//    }
//}
