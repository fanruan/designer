package com.fr.design.widget.ui.designer.component;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.widget.accessibles.AccessibleTabBackgroundEditor;
import com.fr.design.widget.component.BackgroundCompPane;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Background;


/**
 * Created by ibm on 2017/8/8.
 */
public class TabFitLayoutBackgroundPane extends BackgroundCompPane<WTabFitLayout> {

    public TabFitLayoutBackgroundPane(){

    }

    @Override
    protected void initBackgroundEditor(){
        initialBackgroundEditor = new AccessibleTabBackgroundEditor();
        overBackgroundEditor = new AccessibleTabBackgroundEditor();
        clickBackgroundEditor = new AccessibleTabBackgroundEditor();
    }

    @Override
    protected UILabel getClickLabel(){
        return new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Background_Select"));
    }

    @Override
    public void update(WTabFitLayout tabFitLayout){
        int selectIndex = backgroundHead.getSelectedIndex();
        if(selectIndex == 0){
            tabFitLayout.setCustomStyle(false);
            tabFitLayout.setInitialBackground(null);
            tabFitLayout.setOverBackground(null);
            tabFitLayout.setClickBackground(null);
        }else{
            tabFitLayout.setCustomStyle(true);
            tabFitLayout.setInitialBackground((Background) initialBackgroundEditor.getValue());
            tabFitLayout.setOverBackground((Background) overBackgroundEditor.getValue());
            tabFitLayout.setClickBackground((Background)clickBackgroundEditor.getValue());
        }
        switchCard();
    }

    @Override
    public void populate(WTabFitLayout tabFitLayout){
        if(!tabFitLayout.isCustomStyle()){
            backgroundHead.setSelectedIndex(0);
            initialBackgroundEditor.setValue(null);
            overBackgroundEditor.setValue(null);
            clickBackgroundEditor.setValue(null);
        }else{
            backgroundHead.setSelectedIndex(1);
            initialBackgroundEditor.setValue(tabFitLayout.getInitialBackground());
            overBackgroundEditor.setValue(tabFitLayout.getOverBackground());
            clickBackgroundEditor.setValue(tabFitLayout.getClickBackground());
        }
        switchCard();
    }

    protected UILabel createUILable(){
        return new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Style"));
    }

    protected  String title4PopupWindow() {
        return "tabFitBackground";
    }


}
