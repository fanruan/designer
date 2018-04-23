package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.widget.editors.NameWidgetComboboxEditor;
import com.fr.form.ui.*;
import com.fr.general.FRFont;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.awt.*;
import java.beans.IntrospectionException;

public class XNameWidget extends XWidgetCreator {

	public XNameWidget(NameWidget widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	 * 返回对应的widget
	 * @return 返回NameWidget
	 */
	public NameWidget toData() {
		return (NameWidget) data;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (editor == null) {
			Graphics2D g2d = (Graphics2D) g.create();
			BaseUtils.drawStringStyleInRotation(g2d, this.getWidth(), this.getHeight(), Inter.getLocText("FR-Engine_NameWidget-Invalid"), Style.getInstance()
					.deriveHorizontalAlignment(SwingConstants.CENTER).deriveVerticalAlignment(SwingConstants.CENTER)
					.deriveFRFont(FRFont.getInstance().applyForeground(Color.RED)), ScreenResolution
					.getScreenResolution());
		}
	}

	/**
	 * 返回控件支持的属性表
	 * @return 属性表
	 * @throws 内省异常
	 */
	@Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
						.getLocText("Form-Widget_Name")),
				new CRPropertyDescriptor("name", this.data.getClass()).setI18NName(Inter.getLocText("FR-Engine_NameWidget-Name")).setEditorClass(
						NameWidgetComboboxEditor.class).setPropertyChangeListener(new PropertyChangeAdapter() {

					@Override
					public void propertyChange() {
						rebuild();
					}
				}) };
	}
	
	protected JComponent initEditor() {
		if (editor == null) {
			WidgetInfoConfig widgetManager = WidgetInfoConfig.getInstance();
			WidgetConfig wc = widgetManager.getWidgetConfig(toData().getName());
			Widget widget;
			if (wc != null && (widget= wc.toWidget()) != null) {
				editor = XCreatorUtils.createXCreator(widget);
				toData().setVisible(widget.isVisible());
				this.setBorder(null);
			} else {
				this.setBorder(DEFALUTBORDER);
			}
		}
		return editor;
	}

	@Override
	protected String getIconName() {
		return "user_widget.png";
	}

	/**
	 * 属性改变后，重新构建控件
	 */
	public void rebuild() {
		editor = null;
		removeAll();
		initEditor();
		if (editor != null) {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			add(editor, BorderLayout.CENTER);
			this.setVisible(toData().isVisible());
		}
	}
	
	/**
	 * 获取当前XCreator的一个封装父容器
	 * 
	 * @param widgetName 当前组件名
	 * 
	 * @return 封装的父容器
	 * 
	 *
	 * @date 2014-11-25-下午4:47:23
	 * 
	 */
	protected XLayoutContainer getCreatorWrapper(String widgetName){
		return shouldScaleCreator() ? new XWScaleLayout() : new XWTitleLayout();
	}
	
	/**
	 * 将当前对象添加到父容器中
	 * 
	 * @param parentPanel 父容器组件
	 * 
	 *
	 * @date 2014-11-25-下午4:57:55
	 * 
	 */
	protected void addToWrapper(XLayoutContainer parentPanel, int width, int minHeight){
		if(!shouldScaleCreator()){
			super.addToWrapper(parentPanel, width, minHeight);
			return;
		}
		
		this.setSize(width, minHeight);
		parentPanel.add(this);
	}
	
	/**
	 * 此控件在自适应布局要保持原样高度
	 * 
	 * @return 是则返回true
	 */
	@Override
	public boolean shouldScaleCreator() {
		if (editor == null) {
			return false;
		}
		XCreator creator = (XCreator) editor;
		return creator.shouldScaleCreator();
	}
	
}