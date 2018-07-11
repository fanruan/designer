/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.layout;

import java.awt.BorderLayout;

/**
 * @author richer
 * @since 6.5.3
 */
public class FRBorderLayout extends BorderLayout implements FRLayoutManager {

    public FRBorderLayout() {
        this(0, 0);
    }

    public FRBorderLayout(int hgap, int vgap) {
        super(hgap, vgap);
    }

    @Override
    public boolean isResizable() {
        return false;
    }
}