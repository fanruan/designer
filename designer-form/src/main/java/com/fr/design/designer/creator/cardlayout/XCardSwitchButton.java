/**
 *
 */
package com.fr.design.designer.creator.cardlayout;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.background.ColorBackground;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XButton;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.mainframe.JForm;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.WTabTextDirection;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Background;
import com.fr.general.act.BorderPacker;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.act.TitlePacker;
import com.fr.general.cardtag.TemplateStyle;
import com.fr.stable.ProductConstants;
import com.fr.stable.unit.PT;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 *
 *
 * @date: 2014-11-27-上午10:28:14
 */
public class XCardSwitchButton extends XButton {

	private static final int LEFT_GAP = 16;
	private static Icon MOUSE_CLOSE = BaseUtils.readIcon("/com/fr/design/images/buttonicon/close_icon.png");

	//设置的图片类型
	private static final String COLOR_BACKGROUND_TYPE = "ColorBackground";
	private static final String DEFAULT_TYPE = "default";

	//默认颜色
	public static final Color NORMAL_GRAL = new Color(236,236,236);
	public static final Color CHOOSED_GRAL = new Color(222,222,222);

	private static final int MIN_SIZE = 1;

	private static final int HALF_NUMBER = 2;

	// 删除按钮识别区域偏移量
	private static final int CLOSE_ICON_RIGHT_OFFSET = 15;
	private static final int CLOSE_ICON_TOP_OFFSET = 15;

	// tab按钮里的字体因为按钮内部的布局看起来比正常的要小，加个调整量
	private static final int FONT_SIZE_ADJUST = 2;

	//文字竖排时用来计算文字大小
	private static final int RESLUTION = 120;


	private XWCardLayout cardLayout;
	private XWCardTagLayout tagLayout;

	private Background selectBackground;

	private UILabel label;

	private Icon closeIcon = MOUSE_CLOSE;

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

	public Background getSelectBackground() {
		return selectBackground;
	}

	public void setSelectBackground(Background selectBackground) {
		this.selectBackground = selectBackground;
	}

	public UILabel getLabel() {
		return label;
	}

	public void setLabel(UILabel label) {
		this.label = label;
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
	@Override
	public void respondClick(EditingMouseListener editingMouseListener,
							 MouseEvent e) {
		FormDesigner designer = editingMouseListener.getDesigner();
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();

		//关闭重新打开，相关的layout未存到xml中，初始化
		if(cardLayout == null){
			initRelateLayout();
		}

		//获取当前tab的index
		CardSwitchButton currentButton = (CardSwitchButton) this.toData();
		int index = currentButton.getIndex();
		int maxIndex = cardLayout.getComponentCount() - 1;

		//点击删除图标时
		if (isSelectedClose(e, designer)) {
			//当删除到最后一个tab时，删除整个tab布局
			if(tagLayout.getComponentCount() <= MIN_SIZE){
				deleteTabLayout(selectionModel, designer);
				return;
			}
			deleteCard(this, index);
			designer.fireTargetModified();
			LayoutUtils.layoutRootContainer(designer.getRootComponent());
			FormHierarchyTreePane.getInstance().refreshRoot();
			return;
		}

		//将当前tab按钮改为选中状态
		changeButtonState(index);

		// 切换到当前tab按钮对应的tabFitLayout
		XWTabFitLayout tabFitLayout = (XWTabFitLayout) cardLayout.getComponent(index);
		XCardSwitchButton xCardSwitchButton = (XCardSwitchButton) this.tagLayout.getComponent(index);
		tabFitLayout.setxCardSwitchButton(xCardSwitchButton);
		selectionModel.setSelectedCreator(tabFitLayout);

		if (editingMouseListener.stopEditing()) {
			ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
			editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
		}
		if(SwingUtilities.isRightMouseButton(e)){
			showPopupMenu(editingMouseListener, e, index, maxIndex);
		}
	}

	private void showPopupMenu(EditingMouseListener editingMouseListener, MouseEvent e, int index, int maxIndex) {
		JPopupMenu jPopupMenu = new JPopupMenu();
		Action first = new TabMoveFirstAction(editingMouseListener.getDesigner(), this);
		Action prev = new TabMovePrevAction(editingMouseListener.getDesigner(), this);
		Action next = new TabMoveNextAction(editingMouseListener.getDesigner(), this);
		Action end = new TabMoveEndAction(editingMouseListener.getDesigner(), this);
		if (index == 0){
			first.setEnabled(false);
			prev.setEnabled(false);
		}
		if (index == maxIndex){
			next.setEnabled(false);
			end.setEnabled(false);
		}
		jPopupMenu.add(first);
		jPopupMenu.add(prev);
		jPopupMenu.add(next);
		jPopupMenu.add(end);
		GUICoreUtils.showPopupMenu(jPopupMenu, editingMouseListener.getDesigner(), e.getX(), e.getY());
	}

	@Override
	public UIPopupMenu createPopupMenu(FormDesigner formDesigner) {
		return UIPopupMenu.EMPTY;  // 自己有一个showPopupMenu，不需要使用通用的弹出菜单
	}

	//删除card，同时修改其他switchbutton和tabfit的index
	private void deleteCard(XCardSwitchButton button, int index){
		String titleName = button.getContentLabel().getText();
		int value = FineJOptionPane.showConfirmDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Confirm_Dialog_Content") + "“" + titleName + "”",
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm"), JOptionPane.YES_NO_OPTION);
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
	private void initRelateLayout(){
		this.tagLayout = (XWCardTagLayout)this.getBackupParent();
		XWCardTitleLayout titleLayout = (XWCardTitleLayout) this.tagLayout.getBackupParent();
		XWCardMainBorderLayout borderLayout = (XWCardMainBorderLayout)titleLayout.getBackupParent();
		this.cardLayout = borderLayout.getCardPart();
	}

	//是否进入点击关闭按钮区域
	private boolean isSelectedClose(MouseEvent e, FormDesigner designer){

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
		JForm jform = (JForm) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
		if(jform.getFormDesign().getParaComponent() != null){
			ey -= jform.getFormDesign().getParaHeight();
		}

		//减掉tab布局的相对位置
		ex -= mainX;
		ey -= mainY;

		XLayoutContainer titleLayout = tagLayout.getBackupParent();
		Point titlePoint = titleLayout.getLocation();
		// button position
		XCardSwitchButton button = this;
		Point position = button.getLocation();
		int width = button.getWidth();

		// 鼠标进入按钮右侧删除图标区域
		double recX = position.getX() + titlePoint.getX() +  (width - CLOSE_ICON_RIGHT_OFFSET);
		double recY = position.getY() + titlePoint.getY() + CLOSE_ICON_TOP_OFFSET;

		return (recX < ex && ex < recX + CLOSE_ICON_RIGHT_OFFSET &&  ey < recY && ey > position.getY());
	}

	//将当前switchButton改为选中状态
	private void changeButtonState(int index) {
		for (int i = 0; i < this.tagLayout.getComponentCount(); i++) {
			XCardSwitchButton temp = (XCardSwitchButton) tagLayout.getComponent(i);
			CardSwitchButton tempButton = (CardSwitchButton) temp.toData();
			tempButton.setShowButton(tempButton.getIndex() == index);
		}
	}

    @Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
		CardSwitchButton button = (CardSwitchButton) this.toData();
		TitlePacker widgetTitle = getWidgetTitle();
        drawBackground(button, widgetTitle);
        drawTitle(button, widgetTitle);
		Dimension panelSize = this.getContentLabel().getSize();
		this.getContentBackground().paint(g, new Rectangle2D.Double(0, 0, panelSize.getWidth(), panelSize.getHeight()));
		drawCloseIcon(g2d);
    }

    //画删除图标
	private void drawCloseIcon(Graphics2D g2d){
		closeIcon.paintIcon(this, g2d, this.getWidth() - LEFT_GAP, 0);
	}

	//画背景
	private void drawBackground(CardSwitchButton button, TitlePacker widgetTitle){
		Background background = widgetTitle.getBackground() == null ? ColorBackground.getInstance(NORMAL_GRAL) : widgetTitle.getBackground();
		TemplateStyle templateStyle = ((WCardTagLayout) tagLayout.toData()).getTemplateStyle();
		//获取当前tab的index
		CardSwitchButton currentButton = (CardSwitchButton) this.toData();
		int index = currentButton.getIndex();
		XWTabFitLayout tabFitLayout = (XWTabFitLayout) cardLayout.getComponent(index);
		WTabFitLayout wTabFitLayout = tabFitLayout.getWTabFitLayout();
		Background initialBackground = wTabFitLayout.getInitialBackground();
		Background selectBackground = wTabFitLayout.getClickBackground();
		if (button.isShowButton()) {
			this.setContentBackground(selectBackground == null ? templateStyle.getSelectBackground() : selectBackground);
		} else {
			this.setContentBackground(initialBackground == null ? background : initialBackground);
		}
	}

	//画标题
	private void drawTitle(CardSwitchButton button, TitlePacker widgetTitle) {
		String titleText = button.getText();
		this.setButtonText(titleText);
        FRFont font = widgetTitle.getFrFont();
        FRFont newFont = FRFont.getInstance(font.getName(),font.getStyle(),font.getSize() + FONT_SIZE_ADJUST);
		UILabel label = this.getContentLabel();
		label.setFont(newFont);
		label.setForeground(font.getForeground());

	}

	private TitlePacker getWidgetTitle() {
		if (this.cardLayout == null) {
			initRelateLayout();
		}
		BorderPacker style = this.cardLayout.toData().getBorderStyle();
		return style.getTitle();
	}

	//删除tab布局
	private void deleteTabLayout(SelectionModel selectionModel, FormDesigner designer){
		String titleName = this.getContentLabel().getText();
		int value = FineJOptionPane.showConfirmDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Confirm_Dialog_Content") + "“" + titleName + "”",
				com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm"), JOptionPane.YES_NO_OPTION);
		if (value != JOptionPane.OK_OPTION) {
			return;
		}
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


    @Override
    public void doLayout() {
        super.doLayout();
    }


	/**
	 * 控件树里需要隐藏xwcardmainLayout，返回其子组件xwcardLayout；
	 * 标题样式下，this.getComponent(1)==xwcardLayout
	 * 标准样式下，this.getComponent(0)==xwcardLayout
	 * @return 子组件xwcardLayout
	 */
	@Override
	public XCreator getXCreator() {
		//根据index获取对应的tabFitLayout
		int index = ((CardSwitchButton) this.toData()).getIndex();
		//关闭重新打开，相关的layout未存到xml中，初始化
		if(cardLayout == null){
			initRelateLayout();
		}
		return (XCreator) cardLayout.getComponent(index);
	}


	@Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		label = this.getContentLabel();
	}

	@Override
	public void firePropertyChange() {
		super.firePropertyChange();
		tagLayout.setTabsAndAdjust();
		repaint();
	}

    @Override
    protected UILabel initContentLabel() {
        return  new CardSwitchBtnLabel();
    }

	public class CardSwitchBtnLabel extends UILabel {

		public CardSwitchBtnLabel() {
			updateUI();
		}

		@Override
		public void updateUI() {
			setUI(new CardSwitchBtnLabelUI());
		}
	}


	public class CardSwitchBtnLabelUI extends BasicLabelUI {
		private static final int DOT_COUNT = 3;
		private static final String DOT = ".";
		private static final int DOTS_LINESPACE = 6;
		private static final int DOTS_HEIGHT = 10;

		@Override
		public void paint(Graphics g, JComponent c) {
			WCardTagLayout wCardTagLayout = (WCardTagLayout) tagLayout.toData();
			if (ComparatorUtils.equals(wCardTagLayout.getTextDirection(), WTabTextDirection.TEXT_VER_DIRECTION)) {
				//绘制文本竖排展示
				paintVerticalText(g);
			} else {
				super.paint(g, c);
			}
		}

		private void paintVerticalText(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			int width = XCardSwitchButton.this.getWidth();
			int height = XCardSwitchButton.this.getHeight();
			CardSwitchButton button = (CardSwitchButton) XCardSwitchButton.this.toData();
			String titleText = button.getText();
			java.util.List verticalTextList = new ArrayList();
			StringBuilder titleStringBuf = new StringBuilder();
			TitlePacker title = getWidgetTitle();
			FRFont font = title.getFrFont();
			int fontSize = font.getSize() + FONT_SIZE_ADJUST;
			FRFont newFont = FRFont.getInstance(font.getName(), font.getStyle(), fontSize);
			FontMetrics fm = GraphHelper.getFontMetrics(newFont);
			for (int i = 0; i < titleText.length(); i++) {
				titleStringBuf.append(titleText.charAt(i));
				verticalTextList.add(titleStringBuf.substring(0, titleStringBuf.length()));
				titleStringBuf.delete(0, titleStringBuf.length());
			}
			int textAscent = fm.getAscent();
			int textHeight = fm.getHeight();
			int textY = 0;
			textY += textAscent;
			for (int i = 0; i < verticalTextList.size(); i++) {
				String paint_str = (String) verticalTextList.get(i);

				GraphHelper.drawString(g2d, paint_str,
						(width - fm.stringWidth(paint_str)) / (HALF_NUMBER * 1.0D), textY);
				textY += textHeight;
				textY += PT.pt2pix(0, RESLUTION);
				if (textY > height - textHeight && i < verticalTextList.size() - 1) {
					textY -= DOTS_HEIGHT;
					paintDots(g2d, textY, (width - fm.stringWidth(paint_str)) / HALF_NUMBER);
					break;
				}
			}
		}

		public void paintDots(Graphics2D g2d, int startY, int startX) {
			for (int i = 0; i < DOT_COUNT; i++) {
				GraphHelper.drawString(g2d, DOT, startX, startY);
				startY += DOTS_LINESPACE;
				startY += PT.pt2pix(0, RESLUTION);
			}
		}
	}
}
