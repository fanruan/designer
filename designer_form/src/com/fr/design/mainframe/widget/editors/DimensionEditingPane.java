package com.fr.design.mainframe.widget.editors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

public class DimensionEditingPane extends BasicPane {

    private com.fr.design.gui.ilable.UILabel jLabel1;
    private com.fr.design.gui.ilable.UILabel jLabel2;
    private UISpinner spHeight;
    private UISpinner spWidth;

    public DimensionEditingPane() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new/**/ GridLayout(2, 1));
        jLabel1 = new com.fr.design.gui.ilable.UILabel(Inter.getLocText("Widget-Width") + ":");
        jLabel2 = new com.fr.design.gui.ilable.UILabel(Inter.getLocText("Widget-Height") + ":");
        spWidth = new UISpinner(0, Integer.MAX_VALUE, 1, 0);
        spHeight = new UISpinner(0, Integer.MAX_VALUE, 1, 0);

        spWidth.setPreferredSize(new java.awt.Dimension(29, 22));

        spHeight.setPreferredSize(new java.awt.Dimension(29, 22));

        JPanel panel1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(panel1);
        panel1.add(jLabel1, BorderLayout.WEST);
        panel1.add(spWidth, BorderLayout.CENTER);

        JPanel panel2 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        add(panel2);
        panel2.add(jLabel2, BorderLayout.WEST);
        panel2.add(spHeight, BorderLayout.CENTER);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("Widget-Sizing");
    }

    public Dimension update() {
        return new Dimension(((Number) spWidth.getValue()).intValue(), ((Number) spHeight.getValue()).intValue());
    }

    public void populate(Dimension d) {
        spWidth.setValue(d.width);
        spHeight.setValue(d.height);
    }
}