package com.fr.design.javascript;

import com.fr.base.MultiFieldParameter;
import com.fr.stable.ParameterProvider;

public abstract class ProcessTransitionAdapter {
	private static ProcessTransitionAdapter adapter;
	
	public ProcessTransitionAdapter() {
	}
	
	public static void setProcessTransitionAdapter(ProcessTransitionAdapter adapter) {
		ProcessTransitionAdapter.adapter = adapter;
	}
	
	public static String[] getTransitionNamesByBookWithShared(String book) {
		return adapter == null ? new String[0] : adapter.getTransitionNamesByBook(book);
	}
	
	public static String[] getParaNamesWithShared(String book) {
		return adapter == null ? new String[0] : adapter.getParaNames(book);
	}
	
	public static ParameterProvider[] getParasWithShared(String book) {
		return adapter == null ? new ParameterProvider[0] : adapter.getParas(book);
	}
	
	public static MultiFieldParameter[] getAllMultiFieldWithShared(String book) {
		return adapter == null ? new MultiFieldParameter[0] : adapter.getAllMultiFieldParas(book);
	}


	protected abstract String[] getTransitionNamesByBook(String book);
	
	protected abstract String[] getParaNames(String book);
	
	protected abstract ParameterProvider[] getParas(String book);
	
	protected abstract MultiFieldParameter[] getAllMultiFieldParas(String book);
}