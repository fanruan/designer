package com.fr.design.mainframe.widget.ui;

import com.fr.design.designer.creator.XCreator;

/**
 * Created by kerry on 2017/9/30.
 */
public class WidgetBasicPropertyPaneFactory {

    public static FormBasicPropertyPane createBasicPropertyPane(XCreator xCreator){
        if(xCreator.supportSetVisible() && xCreator.supportSetEnable()){
            return new FormBasicWidgetPropertyPane();
        }
        if(xCreator.supportSetVisible()){
            return new BasicSetVisiblePropertyPane();
        }else{
            return new FormBasicPropertyPane();
        }

    }
}
