package com.fr.design.designer.creator.cardlayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.IntrospectionException;

import javax.swing.border.Border;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRTabFitLayoutAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.CRPropertyDescriptor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormHierarchyTreePane;
import com.fr.design.mainframe.widget.editors.PaddingMarginEditor;
import com.fr.design.mainframe.widget.renderer.PaddingMarginCellRenderer;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Inter;

/**
 * @author focus
 * @date 2014-6-23
 */
public class XWTabFitLayout extends XWFitLayout {
	
	private static final int MIN_SIZE = 1;
	// tab布局在拖拽导致的缩放里（含间隔时），如果拖拽宽高大于组件宽高，会导致调整的时候找不到原来的组件
	// 这里先将拖拽之前的宽高先做备份
	private Dimension referDim;
	

	public Dimension getReferDim() {
		return referDim;
	}

	public void setReferDim(Dimension referDim) {
		this.referDim = referDim;
	}

	public XWTabFitLayout(){
		this(new WTabFitLayout(), new Dimension());
	}
	
	public XWTabFitLayout(WTabFitLayout widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	*  得到属性名
	 * @return 属性名
	* @throws IntrospectionException
	*/
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
	return  new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
					        .getLocText("FR-Designer_Form-Widget_Name")),
                new CRPropertyDescriptor("margin", this.data.getClass()).setEditorClass(PaddingMarginEditor.class)
                       .setRendererClass(PaddingMarginCellRenderer.class).setI18NName(Inter.getLocText("FR-Designer_Layout-Padding"))
                       .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced"),
                    };
	}
	
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRTabFitLayoutAdapter(this);
	}
	
	/**
	 * tab布局里删除XWTabFitLayout对应的tab按钮
	 * 
	 * @param creator 当前组件
	 * @param designer 表单设计器
	 * 
	 */
	public void deleteRelatedComponent(XCreator creator,FormDesigner designer){
		//逐层回溯找出相关的layout和对应的tab按钮
    	XWTabFitLayout fitLayout = (XWTabFitLayout)creator;
    	WTabFitLayout fit = (WTabFitLayout) fitLayout.toData();
    	//关联tabfitLayout和tab按钮的index
    	int index = fit.getIndex();
    	//放置tabFitLayout的cardLayout
    	XWCardLayout cardLayout = (XWCardLayout) fitLayout.getBackupParent();
    	XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) cardLayout.getBackupParent();
    	XWCardTitleLayout titleLayout = mainLayout.getTitlePart();
    	//放置tab按钮的tagLayout
    	XWCardTagLayout tagLayout = titleLayout.getTagPart();
    	WCardTagLayout tag = (WCardTagLayout) tagLayout.toData();
    	
    	//删除整个tab布局
    	if(tag.getWidgetCount() <= MIN_SIZE){
    		deleteTabLayout(mainLayout,designer);
    		return;
    	}
    	
    	//先删除对应的tab按钮
    	for(int i=0;i<tagLayout.getComponentCount();i++){
    		CardSwitchButton button = tag.getSwitchButton(i);
    		if(button.getIndex()==index){
    			tagLayout.remove(i);
    			break;
    		}
    	}
    	//刷新tab按钮和tabFitLayout的index
    	refreshIndex(tag,cardLayout,index);
    	
    	LayoutUtils.layoutRootContainer(designer.getRootComponent());
	}
	
	
	private void deleteTabLayout(XLayoutContainer mainLayout,FormDesigner designer){
		SelectionModel selectionModel = designer.getSelectionModel();
		if(mainLayout != null){
			selectionModel.setSelectedCreator(mainLayout);
			selectionModel.deleteSelection();
		}
		LayoutUtils.layoutRootContainer(designer.getRootComponent());
		FormHierarchyTreePane.getInstance().refreshRoot();
		selectionModel.setSelectedCreator(designer.getRootComponent());
	}
	
	private void refreshIndex(WCardTagLayout tag,XWCardLayout cardLayout,int index){
    	for(int i=0;i<tag.getWidgetCount();i++){
    		CardSwitchButton button = tag.getSwitchButton(i);
    		XWTabFitLayout tempFit = (XWTabFitLayout) cardLayout.getComponent(i);
    		WTabFitLayout tempFitLayout = (WTabFitLayout) tempFit.toData();
    		int currentFitIndex = tempFitLayout.getIndex();
    		int buttonIndex = button.getIndex();
    		if(buttonIndex > index){
    			button.setIndex(--buttonIndex);
    		}
    		if(currentFitIndex > index){
    			tempFitLayout.setIndex(--currentFitIndex);
    		}
    	}
	}
	
	/**
	 * tab布局里切换到相应的tab按钮
	 * @param comp 当前组件
	 * void
	 */
    public void seleteRelatedComponent(XCreator comp){
    	XWTabFitLayout fitLayout = (XWTabFitLayout)comp;
    	WTabFitLayout fit = (WTabFitLayout) fitLayout.toData();
    	int index = fit.getIndex();
    	XWCardLayout cardLayout = (XWCardLayout) fitLayout.getBackupParent();
    	XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) cardLayout.getBackupParent();
    	XWCardTitleLayout titleLayout = mainLayout.getTitlePart();
    	XWCardTagLayout tagLayout = titleLayout.getTagPart();
    	WCardTagLayout layout = (WCardTagLayout) tagLayout.toData();
    	for(int i=0;i<tagLayout.getComponentCount();i++){
    		CardSwitchButton button = layout.getSwitchButton(i);
    		button.setShowButton(button.getIndex()==index);
    	}
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
    public XLayoutContainer findNearestFit(){
    	XLayoutContainer parent = this.getBackupParent();
    	return parent == null ? null : parent.findNearestFit();
    } 
    
	/**
	 * 非顶层自适应布局的缩放
	 * @param percent 百分比
	 */
	public void adjustCompSize(double percent) {
		this.adjustCreatorsWhileSlide(percent);
	}
	
	/**
	 * 该布局需要隐藏，无需对边框进行操作
	 * @param border 边框
	 * 
	 */
    public void setBorder(Border border) {
    	return;
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
			updateCompsWidget();
		}
		layoutWidthResize(percent); 
		if (percent < 0 && this.getNeedAddWidth() > 0) {
			this.setSize(this.getWidth()+this.getNeedAddWidth(), this.getHeight());
			modifyEdgemostCreator(true);
		}
		addCompInterval(getAcualInterval());
		// 本次缩放结束，参照宽高清掉
		this.setReferDim(null);
		updateCompsWidget();
		this.toData().setContainerWidth(this.getWidth());
		updateWidgetBackupBounds();
		LayoutUtils.layoutContainer(this);
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
			updateCompsWidget();
		}
		layoutHeightResize(percent);
		if (percent < 0 && this.getNeedAddHeight() > 0) {
			this.setSize(this.getWidth(), this.getHeight()+this.getNeedAddHeight());
			modifyEdgemostCreator(false);
		}
		addCompInterval(getAcualInterval());
		updateCompsWidget();
		this.toData().setContainerHeight(this.getHeight());
		updateWidgetBackupBounds();
		LayoutUtils.layoutContainer(this);
	}
	
	public XLayoutContainer getOuterLayout(){
		XWCardLayout cardLayout = (XWCardLayout) this.getBackupParent();
		return cardLayout.getBackupParent();
	}
	
	// 更新内部组件的widget
	private void updateCompsWidget(){
		for(int m=0;m<this.getComponentCount();m++){
			XCreator childCreator = this.getXCreator(m);
			BoundsWidget wgt = this.toData().getBoundsWidget(childCreator.toData());
			wgt.setBounds(this.getComponent(m).getBounds());
			wgt.setBackupBounds(this.getComponent(m).getBounds());
		}
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
    	
    	// 比较组件大小和tab布局的大小的参照宽高
    	double referWidth = getReferWidth();
    	double referHeight = getReferHeight();
    	
    	for (int i=0, len=this.getComponentCount(); i<len; i++) {
    		Component comp = this.getComponent(i);
    		Rectangle rec = comp.getBounds();
    		Rectangle bound = new Rectangle(rec);
    		if (rec.x > 0) {
    			bound.x -= val;
    			bound.width += val;
    		}
    		if (rec.width+rec.x < referWidth) {
    			bound.width  += val;
    		}
    		if (rec.y > 0) {
    			bound.y -= val;
    			bound.height += val;
    		}
    		if (rec.height+rec.y < referHeight) {
    			bound.height += val;
    		}
    		comp.setBounds(bound);
    	}
	
    	this.hasCalGap = false;
    }
    
    private double getReferWidth(){
    	if(referDim != null){
    		return referDim.getWidth();
    	}else{
    		return this.getWidth();
    	}
    }
    
    private double getReferHeight(){
    	if(referDim != null){
    		return referDim.getHeight();
    	}else{
    		return this.getHeight();
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
    	double referWidth = getReferWidth();
    	double referHeight = getReferHeight();
    	for (int i=0, len=this.getComponentCount(); i<len; i++) {
    		Component comp = this.getComponent(i);
    		Rectangle rec = comp.getBounds();
    		Rectangle bound = new Rectangle(rec);
    		if (rec.x > 0) {
    			bound.x += val;
    			bound.width -= val;
    		}
    		if (rec.width+rec.x < referWidth) {
    			bound.width  -= val;
    		}
    		if (rec.y > 0) {
    			bound.y += val;
    			bound.height -= val;
    		}
    		if (rec.height+rec.y < referHeight) {
    			bound.height -= val;
    		}
    		comp.setBounds(bound);
    	}
	
    	this.hasCalGap = true;
    }

	@Override
	public XLayoutContainer getTopLayout() {
		return this.getBackupParent().getTopLayout();
	}
}