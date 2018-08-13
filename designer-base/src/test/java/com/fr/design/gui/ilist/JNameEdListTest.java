package com.fr.design.gui.ilist;

import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by plough on 2018/8/13.
 */
public class JNameEdListTest {
    private static final int TEST_LIST_LENTH = 20;

    public static void main(String... args) {
        JFrame f = new JFrame();
        JPanel c = (JPanel) f.getContentPane();
        c.setLayout(new BorderLayout());
        ListModelElement[] data = new ListModelElement[TEST_LIST_LENTH];
        for (int i = 0; i < TEST_LIST_LENTH; i++) {
            data[i] = new ListModelElement(new NameObject(i + 1 + "", i));
        }
        final JNameEdList list = new JNameEdList(data);
        list.setEditable(true);
        list.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent evt) {
                list.stopEditing();
                if (evt.getClickCount() >= 2
                        && SwingUtilities.isLeftMouseButton(evt)) {
                    list.editItemAt(list.getSelectedIndex());
                }
            }
        })
        ;

        list.setCellEditor(new DefaultListCellEditor(new UITextField()));
        list.setCellRenderer(new NameableListCellRenderer());
        c.add(list, BorderLayout.CENTER);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(400, 600);
        f.setVisible(true);
    }

    private static class NameableListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Nameable) {
                Nameable wrappee = (Nameable) value;
                this.setText(wrappee.getName());
            }
            return this;
        }
    }
}
