/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.beans.actions;

import com.fr.base.BaseUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.creator.XWidgetCreator;

/**
 * @author richer
 * @since 6.5.3
 */
public class ChangeNameAction extends FormUndoableAction {

	public ChangeNameAction(FormDesigner t) {
		super(t);
		
        this.setName(Inter.getLocText("Form-Change_Widget_Name"));
        this.setMnemonic('G');
        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
    }

	/**
	 * 重命名
	 * 
	 * @return 是否重命名成功
	 * 
	 */
    @Override
    public boolean executeActionReturnUndoRecordNeeded() {
        FormDesigner designer = getEditingComponent();
        if (designer == null) {
            return false;
        }
        
        // 如果选中了多个也只改变选中的第一个控件的名字
        XWidgetCreator creator = (XWidgetCreator) designer.getSelectionModel().getSelection().getSelectedCreator();
        if(creator == null) {
        	return false;
        }
        creator.ChangeCreatorName(designer, creator);
        return false;
    }
}