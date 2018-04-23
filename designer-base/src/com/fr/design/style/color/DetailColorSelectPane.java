/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style.color;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.GraphHelper;
import com.fr.design.border.UITitledBorder;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

/**
 * The detail color select pane.
 * Often used for foreground selection.
 */
public class DetailColorSelectPane extends BasicPane {
    private ColorSelectPane colorSelectPane;
    private UILabel colorPreviewLabel;

    public DetailColorSelectPane() {
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel titledefaultPane=FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Color"));
        JPanel defaultPane =FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        this.add(titledefaultPane, BorderLayout.WEST);
        titledefaultPane.add(defaultPane);

        defaultPane.add(Box.createHorizontalStrut(4));
        JPanel colorSelectContainPane =FRGUIPaneFactory.createBorderLayout_L_Pane();
        defaultPane.add(colorSelectContainPane);
        colorSelectPane = new ColorSelectPane();
        colorSelectContainPane.add(colorSelectPane, BorderLayout.NORTH);

        defaultPane.add(Box.createHorizontalStrut(8));

        //color preview
        JPanel colorPreviewContainPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        defaultPane.add(colorPreviewContainPane);

        JPanel colorPreviewPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        colorPreviewPane.setPreferredSize(new Dimension(180, 60));
        colorPreviewContainPane.add(colorPreviewPane, BorderLayout.NORTH);

        colorPreviewLabel = new UILabel("                                        ") {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                g2d.setPaint(this.getBackground());
                GraphHelper.fill(g2d, new Rectangle2D.Double(0, 0,
                        this.getSize().getWidth(), this.getSize().getHeight()));
            }
        };
        colorPreviewLabel.setFont(new Font(this.getFont().getFontName(),
                this.getFont().getStyle(), this.getFont().getSize() + 4));
        colorPreviewLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 2));
        colorPreviewPane.add(colorPreviewLabel, BorderLayout.CENTER);
        UITitledBorder explainBorder = UITitledBorder.createBorderWithTitle(Inter.getLocText("Preview"));
        colorPreviewPane.setBorder(explainBorder);

        this.addChangeListener(colorPreviewLabelChangeListener);

        //use the white as the default color.
        this.populate(Color.WHITE);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return "Color";
    }

    public void populate(Color color) {
        this.colorSelectPane.setColor(color);
    }

    public Color update() {
        return this.colorSelectPane.getColor();
    }

    public void addChangeListener(ChangeListener changeListener) {
        colorSelectPane.addChangeListener(changeListener);
    }

    ChangeListener colorPreviewLabelChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent evt) {
            colorPreviewLabel.setBackground(colorSelectPane.getColor());
        }
    };
}