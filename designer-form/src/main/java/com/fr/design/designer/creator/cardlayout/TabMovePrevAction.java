package com.fr.design.designer.creator.cardlayout;

import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

/**
 * Created by zhouping on 2017/2/9.
 */
public class TabMovePrevAction extends TabMoveAction {

    public TabMovePrevAction(FormDesigner t, XCardSwitchButton xCardSwitchButton) {
        super(t, xCardSwitchButton);
        this.setName(Inter.getLocText("FR-Designer-Move_Tab_Prev"));
        this.setSmallIcon(IOUtils.readIcon("com/fr/design/images/control/tab/prev.png"));
    }

    @Override
    protected void changeTabIndex(XWCardTagLayout xwCardTagLayout, XWCardLayout xwCardLayout, int currentIndex, int maxIndex) {
        //修改上一个tab的索引号
        CardSwitchButton prevBtn = (CardSwitchButton) xwCardTagLayout.getXCreator(currentIndex - 1).toData();
        prevBtn.setIndex(currentIndex);
        WTabFitLayout prevTab = (WTabFitLayout) xwCardLayout.getXCreator(currentIndex - 1).toData();
        prevTab.setIndex(currentIndex);
        prevTab.setTabNameIndex(currentIndex);
    }

    @Override
    protected int getTabMoveIndex(CardSwitchButton currentButton) {
        return currentButton.getIndex() - 1;
    }
}
