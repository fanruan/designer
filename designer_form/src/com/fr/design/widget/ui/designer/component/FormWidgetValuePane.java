package com.fr.design.widget.ui.designer.component;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.widget.ui.designer.custom.*;
import com.fr.form.ui.TextEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/7/27.
 */
public class FormWidgetValuePane extends JPanel {
    private UIHeadGroup widgetValueHead;

    public FormWidgetValuePane() {
        WidgetValuePane StringPane = new WidgetValueString();
        WidgetValuePane FormulaPane = new WidgetValueFormula();
        WidgetValuePane FieldPane = new WidgetValueField();
        this.setLayout(new BorderLayout(0, LayoutConstants.VGAP_SMALL));
        final CardLayout cardLayout = new CardLayout();
        final JPanel customPane = new JPanel(cardLayout);
        customPane.add(StringPane.createWidgetValuePane(), StringPane.markTitle());
        customPane.add(FormulaPane.createWidgetValuePane(), FormulaPane.markTitle());
        customPane.add(FieldPane.createWidgetValuePane(), FieldPane.markTitle());

        final String[] tabTitles = new String[]{StringPane.markTitle(), FormulaPane.markTitle(), FieldPane.markTitle()};
        widgetValueHead = new UIHeadGroup(tabTitles) {
            @Override
            public void tabChanged(int index) {
                //todo
                if (index == 2) {
                    customPane.setPreferredSize(new Dimension(100, 50));
                } else {
                    customPane.setPreferredSize(new Dimension(100, 20));
                }
                cardLayout.show(customPane, tabTitles[index]);
            }
        };
        this.add(widgetValueHead, BorderLayout.NORTH);
        this.add(customPane, BorderLayout.CENTER);

    }

    public void update(TextEditor ob) {
        //todo

    }

    public void populate(TextEditor ob) {
        //todo

    }

}
