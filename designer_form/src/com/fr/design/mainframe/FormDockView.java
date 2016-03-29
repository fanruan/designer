package com.fr.design.mainframe;


public abstract class FormDockView extends DockingView {
	
	private FormDesigner editor;
	
	public void setEditingFormDesigner(FormDesigner  editor) {
		this.editor = editor;
	}
    
    // TODO ALEX_SEP dockingView.enabled不知能否利用这个方法 & 暂时不实现
    public FormDesigner getEditingFormDesigner() {
    	return editor;
    }
    
}