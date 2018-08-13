package com.fr.design.webattr;

import com.fr.base.ConfigManager;
import com.fr.config.Configuration;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.event.Listener;

import com.fr.report.web.Location;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.report.web.WebPage;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PageToolBarPane extends AbstractEditToolBarPane {
	
	private UIRadioButton centerRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Center_Display"));
	private UIRadioButton leftRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Left_Display"));
	
	private UICheckBox isUseToolBarCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Use_ToolBar"));
	private UICheckBox isShowAsImageBox;
	private UICheckBox isAutoScaleBox;
	private UICheckBox isTDHeavyBox;
	private EventPane eventPane;
	
	private UILabel showLocationLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Show_Location") + ":");
	private UILabel showListenersLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Editing_Listeners") + ":");
 	
	private UIButton editToolBarButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit"));
	
	public PageToolBarPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel allPanel = FRGUIPaneFactory.createBorderLayout_L_Pane();
		this.add(allPanel, BorderLayout.CENTER);
		JPanel north = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
		allPanel.add(north, BorderLayout.NORTH);
		ButtonGroup buttonGroup = new ButtonGroup();
		leftRadioButton.setSelected(true);
		buttonGroup.add(centerRadioButton);
		buttonGroup.add(leftRadioButton);
		north.add(GUICoreUtils.createFlowPane(new Component[] {
				showLocationLabel, new UILabel(" "), centerRadioButton, leftRadioButton}, FlowLayout.LEFT));
		isShowAsImageBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Is_Paint_Page"));
		north.add(GUICoreUtils.createFlowPane(isShowAsImageBox, FlowLayout.LEFT));
		isAutoScaleBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_IS_Auto_Scale"));
		north.add(GUICoreUtils.createFlowPane(isAutoScaleBox, FlowLayout.LEFT));
		isTDHeavyBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_IS_TD_HEAVY_EXPORT"), false);
		north.add(GUICoreUtils.createFlowPane(isTDHeavyBox, FlowLayout.LEFT));

		editToolBarButton.addActionListener(editBtnListener);
		isUseToolBarCheckBox.setSelected(true);
		isUseToolBarCheckBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				editToolBarButton.setEnabled(isUseToolBarCheckBox.isSelected());
			}
		});
		north.add(GUICoreUtils.createFlowPane(new Component[] {isUseToolBarCheckBox, editToolBarButton}, FlowLayout.LEFT));
        north.add(GUICoreUtils.createFlowPane(showListenersLabel, FlowLayout.LEFT));
		eventPane = new EventPane(new WebPage().supportedEvents());	
		
		JPanel center = FRGUIPaneFactory.createBorderLayout_S_Pane();
		center.add(eventPane, BorderLayout.CENTER);
		allPanel.add(center, BorderLayout.CENTER);
		//wei : 默认没config.xml的情况下，就有默认工具栏
		ToolBarManager toolBarManager = ToolBarManager.createDefaultToolBar();
		toolBarManager.setToolBarLocation(Location.createTopEmbedLocation());
		this.toolBarManagers = new ToolBarManager[] {toolBarManager};
	}
	
	@Override
	protected WidgetOption[] getToolBarInstance() {
		return ReportWebWidgetConstants.getPageToolBarInstance();
	}
	
	@Override
	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		this.centerRadioButton.setEnabled(isEnabled);
		this.eventPane.setEnabled(isEnabled);
		this.isTDHeavyBox.setEnabled(isEnabled);
		this.isAutoScaleBox.setEnabled(isEnabled);
		this.isShowAsImageBox.setEnabled(isEnabled);
		this.leftRadioButton.setEnabled(isEnabled);
		this.isUseToolBarCheckBox.setEnabled(isEnabled);
		this.editToolBarButton.setEnabled(isEnabled && isUseToolBarCheckBox.isSelected());
		this.showLocationLabel.setEnabled(isEnabled);
		this.showListenersLabel.setEnabled(isEnabled);
	}
	
	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_WEB_Pagination_Setting");
	}

	@Override
	public void populateBean(WebContent webContent) {
		if (webContent == null) {
			webContent = new WebPage();
		}
        WebPage webPage = (WebPage)webContent;
		if (webPage.isViewAtCenter()) {
			centerRadioButton.setSelected(true);
		} else {
			leftRadioButton.setSelected(true);
		}
		
		isShowAsImageBox.setSelected(webPage.isShowAsImage());
		isAutoScaleBox.setSelected(webPage.isAutoScaleWhenEmbeddedInIframe());
		isTDHeavyBox.setSelected(webPage.isTDHeavy());
		if (webPage.isUseToolBar()) {
			this.toolBarManagers = webPage.getToolBarManagers();
			this.isUseToolBarCheckBox.setSelected(true);
		} else {
			this.isUseToolBarCheckBox.setSelected(false);
			editToolBarButton.setEnabled(false);
		}
		
		if (webPage.getListenerSize() != 0) {
			List<Listener> list = new ArrayList<Listener>();
			for (int i = 0; i < webPage.getListenerSize(); i++) {
				list.add(webPage.getListener(i));
			}
			eventPane.populate(list);	
		}
	}

	@Override
	public WebPage updateBean() {
		WebPage webPage = new WebPage();
		if (isUseToolBarCheckBox.isSelected()) {
			webPage.setToolBarManagers(toolBarManagers);
		}else {
			webPage.setToolBarManagers(new ToolBarManager[0]);
		}
		for (int i = 0; i < eventPane.update().size(); i++) {
			Listener listener = eventPane.update().get(i);
			webPage.addListener(listener);
		}
		webPage.setViewAtCenter(centerRadioButton.isSelected());
		webPage.setShowAsImage(isShowAsImageBox.isSelected());
		webPage.setAutoScaleWhenEmbeddedInIframe(isAutoScaleBox.isSelected());
		webPage.setTDHeavy(isTDHeavyBox.isSelected());
		return webPage;
	}

    /**
     *  编辑服务器工具栏窗格
     */
	@Override
	public void editServerToolBarPane() {
		final PageToolBarPane serverPageToolBarPane = new PageToolBarPane();
		ReportWebAttr reportWebAttr = ((ReportWebAttr) ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
		if (reportWebAttr != null) {
			serverPageToolBarPane.populateBean(reportWebAttr.getWebPage());
		}
		BasicDialog serverPageDialog = serverPageToolBarPane.showWindow(SwingUtilities.getWindowAncestor(PageToolBarPane.this));
		serverPageDialog.addDialogActionListener(new DialogActionAdapter() {
			
			@Override
			public void doOk() {
				Configurations.update(new Worker() {
					@Override
					public void run() {
						ReportWebAttr reportWebAttr = ((ReportWebAttr)ConfigManager.getProviderInstance().getGlobalAttribute(ReportWebAttr.class));
						reportWebAttr.setWebPage(serverPageToolBarPane.updateBean());
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
}
