package com.fr.design.designer.creator.cardlayout;

import com.fr.design.designer.beans.actions.FormUndoableAction;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.FRLogger;

/**
 * Created by zhouping on 2017/2/17.
 */
public class TabMoveAction extends FormUndoableAction {
    private XCardSwitchButton xCardSwitchButton;

    public TabMoveAction(FormDesigner t, XCardSwitchButton xCardSwitchButton) {
        super(t);
        this.xCardSwitchButton = xCardSwitchButton;
    }

    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        XCardSwitchButton xCardSwitchButton = getxCardSwitchButton();
        XWCardTagLayout xwCardTagLayout = xCardSwitchButton.getTagLayout();
        XWCardLayout xwCardLayout = xCardSwitchButton.getCardLayout();
        CardSwitchButton currentButton = (CardSwitchButton) xCardSwitchButton.toData();
        try {
            int currentIndex = currentButton.getIndex();
            int maxIndex = xwCardTagLayout.getComponentCount();
            XWTabFitLayout xCurrentTab = (XWTabFitLayout) xwCardLayout.getXCreator(currentIndex);
            WTabFitLayout currentTab = (WTabFitLayout) xCurrentTab.toData();
            xwCardTagLayout.setSwitchingTab(true);

            changeTabIndex(xwCardTagLayout, xwCardLayout, currentIndex, maxIndex);

            moveTabAction(xwCardTagLayout, currentButton, xCurrentTab, currentTab);

            xwCardTagLayout.setSwitchingTab(false);
            xwCardTagLayout.doLayout();
            FormHierarchyTreePane.getInstance().refreshDockingView();
        }catch (Exception e){
            xwCardTagLayout.setSwitchingTab(false);
            FRLogger.getLogger().error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private void moveTabAction(XWCardTagLayout xwCardTagLayout, CardSwitchButton currentButton, XWTabFitLayout xCurrentTab, WTabFitLayout currentTab) {
        int move2Index = getTabMoveIndex(currentButton);
        XWCardLayout xwCardLayout = xCardSwitchButton.getCardLayout();
        xwCardTagLayout.remove(xCardSwitchButton);
        xwCardTagLayout.add(xCardSwitchButton, move2Index);
        xwCardLayout.remove(xCurrentTab);
        xwCardLayout.add(xCurrentTab, move2Index);
        currentButton.setIndex(move2Index);
        currentTab.setIndex(move2Index);
        currentTab.setTabNameIndex(move2Index);
        xwCardLayout.toData().setShowIndex(move2Index);
        xwCardLayout.showCard();
    }

    //改变Tab的索引号
    protected void changeTabIndex(XWCardTagLayout xwCardTagLayout, XWCardLayout xwCardLayout, int currentIndex, int maxIndex) {
    }

    /**
     * 获取tab移动的目的索引：首位，末尾，下一个，上一个
     * @param currentButton 当前按钮
     * @return 索引
     */
    protected int getTabMoveIndex(CardSwitchButton currentButton) {
        return currentButton.getIndex();
    }

    public XCardSwitchButton getxCardSwitchButton() {
        return xCardSwitchButton;
    }

    public void setxCardSwitchButton(XCardSwitchButton xCardSwitchButton) {
        this.xCardSwitchButton = xCardSwitchButton;
    }
}
