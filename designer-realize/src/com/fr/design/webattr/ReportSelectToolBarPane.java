package com.fr.design.webattr;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

public class ReportSelectToolBarPane<T> extends BasicBeanPane<T> {
	private UIRadioButton reportRadioButton = new UIRadioButton(Inter.getLocText("I_Want_To_Set_Single"));
	private UIRadioButton serverRadioButton = new UIRadioButton(Inter.getLocText("Using_Server_Report_View_Settings"));
	private UIButton serverEditButton = new UIButton(Inter.getLocText("Edit"));
	
	EditToolBarPane<T> editToolBarPane;
	
	public ReportSelectToolBarPane(EditToolBarPane<T> editToolBarPane) {
		this.editToolBarPane = editToolBarPane;
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel northPane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
		this.add(northPane, BorderLayout.NORTH);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(reportRadioButton);
		buttonGroup.add(serverRadioButton);
		
		northPane.add(reportRadioButton);
		northPane.add(serverRadioButton);
		
		reportRadioButton.addActionListener(checkEnabledActionListener);
		serverRadioButton.addActionListener(checkEnabledActionListener);
		
		northPane.add(serverEditButton);
		serverEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReportSelectToolBarPane.this.editToolBarPane.editServerToolBarPane();
			}
		});
		this.add(FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane(), BorderLayout.WEST);
		this.add(editToolBarPane, BorderLayout.CENTER);
	}
	
	private void checkEnabled() {
		editToolBarPane.setEnabled(reportRadioButton.isSelected());
		serverEditButton.setEnabled(serverRadioButton.isSelected());
	}
	
	private ActionListener checkEnabledActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			checkEnabled();
		}
	};
	
	@Override
	protected String title4PopupWindow() {
		return "select";
	}

	@Override
	public void populateBean(T bean) {
		this.reportRadioButton.setSelected(bean != null);
		this.serverRadioButton.setSelected(bean == null);
		
		if (bean != null) {
			editToolBarPane.populateBean(bean);
		}
		
		checkEnabled();
	}

	@Override
	public T updateBean() {
		if (this.reportRadioButton.isSelected()) {
			return this.editToolBarPane.updateBean();
		} else {
			return null;
		}
	}
	
	public static abstract class EditToolBarPane<T> extends BasicBeanPane<T> {
		public abstract void editServerToolBarPane();
	}
}