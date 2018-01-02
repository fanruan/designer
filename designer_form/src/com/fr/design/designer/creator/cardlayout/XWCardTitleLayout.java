/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.border.Border;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WTabDisplayPosition;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;

/**
 *
 *
 * @date: 2014-12-1-下午7:26:21
 */
public class XWCardTitleLayout extends XWBorderLayout {
	
	private static final int CENTER = 1;

	private static final int LAYOUT_INDEX = 0;

	private static final int POSISIONT_OFFSET = 1;


	/**
	 * 构造函数
	 */
	public XWCardTitleLayout() {
	
	}
	
	/**
	 * 构造函数
	 */
	public XWCardTitleLayout(WCardTitleLayout widget, Dimension initSize) {
		super(widget, initSize);
	}


	public WTabDisplayPosition getDisplayPosition(){
		return ((WCardTagLayout)this.getTagPart().toData()).getDisplayPosition();
	}

	/**
	 * 控件树不显示此组件
	 * @param path 控件树list
	 */
	@Override
	public void notShowInComponentTree(List<Component> path) {
		path.remove(LAYOUT_INDEX);
	}

	@Override
	public int getIndexOfChild(Object child) {
		int count = getComponentCount();
		for (int i = 0; i < count; i++) {
			Component comp = getComponent(i);
			if (comp == child) {
				return i - POSISIONT_OFFSET;
			}
		}
		return -1;
	}
	
    /**
     * 将WLayout转换为XLayoutContainer
     */
	@Override
	public void convert(){
        isRefreshing = true;
        WCardTitleLayout titleLayout = (WCardTitleLayout)this.toData();
        this.setVisible(titleLayout.isVisible());
        this.removeAll();
        String[] arrs = {WBorderLayout.NORTH, WBorderLayout.SOUTH, WBorderLayout.EAST, WBorderLayout.WEST, WBorderLayout.CENTER};
        for (int i = 0; i < arrs.length; i++) {
            Widget wgt = titleLayout.getLayoutWidget(arrs[i]);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, arrs[i]);
                comp.setBackupParent(this);
            }
        }
        isRefreshing = false;
	}
	
	/**
	 * 获取标签区域
	 * 
	 * @return 标签区域
	 * 
	 *
	 * @date 2014-12-10-下午1:50:04
	 * 
	 */
	public XWCardTagLayout getTagPart(){
		return (XWCardTagLayout) this.getComponent(CENTER);
	}
	
	/**
	 * 添加标签区域
	 * 
	 * @param tagPart 标签区域
	 * 
	 *
	 * @date 2014-12-10-下午1:49:40
	 * 
	 */
	public void addTagPart(XWCardTagLayout tagPart){
		this.add(tagPart, WBorderLayout.CENTER);
	}
	
	/**
	 * 添加新建按钮
	 * 
	 * @param addBtn 新建按钮
	 * 
	 *
	 * @date 2014-12-10-下午1:49:19
	 * 
	 */
	public void addNewButton(XCardAddButton addBtn){
		this.add(addBtn, WBorderLayout.EAST);
	}

	public void resetNewBtnPosition(WTabDisplayPosition wTabDisplayPosition){
		XCardAddButton xCardAddButton = (XCardAddButton) this.getComponent(0);
		switch (wTabDisplayPosition){
			case TOP_POSITION:
				this.add(xCardAddButton, WBorderLayout.EAST);
				break;
			case LEFT_POSITION:
				this.add(xCardAddButton, WBorderLayout.SOUTH);
				break;
			case BOTTOM_POSITION:
				this.add(xCardAddButton, WBorderLayout.EAST);
				break;
			case RIGHT_POSITION:
				this.add(xCardAddButton, WBorderLayout.SOUTH);
				break;
			default:
				break;
		}
		//需要重新添加一次保证组件顺序不变(重新初始化CardTagLayout改变内部布局)
		XWCardTagLayout xwCardTagLayout =  (XWCardTagLayout) this.getComponent(0);
		this.addTagPart(xwCardTagLayout);
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
    
    /**
     * 该布局隐藏，无需对边框进行操作
     * @param border 边框
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

		if (e.getClickCount() <= 1) {
			selectionModel.selectACreatorAtMouseEvent(e);
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

	public String createDefaultName() {
		return "tabpane";
	}


	@Override
	public XCreator getXCreator() {
		return (XCreator)this.getComponent(1);
	}

}