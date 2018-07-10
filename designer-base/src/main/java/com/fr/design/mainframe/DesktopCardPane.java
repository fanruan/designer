/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import java.awt.*;


import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

/**
 * Created by IntelliJ IDEA. User : Richer Version: 6.5.5 Date : 11-7-21 Time :
 * 下午5:09
 */
public class DesktopCardPane extends BasicPane implements TargetModifiedListener {

	private static final long serialVersionUID = 1L;
	private JTemplate<?, ?> component;

	protected DesktopCardPane() {
		setLayout(new BorderLayout());
	}

	protected void showJTemplate(final JTemplate<?, ?> jt) {
		DesignerFrameFileDealerPane.getInstance().setCurrentEditingTemplate(jt);
        if(component != null) {
            remove(component);
        }
        add(component = jt, BorderLayout.CENTER);
        validate();
        repaint();
        revalidate();
        component.requestGridFocus();
	}

	protected JTemplate<?, ?> getSelectedJTemplate() {
		return component;
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Desktop");
	}

	@Override
	public void targetModified(TargetModifiedEvent e) {
	}
}