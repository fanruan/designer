package com.fr.design.gui.style;

import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.backgroundpane.BackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.GradientBackgroundQuickPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundQuickPane;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-9-17
 * Time: 下午4:34
 */
public class BackgroundNoImagePane extends BackgroundPane{

    public BackgroundNoImagePane(){
        super();
    }

    @Override
    protected BackgroundQuickPane[] supportKindsOfBackgroundUI() {
        ColorBackgroundQuickPane colorBackgroundPane = new ColorBackgroundQuickPane();

        colorBackgroundPane.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                fireStateChanged();
            }
        });
        GradientBackgroundQuickPane gradientPane = new GradientBackgroundQuickPane();

        gradientPane.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                fireStateChanged();
            }
        });
        return new BackgroundQuickPane[]{
                new NullBackgroundQuickPane(),
                colorBackgroundPane,
                gradientPane
        };
    }
}