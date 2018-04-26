/**
 * 
 */
package com.fr.design.designer.properties;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.mainframe.widget.editors.IntegerPropertyEditor;
import com.fr.design.mainframe.widget.editors.PropertyCellEditor;
import com.fr.design.mainframe.widget.editors.StringEditor;
import com.fr.form.ui.CardSwitchButton;
import com.fr.form.ui.container.cardlayout.WCardMainBorderLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.form.ui.container.cardlayout.WCardTitleLayout;
import com.fr.form.ui.container.cardlayout.WTabFitLayout;
import com.fr.general.Inter;

/**
 * tab布局tabFit属性表
 * 
 * @author focus
 * @date 2014-6-24
 */
public class FRTabFitLayoutPropertiesGroupModel extends FRFitLayoutPropertiesGroupModel {
	
	private PropertyCellEditor titleEditor;
	private PropertyCellEditor gapEditor;
	private DefaultTableCellRenderer renderer;
	private WTabFitLayout layout;
	private XWTabFitLayout xfl;
	
	private static int PROPERTY_NAME_COLUMN = 0;
	private static int PROPERTY_VALUE_COLUMN = 1;
	
	public FRTabFitLayoutPropertiesGroupModel(XWTabFitLayout xfl){
		super(xfl);
		this.xfl = xfl;
		this.layout = (WTabFitLayout) xfl.toData();
		renderer = new DefaultTableCellRenderer();
		titleEditor = new PropertyCellEditor(new StringEditor());
		gapEditor = new PropertyCellEditor(new IntegerPropertyEditor());
	}

	/** 
	 * 布局管理器自己的属性
	 */
	@Override
	public String getGroupName() {
		return Inter.getLocText("FR-Designer_Current_tab");
	}

	@Override
	public int getRowCount() {
		return 2;
	}

	@Override
	public TableCellRenderer getRenderer(int row) {
		return renderer;
	}

	@Override
	public TableCellEditor getEditor(int row) {
		switch(row){
			case 0:
				return gapEditor;
			default:
				return titleEditor;
		}
	}

	@Override
	public Object getValue(int row, int column) {
		if (column == PROPERTY_NAME_COLUMN) {
            switch (row) {
                case 0:
                    return Inter.getLocText("FR-Designer_Component_Interval");
                default :
                    return Inter.getLocText("FR-Engine_Tab_Layout_Title");
            }
        } else {
            switch (row) {
                case 0:
                    return layout.getCompInterval();
                default :
                	return  getTitle();
            }
        }
	}

	@Override
	public boolean setValue(Object value, int row, int column) {
		if(column == PROPERTY_VALUE_COLUMN){
			if(row == 0){
				int gap = Integer.parseInt(String.valueOf(value));
				setLayoutGap(gap);
				return true;
			}else{
				layout.getCurrentCard().setText((String.valueOf(value)));
				return true;
			}
		}
		return false;
	}
	
	private void setLayoutGap(int gap) {
		if(xfl.canAddInterval(gap)){
			int  interval = layout.getCompInterval();
	    	if (gap != interval) {
	    		xfl.moveContainerMargin();
	    		xfl.moveCompInterval(xfl.getAcualInterval());
	    		layout.setCompInterval(gap);
	    		xfl.addCompInterval(xfl.getAcualInterval());
	    	}
		}
	}
	
	//获取标题
	private String getTitle(){
		if(layout.getCurrentCard() == null){
			layout.setCurrentCard(getRelateSwitchButton());
		}
		return layout.getCurrentCard().getText();
	}
	
	//获取layout对应的switchButton,由于暂时没有把xcardSwitchButton存到xml中，，关闭打开后，暂时根据固定的父子层关系获取
	private CardSwitchButton getRelateSwitchButton(){
		int index = layout.getIndex();
		
		XWCardLayout cardLayout = (XWCardLayout)xfl.getBackupParent();
		XWCardMainBorderLayout  border = (XWCardMainBorderLayout)cardLayout.getBackupParent();
		WCardMainBorderLayout borderLayout = border.toData();
		WCardTitleLayout titleLayout = borderLayout.getTitlePart();
		if(titleLayout == null){
			return null;
		}
		
		WCardTagLayout tagLayout = titleLayout.getTagPart();
		return tagLayout == null ? null : tagLayout.getSwitchButton(index);
	}
	
	/**
	 * 是否可编辑
	 * @param row 行
	 * @return 否
	 */
	@Override
	public boolean isEditable(int row) {
		return true;
	}

}