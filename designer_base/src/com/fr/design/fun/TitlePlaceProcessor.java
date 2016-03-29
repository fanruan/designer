package com.fr.design.fun;

import com.fr.stable.fun.Level;

import java.awt.*;

public interface TitlePlaceProcessor extends Level{

    String MARK_STRING = "TitlePlaceProcessor";

    int CURRENT_LEVEL = 1;


    void hold(Container container, Component loggerComponent, Component loginComponent);
}