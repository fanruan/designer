package com.fr.design.gui.frpane;

import com.fr.design.gui.controlpane.NameableCreator;

/**
 * 超级链接 支持的类型 种类. 
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-26 下午04:41:55
 */
public interface HyperlinkGroupType {
	
	/**
	 * 返回支持的超级链接类型
	 * @return NameableCreator[]
	 */
	public NameableCreator[] getHyperlinkCreators();

}