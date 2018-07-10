package com.fr.design.webattr;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.JPanel;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.gui.core.WidgetOption;
import com.fr.general.Inter;
import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebPage;
import com.fr.web.attr.ReportWebAttr;

public class PageWebSettingPane extends WebSettingPane<WebPage> {
	private UIRadioButton centerRadioButton;
	private UIRadioButton leftRadioButton;
	private UICheckBox isShowAsImageBox;
	private UICheckBox isAutoScaleBox;
	private UICheckBox isTDHeavyBox;

	public PageWebSettingPane() {
		super();
	}

	@Override
	protected JPanel createOtherSetPane() {
		centerRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Center_Display"));
		leftRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_Left_Display"));
		ButtonGroup buttonGroup = new ButtonGroup();
		leftRadioButton.setSelected(true);
		buttonGroup.add(centerRadioButton);
		buttonGroup.add(leftRadioButton);
		JPanel buttonpane = new JPanel(FRGUIPaneFactory.createBoxFlowLayout());
		buttonpane.add(centerRadioButton);
		buttonpane.add(leftRadioButton);
		isShowAsImageBox = new UICheckBox(Inter.getLocText("FR-Designer_Is_Paint_Page"));
		isAutoScaleBox = new UICheckBox(Inter.getLocText("FR-Designer_IS_Auto_Scale"));
		isTDHeavyBox = new UICheckBox(Inter.getLocText("FR-Designer_IS_TD_HEAVY_EXPORT"), false);
		double p = TableLayout.PREFERRED;
		double[] columnSize = { p,p,p};
		double[] rowSize = { p, p,p,p };
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Report_Show_Location") + ":", UILabel.RIGHT), buttonpane,null},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_PageSetup_Page") + ":", UILabel.RIGHT), isShowAsImageBox, isAutoScaleBox},
                new Component[]{null, isTDHeavyBox, null}
        };

        return  TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
	}


	@Override
	protected void checkEnabled(boolean isSelected) {
		super.checkEnabled(isSelected);
		centerRadioButton.setEnabled(isSelected);
		leftRadioButton.setEnabled(isSelected);
		isShowAsImageBox.setEnabled(isSelected);
		isAutoScaleBox.setEnabled(isSelected);
		isTDHeavyBox.setEnabled(isSelected);
	}
	protected void setDefault(){
		super.setDefault();
		leftRadioButton.setSelected(true);
		isShowAsImageBox.setSelected(false);
		isAutoScaleBox.setSelected(false);
		isTDHeavyBox.setSelected(false);
	}
	@Override
	protected void populateSubWebSettingrBean(WebPage webPage) {
		if (webPage == null) {
			webPage = new WebPage();
		}
		if (webPage.isViewAtCenter()) {
			centerRadioButton.setSelected(true);
		} else {
			leftRadioButton.setSelected(true);
		}

		isShowAsImageBox.setSelected(webPage.isShowAsImage());
		isAutoScaleBox.setSelected(webPage.isAutoScaleWhenEmbeddedInIframe());
		isTDHeavyBox.setSelected(webPage.isTDHeavy());
	}

	@Override
	protected WebPage updateSubWebSettingBean() {
		WebPage webPage = new WebPage();
		webPage.setViewAtCenter(centerRadioButton.isSelected());
		webPage.setShowAsImage(isShowAsImageBox.isSelected());
		webPage.setAutoScaleWhenEmbeddedInIframe(isAutoScaleBox.isSelected());
		webPage.setTDHeavy(isTDHeavyBox.isSelected());
		return webPage;
	}

	@Override
	protected ToolBarManager getDefaultToolBarManager() {
		return ToolBarManager.createDefaultToolBar();
	}

	@Override
	protected WidgetOption[] getToolBarInstance() {
		List<WidgetOption> defaultOptions = Arrays.asList(ReportWebWidgetConstants.getPageToolBarInstance());
		List<WidgetOption> extraOptions = Arrays.asList(ExtraDesignClassManager.getInstance().getWebWidgetOptions());
		List<WidgetOption> options = new ArrayList<WidgetOption>();
		options.addAll(defaultOptions);
		options.addAll(extraOptions);
		return options.toArray(new WidgetOption[options.size()]);
	}

	@Override
	protected WebPage getWebContent(ReportWebAttr reportWebAttr) {
		return reportWebAttr == null ? null :reportWebAttr.getWebPage();
	}

	@Override
	protected String[] getEventNames() {
		return new WebPage().supportedEvents();
	}

	@Override
	protected void setWebContent(ReportWebAttr reportWebAttr,WebPage webContent) {
		reportWebAttr.setWebPage(webContent);
	}
}