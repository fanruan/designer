package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.IntrospectionException;

import javax.swing.JComponent;

import com.fr.stable.core.PropertyChangeAdapter;
import com.fr.design.mainframe.widget.editors.DataTableConfigPane;
import com.fr.design.mainframe.widget.editors.WidgetValueEditor;
import com.fr.design.designer.beans.events.DesignerEditor;
import com.fr.form.data.DataTableConfig;
import com.fr.form.ui.DataTable;
import com.fr.form.ui.WidgetValue;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.design.utils.gui.LayoutUtils;

public class XDataTable extends XWidgetCreator{

	private DesignerEditor<DataTableConfigPane> designerEditor;

	public XDataTable(DataTable widget, Dimension initSize) {
		super(widget, initSize);
	}

	@Override
	protected String getIconName() {
		return "text_field_16.png";
	}

	@Override
	public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
		return (CRPropertyDescriptor[]) ArrayUtils.addAll(super.supportedDescriptor(),
				new CRPropertyDescriptor[] { new CRPropertyDescriptor("widgetValue", this.data.getClass()).setI18NName(
						Inter.getLocText(new String[]{"Widget", "Value"})).setEditorClass(WidgetValueEditor.class)
						.setPropertyChangeListener(new PropertyChangeAdapter() {

							@Override
							public void propertyChange() {
								if (((DataTable) toData()).getWidgetValue() != null
										&& ((DataTable) toData()).getWidgetValue().getValue() instanceof DataTableConfig) {
									designerEditor.getEditorTarget().populate(
											(DataTableConfig) ((DataTable) toData()).getWidgetValue().getValue());
								}
							}
						}) });
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		designerEditor.paintEditor(g, this.getSize());
	}

	@Override
	protected void initXCreatorProperties() {
		super.initXCreatorProperties();
		designerEditor.getEditorTarget().setSize(this.getSize());
		LayoutUtils.layoutContainer(designerEditor.getEditorTarget());
		if (((DataTable) toData()).getWidgetValue() != null
				&& ((DataTable) toData()).getWidgetValue().getValue() instanceof DataTableConfig) {
			designerEditor.getEditorTarget().populate(
					(DataTableConfig) ((DataTable) toData()).getWidgetValue().getValue());
		}
	}

	@Override
	public Dimension initEditorSize() {
		return new Dimension(250, 100);
	}

	@Override
	public DesignerEditor<DataTableConfigPane> getDesignerEditor() {
		return designerEditor;
	}

	@Override
	protected JComponent initEditor() {
		if (designerEditor == null) {
			final DataTableConfigPane configPane = new DataTableConfigPane();
			designerEditor = new DesignerEditor<DataTableConfigPane>(configPane);
			configPane.addpropertyChangeListener(designerEditor);
			designerEditor.addStopEditingListener(new PropertyChangeAdapter() {
				@Override
				public void propertyChange() {
					((DataTable) toData()).setWidgetValue(new WidgetValue(configPane.update()));
				}
			});
		}
		return null;
	}
}