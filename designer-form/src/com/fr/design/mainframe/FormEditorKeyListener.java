package com.fr.design.mainframe;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;

public class FormEditorKeyListener extends KeyAdapter{
	private FormDesigner designer;
	private boolean moved;

	public FormEditorKeyListener(FormDesigner formEditor) {
		this.designer = formEditor;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		XCreator creator = designer.getSelectionModel().getSelection().getSelectedCreator();
		XLayoutContainer container;
		if(creator == null ||  (container =XCreatorUtils.getParentXLayoutContainer(creator)) == null || !(container instanceof XWAbsoluteLayout)) {
			return;
		}
		moved = true;
		
		switch (code) {
		case KeyEvent.VK_LEFT:
			designer.getSelectionModel().move(-1, 0);
			break;
		case KeyEvent.VK_RIGHT:
			designer.getSelectionModel().move(1, 0);
			break;
		case KeyEvent.VK_UP:
			designer.getSelectionModel().move(0, -1);
			break;
		case KeyEvent.VK_DOWN:
			designer.getSelectionModel().move(0, 1);
			break;
		default:
			moved = false;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (moved) {
			designer.getEditListenerTable().fireCreatorModified(
					designer.getSelectionModel().getSelection().getSelectedCreator(), DesignerEvent.CREATOR_RESIZED);
			moved = false;
		}
	}
}