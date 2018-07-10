/**
 * 
 */
package com.fr.design.mainframe.widget.editors;

import com.fr.design.designer.properties.EnumerationEditor;
import com.fr.design.designer.properties.items.FRFitConstraintsItems;

/**
 * @author jim
 * @date 2014-7-31
 */
public class FitLayoutDirectionEditor extends EnumerationEditor{
	
	public FitLayoutDirectionEditor() {
		super(new FRFitConstraintsItems());
	}

}