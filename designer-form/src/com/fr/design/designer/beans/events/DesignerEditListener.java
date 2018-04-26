package com.fr.design.designer.beans.events;

import java.util.EventListener;

/**
 * 界面设计组件触发的编辑处理器接口
 * @since 6.5.4
 * @author richer
 */
public interface DesignerEditListener extends EventListener {

	void fireCreatorModified(DesignerEvent evt);
	
}