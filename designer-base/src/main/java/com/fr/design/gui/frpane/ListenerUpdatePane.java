package com.fr.design.gui.frpane;


import java.awt.BorderLayout;


import com.fr.design.beans.BasicBeanPane;
import com.fr.design.javascript.JavaScriptActionPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.event.Listener;

public abstract class ListenerUpdatePane extends BasicBeanPane<Listener> {
	private JavaScriptActionPane actionPane;
	
	private Listener editing;
	
	public ListenerUpdatePane() {
		this.initComponents();
	}
	
	protected void initComponents(){
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		this.add(actionPane = createJavaScriptActionPane(), BorderLayout.CENTER);
	}
	
	protected abstract JavaScriptActionPane createJavaScriptActionPane();
	
	protected abstract boolean supportCellAction();

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Event");
	}

    @Override
	public void populateBean(Listener ob) {
    	editing = ob;
    	actionPane.reset();
		actionPane.populateBean(ob.getAction());
	}

    @Override
	public Listener updateBean() {
		editing.setAction(actionPane.updateBean());
		
		return editing;
	}

	@Override
	public void checkValid() throws Exception{
		actionPane.checkValid();
	}
}