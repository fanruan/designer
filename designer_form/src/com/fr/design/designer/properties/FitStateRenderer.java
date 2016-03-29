/**
 * 
 */
package com.fr.design.designer.properties;

import com.fr.design.mainframe.widget.renderer.EncoderCellRenderer;

/**
 * @author jim
 * @date 2014-7-31
 */
public class FitStateRenderer extends EncoderCellRenderer{

	/**
	 * @param encoder
	 */
	public FitStateRenderer() {
		super(new FitStateWrapper());
	}

}