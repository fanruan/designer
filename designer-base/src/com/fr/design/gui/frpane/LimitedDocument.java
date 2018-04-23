package com.fr.design.gui.frpane;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
  
public class LimitedDocument extends PlainDocument {
  
	private int maxLength = -1;
    private String allowCharAsString = null;
  
    public LimitedDocument() {
    	super();
    }
 
    public LimitedDocument(int maxLength, String str) {
    	super();
    	this.maxLength = maxLength;
    	allowCharAsString = str;
    }
 
    @Override
  	public void insertString(int offset, String str, AttributeSet attrSet)
           throws BadLocationException {
 
    	if (str == null) {
    		return;
    	}
 
    	if (allowCharAsString != null && str.length() == 1) {
    		if (allowCharAsString.indexOf(str) == -1) {
    			return;
    		}
    	}
 
    	char[] charVal = str.toCharArray();
    	String strOldValue = getText(0, getLength());
    	byte[] tmp = strOldValue.getBytes();
 
    	if (maxLength != -1 && (tmp.length + charVal.length > maxLength)) {
    		return;
    	}
 
    	super.insertString(offset, str, attrSet);
	}
}