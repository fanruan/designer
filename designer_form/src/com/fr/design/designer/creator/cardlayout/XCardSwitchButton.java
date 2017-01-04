/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import com.fr.base.BaseUtils;
import com.fr.base.background.ColorBackground;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XButton;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.mainframe.JForm;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.WidgetTitle;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Background;
import com.fr.general.FRFont;
import com.fr.general.Inter;

/**
 *
 *
 * @date: 2014-11-27-上午10:28:14
 */
public class XCardSwitchButton extends XButton {

	private XWCardLayout cardLayout;
	private XWCardTagLayout tagLayout;
	
	private static final int LEFT_GAP = 16;
	public static final Color NORMAL_GRAL = new Color(236,236,236);
	public static final Color CHOOSED_GRAL = new Color(222,222,222);
	
	private static final int MIN_SIZE = 1;
	
	// 删除按钮识别区域偏移量
	private static final int RIGHT_OFFSET = 15;
	private static final int TOP_OFFSET = 25;
	
	// tab按钮里的字体因为按钮内部的布局看起来比正常的要小，加个调整量
	private static final int FONT_SIZE_ADJUST = 2;

	private Background selectBackground;
	private boolean isCustomStyle;
	
	
	
	private static Icon MOUSE_COLSE = BaseUtils.readIcon("/com/fr/design/images/buttonicon/close_icon.png");
	private static String COLORBACKGROUNDTYPE = "ColorBackground";
	private static String DEFAULTTYPE = "default";

	private Icon closeIcon = MOUSE_COLSE;
	
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

	public boolean isCustomStyle() {
		return isCustomStyle;
	}

	public void setCustomStyle(boolean customStyle) {
		isCustomStyle = customStyle;
	}

	public Background getSelectBackground() {
		return selectBackground;
	}

	public void setSelectBackground(Background selectBackground) {
		this.selectBackground = selectBackground;
	}

	public XCardSwitchButton(CardSwitchButton widget, Dimension initSize) {
		super(widget, initSize);
	}

	public XCardSwitchButton(CardSwitchButton widget, Dimension initSize,
			XWCardLayout cardLayout, XWCardTagLayout tagLayout) {
		super(widget, initSize);
		this.cardLayout = cardLayout;
		this.tagLayout = tagLayout;
	}

	/**
	 * 响应点击事件
	 * 
	 * @param editingMouseListener
	 *            事件处理器
	 * @param e
	 *            点击事件
	 * 
	 */
	public void respondClick(EditingMouseListener editingMouseListener,
			MouseEvent e) {
		FormDesigner designer = editingMouseListener.getDesigner();
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();

		//关闭重新打开，相关的layout未存到xml中，初始化
		if(cardLayout == null){
			initRalateLayout(this);
		}
		
		//获取当前tab的index
		XCardSwitchButton button = this;
		CardSwitchButton currentButton = (CardSwitchButton) button.toData();
		int index = currentButton.getIndex();
		
		//点击删除图标时
		if (isSeletectedClose(e,designer)) {
			//当删除到最后一个tab时，删除整个tab布局
			if(tagLayout.getComponentCount() <= MIN_SIZE){
				deleteTabLayout(selectionModel, designer);
				return;
			}
			deleteCard(button,index);
			this.tagLayout.adjustComponentWidth();
			designer.fireTargetModified();
			LayoutUtils.layoutRootContainer(designer.getRootComponent());
			FormHierarchyTreePane.getInstance().refreshRoot();
			return;
		}
		
		//将当前tab按钮改为选中状态
		changeButtonState(index);
		
		// 切换到当前tab按钮对应的tabFitLayout
		XWTabFitLayout tabFitLayout = (XWTabFitLayout) cardLayout.getComponent(index);
		tabFitLayout.setxCardSwitchButton(this);
		selectionModel.setSelectedCreator(tabFitLayout);
		
		if (editingMouseListener.stopEditing()) {
			ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer,
					this);
			editingMouseListener.startEditing(this,
					adapter.getDesignerEditor(), adapter);
		}
		
	}
	
	//删除card，同时修改其他switchbutton和tabfit的index
	private void deleteCard(XCardSwitchButton button,int index){
		String titleName = button.getContentLabel().getText();
		int value = JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Designer_ConfirmDialog_Content") + "“" + titleName + "”",
				Inter.getLocText("FR-Designer_ConfirmDialog_Title"),JOptionPane.YES_NO_OPTION);
		if (value != JOptionPane.OK_OPTION) {
			return;
		}
		tagLayout.remove(button);
		// 先清除该tab内部组件，否在再显示上有样式的残留
		XWTabFitLayout tabLayout = (XWTabFitLayout)cardLayout.getComponent(index);
		tabLayout.removeAll();
		cardLayout.remove(index);
		for (int i = 0; i < tagLayout.getComponentCount(); i++) {
			XCardSwitchButton temp = (XCardSwitchButton) tagLayout.getComponent(i);
			CardSwitchButton tempButton = (CardSwitchButton) temp.toData();
			XWTabFitLayout fit = (XWTabFitLayout) cardLayout.getComponent(i);
			WTabFitLayout layout = (WTabFitLayout) fit.toData();
			int currentIndex = tempButton.getIndex();
			int tabFitIndex = layout.getIndex();
			if (currentIndex > index) {
				tempButton.setIndex(--currentIndex);
			}
			if (tabFitIndex > index) {
				layout.setIndex(--tabFitIndex);
			}
		}
	}
	
	
	//SwitchButton对应的XWCardLayout和XWCardTagLayout暂未存到xml中,重新打开时根据父子层关系获取
	private void initRalateLayout(XCardSwitchButton button){
		this.tagLayout = (XWCardTagLayout)this.getBackupParent();
		XWCardTitleLayout titleLayout = (XWCardTitleLayout) this.tagLayout.getBackupParent();
		XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
		this.cardLayout = borderLayout.getCardPart();
	}
	
	//是否进入点击关闭按钮区域
	private boolean isSeletectedClose(MouseEvent e,FormDesigner designer){
		
		int diff = designer.getArea().getHorScrollBar().getValue();
		
		// mouse position
		int ex = e.getX() + diff;
		int ey = e.getY();
		
		//获取tab布局的位置,鼠标相对于tab按钮的位置
		XLayoutContainer mainLayout = cardLayout.getBackupParent();
		Point point = mainLayout.getLocation();
		double mainX = point.getX();
		double mainY = point.getY();
		
		// 参数界面对坐标的影响
		JForm jform = (JForm)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
		if(jform.getFormDesign().getParaComponent() != null){
			ey -= jform.getFormDesign().getParaHeight();
		}
		
		//减掉tab布局的相对位置
		ex -= mainX;
		ey -= mainY;
		
		// button position
		XCardSwitchButton button = this;
		Point position = button.getLocation();
		int width = button.getWidth();
		int height = button.getHeight();
		
		// 鼠标进入按钮右侧删除图标区域
		double recX = position.getX() + (width - RIGHT_OFFSET);
		double recY = position.getY() + (height - TOP_OFFSET);
		
		return (recX < ex && ex < recX + RIGHT_OFFSET &&  ey < recY);
	}
	
	//将当前switchButton改为选中状态
	private void changeButtonState(int index){
		for(int i=0;i<this.tagLayout.getComponentCount();i++){
			XCardSwitchButton temp = (XCardSwitchButton) tagLayout.getComponent(i);
			CardSwitchButton tempButton = (CardSwitchButton) temp.toData();
			tempButton.setShowButton(tempButton.getIndex()==index);
		}
	}
	
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBackgorund();
        drawTitle();
		Dimension panelSize = this.getContentLabel().getSize();
		this.getContentBackground().paint(g, new Rectangle2D.Double(0, 0, panelSize.getWidth(), panelSize.getHeight()));
		drawCloseIcon(g2d);
    }
	
    //画删除图标
	private void drawCloseIcon(Graphics2D g2d){
		closeIcon.paintIcon(this, g2d,this.getWidth()-LEFT_GAP,0);
	}
	
	//画背景
	private void drawBackgorund(){
        CardSwitchButton button = (CardSwitchButton)this.toData();
		Background currentBackground;
		currentBackground = this.getSelectBackground();
		//这边就是button的背景图片,图片的是image,默认的是color,所以不应该是针对null的判断
		String type = currentBackground != null? currentBackground.getBackgroundType() : DEFAULTTYPE;
		if (type.equals(COLORBACKGROUNDTYPE) || type.equals(DEFAULTTYPE)) {
			ColorBackground background;
			if(button.isShowButton()){
				this.rebuid();
				background = ColorBackground.getInstance(CHOOSED_GRAL);
				this.setContentBackground(background);
			}else{
				this.rebuid();
				background = ColorBackground.getInstance(NORMAL_GRAL);
				this.setContentBackground(background);
			}
		}
	}
	
	//画标题
	private void drawTitle() {
		CardSwitchButton button = (CardSwitchButton) this.toData();
		this.setButtonText(button.getText());
		if (this.cardLayout == null) {
			initRalateLayout(this);
		}

		LayoutBorderStyle style = this.cardLayout.toData().getBorderStyle();

		// 标题部分
		WidgetTitle title = style.getTitle();
		FRFont font = title.getFrFont();
		FRFont newFont = FRFont.getInstance(font.getName(),font.getStyle(),font.getSize() + FONT_SIZE_ADJUST);
		UILabel label = this.getContentLabel();
		label.setFont(newFont);
		label.setForeground(font.getForeground());
		Background background = title.getBackground();
		if (background != null) {
			if(button.isShowButton() && selectBackground != null){
				this.setContentBackground(selectBackground);
			}else if (button.isShowButton() && selectBackground == null){
				background = ColorBackground.getInstance(CHOOSED_GRAL);
				this.setContentBackground(background);
			} else {
				this.setContentBackground(background);
			}
		}
	}
	
	//删除tab布局
	private void deleteTabLayout(SelectionModel selectionModel,FormDesigner designer){
		XLayoutContainer mainLayout = this.cardLayout.getBackupParent();
		if(mainLayout != null){
			selectionModel.setSelectedCreator(mainLayout);
			selectionModel.deleteSelection();
		}
		LayoutUtils.layoutRootContainer(designer.getRootComponent());
		FormHierarchyTreePane.getInstance().refreshRoot();
		selectionModel.setSelectedCreator(designer.getRootComponent());
	}

	@Override
	public XLayoutContainer getTopLayout() {
		return this.getBackupParent().getTopLayout();
	}
	
}