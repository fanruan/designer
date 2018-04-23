/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.headerfooter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.base.headerfooter.DateHFElement;
import com.fr.base.headerfooter.FormulaHFElement;
import com.fr.base.headerfooter.HFElement;
import com.fr.base.headerfooter.ImageHFElement;
import com.fr.base.headerfooter.NewLineHFElement;
import com.fr.base.headerfooter.NumberOfPageHFElement;
import com.fr.base.headerfooter.PageNumberHFElement;
import com.fr.base.headerfooter.TextHFElement;
import com.fr.base.headerfooter.TimeHFElement;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * The basic HF edit component.
 */
public class HFComponent extends UILabel implements MoveActionListener {

    private HFElement hfElement;

    private ActionListener moveLeftActionListener = null;
    private ActionListener moveRightActionListener = null;
    private ActionListener deleteActionListener = null;
    private ChangeListener contentChangeListener = null;

    //popup menu
    private JPopupMenu popupMenu;

    /**
     * Constructor to set border.
     */
    public HFComponent(HFElement hfElement) {
        this.setBorder(BorderFactory.createLineBorder(Color.gray));
        this.setHorizontalAlignment(SwingConstants.CENTER);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(this.editMouseListener);

        //popup menu
        popupMenu = new JPopupMenu();

        UIMenuItem editMenuItem = new UIMenuItem(Inter.getLocText("Edit") + "...");
        editMenuItem.setMnemonic('E');
        popupMenu.add(editMenuItem);

        editMenuItem.addMouseListener(this.editMouseListener);

        popupMenu.add(new JSeparator());
        
        menuItemAction("HF-Move_Left",'L');
        
        menuItemAction("HF-Move_Right",'R');

        popupMenu.add(new JSeparator());
        
        menuItemAction("Delete",'D');

        this.setHFElement(hfElement);
    }

    public void menuItemAction(String s,final char o){
        UIMenuItem menuItem = new UIMenuItem(Inter.getLocText(s));
        menuItem.setMnemonic(o);
        popupMenu.add(menuItem);

		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				switch(o){
	  
				case'D':doDelete();
				break;
  
				case'R':doMoveRight();
				break;
  
				case'L':doMoveLeft();
				break;
				}
			}
		});
    }
    
    /**
     * Return HFElement.
     */
    public HFElement getHFElement() {
        return this.hfElement;
    }

    /**
     * Set HFElement.
     */
    public void setHFElement(HFElement hfElement) {
        this.hfElement = hfElement;

        //ajust icon
        this.setIcon(getHFElementIcon(hfElement));
        this.setToolTipText(getHFELementText(hfElement));
    }

    /**
     * Return moveLeftActionListener
     */
    public ActionListener getMoveLeftActionListener() {
        return this.moveLeftActionListener;
    }

    /**
     * Set moveLeftActionListener
     */
    public void setMoveLeftActionListener(ActionListener moveLeftActionListener) {
        this.moveLeftActionListener = moveLeftActionListener;
    }

    /**
     * Return moveRightActionListener
     */
    public ActionListener getMoveRightActionListener() {
        return this.moveRightActionListener;
    }

    /**
     * Set moveRightActionListener
     */
    public void setMoveRightActionListener(ActionListener moveRightActionListener) {
        this.moveRightActionListener = moveRightActionListener;
    }

    /**
     * Return deleteActionListener.
     */
    public ActionListener getDeleteActionListener() {
        return this.deleteActionListener;
    }

    /**
     * Set deleteActionListener.
     */
    public void setDeleteActionListener(ActionListener deleteActionListener) {
        this.deleteActionListener = deleteActionListener;
    }

    public ChangeListener getContentChangeListener() {
        return contentChangeListener;
    }

    public void setContentChangeListener(ChangeListener contentChangeListener) {
        this.contentChangeListener = contentChangeListener;
    }

    /**
     * Return preferredsize.
     */
    @Override
	public Dimension getPreferredSize() {
        return new Dimension(24, 24);
    }

    /**
     * Edit mouse listner.
     */
    private MouseListener editMouseListener = new MouseAdapter() {
        @Override
		public void mouseReleased(MouseEvent evt) {
            if (evt.isPopupTrigger()) {//donot suport right mouse action.
                GUICoreUtils.showPopupMenu(popupMenu, HFComponent.this,
                        evt.getX(), evt.getY());
                return;
            }

            popupHFAttributesEditDialog();
        }
    };

    /**
     * Popup edit dialog
     */
    private void popupHFAttributesEditDialog() {
        final HFAttributesEditDialog hfAttributesEditDialog = new HFAttributesEditDialog();
        hfAttributesEditDialog.populate(hfElement);
        hfAttributesEditDialog.addMoveActionListener(this);
        hfAttributesEditDialog.showWindow(SwingUtilities.getWindowAncestor(HFComponent.this), new DialogActionAdapter(){
        	@Override
			public void doOk(){
               hfAttributesEditDialog.update();
               contentChanged();
        	}
        }).setVisible(true);
    }

    /**
     * Move left
     */
    public void doMoveLeft() {
        if (moveLeftActionListener != null) {
            ActionEvent deleteEvt = new ActionEvent(HFComponent.this, 100, "Move Left");
            moveLeftActionListener.actionPerformed(deleteEvt);
        }
    }

    /**
     * Move right
     */
    public void doMoveRight() {
        if (moveRightActionListener != null) {
            ActionEvent deleteEvt = new ActionEvent(HFComponent.this, 100, "Move Right");
            moveRightActionListener.actionPerformed(deleteEvt);
        }
    }

    /**
     * Delete
     */
    public void doDelete() {
        if (deleteActionListener != null) {
            ActionEvent deleteEvt = new ActionEvent(HFComponent.this, 100, "Delete");
            deleteActionListener.actionPerformed(deleteEvt);
        }
    }

    /**
     * DataChanged.
     */
    private void contentChanged() {
        if (this.contentChangeListener != null) {
            ChangeEvent changeEvent = new ChangeEvent(HFComponent.this);
            contentChangeListener.stateChanged(changeEvent);
        }
    }

    /**
     * Gets the tooltip corresponding the HFElement.
     */
    public static String getHFELementText(HFElement hfElement) {
        if (hfElement.getClass().equals(TextHFElement.class)) {
            return Inter.getLocText("Text");
        } else if (hfElement.getClass().equals(FormulaHFElement.class)) {
            return Inter.getLocText("Formula");
        } else if (hfElement.getClass().equals(PageNumberHFElement.class)) {
            return Inter.getLocText("HF-Page_Number");
        } else if (hfElement.getClass().equals(NumberOfPageHFElement.class)) {
            return Inter.getLocText("HF-Number_of_Page");
        } else if (hfElement.getClass().equals(DateHFElement.class)) {
            return Inter.getLocText("Date");
        } else if (hfElement.getClass().equals(TimeHFElement.class)) {
            return Inter.getLocText("Time");
        } else if (hfElement.getClass().equals(ImageHFElement.class)) {
            return Inter.getLocText("Image");
        } else if (hfElement.getClass().equals(NewLineHFElement.class)) {
            return Inter.getLocText("HF-New_Line");
        }

        return Inter.getLocText("HF-Undefined");
    }

    /**
     * Gets the icon corresponding the HFElement.
     */
    public static Icon getHFElementIcon(HFElement hfElement) {
        if (hfElement.getClass().equals(TextHFElement.class)) {
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/text.png");
        } else if (hfElement.getClass().equals(FormulaHFElement.class)) {
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/formula.png");
        } else if (hfElement.getClass().equals(PageNumberHFElement.class)) {
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/page.png");
        } else if (hfElement.getClass().equals(NumberOfPageHFElement.class)) {
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/pages.png");
        } else if (hfElement.getClass().equals(DateHFElement.class)) {
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/date.png");
        } else if (hfElement.getClass().equals(TimeHFElement.class)) {
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/time.png");
        } else if (hfElement.getClass().equals(ImageHFElement.class)) {
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/image.png");
        } else if (hfElement.getClass().equals(NewLineHFElement.class)) {
            return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/newLine.png");
        }

        return BaseUtils.readIcon("/com/fr/base/images/dialog/headerfooter/undefined.png");
    }
}