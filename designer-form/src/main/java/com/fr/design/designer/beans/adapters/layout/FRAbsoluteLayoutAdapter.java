package com.fr.design.designer.beans.adapters.layout;

import com.fr.design.beans.GroupModel;
import com.fr.design.designer.beans.ConstraintsGroupModel;
import com.fr.design.designer.beans.HoverPainter;
import com.fr.design.designer.beans.painters.FRAbsoluteLayoutPainter;
import com.fr.design.designer.creator.*;
import com.fr.design.designer.properties.BoundsGroupModel;
import com.fr.design.designer.properties.FRAbsoluteLayoutPropertiesGroupModel;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;

import java.awt.*;

public class FRAbsoluteLayoutAdapter extends FRBodyLayoutAdapter {
	//是不是添加到父容器上
	private boolean isAdd2ParentLayout = false;
	private HoverPainter painter;

    public FRAbsoluteLayoutAdapter(XLayoutContainer container) {
        super(container);
		painter = new FRAbsoluteLayoutPainter(container);
		initMinSize();
    }

	private void initMinSize() {
		XWAbsoluteLayout layout = (XWAbsoluteLayout) container;
		minWidth = layout.getActualMinWidth();
		minHeight = layout.getActualMinHeight();
		actualVal = layout.getAcualInterval();
		margin = layout.toData().getMargin();
	}

	@Override
	public HoverPainter getPainter() {
		return painter;
	}

    /**
     * 是否能在指定位置添加组件
     * @param creator 组件
     * @param x 坐标x
     * @param y 坐标y
     * @return 能则返回true
     */
	//这个地方的逻辑非常复杂，
	// 1.当前绝对布局是不可编辑且是最外层，那么其他控件添加在它周围，
	// 2.当前绝对布局是不可编辑且不是最外层，那么控件不可添加，（嵌套）
	// 3.当前绝对布局可编辑，那么控件添加
    @Override
	public boolean accept(XCreator creator, int x, int y) {
		Component comp = container.getComponentAt(x, y);
		//布局控件要先判断是不是可编辑
		//可以编辑，按原有逻辑判断
		//不可编辑，当成一整个控件处理
		if (comp == null){
			return false;
		}
		//参数面板内的组件不允许拖往绝对布局中
		if (creator.getParent() != null && ((XCreator)creator.getParent()).acceptType(XWParameterLayout.class)){
			Rectangle rec = creator.getBounds();
			rec.y = creator.getParent().getHeight() - rec.height;
			creator.setBounds(rec);
			return false;
		}
		//判断下组件能不能拖入绝对布局
		if (!creator.canEnterIntoAbsolutePane()){
			return false;
		}
		XLayoutContainer topLayout = XCreatorUtils.getHotspotContainer((XCreator)comp).getTopLayout();
		if(topLayout != null){
			if (topLayout.isEditable()){
                return topLayoutAccept(creator, x, y);
			}
			//绝对布局嵌套，处于内层，不可编辑，不添加，topLayout只能获取到最外层可编辑的布局
			else if (((XLayoutContainer)topLayout.getParent()).acceptType(XWAbsoluteLayout.class)) {
				return false;
            } else {
                return acceptWidget(x, y);
			}
        } else {
			FineLoggerFactory.getLogger().error("top layout is null!");
		}

		return false;
	}

    //topLayout假如可以编辑的话就往里面添加组件
    private boolean topLayoutAccept(XCreator creator, int x, int y) {
        //允许组件重叠，可以不判断有没有和当前控件重叠
        //先计算当前控件的位置
        int creatorX, creatorY;
        if (XCreatorUtils.getParentXLayoutContainer(creator) != null) {
            Rectangle creatorRectangle = ComponentUtils.getRelativeBounds(creator);
            creatorX = creatorRectangle.x;
            creatorY = creatorRectangle.y;
        } else {
            //这边计算得到的组件其实位置是正确的，
            //因为传入的x和y已经加上了宽度或者高度的一半，再减去相同的宽度和高度的一半是没区别的，
            // 例如高度为21，那么就是+10-10;
            // 高度为20，那么就是+10-10; 没区别
            int w = creator.getWidth() / 2;
            int h = creator.getHeight() / 2;
            creatorX = x - w;
            creatorY = y - h;
        }
		if (creatorX < 0 || creatorX + creator.getWidth() > container.getWidth()) {
            return false;
        }
		if (creatorY < 0 || creatorY + creator.getHeight() > container.getHeight()){
            return false;
        }
		return x >= 0 && y >= 0 && creator.getHeight() <= container.getHeight()
                && creator.getWidth() <= container.getWidth();
	}

	/**
	 * 判断是否鼠标在组件的三等分区域，如果组件在布局管理器中间，上下左右都可能会三等分
	 * @param parentComp 鼠标所在区域的组件
	 * @param x 坐标x
	 * @param y 坐标y
	 * @return 是则返回true
	 */
	public boolean isTrisectionArea(Component parentComp, int x, int y) {
		XCreator creator = (XCreator)parentComp;
		trisectAreaDirect = 0;
		if (container.getComponentCount()<=1) {
			return false;
		}
		int maxWidth = parentComp.getWidth();
		int maxHeight = parentComp.getHeight();
		int xL = parentComp.getX();
		int yL = parentComp.getY();
		// 组件宽高的十分之一和默认值取大
		int minRangeWidth = Math.max(maxWidth/BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
		int minRangeHeight = Math.max(maxHeight/BORDER_PROPORTION, DEFAULT_AREA_LENGTH);
		if(y<yL+minRangeHeight ) {
			// 在组件上侧三等分
			trisectAreaDirect = COMP_TOP;
		} else if(y>yL+maxHeight-minRangeHeight) {
			// 在组件下侧三等分
			trisectAreaDirect = COMP_BOTTOM;
		} else if (x<xL+minRangeWidth) {
			// 在组件左侧三等分
			trisectAreaDirect = COMP_LEFT;
		} else if(x>xL+maxWidth-minRangeWidth) {
			// 在组件右侧三等分
			trisectAreaDirect = COMP_RIGHT;
		}
		// tab布局的边界特殊处理，不进行三等分
		if(!creator.getTargetChildrenList().isEmpty()){
			return false;
		}

		return !ComparatorUtils.equals(trisectAreaDirect, 0);
	}

    //当前绝对布局不可编辑，就当成一个控件，组件添加在周围
    private boolean acceptWidget(int x, int y) {
        isFindRelatedComps = false;
        //拖入组件判断时，先判断是否为交叉点区域，其次三等分区域，再次平分区域
        Component comp = container.getComponentAt(x, y);
        //如果当前处于边缘地带, 那么就把他贴到父容器上
        XLayoutContainer parent = container.findNearestFit();
        container = parent != null ? parent : container;
        isAdd2ParentLayout = true;

		int componentHeight = comp.getHeight();
		int componentWidth = comp.getWidth();
		//上半部分高度
		int upHeight = (int) (componentHeight * TOP_HALF) + comp.getY();
		//下半部分高度
		int downHeight = (int) (componentHeight * BOTTOM_HALF) + comp.getY();

		if (isCrossPointArea(comp, x, y)) {
			return canAcceptWhileCrossPoint(comp, x, y);
		}

		if (isTrisectionArea(comp, x, y)) {
			return canAcceptWhileTrisection(comp, x, y);
		}

		boolean horizonValid = componentWidth >= minWidth * 2 + actualVal;
		boolean verticalValid = componentHeight >= minHeight * 2 + actualVal;
		return y > upHeight && y < downHeight ? horizonValid : verticalValid;
	}

	/**
	 * 组件的ComponentAdapter在添加组件时，如果发现布局管理器不为空，会继而调用该布局管理器的
	 * addComp方法来完成组件的具体添加。在该方法内，布局管理器可以提供额外的功能。
	 *
	 * @param creator 被添加的新组件
	 * @param x       添加的位置x，该位置是相对于container的
	 * @param y       添加的位置y，该位置是相对于container的
	 * @return 是否添加成功，成功返回true，否则false
	 */
	@Override
	public boolean addBean(XCreator creator, int x, int y) {
		Rectangle rect = ComponentUtils.getRelativeBounds(container);

		int posX = x + rect.x;
		int posY = y + rect.y;
		if (!accept(creator, x, y)) {
			return false;
		}
		addComp(creator, posX, posY);
		((XWidgetCreator) creator).recalculateChildrenSize();
		return true;
	}

	@Override
	protected void addComp(XCreator creator, int x, int y) {
		if(!isAdd2ParentLayout) {
			Rectangle r = ComponentUtils.getRelativeBounds(container);
			x = x - r.x;
			y = y - r.y;
			if (XCreatorUtils.getParentXLayoutContainer(creator) != null) {

				Rectangle creatorRectangle = ComponentUtils.getRelativeBounds(creator);
				x = creatorRectangle.x - r.x;
				y = creatorRectangle.y - r.y;
			} else {
				int w = creator.getWidth() / 2;
				int h = creator.getHeight() / 2;
				x = x - w;
				y = y - h;
			}
			fix(creator, x, y);

			if (creator.hasTitleStyle()) {
				addParentCreator(creator);
			} else {
                container.add(creator, creator.toData().getWidgetName(),0);
			}
			XWAbsoluteLayout layout = (XWAbsoluteLayout) container;
			layout.updateBoundsWidget(creator);
			updateCreatorBackBound();
			LayoutUtils.layoutRootContainer(container);
		}else{
			//添加到其父组件布局中的时候，要用其父组件布局添加
			if (creator.shouldScaleCreator() || creator.hasTitleStyle()) {
				XLayoutContainer parentPanel = creator.initCreatorWrapper(creator.getHeight());
				container.getLayoutAdapter().addBean(parentPanel, x, y);
			} else {
				container.getLayoutAdapter().addBean(creator, x, y);
			}
		}
	}

	private void updateCreatorBackBound() {
		for (int i=0,size=container.getComponentCount(); i<size; i++) {
			XCreator creator = (XCreator) container.getComponent(i);
			creator.updateChildBound(minHeight);
			creator.setBackupBound(creator.getBounds());
		}
	}

	private void addParentCreator(XCreator child) {
		XLayoutContainer parentPanel = child.initCreatorWrapper(child.getHeight());
        container.add(parentPanel, child.toData().getWidgetName(),0);
	}

	/**
	 *  新拖入组件时，计算调整其他关联组件位置大小
	 * @param child  新拖入的组件
	 * @param x 鼠标所在x坐标
	 * @param y 鼠标所在y坐标
	 */
	private void fixAbsolute(XCreator child, int x, int y) {
		Component parentComp = container.getComponentAt(x, y);
		if (container.getComponentCount()==0){
			child.setLocation(0, 0);
			child.setSize(parentComp.getWidth(), parentComp.getHeight());
		} else if(isCrossPointArea(parentComp, x, y)){
			//交叉区域插入组件时，根据具体位置进行上下或者左右或者相邻三个组件的位置大小插入
			fixCrossPointArea(parentComp, child, x, y);
			return;
		} else if (isTrisectionArea(parentComp, x, y)) {
			// 在边界三等分区域，就不再和组件二等分了
			fixTrisect(parentComp, child, x, y);
			return;
		} else{
			fixHalve(parentComp, child, x, y);
		}
	}

	/**
     * 组件拖拽后调整大小
     * @param creator 组件
     */
    @Override
	public void fix(XCreator creator) {
    	WAbsoluteLayout wabs = (WAbsoluteLayout)container.toData();
    	fix(creator,creator.getX(),creator.getY());
    	wabs.setBounds(creator.toData(),creator.getBounds());

		XWAbsoluteLayout layout = (XWAbsoluteLayout) container;
		layout.updateBoundsWidget(creator);
    }

    /**
     * 调整组件大小到合适尺寸位置
     * @param creator 组件
     * @param x 坐标x
     * @param y 坐标y
     */
    public void fix(XCreator creator ,int x, int y) {
		int height = creator.getHeight();
		int width = creator.getWidth();
    	if (x < 0) {
            x = container.getX();
		} else if (x + creator.getWidth() > container.getWidth()) {
            x = container.getWidth() - width;
		}

		if (y < 0) {
            y = container.getY();
		} else if (y + creator.getHeight() > container.getHeight()) {
            y = container.getHeight() - height;
		}

		creator.setBounds(x, y, width, height);
    }

    @Override
    public ConstraintsGroupModel getLayoutConstraints(XCreator creator) {
        return new BoundsGroupModel((XWAbsoluteLayout)container, creator);
    }

	@Override
	public GroupModel getLayoutProperties() {
		XWAbsoluteLayout xwAbsoluteLayout = (XWAbsoluteLayout) container;
		return new FRAbsoluteLayoutPropertiesGroupModel(xwAbsoluteLayout);
	}
}
