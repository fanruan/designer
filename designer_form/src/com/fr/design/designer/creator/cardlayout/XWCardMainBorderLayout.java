/**
 *
 */
package com.fr.design.designer.creator.cardlayout;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WTabDisplayPosition;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
public class XWCardMainBorderLayout extends XWBorderLayout{

	private static final int CENTER = 1;
	private static final int NORTH = 0;
	private static final int TITLE_STYLE = 2;

	private static final int EDIT_BTN_WIDTH = 60;
	private static final int EDIT_BTN_HEIGHT = 24;

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
			if(component instanceof  XWCardTitleLayout){
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
     */
    @Override
	public void recalculateChildWidth(int width){
		ArrayList<?> childrenList = this.getTargetChildrenList();
		int size = childrenList.size();
		if (size > 0) {
			for (int j = 0; j < size; j++) {
				XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList
						.get(j);
				tabLayout.setBackupBound(tabLayout.getBounds());
				int refSize = tabLayout.getWidth();
				int offest = width - refSize;
				double percent = (double) offest / refSize;
				if (percent < 0 && !tabLayout.canReduce(percent)) {
					return;
				}
				tabLayout.setSize(tabLayout.getWidth() + offest,
						tabLayout.getHeight());
				for (int m = 0; m < tabLayout.getComponentCount(); m++) {
					XCreator childCreator = tabLayout.getXCreator(m);
					BoundsWidget wgt = (BoundsWidget) tabLayout.toData()
							.getBoundsWidget(childCreator.toData());
					wgt.setBounds(tabLayout.getComponent(m).getBounds());
				}
				tabLayout.adjustCreatorsWidth(percent);
			}
		}
    }

    /**
     * 重新调整子组件的高度
     * @param height 高度
     */
    @Override
	public void recalculateChildHeight(int height){
		ArrayList<?> childrenList = this.getTargetChildrenList();
		int size = childrenList.size();
		if (size > 0) {
			for (int j = 0; j < size; j++) {
				XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList
						.get(j);
				tabLayout.setBackupBound(tabLayout.getBounds());
				int refSize = tabLayout.getHeight();
				int offset = height - refSize - WCardMainBorderLayout.TAB_HEIGHT;
		    	if(offset < 0){
		    		// 缩放时需要备份原tab布局宽高
		    		tabLayout.setReferDim(new Dimension(tabLayout.getWidth(),tabLayout.getHeight()));
		    	}
				double percent = (double) offset / refSize;
				if (percent < 0 && !tabLayout.canReduce(percent)) {
					return;
				}
				tabLayout.setSize(tabLayout.getWidth(),
						tabLayout.getHeight() + offset);
				for (int m = 0; m < tabLayout.getComponentCount(); m++) {
					XCreator childCreator = tabLayout.getXCreator(m);
					BoundsWidget wgt = (BoundsWidget) tabLayout.toData()
							.getBoundsWidget(childCreator.toData());
					wgt.setBounds(tabLayout.getComponent(m).getBounds());
				}
				tabLayout.adjustCreatorsHeight(percent);
			}
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
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 60 / 100.0F));
			g2d.setColor(Color.WHITE);
			g2d.fillRect(x, y, w, h);
			//画编辑按钮所在框
			g2d.setComposite(oldComposite);
			g2d.setColor(new Color(176, 196, 222));
			g2d.fillRect((x + w / 2 - EDIT_BTN_WIDTH / 2), (y + h / 2 - EDIT_BTN_HEIGHT / 2), EDIT_BTN_WIDTH, EDIT_BTN_HEIGHT);
			//画编辑按钮图标
			BufferedImage image = IOUtils.readImage(IconPathConstants.TD_EDIT_ICON_PATH);
			g2d.drawImage(
					image,
					(x + w / 2 - 23),
					(y + h / 2 - image.getHeight() / 2),
					image.getWidth(),
					image.getHeight(),
					null,
					this
			);
			g2d.setColor(Color.BLACK);
			//画编辑文字
			g2d.drawString(Inter.getLocText("FR-Designer_Edit"), x + w / 2 - 2, y + h / 2 + 5);
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
		XLayoutContainer xTopLayout = XCreatorUtils.getParentXLayoutContainer(this).getTopLayout();
		if (xTopLayout != null && !xTopLayout.isEditable()){
			return xTopLayout;
		}
		else{
			return this;
		}
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
		getCardPart().initStyle();
	}


	public void resetTabDisplayPosition(WTabDisplayPosition wTabDisplayPosition){
		XWCardTitleLayout xwCardTitleLayout = getTitlePart();
		int titleSize = getTitleWidth();
		xwCardTitleLayout.resetNewBtnPosition(wTabDisplayPosition);
		Rectangle parentBounds = new Rectangle(xwCardTitleLayout.getBounds());
		switch (wTabDisplayPosition){
			case TOP_POSITION:
				this.addTitlePart(xwCardTitleLayout,WBorderLayout.NORTH);
				parentBounds.height = titleSize;
				break;
			case LEFT_POSITION:
				this.addTitlePart(xwCardTitleLayout,WBorderLayout.WEST);
				parentBounds.width = titleSize;
				break;
			case BOTTOM_POSITION:
				this.addTitlePart(xwCardTitleLayout,WBorderLayout.SOUTH);
				parentBounds.height = titleSize;
				break;
			case RIGHT_POSITION:
				this.addTitlePart(xwCardTitleLayout,WBorderLayout.EAST);
				parentBounds.width = titleSize;
				break;
			default:
				break;
		}
		xwCardTitleLayout.setBounds(parentBounds);
		this.addCardPart((XWCardLayout)this.getComponent(0));
	}
}