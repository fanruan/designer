package com.fr.grid;

import com.fr.design.fun.impl.AbstractGridUIProcessor;

import javax.swing.plaf.ComponentUI;

/**
 * Created by Administrator on 2016/6/28/0028.
 */
public class DefaultGridUIProcessor extends AbstractGridUIProcessor{

    @Override
    public ComponentUI appearanceForGrid(int resolution) {
        return new GridUI(resolution);
    }

}
