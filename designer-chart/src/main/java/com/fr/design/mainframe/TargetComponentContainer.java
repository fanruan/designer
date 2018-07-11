package com.fr.design.mainframe;

import com.fr.design.designer.TargetComponent;

public class TargetComponentContainer {
	private  TargetComponent<?> ePane;

	public TargetComponent<?> getEPane() {
		return ePane;
	}

	public void setEPane(TargetComponent<?> ePane) {
		this.ePane = ePane;
	}
}