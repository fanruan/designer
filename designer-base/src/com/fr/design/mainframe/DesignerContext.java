/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import com.fr.base.Style;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.BasicDialog;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.datatransfer.Clipboard;
import java.lang.reflect.Method;
import java.util.Hashtable;

public class DesignerContext {

	//格式刷的三个状态
	public static final int FORMAT_STATE_NULL = 0;
	public static final int FORMAT_STATE_ONCE = 1;
	public static final int FORMAT_STATE_MORE = 2;

	private static Clipboard clipboard = null; //当前的剪贴板.
	private static int formatState = FORMAT_STATE_NULL;
	private static Style[][] referencedStyle = null;
	private static TargetComponent referencedElementCasePane;
	private static int referencedIndex = 0;
    private static ThreadLocal<BasicDialog> reportWriteThread = new ThreadLocal<BasicDialog>();

	public DesignerContext() {

	}

	// to hold some env properties values.
	private static Hashtable<String, DesignerFrame> prop = new Hashtable<String, DesignerFrame>();

	private static Hashtable<String, DesignerBean> beans = new Hashtable<String, DesignerBean>();

	private static boolean refreshOnTargetModified = false;

	/**
	 * Return the main design frame from context
	 */
	public static DesignerFrame getDesignerFrame() {
		return prop.get("DesignerFrame");
	}

	/**
	 * Set the main design frame to context.
	 */
	public static void setDesignerFrame(DesignerFrame designerFrame) {
		prop.put("DesignerFrame", designerFrame);
	}

	public static DesignerBean getDesignerBean(String name) {
		return beans.get(name) == null ? DesignerBean.NULL : beans.get(name);
	}

	public static void setDesignerBean(String name, DesignerBean bean) {
		beans.put(name, bean);
	}

	/**
	 * Gets the Clipboard.
	 */
	public static Clipboard getClipboard(JComponent comp) {
		if (DesignerContext.clipboard == null) {
			try {
				Action transferAction = TransferHandler.getCutAction();
				Method clipMethod = StableUtils.getDeclaredMethod(transferAction.getClass(), "getClipboard", new Class[]{JComponent.class});
				clipMethod.setAccessible(true);

				return (Clipboard) clipMethod.invoke(transferAction, new Object[]{comp});
			} catch (Exception securityException) {
				FineLoggerFactory.getLogger().error(securityException.getMessage(), securityException);
				//用反射机制，获得TransferHandler的getClipboard
				//这样可以保证和TextField直接的copy paste
				try {
					//控件的Clipboard.
					DesignerContext.clipboard = comp.getToolkit().getSystemClipboard();
				} catch (Exception exp) {
					FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
					DesignerContext.clipboard = new Clipboard("FR");
				}
			}
		}

		return DesignerContext.clipboard;
	}


	public static void setFormatState(int setformatState) {
		formatState = setformatState;
	}

	public static int getFormatState() {
		return formatState;
	}


	public static void setReferencedStyle(Style[][] styles) {
		referencedStyle = styles;
	}

	public static Style[][] getReferencedStyle() {
		return referencedStyle;
	}

	public static void setReferencedElementCasePane(TargetComponent t) {
		referencedElementCasePane = t;
	}

	public static TargetComponent getReferencedElementCasePane() {
		return referencedElementCasePane;
	}

	public static void setReferencedIndex(int index) {
		referencedIndex = index;
	}

	public static int getReferencedIndex() {
		return referencedIndex;
	}

    /**
     * 得到当前实例
     * @return 实例。一般一次只能打开一个报表填报属性面板
     */
    public static BasicDialog getReportWritePane(){
        return reportWriteThread.get();
    }

    /**
     * 记录当前报表填报属性面板（具体的实例）
     */
    public static void setReportWritePane(BasicDialog dlg){
        reportWriteThread.set(dlg);
    }

    /**
     * 在修改 Target 时，刷新右侧属性面板
     */
    public static void enableRefreshOnTargetModified() {
        refreshOnTargetModified = true;
    }

    /**
     * 在修改 Target 时，是否刷新右侧属性面板。仅可刷新一次，取值后重置为 false
     */
    public static boolean isRefreshOnTargetModifiedEnabled() {
        boolean isRefresh = refreshOnTargetModified;
        refreshOnTargetModified = false;
        return isRefresh;
    }
}