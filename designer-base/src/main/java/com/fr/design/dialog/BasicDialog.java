package com.fr.design.dialog;

import com.fr.common.annotations.Open;

import java.awt.*;

@Open
public abstract class BasicDialog extends UIDialog {
	public static final Dimension SMALL = new Dimension(340, 180);
	public static final Dimension MEDIUM = new Dimension(600, 400);
	public static final Dimension DEFAULT = new Dimension(660, 600);
	public static final Dimension LARGE = new Dimension(900, 600);
	public static final Dimension CHART = new Dimension(760, 560);
	public static final Dimension MAP_SIZE = new Dimension(760, 450);
	public static final Dimension UPDATE_ONLINE_SIZE = new Dimension(600,300);
	public static final Dimension TOOLBAR_SIZE = new Dimension(660, 327);

	public BasicDialog(Frame parent) {
		super(parent);
	}

	public BasicDialog(Dialog parent) {
		super(parent);
	}

	public BasicDialog(Frame parent, BasicPane pane) {
		this(parent, pane, true);
	}

	public BasicDialog(Dialog parent, BasicPane pane) {
		this(parent, pane, true);
	}


	public BasicDialog(Frame parent, BasicPane pane, boolean isNeedButtonPane) {
		super(parent, pane, isNeedButtonPane);
	}

	public BasicDialog(Dialog parent, BasicPane pane, boolean isNeedButtonPane) {
		super(parent, pane, isNeedButtonPane);
	}

	protected void setBasicDialogSize(Dimension d) {
		super.setSize(d.width, d.height);
	}

	protected void setBasicDialogSize(int w, int h) {
		super.setSize(w, h);
	}

	@Override
	public void setSize(Dimension d) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSize(int width, int height) {
		throw new UnsupportedOperationException();
	}


}