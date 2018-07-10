package com.fr.design.selection;

import java.util.EventListener;

/**
 * 
 * @author zhou
 * @since 2012-7-25下午3:45:33
 */
public interface SelectionListener extends EventListener {

	public void selectionChanged(SelectionEvent e);

}