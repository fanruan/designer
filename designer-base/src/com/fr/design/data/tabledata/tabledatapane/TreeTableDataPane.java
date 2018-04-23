package com.fr.design.data.tabledata.tabledatapane;

import com.fr.design.data.datapane.TreeTableDataDictPane;
import com.fr.data.impl.RecursionTableData;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

import java.awt.*;

public class TreeTableDataPane extends AbstractTableDataPane<RecursionTableData>{
	
	protected TreeTableDataDictPane treeTableDataDictPane;
	
	public TreeTableDataPane() {
        this.treeTableDataDictPane = new TreeTableDataDictPane();
		initComponent();
	}

    public TreeTableDataPane(String treeName) {
        this.treeTableDataDictPane = new TreeTableDataDictPane(treeName);
        initComponent();
    }
	
	protected void initComponent() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.add(treeTableDataDictPane, BorderLayout.CENTER);
	}

	@Override
	public void populateBean(RecursionTableData ob) {
		treeTableDataDictPane.populate(ob);
	}

	@Override
	public RecursionTableData updateBean() {
		// TODO Auto-generated method stub
		return treeTableDataDictPane.update();
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Tree", "DS-TableData"});
	}
	
}