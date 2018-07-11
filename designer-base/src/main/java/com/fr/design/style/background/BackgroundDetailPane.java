package com.fr.design.style.background;

import com.fr.general.Background;

import javax.swing.*;
import javax.swing.event.ChangeListener;

/**
 * Created by richie on 16/5/18.
 */
public abstract class BackgroundDetailPane extends JPanel {

    public abstract void populate(Background background);

    public abstract Background update() throws Exception;

    public abstract void addChangeListener(ChangeListener changeListener);
}
