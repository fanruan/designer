package com.fr.design.widget.component;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.editor.editor.*;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/8.
 */
public class DateValuePane extends JPanel {
    private UIHeadGroup widgetValueHead;
    private Editor[] editor;


    public DateValuePane() {
        editor = new Editor[]{
                new NoneEditor(null, Inter.getLocText("None")),
                new DateEditor(true, Inter.getLocText("Date")),
                new FormulaEditor(Inter.getLocText("Parameter-Formula"))
        };
        this.setLayout(new BorderLayout(0, LayoutConstants.VGAP_SMALL));
        final CardLayout cardLayout = new CardLayout();
        final JPanel customPane = new JPanel(cardLayout);
        final String[] tabTitles = new String[editor.length];
        for (int i = 0; i < editor.length; i++) {
            customPane.add(editor[i], editor[i].getName());
            tabTitles[i] = editor[i].getName();
        }
        widgetValueHead = new UIHeadGroup(tabTitles) {
            @Override
            public void tabChanged(int index) {
                if (ComparatorUtils.equals(tabTitles[index], Inter.getLocText("None"))) {
                    customPane.setVisible(false);
                } else {
                    customPane.setVisible(true);
                }
                cardLayout.show(customPane, tabTitles[index]);
            }
        };
        this.add(widgetValueHead, BorderLayout.NORTH);
        this.add(customPane, BorderLayout.CENTER);

    }


    public Object update() {
        int index = widgetValueHead.getSelectedIndex();
        Editor e = editor[index];
        Object value = e.getValue();
        return value;
    }

    public void populate(Object ob) {
        for (int i = 0; i < editor.length; i++) {
            if (editor[i].accept(ob)) {
                setCardValue(i, ob);
                break;
            }
        }
    }

    private void setCardValue(int i, Object object) {
        widgetValueHead.setSelectedIndex(i);
        editor[i].setValue(object);
        // kunsnat: bug7861 所有的Editor值都要跟随改变, 因为populate的editor 从""
        // 一定是最后的Editor哦.
        for (int j = 0; j < editor.length; j++) {
            if (i == j) {
                continue;
            }
            this.editor[j].setValue(null);
        }
    }


}