package com.fr.design.gui.icombobox.filter;


public class LastStartWithFilter implements Filter {
    public boolean accept(String prefix, Object object) {
    	boolean result = false;
    	if(prefix == null ){
    		result = true;
    	}else if(object != null ){
    		String tempString = object.toString().toLowerCase();
    		tempString =tempString.substring(tempString.lastIndexOf("/")+1);
    		result=tempString.startsWith(prefix.toLowerCase());
    	}else {
    		result = false ;
    	}
        return result;
    }
}