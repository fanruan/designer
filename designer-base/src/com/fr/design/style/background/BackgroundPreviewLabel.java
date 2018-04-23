/*
 * Copyright(c) 2001-2010, FineReport  Inc, All Rights Reserved.
 */
package com.fr.design.style.background;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import com.fr.general.Background;
import com.fr.general.Inter;

/**
 * Preview background.
 */
public class BackgroundPreviewLabel extends UILabel {
    private Background background = null;
    private EventListenerList backgroundChangeListenerList = new EventListenerList();

    public BackgroundPreviewLabel() {
        this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public BackgroundPreviewLabel(Background background) {
        this.setBackgroundObject(background);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.getBackgroundObject() != null) {
            this.getBackgroundObject().paint(g, new Rectangle2D.Double(0, 0,
                    this.getSize().getWidth(), this.getSize().getHeight()));
            this.setText("");
        } else {
            this.setText(Inter.getLocText("Background-Background_is_NULL") + "...");
        }
    }

    public Background getBackgroundObject() {
        return this.background;
    }

    public void setBackgroundObject(Background background) {
        this.background = background;
        fireBackgroundStateChanged();
        this.repaint();
    }
    
    public void addBackgroundChangeListener(ChangeListener changeListener) {
    	backgroundChangeListenerList.add(ChangeListener.class, changeListener);
    }

    /**
     */
    public void fireBackgroundStateChanged() {
        Object[] listeners = backgroundChangeListenerList.getListenerList();
        ChangeEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }
}