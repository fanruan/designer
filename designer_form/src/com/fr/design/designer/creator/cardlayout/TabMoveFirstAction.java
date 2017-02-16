package com.fr.design.designer.creator.cardlayout;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.actions.FormUndoableAction;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

/**
 * Created by zhouping on 2017/2/9.
 */
public class TabMoveFirstAction extends FormUndoableAction {
    private XCardSwitchButton xCardSwitchButton;

    public TabMoveFirstAction(FormDesigner t, XCardSwitchButton xCardSwitchButton) {
        super(t);
        this.setName(Inter.getLocText("FR-Designer-Move_Tab_First"));
        this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/control/leftleft.png"));
        this.xCardSwitchButton = xCardSwitchButton;
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        XWCardTagLayout xwCardTagLayout = xCardSwitchButton.getTagLayout();
        XWCardLayout xwCardLayout = xCardSwitchButton.getCardLayout();
        CardSwitchButton currentButton = (CardSwitchButton) xCardSwitchButton.toData();
        try {
            int currentIndex = currentButton.getIndex();
            XWTabFitLayout xCurrentTab = (XWTabFitLayout) xwCardLayout.getXCreator(currentIndex);
            WTabFitLayout currentTab = (WTabFitLayout) xCurrentTab.toData();
            xwCardTagLayout.setSwitchingTab(true);
            //修改当前tab往前所有tab的索引号
            for (int i = currentIndex - 1; i >= 0; i--) {
                CardSwitchButton tempBtn = (CardSwitchButton) xwCardTagLayout.getXCreator(i).toData();
                tempBtn.setIndex(i + 1);
                WTabFitLayout tempTab = (WTabFitLayout) xwCardLayout.getXCreator(i).toData();
                tempTab.setIndex(i + 1);
                tempTab.setTabNameIndex(i + 1);
            }
            xwCardTagLayout.remove(xCardSwitchButton);
            xwCardTagLayout.add(xCardSwitchButton, 0);
            xwCardLayout.remove(xCurrentTab);
            xwCardLayout.add(xCurrentTab, 0);
            currentButton.setIndex(0);
            currentTab.setIndex(0);
            currentTab.setTabNameIndex(0);
            xwCardLayout.toData().setShowIndex(0);
            xwCardLayout.showCard();
            xwCardTagLayout.setSwitchingTab(false);
        }catch (Exception e){
            xwCardTagLayout.setSwitchingTab(false);
            FRLogger.getLogger().error(e.getMessage());
            return false;
        }
        return true;
    }

    public XCardSwitchButton getxCardSwitchButton() {
        return xCardSwitchButton;
    }

    public void setxCardSwitchButton(XCardSwitchButton xCardSwitchButton) {
        this.xCardSwitchButton = xCardSwitchButton;
    }
}
