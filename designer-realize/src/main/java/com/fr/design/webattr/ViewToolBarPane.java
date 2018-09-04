package com.fr.design.webattr;

import com.fr.base.ConfigManager;
import com.fr.config.Configuration;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.event.Listener;

import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.report.web.WebView;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ViewToolBarPane extends AbstractEditToolBarPane {
	private EventPane eventPane;
	
	private UICheckBox isUseToolBarCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Use_ToolBar"));
	private UIButton editToolBarButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Edit"));
	private UILabel showListenersLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Editing_Listeners") + ":");
	private UICheckBox sortCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Sort_Sort"));
	private UICheckBox conditonFilterBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Selection_Filter"));
	private UICheckBox listFilterBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_List_Filter"));
	
	public ViewToolBarPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel allPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();

		this.add(allPanel, BorderLayout.CENTER);
        JPanel northPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        allPanel.add(northPane, BorderLayout.NORTH);
        editToolBarButton.addActionListener(editBtnListener);
		isUseToolBarCheckBox.setSelected(true);
		isUseToolBarCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editToolBarButton.setEnabled(isUseToolBarCheckBox.isSelected());
			}
		});
		sortCheckBox.setSelected(true);
		conditonFilterBox.setSelected(true);
		listFilterBox.setSelected(true);
        northPane.add(GUICoreUtils.createFlowPane(new Component[]{sortCheckBox, conditonFilterBox, listFilterBox}, FlowLayout.LEFT, 6));
		northPane.add(GUICoreUtils.createFlowPane(new Component[] {isUseToolBarCheckBox, editToolBarButton}, FlowLayout.LEFT));
        northPane.add(GUICoreUtils.createFlowPane(showListenersLabel, FlowLayout.LEFT));
		eventPane = new EventPane(new WebView().supportedEvents());
		JPanel center = FRGUIPaneFactory.createBorderLayout_S_Pane();
		center.add(eventPane, BorderLayout.CENTER);
		allPanel.add(center, BorderLayout.CENTER);
		//wei : 默认没config.xml的情况下，就有默认工具栏
		ToolBarManager toolBarManager = ToolBarManager.createDefaultViewToolBar();
		toolBarManager.setToolBarLocation(Location.createTopEmbedLocation());
		this.toolBarManagers = new ToolBarManager[] {toolBarManager};
	}
	
	@Override
	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		
		this.eventPane.setEnabled(isEnabled);
		
		this.isUseToolBarCheckBox.setEnabled(isEnabled);
		this.sortCheckBox.setEnabled(isEnabled);
		this.conditonFilterBox.setEnabled(isEnabled);
		this.listFilterBox.setEnabled(isEnabled);
		this.editToolBarButton.setEnabled(isEnabled && isUseToolBarCheckBox.isSelected());
		this.showListenersLabel.setEnabled(isEnabled);
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Data_Analysis_Settings");
	}

	@Override
	public void populateBean(WebContent webView) {
		if (webView == null) {
			webView = new WebView();
		}

		if (webView.isUseToolBar()) {
			this.toolBarManagers = webView.getToolBarManagers();
			this.isUseToolBarCheckBox.setSelected(true);
		} else {
			this.isUseToolBarCheckBox.setSelected(false);
			editToolBarButton.setEnabled(false);
		}
		WebView wv = (WebView) webView;
		this.listFilterBox.setSelected(wv.isListFuncCheck());
		this.conditonFilterBox.setSelected(wv.isConditionFuncCheck());
		this.sortCheckBox.setSelected(wv.isSortFuncCheck());
		if (webView.getListenerSize() != 0) {
			List<Listener> list = new ArrayList<Listener>();
			for (int i = 0; i < webView.getListenerSize(); i++) {
				list.add(webView.getListener(i));
			}
			eventPane.populate(list);
		}
	}

	@Override
	public WebView updateBean() {
		WebView webView = new WebView();
		if (isUseToolBarCheckBox.isSelected()) {
			webView.setToolBarManagers(toolBarManagers);
		} else {
			webView.setToolBarManagers(new ToolBarManager[0]);
		}
		webView.setSortFuncCheck(sortCheckBox.isSelected());
		webView.setConditionFuncCheck(conditonFilterBox.isSelected());
		webView.setListFuncCheck(listFilterBox.isSelected());
		for (int i = 0; i < eventPane.update().size(); i++) {
			Listener listener = eventPane.update().get(i);
			webView.addListener(listener);
		}
		return webView;
	}

    /**
     * 编辑服务器工具栏窗格
     */
	@Override
	public void editServerToolBarPane() {
		final ViewToolBarPane serverPageToolBarPane = new ViewToolBarPane();
		ReportWebAttr reportWebAttr = ((ReportWebAttr) ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
		if (reportWebAttr != null) {
			serverPageToolBarPane.populateBean(reportWebAttr.getWebView());
		}
		BasicDialog serverPageDialog = serverPageToolBarPane.showWindow(SwingUtilities.getWindowAncestor(ViewToolBarPane.this));
		serverPageDialog.addDialogActionListener(new DialogActionAdapter() {
			
			@Override
			public void doOk() {
				Configurations.update(new Worker() {
					@Override
					public void run() {
						ReportWebAttr reportWebAttr = ((ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
						reportWebAttr.setWebView(serverPageToolBarPane.updateBean());
					}

					@Override
					public Class<? extends Configuration>[] targets() {
						return new Class[]{ReportWebAttr.class};
					}
				});

			}
		});
		serverPageDialog.setVisible(true);
		
	}


    @Override
    protected WidgetOption[] getToolBarInstance() {
        return ReportWebWidgetConstants.getViewToolBarInstance();
    }
}
