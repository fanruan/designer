package com.fr.design.widget.ui.designer.component;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.widget.accessibles.AccessibleTabBackgroundEditor;
import com.fr.design.widget.component.BackgroundCompPane;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Background;
import com.fr.general.Inter;

/**
 * Created by ibm on 2017/8/8.
 */
public class TabFitLayoutBackgroundPane extends BackgroundCompPane<WTabFitLayout> {

    public TabFitLayoutBackgroundPane(){

    }

    @Override
    protected void initBackgroundEditor(){
        initalBackgroundEditor = new AccessibleTabBackgroundEditor();
        overBackgroundEditor = new AccessibleTabBackgroundEditor();
        clickBackgroundEditor = new AccessibleTabBackgroundEditor();
    }

    @Override
    protected UILabel getClickLabel(){
        return new UILabel(Inter.getLocText("FR-Designer_Background_Select"));
    }

    public void update(CardSwitchButton cardSwitchButton){
        int selectIndex = backgroundHead.getSelectedIndex();
        if(selectIndex == 0){
            cardSwitchButton.setCustomStyle(false);
            cardSwitchButton.setInitialBackground(null);
            cardSwitchButton.setOverBackground(null);
            cardSwitchButton.setClickBackground(null);
        }else{
            cardSwitchButton.setCustomStyle(true);
            cardSwitchButton.setInitialBackground((Background) initalBackgroundEditor.getValue());
            cardSwitchButton.setOverBackground((Background) overBackgroundEditor.getValue());
            cardSwitchButton.setClickBackground((Background)clickBackgroundEditor.getValue());
        }
        switchCard();
    }

    public void populate(CardSwitchButton cardSwitchButton){
        if(!cardSwitchButton.isCustomStyle()){
            backgroundHead.setSelectedIndex(0);
            initalBackgroundEditor.setValue(null);
            overBackgroundEditor.setValue(null);
            clickBackgroundEditor.setValue(null);
        }else{
            backgroundHead.setSelectedIndex(1);
            initalBackgroundEditor.setValue(cardSwitchButton.getInitialBackground());
            overBackgroundEditor.setValue(cardSwitchButton.getOverBackground());
            clickBackgroundEditor.setValue(cardSwitchButton.getClickBackground());
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
