package com.fr.design.mainframe.chart.gui.style;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import com.fr.chart.chartattr.Axis;
import com.fr.chart.chartattr.CategoryAxis;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

/**
 * 坐标轴 值类型界面(文本坐标轴 或者 日期坐标轴).
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-4 上午10:15:35
 */
public class ChartAxisValueTypePane extends BasicPane {
	
	private UIComboBoxPane boxPane;
	
	private DateAxisValuePane dateAxisPane;
	private TextAxisValueTypePane textAxisPane;
	
	public ChartAxisValueTypePane() {
		this.setLayout(new BorderLayout());
		
		boxPane = new UIComboBoxPane<Axis>() {

			protected List<FurtherBasicBeanPane<? extends Axis>> initPaneList() {
				
				List list = new ArrayList<FurtherBasicBeanPane>();
				
				list.add(dateAxisPane = new DateAxisValuePane());
				list.add(textAxisPane = new TextAxisValueTypePane());
				return list;
			}

			protected String title4PopupWindow() {
				return "";
			}
		};
		
		this.add(boxPane, BorderLayout.NORTH);
	}
	
	/**
	 * 界面标题.
	 */
	protected String title4PopupWindow() {
		return Inter.getLocText("AxisValue");
	}
	
	/**
	 * 判断类型, 更新界面属性
	 */
	public void populateBean(CategoryAxis axis) {
		if(axis != null && axis.isDate()) {
			boxPane.setSelectedIndex(0);
			dateAxisPane.populateBean(axis);
		} else {
			boxPane.setSelectedIndex(1);
		}
	}
	
	/**
	 * 保存界面属性.
	 */
	public void updateBean(CategoryAxis axis) {
		if(boxPane.getSelectedIndex() == 0) {
			dateAxisPane.updateBean(axis);
			axis.setDate(true);
		} else {
			axis.setDate(false);
			textAxisPane.updateBean(axis);
		}
	}
	
	//甘特图的时间坐标轴不能有文本坐标轴的下拉项
	public void removeTextAxisPane(){
		if(this.boxPane.getUIComboBox().getItemCount() > 1){ //防止多次调用
			this.boxPane.getUIComboBox().removeItemAt(1);
			this.boxPane.getCards().remove(1);
		}
	}
	
	/**
	 * 文本坐标轴.
	* @author kunsnat E-mail:kunsnat@gmail.com
	* @version 创建时间：2013-1-4 上午10:55:05
	 */
	private class TextAxisValueTypePane extends FurtherBasicBeanPane<CategoryAxis> {
		
		/**
		 *  判断界面接受.
		 */
		public boolean accept(Object ob) {
			return ob instanceof CategoryAxis;
		}

		/**
		 * 重置
		 */
		public void reset() {
			
		}

		/**
		 * 界面标题
		 */
		public String title4PopupWindow() {
			return Inter.getLocText("Chart_Text_Axis");
		}

		/**
		 * 更新界面 donothing
		 */
		public void populateBean(CategoryAxis ob) {
		}

		/**
		 * 保存界面属性. donothing
		 */
		public CategoryAxis updateBean() {
			return null;
		}
		
		/**
		 * 保存界面 donothing
		 */
		public void updateBean(CategoryAxis axis) {
			
		}
	}
}