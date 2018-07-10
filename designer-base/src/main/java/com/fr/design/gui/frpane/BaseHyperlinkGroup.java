package com.fr.design.gui.frpane;

import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.module.DesignModuleFactory;

/**
 * 基础的超级链接类型, 不包括图表相关超链.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-6-26 下午04:46:02
 */
public class BaseHyperlinkGroup implements HyperlinkGroupType {

	/**
	 * 返回支持的超级链接类型
	 *
	 * @return
	 */
	public NameableCreator[] getHyperlinkCreators() {
		return DesignModuleFactory.getCreators4Hyperlink();
	}

}