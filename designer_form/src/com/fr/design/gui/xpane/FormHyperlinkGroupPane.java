package com.fr.design.gui.xpane;

import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.form.javascript.FormEmailPane;
import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.js.EmailJavaScript;

public class FormHyperlinkGroupPane extends HyperlinkGroupPane{
	private static FormHyperlinkGroupPane singleton;

	protected FormHyperlinkGroupPane(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
		super(hyperlinkGroupPaneActionProvider);
	}

	public synchronized static FormHyperlinkGroupPane getInstance(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
		if (singleton == null) {
			singleton = new FormHyperlinkGroupPane(hyperlinkGroupPaneActionProvider);
		}
		return singleton;
	}

	/**
     * 生成添加按钮的NameableCreator
     * 由于表单报表块的单元格超链和单元格条件属性超链中的emailPane都要用表单的emailPane，这里调整下
     *
     * @return 返回Nameable按钮数组.
     */
	 @Override
     public NameableCreator[] createNameableCreators() {
		 NameableCreator[] creators = super.createNameableCreators();
		 for (int i=0; i<creators.length; i++) {
			 if (ComparatorUtils.equals(creators[i].menuName(), Inter.getLocText("FR-Designer_Email"))) {
				 creators[i] = new NameObjectCreator(Inter.getLocText("FR-Designer_Email"), EmailJavaScript.class, FormEmailPane.class);
				 break;
			 }
		 }
		 return creators;
	 }
}