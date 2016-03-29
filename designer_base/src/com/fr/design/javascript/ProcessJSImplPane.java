package com.fr.design.javascript;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;

import com.fr.base.Parameter;
import com.fr.base.core.KV;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.ReportletParameterViewPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itableeditorpane.UITableModelAdapter;
import com.fr.design.gui.icombobox.ComboCheckBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.editor.editor.Editor;
import com.fr.general.Inter;
import com.fr.js.ProcessJSImpl;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.design.utils.gui.GUICoreUtils;

public abstract class ProcessJSImplPane extends FurtherBasicBeanPane<ProcessJSImpl> {
	private ComboCheckBox comboBox;
	private ProcessParameterPane paraPane;
	
	public ProcessJSImplPane() {
		this.initComponents();
	}
	
	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		/*b:transition pane*/
		JPanel tranpane = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		tranpane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText(new String[]{"Please_Select", "Transition"}) + ":"));
		comboBox = new ComboCheckBox();
		comboBox.setRenderer(new UIComboBoxRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				if (value != null) {
					if (value instanceof Object[]) {
						Object[] obj = (Object[]) value;
						String[] res = new String[obj.length];
						for (int i = 0, len = obj.length; i < len; i++) {
							res[i] = (String)obj[i];
						}
						setText(StringUtils.join(",", res));
					}
				} else {
					setText("");
				}
				return this;
			}
		});
		tranpane.add(new UILabel(Inter.getLocText("Transition") + ":"));
		tranpane.add(comboBox);
		this.add(tranpane, BorderLayout.NORTH);
		
		/*b:parameters pane*/
		JPanel paraspane = new JPanel(new BorderLayout());
		paraspane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Set-Parameter-Name") + ":"));
		paraPane = new ProcessParameterPane();
		paraspane.add(paraPane, BorderLayout.CENTER);
		this.add(paraspane, BorderLayout.CENTER);
	}
	
	protected abstract  Editor[] getCorrespondEditors();
	
	private String[] getTransitions() {
		JTemplate<?, ?> template = DesignerContext.getDesignerFrame().getSelectedJTemplate();
		if (template == null) {
			return new String[0];
		}
		return ProcessTransitionAdapter.getTransitionNamesByBookWithShared(template.getEditingFILE().getEnvFullName());
	}
	
	private ParameterProvider[] getParameters() {
		JTemplate<?, ?> template = DesignerContext.getDesignerFrame().getSelectedJTemplate();
		if (template == null) {
			return new ParameterProvider[0];
		}
		return ProcessTransitionAdapter.getParasWithShared(template.getEditingFILE().getEnvFullName());
	}
	
	/**
	 * 更新表单在流程里可以往前进的分支。
	 */
	public void refreshTransitions() {
		comboBox.setData(this.getTransitions());
	}
	
	/**
	 * 重置界面
	 */
	public void reset() {
		populateBean(null);
	}
	
	@Override
	public void populateBean(ProcessJSImpl ob) {
		if (ob == null) {			
			ob = new ProcessJSImpl();
		}
		//b:每次Pop是刷新transitions		
		this.refreshTransitions();
		comboBox.getModel().setSelectedItem(ob.getTransitionNames());
		paraPane.populate(ob.getParameters());
	}

	@Override
	public ProcessJSImpl updateBean() {
		ProcessJSImpl js = new ProcessJSImpl();
		if (comboBox.getSelectedItem() instanceof Object[]) {
			Object[] os = (Object [])comboBox.getSelectedItem();
			String[] ss = new String[os.length];
			for (int i = 0, len = os.length; i < len; i++) {
				ss[i] = (String)os[i];
			}
			js.setTransitionNames(ss);
		}

		List<ParameterProvider> list = paraPane.update();
		js.setParameters(list.toArray(new ParameterProvider[list.size()]));
		return js;
	}

	@Override
	/**
	 * 弹出的窗口的标题
	 * @return 标题
	 */
	public String title4PopupWindow() {
		return Inter.getLocText("ProcessManager");
	}

	private class ProcessParameterPane extends ReportletParameterViewPane {
		private UITableEditorPane<ParameterProvider> editorPane;
		
		public ProcessParameterPane() {
			this.initComponents();
		}
		
		private void initComponents() {
			this.setLayout(FRGUIPaneFactory.createBorderLayout());
			UITableModelAdapter<ParameterProvider> model = new ProcessParameterTableModel() {
				@Override
				public UITableEditAction[] createAction() {
					return new UITableEditAction[] { new AddParameterAction(), new DeleteAction()};
				}
				
				protected Editor[] getCorrespondEditors() {
					return ProcessJSImplPane.this.getCorrespondEditors();
				}
 			};

			editorPane = new UITableEditorPane<ParameterProvider>(model);
			this.add(editorPane, BorderLayout.CENTER);
		}
		
		public void populate(ParameterProvider[] parameters) {
			if (parameters == null) {
				return;
			}
			editorPane.populate(parameters);
		}

		public void populate(KV[] kv) {
			if (kv == null) {
				return;
			}
			Parameter[] parameters = new Parameter[kv.length];
			for (int i = 0; i < kv.length; i++) {
				parameters[i] = new Parameter(kv[i].getKey(), kv[i].getValue());
			}
			this.populate(parameters);
		}
		
		public List<ParameterProvider> update() {
			return editorPane.update();
		}

		public KV[] updateKV() {
			List<ParameterProvider> list = this.update();
			int length = list.size();
			KV[] kv = new KV[length];
			for (int i = 0; i < length; i++) {
				kv[i] = new KV();
				kv[i].setKey(list.get(i).getName());
				kv[i].setValue( list.get(i).getValue());
			}
			return kv;
		}
		
		protected String title4PopupWindow() {
			return Inter.getLocText(new String[]{"ProcessManager", "Parameter"});
		}
	}

	@Override
	/**
	 * 判断传进的对象是否是面板支持（能显示）的对象
	 * @ob 需要展示的对象
	 * @return 是否是支持的对象
	 */
	public boolean accept(Object ob) {
		return ob instanceof ProcessJSImpl;
	}
	
	
}