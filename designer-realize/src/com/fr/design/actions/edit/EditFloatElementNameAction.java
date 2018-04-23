package com.fr.design.actions.edit;

import javax.swing.SwingUtilities;

import com.fr.design.actions.FloatSelectionAction;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.grid.selection.FloatSelection;
import com.fr.report.cell.FloatElement;
import com.fr.report.elementcase.ElementCase;

public class EditFloatElementNameAction extends FloatSelectionAction {
	
	public EditFloatElementNameAction(ElementCasePane t) {
		super(t);
		
		this.setName(Inter.getLocText(new String[]{"Set", "Float_Element_Name"}));
	}
	
	@Override
	protected boolean executeActionReturnUndoRecordNeededWithFloatSelection(
			FloatSelection fs) {
		final ElementCasePane reportPane = getEditingComponent();
        final ElementCase report = reportPane.getEditingElementCase();

        //p:获得最后一个选中的悬浮元素.
        final FloatElement selectedFloatElement = report.getFloatElement(fs.getSelectedFloatName());
        
        final NamePane pane = new NamePane();
        pane.populate(selectedFloatElement.getName());
        BasicDialog nameDialog = pane.showSmallWindow(SwingUtilities.getWindowAncestor(reportPane),new DialogActionAdapter() {
			@Override
			public void doOk() {
				String name = pane.update();
				if (report.getFloatElement(name) == null) {
					selectedFloatElement.setName(name);
				}
				reportPane.setSelection(new FloatSelection(name));
			}                
        });
        nameDialog.setVisible(true);
        
        return true;
	}
    
    class NamePane extends BasicPane{
    	private UITextField jtext;
    	public NamePane() {
    		jtext = new UITextField(15);
    		this.add(jtext);
    	}
    	
    	@Override
    	protected String title4PopupWindow() {
    		return Inter.getLocText(new String[]{"Set", "Float_Element_Name"});
    	}
    	
    	public void populate(String name) {
    		jtext.setText(name);
    	}
    	
    	public String update() {
    		return jtext.getText();
    	}
    }
}