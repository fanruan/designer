package com.fr.design.style.background.impl;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundDetailPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/5/18.
 */
public abstract class BPane extends BackgroundDetailPane {

    public BPane(int nColumn) {
        this.initComponents(nColumn);
    }

    private void initComponents(int nColumn) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        this.add(contentPane, BorderLayout.NORTH);
//            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        // type type.

        JPanel typePane = FRGUIPaneFactory.createTitledBorderPane(titleOfTypePane());
        contentPane.add(typePane);
        JPanel typePane2 = new JPanel();
        typePane.add(typePane2);
        typePane2.setLayout(layoutOfTypePane(nColumn));
        setChildrenOfTypePane(typePane2);

        setChildrenOfContentPane(contentPane);
    }

    protected abstract String titleOfTypePane();

    protected abstract LayoutManager layoutOfTypePane(int nColumn);

    protected abstract void setChildrenOfTypePane(JPanel typePane2);

    protected void setChildrenOfContentPane(JPanel contentPane) {
    }
}
