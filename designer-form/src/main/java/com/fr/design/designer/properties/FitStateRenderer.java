/**
 * 
 */
package com.fr.design.designer.properties;

/**
 * @author jim
 * @date 2014-7-31
 */
public class FitStateRenderer extends BodyLayoutAttrRenderer{

	/**
	 * @param encoder
	 */
	public FitStateRenderer() {
		super(new FitStateWrapper());
	}

}