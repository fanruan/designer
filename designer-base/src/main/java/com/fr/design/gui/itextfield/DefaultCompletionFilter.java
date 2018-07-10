package com.fr.design.gui.itextfield;

import java.util.ArrayList;



public class DefaultCompletionFilter implements CompletionFilter {

	private Object[] vector;

	public DefaultCompletionFilter(Object[] v) {
		vector = v;
	}

	public ArrayList filter(String text) {
		ArrayList list = new ArrayList();
		//不区分大小写
		String txt = text.trim().toLowerCase();
		int length = txt.length();
		for (int i = 0; i < vector.length; i++) {
			Object o = vector[i];
			String str = o.toString().toLowerCase();
			if (length == 0 || str.startsWith(txt))
				list.add(o);
		}
		return list;
	}
}