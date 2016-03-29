package com.fr.quickeditor.cellquick;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.fr.design.actions.insert.cell.RichTextCellAction;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.general.Inter;
import com.fr.quickeditor.CellQuickEditor;

public class CellRichTextEditor extends CellQuickEditor {
	private UIButton subReportButton;
	private static CellRichTextEditor THIS;
	
	public static final CellRichTextEditor getInstance() {
		if(THIS == null) {
			THIS = new CellRichTextEditor();
		}
		return THIS;
	}

	private CellRichTextEditor() {
		super();
	}

	/**
	 * 创建界面上中间的部分
	 * 
	 * @return 界面元素
	 * 
	 *
	 * @date 2014-12-7-下午9:41:52
	 * 
	 */
	public JComponent createCenterBody() {
		subReportButton = new UIButton();
		subReportButton.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		subReportButton.setMargin(null);
		subReportButton.setOpaque(false);
		return subReportButton;
	}

	@Override
	protected void refreshDetails() {
		RichTextCellAction subReportCellAction = new RichTextCellAction(tc);
		subReportCellAction.setName(Inter.getLocText("FR-Designer_RichTextEditor"));
		subReportButton.setAction(subReportCellAction);
	}

}