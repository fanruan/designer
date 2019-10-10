package com.fr.design.designer.creator.cardlayout;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XButton;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardAddButton;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.ComparatorUtils;

import com.fr.general.cardtag.DefaultTemplateStyle;

import javax.swing.Icon;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class XCardAddButton extends XButton {
	
	private XWCardTagLayout tagLayout;
	private XWCardLayout cardLayout;
	

	private static final int DEFAULT_BUTTON_WIDTH = 80;
	private static final int ICON_OFFSET = 8;

	
	private static Icon ADD_ICON = BaseUtils.readIcon("/com/fr/design/form/images/add.png");
	private Icon addIcon = ADD_ICON;
	
	/**
	 * card布局添加card按钮
	 */

	public XWCardTagLayout getTagLayout() {
		return tagLayout;
	}

	public void setTagLayout(XWCardTagLayout tagLayout) {
		this.tagLayout = tagLayout;
	}

	public XWCardLayout getCardLayout() {
		return cardLayout;
	}

	public void setCardLayout(XWCardLayout cardLayout) {
		this.cardLayout = cardLayout;
	}

	/**
	 * 构造函数
	 * @param widget 按钮
	 * @param initSize 大小
	 */
	public XCardAddButton(CardAddButton widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	 * 构造函数
	 * @param widget 按钮
	 * @param initSize 大小
	 */
	public XCardAddButton(CardAddButton widget, Dimension initSize, XWCardTagLayout fit, XWCardLayout cardLayout) {
		super(widget, initSize);
		this.tagLayout = fit;
		this.cardLayout = cardLayout;
	}
	
	/**
	 * 响应点击事件
	 * @param editingMouseListener 事件处理器
	 * @param e 点击事件
	 * 
	 */
    @Override
	public void respondClick(EditingMouseListener editingMouseListener, MouseEvent e){
		FormDesigner designer = editingMouseListener.getDesigner();
		designer.fireTargetModified();
    	
    	// addbutton对应的XWCardLayout和XWCardTagLayout暂未存入到xml中，重新打开之后先根据父子层获取
    	if(cardLayout == null && tagLayout ==null ){
    		initRelateLayout();
    	}
		if (cardLayout == null) {
			throw new IllegalArgumentException("cardLayout can not be null");
		}
    	int index = cardLayout.toData().getWidgetCount();
    	
    	//添加新的tab，并将原来的设为未选中状态
    	setTabUnselected();
    	addTab(index);
    	this.tagLayout.adjustComponentWidth();
    	
		if (editingMouseListener.stopEditing()) {
			ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
			editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
		}
		
		FormHierarchyTreePane.getInstance().refreshRoot();
		//将焦点切换到新增的tab对应的tabfitLayout上
		showNewTab(editingMouseListener,index);
		tagLayout.setTabsAndAdjust();
		LayoutUtils.layoutRootContainer(designer.getRootComponent());
    }

	@Override
	public UIPopupMenu createPopupMenu(FormDesigner formDesigner) {
		return UIPopupMenu.EMPTY;  // 不要菜单
	}
    
    private void initRelateLayout(){
    	XWCardTitleLayout titleLayout = (XWCardTitleLayout)this.getBackupParent();
		this.tagLayout = titleLayout.getTagPart();
		
		XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
		this.cardLayout = borderLayout.getCardPart();
    }
    
    @Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawAddIcon(g2d);
    }
    
	private void drawAddIcon(Graphics2D g2d){
		int width = this.getWidth();
		int height = this.getHeight();
		addIcon.paintIcon(this, g2d, width / 2 - ICON_OFFSET, height / 2 - ICON_OFFSET);
	}
	
	//将原来的tab页设置为未选中状态
	private void setTabUnselected(){
		for(int i=0;i<this.tagLayout.getComponentCount();i++){
			WCardTagLayout layout = (WCardTagLayout) this.tagLayout.toData();
			CardSwitchButton button = layout.getSwitchButton(i);
			button.setShowButton(false);
		}
	}
	
	//新增tab
	private void addTab(int index){
		Dimension dimension = new Dimension();
		dimension.width = DEFAULT_BUTTON_WIDTH;
    	
		String cardLayoutName = cardLayout.toData().getWidgetName();
    	CardSwitchButton titleButton = new CardSwitchButton(index,cardLayoutName);
		WCardTagLayout layout = (WCardTagLayout) this.tagLayout.toData();
        if(!ComparatorUtils.equals(layout.getTemplateStyle().getStyle(), DefaultTemplateStyle.DEFAULT_TEMPLATE_STYLE)){
            titleButton.setInitialBackground(layout.getTemplateStyle().getTabDefaultBackground());
            titleButton.setCustomStyle(true);
        }
    	//设置标题
    	titleButton.setText(getTabTitleName(layout));
    	XCardSwitchButton showButton = new XCardSwitchButton(titleButton, dimension, cardLayout, tagLayout);
    	titleButton.setShowButton(true);
		showButton.setBackupParent(tagLayout);
    	this.tagLayout.setCurrentCard(titleButton);
    	this.tagLayout.setTabFitIndex(index);
    	this.tagLayout.add(showButton);
	}
	
	//切换焦点到新增tab页
	private void showNewTab(EditingMouseListener editingMouseListener, int index){
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();
		XWTabFitLayout tabFitLayout = (XWTabFitLayout) cardLayout.getComponent(index);
		XCardSwitchButton xCardSwitchButton = (XCardSwitchButton) this.tagLayout.getComponent(index);
		tabFitLayout.setxCardSwitchButton(xCardSwitchButton);
		selectionModel.setSelectedCreator(tabFitLayout);
	}
	
    //新增时去tabFitLayout名字中最大的Index+1，防止重名
    private String getTabTitleName(WCardTagLayout layout){
    	int size = layout.getWidgetCount();
    	String prefix = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Title");
    	String newTextName = prefix + size;

		for (int i = 0; i < size; i++) {
			CardSwitchButton button = layout.getSwitchButton(i);
			String _text = button.getText();
			if (ComparatorUtils.equals(_text, newTextName)) {
				int lastSize = Integer.parseInt(newTextName.replaceAll(prefix, ""));
				newTextName = prefix + (lastSize + 1);
				i = 0;
			}
		}
		return newTextName;
    }

	@Override
	public XLayoutContainer getTopLayout() {
		return this.getBackupParent().getTopLayout();
	}
}
