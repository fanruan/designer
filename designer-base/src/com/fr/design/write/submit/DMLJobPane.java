package com.fr.design.write.submit;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.write.DMLConfigJob;

import java.awt.*;

public class DMLJobPane extends BasicBeanPane<DMLConfigJob> {
	private DBManipulationPane dbPane;

	public DMLJobPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		dbPane = new DBManipulationPane(ValueEditorPaneFactory.extendedEditors());

		this.add(dbPane, BorderLayout.CENTER);
	}

	@Override
	protected String title4PopupWindow() {
		return "DB";
	}

	@Override
	public void populateBean(DMLConfigJob ob) {
		this.dbPane.populateBean(ob.getDBManipulation());
	}

	@Override
	public DMLConfigJob updateBean() {
		DMLConfigJob job = new DMLConfigJob();
		job.setDBManipulation(this.dbPane.updateBean());

		return job;
	}
}