package com.fr.design.style;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.fr.base.Utils;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

public class FontFamilyPane extends JPanel {
    private UITextField familyField;
    private JList familyList;
    
    public FontFamilyPane() {
    	this.init();
    }
    
    private void init() {
    	this.setLayout(FRGUIPaneFactory.createBorderLayout());
    	// 名字
        familyField = new UITextField();
        familyField.setEditable(false);
        
        familyList = new JList(Utils.getAvailableFontFamilyNames4Report());
        familyList.setVisibleRowCount(4);
        
        familyList.addListSelectionListener(listener);
        
        JPanel familyPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

//        familyPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        familyPane.add(FRFontPane.createTextFieldListPane("", familyField, familyList), BorderLayout.CENTER);
        familyPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FRFont-Family"),null));
        
        this.add(familyPane);
    }
    
    ListSelectionListener listener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			 Object source = e.getSource();
			if (source == getFamilyList()) {
                String family = (String) getFamilyList().getSelectedValue();
                if (family != null) getFamilyField().setText(family);
            }
		}
	};
    
    public UITextField getFamilyField() {
    	return this.familyField;
    }
    
    public JList getFamilyList() {
    	return this.familyList;
    }
    
    public String getText() {
    	return this.familyField.getText();
    }
    
    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
    	familyList.addListSelectionListener(listSelectionListener);
    }
    
    public void populate(FRFont frFont) {
        familyList.setSelectedValue(frFont.getName(), true);
        familyField.setText(frFont.getName());
    }
    
    public void update(FRFont font) {
    	font.applyName(getText());
    }
}