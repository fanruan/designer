package com.fr.design.designer.creator;

import com.fr.base.GraphHelper;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.ComponentAdapter;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.properties.mobile.ChartEditorPropertyUI;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.CoverReportPane;
import com.fr.design.mainframe.EditingMouseListener;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.HelpDialogManager;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.module.DesignModuleFactory;
import com.fr.form.ui.BaseChartEditor;
import com.fr.form.ui.Widget;
import com.fr.stable.Constants;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.IntrospectionException;

/**
 * form中的图表按钮弹出的控件, 创建初始化图表内容.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-7-5 上午10:28:30
 *          类说明
 */
public class XChartEditor extends XBorderStyleWidgetCreator {
	private static final long serialVersionUID = -7009439442104836657L;
	private static final int BORDER_WIDTH = 2;
	//具体来说是DesignerEditor<SimpleChartComponent>
	private DesignerEditor<JComponent> designerEditor;
	//	private DesignerEditor<SimpleChartComponent> designerEditor;
	//marro：无奈的属性，暂时想不出好办法
	private boolean isRefreshing = false;

	private boolean isEditing = false;

	private boolean isHovering = false;
	private static final Color OUTER_BORDER_COLOR = new Color(65, 155, 249, 30);
	private static final Color INNER_BORDER_COLOR = new Color(65, 155, 249);

	public XChartEditor(BaseChartEditor editor) {
		this(editor, new Dimension(250, 150));
	}

	public XChartEditor(BaseChartEditor editor, Dimension size) {
		super((Widget)editor, size);
	}

	@Override
	public String getIconPath() {
		return super.getIconPath();
	}


	@Override
	protected String getIconName() {
		return "Chart.png";
	}

	/**
	 * 返回组件默认名
	 * @return 组件类名(小写)
	 */
	public String createDefaultName() {
		return "chart";
	}

	/**
	 * 是否支持设置标题
	 * @return 是返回true
	 */
	public boolean hasTitleStyle() {
		return true;
	}

	@Override
	public void stopEditing() {
		isEditing = false;
	}

	/**
	 *  得到属性名
	 * @return 属性名
	 * @throws java.beans.IntrospectionException
	 */
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return  new CRPropertyDescriptor[] {
				new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Toolkit.i18nText("Fine-Design_Form_Form_Widget_Name")),
				new CRPropertyDescriptor("visible", this.data.getClass()).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Visible")).setPropertyChangeListener(new PropertyChangeAdapter() {

					@Override
					public void propertyChange() {
						makeVisible(toData().isVisible());}
				}),
				new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
						WLayoutBorderStyleEditor.class).setI18NName(
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Style")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Fine-Design_Basic_Advanced")
						.setPropertyChangeListener(new PropertyChangeAdapter() {

					@Override
					public void propertyChange() {
						initStyle();
					}
				}),
		};
	}

	/**
	 * 该组件是否可以拖入参数面板
	 * @return 是则返回true
	 */
	public boolean canEnterIntoParaPane(){
		return false;
	}


	/**
	 *  编辑状态的时候需要重新绘制下边框
	 *
	 */
	@Override
	public void paintBorder(Graphics g, Rectangle bounds){
		if(isEditing){
			g.setColor(OUTER_BORDER_COLOR);
			GraphHelper.draw(g, new Rectangle(bounds.x - BORDER_WIDTH, bounds.y - BORDER_WIDTH, bounds.width + BORDER_WIDTH + 1, bounds.height + BORDER_WIDTH + 1), Constants.LINE_LARGE);
		}else if(!isHovering){
			super.paintBorder(g, bounds);
		}
	}


	/**
	 * 返回设计器的Editor
	 */
	public DesignerEditor<JComponent> getDesignerEditor() {
		return designerEditor;
	}

	@Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		initBorderStyle();
		BaseChartCollection collection = ((BaseChartEditor) data).getChartCollection();
		isRefreshing = true;
		((MiddleChartComponent) designerEditor.getEditorTarget()).populate(collection);
		isRefreshing = false;
	}

	/**
	 * 点击选中的时候, 刷新界面
	 * 右键 reset之后, 触发事件 populate此方法
	 *
	 * @param jform        表单
	 * @param formDesigner 表单设计器
	 * @return 控件.
	 */
	public JComponent createToolPane(final BaseJForm jform, final FormDesigner formDesigner) {
		MiddleChartComponent middleChartComponent = getDesignerEditorTarget();
		if (null != middleChartComponent) {
			middleChartComponent.addStopEditingListener(new PropertyChangeAdapter() {
				public void propertyChange() {
					JComponent pane = jform.getEditingPane();
					if (pane instanceof BaseChartPropertyPane) {
						((BaseChartPropertyPane) pane).setSupportCellData(true);
						MiddleChartComponent middleChartComponent = getDesignerEditorTarget();
						if (null != middleChartComponent) {
							((BaseChartPropertyPane) pane).populateChartPropertyPane(middleChartComponent.update(), formDesigner);
						}
					}
				}
			});
		}
		if (isEditing){
			final BaseChartPropertyPane propertyPane = DesignModuleFactory.getChartPropertyPane();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (getDesignerEditor().getEditorTarget() != null) {
						propertyPane.setSupportCellData(true);
						MiddleChartComponent middleChartComponent = getDesignerEditorTarget();
						if (null != middleChartComponent) {
							propertyPane.populateChartPropertyPane(middleChartComponent.update(), formDesigner);
						}
					}
				}
			});
			return (JComponent)propertyPane;
		}
		else{
			return (JComponent)DesignModuleFactory.getWidgetPropertyPane(formDesigner);
		}
	}

	private MiddleChartComponent getDesignerEditorTarget() {
		MiddleChartComponent bcc = null;
		if (getDesignerEditor().getEditorTarget() instanceof MiddleChartComponent) {
			bcc = (MiddleChartComponent) getDesignerEditor().getEditorTarget();
		}
		return bcc;
	}

	/**
	 * 渲染Painter
	 */
	public void paint(Graphics g) {
		designerEditor.paintEditor(g, this.getSize());
		super.paint(g);
		if(isEditing){
			g.setColor(INNER_BORDER_COLOR);
			GraphHelper.draw(g, new Rectangle(0, 0, getWidth(), getHeight()), Constants.LINE_MEDIUM);
		}
	}

	/**
	 * 初始化Editor大小.
	 *
	 * @return 返回大小.
	 */
	public Dimension initEditorSize() {
		return BORDER_PREFERRED_SIZE;
	}

	/**
	 * 响应点击事件
	 *
	 * @param editingMouseListener 鼠标点击，位置处理器
	 * @param e 鼠标点击事件
	 */
	public void respondClick(EditingMouseListener editingMouseListener,MouseEvent e){
		FormDesigner designer = editingMouseListener.getDesigner();
		SelectionModel selectionModel = editingMouseListener.getSelectionModel();
		isEditing =  e.getButton() == MouseEvent.BUTTON1 &&
				(e.getClickCount() == 2 || designer.getCursor().getType() == Cursor.HAND_CURSOR);
		displayCoverPane(!isEditing);
		selectionModel.selectACreatorAtMouseEvent(e);

		if (editingMouseListener.stopEditing()) {
			if (this != (XCreator)designer.getRootComponent()) {
				ComponentAdapter adapter = AdapterBus.getComponentAdapter(designer, this);
				editingMouseListener.startEditing(this, isEditing ? adapter.getDesignerEditor() : null, adapter);
			}
		}
		HelpDialogManager.getInstance().setPane(coverPanel);
		if (this.isHelpBtnOnFocus()) {
			coverPanel.setMsgDisplay(e);
		}
	}

	@Override
	protected JComponent initEditor() {
		if (designerEditor == null) {
			final MiddleChartComponent chartComponent = DesignModuleFactory.getChartComponent(((BaseChartEditor) data).getChartCollection());
			if (chartComponent != null) {
				JComponent jChart = chartComponent;
				jChart.setBorder(BorderFactory.createLineBorder(Color.lightGray));
				designerEditor = new DesignerEditor<JComponent>(jChart);
				chartComponent.addStopEditingListener(designerEditor);
				designerEditor.addPropertyChangeListener(new PropertyChangeAdapter() {
					public void propertyChange() {
						if (!isRefreshing) {
							((BaseChartEditor) data).resetChangeChartCollection(chartComponent.update());
						}
					}
				});
			}
		}

		if (editor == null) {
			setBorder(DEFALUTBORDER);
			editor = new JPanel();
			editor.setBackground(null);
			editor.setLayout(null);
			editor.setOpaque(false);

			coverPanel = new CoverReportPane();
			coverPanel.setPreferredSize(this.getPreferredSize());
			coverPanel.setBounds(this.getBounds());

			editor.add(coverPanel);
			coverPanel.setVisible(false);
		}
		return editor;
	}

	/**
	 * 是否展现覆盖的pane
	 * @param display     是否
	 */
	public void  displayCoverPane(boolean display){
		isHovering = display;
		coverPanel.setVisible(display);
		coverPanel.setPreferredSize(editor.getPreferredSize());
		coverPanel.setBounds(editor.getBounds());
		editor.repaint();
	}

	public JComponent getCoverPane(){
		return coverPanel;
	}

	/**
	 * 是否支持设置可用
	 * return boolean
	 */
	public boolean supportSetEnable(){
		return false;
	}

	/**
	 * data属性改变触发其他操作
	 *
	 */
	public void firePropertyChange(){
		initStyle();
	}

	@Override
	public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
		return new WidgetPropertyUIProvider[]{ new ChartEditorPropertyUI(this)};
	}

	/**
	 * 是否支持共享-现只支持报表块、图表、tab块、绝对布局
	 * @return
	 */
	public boolean isSupportShared() {
		return true;
	}

	@Override
	public boolean supportMobileStyle() {
		return false;
	}
}
