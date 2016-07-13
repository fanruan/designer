package com.fr.design.designer.creator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.IntrospectionException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.fr.base.chart.BaseChart;
import com.fr.base.chart.BaseChartCollection;
import com.fr.design.gui.chart.BaseChartPropertyPane;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.widget.editors.WLayoutBorderStyleEditor;
import com.fr.design.mainframe.widget.renderer.LayoutBorderStyleRenderer;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.BaseChartEditor;
import com.fr.form.ui.Widget;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.core.PropertyChangeAdapter;

/**
 * form中的图表按钮弹出的控件, 创建初始化图表内容.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2013-7-5 上午10:28:30
 *          类说明
 */
public class XChartEditor extends XBorderStyleWidgetCreator {
	private static final long serialVersionUID = -7009439442104836657L;
	//具体来说是DesignerEditor<SimpleChartComponent>
	private DesignerEditor<JComponent> designerEditor;
	//	private DesignerEditor<SimpleChartComponent> designerEditor;
	//marro：无奈的属性，暂时想不出好办法
	private boolean isRefreshing = false;

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

    /**
     *  得到属性名
     * @return 属性名
     * @throws java.beans.IntrospectionException
     */
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return  new CRPropertyDescriptor[] {
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Inter
                        .getLocText("Form-Widget_Name")),
                new CRPropertyDescriptor("borderStyle", this.data.getClass()).setEditorClass(
                        WLayoutBorderStyleEditor.class).setRendererClass(LayoutBorderStyleRenderer.class).setI18NName(
                        Inter.getLocText("Chart-Style_Name")).putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
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
		getDesignerEditorTarget().addStopEditingListener(new PropertyChangeAdapter() {
			public void propertyChange() {
				JComponent pane = jform.getEditingPane();
				if (pane instanceof BaseChartPropertyPane) {
					((BaseChartPropertyPane) pane).setSupportCellData(true);
					((BaseChartPropertyPane) pane).populateChartPropertyPane(getDesignerEditorTarget().update(), formDesigner);
				}
			}
		});

		final BaseChartPropertyPane propertyPane = DesignModuleFactory.getChartWidgetPropertyPane(formDesigner);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (getDesignerEditor().getEditorTarget() != null) {
					propertyPane.setSupportCellData(true);
					propertyPane.populateChartPropertyPane(getDesignerEditorTarget().update(), formDesigner);
				}
			}
		});
		return (JComponent)propertyPane;
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
		super.paint(g);
		designerEditor.paintEditor(g, this.getSize());
	}

	/**
	 * 初始化Editor大小.
	 *
	 * @return 返回大小.
	 */
	public Dimension initEditorSize() {
		return new Dimension(250, 100);
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
		return null;
	}
}