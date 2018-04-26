package com.fr.design.widget.ui.designer.component;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.widget.component.BackgroundCompPane;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Background;
import com.fr.general.Inter;

/**
 * Created by ibm on 2017/8/8.
 */
public class TabFitLayoutBackgroundPane extends BackgroundCompPane<WTabFitLayout> {

    public TabFitLayoutBackgroundPane(){

    }

    public void update(WTabFitLayout wTabFitLayout){
        int selectIndex = backgroundHead.getSelectedIndex();
        if(selectIndex == 0){
            wTabFitLayout.setCustomStyle(false);
            wTabFitLayout.setInitialBackground(null);
            wTabFitLayout.setOverBackground(null);
            wTabFitLayout.setClickBackground(null);
        }else{
            wTabFitLayout.setCustomStyle(true);
            wTabFitLayout.setInitialBackground((Background) initialBackgroundEditor.getValue());
            wTabFitLayout.setOverBackground((Background) overBackgroundEditor.getValue());
            wTabFitLayout.setClickBackground((Background)clickBackgroundEditor.getValue());
        }
        switchCard();
    }

    public void populate(WTabFitLayout wTabFitLayout){
        if(!wTabFitLayout.isCustomStyle()){
            backgroundHead.setSelectedIndex(0);
            initialBackgroundEditor.setValue(null);
            overBackgroundEditor.setValue(null);
            clickBackgroundEditor.setValue(null);
        }else{
            backgroundHead.setSelectedIndex(1);
            initialBackgroundEditor.setValue(wTabFitLayout.getInitialBackground());
            overBackgroundEditor.setValue(wTabFitLayout.getOverBackground());
            clickBackgroundEditor.setValue(wTabFitLayout.getClickBackground());
        }
        switchCard();
    }

    protected UILabel createUILable(){
        return new UILabel(Inter.getLocText("FR-Designer_Style"));
    }

    protected  String title4PopupWindow() {
        return "tabFitBackground";
    }


}
