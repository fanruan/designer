package com.fr.design.gui.style;

import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.backgroundpane.BackgroundSettingPane;
import com.fr.design.mainframe.backgroundpane.ColorBackgroundPane;
import com.fr.design.mainframe.backgroundpane.ImageBackgroundPane;
import com.fr.design.mainframe.backgroundpane.NullBackgroundPane;

import java.util.ArrayList;

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

    protected void initPaneList(){
        ColorBackgroundPane colorBackgroundPane = new ColorBackgroundPane();

        colorBackgroundPane.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                fireStateChanged();
            }
        });
        ImageBackgroundPane imageBackgroundPane = new ImageBackgroundPane();

        imageBackgroundPane.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                fireStateChanged();
            }
        });
        GradientPane gradientPane = new GradientPane();
        gradientPane.registerChangeListener(new UIObserverListener() {
            @Override
            public void doChange() {
                fireStateChanged();
            }
        });
        paneList = new ArrayList<BackgroundSettingPane>();
        paneList.add(new NullBackgroundPane());
        paneList.add(colorBackgroundPane);
        paneList.add(imageBackgroundPane);
        paneList.add(gradientPane);

    }
}