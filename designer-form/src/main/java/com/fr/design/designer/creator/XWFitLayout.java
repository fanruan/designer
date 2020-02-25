package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRFitLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.designer.properties.mobile.BodyMobilePropertyUI;
import com.fr.design.form.layout.FRFitLayout;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.mainframe.FormArea;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WBodyLayoutType;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.FRLogger;
import com.fr.general.FRScreen;
import com.fr.stable.ArrayUtils;
import com.fr.stable.AssistUtils;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author jim
 * @date 2014-6-23
 */
public class XWFitLayout extends XLayoutContainer {

	private static final long serialVersionUID = 8112908607102660176L;
	private static final int EACH_ROW_COUNT = 4;

	//由于屏幕分辨率不同，界面上的容器大小可能不是默认的100%，此时拖入组件时，保存的大小按照100%时的计算
	protected double containerPercent = 1.0;
	// 布局缩小的时候，考虑最小宽高，若挨着右侧或底侧边框的控件缩小后达到最小宽或高，此时容器大小微调下
	private int needAddWidth = 0;
	private int needAddHeight = 0;
	private int minWidth = WLayout.MIN_WIDTH;
	private int minHeight = WLayout.MIN_HEIGHT;
	private int backupGap = 0;
	protected boolean hasCalGap = false;


	public XWFitLayout(){
		this(new WFitLayout(), new Dimension());
	}

	public XWFitLayout(WFitLayout widget, Dimension initSize) {
		super(widget, initSize);

		initPercent();

		widget.setResolutionScaling(containerPercent);
	}

	//根据屏幕大小来确定显示的百分比, 1440*900默认100%, 1366*768缩放90%
	private void initPercent(){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		double screenValue = FRScreen.getByDimension(scrnsize).getValue();
		if (!AssistUtils.equals(FormArea.DEFAULT_SLIDER, screenValue)) {
			this.setContainerPercent(screenValue / FormArea.DEFAULT_SLIDER);
		}
	}

	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRFitLayoutAdapter(this);
	}

	@Override
	protected void initLayoutManager() {
		this.setLayout(new FRFitLayout());
	}

	@Override
	protected String getIconName() {
		return "layout_absolute.png";
	}

	/**
	 * 返回最右侧控件的微调宽度
	 * @return 微调宽度
	 */
	public int getNeedAddWidth() {
		return needAddWidth;
	}

	public void setNeedAddWidth(int needAddWidth) {
		this.needAddWidth = needAddWidth;
	}

	/**
	 * 返回最右侧控件的微调高度
	 * @return 微调宽度
	 */
	public int getNeedAddHeight() {
		return needAddHeight;
	}

	public void setNeedAddHeight(int needAddHeight) {
		this.needAddHeight = needAddHeight;
	}

	/**
	 * 更新组件的backupBound
	 *  拖动滑块改变容器大小，改变的是界面显示大小，更新bound，再次拖入或拉伸边框用到
	 */
	private void updateCreatorsBackupBound() {
		for (int i=0,size=this.getComponentCount(); i<size; i++) {
			Component comp = this.getComponent(i);
			XCreator creator = (XCreator) comp;
			creator.setBackupBound(comp.getBounds());
		}
	}

	/**
	 * 直接拖动滑条改变整体像素大小时，不用考虑控件的最小高度宽度，内部组件全部一起缩小放大
	 * 只是界面显示大小改变，不改变对应的BoundsWidget大小
	 * @param percent 缩放的百分值
	 */
	public void adjustCreatorsWhileSlide(double percent) {
		int count = this.getComponentCount();
		if (count == 0) {
			Dimension size = new Dimension(this.getSize());
			size.width += size.width*percent;
			size.height += size.height*percent;
			this.setSize(size);
			return;
		}
		// 初始化时还未加间隔
		if (hasCalGap) {
			moveContainerMargin();
			moveCompInterval(backupGap);
			LayoutUtils.layoutContainer(this);
		}
		int containerW = 0;
		int containerH = 0;
		int[] hors = getHors(false);
		int[] veris = getVeris(false);
		PaddingMargin margin = toData().getMargin();
		for (int i=0; i<count; i++) {
			XCreator creator = getXCreator(i);
			// 百分比和updateBoundsWidget时都会调整大小
			// 子组件非空时，调整界面大小、 撤销、再次打开表单，都会按屏幕百分比调整，此时必须考虑内边距
			Rectangle rec = modifyCreatorPoint(creator.getBounds(), percent, hors, veris);
			if (rec.x == margin.getLeft()) {
				containerH += rec.height;
			}
			if (rec.y ==  margin.getTop()) {
				containerW += rec.width;
			}
			creator.setBounds(rec);
			creator.updateChildBound(getActualMinHeight());
		}
		// 布局内部组件放大缩小后，都是乘以百分比后取整，可能会产生空隙，此处调整容器
		this.setSize(
				containerW + (int)(margin.getLeft() * (1.0+percent) + margin.getRight() * (1.0+percent)),
				containerH + (int)(margin.getTop() * (1.0+percent) + margin.getBottom() * (1.0+percent))
		);
		updateCreatorsBackupBound();
		// 间隔也按显示大小比例调整
		if (!hasCalGap) {
			moveContainerMargin();
			addCompInterval(getAcualInterval());
		}
		LayoutUtils.layoutContainer(this);
	}

	/**
	 * 调整控件的point和size,避免拖动滑块出现空隙
	 */
	private Rectangle modifyCreatorPoint(Rectangle rec, double percent, int[] hors, int[] veris) {
		int xIndex = 0, yIndex = 0;
		PaddingMargin margin = toData().getMargin();
		Rectangle bound = new Rectangle(rec);
		if (rec.x > margin.getLeft()) {
			for (int i=1, len=hors.length; i<len; i++) {
				rec.x += (hors[i] - hors[i-1]) *percent;
				if (bound.x == hors[i]) {
					xIndex = i;
					break ;
				}
			}
		}
		for (int i=xIndex,len=hors.length; i<len-1; i++) {
			rec.width +=  (hors[i+1]-hors[i])*percent;
			if (hors[i+1]-hors[xIndex] == bound.width) {
				break;
			}
		}
		if (rec.y >  margin.getTop()	) {
			for (int j=1, num=veris.length; j<num; j++) {
				rec.y += (veris[j] - veris[j-1]) *percent;
				if (bound.y == veris[j]) {
					yIndex = j;
					break ;
				}
			}
		}
		for (int j=yIndex,num=veris.length; j<num-1; j++) {
			rec.height +=  (veris[j+1]-veris[j])*percent;
			if (veris[j+1]-veris[yIndex] == bound.height) {
				break;
			}
		}
		return rec;
	}
	/**
	 * 获取内部组件横坐标的值
	 * @return int[] 横坐标数组
	 */

	public int[] getHors(){
		return getHors(false);
	}

	/**
	 * 获取内部组件纵坐标值
	 * @return int[] 纵坐标数组
	 *
	 */
	public int[] getVeris(){
		return getVeris(false);
	}

	/**
	 * 获取内部组件横坐标的值
	 * @param isActualSize 实际大小
	 * @return int[] 横坐标数组
	 */
	public int[] getHors(boolean isActualSize) {
		double perc = isActualSize ? containerPercent : 1.0;
		List<Integer> posX = new ArrayList<Integer>();
		// 保存实际大小时,组件大小已经去除内边距，此处也判断下
		PaddingMargin margin = isActualSize ? new PaddingMargin(0,0,0,0) : toData().getMargin();
		posX.add(margin.getLeft());
		int width = this.getWidth() - margin.getLeft() - margin.getRight();
		int containW = (int) (width / perc);
		posX.add(containW + margin.getLeft());
		for (int i=0, len=this.getComponentCount(); i < len; i++) {
			int x = this.getComponent(i).getX();
			int finalX = (int) (x / perc);
			if (!posX.contains(finalX)) {
				posX.add(finalX);
			}
		}
		Collections.sort(posX);
		return ArrayUtils.toPrimitive(posX.toArray(new Integer[]{posX.size()}));
	}

	/**
	 * 获取内部组件纵坐标值
	 * @param isActualSize 实际大小
	 * @return int[] 纵坐标数组
	 *
	 */
	public int[] getVeris(boolean isActualSize) {
		double perc = isActualSize ? containerPercent : 1.0;
		List<Integer> posY = new ArrayList<Integer>();
		// 保存实际大小时,组件大小已经去除内边距，此处也判断下
		PaddingMargin margin = isActualSize ? new PaddingMargin(0,0,0,0) : toData().getMargin();
		posY.add(margin.getTop());
		int height = this.getHeight() - margin.getTop() - margin.getBottom();
		int containH = (int) (height / perc);
		posY.add(containH + margin.getTop());
		for (int i=0, len=this.getComponentCount(); i < len; i++) {
			int y = this.getComponent(i).getY();
			int finalY = (int) (y / perc);
			if (!posY.contains(finalY)) {
				posY.add(finalY);
			}
		}
		Collections.sort(posY);
		return ArrayUtils.toPrimitive(posY.toArray(new Integer[]{posY.size()}));
	}

	/**
	 * 是否能缩小
	 * @param percent 百分比
	 * @return 是则返回true
	 */
	public boolean canReduce(double percent) {
		boolean canReduceSize = true;
		if (percent < 0 && hasCalGap) {
			// 缩小时考虑间隔的存在是否能缩小
			canReduceSize = canReduceSize(percent);
		}
		return canReduceSize;
	}

	/**
	 * 按照百分比缩放内部组件宽度
	 *
	 * @param percent 宽度变化的百分比
	 */
	public void adjustCreatorsWidth(double percent) {
		if (this.getComponentCount()==0) {
			// 初始化没有拖入控件时，实际宽度依然调整
			this.toData().setContainerWidth(this.getWidth());
			return;
		}
		updateWidgetBackupBounds();
		int gap = toData().getCompInterval();
		if (gap >0 && hasCalGap) {
			moveCompInterval(getAcualInterval());
		}
		layoutWidthResize(percent);
		if (percent < 0 && needAddWidth > 0) {
			this.setSize(this.getWidth()+needAddWidth, this.getHeight());
			modifyEdgemostCreator(true);
		}
		addCompInterval(getAcualInterval());
		this.toData().setContainerWidth(this.getWidth());
		updateWidgetBackupBounds();
		LayoutUtils.layoutContainer(this);
	}

	// 手动修改宽高时，全部用的实际大小计算，所以最小值不用百分比计算后的
	protected void layoutWidthResize(double percent) {
		int[] hroValues= toData().getHorComps();
		int num=hroValues.length;
		int x=0;
		int dw = 0;
		int nextX = 0;
		for (int i=0; i<num-1; i++) {
			x = hroValues[i];
			nextX = hroValues[i+1];
			dw = (int) ((nextX-x)*percent);
			//自适应布局里，控件的最小宽度为36
			if (nextX-x < MIN_WIDTH-dw) {
				dw = MIN_WIDTH +x - nextX;
			}
			caculateWidth(x, dw);
		}
	}

	/**
	 * x位置的组件宽度按dw进行调整
	 */
	private void caculateWidth(int x, int dw) {
		List<Component> comps = getCompsAtX(x);
		if (comps.isEmpty()) {
			return;
		}
		for (int i=0, size=comps.size(); i<size; i++) {
			XCreator creator = (XCreator) comps.get(i);
			BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBounds();
			Rectangle backRec = widget.getBackupBounds();
			if (backRec.x<x) {
				if (notHasRightCreator(backRec)) {
					continue;
				}
				creator.setSize(rec.width+dw, rec.height);
				toData().setBounds(creator.toData(), creator.getBounds());
				continue;
			}
			calculateCreatorWidth(creator, rec, dw, x);
		}
	}

	private void calculateCreatorWidth(XCreator creator, Rectangle rec, int dw, int x) {
		if (x == 0) {
			int width = notHasRightCreator(rec) ? this.getWidth() : rec.width+dw;
			creator.setBounds(0, rec.y, width, rec.height);
			creator.recalculateChildWidth(width, true);
		} else {
			XCreator leftCreator = getCreatorAt(rec.x-1, rec.y);
			int posX = getPosX(leftCreator);
			int width = notHasRightCreator(rec) ? this.getWidth()-posX : rec.width+dw;
			int tempWidth = width;
			if(width < MIN_WIDTH){
				tempWidth = MIN_WIDTH;
			}
			creator.setBounds(posX, rec.y, tempWidth, rec.height);
			// 缩小：小于minwidth只有最右侧时，前面全部已缩小到最小值
			if (width<MIN_WIDTH) {
				needAddWidth = Math.max(needAddWidth, MIN_WIDTH-width);
			}
		}
		creator.adjustCompWidth((double) creator.getBounds().width / rec.width);
		toData().setBounds(creator.toData(), creator.getBounds());
	}

	/**
	 * 是否在布局最右侧
	 */
	private boolean notHasRightCreator(Rectangle rec) {
		if ( rec.x+rec.width==this.getBackupBound().width) {
			return true;
		}
		return false;
	}

	/**
	 * 是否在布局最下侧
	 */
	private boolean notHasBottomCreator(Rectangle rec) {
		if ( rec.y+rec.height == this.getBackupBound().height) {
			return true;
		}
		return false;
	}

	/**
	 * 布局最右侧或下侧有控件在缩小时达到最小宽高，则微调下
	 */
	protected void modifyEdgemostCreator(boolean isHor) {
		for (int i=0, size=this.getComponentCount(); i<size; i++) {
			XCreator creator = (XCreator) this.getComponent(i);
			BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBackupBounds();
			if (isHor && notHasRightCreator(rec)) {
				creator.setSize(creator.getWidth()+needAddWidth, creator.getHeight());
			} else if (!isHor && notHasBottomCreator(rec)) {
				creator.setSize(creator.getWidth(), creator.getHeight()+needAddHeight);
			}
		}
	}

	/**
	 * 布局容器高度手动修改时，
	 * 同时调整容器内的组件们,缩小时需要考虑有的组件高度不满足缩小高度
	 * @param percent 高度变化的百分比
	 */
	public void adjustCreatorsHeight(double percent) {
		if (this.getComponentCount()==0) {
			//调整高度后，wlayout那边记录下
			this.toData().setContainerHeight(this.getHeight());
			return;
		}
		updateWidgetBackupBounds();
		int gap = toData().getCompInterval();
		if (gap >0 && hasCalGap) {
			moveCompInterval(getAcualInterval());
		}
		layoutHeightResize(percent);
		if (percent < 0 && needAddHeight > 0) {
			this.setSize(this.getWidth(), this.getHeight()+needAddHeight);
			modifyEdgemostCreator(false);
		}
		addCompInterval(getAcualInterval());
		this.toData().setContainerHeight(this.getHeight());
		updateWidgetBackupBounds();
		LayoutUtils.layoutContainer(this);
	}

	protected void layoutHeightResize(double percent) {
		int[] vertiValues= toData().getVertiComps();
		int num=vertiValues.length;
		int y=0;
		int dh = 0;
		int nextY = 0;
		for (int i=0; i<num-1; i++) {
			y = vertiValues[i];
			nextY = vertiValues[i+1];
			dh = (int) ((nextY-y)*percent);
			if (nextY-y < MIN_HEIGHT-dh) {
				dh = MIN_HEIGHT + y - nextY;
			}

			calculateHeight(y, dh);
		}
	}

	/**
	 * y位置的组件按dh进行调整
	 */
	private void calculateHeight(int y, int dh) {
		List<Component> comps = getCompsAtY(y);
		if (comps.isEmpty()) {
			return;
		}
		for (int i=0, size=comps.size(); i<size; i++) {
			XCreator creator = (XCreator) comps.get(i);
			BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBounds();
			Rectangle backRec = widget.getBackupBounds();
			if (backRec.y < y) {
				if (notHasBottomCreator(backRec)) {
					continue;
				}
				creator.setSize(rec.width, rec.height+dh);
				toData().setBounds(creator.toData(), creator.getBounds());
				continue;
			}
			calculateCreatorHeight(creator, rec, dh, y);
		}
	}

	private void calculateCreatorHeight(XCreator creator, Rectangle rec, int dh, int y) {
		if (y==0) {
			int height = notHasBottomCreator(rec) ? this.getHeight() : rec.height+dh;
			creator.setBounds(rec.x, 0, rec.width, height);
			creator.recalculateChildHeight(height, true);
		} else {
			XCreator topCreator = getCreatorAt(rec.x, rec.y-1);
			int posY = getPosY(topCreator);
			int h = notHasBottomCreator(rec) ? this.getHeight()-posY : rec.height+dh;
			creator.setBounds(rec.x, posY, rec.width, h);
			if (h<MIN_HEIGHT) {
				// 缩小：小于minheight只有最下侧时，前面全部已缩小到最小值
				needAddHeight = Math.max(needAddHeight, MIN_HEIGHT-h);
			}
		}
		creator.adjustCompHeight((double) creator.getBounds().height / rec.height);
		toData().setBounds(creator.toData(), creator.getBounds());
	}

	private List<Component> getCompsAtX(int x) {
		List<Component> comps = new ArrayList<Component>();
		int size = toData().getWidgetCount();
		for (int i=0; i<size; i++) {
			Component comp = this.getComponent(i);
			XCreator creator = (XCreator) comp;
			BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBackupBounds();
			//rec.x小于x，右侧大于x
			boolean isLowX = rec.x<x && x<rec.x+rec.width;
			if (isLowX || rec.x==x) {
				comps.add(comp);
			}
		}
		return comps;
	}

	private List<Component> getCompsAtY(int y) {
		List<Component> comps = new ArrayList<Component>();
		for (int i=0,size=this.getComponentCount(); i<size; i++) {
			Component comp = this.getComponent(i);
			XCreator creator = (XCreator) comp;
			BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBackupBounds();
			boolean isLowY = rec.y<y && y<rec.y+rec.height;
			if (isLowY || rec.y==y) {
				comps.add(comp);
			}
		}
		return comps;
	}

	private int getPosX(XCreator creator) {
		if (creator == null) {
			return 0;
		} else {
			return creator.getX()+creator.getWidth();
		}
	}

	/**
	 * 返回上侧组件最新的位置
	 */
	private int getPosY(XCreator creator) {
		if (creator == null) {
			return 0;
		} else {
			return creator.getY()+creator.getHeight();
		}
	}

	/**
	 * 返回 x、y 所在区域的组件
	 *
	 * @param x 坐标x
	 * @param y 坐标y
	 * @return 指定坐标的组件
	 */
	public XCreator getCreatorAt(int x, int y) {
		for (int i=0,size=this.getComponentCount(); i<size; i++) {
			XCreator creator = (XCreator) this.getComponent(i);
			BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBackupBounds();
			boolean isCurrent = rec.x<=x && x<rec.x+rec.width && rec.y<=y && y<rec.y+rec.height;
			if (isCurrent) {
				return creator;
			}
		}
		return null;
	}

	/**
	 * 更新boundsWidget的backupBound
	 */
	protected void updateWidgetBackupBounds() {
		for (int i=0, size=this.getComponentCount(); i<size; i++) {
			Component comp = this.getComponent(i);
			XCreator creator = (XCreator) comp;
			BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(creator.toData());
			widget.setBackupBounds(widget.getBounds());
		}
	}


	/**
	 * 返回内部组件的最小高度
	 * @param comps 组件集合
	 * @return 最小高度
	 */
	public int getMinHeight(List<Component> comps) {
		//容器高度拉伸时，计算内部组件的最小高度
		if (comps.isEmpty()) {
			return 0;
		}
		int minH =this.getWidth();
		for (int i=0, size=comps.size(); i<size; i++) {
			minH = minH>comps.get(i).getHeight() ? comps.get(i).getHeight() : minH;
		}
		return minH;
	}

	/**
	 * 初始化组件大小
	 * @return 默认大小
	 */
	@Override
	public Dimension initEditorSize() {
		return new Dimension(0, 0);
	}

	/**
	 * f返回默认组件name
	 * @return 容器名
	 */
	public String createDefaultName() {
		return "fit";
	}

	/**
	 * 返回容器对应的wlayout
	 * @return 同上
	 */
	@Override
	public WFitLayout toData() {
		return (WFitLayout) data;
	}

	/**
	 * 当前组件zorder位置替换新的控件
	 * @param widget 控件
	 * @param  oldcreator 旧组件
	 * @return 组件
	 */
	@Override
	public XCreator replace(Widget widget, XCreator oldcreator) {
		int i = this.getComponentZOrder(oldcreator);
		if (i != -1) {
			this.toData().replace(new BoundsWidget(widget, oldcreator.getBounds()),
					new BoundsWidget(oldcreator.toData(), oldcreator.getBounds()));
			this.convert();
			return (XCreator) this.getComponent(i);
		}
		return null;
	}

	@Override
	public Dimension getMinimumSize() {
		return toData().getMinDesignSize();
	}

	/**
	 * 将WLayout转换为XLayoutContainer
	 */
	public void convert() {
		isRefreshing = true;
		WFitLayout layout = this.toData();
		this.removeAll();
		for (int i=0, num=layout.getWidgetCount(); i<num ; i++) {
			BoundsWidget bw = (BoundsWidget)layout.getWidget(i);
			if (bw != null) {
				Rectangle bounds = bw.getBounds();
				bw.setBackupBounds(bounds);
				XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(bw.getWidget());
				comp.setBounds(bounds);
				this.add(comp, bw.getWidget().getWidgetName(), true);
				comp.setBackupParent(this);
			}
		}
		isRefreshing = false;
	}

	/**
	 * 组件增加
	 * @param e 容器事件
	 */
	public void componentAdded(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		//当前的body布局为绝对布局的时候不要doLayout
		if (toData().getBodyLayoutType() != WBodyLayoutType.ABSOLUTE){
			LayoutUtils.layoutContainer(this);
		}
		WFitLayout layout = this.toData();
		//自适应布局新增控件后，其他控件位置也会变
		XWidgetCreator creator = (XWidgetCreator) e.getChild();
		Rectangle rec = creator.getBounds();
		layout.addWidget(new BoundsWidget(creator.toData(), rec));
		creator.setBackupParent(this);
	}

	/**
	 * 界面容器大小不是默认的时，处理控件的BoundsWidget，且避免出现空隙
	 */
	private Rectangle dealWidgetBound(Rectangle rec) {
		if (AssistUtils.equals(1.0, containerPercent)) {
			return rec;
		}
		rec.x = (int) (rec.x/containerPercent);
		rec.y = (int) (rec.y/containerPercent);
		rec.width = (int) (rec.width/containerPercent);
		rec.height = (int) (rec.height/containerPercent);
		return rec;
	}

	/**
	 * 界面容器大小不是默认的时，恢复组件实际大小
	 */
	private Rectangle dealWgtBound(Rectangle rec) {
		if (AssistUtils.equals(1.0, containerPercent)) {
			return rec;
		}
		rec.x = (int) (rec.x * containerPercent);
		rec.y = (int) (rec.y * containerPercent);
		rec.width = (int) (rec.width * containerPercent);
		rec.height = (int) (rec.height * containerPercent);
		return rec;
	}

	/**
	 * 新增删除拉伸后更新每个组件的BoundsWidget
	 */
	public void updateBoundsWidget() {
		WFitLayout layout = this.toData();
		if (this.getComponentCount() == 0) {
			// 删除最后一个组件时就不再需要调整了
			return;
		}
		// 什么间隔啊 边距啊都去掉
		moveContainerMargin();
		moveCompInterval(getAcualInterval());
		int[] hors = getHors(true);
		int[] veris = getVeris(true);
		int containerWidth = 0;
		int containerHeight = 0;
		for (int index=0, n=this.getComponentCount(); index<n; index++) {
			XCreator creator = (XCreator) this.getComponent(index);
			BoundsWidget wgt = (BoundsWidget) layout.getBoundsWidget(creator.toData());
			// 用当前的显示大小计算后调正具体位置
			Rectangle wgtBound = dealWidgetBound(creator.getBounds());
			Rectangle rec = recalculateWidgetBounds(wgtBound, hors, veris);
			wgt.setBounds(rec);
			creator.toData().updateChildBounds(rec);
			if (rec.x == 0) {
				containerHeight += rec.height;
			}
			if (rec.y == 0) {
				containerWidth += rec.width;
			}
			// 如果子组件时tab布局，则tab布局内部的组件的wiget也要更新，否则保存后重新打开大小不对
			ArrayList<?> childrenList = creator.getTargetChildrenList();
			if(!childrenList.isEmpty()){
				for(int i=0; i<childrenList.size(); i++){
					XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList.get(i);
					tabLayout.updateBoundsWidget();
				}
			}
			//如果子组件是绝对布局，则内部的widget也要更新
			if (creator.acceptType(XWAbsoluteLayout.class)){
				//更新的时候一定要带上backupBound
				if (creator.getBackupBound() == null && wgt.getBeforeScaleBounds() != null) {
					creator.setBackupBound(dealWgtBound(wgt.getBeforeScaleBounds()));
				}
				((XWAbsoluteLayout) creator).updateBoundsWidget();
				creator.setBackupBound(creator.getBounds());
			}
		}
		layout.setContainerHeight(containerHeight);
		layout.setContainerWidth(containerWidth);
		addCompInterval(getAcualInterval());
	}

	private Rectangle recalculateWidgetBounds(Rectangle rec, int[] hors, int[] veris) {
		int xIndex = 0, yIndex = 0;
		Rectangle bound = new Rectangle();
		if (rec.x > 0) {
			for (int i=1, len=hors.length; i<len; i++) {
				bound.x += (hors[i] - hors[i-1]);
				if (rec.x == hors[i]) {
					xIndex = i;
					break ;
				}
			}
		}
		for (int i=xIndex,len=hors.length; i<len-1; i++) {
			bound.width +=  (hors[i+1]-hors[i]);
			// 下一个x值减去当前x值可能会比实际宽度大1
			if (hors[i+1]-hors[xIndex] >= rec.width) {
				break;
			}
		}
		if (rec.y > 0) {
			for (int j=1, num=veris.length; j<num; j++) {
				bound.y += (veris[j] - veris[j-1]);
				if (rec.y == veris[j]) {
					yIndex = j;
					break ;
				}
			}
		}
		for (int j=yIndex,num=veris.length; j<num-1; j++) {
			bound.height +=  (veris[j+1]-veris[j]) ;
			if (veris[j+1]-veris[yIndex] >= rec.height) {
				break;
			}
		}
		return bound;
	}

	/**
	 * 组件删除
	 * @param e 容器事件
	 */
	public void componentRemoved(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		WFitLayout wlayout = this.toData();
		XWidgetCreator xwc = ((XWidgetCreator) e.getChild());
		Widget wgt = xwc.toData();
		BoundsWidget bw = (BoundsWidget) wlayout.getBoundsWidget(wgt);
		wlayout.removeWidget(bw);
		updateBoundsWidget();
		((FRFitLayoutAdapter)getLayoutAdapter()).updateCreatorBackBound();
	}

	/**
	 *  在添加的时候需要把可拉伸的方向确定，所以重写了add方法
	 *  @param comp 组件
	 *  @param constraints 属性
	 */
	public void add(Component comp, Object constraints) {
		if (comp == null) {
			return;
		}
		super.add(comp, constraints);
		XCreator creator = (XCreator) comp;
		dealDirections(creator, false);
	}

	private void add(Component comp, Object constraints, boolean isInit) {
		super.add(comp, constraints);
		XCreator creator = (XCreator) comp;
		dealDirections(creator, isInit);
	}

	/**
	 * 处理自适应布局的directions
	 * @param xcreator 组件
	 */
	public void dealDirections(XCreator xcreator, boolean isInit) {
		if (xcreator == null) {
			return;
		}
		// 重新打开模版时，容器还没初始化，大小都还为0
		int containerWidth = isInit ? toData().getContainerWidth() : this.getWidth();
		int containerHeight = isInit ? toData().getContainerHeight() : this.getHeight();
		PaddingMargin margin = isInit ? new PaddingMargin(0,0,0,0) : toData().getMargin();
		// 再次打开时和初始设计时的区别是没计算过内边距和没按屏幕分辨率调整大小
		for (int i=0; i<this.getXCreatorCount(); i++) {
			XCreator creator = this.getXCreator(i);
			int x = creator.getX();
			int y = creator.getY();
			int w = creator.getWidth();
			int h = creator.getHeight();
			List<Integer> directions = new ArrayList<Integer>();
			// 只要组件边框没有和container贴着的，都可以拉伸
			if (x > margin.getLeft()) {
				directions.add(Direction.LEFT);
			}
			if (x+w < containerWidth - margin.getRight()) {
				directions.add(Direction.RIGHT);
			}
			if (y > margin.getTop()) {
				directions.add(Direction.TOP);
			}
			if (y+h < containerHeight - margin.getBottom()) {
				directions.add(Direction.BOTTOM);
			}
			if (directions.isEmpty()) {
				creator.setDirections(null);
			}else  {
				creator.setDirections(ArrayUtils.toPrimitive(directions.toArray(new Integer[directions.size()])));
			}
		}

	}


	/**
	 * 间隔大于0时，界面处加上间隔
	 * 界面的间隔是针对显示，实际保存的大小不受间隔影响
	 * ps:改变布局大小或者拖入、删除、拉伸都要重新考虑间隔
	 * @param gap 间隔
	 */
	public void addCompInterval(int gap) {
		if (gap == 0) {
			return;
		}
		int val = gap/2;
		for (int i=0, len=this.getComponentCount(); i<len; i++) {
			Component comp = this.getComponent(i);
			Rectangle rec = comp.getBounds();
			Rectangle bound = new Rectangle(rec);
			if (rec.x > 0) {
				bound.x += val;
				bound.width -= val;
			}
			if (rec.width+rec.x < this.getWidth()) {
				bound.width  -= val;
			}
			if (rec.y > 0) {
				bound.y += val;
				bound.height -= val;
			}
			if (rec.height+rec.y < this.getHeight()) {
				bound.height -= val;
			}
			comp.setBounds(bound);
		}
		this.hasCalGap = true;
	}

	/**
	 * 去除原有的间隔
	 * @param gap 间隔
	 */
	public void moveCompInterval(int gap) {
		if (gap == 0) {
			return;
		}
		int val = gap/2;
		for (int i=0, len=this.getComponentCount(); i<len; i++) {
			Component comp = this.getComponent(i);
			Rectangle rec = comp.getBounds();
			Rectangle bound = new Rectangle(rec);
			if (rec.x > 0) {
				bound.x -= val;
				bound.width += val;
			}
			if (rec.width+rec.x < this.getWidth()) {
				bound.width  += val;
			}
			if (rec.y > 0) {
				bound.y -= val;
				bound.height += val;
			}
			if (rec.height+rec.y < this.getHeight()) {
				bound.height += val;
			}
			comp.setBounds(bound);
		}
		this.hasCalGap = false;
	}

	/**
	 * 是否可以加入当前间隔
	 * @param interval 间隔
	 * @return 默认返回true
	 */
	public boolean canAddInterval(int interval) {
		int val = interval/2;
		for (int i=0, len=this.getComponentCount(); i<len; i++) {
			XCreator comp = (XCreator) this.getComponent(i);
			Rectangle rec = comp.getBounds();
			Dimension d = new Dimension(this.getWidth(), this.getHeight());
			Rectangle bound = dealBound(rec, d, val, 0);
			if (bound.width < minWidth || bound.height< minHeight) {
				String widgetName = comp.toData().getWidgetName();
				JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Engine_Invalid_Setting_Cause_Reach_Min_Widget_Size", widgetName));
				return false;
			}
		}
		return true;
	}

	private Rectangle dealBound(Rectangle rec, Dimension d, int val, double perc) {
		Rectangle bound = reSetBound(rec, perc);
		if (rec.x > 0) {
			bound.width -= val;
		}
		if (rec.width+rec.x < d.width) {
			bound.width -= val;
		}
		if (rec.y > 0) {
			bound.height -= val;
		}
		if (rec.height+rec.y < d.height) {
			bound.height -= val;
		}
		return new Rectangle(bound);
	}

	/**
	 * 缩小宽度或者高度时  存在间隔的话是否支持缩小
	 */
	private boolean canReduceSize(double percent) {
		int val = toData().getCompInterval()/2;
		for (int i=0, len=this.getComponentCount(); i<len; i++) {
			XCreator creator = (XCreator) this.getComponent(i);
			BoundsWidget widget = (BoundsWidget) toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBounds();
			Dimension d = new Dimension(this.getBackupBound().width, this.getBackupBound().height);
			Rectangle bound = dealBound(rec, d, val, percent);
			// 缩小宽高都是实际大小判断
			if (bound.width < MIN_WIDTH || bound.height< MIN_HEIGHT) {
				return false;
			}
		}
		return true;
	}

	private Rectangle reSetBound(Rectangle rec, double percent) {
		Rectangle b = new Rectangle(rec);
		b.x += b.x*percent;
		b.y += b.y*percent;
		b.width += b.width*percent;
		b.height += b.height*percent;
		return new Rectangle(b);
	}

	/**
	 * 去除内边距
	 * 重设间隔时，保存实际大小时都要先去掉内边距
	 */
	public void moveContainerMargin() {
		PaddingMargin margin = toData().getMargin();
		int num = this.getComponentCount();
		int maxW = this.getWidth() - margin.getRight();
		int maxH = this.getHeight() - margin.getBottom();
		for (int i=0; i<num; i++) {
			Component comp = this.getComponent(i);
			Rectangle rec = comp.getBounds();
			if (rec.x == margin.getLeft()) {
				rec.x = 0;
				rec.width += margin.getLeft();
			}
			if (rec.y == margin.getTop()) {
				rec.y = 0;
				rec.height += margin.getTop();
			}
			if (rec.x +rec.width == maxW) {
				rec.width += margin.getRight();
			}
			if (rec.y + rec.height == maxH) {
				rec.height += margin.getBottom();
			}
			comp.setBounds(rec);
		}
	}

	public Component getTopComp(int x, int y) {
		int val = getAcualInterval();
		return this.getComponentAt(x, y-default_Length-val);
	}

	public Component getLeftComp(int x, int y) {
		int val = getAcualInterval();
		return this.getComponentAt(x-default_Length-val, y);
	}

	public Component getRightComp(int x, int y, int w) {
		int val = getAcualInterval();
		return this.getComponentAt(x+w+default_Length+val, y);
	}

	public Component getBottomComp(int x, int y, int h) {
		int val = getAcualInterval();
		return this.getComponentAt(x, y+h+default_Length+val);
	}

	public Component getRightTopComp(int x, int y, int w) {
		int val = getAcualInterval();
		return this.getComponentAt(x+w-default_Length, y-default_Length-val);
	}

	public Component getBottomLeftComp(int x, int y, int h) {
		int val = getAcualInterval();
		return this.getComponentAt(x-default_Length-val, y+h-default_Length);
	}

	public Component getBottomRightComp(int x, int y, int h, int w) {
		int val = getAcualInterval();
		return this.getComponentAt(x+w+default_Length+val, y+h-default_Length);
	}

	public Component getRightBottomComp(int x, int y, int h, int w) {
		int val = getAcualInterval();
		return this.getComponentAt(x+w-default_Length, y+h+default_Length+val);
	}

	/**
	 * 返回容器大小的百分比
	 * @return the containerPercent
	 */
	public double getContainerPercent() {
		return containerPercent;
	}

	/**
	 * 设置容器大小的百分比
	 * @param containerPercent the containerPercent to set
	 */
	public void setContainerPercent(double containerPercent) {
		this.containerPercent = containerPercent;
		minWidth = (int) (XWFitLayout.MIN_WIDTH*containerPercent);
		minHeight = (int) (XWFitLayout.MIN_HEIGHT*containerPercent);
	}

	/**
	 * 该组件是否可以拖拽(表单中参数面板和自适应布局不可以拖拽)
	 * @return 是则返回true
	 */
	public boolean isSupportDrag(){
		return false;
	}

	/**
	 * 返回界面处根据百分比调整后的最小宽度
	 * @return 最小宽度
	 */
	public int getActualMinWidth() {
		return this.minWidth;
	}

	/**
	 * 返回界面处根据百分比调整后的最小高度
	 * @return 最小高度
	 */
	public int getActualMinHeight() {
		return this.minHeight;
	}

	/**
	 * 返回界面处根据百分比调整后的间隔大小（且为偶数）
	 * @return 间隔
	 */
	public int getAcualInterval() {
		// adapter那边交叉三等分、删除都要判断是否对齐，所以间隔转为偶数
		int interval = (int) (toData().getCompInterval()*containerPercent);
		int val = interval/2;
		return val*2;
	}

	/**
	 * 返回是否已经加上间隔
	 * @return the hasCalGap 是否
	 */
	public boolean isHasCalGap() {
		return hasCalGap;
	}

	/**
	 * 设置当前的间隔
	 * @param hasCalGap the hasCalGap to set 间隔
	 */
	public void setHasCalGap(boolean hasCalGap) {
		this.hasCalGap = hasCalGap;
	}

	/**
	 * 设置上次的间隔
	 * @param backupPercent 上次的百分比
	 */
	public void setBackupGap(double backupPercent) {
		int value = (int) (toData().getCompInterval()*backupPercent);
		int val = value/2;
		this.backupGap = val*2;
	}

	/**
	 * 寻找最近的为自适应布局的父容器
	 *
	 * @return 布局容器
	 *
	 *
	 * @date 2014-12-30-下午3:15:28
	 *
	 */
	public XLayoutContainer findNearestFit() {
		return this;
	}

	@Override
	public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
		return new WidgetPropertyUIProvider[]{ new BodyMobilePropertyUI(this)};
	}

	@Override
	public boolean isMovable() {
		return false;
	}

	public  boolean switch2FitBodyLayout(XCreator creator) {
		try {
			XWFitLayout xfl = (XWFitLayout) creator.getBackupParent();
			//备份一下组件间隔
			int compInterval = xfl.toData().getCompInterval();
			Component[] components = creator.getComponents();

			Arrays.sort(components, new ComparatorComponentLocation());

			xfl.getLayoutAdapter().removeBean(creator, creator.getWidth(), creator.getHeight());
			xfl.remove(creator);

			for (Component comp : components) {
				XCreator xCreator = (XCreator) comp;
				if (xCreator.shouldScaleCreator()) {
					XLayoutContainer parentPanel = xCreator.initCreatorWrapper(xCreator.getHeight());
					xfl.add(parentPanel, xCreator.toData().getWidgetName());
					parentPanel.updateChildBound(xfl.getActualMinHeight());
					continue;
				}
				xfl.add(xCreator);
			}
			//这边计算的时候会先把组件间隔去掉
			moveComponents2FitLayout(xfl);


			for (int i = 0; i < components.length; i++) {
				Component comp = xfl.getComponent(i);
				creator = (XCreator) comp;
				creator.setBackupBound(components[i].getBounds());
			}

			//把组件间隔加上
			if (xfl.toData().getCompInterval() != compInterval) {
				xfl.moveContainerMargin();
				xfl.moveCompInterval(xfl.getAcualInterval());
				xfl.toData().setCompInterval(compInterval);
				xfl.addCompInterval(xfl.getAcualInterval());
			}
			xfl.toData().setLayoutType(WBodyLayoutType.FIT);
			FormDesigner formDesigner = WidgetPropertyPane.getInstance().getEditingFormDesigner();
			formDesigner.getSelectionModel().setSelectedCreator(xfl);
			return true;
		} catch (Exception e) {
			FRLogger.getLogger().error(e.getMessage());
			return false;
		}
	}

	private void moveComponents2FitLayout(XWFitLayout xwFitLayout) {
		Component[] components = xwFitLayout.getComponents();
		if (components.length == 0) {
			xwFitLayout.updateBoundsWidget();
			return;
		}
		int layoutWidth = xwFitLayout.getWidth() - xwFitLayout.toData().getMargin().getLeft() - xwFitLayout.toData().getMargin().getRight();
		int layoutHeight = xwFitLayout.getHeight() - xwFitLayout.toData().getMargin().getTop() - xwFitLayout.toData().getMargin().getBottom();
		int leftMargin = xwFitLayout.toData().getMargin().getLeft();
		int topMargin = xwFitLayout.toData().getMargin().getTop();
		xwFitLayout.toData().setCompInterval(0);
		int row = (components.length / EACH_ROW_COUNT) + (components.length % EACH_ROW_COUNT == 0 ? 0 : 1);
		//最后一行的列数不定
		int column = components.length % EACH_ROW_COUNT == 0 ? EACH_ROW_COUNT : components.length % EACH_ROW_COUNT;
		int componentWidth = layoutWidth / EACH_ROW_COUNT;
		int componentHeight = layoutHeight / row;
		for (int i = 0; i < row - 1; i++) {
			for (int j = 0; j < EACH_ROW_COUNT; j++) {
				components[EACH_ROW_COUNT * i + j].setBounds(
						leftMargin + componentWidth * j,
						topMargin + componentHeight * i,
						j == EACH_ROW_COUNT - 1 ? layoutWidth - componentWidth * (EACH_ROW_COUNT - 1) : componentWidth,
						componentHeight
				);
			}
		}
		//最后一行列数是特殊的，要单独处理
		int lastRowWidth = layoutWidth / column;
		int lastRowHeight = layoutHeight - componentHeight * (row - 1);
		for (int i = 0; i < column; i++) {
			components[EACH_ROW_COUNT * (row - 1) + i].setBounds(
					leftMargin + lastRowWidth * i,
					topMargin + componentHeight * (row - 1),
					i == column - 1 ? layoutWidth - lastRowWidth * (column - 1) : lastRowWidth,
					lastRowHeight
			);
		}
		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof XWCardMainBorderLayout) {
				((XWCardMainBorderLayout) components[i]).recalculateChildWidth(components[i].getWidth(), false);
				((XWCardMainBorderLayout) components[i]).recalculateChildHeight(components[i].getHeight(), false);
			}
			xwFitLayout.dealDirections((XCreator) components[i], false);
		}
		xwFitLayout.updateBoundsWidget();
	}

	//以组件的位置来确定先后顺序，y小的在前，x小的在前
	private class ComparatorComponentLocation implements Comparator {
		@Override
		public int compare(Object o1, Object o2) {
			if (((Component) o1).getY() < ((Component) o2).getY()) {
				return -1;
			} else if (((Component) o1).getY() > ((Component) o2).getY()) {
				return 1;
			} else {
				if (((Component) o1).getX() < ((Component) o2).getX()) {
					return -1;
				} else if (((Component) o1).getX() > ((Component) o2).getX()) {
					return 1;
				} else {
					return 0;
				}
			}
		}
	}
}