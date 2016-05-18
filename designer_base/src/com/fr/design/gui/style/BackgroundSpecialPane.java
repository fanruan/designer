package com.fr.design.gui.style;

import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.backgroundpane.*;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-9-17
 * Time: 下午5:33
 */
public class BackgroundSpecialPane extends BackgroundPane{

    public BackgroundSpecialPane(){
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
        ImageBackgroundQuickPane imageBackgroundPane = new ImageBackgroundQuickPane();

        imageBackgroundPane.registerChangeListener(new UIObserverListener() {
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
        return new BackgroundQuickPane[] {
                new NullBackgroundQuickPane(),
                colorBackgroundPane,
                imageBackgroundPane,
                gradientPane
        };
    }
}