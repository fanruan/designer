package com.fr.design.dialog;

import com.fr.common.annotations.Open;

@Open
public abstract class DialogActionAdapter implements DialogActionListener {
	@Override
	public void doOk() {}
	
	@Override
	public void doCancel() {}
}