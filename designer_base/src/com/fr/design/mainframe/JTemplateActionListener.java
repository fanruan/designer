package com.fr.design.mainframe;

import java.util.EventListener;

public interface JTemplateActionListener extends EventListener {

	public void templateOpened(JTemplate<?, ?> jt);

	public void templateSaved(JTemplate<?, ?> jt);

	public void templateClosed(JTemplate<?, ?> jt);
}