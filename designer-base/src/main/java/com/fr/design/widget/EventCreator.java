package com.fr.design.widget;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameableSelfCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.form.event.Listener;
import com.fr.general.NameObject;
import com.fr.report.web.util.ReportEngineEventMapping;
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
	 * richer:国际化事件名称，所有需要国际化的事件都应根据使用的位置
	 * 按格式 Fine-模块_xx 来进行国际化
	 */
	public static final String switchLang(String eventName) {
		try {
		    // 通过映射 ， 找到相对应的 properties 文件中的 key 值
            String localeKey = ReportEngineEventMapping.getLocaleName(eventName);
            return com.fr.design.i18n.Toolkit.i18nText(localeKey);
		} catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
			return eventName;
		}
	}
	@Override
	public void saveUpdatedBean(ListModelElement wrapper, Object bean) {
		((NameObject)wrapper.wrapper).setObject(bean);
	}

	@Override
	public String createTooltip() {
		return null;
	}
}