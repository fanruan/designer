/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;



import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;

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
	public static final Color DEFAULT_BORDER_COLOR = new Color(210,210,210);
	private static final int LAYOUT_INDEX = 0;
	private static final int TITLE_STYLE = 2;
	private static final int NORMAL_STYLE = 1;
	
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
	public void addTitlePart(XWCardTitleLayout title){
		this.add(title, WBorderLayout.NORTH);
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
    	switch(this.getComponentCount()){
    		case TITLE_STYLE:
    			return (XCreator)this.getComponent(TITLE_STYLE-1);
    		case NORMAL_STYLE:
    			return (XCreator)this.getComponent(NORMAL_STYLE-1);
    		default:
    			return this;
    	}
    }
	/**
	 * 控件树不显示此组件
	 * @param path 控件树list
	 */
	public void notShowInComponentTree(ArrayList<Component> path) {
		path.remove(LAYOUT_INDEX);
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
     * @param 宽度
     */
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
					BoundsWidget wgt = tabLayout.toData()
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
					BoundsWidget wgt = tabLayout.toData()
							.getBoundsWidget(childCreator.toData());
					wgt.setBounds(tabLayout.getComponent(m).getBounds());
				}
				tabLayout.adjustCreatorsHeight(percent);
			}
		}
    
    }
}