package com.fr.design.selection;

/**
 * 
 * @author zhou
 * @since 2012-7-26上午10:20:32
 */
public interface Selectedable<S extends SelectableElement> {

	public S getSelection();

	public void setSelection(S selectElement);

	/**
	 * Adds a <code>ChangeListener</code> to the listener list.
	 */
	public void addSelectionChangeListener(SelectionListener selectionListener);

	/**
	 * removes a <code>ChangeListener</code> from the listener list.
	 */
	public void removeSelectionChangeListener(SelectionListener selectionListener);

	// august:这儿就不要加fireSelectionChangeListener方法了。因为这个方法一般要定义成私有的，不然外部随即的调用！
}