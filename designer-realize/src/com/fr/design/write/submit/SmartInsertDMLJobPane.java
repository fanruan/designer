package com.fr.design.write.submit;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.write.submit.SmartInsertDBManipulationPane;
import com.fr.write.DMLConfigJob;

import java.awt.*;

public  class SmartInsertDMLJobPane extends BasicBeanPane<DMLConfigJob> {
	private SmartInsertDBManipulationPane dbPane;

	public SmartInsertDMLJobPane(ElementCasePane ePane) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		dbPane = new SmartInsertDBManipulationPane(ePane);
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