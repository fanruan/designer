package com.fr.design.designer.creator.cardlayout;

import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.IOUtils;
import com.fr.general.Inter;

/**
 * Created by zhouping on 2017/2/9.
 */
public class TabMoveEndAction extends TabMoveAction {

    public TabMoveEndAction(FormDesigner t, XCardSwitchButton xCardSwitchButton) {
        super(t, xCardSwitchButton);
        this.setName(Inter.getLocText("FR-Designer-Move_Tab_End"));
        this.setSmallIcon(IOUtils.readIcon("com/fr/design/images/control/tab/end.png"));
    }

    //改变Tab的索引号
    protected void changeTabIndex(XWCardTagLayout xwCardTagLayout, XWCardLayout xwCardLayout, int currentIndex, int maxIndex) {
        //修改当前tab往后所有tab的索引号
        for (int i = currentIndex + 1; i < maxIndex; i++) {
            CardSwitchButton tempBtn = (CardSwitchButton) xwCardTagLayout.getXCreator(i).toData();
            tempBtn.setIndex(i - 1);
            WTabFitLayout tempTab = (WTabFitLayout) xwCardLayout.getXCreator(i).toData();
            tempTab.setIndex(i - 1);
            tempTab.setTabNameIndex(i - 1);
        }
    }

    @Override
    protected int getTabMoveIndex(CardSwitchButton btn) {
        return getxCardSwitchButton().getTagLayout().getComponentCount() - 1;
    }
}
