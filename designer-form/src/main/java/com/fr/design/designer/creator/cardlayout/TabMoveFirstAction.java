package com.fr.design.designer.creator.cardlayout;

import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.IOUtils;


/**
 * Created by zhouping on 2017/2/9.
 */
public class TabMoveFirstAction extends TabMoveAction {

    public TabMoveFirstAction(FormDesigner t, XCardSwitchButton xCardSwitchButton) {
        super(t, xCardSwitchButton);
        this.setName(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Move_Tab_First"));
        this.setSmallIcon(IOUtils.readIcon("com/fr/design/images/control/tab/first.png"));
    }

    @Override
    protected void changeTabIndex(XWCardTagLayout xwCardTagLayout, XWCardLayout xwCardLayout, int currentIndex, int maxIndex) {
        //修改当前tab往前所有tab的索引号
        for (int i = currentIndex - 1; i >= 0; i--) {
            CardSwitchButton tempBtn = (CardSwitchButton) xwCardTagLayout.getXCreator(i).toData();
            tempBtn.setIndex(i + 1);
            WTabFitLayout tempTab = (WTabFitLayout) xwCardLayout.getXCreator(i).toData();
            tempTab.setIndex(i + 1);
            tempTab.setTabNameIndex(i + 1);
        }
    }

    @Override
    protected int getTabMoveIndex(CardSwitchButton btn) {
        return 0;
    }
}
