/**
 * 
 */
package com.fr.design.designer.creator.cardlayout;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.border.Border;

import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWidgetCreator;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;

/**
 *
 *
 * @date: 2014-12-1-下午7:26:21
 */
public class XWCardTitleLayout extends XWBorderLayout {
	
	private static final int CENTER = 1;

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
	
    /**
     * 将WLayout转换为XLayoutContainer
     */
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

		XWCardMainBorderLayout mainLayout = (XWCardMainBorderLayout) this.getBackupParent();
		if(mainLayout != null){
			XWCardLayout cardLayout = mainLayout.getCardPart();
			selectionModel.setSelectedCreator(cardLayout);
		}
		
		if (editingMouseListener.stopEditing()) {
			if (this != designer.getRootComponent()) {
				ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
				editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
			}
		}
    }
}