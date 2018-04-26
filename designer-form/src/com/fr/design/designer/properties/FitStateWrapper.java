/**
 * 
 */
package com.fr.design.designer.properties;

import com.fr.design.designer.properties.items.FRFitConstraintsItems;

/**
 * @author jim
 * @date 2014-7-31
 */
public class FitStateWrapper extends ItemWrapper{

	/**
	 * @param provider
	 */
	public FitStateWrapper() {
		super(new FRFitConstraintsItems());
	}

}