package com.fr.design.gui.style;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.event.UIObserverListener;
import com.fr.design.fun.BackgroundQuickUIProvider;
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
        //hugh:表单支持背景接口
        List<BackgroundQuickPane> kinds = new ArrayList<BackgroundQuickPane>();
        
        kinds.add(new NullBackgroundQuickPane());
        kinds.add(colorBackgroundPane);
        kinds.add(imageBackgroundPane);
        kinds.add(gradientPane);
        
        Set<BackgroundQuickUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(BackgroundQuickUIProvider.MARK_STRING);
        for (BackgroundQuickUIProvider provider : providers) {
        	BackgroundQuickPane newTypePane = provider.appearanceForBackground();
        	newTypePane.registerChangeListener(new UIObserverListener() {
                @Override
                public void doChange() {
                    fireStateChanged();
                }
            });
            kinds.add(newTypePane);
        }
        
        return kinds.toArray(new BackgroundQuickPane[kinds.size()]);
    }
}