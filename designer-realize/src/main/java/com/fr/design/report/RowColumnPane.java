/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.report;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

/**
 * RowColumn dialog.
 */
public class RowColumnPane extends BasicPane {

    public RowColumnPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        JPanel topPane =FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(topPane, BorderLayout.NORTH);
        titleLabel = new UILabel(Inter.getLocText("Delete"));
        topPane.add(titleLabel, BorderLayout.WEST);
        JPanel separatorPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        separatorPane.add(new JSeparator());
        topPane.add(separatorPane, BorderLayout.CENTER);

        //Center.
        JPanel centerPane =FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        this.add(centerPane, BorderLayout.CENTER);
        centerPane.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 8));

        entireRowRadioButton = new UIRadioButton(Inter.getLocText("EditRC-Entire_row"));
        entireRowRadioButton.setMnemonic('r');
        entireColRadioButton = new UIRadioButton(Inter.getLocText("EditRC-Entire_column"));
        entireColRadioButton.setMnemonic('c');
        entireRowRadioButton.setSelected(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(entireRowRadioButton);
        buttonGroup.add(entireColRadioButton);

        centerPane.add(entireRowRadioButton);
        centerPane.add(entireColRadioButton);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    @Override
    protected String title4PopupWindow() {
    	return this.titleLabel.getText();
    }

    public boolean isEntireRow() {
        if (this.entireRowRadioButton.isSelected()) {
            return true;
        }

        return false;
    }

    private UILabel titleLabel;

    private UIRadioButton entireRowRadioButton;
    private UIRadioButton entireColRadioButton;
}