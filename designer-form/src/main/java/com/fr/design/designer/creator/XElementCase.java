package com.fr.design.designer.creator;

import com.fr.base.BaseUtils;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.designer.properties.mobile.ElementCasePropertyUI;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.fun.FormElementCaseEditorProvider;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.fun.impl.AbstractFormElementCaseEditorProvider;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.*;
import com.fr.design.mainframe.widget.editors.ElementCaseToolBarEditor;
import com.fr.design.mainframe.widget.editors.PaddingMarginEditor;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.propertypane.BrowserFitPropertyEditor;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.FormProvider;
import com.fr.form.ui.ElementCaseEditor;
import com.fr.form.ui.ElementCaseEditorProvider;

import com.fr.report.fit.ReportFitAttr;
import com.fr.stable.ArrayUtils;
import com.fr.stable.CoreGraphHelper;
import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.report.fit.FitProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Set;

public class XElementCase extends XBorderStyleWidgetCreator implements FormElementCaseContainerProvider{
	private UILabel imageLable;
	private CoverReportPane coverPanel;
	private FormDesigner designer;
	private static BufferedImage DEFAULT_BACKGROUND;
	private boolean isHovering = false;

	static{
		try{
			DEFAULT_BACKGROUND = BaseUtils.readImageWithCache("com/fr/base/images/report/elementcase.png");
		}catch (Throwable e) {
			//IBM jdk 1.5.0_22 并发下读取图片有时会异常(EOFException), 这个图片反正只有设计器用到, 捕获住
			DEFAULT_BACKGROUND = CoreGraphHelper.createBufferedImage(0, 0);
		}
	}

	public XElementCase(ElementCaseEditor widget, Dimension initSize) {
		super(widget, initSize);


	}

	protected void initXCreatorProperties() {
		super.initXCreatorProperties();

		// 报表块初始化时要加载对应的borderStyle
		initBorderStyle();
	}

	/**
	 * 是否支持设置标题
	 * @return 是返回true
	 */
	public boolean hasTitleStyle() {
		return true;
	}

	/**
	 * 返回组件属性值
	 * @return 返回组件属性值
	 * @throws IntrospectionException 异常
	 */
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {

		CRPropertyDescriptor[] propertyTableEditor = new CRPropertyDescriptor[]{
				new CRPropertyDescriptor("widgetName", this.data.getClass())
						.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Form_Widget_Name")),
				new CRPropertyDescriptor("visible", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Visible")).setPropertyChangeListener(new PropertyChangeAdapter() {

					@Override
					public void propertyChange() {
						makeVisible(toData().isVisible());
					}
				}),
				new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
						WLayoutBorderStyleEditor.class).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Style")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced")
						.setPropertyChangeListener(new PropertyChangeAdapter() {

					@Override
					public void propertyChange() {
						initStyle();
					}
				}),
				new CRPropertyDescriptor("margin", this.data.getClass()).setEditorClass(PaddingMarginEditor.class)
						.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout_Padding"))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced"),
				new CRPropertyDescriptor("toolBars", this.data.getClass()).setEditorClass(ElementCaseToolBarEditor.class)
						.setI18NName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_EC_Toolbar"))
						.putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced")


		};
		CRPropertyDescriptor[] extraTableEditor = getExtraTableEditor();
		return  ArrayUtils.addAll(propertyTableEditor, extraTableEditor);
	}


	public CRPropertyDescriptor[] getExtraTableEditor(){
		CRPropertyDescriptor[] extraTableEditor = resolveCompatible();
		CRPropertyDescriptor reportFitEditor = getReportFitEditor();
		if (reportFitEditor == null) {
			return extraTableEditor;
		}
		return ArrayUtils.add(extraTableEditor, reportFitEditor);
	}

	@Override
	public boolean supportMobileStyle() {
		return false;
	}

	private CRPropertyDescriptor getReportFitEditor() {
		this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
		FitProvider wbTpl = designer.getTarget();
		ReportFitAttr fitAttr = wbTpl.getReportFitAttr();
		ElementCaseEditor editor = this.toData();
		//兼容之前报表块（之前三个选项为：默认 横向 双向 现在是：横向 双向 不自适应)
		if (editor.getFitStateInPC() == 0) {
			editor.setReportFitAttr(null);
		}
		ReportFitAttr reportFit = editor.getReportFitAttr();
		if(fitAttr != null){
			reportFit = fitAttr.fitInBrowser() ? editor.getReportFitAttr() : fitAttr;
		}
		ReportFitAttr reportFitAttr = editor.getReportFitAttr() == null ? fitAttr : reportFit;
		BrowserFitPropertyEditor browserFitPropertyEditor = new BrowserFitPropertyEditor();
		CRPropertyDescriptor extraEditor = browserFitPropertyEditor.createPropertyDescriptor(this.data.getClass(), reportFitAttr);
		if (editor.getReportFitAttr() == null) {
			editor.setReportFitInPc(browserFitPropertyEditor.getFitStateInPC(fitAttr));
		}
		return extraEditor;
	}


	private CRPropertyDescriptor[] resolveCompatible() {
		CRPropertyDescriptor[] extraProperty = new CRPropertyDescriptor[0];
		//这边有个插件兼容问题,之后还是要改回process才行
		Set<FormElementCaseEditorProvider> set = ExtraDesignClassManager.getInstance().getArray(AbstractFormElementCaseEditorProvider.MARK_STRING);
		for (FormElementCaseEditorProvider provider : set) {
			if (provider == null) {
				continue;
			}
			this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
			FormProvider formProvider = designer.getTarget();
			ElementCaseEditorProvider elementCaseEditorProvider = this.toData();
			PropertyDescriptor[] extraEditor = provider.createPropertyDescriptor(this.data.getClass(), formProvider, elementCaseEditorProvider);
			extraProperty = (CRPropertyDescriptor[]) ArrayUtils.addAll(extraProperty, extraEditor);
		}
		return extraProperty;
	}

	@Override
	protected String getIconName() {
		return "text_field_16.png";
	}

	/**
	 * 返回组件默认名
	 * @return 组件类名(小写)
	 */
	public String createDefaultName() {
		return "report";
	}

	@Override
	protected JComponent initEditor() {
		if (editor == null) {
			setBorder(DEFALUTBORDER);
			editor = new JPanel();
			editor.setBackground(null);
			editor.setLayout(null);
			imageLable = initImageBackground();

			coverPanel = new CoverReportPane();
			coverPanel.setPreferredSize(imageLable.getPreferredSize());
			coverPanel.setBounds(imageLable.getBounds());

			editor.add(coverPanel);
			coverPanel.setVisible(false);
			editor.add(imageLable);
		}
		return editor;
	}

	/**
	 * 从data中获取到图片背景, 并设置到Label上
	 */
	private UILabel initImageBackground(){
		UILabel imageLable = new UILabel();
		BufferedImage image = toData().getECImage();
		if (image == null) {
			image = DEFAULT_BACKGROUND;
		}
		setLabelBackground(image, imageLable);

		return imageLable;
	}

	/**
	 * 设置指定Label的背景
	 */
	private void setLabelBackground(Image image, UILabel imageLable){
		ImageIcon icon = new ImageIcon(image);
		imageLable.setIcon(icon);
		imageLable.setOpaque(true);
		imageLable.setLayout(null);
		imageLable.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
	}

	/**
	 * 是否展现覆盖的pane
	 * @param display     是否
	 */
	public void  displayCoverPane(boolean display){
		isHovering = display;
		coverPanel.setVisible(display);
		coverPanel.setBounds(1, 1, (int) editor.getBounds().getWidth(), (int) editor.getBounds().getHeight());
		editor.repaint();
	}

	/**
	 * 销毁帮助提示框
	 */
	public void destroyHelpDialog(){
		coverPanel.destroyHelpDialog();
	}

	public JComponent getCoverPane(){
		return coverPanel;
	}

	@Override
	public void paintBorder(Graphics g, Rectangle bounds){
		if (!isHovering) {
			super.paintBorder(g, bounds);
		}
	}


	/**
	 * 初始化大小
	 * @return  尺寸
	 */
	public Dimension initEditorSize() {
		return BORDER_PREFERRED_SIZE;
	}

	/**
	 * 是否是报表块
	 * @return  是
	 */
	public boolean isReport() {
		return true;
	}

	/**
	 * 该组件是否可以拖入参数面板
	 * @return 是则返回true
	 */
	public boolean canEnterIntoParaPane(){
		return false;
	}

	/**
	 * 返回报表块对应的widget
	 * @return 返回ElementCaseEditor
	 */
	public ElementCaseEditor toData() {
		return ((ElementCaseEditor) data);
	}

	public FormElementCaseProvider getElementCase() {
		return toData().getElementCase();
	}

	public String getElementCaseContainerName() {
		return toData().getWidgetName();
	}

	public void setElementCase(FormElementCaseProvider el) {
		toData().setElementCase(el);
	}

	public void setBackground(BufferedImage image){
		toData().setECImage(image);
		setEditorIcon(image);
	}

	private void setEditorIcon(BufferedImage image){
		setLabelBackground(image, imageLable);
	}

	public Dimension getSize(){
		return new Dimension(this.getWidth(), this.getHeight());
	}

	/**
	 * 响应点击事件
	 * @param editingMouseListener 事件处理器
	 * @param e 点击事件
	 */
	public void respondClick(EditingMouseListener editingMouseListener,MouseEvent e){
		HelpDialogManager.getInstance().setPane(coverPanel);
		super.respondClick(editingMouseListener, e);
		if (this.isHelpBtnOnFocus()) {
			coverPanel.setMsgDisplay(e);
		} else {
			switchTab(e, editingMouseListener);
		}
	}


	private void switchTab(MouseEvent e,EditingMouseListener editingMouseListener){
		FormDesigner designer = editingMouseListener.getDesigner();
		if (e.getButton() == MouseEvent.BUTTON1 &&
				(e.getClickCount() == 2 || designer.getCursor().getType() == Cursor.HAND_CURSOR)){
			FormElementCaseContainerProvider component = (FormElementCaseContainerProvider) designer.getComponentAt(e);
			//切换设计器
			designer.switchTab(component);
		}
	}

	@Override
	public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
		return new WidgetPropertyUIProvider[]{ new ElementCasePropertyUI(this)};
	}

	@Override
	public void setXDescrption(String msg) {
		coverPanel.setHelpMsg(msg);
	}

	/**
	 * data属性改变触发其他操作
	 *
	 */
	public void firePropertyChange(){
		initStyle();
	}

	/**
	 * 是否支持设置可用
	 * return boolean
	 */
	public boolean supportSetEnable(){
		return false;
	}

	/**
	 * 是否支持共享-现只支持报表块、图表、tab块、绝对布局
	 * @return
	 */
	public boolean isSupportShared() {
		return true;
	}
}
