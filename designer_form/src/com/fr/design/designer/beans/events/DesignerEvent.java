package com.fr.design.designer.beans.events;

import com.fr.design.designer.creator.XComponent;

/**
 * 设计事件
 */
public class DesignerEvent {

	public static final int CREATOR_ADDED = 1;

	public static final int CREATOR_DELETED = 2;

	public static final int CREATOR_CUTED = 3;

	public static final int CREATOR_PASTED = 4;

	public static final int CREATOR_EDITED = 5;

	public static final int CREATOR_RESIZED = 6;

	public static final int CREATOR_SELECTED = 7;
	
	public static final int CREATOR_RENAMED = 8;

	public static final int CREATOR_ORDER_CHANGED = 9;

	private int eventID;
	private XComponent affectedXCreator;

	DesignerEvent(int eventID, XComponent comp) {
		this.eventID = eventID;
		this.affectedXCreator = comp;
	}

	public int getCreatorEventID() {
		return eventID;
	}

	public XComponent getAffectedCreator() {
		return affectedXCreator;
	}
}