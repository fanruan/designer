package com.fr.design.actions;


import com.fr.design.designer.TargetComponent;

public abstract class TemplateComponentAction<T extends TargetComponent> extends UndoableAction implements TemplateComponentActionInterface<T> {
	private T t;
	protected TemplateComponentAction(T t) {
		this.t = t;
	}
	
	protected void setEditingComponent(T t) {
		this.t = t;
	}
	
	@Override
	public T getEditingComponent() {
		return t;
	}
	
	@Override
	public void prepare4Undo() {
		this.getEditingComponent().fireTargetModified();
        T component = getEditingComponent();
        if (component == null) {
            return;
        }

        component.requestFocus();
	}

    /**
     * update enable
     * TODO ALEX_SEP 这个方法的名字只是简单的叫update,太不明了了
     */
    @Override
    public void update() {
    	this.setEnabled(this.getEditingComponent() != null);
    }
}