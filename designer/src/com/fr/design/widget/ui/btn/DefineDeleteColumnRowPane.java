package com.fr.design.widget.ui.btn;

import javax.swing.BorderFactory;
import com.fr.design.gui.ilable.UILabel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.editor.ColumnRowEditor;
import com.fr.general.Inter;
import com.fr.report.web.button.write.DeleteRowButton;

/**
 * Created by IntelliJ IDEA. Author : Richer Version: 6.5.6 Date : 11-11-16 Time
 * : 上午10:56
 */
public class DefineDeleteColumnRowPane extends BasicPane {
    private static final int BORDER_LEFT = -10;
	private ColumnRowEditor crEditor;


	public DefineDeleteColumnRowPane() {
		initComponents();
	}

	private void initComponents() {
		crEditor = new ColumnRowEditor();
		setLayout(FRGUIPaneFactory.createL_FlowLayout());
		setBorder(BorderFactory.createEmptyBorder(0, BORDER_LEFT, 0, 0));
		add(new UILabel(Inter.getLocText(new String[]{"Specify", "Cell"}) + ":"));
		add(crEditor);
		add(new UILabel(Inter.getLocText("Append_Delete_Row_Message")));
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Button");
	}

	public void populate(DeleteRowButton btn) {
		crEditor.setValue(btn.getFixCell());
	}

	public void update(DeleteRowButton btn) {
		btn.setFixCell(crEditor.getValue());
	}
}