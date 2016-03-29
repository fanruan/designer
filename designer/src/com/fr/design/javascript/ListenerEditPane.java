package com.fr.design.javascript;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.write.submit.DBManipulationPane;
import com.fr.form.event.Listener;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.js.Commit2DBJavaScript;
import com.fr.js.CustomActionJavaScript;
import com.fr.js.EmailJavaScript;
import com.fr.js.FormSubmitJavaScript;
import com.fr.js.JavaScript;
import com.fr.js.JavaScriptImpl;

public class ListenerEditPane extends BasicBeanPane<Listener> {
	private UITextField nameText;
	private UIComboBox styleBox;
	private CardLayout card;
	private JPanel hyperlinkPane;
	
	private JavaScriptImplPane javaScriptPane;
	private FormSubmitJavaScriptPane formSubmitScriptPane;
	private Commit2DBJavaScriptPane commit2DBJavaScriptPane;
	// 自定义事件
	private CustomActionPane customActionPane;
	// 发送邮件
	private EmailPane emailPane;
	
	private static final String JS = Inter.getLocText("JavaScript");
	private static final String FORMSUBMIT = Inter.getLocText("JavaScript-Form_Submit");
	private static final String DBCOMMIT = Inter.getLocText("JavaScript-Commit_to_Database");
	private static final String CUSTOMACTION= Inter.getLocText(new String[]{"Custom", "RWA-Submit"});
	private static final String EMAIL = Inter.getLocText("Email_sentEmail");
	
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
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel namePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		nameText = new UITextField(8);
		nameText.setEditable(false);
		namePane.add(nameText, BorderLayout.WEST);
		String[] style = {JS, DBCOMMIT, CUSTOMACTION,EMAIL};
		styleBox = new UIComboBox(style);
		namePane.add(styleBox);
		namePane = GUICoreUtils.createFlowPane(new Component[]{new UILabel("  " + Inter.getLocText("Event_Name") + ":"), nameText, new UILabel("    " + Inter.getLocText("Event_Type") + ":"), styleBox}, FlowLayout.LEFT);
		namePane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Event_Name_Type")));
		this.add(namePane, BorderLayout.NORTH);
		
		card = new CardLayout();
		hyperlinkPane = FRGUIPaneFactory.createCardLayout_S_Pane();
		hyperlinkPane.setLayout(card);
		// js
		javaScriptPane = new JavaScriptImplPane(defaultArgs);
		hyperlinkPane.add(JS, javaScriptPane);
//		formSubmitScriptPane = new FormSubmitJavaScriptPane(JavaScriptActionPane.defaultJavaScriptActionPane
//				.createCallButton());
//		hyperlinkPane.add(FORMSUBMIT, formSubmitScriptPane);
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
	
		hyperlinkPane.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("JavaScript_Set")));
		this.add(hyperlinkPane);
		
		styleBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				card.show(hyperlinkPane, styleBox.getSelectedItem().toString());
			}
		});
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
		return Inter.getLocText("Event_Set");
	}
	
	@Override
	public void populateBean(Listener listener) {
		this.listener = listener;
		if (this.listener == null) {
			this.listener = new Listener();
		}
		
		this.nameText.setText(listener.getEventName());
		
		JavaScript js = listener.getAction();
		if (js instanceof JavaScriptImpl) {
			styleBox.setSelectedItem(JS);
			card.show(hyperlinkPane, JS);
			javaScriptPane.populateBean((JavaScriptImpl)js);
		} else if (js instanceof FormSubmitJavaScript){
			styleBox.setSelectedItem(FORMSUBMIT);
			card.show(hyperlinkPane, FORMSUBMIT);
			formSubmitScriptPane.populateBean((FormSubmitJavaScript)js);
		} else if (js instanceof Commit2DBJavaScript) {
			styleBox.setSelectedItem(DBCOMMIT);
			card.show(hyperlinkPane, DBCOMMIT);
			commit2DBJavaScriptPane.populateBean((Commit2DBJavaScript)js);
		}  else if (js instanceof EmailJavaScript){
			styleBox.setSelectedItem(EMAIL);
			card.show(hyperlinkPane, EMAIL);
			emailPane.populateBean((EmailJavaScript)js);
		} else if (js instanceof CustomActionJavaScript){
			styleBox.setSelectedItem(CUSTOMACTION);
			card.show(hyperlinkPane, CUSTOMACTION);
			customActionPane.populateBean((CustomActionJavaScript) js);
		}
	}
	
	@Override
	public Listener updateBean(){
		this.listener.setEventName(this.nameText.getText());
		if (ComparatorUtils.equals(styleBox.getSelectedItem(), JS)) {
			this.listener.setAction(javaScriptPane.updateBean());
		} else if (ComparatorUtils.equals(styleBox.getSelectedItem(), FORMSUBMIT)) {
			this.listener.setAction(formSubmitScriptPane.updateBean());
		} else if (ComparatorUtils.equals(styleBox.getSelectedItem(), DBCOMMIT)) {
			this.listener.setAction(commit2DBJavaScriptPane.updateBean());
		} else if (ComparatorUtils.equals(styleBox.getSelectedItem(),EMAIL)){
			this.listener.setAction(emailPane.updateBean());
		} else if (ComparatorUtils.equals(styleBox.getSelectedItem(), CUSTOMACTION)){
			this.listener.setAction(customActionPane.updateBean());
		}
		return this.listener;
	}
}