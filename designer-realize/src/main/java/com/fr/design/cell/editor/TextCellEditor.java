/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.design.gui.itextfield.EditTextField;
import com.fr.grid.Grid;
import com.fr.quickeditor.cellquick.CellStringQuickEditor;
import com.fr.report.cell.TemplateCellElement;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * CellEditor used to edit text object.
 */
public class TextCellEditor extends AbstractCellEditor {
    private Grid grid;

    protected EditTextField textField; //text field.
    //the old value of text field.
    protected String oldValue = "";

    public Grid getGrid() {
        return grid;
    }

    /**
     * Constructor.
     */
    public TextCellEditor() {
        textField = new EditTextField();
        textField.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        this.textField.addKeyListener(textKeyListener);
        this.textField.addFocusListener(new FocusAdapter() {
            @Override
			public void focusLost(FocusEvent evt) {
                stopCellEditing();
            }
        });
        this.textField.getDocument().addDocumentListener(documentlistener);
        //兼容JDK1.4
        this.textField.setFocusTraversalKeysEnabled(false);
    }
    DocumentListener documentlistener = new DocumentListener() {
		
		@Override
		public void removeUpdate(DocumentEvent e) {
			textchanged();
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			textchanged();
		}
		
		@Override
		public void changedUpdate(DocumentEvent e) {
			textchanged();
		}
	};
    /**
     * Gets the max length.
     * @return the max length
     */
    public int getMaxLength() {
        return this.textField.getMaxLength();
    }

    private void textchanged() {
    	CellStringQuickEditor quickeditor = (CellStringQuickEditor)grid.getElementCasePane().getCurrentEditor();
    	quickeditor.showText(textField.getText());
	}

	/**
     * Set the max length.
     * @param maxLength the new max length.
     */
    public void setMaxLength(int maxLength) {
        this.textField.setMaxLength(maxLength);
    }

    /**
     * Gets the value of the CellEditor.
     */
    public Object getCellEditorValue() throws Exception {
        return this.textField.getText();
    }

    /**
     * Sets an initial <code>cellElement</code> for the editor.  This will cause
     * the editor to <code>stopCellEditing</code> and lose any partially
     * edited value if the editor is editing when this method is called. <p>
     * <p/>
     * Returns the component that should be added to the client's
     * <code>Component</code> hierarchy.  Once installed in the client's
     * hierarchy this component will then be able to draw and receive
     * user input.
     *
     * @param grid        the <code>Grid</code> that is asking the
     *                    editor to edit; can be <code>null</code>
     * @param cellElement the value of the cell to be edited; it is
     *                    up to the specific editor to interpret
     *                    and draw the value.
     */
    public Component getCellEditorComponent(Grid grid, TemplateCellElement cellElement, int resolution) {
        this.grid = grid;

        Object value = null;
        if(cellElement != null) {
            value = cellElement.getValue();
        }
        if (value == null) {
            value = "";
        }
        if (!(value instanceof String) && !(value instanceof Number)) {
            value = "";
        }

        this.oldValue = Utils.objectToString(value);
        this.textField.setText(oldValue);
        //peter:只读方式获得Style.
        Style style = null;
        if(cellElement != null) {
            style = cellElement.getStyle();
        }
        this.ajustTextStyle(grid, style, value, resolution);

        return this.textField;
    }

    public String getOldValue () {
        return oldValue;
    }

    protected void ajustTextStyle(Grid grid, Style style, Object value, int resolution) {
    	GUICoreUtils.adjustStyle(style, textField, resolution, value);
    }

    private void ajustTextFieldSize() {
        Dimension textSize = textField.getSize();
        Dimension textPrefSize = textField.getPreferredSize();
        this.textField.setSize((int) Math.max(textSize.getWidth(), textPrefSize.getWidth()), (int) textSize.getHeight());
    }

    private KeyListener textKeyListener = new KeyListener() {
        public void keyTyped(KeyEvent e) {
            ajustTextFieldSize();
        }

        public void keyPressed(KeyEvent evt) {
            int code = evt.getKeyCode();

            if (code == KeyEvent.VK_ESCAPE) {
                textField.setText(oldValue);
                evt.consume();
            } else if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_TAB ||
                    code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                //将事件传递给Grid控件来实现, Enter键向下移动 和 Tab键向右移动.
                grid.requestFocus();
                grid.dispatchEvent(evt);
            }
        }

        public void keyReleased(KeyEvent evt) {
        }
    };
}