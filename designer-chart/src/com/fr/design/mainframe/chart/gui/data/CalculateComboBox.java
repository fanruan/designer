package com.fr.design.mainframe.chart.gui.data;

import com.fr.data.util.function.AbstractDataFunction;
import com.fr.data.util.function.AverageFunction;
import com.fr.data.util.function.CountFunction;
import com.fr.data.util.function.MaxFunction;
import com.fr.data.util.function.MinFunction;
import com.fr.data.util.function.NoneFunction;
import com.fr.data.util.function.SumFunction;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;

/**
 * 公式选择.
* @author kunsnat E-mail:kunsnat@gmail.com
* @version 创建时间：2013-1-8 上午09:52:15
 */
public class CalculateComboBox extends UIComboBox{

	public static final String[] CALCULATE_ARRAY = {Inter.getLocText("DataFunction-None"), Inter.getLocText("DataFunction-Sum"),
			Inter.getLocText("DataFunction-Average"), Inter.getLocText("DataFunction-Max"), 
			Inter.getLocText("DataFunction-Min"), Inter.getLocText("DataFunction-Count")};
	public static final Class[] CLASS_ARRAY = {NoneFunction.class, SumFunction.class, AverageFunction.class, 
			MaxFunction.class, MinFunction.class, CountFunction.class};
	
	public CalculateComboBox() {
		super(CALCULATE_ARRAY);
		setSelectedIndex(0);
	}
	
	public void reset() {
		this.setSelectedItem(Inter.getLocText("DataFunction-None"));
	}
	
	/**
	 * 更新公式选择.
	 */
	public void populateBean(AbstractDataFunction function) {
		for(int i = 0; i < CLASS_ARRAY.length; i++) {
			if(function != null && ComparatorUtils.equals(function.getClass(), CLASS_ARRAY[i])) {
				setSelectedIndex(i);
				break;
			}
		}
	}
	
	/**
	 * 返回当前选择的公式
	 */
	public AbstractDataFunction updateBean() {
		try {
			int selectIndex = getSelectedIndex();
			if(selectIndex >= 0 && selectIndex < CLASS_ARRAY.length) {
				return (AbstractDataFunction)CLASS_ARRAY[selectIndex].newInstance();
			}
		} catch (InstantiationException e) {
			FRLogger.getLogger().error("Function Error");
			return null;
		} catch (IllegalAccessException e) {
			FRLogger.getLogger().error("Function Error");
			return null;
		}
		
		return null;
	}
}