package com.fr.design.mainframe.hold;

import com.fr.design.fun.impl.AbstractTitleProcessor;

import java.awt.*;

public class DefaultTitlePlace extends AbstractTitleProcessor {

    @Override
    public void hold(Container container, Component loggerComponent, Component loginComponent) {
        container.add(loggerComponent, BorderLayout.CENTER);
        container.add(loginComponent, BorderLayout.EAST);
    }
}