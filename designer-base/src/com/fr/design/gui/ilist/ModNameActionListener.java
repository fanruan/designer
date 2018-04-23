package com.fr.design.gui.ilist;

/*
 * 专门给JNameEdList用的,当名字改变时的执行的Action
 */
public interface ModNameActionListener {
	/*
	 * 第index个item的名字由oldName改为newName了
	 */
	public void nameModed(int index, String oldName, String newName);
}