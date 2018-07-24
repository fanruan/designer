package com.fr.design.write.submit;


import com.fr.data.ClassSubmitJob;
import com.fr.data.SubmitJob;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ObjectJControlPane;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.write.DMLConfigJob;
import com.fr.write.NameSubmitJob;

public class SubmitJobListPane extends ObjectJControlPane {
	
	public SubmitJobListPane() {
		this(null);
	}

	public SubmitJobListPane(Object object) {
		super(object);
	}

	/**
	 * 选项
	 * @return
	 */
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] {
			new NameObjectCreator(
					com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Write_Submit_Event"),
					"/com/fr/web/images/reportlet.png",
					DMLConfigJob.class,
					DMLJobPane.class),
			new NameObjectCreator(
					com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Write_Custom_Event"),
					"/com/fr/web/images/reportlet.png",
					ClassSubmitJob.class,
					CustomSubmitJobPane.class)
		};
	}

	/**
	 *
	 * @return
	 */
	public NameSubmitJob[] updateDBManipulation() {
		Nameable[] res = this.update();
		NameSubmitJob[] jobs = new NameSubmitJob[res.length];
		for (int i = 0; i < res.length; i++) {
			NameObject no = (NameObject)res[i];
			jobs[i] = new NameSubmitJob(no.getName(), (SubmitJob)no.getObject());
		}
		
		return jobs;
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Set_Submit_Event");
	}
}