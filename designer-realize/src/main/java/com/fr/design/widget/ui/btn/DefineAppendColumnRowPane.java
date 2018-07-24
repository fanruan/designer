package com.fr.design.widget.ui.btn;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.editor.ColumnRowEditor;

import com.fr.report.web.button.write.AppendRowButton;

/**
 * Created by IntelliJ IDEA. Author : Richer Version: 6.5.6 Date : 11-11-16 Time
 * : 上午9:34
 */
public class DefineAppendColumnRowPane extends BasicPane {
	private ColumnRowEditor crEditor;
	private UISpinner jNumberEditor;
	private UILabel rowCountLable;

	public DefineAppendColumnRowPane() {
		this.initComponents();
	}

	private void initComponents() {
		double f = TableLayout.FILL;

		double p = TableLayout.PREFERRED;
		double rowSize[] = { p, p ,p};
		double columnSize[] = { p, f};

		crEditor = new ColumnRowEditor();
		jNumberEditor = new UISpinner(0, 100 , 1, 0);

		rowCountLable = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Edit-Row_Count"));
		JPanel lpane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		lpane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Append_Delete_Row_Message"));
		label.setForeground(new Color(0x8F8F92));
		lpane.add(label);
		Component[][] components = { { new UILabel(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"Specify", "Cell"})), crEditor }, { rowCountLable, jNumberEditor } ,{lpane,null}};
		JPanel contentPane = TableLayoutHelper.createGapTableLayoutPane(components, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.add(contentPane);
	}

	@Override
	protected String title4PopupWindow() {
		return "Button";
	}

	public void populate(AppendRowButton btn) {
		crEditor.setValue(btn.getFixCell());
		jNumberEditor.setValue(btn.getCount());
	}

	public void update(AppendRowButton btn) {
		btn.setFixCell(crEditor.getValue());
		btn.setCount((int)jNumberEditor.getValue());
	}
}