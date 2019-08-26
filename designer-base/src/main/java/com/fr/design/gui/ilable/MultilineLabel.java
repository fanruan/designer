/*
 * @(#)MultilineLabel.java
 *
 * Copyright 2002 JIDE Software. All rights reserved.
 */
package com.fr.design.gui.ilable;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.LookAndFeel;
import javax.swing.text.DefaultCaret;

import com.fr.design.gui.itextarea.UITextArea;

/**
 * Normal UILabel cannot have multiple lines. If you want to multiple
 * label, you can use this class.
 */
public class MultilineLabel extends UITextArea {
    public MultilineLabel() {
        initCurrentComponents();
    }

    public MultilineLabel(String s) {
        super(s);
        initCurrentComponents();
    }

    private void initCurrentComponents() {
        adjustUI();
    }

    /**
     * Reloads the pluggable UI.  The key used to fetch the
     * new interface is <code>getUIClassID()</code>.  The type of
     * the UI is <code>TextUI</code>.  <code>invalidate</code>
     * is called after setting the UI.
     */
   
    public void updateUI() {
        super.updateUI();
        adjustUI();
    }

    /**
     * Adjusts UI to make sure it looks like a label instead of a text area.
     */
    protected void adjustUI() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
        setRequestFocusEnabled(false);
        setFocusable(false);
        setOpaque(false);
        setCaret(new DefaultCaret() {
            protected void adjustVisibility(Rectangle nloc) {
            }
        });

        LookAndFeel.installBorder(this, "Label.border");
        LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
    }

    /**
     * Overrides <code>getMinimumSize</code> to return <code>getPreferredSize()</code> instead.
     * We did this because of a bug at http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4824261.
     *
     * @return the preferred size as minimum size.
     */
    
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}