package com.fr.design.designer.properties;

import com.fr.general.ComparatorUtils;
import com.fr.form.event.Listener;

public class NameWithListeners {

	private String name;
	private Listener[] ls;

	public NameWithListeners(String name, Listener[] ls) {
		this.name = name;
		this.ls = ls == null ? new Listener[0] : ls;
	}

	public String getName() {
		return this.name;
	}

	public Listener[] getListeners() {
		return this.ls;
	}

	public int getCountOfListeners4ThisName() {
		int count = 0;
		for (int i = 0; i < ls.length; i++) {
			if (ComparatorUtils.equals(name, ls[i].getEventName())) {
				count++;
			}
		}
		return count;
	}
}