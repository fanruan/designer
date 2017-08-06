package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.form.ui.WriteAbleRepeatEditor;


public abstract class WritableRepeatEditorPane<E extends WriteAbleRepeatEditor> extends DirectWriteEditorDefinePane<E> {

	public WritableRepeatEditorPane(XCreator xCreator) {
		super(xCreator);
	}

	@Override
	protected void populateSubDirectWriteEditorBean(E e) {
		populateSubWritableRepeatEditorBean(e);
	}

	protected abstract void populateSubWritableRepeatEditorBean(E e);

	@Override
	protected E updateSubDirectWriteEditorBean() {
		return updateSubWritableRepeatEditorBean();
	}

	protected abstract E updateSubWritableRepeatEditorBean();
}