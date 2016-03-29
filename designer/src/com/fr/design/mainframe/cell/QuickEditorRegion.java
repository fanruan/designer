package com.fr.design.mainframe.cell;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.design.selection.QuickEditor;

/**
 * QuickEditorRegion
 * 
 * @editor zhou
 * @since 2012-3-23下午3:21:36
 */

public class QuickEditorRegion extends JPanel {

	public static QuickEditorRegion getInstance() {
		return singleton;
	}
	
	public static JPanel getEmptyEditor() {
		if(EMPTY == null) {
			EMPTY = new JPanel(new BorderLayout());
			UILabel content = new UILabel(Inter.getLocText(new String[]{"DataFunction-None", "HJS-Message", "Form-Widget_Property_Table"})+"!");
			content.setBorder(BorderFactory.createEmptyBorder(0, 70, 0, 0));
			EMPTY.add(content, BorderLayout.CENTER);
		}
		return EMPTY;
	}

	private static QuickEditorRegion singleton = new QuickEditorRegion();
	private static JPanel EMPTY;

	private QuickEditorRegion() {
		this.setLayout(new BorderLayout());
	}

	/**
	 * 传值
	 * 
	 * @param ePane
	 */
	public void populate(final QuickEditor quickEditor) {
		this.removeAll();
		if(quickEditor.getComponentCount() == 0) {
			this.add(getEmptyEditor(), BorderLayout.CENTER);
		} else {
			this.add(quickEditor, BorderLayout.CENTER);
		}
		validate();
		repaint();
	}
}