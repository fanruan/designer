package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.BaseFormula;
import com.fr.base.StoreProcedureParameter;
import com.fr.data.impl.storeproc.StoreProcedureConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.CursorEditor;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;

import com.fr.general.NameObject;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;


public class StoreProcedureParameterPane extends BasicPane {

	private static String[] schemaName = new String[]{
		"IN", 	"OUT",	"INOUT"
	};

	private static NameObject[] nameAndValue = new NameObject[]{
		new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cursor"), StoreProcedureConstants.CURSOR),
		new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_String"), StoreProcedureConstants.VARCHAR),
		new NameObject(com.fr.design.i18n.Toolkit.i18nText("Integer"), StoreProcedureConstants.INTEGER),
		new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Double"), StoreProcedureConstants.DECIMAL),
		new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date"), StoreProcedureConstants.DATE),
		new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_Boolean"), StoreProcedureConstants.BOOLEAN),
		new NameObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula"), StoreProcedureConstants.FORMULA),
		new NameObject("IN",StoreProcedureConstants.IN),
		new NameObject("OUT",StoreProcedureConstants.OUT),
		new NameObject("INOUT",StoreProcedureConstants.INOUT)
	};
	
	private static HashMap<String, Integer> infoMap = new HashMap<String, Integer>();
	static {
		for(int i = 0; i < nameAndValue.length; i ++){
			infoMap.put(nameAndValue[i].getName(), (Integer) nameAndValue[i].getObject());
		}
	}
	
	private UITextField nameField;
	private UIComboBox schemaCombo;
	private ValueEditorPane valueEditPane;
	private JPanel valuePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
	
	public StoreProcedureParameterPane(){
		valueEditPane = ValueEditorPaneFactory.createStoreProcedValueEditorPane();
		this.initComponents();
	}
	
	public void checkValid() throws Exception{
		StoreProcedureParameter spp=this.update();
    	if(spp.getSchema()!= StoreProcedureConstants.OUT && spp.getType() == StoreProcedureConstants.CURSOR){
    		throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_In_And_Inout_Type_Not_As_Cursor"));
    	}
	}
	
	private void initComponents(){
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		schemaCombo = new UIComboBox();
	    initUIComboBox(schemaCombo, schemaName);
		JPanel namePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		namePane.add(new UILabel("     " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Name") + ":"), BorderLayout.WEST);
		nameField = new UITextField(10);
		namePane.add(nameField, BorderLayout.CENTER);
		namePane.add(new UILabel("     "), BorderLayout.EAST);
		valuePane.add(new UILabel("   " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_CellWrite_InsertRow_Default") + ":"), BorderLayout.WEST);
		valuePane.add(valueEditPane, BorderLayout.CENTER);
		valuePane.add(new UILabel("     "), BorderLayout.EAST);
		Component[][] components = {{null},
				{namePane},
				{addPane("Fine-Design_Basic_Model", 1, schemaCombo)},
				{valuePane},
				{null}
		};
		double p = TableLayout.PREFERRED;
		double[] rowSize = {p, p, p, p, p, p};
		double[] columnSize = {p};
		JPanel centerPane = TableLayoutHelper.createGapTableLayoutPane(
				components, rowSize, columnSize, 20, 10);
		this.add(centerPane, BorderLayout.CENTER);
		
	}
	
	private JPanel addPane(String s, int i, UIComboBox combo){		  
	      JPanel pane = FRGUIPaneFactory.createBorderLayout_S_Pane();   
	      pane.add(new UILabel("     " + com.fr.design.i18n.Toolkit.i18nText(s) + ":"), BorderLayout.WEST);
	      combo.setSelectedIndex(i);
	      pane.add(combo, BorderLayout.CENTER);  
	      pane.add(new UILabel("     "), BorderLayout.EAST);
	      
	      return pane;
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter");
	}
	
	public void populate(StoreProcedureParameter stpParameter) {
		if(stpParameter == null)
			return;
		this.nameField.setText(stpParameter.getName());
		String schema = getInfo4Name(stpParameter.getSchema());
		this.schemaCombo.setSelectedItem(schema);
		this.valueEditPane.populate(stpParameter.getValue());
		
	}
	
	public StoreProcedureParameter update(){
		StoreProcedureParameter p = new StoreProcedureParameter();
		p.setName(nameField.getText());
		Object value = valueEditPane.update();
		String type = "";
		if(value instanceof CursorEditor)
			type=com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cursor");
		else if(value instanceof String ){
			if(((String) value).length() > 0 && ((String) value).charAt(0) == '=')
				type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula");
			else
				type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_String");
		}else if(value instanceof Integer)
			type = com.fr.design.i18n.Toolkit.i18nText("Integer");
		else if(value instanceof Double)
			type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Double");
		else if(value instanceof Date)
			type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date");
		else if(value instanceof Boolean)
			type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_Boolean");
		else if(value instanceof BaseFormula)
			type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Formula");
		else 
			type = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_String");
		int typeVl = getInfo4Value(type);
		p.setType(typeVl);
		String schema = (String)schemaCombo.getSelectedItem();
		int schemaVl = getInfo4Value(schema);
		p.setSchema(schemaVl);
		p.setValue(value);
		
		return p;
	}
	
	private void initUIComboBox(UIComboBox combo, String[] p){
		for(int i = 0; i < p.length; i ++)
			combo.addItem(p[i]);
	}
	
	public static int getInfo4Value(String name){
		return infoMap.get(name);
	}
	
	public static String getInfo4Name(int value){
		java.util.Set<String> set = infoMap.keySet();
		for(String s : set){
			if(infoMap.get(s) == value)
				return s;
		}
		return null;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
}