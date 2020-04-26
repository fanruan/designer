/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.parameter;

import com.fr.base.BaseFormula;
import com.fr.base.StoreProcedureParameter;
import com.fr.base.Utils;
import com.fr.data.impl.storeproc.StoreProcedureConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.ValueEditorPane;
import com.fr.design.editor.ValueEditorPaneFactory;
import com.fr.design.editor.editor.BooleanEditor;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.editor.editor.DoubleEditor;
import com.fr.design.editor.editor.Editor;
import com.fr.design.editor.editor.FloatEditor;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.editor.editor.TextEditor;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The dialog used to input parameter.
 * @editor zhou
 * @since 2012-3-26上午11:09:45
 */
public class ParameterInputPane extends BasicPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// alex:保存编辑器对应的参数的名字
	private java.util.Map<ValueEditorPane,String> editorNameMap; // Map<Editor, Name>

	private boolean allowBlank = true;
	
	/**
     * Constructor.
     */
    public ParameterInputPane(ParameterProvider[] parameters) {
        this.initComponents(parameters);
    }

	public ParameterInputPane(ParameterProvider[] parameters, boolean allowBlank) {
		this.allowBlank = allowBlank;
		this.initComponents(parameters);
	}

    private void initComponents(ParameterProvider[] parameters) {
        this.setLayout(new BorderLayout(0, 4));

        //Content Pane.
        JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        this.add(new JScrollPane(contentPane), BorderLayout.CENTER);
        contentPane.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameters") + ":"));

        FlowTableLayoutHelper flowTableLayoutHelper = new FlowTableLayoutHelper();

        editorNameMap = new java.util.HashMap<ValueEditorPane,String>();
        
        //Parameter list.
        java.util.List<String> nameAddedList = new java.util.ArrayList<String>(); // alex:已经加到界面中去的参数名
        if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                ParameterProvider parameter = parameters[i];
                
                // alex:已经在界面中的参数,不加了
                if (nameAddedList.contains(parameter.getName())) {
                	continue;
				}
                if(parameter instanceof StoreProcedureParameter
						&& ((StoreProcedureParameter) parameter).getSchema() == StoreProcedureConstants.OUT) {
                	continue;
				}
                final Object pv = parameter.getValue();
                Editor[] editors = makeEditorByValue(pv);

                //set value.
                final ValueEditorPane textF = ValueEditorPaneFactory.createValueEditorPane(editors);
                textF.populate(pv);
                JPanel editPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
                editPane.add(textF, BorderLayout.CENTER);
                editPane.setPreferredSize(new Dimension(180, editPane.getPreferredSize().height));

                String parameterDisplayName = parameter.getName();
                if(StringUtils.isNotBlank(parameter.getName())) {
                	parameterDisplayName = parameter.getName();
                }
                contentPane.add(flowTableLayoutHelper.createLabelFlowPane(parameterDisplayName + ":", editPane));

                //add editor to parameter hashtable.
				textF.getCurrentEditor().addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().fireTargetModified();
					}
				});
                this.editorNameMap.put(textF, parameter.getName());
                nameAddedList.add(parameter.getName());
            }
        }

        flowTableLayoutHelper.adjustLabelWidth();
    }

	private Editor[] makeEditorByValue(Object pv) {
		Editor[] editors = {null};
		if (pv instanceof Integer) {
			editors[0] = new IntegerEditor();
		} else if (pv instanceof Double || pv instanceof Float) {
			editors[0] = new DoubleEditor();
		} else if (pv instanceof Float) {
			editors[0] = new FloatEditor();
		} else if (pv instanceof Date) {
			editors[0] = new DateEditor(true, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Date"));
		} else if (pv instanceof Boolean) {
			editors[0] = new BooleanEditor();
		} else if (pv instanceof BaseFormula) {
			editors = ValueEditorPaneFactory.basicEditors();
		} else {
			editors[0] = new TextEditor();
		}
		return editors;
	}
    
    @Override
    protected String title4PopupWindow() {
    	return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameters");
    }

    /**
     * Update
     */
    public Map<String,Object> update() {
    	
    	java.util.Map<String,Object> nameValueMap = new java.util.HashMap<String,Object>();
    	
    	Iterator<Entry<ValueEditorPane, String>> entryIt = this.editorNameMap.entrySet().iterator();
    	while(entryIt.hasNext()) {
    		java.util.Map.Entry<ValueEditorPane, String> entry = entryIt.next();
    		ValueEditorPane editor = entry.getKey();
    		String parameterName = entry.getValue();
    		
    		Object editorStringValue = editor.update();
    		nameValueMap.put(parameterName, editorStringValue);
    	}
    	
    	return nameValueMap;
    }

	public void checkValid() throws Exception {
		if (!allowBlank) {
			boolean valid = true;
			String error = "";
			Iterator<Entry<ValueEditorPane, String>> entryIt = this.editorNameMap.entrySet().iterator();
			while (entryIt.hasNext()) {
				java.util.Map.Entry<ValueEditorPane, String> entry = entryIt.next();
				ValueEditorPane editor = entry.getKey();
				String parameterName = entry.getValue();
				Object editorStringValue = editor.update();
				if (editorStringValue == null || StringUtils.isEmpty(Utils.objectToString(editorStringValue))) {
					valid = false;
					error += parameterName + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Not_Null_Des") + "\n";
				}
			}
			if (!valid) {
				throw new Exception(error);
			}
		}
	}
    
    /**
     * The class help to flowlayout components
     */
    private static class FlowTableLayoutHelper {
    	private List<UILabel> labelList = new ArrayList<UILabel>();

    	public FlowTableLayoutHelper() {
    	}

    	public JPanel createLabelFlowPane(String text, JComponent comp) {
    		JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

    		UILabel textLabel = new UILabel(text);
    		centerPane.add(textLabel);
    		textLabel.setHorizontalAlignment(SwingConstants.LEFT);

    		this.labelList.add(textLabel);
    		centerPane.add(comp);

    		return centerPane;
    	}

    	public void adjustLabelWidth() {
    		int maxWidth = 0;

    		for (int i = 0; i < labelList.size(); i++) {
    			maxWidth = Math.max(maxWidth, labelList.get(i).getPreferredSize().width);
    		}

    		for (int i = 0; i < labelList.size(); i++) {
    			UILabel label = labelList.get(i);

    			Dimension labelDim = new Dimension(maxWidth, label.getPreferredSize().height);

    			label.setPreferredSize(labelDim);
    			label.setSize(labelDim);
    			label.setMinimumSize(labelDim);
    		}
    	}
    }
}