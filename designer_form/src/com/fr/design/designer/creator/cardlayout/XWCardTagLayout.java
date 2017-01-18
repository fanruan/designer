/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseEvent;

import javax.swing.border.Border;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWHorizontalBoxLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;

/**
 *
 *
 * @date: 2014-11-25-下午3:11:14
 */
public class XWCardTagLayout extends XWHorizontalBoxLayout {
	
	private static final int MIN_SIZE = 1;
	
	private String tagName = "Tab";
	
	//增加一个tabNameIndex防止tabFitLayout重名
	private int tabFitIndex = 0;
	private CardSwitchButton currentCard;

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

	private XWCardLayout cardLayout;
	
	public XWCardTagLayout(WCardTagLayout widget, Dimension initSize){
		super(widget, initSize);
	}
	
	/**
	 * 构造函数
	 */
	public XWCardTagLayout(WCardTagLayout widget, Dimension initSize, XWCardLayout cardLayout) {
		super(widget, initSize);
		
		this.cardLayout = cardLayout;
	}

	/**
	 * 添加组件的监听事件
	 * 
	 * @param e 事件
	 * 
	 *
	 * @date 2014-11-25-下午6:20:10
	 * 
	 */
	public void componentAdded(ContainerEvent e) {
		super.componentAdded(e);
		
		if(this.cardLayout == null){
			initCardLayout();
		}
		
		int index = this.cardLayout.toData().getWidgetCount();
		//新加一个card
		String widgetName = tagName+getTabNameIndex();
		WTabFitLayout fitLayout = new WTabFitLayout(widgetName,tabFitIndex,currentCard);
		fitLayout.setTabNameIndex(getTabNameIndex());
		XWTabFitLayout tabFitLayout = new XWTabFitLayout(fitLayout, new Dimension());
		tabFitLayout.setBackupParent(cardLayout);
		cardLayout.add(tabFitLayout, widgetName);
		this.cardLayout.toData().setShowIndex(index);
		cardLayout.showCard();
	}
	
	private void initCardLayout(){
		XWCardTitleLayout titleLayout = (XWCardTitleLayout)this.getBackupParent();
		XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
		
		this.cardLayout = borderLayout.getCardPart();
	}
	
    /**
     * 将WLayout转换为XLayoutContainer
     */
    public void convert() {
        isRefreshing = true;
        WCardTagLayout layout = (WCardTagLayout)this.toData();
        this.removeAll();
        for (int i = 0; i < layout.getWidgetCount(); i++) {
            Widget wgt = layout.getWidget(i);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, i);
                comp.setBackupParent(this);
            }
        }
        isRefreshing = false;
    }
    
    /**
     * 切换到非添加状态
     * 
     * @return designer 表单设计器
     */
    public void stopAddingState(FormDesigner designer){
    	designer.stopAddingState();
    	return;
    }
    
    //新增时去tabFitLayout名字中最大的Index+1，防止重名
    private int getTabNameIndex(){
    	int tabNameIndex = 0;
    	WCardLayout layout = this.cardLayout.toData();
    	int size = layout.getWidgetCount();
    	if(size < MIN_SIZE){
    		return tabNameIndex;
    	}
		for(int i=0;i<size;i++){
			WTabFitLayout fitLayout = (WTabFitLayout) layout.getWidget(i);
			int tempIndex = fitLayout.getTabNameIndex();
			tabNameIndex = Math.max(tempIndex, tabNameIndex);
		}
		return ++tabNameIndex;
    }
    
	/**
	 * 调整tab宽度
	 * 
	 * void
	 */
	public void adjustComponentWidth(){
	}
	
	
	/**
	 * 该布局需要隐藏，无需对边框进行操作
	 * @param 边框
	 * 
	 */
    public void setBorder(Border border) {
    	return;
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

		XWCardTitleLayout titleLayout = (XWCardTitleLayout) this.getBackupParent();
		if(titleLayout != null){
			XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
			if(mainLayout != null){
				XWCardLayout cardLayout = mainLayout.getCardPart();
				selectionModel.setSelectedCreator(cardLayout);
			}
		}
		
		if (editingMouseListener.stopEditing()) {
			if (this != designer.getRootComponent()) {
				ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
				editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
			}
		}
	}

	@Override
	public XLayoutContainer getTopLayout() {
		return this.getBackupParent().getTopLayout();
	}
}