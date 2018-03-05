package com.fr.design.javascript;

import com.fr.config.EmailConfig;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.js.EmailJavaScript;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class EmailPane extends FurtherBasicBeanPane<EmailJavaScript> {

	protected UILabel tipsPane1;
	protected UITextField maitoEditor;
	protected UITextField ccEditor;
	protected UITextField bccEditor;
	protected UITextField titleEditor;
	protected UILabel tipsPane2;
	protected JPanel centerPane;
	protected UICheckBox showTplContent;
	private JTextArea mainTextEditor;
	
	public EmailPane() {
		this.initComponents();
	}
	
	private void initComponents() {
		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
		tipsPane1 = new UILabel();
		tipsPane1.setHorizontalAlignment(SwingConstants.RIGHT);
		tipsPane1.setForeground(Color.pink);
		tipsPane2 = new UILabel(Inter.getLocText("FR-Designer_EmailPane-tips"));
		
		UILabel mainTextLabel = new UILabel(Inter.getLocText("FR-Designer_EmailPane-mailContent") + ":");
		mainTextLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		mainTextLabel.setVerticalAlignment(SwingConstants.TOP);
		JScrollPane scrollPane = new JScrollPane(mainTextEditor = new JTextArea());
		scrollPane.setBorder(null);
		double fill = TableLayout.FILL;
		double preferred = TableLayout.PREFERRED;
		initCenterPane(mainTextLabel, scrollPane, fill, preferred);
		this.add(centerPane, BorderLayout.CENTER);
		mainTextEditor.setAutoscrolls(true);
		checkEmailConfig(EmailConfig.getInstance().isEmailConfigValid());
	}
    
    /**
     * 由于发邮件pane中的showTplContent单选框和图表超链itemNameTextField文本框
     * 在一些情况下不使用，所以默认centerPane为都有，其他情况下子类实现
     */
    protected void initCenterPane(UILabel mainTextLabel, JScrollPane scrollPane, double fill, double preferred) {
    	double[] rowSize = { preferred, preferred, preferred, preferred, preferred, fill, preferred, preferred, preferred };
		double[] columnSize = { preferred, fill};
		showTplContent = new UICheckBox(Inter.getLocText("Email-Can_Preview_Report_Content"));
    	centerPane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                {new UILabel(), tipsPane1},
                createLinePane(Inter.getLocText("HJS-Mail_to"), maitoEditor = new UITextField()),
                createLinePane(Inter.getLocText("HJS-CC_to"), ccEditor = new UITextField()),
                createLinePane(Inter.getLocText("FR-Designer_EmailPane-BCC"), bccEditor = new UITextField()),
                createLinePane(Inter.getLocText("FR-Designer_EmailPane-mailSubject"), titleEditor = new UITextField()),
                {mainTextLabel, scrollPane},
                {new UILabel(), showTplContent},
                {new UILabel(), tipsPane2}},rowSize, columnSize, 8);
    }

	protected JComponent[] createLinePane(String string, JTextComponent textComp) {
		UILabel label = new UILabel(string + ":");
		label.setPreferredSize(new Dimension(70, label.getPreferredSize().height));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		return new JComponent[] { label, textComp };
	}

	protected void checkEmailConfig(boolean valid) {
		tipsPane1.setText(valid ? StringUtils.BLANK : Inter.getLocText("FR-Designer_EmailPane-warnings"));
		centerPane.setEnabled(valid);
		mainTextEditor.setEnabled(valid);
		mainTextEditor.setBackground(valid ? Color.WHITE : UIConstants.DEFAULT_BG_RULER);
		mainTextEditor.setBorder(BorderFactory.createLineBorder(valid ? new Color(128, 152, 186) : UIConstants.TITLED_BORDER_COLOR));
		maitoEditor.setEnabled(valid);
		ccEditor.setEnabled(valid);
		bccEditor.setEnabled(valid);
		titleEditor.setEnabled(valid);
		if (showTplContent != null) {
			showTplContent.setEnabled(valid);
		}
	}
	

    /**
     * 重置各部分
     */
	public void reset() {
		populateBean(null);
	}
	
	@Override
	public void populateBean(EmailJavaScript ob) {
		maitoEditor.setText(ob == null ? null : ob.getMailTo());
		ccEditor.setText(ob == null ? null : ob.getCC());
		bccEditor.setText(ob == null ? null : ob.getBCC());
		titleEditor.setText(ob == null ? null : ob.getTitle());
		mainTextEditor.setText(ob == null ? null : ob.getMainText());
		if (showTplContent != null) {
			showTplContent.setSelected(ob ==null ? false: ob.isShowTplContent());
		}
		checkEmailConfig(EmailConfig.getInstance().isEmailConfigValid());
	}

	@Override
	public EmailJavaScript updateBean() {
		EmailJavaScript js = new EmailJavaScript();
		updateBean(js);
		return js;
	}
	
	public void updateBean(EmailJavaScript email) {
		email.setMailTo(maitoEditor.getText());
		email.setCC(ccEditor.getText());
		email.setBCC(bccEditor.getText());
		email.setTitle(titleEditor.getText());
		email.setMainText(mainTextEditor.getText());
		if (showTplContent != null) {
			email.setShowTplContent(showTplContent.isSelected());
		}
	}

    /**
     * 界面的标题
     * @return 标题字串
     */
	public String title4PopupWindow() {
		return Inter.getLocText("FR-Designer_Email_sentEmail");
	}

    /**
     * 是否是本类对象
     * @param ob 需要判断的对象
     * @return 是否是本类对象
     */
	public boolean accept(Object ob) {
		return ob instanceof EmailJavaScript;
	}

}