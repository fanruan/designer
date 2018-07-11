/**
 * 
 */
package com.fr.poly.hanlder;

import javax.swing.event.MouseInputAdapter;

import com.fr.report.poly.PolyWorkSheet;

/**
 * @author neil
 *
 * @date: 2015-2-11-下午3:48:29
 */
public abstract class BlockOperationMouseHandler extends MouseInputAdapter {
	
	/**
	 * 获取当期编辑对象
	 * 
	 * @return 编辑对象
	 * 
	 * @date 2015-2-12-下午3:20:49
	 * 
	 */
	protected abstract PolyWorkSheet getTarget();
	
}