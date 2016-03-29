package com.fr.grid.selection;

import java.io.Serializable;

import javax.swing.JPopupMenu;

import com.fr.base.FRContext;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.cell.clipboard.FloatElementsClip;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.design.selection.SelectableElement;
import com.fr.stable.ColumnRow;
import com.fr.stable.FCloneable;

/*
 * TODO ALEX_SEP Selection是跟ElementCasePane绑定的,能不能把ElementCasePane保存在Selection里面呢?
 * 
 * 这样,与Selection相关的几个主要函数也就不用传ElementCasePane了
 * 
 * 但是,不知道持久化会不会有问题(咦?Selection在哪里会需要持久化? redo & undo?)
 */
public abstract class Selection implements FCloneable, Serializable , SelectableElement {
	public abstract boolean isSelectedOneCell(ElementCasePane ePane);

	// ///////////////////////////////copy/////////////////////////////////
	public abstract void asTransferable(ElementsTransferable transferable, ElementCasePane ePane);

	// ///////////////////////////////paste////////////////////////////////
	public boolean pasteFloatElementClip(FloatElementsClip feClip, ElementCasePane ePane) {
		FloatElementsClip floatElementClip;
		try {
			floatElementClip = (FloatElementsClip) (feClip).clone();
		} catch (CloneNotSupportedException e) {
			FRContext.getLogger().error(e.getMessage(), e);
			return false;
		}

		TemplateElementCase ec = ePane.getEditingElementCase();

		FloatSelection fs = floatElementClip.pasteAt(ec);
		if (fs != null) {
			ePane.setSelection(fs);
		}

		return true;
	}

	public abstract boolean pasteCellElementsClip(CellElementsClip ceClip, ElementCasePane ePane);

	public abstract boolean pasteString(String str, ElementCasePane ePane);

	public abstract boolean pasteOtherType(Object ob, ElementCasePane ePane);

	// ///////////////////////////////merge////////////////////////////////
	public abstract boolean canMergeCells(ElementCasePane ePane);

	public abstract boolean mergeCells(ElementCasePane ePane);

	public abstract boolean canUnMergeCells(ElementCasePane ePane);

	public abstract boolean unMergeCells(ElementCasePane ePane);

	// ///////////////////////////////popup////////////////////////////////
	public abstract JPopupMenu createPopupMenu(ElementCasePane ePane);

	// ///////////////////////////////clear////////////////////////////////
	public abstract boolean clear(ElementCasePane.Clear type, ElementCasePane ePane);

	// ////////////////////////////////////////////////////////////////////
	public abstract int[] getSelectedRows();

	public abstract int[] getSelectedColumns();

	// //////////////////////////////move//////////////////////////////////
	public abstract void moveLeft(ElementCasePane ePane);

	public abstract void moveRight(ElementCasePane ePane);

	public abstract void moveUp(ElementCasePane ePane);

	public abstract void moveDown(ElementCasePane ePane);

	// //////////////////////////DeleteAction///////////////////////////////
	public abstract boolean triggerDeleteAction(ElementCasePane ePane);

	// //////////////////////////Just4CellSelection///////////////////////////////
	public abstract boolean containsColumnRow(ColumnRow cr);
	

	@Override
	public Selection clone() throws CloneNotSupportedException {
		return (Selection)super.clone();
	}

}