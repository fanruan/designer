/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.PaddingMargin;
import com.fr.general.Inter;

/**
 * @author richer
 * @since 6.5.3
 */
public class PaddingMarginPane extends BasicPane {

	private static final double MIN_VALUE = 0; // 内边距的最小值
    private UISpinner topSpinner;
    private UISpinner leftSpinner;
    private UISpinner bottomSpinner;
    private UISpinner rightSpinner;
    private JPanel contentPane;

    public PaddingMarginPane() {
        setLayout(coverLayout);
        contentPane = new JPanel(new GridLayout(2, 2, 4, 4));
        this.add(contentPane);
        JPanel topPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        topPane.add(new UILabel(Inter.getLocText("FR-Base_Top") + " "), BorderLayout.WEST);
        topSpinner = new UISpinner(MIN_VALUE, Integer.MAX_VALUE, 1, 0);
        topPane.add(topSpinner, BorderLayout.CENTER);
        UILabel topLabel = new UILabel(" " + Inter.getLocText("FR-Designer_Indent-Pixel") + "  ");
        topPane.add(topLabel, BorderLayout.EAST);
        contentPane.add(topPane);

        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        leftPane.add(new UILabel(Inter.getLocText("FR-Base_Left") + " "), BorderLayout.WEST);
        leftSpinner = new UISpinner(MIN_VALUE, Integer.MAX_VALUE, 1, 0);
        leftPane.add(leftSpinner, BorderLayout.CENTER);
        UILabel leftLabel = new UILabel(" " + Inter.getLocText("FR-Designer_Indent-Pixel") + "  ");
        leftPane.add(leftLabel, BorderLayout.EAST);
        contentPane.add(leftPane);

        JPanel bottomPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        bottomPane.add(new UILabel(Inter.getLocText("FR-Base_Bottom") + " "), BorderLayout.WEST);
        bottomSpinner = new UISpinner(MIN_VALUE, Integer.MAX_VALUE, 1, 0);
        bottomPane.add(bottomSpinner, BorderLayout.CENTER);
        UILabel bottomLabel = new UILabel(" " + Inter.getLocText("FR-Designer_Indent-Pixel") + "  ");
        bottomPane.add(bottomLabel, BorderLayout.EAST);
        contentPane.add(bottomPane);

        JPanel rightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        rightPane.add(new UILabel(Inter.getLocText("FR-Base_Right") + " "), BorderLayout.WEST);
        rightSpinner = new UISpinner(MIN_VALUE, Integer.MAX_VALUE, 1, 0);
        rightPane.add(rightSpinner, BorderLayout.CENTER);
        UILabel rightLabel = new UILabel(" " + Inter.getLocText("FR-Designer_Indent-Pixel") + "  ");
        rightPane.add(rightLabel, BorderLayout.EAST);
        contentPane.add(rightPane);
    }

    private LayoutManager coverLayout = new LayoutManager() {

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return BasicDialog.SMALL;
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int height = parent.getHeight();
            int preferWidth = contentPane.getPreferredSize().width;
            int preferHeight = contentPane.getPreferredSize().height;
            contentPane.setBounds((width - preferWidth)/2, (height - preferHeight)/2, preferWidth, preferHeight);
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }
    };


    @Override
    protected String title4PopupWindow() {
    	return Inter.getLocText("FR-Base_Margin");
    }

    public void populate(PaddingMargin pm) {
        if (pm == null) {
            pm = new PaddingMargin();
        }
        topSpinner.setValue(pm.getTop());
        leftSpinner.setValue(pm.getLeft());
        bottomSpinner.setValue(pm.getBottom());
        rightSpinner.setValue(pm.getRight());
    }

    public PaddingMargin update() {
        PaddingMargin pm = new PaddingMargin();
        pm.setTop((int) topSpinner.getValue());
        pm.setLeft((int) leftSpinner.getValue());
        pm.setBottom((int) bottomSpinner.getValue());
        pm.setRight((int) rightSpinner.getValue());
        return pm;
    }
}