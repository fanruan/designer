package com.fr.design.javascript;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.fun.JavaScriptActionProvider;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.form.event.Listener;
import com.fr.general.Inter;
import com.fr.js.JavaScript;

public class ListenerEditPane extends BasicBeanPane<Listener> {
	private UITextField nameText;
	private UIComboBox styleBox;
	private CardLayout card;
	private List<FurtherBasicBeanPane<? extends JavaScript>> cards;
	private JPanel hyperlinkPane;
	
	private JavaScriptImplPane javaScriptPane;
	private FormSubmitJavaScriptPane formSubmitScriptPane;
	private Commit2DBJavaScriptPane commit2DBJavaScriptPane;
	// 自定义事件
	private CustomActionPane customActionPane;
	// 发送邮件
	private EmailPane emailPane;
	
	private static final String JS = Inter.getLocText("FR-Designer_JavaScript");
	private static final String FORMSUBMIT = Inter.getLocText("FR-Designer_JavaScript_Form_Submit");
	private static final String DBCOMMIT = Inter.getLocText("FR-Designer_JavaScript_Commit_to_Database");
	private static final String CUSTOMACTION= Inter.getLocText(new String[]{"FR-Designer_JavaScript_Custom", "FR-Designer_RWA_Submit"});
	private static final String EMAIL = Inter.getLocText("FR-Designer_Email_sentEmail");
	
	private Listener listener;
	
	public ListenerEditPane() {
		this.initComponents(new String[0]);
	}

	public ListenerEditPane(String[] defaultArgs) {
		this.initComponents(defaultArgs);
	}

	/**
	 * 初始化各个组件
	 * @param defaultArgs 初始化参数
	 */
	public void initComponents(String[] defaultArgs) {
		cards = new ArrayList<FurtherBasicBeanPane<? extends JavaScript>>();
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel namePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		nameText = new UITextField(8);
		nameText.setEditable(false);
		namePane.add(nameText, BorderLayout.WEST);
		String[] style = {JS, DBCOMMIT, CUSTOMACTION,EMAIL};
		styleBox = new UIComboBox(style);
		namePane.add(styleBox);
		namePane = GUICoreUtils.createFlowPane(new Component[]{new UILabel("  " + Inter.getLocText("FR-Designer_Event_Name") + ":"), nameText, new UILabel("    " + Inter.getLocText("FR-Designer_Event_Type") + ":"), styleBox}, FlowLayout.LEFT);
		namePane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("FR-Designer_Event_Name_Type")));
		this.add(namePane, BorderLayout.NORTH);
		card = new CardLayout();
		hyperlinkPane = FRGUIPaneFactory.createCardLayout_S_Pane();
		hyperlinkPane.setLayout(card);
		javaScriptPane = new JavaScriptImplPane(defaultArgs);
		hyperlinkPane.add(JS, javaScriptPane);
		// 提交入库
        List dbmaniList = new ArrayList();
        dbmaniList.add(autoCreateDBManipulationPane());
		commit2DBJavaScriptPane = new Commit2DBJavaScriptPane(JavaScriptActionPane.defaultJavaScriptActionPane,
                dbmaniList);
		hyperlinkPane.add(DBCOMMIT, commit2DBJavaScriptPane);
		// 自定义事件
		customActionPane = new CustomActionPane();
		hyperlinkPane.add(CUSTOMACTION, customActionPane);
		// 发送邮件
		emailPane = new EmailPane();
		hyperlinkPane.add(EMAIL,emailPane);
		cards.add(javaScriptPane);
		cards.add(commit2DBJavaScriptPane);
		cards.add(customActionPane);
		cards.add(emailPane);
		//其他事件
		addOtherEvent();
		hyperlinkPane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("FR-Designer_JavaScript_Set")));
		this.add(hyperlinkPane);
		styleBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				card.show(hyperlinkPane, styleBox.getSelectedItem().toString());
			}
		});
	}

	private void addOtherEvent(){
		Set<JavaScriptActionProvider> javaScriptActionProviders = ExtraDesignClassManager.getInstance().getArray(JavaScriptActionProvider.XML_TAG);
		if (javaScriptActionProviders != null) {
			for (JavaScriptActionProvider jsp : javaScriptActionProviders) {
				FurtherBasicBeanPane pane = jsp.getJavaScriptActionPane();
				String title = pane.title4PopupWindow();
				styleBox.addItem(title);
				hyperlinkPane.add(title, pane);
				cards.add(pane);
			}
		}
	}

    /**
     *  根据有无单元格创建 DBManipulationPane
     * @return   有单元格。有智能添加单元格等按钮，返回 SmartInsertDBManipulationPane
     */
	private DBManipulationPane autoCreateDBManipulationPane() {
		JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
		return jTemplate.createDBManipulationPane();
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Designer_Event_Set");
	}
	
	@Override
	public void populateBean(Listener listener) {
		this.listener = listener;
		if (this.listener == null) {
			this.listener = new Listener();
		}
		this.nameText.setText(listener.getEventName());
		JavaScript js = listener.getAction();
		for (int i = 0; i < this.cards.size(); i++) {
			FurtherBasicBeanPane pane = cards.get(i);
			if (pane.accept(js)) {
				styleBox.setSelectedItem(pane.title4PopupWindow());
				card.show(hyperlinkPane, pane.title4PopupWindow());
				pane.populateBean(js);
				return;
			}
		}
	}

	public void checkValid() throws Exception{
		this.cards.get(this.styleBox.getSelectedIndex()).checkValid();
	}

	@Override
	public Listener updateBean(){
		this.listener.setEventName(this.nameText.getText());
		FurtherBasicBeanPane<? extends JavaScript> pane = this.cards.get(this.styleBox.getSelectedIndex());
		this.listener.setAction(pane.updateBean());
		return this.listener;
	}
}