package com.fr.design.mainframe.cell.settingpane;

import com.fr.design.mainframe.AbstractAttrPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.CellSelection;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.elementcase.TemplateElementCase;

/**
 * 单元格属性表的每个tab对应的pane，已经实现了滚动条功能，如果页面装不下，会自动出来
 * 几点：子类不需要写构造函数了，所有的组件都放在createContentPane()方法生成的pane里面,它不需要考虑border。
 *
 * @author zhou
 * @since 2012-5-11下午4:02:18
 */
public abstract class AbstractCellAttrPane extends AbstractAttrPane {
	protected TemplateCellElement cellElement;
	protected ElementCasePane elementCasePane;
	protected CellSelection cs;

	protected abstract void populateBean();

	public abstract void updateBeans();

	public abstract void updateBean(TemplateCellElement cellElement);

	public void populateBean(TemplateCellElement cellElement, ElementCasePane epane) {
		if (epane == null || cellElement == null) {
			return;
		}
		removeAttributeChangeListener();
		this.cellElement = cellElement;
		elementCasePane = epane;
		if (elementCasePane.getSelection() instanceof FloatSelection) {
			return;
		}
		//august：这边必须保存一个CellSelection的镜像，因为单元格属性的某些操作可能使得CellSelection发生变化，那么elementCasePane.getSelection()获取的就不是准确的了
		cs = (CellSelection) elementCasePane.getSelection();
		populateBean();
	}

	/**
	 * 分成两个方法的意义在于，这个面板如果是个对话框，那么可以传cellElement进来update， 方便重复使用面板 为了对话框做准备
	 */
	public void updateBean() {
		updateBean(this.cellElement);
		TemplateElementCase elementCase = elementCasePane.getEditingElementCase();
		elementCase.addCellElement(cellElement);
	}
}