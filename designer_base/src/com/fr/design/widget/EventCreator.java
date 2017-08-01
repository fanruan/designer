package com.fr.design.widget;

import com.fr.base.FRContext;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameableSelfCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.form.event.Listener;
import com.fr.form.ui.FileEditor;
import com.fr.form.ui.Widget;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

public class EventCreator extends NameableSelfCreator {
	private String eventName;

	public EventCreator(String eventName, Class<? extends BasicBeanPane> updatePane) {
		super(switchLang(eventName), Listener.class, updatePane);
		this.eventName = eventName;
	}

	@Override
	public Nameable createNameable(UnrepeatedNameHelper helper) {
		return new NameObject(helper.createUnrepeatedName(this.menuName()),new Listener(this.eventName)) ;
	}

	public static EventCreator[] createEventCreator(String[] supportedEvents, Class<? extends BasicBeanPane> updatePane) {
		 EventCreator[] creators = new EventCreator[supportedEvents.length];
		
		 for (int i = 0; i < supportedEvents.length; i++) {
		 creators[i] = new EventCreator(supportedEvents[i], updatePane);
		}

		return creators;
	}

	/*
	 * richer:国际化事件名称，所有需要国际化的事件都应按格式Event-eventName来进行国际化
	 */
	public static final String switchLang(String eventName) {
		try {
			return Inter.getLocText("Event-" + eventName);
		} catch (Exception e) {
			FRContext.getLogger().error(e.getMessage(), e);
			return eventName;
		}
	}
	@Override
	public void saveUpdatedBean(ListModelElement wrapper, Object bean) {
		((NameObject)wrapper.wrapper).setObject(bean);
	}
	

//	public static final EventCreator BEFOREEDIT = new EventCreator(Widget.BEFOREEDIT);
//
//	public static final EventCreator AFTEREDIT = new EventCreator(Widget.AFTEREDIT);
//
//	public static final EventCreator CHANGE = new EventCreator(Widget.CHANGE);
//
//	public static final EventCreator CLICK = new EventCreator(Widget.EVENT_CLICK);
//
//	public static final EventCreator SUCCESS = new EventCreator("success");
//
//    public static final EventCreator AFTERINIT = new EventCreator(Widget.AFTERINIT);
//
//	public static final EventCreator STOPEDIT = new EventCreator(Widget.STOPEDIT);
//
//	public static final EventCreator STATECHANGE = new EventCreator(Widget.EVENT_STATECHANGE);
//
//	public static final EventCreator CALLBACK = new EventCreator(FileEditor.EVENT_UPLOAD_CALLBACK);

	@Override
	public String createTooltip() {
		return null;
	}
}