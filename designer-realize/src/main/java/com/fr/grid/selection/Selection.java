package com.fr.grid.selection;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.cell.clipboard.CellElementsClip;
import com.fr.design.cell.clipboard.ElementsTransferable;
import com.fr.design.cell.clipboard.FloatElementsClip;
import com.fr.design.fun.RightSelectionHandlerProvider;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.selection.SelectableElement;
import com.fr.report.elementcase.TemplateElementCase;
import com.fr.stable.ColumnRow;
import com.fr.stable.FCloneable;

import javax.swing.JPopupMenu;
import java.io.Serializable;
import java.util.Set;

/*
 * TODO ALEX_SEP Selection是跟ElementCasePane绑定的,能不能把ElementCasePane保存在Selection里面呢?
 * 
 * 这样,与Selection相关的几个主要函数也就不用传ElementCasePane了
 * 
 * 但是,不知道持久化会不会有问题(咦?Selection在哪里会需要持久化? redo & undo?)
 */
public abstract class Selection implements FCloneable, Serializable , SelectableElement {
	public abstract boolean isSelectedOneCell(ElementCasePane ePane);

	public abstract void asTransferable(ElementsTransferable transferable, ElementCasePane ePane);

	public boolean pasteFloatElementClip(FloatElementsClip feClip, ElementCasePane ePane) {
		FloatElementsClip floatElementClip;
		try {
			floatElementClip = (FloatElementsClip) (feClip).clone();
		} catch (CloneNotSupportedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
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

	public abstract boolean canMergeCells(ElementCasePane ePane);

	public abstract boolean mergeCells(ElementCasePane ePane);

	public abstract boolean canUnMergeCells(ElementCasePane ePane);

	public abstract boolean unMergeCells(ElementCasePane ePane);

	public abstract JPopupMenu createPopupMenu(ElementCasePane ePane);

	/**
	 * 添加插件菜单(增删改都可以)
	 * @param ePane
	 * @param popupMenu
	 */
	public void addExtraMenu(ElementCasePane ePane, UIPopupMenu popupMenu) {
		Set<RightSelectionHandlerProvider> selectionHandlerProviders = ExtraDesignClassManager.getInstance().getArray(RightSelectionHandlerProvider.XML_TAG);
		for (RightSelectionHandlerProvider handler : selectionHandlerProviders) {
			if (handler.accept(this)) {
				handler.dmlMenu(ePane, popupMenu);
			}
		}
	}

	public abstract boolean clear(ElementCasePane.Clear type, ElementCasePane ePane);

	public abstract int[] getSelectedRows();

	public abstract int[] getSelectedColumns();

	public abstract void moveLeft(ElementCasePane ePane);

	public abstract void moveRight(ElementCasePane ePane);

	public abstract void moveUp(ElementCasePane ePane);

	public abstract void moveDown(ElementCasePane ePane);

	public abstract boolean triggerDeleteAction(ElementCasePane ePane);

	public abstract boolean containsColumnRow(ColumnRow cr);

	public abstract void populatePropertyPane(ElementCasePane ePane);

	public abstract void populateWidgetPropertyPane(ElementCasePane ePane);
	

	@Override
	public Selection clone() throws CloneNotSupportedException {
		return (Selection)super.clone();
	}

}