package com.fr.design.condition;

import com.fr.data.condition.CommonCondition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;

public class DSColumnSimpleLiteConditionPane extends DSColumnLiteConditionPane {
	
	@Override
	protected BasicBeanPane<CommonCondition> createUnFormulaConditionPane() {
		return new SimpleCommonConditionPane();
	}
	
	protected class SimpleCommonConditionPane extends CommonConditionPane {
		
		protected ValueEditorPane createValueEditorPane() {
			return ValueEditorPaneFactory.createNoCRNoColumnValueEditorPane();
		}
	}
}