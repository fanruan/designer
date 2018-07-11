package com.fr.design.gui.itable;

import javax.swing.JComponent;
import com.fr.design.gui.ilable.UILabel;

/**
 * 保持 编辑控件 存在 不toString
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-1-6 下午03:37:18
 */
public class UIComponentTable extends UITable {
	
	public UIComponentTable(int columnSize) {
		this.setModel(new UITableComponentModel(columnSize));
		initComponents();
	}
	
	/**
	 * @param value  该行列的值(字符串)
	 * @param row
	 * @param column
	 * @return 列表中默认显示的东西，如果有很多内容，可以装载一个JPanel里再嵌进来
	 */
	protected JComponent getRenderCompoment(Object value, int row, int column) {
		UILabel text = new UILabel();
		if (value != null) {
			text.setText(value.toString());
		}
		if(value instanceof JComponent) {
			return (JComponent)value;
		}
		return text;
	}

}