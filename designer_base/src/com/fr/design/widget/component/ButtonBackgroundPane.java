package com.fr.design.widget.component;

import com.fr.form.ui.FreeButton;
import com.fr.general.Background;

/**
 * Created by ibm on 2017/8/8.
 */
public class ButtonBackgroundPane extends BackgroundCompPane<FreeButton> {

    public ButtonBackgroundPane(){

    }

    public void update(FreeButton freeButton){
        int selectIndex = backgroundHead.getSelectedIndex();
        if(selectIndex == 0){
            freeButton.setCustomStyle(false);
            freeButton.setInitialBackground(null);
            freeButton.setOverBackground(null);
            freeButton.setClickBackground(null);
        }else{
            freeButton.setCustomStyle(true);
            freeButton.setInitialBackground((Background) initialBackgroundEditor.getValue());
            freeButton.setOverBackground((Background) overBackgroundEditor.getValue());
            freeButton.setClickBackground((Background)clickBackgroundEditor.getValue());
        }
        switchCard();
    }

    public void populate(FreeButton freeButton){
        if(!freeButton.isCustomStyle()){
            backgroundHead.setSelectedIndex(0);
            initialBackgroundEditor.setValue(null);
            overBackgroundEditor.setValue(null);
            clickBackgroundEditor.setValue(null);
        }else{
            backgroundHead.setSelectedIndex(1);
            initialBackgroundEditor.setValue(freeButton.getInitialBackground());
            overBackgroundEditor.setValue(freeButton.getOverBackground());
            clickBackgroundEditor.setValue(freeButton.getClickBackground());
        }
        switchCard();
    }

    protected  String title4PopupWindow() {
        return "buttonBackground";
    }

}
