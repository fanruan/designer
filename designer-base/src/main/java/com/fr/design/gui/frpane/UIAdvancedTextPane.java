package com.fr.design.gui.frpane;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.menu.KeySetUtils;


import static com.fr.design.gui.syntax.ui.rtextarea.RTADefaultInputMap.DEFAULT_MODIFIER;

/**
 * p:这是一个增强的JTextPane,支持很多Action 
 */
public class UIAdvancedTextPane extends UITextPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UpdateAction undoAction, redoAction, 
					cutAction, copyAction, pasteAction;
	private UndoManager undoManager = new UndoManager();

	/**
	 * Constructor
	 */
	public UIAdvancedTextPane() {
		undoAction = new UndoAction();
		redoAction = new RedoAction();
		cutAction = new CutAction();
		copyAction = new CopyAction();
		pasteAction = new PasteAction();

		applyDocument();
		
		//p:当调用setDocument()方法的时候，调用这个property change的方法.
		this.addPropertyChangeListener("document", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				applyDocument();
			}
		});
	}

    //p:将Listener加到Document里面
    private void applyDocument() {
		Document textDocument = getDocument();
		textDocument.addUndoableEditListener(new UndoableEditListener() {
	        public void undoableEditHappened(UndoableEditEvent evt) {
	        	UndoableEdit undoableEdit = evt.getEdit();      	
				if(undoableEdit instanceof DefaultDocumentEvent) {
					DefaultDocumentEvent comEdit = (DefaultDocumentEvent) undoableEdit;					
					if(comEdit.getType() == DocumentEvent.EventType.CHANGE) {
						return;
					}
				}
				
	            undoManager.addEdit(evt.getEdit());
	        }
	    });
		this.getInputMap().put((KeyStroke)undoAction.getValue(Action.ACCELERATOR_KEY), "undo");
		this.getInputMap().put((KeyStroke)redoAction.getValue(Action.ACCELERATOR_KEY), "redo");
	    this.getActionMap().put("undo", undoAction);
	    this.getActionMap().put("redo", redoAction);	    
    }

	public UpdateAction getUndoAction() {
		return undoAction;
	}

	public UpdateAction getRedoAction() {
		return redoAction;
	}

	public UpdateAction getCutAction() {
		return cutAction;
	}

	public UpdateAction getCopyAction() {
		return copyAction;
	}

	public UpdateAction getPasteAction() {
		return pasteAction;
	}

	private class UndoAction extends UpdateAction {
		public UndoAction() {
            this.setMenuKeySet(KeySetUtils.UNDO);
            this.setName(getMenuKeySet().getMenuKeySetName());
            this.setMnemonic(getMenuKeySet().getMnemonic());
	        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/undo.png"));
            this.setAccelerator(getMenuKeySet().getKeyStroke());
		}

		public void actionPerformed(ActionEvent evt) {
			if(undoManager.canUndo()) {
				undoManager.undo();
				requestFocus();
			}
		}
		
		@Override
		public boolean isEnabled() {
			return undoManager.canUndo();
		}
	}

	class RedoAction extends UpdateAction {
		public RedoAction() {
            this.setMenuKeySet(KeySetUtils.REDO);
            this.setName(getMenuKeySet().getMenuKeySetName());
            this.setMnemonic(getMenuKeySet().getMnemonic());
	        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/redo.png"));
            this.setAccelerator(getMenuKeySet().getKeyStroke());
		}

		public void actionPerformed(ActionEvent evt) {		
			if(undoManager.canRedo()) {			
				undoManager.redo();
				requestFocus();
			}
		}
		
		@Override
		public boolean isEnabled() {
			return undoManager.canRedo();
		}
	}
	
	private class CutAction extends UpdateAction {
		public CutAction() {
	        setName(com.fr.design.i18n.Toolkit.i18nText("M_Edit-Cut"));
	        setMnemonic('T');
	        setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/cut.png"));
	        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, DEFAULT_MODIFIER));
		}
		
		public void actionPerformed(ActionEvent evt) {
			cut();
			requestFocus();
		}
		
		@Override
		public boolean isEnabled() {
			return UIAdvancedTextPane.this.getSelectedText() == null;
		}
	}

	private class CopyAction extends UpdateAction {
		public CopyAction() {
	        this.setName(com.fr.design.i18n.Toolkit.i18nText("M_Edit-Copy"));
	        this.setMnemonic('C');
	        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/copy.png"));
	        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, DEFAULT_MODIFIER));
		}
		
		public void actionPerformed(ActionEvent evt) {
			copy();
			requestFocus();
		}
		
		@Override
		public boolean isEnabled() {
			return UIAdvancedTextPane.this.getSelectedText() == null;
		}
	}

	private class PasteAction extends UpdateAction {
		public PasteAction() {
	        this.setName(com.fr.design.i18n.Toolkit.i18nText("M_Edit-Paste"));
	        this.setMnemonic('P');
	        this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_edit/paste.png"));
	        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, DEFAULT_MODIFIER));
		}
		
		public void actionPerformed(ActionEvent evt) {
			paste();
			requestFocus();
		}
		
		@Override
		public boolean isEnabled() {
			return true;
		}
	}	
}