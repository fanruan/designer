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
public class TabMovePrevAction extends FormUndoableAction {
    private XCardSwitchButton xCardSwitchButton;

    public TabMovePrevAction(FormDesigner t, XCardSwitchButton xCardSwitchButton) {
        super(t);
        this.setName(Inter.getLocText("FR-Designer-Move_Tab_Prev"));
        this.setSmallIcon(BaseUtils.readIcon("com/fr/design/images/control/left.png"));
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
            //修改上一个tab的索引号
            CardSwitchButton prevBtn = (CardSwitchButton) xwCardTagLayout.getXCreator(currentIndex - 1).toData();
            prevBtn.setIndex(currentIndex);
            WTabFitLayout prevTab = (WTabFitLayout) xwCardLayout.getXCreator(currentIndex - 1).toData();
            prevTab.setIndex(currentIndex);
            prevTab.setTabNameIndex(currentIndex);

            xwCardTagLayout.remove(xCardSwitchButton);
            xwCardTagLayout.add(xCardSwitchButton, currentIndex - 1);
            xwCardLayout.remove(xCurrentTab);
            xwCardLayout.add(xCurrentTab, currentIndex - 1);
            currentButton.setIndex(currentIndex - 1);
            currentTab.setIndex(currentIndex - 1);
            currentTab.setTabNameIndex(currentIndex - 1);
            xwCardLayout.toData().setShowIndex(currentIndex - 1);
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
