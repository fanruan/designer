/**
 *
 */
package com.fr.design.designer.creator.cardlayout;

import com.fr.base.GraphHelper;
import com.fr.base.iofile.attr.SharableAttrMark;
import com.fr.design.constants.UIConstants;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRCardMainBorderLayoutAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.designer.properties.mobile.MobileBookMarkCombinePropertyUI;
import com.fr.design.form.util.FormDesignerUtils;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetHelpDialog;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.event.Listener;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WCardLayout;
import com.fr.form.ui.container.WTabDisplayPosition;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;

import com.fr.general.act.BorderPacker;
import com.fr.share.ShareConstants;
import com.fr.stable.Constants;

import javax.swing.Icon;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * card布局主体框架
 *
 *
 *
 * @date: 2014-12-9-下午9:59:31
 */
public class XWCardMainBorderLayout extends XWBorderLayout {

	private Icon controlMode = IOUtils.readIcon(IconPathConstants.TD_EL_SHARE_HELP_ICON_PATH);
	private static final int CENTER = 1;
	private static final int NORTH = 0;
	private static final int TITLE_STYLE = 2;

	private static final int EDIT_BTN_WIDTH = 75;
	private static final int EDIT_BTN_HEIGHT = 20;
	private static final int BORDER_WIDTH = 1;

	private final int CARDMAINLAYOUT_CHILD_COUNT = 1;

	/**
	 * 构造函数
	 */
	public XWCardMainBorderLayout(WCardMainBorderLayout border, Dimension dimension) {
		super(border, dimension);
	}

	/**
	 * 获取当前组件里的控件
	 *
	 * @return 控件
	 *
	 *
	 * @date 2014-12-10-下午1:46:33
	 *
	 */
	@Override
	public WCardMainBorderLayout toData() {
		return (WCardMainBorderLayout) super.toData();
	}

	/**
	 * 添加标题区域
	 *
	 * @param title 标题区域
	 *
	 *
	 * @date 2014-12-10-下午1:50:56
	 *
	 */
	public void addTitlePart(XWCardTitleLayout title, String position){
		toData().setTabPosition(position);
		this.add(title, position);
	}

	public int getTitleWidth(){
		String position = toData().getTabPosition();
		if(ComparatorUtils.equals(WBorderLayout.NORTH, position) || ComparatorUtils.equals(WBorderLayout.SOUTH, position)){
			return getTitlePart().getHeight();
		}
		return getTitlePart().getWidth();
	}

    public void add(Component comp, String position) {
        super.add(comp, position);
    }


	/**
	 * 将WLayout转换为XLayoutContainer
	 */
	@Override
	public void convert() {
		isRefreshing = true;
		WBorderLayout wb = this.toData();
		this.removeAll();
		String[] arrs = {WBorderLayout.NORTH, WBorderLayout.SOUTH, WBorderLayout.EAST, WBorderLayout.WEST, WBorderLayout.CENTER};
		for (int i = 0; i < arrs.length; i++) {
			Widget wgt = wb.getLayoutWidget(arrs[i]);
			//用来兼容之前titlePart设置不可见
			if (wgt != null && ComparatorUtils.equals(arrs[i], WBorderLayout.NORTH) && !wgt.isVisible()) {
				wgt.setVisible(true);
				this.toData().setNorthSize(0);
			}
			if (wgt != null) {
				XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
				this.add(comp, arrs[i]);
				comp.setBackupParent(this);
			}
		}
		dealCompatibility(wb);

		isRefreshing = false;
	}

	private void dealCompatibility(WBorderLayout wb){
 		WCardMainBorderLayout ob = (WCardMainBorderLayout)wb;
		WCardLayout cardLayout = ob.getCardPart();
		//tab结构改变需要兼容以前的tab，重新命名tabpane
		WCardTitleLayout wCardTitleLayout = ob.getTitlePart();
		if(cardLayout == null || wCardTitleLayout == null){
			return;
		}
		WCardTagLayout wCardTagLayout = wCardTitleLayout.getTagPart();
		String tabpaneName = cardLayout.getWidgetName();
		if (!wCardTagLayout.isNewTab()) {
			wCardTagLayout.setWidgetName(tabpaneName);
			BorderPacker borderStyle = cardLayout.getBorderStyle();
			if(borderStyle != null){
				//新tab默认都有标题
				borderStyle.setType(LayoutBorderStyle.TITLE);
			}
			String newCardLayoutName = XWCardLayout.DEFAULT_NAME + tabpaneName.replaceAll(XWCardTagLayout.DEFAULT_NAME, "");
			cardLayout.setWidgetName(newCardLayoutName);
			//修改cardswitchbutton所绑定的cardlayoutname
			for (int i = 0, len = wCardTagLayout.getWidgetCount(); i < len; i++) {
				CardSwitchButton button = wCardTagLayout.getSwitchButton(i);
				button.setCardLayoutName(newCardLayoutName);
			}
			wCardTitleLayout.setCardName(newCardLayoutName);
			wCardTagLayout.setNewTab(true);
			//这边需要设置成默认值兼容之前的title高度(不知道为啥之前的title的高度会改变)
			if(this.toData().getNorthSize() != 0){
				ob.setNorthSize(WTitleLayout.TITLE_HEIGHT);
			}
			for(int i = 0 ;i < cardLayout.getListenerSize(); i ++){
				Listener listener = cardLayout.getListener(i);
				if(listener != null){
					wCardTagLayout.addListener(listener);
				}
			}
			cardLayout.clearListeners();
		}
	}

	/**
	 * 切换到非添加状态
	 *
	 * @return designer 表单设计器
	 */
	@Override
	public void stopAddingState(FormDesigner designer){
		designer.stopAddingState();
		return;
	}


	/**
	 * 添加card区域
	 *
	 * @param card card区域
	 *
	 *
	 * @date 2014-12-10-下午1:50:37
	 *
	 */
	public void addCardPart(XWCardLayout card){
		this.add(card, WBorderLayout.CENTER);
	}

	public XWCardLayout getCardPart(){
		return this.getComponentCount() == TITLE_STYLE ? (XWCardLayout)this.getComponent(CENTER) : (XWCardLayout)this.getComponent(NORTH);
	}

	public XWCardTitleLayout getTitlePart(){
		Component[] components = this.getComponents();
		for(Component component : components){
			if(component instanceof XWCardTitleLayout){
				return (XWCardTitleLayout)component;
			}
		}
		return (XWCardTitleLayout)this.getComponent(NORTH);
	}

    /**
     * 控件树里需要隐藏xwcardmainLayout，返回其子组件xwcardLayout；
     * 标题样式下，this.getComponent(1)==xwcardLayout
     * 标准样式下，this.getComponent(0)==xwcardLayout
     * @return 子组件xwcardLayout
     */
    @Override
    public XCreator getXCreator() {
		return this;
    }

	/**
	 * 控件树不显示此组件
	 * @param path 控件树list
	 */
	@Override
	public void notShowInComponentTree(List<Component> path) {
		return;
	}

	@Override
	public int getShowXCreatorCount() {
		return CARDMAINLAYOUT_CHILD_COUNT;
	}

    @Override
    public ArrayList<XWTabFitLayout> getTargetChildrenList() {
    	ArrayList<XWTabFitLayout> tabLayoutList = new ArrayList<XWTabFitLayout>();
    	XWCardLayout cardLayout = this.getCardPart();
    	for(int i=0, size=cardLayout.getComponentCount(); i<size; i++){
    		XWTabFitLayout tabLayout = (XWTabFitLayout)cardLayout.getComponent(i);
    		tabLayoutList.add(tabLayout);
    	}
    	return tabLayoutList;
    }

	/**
	 * 重新调整子组件的宽度
	 * @param width 宽度
	 * @param actualSize 是否按照实际大小计算
	 */
	@Override
	public void recalculateChildWidth(int width, boolean actualSize){
		ArrayList<?> childrenList = this.getTargetChildrenList();
		int size = childrenList.size();
		if (size > 0) {
			for (int j = 0; j < size; j++) {
				XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList
						.get(j);


				Dimension d = new Dimension(tabLayout.toData().getContainerWidth(), tabLayout.toData().getContainerHeight());
				Rectangle rec = actualSize? new Rectangle(d): tabLayout.getBounds();
				// 容器大小改变时，设下backupBound为其之前的实际大小
				tabLayout.setBackupBound(rec);
				int refSize = rec.width;
				int offest = width - refSize;
				double percent = (double) offest / refSize;
				if (percent < 0 && !tabLayout.canReduce(percent)) {
					return;
				}
				tabLayout.setSize(rec.width + offest, rec.height);
				if(!actualSize){
					updateChildWidgetBounds(tabLayout);
				}
				tabLayout.adjustCreatorsWidth(percent);
			}
		}
	}

	/**
	 * 重新调整子组件的高度
	 * @param height 高度
	 * @param actualSize 是否按照实际大小计算
	 */
	@Override
	public void recalculateChildHeight(int height, boolean actualSize){
		ArrayList<?> childrenList = this.getTargetChildrenList();
		int size = childrenList.size();
		if (size > 0) {
			for (int j = 0; j < size; j++) {
				XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList
						.get(j);
				Dimension d = new Dimension(tabLayout.toData().getContainerWidth(), tabLayout.toData().getContainerHeight());
				Rectangle rec = actualSize? new Rectangle(d): tabLayout.getBounds();
				// 容器大小改变时，设下backupBound为其之前的实际大小
				tabLayout.setBackupBound(rec);
				int refSize = rec.height;
				int offset = height - refSize - WCardMainBorderLayout.TAB_HEIGHT;
				if(offset < 0){
					// 缩放时需要备份原tab布局宽高
					tabLayout.setReferDim(new Dimension(rec.width, rec.height));
				}
				double percent = (double) offset / refSize;
				if (percent < 0 && !tabLayout.canReduce(percent)) {
					return;
				}
				tabLayout.setSize(rec.width, rec.height + offset);
				if(!actualSize){
					updateChildWidgetBounds(tabLayout);
				}
				tabLayout.adjustCreatorsHeight(percent);
			}
		}

	}


	/**
	 * 更新下子组件bounds
	 * @param tabLayout tabFit布局
	 */
	private void updateChildWidgetBounds(XWTabFitLayout tabLayout){
		for (int m = 0; m < tabLayout.getComponentCount(); m++) {
			XCreator childCreator = tabLayout.getXCreator(m);
			BoundsWidget wgt = (BoundsWidget) tabLayout.toData()
					.getBoundsWidget(childCreator.toData());
			wgt.setBounds(tabLayout.getComponent(m).getBounds());
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//如果鼠标移动到布局内且布局不可编辑，画出编辑蒙层
		if (isMouseEnter && !editable) {
			int x = 0;
			int y = 0;
			int w = getWidth();
			int h = getHeight();

			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();
			//画白色的编辑层
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 50 / 100.0F));
			g2d.setColor(XCreatorConstants.COVER_COLOR);
			g2d.fillRect(x, y, w, h);
			//画编辑按钮所在框
			FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
			AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, formDesigner.getCursor().getType() != Cursor.DEFAULT_CURSOR ? 0.9f : 0.7f);
			g2d.setColor(XCreatorConstants.EDIT_COLOR);
			g2d.setComposite(alphaComposite);
			g2d.fillRoundRect((x + w / 2 - EDIT_BTN_WIDTH / 2), (y + h / 2 - EDIT_BTN_HEIGHT / 2), EDIT_BTN_WIDTH, EDIT_BTN_HEIGHT, 4, 4);
			g2d.setComposite(oldComposite);

			//画编辑按钮图标
			BufferedImage image = IOUtils.readImage(IconPathConstants.EDIT_ICON_PATH);
			g2d.drawImage(
					image,
					(x + w / 2 - 23),
					(y + h / 2 - image.getHeight() / 2),
					image.getWidth(),
					image.getHeight(),
					null,
					this
			);
			g2d.setColor(Color.WHITE);
			//画编辑文字
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawString(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit"), x + w / 2 - 2, y + h / 2 + 5);
			g.setColor(XCreatorConstants.FORM_BORDER_COLOR);
			GraphHelper.draw(g, new Rectangle(BORDER_WIDTH, BORDER_WIDTH, getWidth() - BORDER_WIDTH * 2, getHeight() - BORDER_WIDTH * 2), Constants.LINE_MEDIUM);
			paintExtro(g);
		}
	}

	public void paintExtro(Graphics g) {
		if (this.toData().getWidgetAttrMark(SharableAttrMark.XML_TAG) != null) {
			int width = getWidth() - ShareConstants.SHARE_EL_CONTROL_BUTTON_HW;
			g.setColor(UIConstants.NORMAL_BACKGROUND);
			g.fillArc(width, 0, ShareConstants.SHARE_EL_CONTROL_BUTTON_HW, ShareConstants.SHARE_EL_CONTROL_BUTTON_HW,
					0, 360);
			controlMode.paintIcon(this, g, width, 0);
		}
	}

	@Override
	public void paintBorder(Graphics g, Rectangle bounds){
		if (!isMouseEnter) {
			super.paintBorder(g, bounds);
		}
	}

	/**
	 * 响应点击事件
	 *
	 * @param editingMouseListener 鼠标点击，位置处理器
	 * @param e 鼠标点击事件
	 */
	@Override
	public void respondClick(EditingMouseListener editingMouseListener, MouseEvent e){
		//帮助弹窗
		if (this.isHelpBtnOnFocus()) {
			new WidgetHelpDialog(DesignerContext.getDesignerFrame(), this.toData().getDescription()).showWindow(e);
			return;
		}
		FormDesigner designer = editingMouseListener.getDesigner();
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();
		boolean isEditing = e.getButton() == MouseEvent.BUTTON1 &&
				(designer.getCursor().getType() == Cursor.HAND_CURSOR || e.getClickCount() == 2);
		setEditable(isEditing);

		selectionModel.selectACreatorAtMouseEvent(e);
		designer.repaint();

		if (editingMouseListener.stopEditing()) {
			if (this != designer.getRootComponent()) {
				ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
				editingMouseListener.startEditing(this, isEditing ? adapter.getDesignerEditor() : null, adapter);
			}
		}
	}

	/**
	 * XWCardMainBorderLayout是card布局主体框架，tab的顶层布局
	 * @return
	 */
	@Override
	public XLayoutContainer getTopLayout() {
			return this;
	}

	@Override
	public int getIndexOfChild(Object child) {
		XWCardTitleLayout titlePart = this.getTitlePart();
		return titlePart.getIndexOfChild(child);
	}

	/**
	 * data属性改变触发其他操作
	 *
	 */
	@Override
	public void firePropertyChange(){
		return;
	}


	public void resetTabDisplayPosition(WTabDisplayPosition wTabDisplayPosition){
		XWCardTitleLayout xwCardTitleLayout = getTitlePart();
		int titleSize = getTitleWidth();
		xwCardTitleLayout.resetNewBtnPosition(wTabDisplayPosition);
		Rectangle parentBounds = new Rectangle(xwCardTitleLayout.getBounds());
		switch (wTabDisplayPosition){
			case TOP_POSITION:
				this.addTitlePart(xwCardTitleLayout, WBorderLayout.NORTH);
				parentBounds.height = titleSize;
				break;
			case LEFT_POSITION:
				this.addTitlePart(xwCardTitleLayout, WBorderLayout.WEST);
				parentBounds.width = titleSize;
				break;
			case BOTTOM_POSITION:
				this.addTitlePart(xwCardTitleLayout, WBorderLayout.SOUTH);
				parentBounds.height = titleSize;
				break;
			case RIGHT_POSITION:
				this.addTitlePart(xwCardTitleLayout, WBorderLayout.EAST);
				parentBounds.width = titleSize;
				break;
			default:
				break;
		}
		xwCardTitleLayout.setBounds(parentBounds);
		this.addCardPart((XWCardLayout)this.getComponent(0));
	}

	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRCardMainBorderLayoutAdapter(this);
	}

	/**
	 * 是否支持共享-现只支持报表块、图表、tab块、绝对布局
	 * @return
	 */
	public boolean isSupportShared() {
		return true;
	}

	@Override
	public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
		if (FormDesignerUtils.isAppRelayout(WidgetPropertyPane.getInstance().getEditingFormDesigner())) {
			return new WidgetPropertyUIProvider[] {new MobileBookMarkCombinePropertyUI(this)};
		} else {
			return super.getWidgetPropertyUIProviders();
		}
	}
}
