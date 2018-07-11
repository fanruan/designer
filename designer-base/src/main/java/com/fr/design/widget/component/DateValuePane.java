package com.fr.design.widget.component;

import com.fr.base.BaseFormula;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FormulaEditor;
import com.fr.design.editor.editor.NoneEditor;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by ibm on 2017/8/8.
 */
public class DateValuePane extends JPanel {
    private UIButtonGroup widgetValueHead;
    private Editor[] editor;
    private static final String NONE_EDITOR_NAME = Inter.getLocText("None");
    private static final String DATE_EDITOR_NAME = Inter.getLocText("Date");
    private static final String FORMULA_EDITOR_NAME = Inter.getLocText("Parameter-Formula");


    public DateValuePane() {
        editor = new Editor[]{
                new NoneEditor(null, NONE_EDITOR_NAME),
                new DateEditor(true, DATE_EDITOR_NAME),
                new FormulaEditor(FORMULA_EDITOR_NAME)
        };
        this.setLayout(new BorderLayout(0, LayoutConstants.VGAP_SMALL));
        final CardLayout cardLayout = new CardLayout();
        final JPanel customPane = new JPanel(cardLayout);
        final String[] tabTitles = new String[editor.length];
        for (int i = 0; i < editor.length; i++) {
            customPane.add(editor[i], editor[i].getName());
            tabTitles[i] = editor[i].getName();
        }
        widgetValueHead = new UIButtonGroup(tabTitles);
        widgetValueHead.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int index = widgetValueHead.getSelectedIndex();
                if (ComparatorUtils.equals(tabTitles[index], Inter.getLocText("None"))) {
                    customPane.setVisible(false);
                } else {
                    customPane.setVisible(true);
                }
                cardLayout.show(customPane, tabTitles[index]);
            }
        });
        this.add(widgetValueHead, BorderLayout.NORTH);
        this.add(customPane, BorderLayout.CENTER);

    }


    public Object update() {
        int index = widgetValueHead.getSelectedIndex();
        Editor e = editor[index];
        Object value = e.getValue();
        if(value == null && ComparatorUtils.equals(FORMULA_EDITOR_NAME, e.getName())){
            value = BaseFormula.createFormulaBuilder().build();
        }
        return value;
    }

    public void populate(Object ob) {
        if(ob == null){
            setCardValue(0, ob);
        }
        for (int i = 0; i < editor.length; i++) {
            if (editor[i].accept(ob)) {
                setCardValue(i, ob);
                break;
            }
        }
    }

    private void setCardValue(int i, Object object) {
        widgetValueHead.setSelectedIndex(i);
        widgetValueHead.populateBean();
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