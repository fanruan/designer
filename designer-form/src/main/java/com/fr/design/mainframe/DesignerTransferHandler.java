package com.fr.design.mainframe;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.models.AddingModel;

public class DesignerTransferHandler extends TransferHandler {

	private FormDesigner designer;
	private AddingModel addingModel;

	public DesignerTransferHandler(FormDesigner designer, AddingModel addingModel) {
		super("rootComponent");
		this.designer = designer;
		this.addingModel = addingModel;
	}

	protected void exportDone(JComponent source, Transferable data, int action) {
		if (!addingModel.isCreatorAdded()) {
			designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_CUTED);
		}
	}
}