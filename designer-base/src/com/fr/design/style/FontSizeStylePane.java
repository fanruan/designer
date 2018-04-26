package com.fr.design.style;


import javax.swing.Box;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

public class FontSizeStylePane extends JPanel {

    private UINumberField sizeField;
    private JList sizeList;
    private UITextField styleField;
    private JList styleList;
    
    public FontSizeStylePane() {
    	this.init();
    }
    
    private void init() {
    	this.setLayout(FRGUIPaneFactory.createBorderLayout());
    	
    	JPanel fontVSizPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
        this.add(fontVSizPane);
        
//        fontVSizPane.setLayout(new BoxLayout(fontVSizPane, BoxLayout.X_AXIS));
        fontVSizPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"FRFont-Style", "FRFont-Size"}), null));
        
        String[] styles = {
            Inter.getLocText("FRFont-plain"),
            Inter.getLocText("FRFont-bold"),
            Inter.getLocText("FRFont-italic"),
            Inter.getLocText("FRFont-bolditalic")};

        styleField = new UITextField();
        styleField.setEditable(false);
        
        styleList = new JList(styles);
        styleList.setVisibleRowCount(4);
        
        JPanel stylePanel = FRFontPane.createTextFieldListPane("", styleField, styleList);
        fontVSizPane.add(stylePanel);

        fontVSizPane.add(Box.createHorizontalStrut(6));

        JPanel sizePanel = FRFontPane.createTextFieldListPane("", 	sizeField = new UINumberField(), sizeList = new JList(FRFontPane.Font_Sizes));
        sizeField.setMaxDecimalLength(1);
        
        sizeField.setEditable(false);
        
        sizeList.setVisibleRowCount(4);
      
        fontVSizPane.add(sizePanel);
        
        styleField.setText((String) styleList.getSelectedValue());
        
        sizeList.addListSelectionListener(listener);
        styleList.addListSelectionListener(listener);
        
    }
    
    ListSelectionListener listener = new ListSelectionListener() {
    	public void valueChanged(ListSelectionEvent evt) {
    		Object source = evt.getSource();
    		if (source == getSizeList()) {
    			Integer size = (Integer) getSizeList().getSelectedValue();
    			if (size != null) {
    				getSizeField().setValue(size.intValue());
    			}
    		} else if (source == getStyleList()) {
    			String style = (String) getStyleList().getSelectedValue();
    			if (style != null) getStyleField().setText(style);
    		}
    	}
    };
    
	public UINumberField getSizeField() {
		return sizeField;
	}
	
	public JList getStyleList() {
		return styleList;
	}
	
	public JList getSizeList() {
		return sizeList;
	}
	
	public UITextField getStyleField() {
		return styleField;
	}
	
	public void populate(FRFont frFont) {
		styleList.setSelectedIndex(frFont.getStyle());
		sizeList.setSelectedValue(new Integer(frFont.getSize()), true);
		sizeField.setValue(frFont.getSize2D());
	}
}