package com.fr.design.fun.impl;

import com.fr.design.fun.OemProcessor;
import com.fr.design.menu.MenuDef;
import com.fr.start.SplashStrategy;

import java.awt.image.BufferedImage;
import java.util.List;

public abstract class AbstractOemProcessor implements OemProcessor{
    @Override
    public MenuDef[] dealWithMenuDef(MenuDef[] menuDefs) {
        return menuDefs;
    }

    @Override
    public List<BufferedImage> createTitleIcon() {
        return null;
    }

    @Override
    public SplashStrategy createSplashStrategy() {
        return null;
    }
}