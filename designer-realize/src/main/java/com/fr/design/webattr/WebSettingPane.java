package com.fr.design.webattr;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.core.WidgetOption;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.event.Listener;

import com.fr.report.web.ToolBarManager;
import com.fr.report.web.WebContent;
import com.fr.stable.StringUtils;
import com.fr.web.attr.ReportWebAttr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public abstract class WebSettingPane<T extends WebContent> extends BasicBeanPane<ReportWebAttr> {
	private static final String[] CHOOSEITEM = new String[] {
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_I_Want_To_Set_Single"),
            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Using_Server_Report_View_Settings")
    };

	private EventPane eventPane;
	private ToolBarDragPane dragToolBarPane;
	private UIComboBox choseComboBox;

	private static final int SINGLE_SET = 0;
	private static final int SERVER_SET = 1;
    private static final int ZERO = 0;
    private static final long LONGZERO = 0L;

	public WebSettingPane() {
		JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
		choseComboBox = new UIComboBox(CHOOSEITEM);
		choseComboBox.addItemListener(itemListener);
		buttonPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Blow_Set") + ":"));
		buttonPane.add(choseComboBox);
		dragToolBarPane = new ToolBarDragPane();
		dragToolBarPane.setDefaultToolBar(getDefaultToolBarManager(), getToolBarInstance());
		JPanel eventpanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		eventpanel.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Event_Set") + ':'), BorderLayout.NORTH);
		eventPane = new EventPane(getEventNames());
		eventpanel.add(eventPane, BorderLayout.CENTER);

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		double[] columnSize = {f};

		JPanel othersetpane = createOtherSetPane();
        JPanel panel ;
		if (othersetpane != null) {

            Component[][] components = new Component[][]{
                 new Component[]{buttonPane},
                 new Component[]{othersetpane},
                 new Component[]{dragToolBarPane},
                 new Component[]{eventpanel}
            };
            double[] rowSize1 = { p,p,p,f };

          panel = TableLayoutHelper.createTableLayoutPane(components,rowSize1,columnSize);

		} else {
            Component[][] components = new Component[][]{
                    new Component[]{buttonPane},
                    new Component[]{dragToolBarPane},
                    new Component[]{eventpanel}
            };
            double[] rowSize2 = { p,p,f };

            panel = TableLayoutHelper.createTableLayoutPane(components,rowSize2,columnSize);
		}

        this.setLayout(new BorderLayout());

		UIScrollPane scrollPane = new UIScrollPane(panel);
        this.add(scrollPane, BorderLayout.CENTER);
	}

	ItemListener itemListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				if (choseComboBox.getSelectedIndex() == 0) {
					checkEnabled(true);
					setDefault();
				} else {
					populateServerSettings();
					checkEnabled(false);
				}
			}
		}
	};

	protected void checkEnabled(boolean isSelected) {
		dragToolBarPane.setAllEnabled(isSelected);
		eventPane.setEnabled(isSelected);
	}

	protected void setDefault() {
		dragToolBarPane.setCheckBoxSelected(true);
		dragToolBarPane.populateBean(new ToolBarManager[0]);
		eventPane.populate(new ArrayList<Listener>());
	}

	protected abstract JPanel createOtherSetPane();

	protected abstract String[] getEventNames();

	@Override
	public void populateBean(ReportWebAttr reportWebAttr) {
		if (reportWebAttr == null || this.getWebContent(reportWebAttr) == null) {// 如果是空值就说明采用服务器配置了
			choseComboBox.removeItemListener(itemListener);
			choseComboBox.setSelectedIndex(SERVER_SET);
			choseComboBox.addItemListener(itemListener);
			populateServerSettings();
			checkEnabled(false);
			return;
		}
		// 模板设置
		choseComboBox.removeItemListener(itemListener);
		choseComboBox.setSelectedIndex(SINGLE_SET);
		choseComboBox.addItemListener(itemListener);
		checkEnabled(true);
		T webContent = this.getWebContent(reportWebAttr);
		if (webContent.getListenerSize() != 0) {
			List<Listener> list = new ArrayList<Listener>();
			for (int i = 0; i < webContent.getListenerSize(); i++) {
				list.add(webContent.getListener(i));
			}
			eventPane.populate(list);
		}
		dragToolBarPane.setCheckBoxSelected(webContent.isUseToolBar());
		dragToolBarPane.populateBean(webContent.getToolBarManagers());
		populateSubWebSettingrBean(webContent);

	}

	public void update(ReportWebAttr reportWebAttr) {
		if (this.choseComboBox.getSelectedIndex() == SERVER_SET) {
			setWebContent(reportWebAttr, null);
			reportWebAttr =  is_Null_ReportWebAttr(reportWebAttr) ? null : reportWebAttr;
		} else {
			reportWebAttr = TemplateupdateBean(reportWebAttr);
		}
	}

	@Override
	public ReportWebAttr updateBean() {
		return null;
	}

	private ReportWebAttr TemplateupdateBean(ReportWebAttr reportWebAttr) {
		T webContent = updateSubWebSettingBean();
		ToolBarManager[] toolBarManagers = dragToolBarPane.updateBean();
		webContent.setToolBarManagers(toolBarManagers);
		for (int i = 0; i < eventPane.update().size(); i++) {
			Listener listener = eventPane.update().get(i);
			webContent.addListener(listener);
		}
		setWebContent(reportWebAttr, webContent);
		return reportWebAttr;
	}

	protected abstract T getWebContent(ReportWebAttr reportWebAttr);

	protected abstract void populateSubWebSettingrBean(T ob);

	protected abstract T updateSubWebSettingBean();

	protected abstract void setWebContent(ReportWebAttr reportWebAttr, T ob);

	protected abstract WidgetOption[] getToolBarInstance();

	protected abstract ToolBarManager getDefaultToolBarManager();

	@Override
	protected String title4PopupWindow() {
		return "WebSetting";
	}

	private void populateServerSettings() {
		ReportWebAttr reportWebAttr = ReportWebAttr.getInstance();
		T webContent = this.getWebContent(reportWebAttr);
		if(webContent == null){
			return;
		}
		ToolBarManager[] toolBarManagers = webContent.getToolBarManagers();
		if (webContent.getListenerSize() != 0) {
			List<Listener> list = new ArrayList<Listener>();
			for (int i = 0; i < webContent.getListenerSize(); i++) {
				list.add(webContent.getListener(i));
			}
			eventPane.populate(list);
		}
		dragToolBarPane.setCheckBoxSelected(webContent.isUseToolBar());
		dragToolBarPane.populateBean(toolBarManagers);
		populateSubWebSettingrBean(webContent);
	}

    /**
     * web属性是否为空
     *
     * @param reportWebAttr 模板web属性
     *
     * @return 模板web属性书否为空
     */
	public static boolean is_Null_ReportWebAttr(ReportWebAttr reportWebAttr) {
		if (reportWebAttr == null) {
			return true;
		}
		return reportWebAttr.getBackground() == null
                && reportWebAttr.getCacheValidateTime() == LONGZERO
                && reportWebAttr.getCSSImportCount() == ZERO
				&& reportWebAttr.getJSImportCount() == ZERO
                && reportWebAttr.getPrinter() == null
                && reportWebAttr.getWebPage() == null
                && reportWebAttr.getWebView() == null
				&& reportWebAttr.getWebWrite() == null
                && StringUtils.isEmpty(reportWebAttr.getTitle());
	}
}
