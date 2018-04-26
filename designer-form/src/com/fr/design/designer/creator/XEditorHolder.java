package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.xpane.ToolTipEditor;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.form.ui.EditorHolder;

import javax.swing.*;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;

public class XEditorHolder extends XWidgetCreator {

	private DesignerEditor<UILabel> designerEditor;
	private static Icon icon = BaseUtils.readIcon("/com/fr/design/images/form/designer/holder.png");

//	public XEditorHolder(EditorHolder widget) {
//		super(widget);
//	}
	
	public XEditorHolder(EditorHolder widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	 * 响应点击事件
	 * 
	 * @param editingMouseListener 事件处理器
	 * @param e 点击事件
	 * 
	 */
	public void respondClick(EditingMouseListener editingMouseListener,MouseEvent e){
		
		FormDesigner designer = editingMouseListener.getDesigner();
		if (editingMouseListener.stopEditing()) {
			ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
			editingMouseListener.startEditing(this, adapter.getDesignerEditor(), adapter);
			Rectangle rect = this.getBounds();
			int min = rect.x + rect.width / 2 - editingMouseListener.getMinMoveSize();
			int max = rect.x + rect.width / 2 + editingMouseListener.getMinMoveSize();
			if (e.getX() > min && e.getX() < max) {
				ToolTipEditor.getInstance().showToolTip((XEditorHolder) this, e.getXOnScreen(),
						e.getYOnScreen());
			}
		
		}
	}

	@Override
	protected String getIconName() {
		return "text_field_16.png";
	}

	@Override
	public DesignerEditor<UILabel> getDesignerEditor() {
		if (designerEditor == null) {
			UILabel comp = new UILabel(icon);
			designerEditor = new DesignerEditor<UILabel>(comp);

			comp.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
				}

				@Override
				public void focusLost(FocusEvent e) {
					ToolTipEditor.getInstance().hideToolTip();

				}

			});

			comp.setBorder(BorderFactory.createLineBorder(new Color(128, 152, 186)));

		}
		return designerEditor;
	}

	@Override
	protected JComponent initEditor() {
		if (editor == null) {
			editor = new UILabel(icon);
			editor.setBorder(BorderFactory.createLineBorder(new Color(128, 152, 186)));
		}
		return editor;
	}
}