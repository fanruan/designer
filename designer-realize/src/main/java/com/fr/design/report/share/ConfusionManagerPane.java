/**
 * 
 */
package com.fr.design.report.share;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fr.data.TableDataSource;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.mainframe.JTemplate;

import com.fr.general.NameObject;

/**
 * @author neil
 *
 * @date: 2015-3-9-上午9:10:20
 */
public class ConfusionManagerPane extends JListControlPane {

	/**
	 * 获取当前面板的编辑对象
	 * 
	 * @return 数据混淆编辑对象
	 * 
	 */
	public NameableCreator[] createNameableCreators() {
		NameableCreator local = new NameObjectCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_DS-TableData"),
				"/com/fr/design/images/data/dock/serverdatatable.png", ConfusionInfo.class,
				ConfusionTableDataPane.class);

		return new NameableCreator[] { local };
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Data-Confusion");
	}

	/**
	 * 获取模板中内置数据集, 并展示混淆页面, 如果没有内置数据集, 则返回false
	 * 
	 * @return 是否存在内置数据集
	 * 
	 */
	public boolean populateTabledataManager() {
		List<NameObject> nameList = initNameObjectList();
		if (nameList.isEmpty()) {
			return false;
		}

		this.populate(nameList.toArray(new NameObject[nameList.size()]));
		return true;
	}

	private List<NameObject> initNameObjectList() {
		JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
		TableDataSource workbook = jt.getTarget();
		Iterator<String> it = workbook.getTableDataNameIterator();
		List<NameObject> nameList = new ArrayList<NameObject>();
		while (it.hasNext()) {
			String tabledataName = it.next();
			EmbeddedTableData tabledata = (EmbeddedTableData) workbook.getTableData(tabledataName);
			ConfusionInfo info = new ConfusionInfo(tabledata, tabledataName);
			nameList.add(new NameObject(tabledataName, info));
		}

		return nameList;
	}

	@Override
	protected ShortCut4JControlPane[] createShortcuts() {
		return new ShortCut4JControlPane[] {
				shortCutFactory.moveUpItemShortCut(),
				shortCutFactory.moveDownItemShortCut(),
				shortCutFactory.sortItemShortCut()
		};
	}

}
