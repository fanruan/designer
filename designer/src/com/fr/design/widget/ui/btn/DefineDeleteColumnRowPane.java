package com.fr.design.widget.ui.btn;

import javax.swing.*;

import com.fr.design.gui.ilable.UILabel;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.editor.ColumnRowEditor;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.report.web.button.write.DeleteRowButton;

import java.awt.*;

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
		double f = TableLayout.FILL;

		double p = TableLayout.PREFERRED;
		double rowSize[] = { p, p};
		double columnSize[] = { p, f};
		crEditor = new ColumnRowEditor();
		Component[][] components = {
				{ new UILabel(Inter.getLocText(new String[]{"Specify", "Cell"}) + ":"), crEditor },
				{new UILabel(Inter.getLocText("Append_Delete_Row_Message")),null}};
		JPanel contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);

		setLayout(FRGUIPaneFactory.createBorderLayout());

		add(contentPane);
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