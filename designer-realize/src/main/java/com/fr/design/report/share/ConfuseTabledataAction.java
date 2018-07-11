/**
 * 
 */
package com.fr.design.report.share;

import java.util.HashMap;

import com.fr.data.impl.EmbeddedTableData;
import com.fr.general.GeneralUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

/**
 * 混淆传入的tabledata
 * 
 * @author neil
 *
 * @date: 2015-3-10-上午10:45:41
 */
public class ConfuseTabledataAction {

	/**
	 * 混淆指定的内置数据集
	 * 
	 * @param info 混淆相关的信息
	 * @param tabledata 需要混淆的数据集
	 * 
	 */
    public void confuse(ConfusionInfo info, EmbeddedTableData tabledata){
		int rowCount = tabledata.getRowCount();
		String[] keys = info.getConfusionKeys();
		for (int j = 0, len = ArrayUtils.getLength(keys); j < len; j++) {
			if(StringUtils.isEmpty(keys[j])){
				continue;
			}
			
			//缓存下已经混淆的数据, 这样相同的原始数据, 混淆出来的结果是一致的.
			HashMap<Object, Object> cachedValue = new HashMap<Object, Object>();
			for (int k = 0; k < rowCount; k++) {
				Object oriValue = tabledata.getValueAt(k, j);
				Object newValue;
				if(cachedValue.containsKey(oriValue)){
					newValue = cachedValue.get(oriValue);
				}else{
					newValue = confusionValue(info, j, keys[j], cachedValue, oriValue);
					cachedValue.put(oriValue, newValue);
				}
				
				tabledata.setValueAt(newValue, k, j);
			}
		}
    }
    
    //混淆每一个格子的数据
    private Object confusionValue(ConfusionInfo info, int colIndex, String key, HashMap<Object, Object> cachedValue, Object oriValue){
		if (info.isNumberColumn(colIndex)){
			//如果是数字格式的, 那么就做乘法, eg: 3 * 3, 8 *3.....
			Number keyValue = GeneralUtils.objectToNumber(key, false);
			Number oriNumber = GeneralUtils.objectToNumber(oriValue, false);
			return oriNumber.doubleValue() * keyValue.doubleValue();
		}
		
		String oriStrValue = GeneralUtils.objectToString(oriValue);
		if(StringUtils.isEmpty(oriStrValue)){
			//如果是空字段, 就默认不混淆. 因为有的客户他就是留了空字段来做一些过滤, 条件属性之类的.
			return oriStrValue;
		}
		
		//默认其他格式的就做加法, eg: 地区1, 地区2......
		return key + cachedValue.size();
    }

}