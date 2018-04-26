package com.fr.design.designer.beans.adapters.layout;


import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;

public class DefaultLayoutAdapter extends AbstractLayoutAdapter {

    public DefaultLayoutAdapter(FormDesigner designer, XLayoutContainer c) {
        super(c);
    }

    @Override
    public HoverPainter getPainter() {
        return null;
    }

    @Override
    public void addComp(XCreator child, int x, int y) {
    	
    }

    @Override
    public boolean accept(XCreator creator, int x, int y) {
        return false;
    }
}