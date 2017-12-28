/**
 *
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.border.Border;

import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRHorizontalLayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRVerticalLayoutAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.form.layout.FRFlowLayout;
import com.fr.design.form.layout.FRHorizontalLayout;
import com.fr.design.form.layout.FRVerticalLayout;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WTabDisplayPosition;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;

/**
 * @date: 2014-11-25-下午3:11:14
 */
public class XWCardTagLayout extends XWHorizontalBoxLayout {

    private static final int MIN_SIZE = 1;

    private String tagName = "Tab";

    private boolean switchingTab = false;

    //增加一个tabNameIndex防止tabFitLayout重名
    private int tabFitIndex = 0;
    private CardSwitchButton currentCard;

    private static final int WIDTH_SIDE_OFFSET = 57;

    private static final int HEIGHT_SIDE_OFFSET = 20;

    private static final int DEFAULT_BUTTON_HEIGHT = 40;

    public CardSwitchButton getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(CardSwitchButton currentCard) {
        this.currentCard = currentCard;
    }

    public int getTabFitIndex() {
        return tabFitIndex;
    }

    public void setTabFitIndex(int tabFitIndex) {
        this.tabFitIndex = tabFitIndex;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public boolean isSwitchingTab() {
        return switchingTab;
    }

    public void setSwitchingTab(boolean switchingTab) {
        this.switchingTab = switchingTab;
    }

    private XWCardLayout cardLayout;

    public XWCardTagLayout(WCardTagLayout widget, Dimension initSize) {
        super(widget, initSize);
        initLayoutManager();
    }

    /**
     * 构造函数
     */
    public XWCardTagLayout(WCardTagLayout widget, Dimension initSize, XWCardLayout cardLayout) {
        this(widget, initSize);
        this.cardLayout = cardLayout;
    }

    /**
     * 添加组件的监听事件
     *
     * @param e 事件
     * @date 2014-11-25-下午6:20:10
     */
    public void componentAdded(ContainerEvent e) {
        super.componentAdded(e);

        if (isSwitchingTab()) {
            return;
        }

        if (this.cardLayout == null) {
            initCardLayout();
        }

        int index = this.cardLayout.toData().getWidgetCount();
        //新加一个card
        String widgetName = tagName + getTabNameIndex();
        WTabFitLayout fitLayout = new WTabFitLayout(widgetName, tabFitIndex, currentCard);
        fitLayout.setTabNameIndex(getTabNameIndex());
        XWTabFitLayout tabFitLayout = new XWTabFitLayout(fitLayout, new Dimension());
        tabFitLayout.setxCardSwitchButton((XCardSwitchButton)this.getComponent(0));
        tabFitLayout.setBackupParent(cardLayout);
        cardLayout.add(tabFitLayout, widgetName);
        this.cardLayout.toData().setShowIndex(index);
        cardLayout.showCard();
    }


    private void initCardLayout() {
        XWCardTitleLayout titleLayout = (XWCardTitleLayout) this.getBackupParent();
        XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout) titleLayout.getBackupParent();

        this.cardLayout = borderLayout.getCardPart();
    }
    /**
     * 将WLayout转换为XLayoutContainer
     */
    public void convert() {
        isRefreshing = true;
        WCardTagLayout layout = (WCardTagLayout) this.toData();
        this.removeAll();
        for (int i = 0; i < layout.getWidgetCount(); i++) {
            Widget wgt = layout.getWidget(i);
            if (wgt != null) {
                initLayoutManager();
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, i);
                comp.setBackupParent(this);
            }
        }
//        setTabsAndAdjust();
        isRefreshing = false;
    }

    public String createDefaultName() {
        return "tabpane";
    }

    /**
     * 切换到非添加状态
     *
     * @return designer 表单设计器
     */
    public void stopAddingState(FormDesigner designer) {
        designer.stopAddingState();
    }

    //新增时去tabFitLayout名字中最大的Index+1，防止重名
    private int getTabNameIndex() {
        int tabNameIndex = 0;
        WCardLayout layout = this.cardLayout.toData();
        int size = layout.getWidgetCount();
        if (size < MIN_SIZE) {
            return tabNameIndex;
        }
        for (int i = 0; i < size; i++) {
            WTabFitLayout fitLayout = (WTabFitLayout) layout.getWidget(i);
            int tempIndex = fitLayout.getTabNameIndex();
            tabNameIndex = Math.max(tempIndex, tabNameIndex);
        }
        return ++tabNameIndex;
    }

    /**
     * 调整tab宽度
     * <p>
     * void
     */
    public void adjustComponentWidth() {
    }


    /**
     * 该布局需要隐藏，无需对边框进行操作
     *
     * @param
     */
    public void setBorder(Border border) {

    }

    @Override
    /**
     * 该布局隐藏，点击该布局时选中相应的tab布局主体
     * @param editingMouseListener 监听
     * @param e 鼠标点击事件
     *
     */
    public void respondClick(EditingMouseListener editingMouseListener,
                             MouseEvent e) {
        FormDesigner designer = editingMouseListener.getDesigner();
        SelectionModel selectionModel = editingMouseListener.getSelectionModel();

        if (e.getClickCount() <= 1) {
            selectionModel.selectACreatorAtMouseEvent(e);
        }
        if (editingMouseListener.stopEditing()) {
            if (this != designer.getRootComponent()) {
                ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
                editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
            }
        }
    }

    public int[] getDirections() {
        return ((XCreator)getParent()).getDirections();
    }

    public Rectangle getBounds() {
        return this.getParent().getBounds();
    }

    @Override
    public XLayoutContainer getTopLayout() {
        return this.getBackupParent().getTopLayout();
    }

    public void notShowInComponentTree(ArrayList<Component> path) {
		path.remove(0);
    }

    public boolean isSupportDrag(){
        return false;
    }

    @Override
    public void doLayout() {
        setTabsAndAdjust();
        //设置布局
        super.doLayout();
    }

    @Override
    protected void initLayoutManager() {
        FRFlowLayout frFlowLayout;
        if (isHori()) {
            frFlowLayout = new FRHorizontalLayout(((WCardTagLayout)toData()).getAlignment(), toData().getHgap(), toData().getVgap());
        }else{
            frFlowLayout = new FRVerticalLayout(((WCardTagLayout)toData()).getAlignment(), toData().getHgap(), toData().getVgap());
        }
        this.setFrFlowLayout(frFlowLayout);
        this.setLayout(frFlowLayout);
    }


    @Override
    public LayoutAdapter getLayoutAdapter() {
        if (isHori()) {
            return new FRHorizontalLayoutAdapter(this);
        }else{
            return new FRVerticalLayoutAdapter(this);
        }

    }

    private boolean isHori(){
        WTabDisplayPosition displayPosition = ((WCardTagLayout)this.toData()).getDisplayPosition();
        if(displayPosition == null){
            displayPosition = WTabDisplayPosition.TOP_POSITION;
        }
        return ComparatorUtils.equals(displayPosition, WTabDisplayPosition.TOP_POSITION) || ComparatorUtils.equals(displayPosition, WTabDisplayPosition.BOTTOM_POSITION);
    }


    /**
     * data属性改变触发其他操作
     *
     */
    public void firePropertyChange() {
        WCardTagLayout wCardTagLayout = (WCardTagLayout) this.toData();
        ((XWCardMainBorderLayout) getTopLayout()).resetTabDisplayPosition(wCardTagLayout.getDisplayPosition());
        //重置内部组件的大小和位置
        initLayoutManager();
        setTabsAndAdjust();
        repaint();
    }

    public void setTabsAndAdjust() {
        WCardTagLayout wCardTagLayout = (WCardTagLayout)this.toData();
        int tabLength = this.getComponentCount();
        Map<Integer, Integer> cardWidth = new HashMap<Integer, Integer>();
        Map<Integer, Integer> cardHeight = new HashMap<Integer, Integer>();
        for (int i = 0; i < tabLength; i++) {
            XCardSwitchButton temp = (XCardSwitchButton) this.getComponent(i);
            CardSwitchButton tempCard = (CardSwitchButton) temp.toData();
            String tempText = tempCard.getText();
            Font f = tempCard.getFont();
            FontMetrics fm = GraphHelper.getFontMetrics(f);

            switch (wCardTagLayout.getTextDirection()){
                case TEXT_HORI_DERECTION:
                    cardWidth.put(i,fm.stringWidth(tempText));
                    cardHeight.put(i,fm.getHeight());
                    break;
                case TEXT_VER_DIRECTION:
                    int perHeight = fm.getHeight();
                    int wordCount = tempText.length();
                    if(tempText.length() !=0 ){
                        cardWidth.put(i,fm.stringWidth(tempText)/tempText.length());
                    }else {
                        cardWidth.put(i, 0);
                    }
                    cardHeight.put(i,(perHeight+3)*wordCount);
                    break;
                default:
                    break;
            }
        }
        if(isHori()){
            adjustTabsH(tabLength, cardWidth, cardHeight);
        }else {
            adjustTabsV(tabLength, cardWidth, cardHeight);
        }
    }

    public void adjustTabsH(int tabLength, Map<Integer, Integer> width, Map<Integer, Integer> height) {
        if (width == null) {
            return;
        }
        XLayoutContainer parent = this.getBackupParent();
        int tabPaneSize = parent.getHeight();
        //调整XWCardTagLayout的高度
        int tempX = 0;
//        int maxHeight = DEFAULT_BUTTON_HEIGHT;
        for (int i = 0; i < tabLength; i++) {

            Rectangle rectangle = this.getComponent(i).getBounds();
            Integer cardWidth = width.get(i) + WIDTH_SIDE_OFFSET;
            Integer cardHeight = tabPaneSize;
            if(cardHeight < DEFAULT_BUTTON_HEIGHT){
                cardHeight = DEFAULT_BUTTON_HEIGHT;
            }
//            maxHeight = maxHeight > cardHeight ? maxHeight : cardHeight ;
            rectangle.setBounds(tempX, 0, cardWidth, cardHeight);
            tempX += cardWidth;
            XCardSwitchButton temp = (XCardSwitchButton) this.getComponent(i);
            setTabBtnSize(cardWidth, cardHeight, temp);
        }

        FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        LayoutAdapter layoutAdapter = AdapterBus.searchLayoutAdapter(formDesigner, parent);
        if (layoutAdapter != null) {
            parent.setBackupBound(parent.getBounds());
            layoutAdapter.fix(parent);
        }
    }

    public void setTabBtnSize(int cardWidth, int cardHeight,  XCardSwitchButton temp){
        Dimension dimension = new Dimension();
        dimension.setSize(cardWidth, cardHeight);
        CardSwitchButton cardSwitchButton = (CardSwitchButton) temp.toData();
        FRFont frFont = cardSwitchButton.getFont();
        UILabel label = temp.getContentLabel();
        label.setSize(dimension);
        label.setFont(frFont.applyResolutionNP(ScreenResolution.getScreenResolution()));
        label.setForeground(frFont.getForeground());
        temp.setContentLabel(label);
        temp.setSize(dimension);
        temp.setPreferredSize(new Dimension(cardWidth, cardHeight));
    }


    public void adjustTabsV(int tabLength, Map<Integer, Integer> width, Map<Integer, Integer> height) {
        if (width == null) {
            return;
        }
        XLayoutContainer parent = this.getBackupParent();
        int tabPaneSize = parent.getWidth();
        int tempY = 0;
//        int maxWidth = DEFAULT_BUTTON_HEIGHT;
        for (int i = 0; i < tabLength; i++) {
            Rectangle rectangle = this.getComponent(i).getBounds();
            Integer cardWidth = tabPaneSize;
//            maxWidth = maxWidth > cardWidth ? maxWidth : cardWidth;
            //先用这边的固定高度
            Integer cardHeight = height.get(i) + HEIGHT_SIDE_OFFSET;

            if(cardWidth < DEFAULT_BUTTON_HEIGHT){
                cardWidth = DEFAULT_BUTTON_HEIGHT;
            }
            if(cardHeight < DEFAULT_BUTTON_HEIGHT){
                cardHeight = DEFAULT_BUTTON_HEIGHT;
            }
            rectangle.setBounds(0, tempY, cardWidth, cardHeight);
            tempY += cardHeight;
            XCardSwitchButton temp = (XCardSwitchButton) this.getComponent(i);
            setTabBtnSize(cardWidth, cardHeight, temp);
        }

        FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        LayoutAdapter layoutAdapter = AdapterBus.searchLayoutAdapter(formDesigner, parent);
        if (layoutAdapter != null) {
            parent.setBackupBound(parent.getBounds());
            layoutAdapter.fix(parent);
        }
    }

}