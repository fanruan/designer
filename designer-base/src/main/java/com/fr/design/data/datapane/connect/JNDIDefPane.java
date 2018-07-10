package com.fr.design.data.datapane.connect;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.fr.data.impl.JNDIDatabaseConnection;
import com.fr.design.border.UITitledBorder;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.FRExplainLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.dialog.BasicPane;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

public class JNDIDefPane extends JPanel {
	private static Map<String, String> jndiMap = new HashMap<String, String>();

	static {
		jndiMap.put("weblogic.jndi.WLInitialContextFactory", "t3://localhost:7001");
		jndiMap.put("com.ibm.websphere.naming.WsnInitialContextFactory", "iiop://localhost:2809");
		jndiMap.put("org.jboss.naming.HttpNamingContextFactory", "http://jboss_server_address:8080/invoker/JNDIFactory");
		jndiMap.put("org.jnp.interfaces.NamingContextFactory", "localhost:1099");
		jndiMap.put("com.caucho.burlap.BurlapContextFactory", "http://localhost:8080/hello/burlap");
	}

	private UITextField jndiNameTextField;

	private UIComboBox JNDIFactoryComboBox;
	private ContextTextField PROVIDER_URL_TF = new ContextTextField(Context.PROVIDER_URL);
	private ContextTextField SECURITY_PRINCIPAL_TF = new ContextTextField(Context.SECURITY_PRINCIPAL);
	private ContextTextField SECURITY_CREDENTIALS_TF = new ContextTextField(Context.SECURITY_CREDENTIALS);

	private ContextTextField OBJECT_FACTORIES_TF = new ContextTextField(Context.OBJECT_FACTORIES);
	private ContextTextField STATE_FACTORIES_TF = new ContextTextField(Context.STATE_FACTORIES);
	private ContextTextField URL_PKG_PREFIXES_TF = new ContextTextField(Context.URL_PKG_PREFIXES);
	private ContextTextField DNS_URL_TF = new ContextTextField(Context.DNS_URL);
	private ContextTextField AUTHORITATIVE_TF = new ContextTextField(Context.AUTHORITATIVE);
	private ContextTextField BATCHSIZE_TF = new ContextTextField(Context.BATCHSIZE);
	private ContextTextField REFERRAL_TF = new ContextTextField(Context.REFERRAL);
	private ContextTextField SECURITY_PROTOCOL_TF = new ContextTextField(Context.SECURITY_PROTOCOL);
	private ContextTextField SECURITY_AUTHENTICATION_TF = new ContextTextField(Context.SECURITY_AUTHENTICATION);
	private ContextTextField LANGUAGE_TF = new ContextTextField(Context.LANGUAGE);
	private ContextTextField APPLET_TF = new ContextTextField(Context.APPLET);

	private JDialog otherAttrDialog;

	public JNDIDefPane() {
		this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
		this.setBorder(UITitledBorder.createBorderWithTitle("JNDI" + ":"));
		JPanel innerthis = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		this.add(innerthis);
		// NorthPane
		JPanel nContentPane = FRGUIPaneFactory.createNormalFlowInnerContainer_M_Pane();
		innerthis.add(nContentPane);
		nContentPane.add(new UILabel(Inter.getLocText("Datasource-JNDI_Name") + ":"));
		jndiNameTextField = new UITextField(20);
		nContentPane.add(jndiNameTextField, BorderLayout.NORTH);

		// CenterPane
		JPanel outcenterPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Datasource-Context"));
		innerthis.add(outcenterPane);
		JPanel centerPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
		outcenterPane.add(centerPane);

		JNDIFactoryComboBox = new UIComboBox(new String[] { "", "weblogic.jndi.WLInitialContextFactory", "com.ibm.websphere.naming.WsnInitialContextFactory",
				"org.jboss.naming.HttpNamingContextFactory", "org.jnp.interfaces.NamingContextFactory", "com.caucho.burlap.BurlapContextFactory", });
		JNDIFactoryComboBox.setEditable(true);

		JNDIFactoryComboBox.addActionListener(jndiListener);
		JNDIFactoryComboBox.setPreferredSize(new Dimension(30, JNDIFactoryComboBox.getPreferredSize().height + 2));

		// ContextPane
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		double[] rowSize = { p, p, p, p };
		double[] columnSize = { f, f };
		Component[][] comps = { { new UILabel("INITIAL_CONTEXT_FACTORY:", SwingConstants.RIGHT), JNDIFactoryComboBox },
				{ new UILabel("PROVIDER_URL:", SwingConstants.RIGHT), PROVIDER_URL_TF }, { new UILabel("SECURITY_PRINCIPAL:", SwingConstants.RIGHT), SECURITY_PRINCIPAL_TF },
				{ new UILabel("SECURITY_CREDENTIALS:", SwingConstants.RIGHT), SECURITY_CREDENTIALS_TF } };
		centerPane.add(TableLayoutHelper.createCommonTableLayoutPane(comps, rowSize, columnSize, 2));

		// ActionLabel
		JPanel actionPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		centerPane.add(actionPane);
		actionPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 6));

		ActionLabel actionLabel = new ActionLabel(Inter.getLocText("Datasource-Other_Attributes"));
		actionPane.add(actionLabel, BorderLayout.EAST);
		actionLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JDialog wDialog = createJDialog();
				wDialog.setVisible(true);
			}
		});

		// South Description
		UILabel explainLabe11l = new FRExplainLabel(Inter.getLocText("Datasource-JNDI_DES"));
		innerthis.add(new JScrollPane(explainLabe11l));
	}

	public void populate(JNDIDatabaseConnection jndiDatabase) {
		if (jndiDatabase == null) {
			jndiDatabase = new JNDIDatabaseConnection();
		}

		// Properties.
		Hashtable<String, String> contextHashtable = jndiDatabase.getContextHashtable();
		Object INITIAL_CONTEXT_FACTORY = contextHashtable.get(Context.INITIAL_CONTEXT_FACTORY);

		this.JNDIFactoryComboBox.setSelectedItem(INITIAL_CONTEXT_FACTORY == null ? "" : INITIAL_CONTEXT_FACTORY);
		this.jndiNameTextField.setText(jndiDatabase.getJNDIName() == null ? "" : jndiDatabase.getJNDIName());
		populateContextAttributes(contextHashtable, this.PROVIDER_URL_TF, Context.PROVIDER_URL);
		populateContextAttributes(contextHashtable, this.SECURITY_PRINCIPAL_TF, Context.SECURITY_PRINCIPAL);
		populateContextAttributes(contextHashtable, this.SECURITY_CREDENTIALS_TF, Context.SECURITY_CREDENTIALS);

		populateContextAttributes(contextHashtable, this.OBJECT_FACTORIES_TF, Context.OBJECT_FACTORIES);
		populateContextAttributes(contextHashtable, this.STATE_FACTORIES_TF, Context.STATE_FACTORIES);
		populateContextAttributes(contextHashtable, this.URL_PKG_PREFIXES_TF, Context.URL_PKG_PREFIXES);

		populateContextAttributes(contextHashtable, this.DNS_URL_TF, Context.DNS_URL);
		populateContextAttributes(contextHashtable, this.AUTHORITATIVE_TF, Context.AUTHORITATIVE);
		populateContextAttributes(contextHashtable, this.BATCHSIZE_TF, Context.BATCHSIZE);
		populateContextAttributes(contextHashtable, this.REFERRAL_TF, Context.REFERRAL);
		populateContextAttributes(contextHashtable, this.SECURITY_PROTOCOL_TF, Context.SECURITY_PROTOCOL);
		populateContextAttributes(contextHashtable, this.SECURITY_AUTHENTICATION_TF, Context.SECURITY_AUTHENTICATION);
		populateContextAttributes(contextHashtable, this.LANGUAGE_TF, Context.LANGUAGE);
		populateContextAttributes(contextHashtable, this.APPLET_TF, Context.APPLET);
	}

	private void populateContextAttributes(Hashtable<String, String> properties, UITextField textField, String contextAttr) {
		String PROVIDER_URL = properties.get(contextAttr);
		if (PROVIDER_URL != null) {
			textField.setText(PROVIDER_URL);
		}
	}

	public JNDIDatabaseConnection update() {
		JNDIDatabaseConnection jndiDatabase = new JNDIDatabaseConnection();

		jndiDatabase.setJNDIName(this.jndiNameTextField.getText());

		Hashtable<String, String> contextHashtable = jndiDatabase.getContextHashtable();

		String factoryString = (String)this.JNDIFactoryComboBox.getEditor().getItem();
		if (factoryString != null && factoryString.trim().length() > 0) {
			contextHashtable.put(Context.INITIAL_CONTEXT_FACTORY, factoryString);
		}
		updateContextAttributes(contextHashtable, this.PROVIDER_URL_TF, Context.PROVIDER_URL);
		updateContextAttributes(contextHashtable, this.SECURITY_PRINCIPAL_TF, Context.SECURITY_PRINCIPAL);
		updateContextAttributes(contextHashtable, this.SECURITY_CREDENTIALS_TF, Context.SECURITY_CREDENTIALS);

		updateContextAttributes(contextHashtable, this.OBJECT_FACTORIES_TF, Context.OBJECT_FACTORIES);
		updateContextAttributes(contextHashtable, this.STATE_FACTORIES_TF, Context.STATE_FACTORIES);
		updateContextAttributes(contextHashtable, this.URL_PKG_PREFIXES_TF, Context.URL_PKG_PREFIXES);

		updateContextAttributes(contextHashtable, this.DNS_URL_TF, Context.DNS_URL);
		updateContextAttributes(contextHashtable, this.AUTHORITATIVE_TF, Context.AUTHORITATIVE);
		updateContextAttributes(contextHashtable, this.BATCHSIZE_TF, Context.BATCHSIZE);
		updateContextAttributes(contextHashtable, this.REFERRAL_TF, Context.REFERRAL);
		updateContextAttributes(contextHashtable, this.SECURITY_PROTOCOL_TF, Context.SECURITY_PROTOCOL);
		updateContextAttributes(contextHashtable, this.SECURITY_AUTHENTICATION_TF, Context.SECURITY_AUTHENTICATION);
		updateContextAttributes(contextHashtable, this.LANGUAGE_TF, Context.LANGUAGE);
		updateContextAttributes(contextHashtable, this.APPLET_TF, Context.APPLET);

		return jndiDatabase;
	}

	private void updateContextAttributes(Hashtable<String, String> contextHashtable, UITextField textField, String contextAttr) {
		String tValue = textField.getText();
		if (tValue != null && tValue.trim().length() > 0) {
			contextHashtable.put(contextAttr, tValue);
		}
	}

	ActionListener jndiListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Object o = JNDIFactoryComboBox.getSelectedItem();
			if (o == null || ComparatorUtils.equals(o, StringUtils.EMPTY)) {
				PROVIDER_URL_TF.setText("");
				return;
			}
			PROVIDER_URL_TF.setText(jndiMap.get(o));
		}

	};

	private JDialog createJDialog() {
		if (this.otherAttrDialog == null) {
			this.otherAttrDialog = new OtherAttrPane().showWindow(SwingUtilities.getWindowAncestor(JNDIDefPane.this));
		}

		return this.otherAttrDialog;
	}

	class OtherAttrPane extends BasicPane {
		public OtherAttrPane() {
			// JPanel northFlowPane
			JPanel northFlowPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
			this.add(northFlowPane, BorderLayout.NORTH);

			// ContextPane
			double f = TableLayout.FILL;
			double[] rowSize = { f, f, f, f, f, f, f, f, f, f, f };
			double[] columnSize = { f, f };
			Component[][] comps = { { new UILabel("OBJECT_FACTORIES:", SwingConstants.RIGHT), OBJECT_FACTORIES_TF },
					{ new UILabel("STATE_FACTORIES:", SwingConstants.RIGHT), STATE_FACTORIES_TF }, { new UILabel("URL_PKG_PREFIXES:", SwingConstants.RIGHT), URL_PKG_PREFIXES_TF },
					{ new UILabel("DNS_URL:", SwingConstants.RIGHT), DNS_URL_TF }, { new UILabel("AUTHORITATIVE:", SwingConstants.RIGHT), AUTHORITATIVE_TF },
					{ new UILabel("BATCHSIZE:", SwingConstants.RIGHT), BATCHSIZE_TF }, { new UILabel("REFERRAL:", SwingConstants.RIGHT), REFERRAL_TF },
					{ new UILabel("SECURITY_PROTOCOL:", SwingConstants.RIGHT), SECURITY_PROTOCOL_TF },
					{ new UILabel("SECURITY_AUTHENTICATION:", SwingConstants.RIGHT), SECURITY_AUTHENTICATION_TF }, { new UILabel("LANGUAGE:", SwingConstants.RIGHT), LANGUAGE_TF },
					{ new UILabel("APPLET:", SwingConstants.RIGHT), APPLET_TF } };
			northFlowPane.add(TableLayoutHelper.createCommonTableLayoutPane(comps, rowSize, columnSize, 2));
		}

		@Override
		protected String title4PopupWindow() {
			return Inter.getLocText("Datasource-Other_Attributes");
		}
	}

	// 主力Context属性
	class ContextTextField extends UITextField {
		private String contextName;

		public ContextTextField(String contextName) {
			this.setContextName(contextName);
			this.setColumns(24);
		}

		public String getContextName() {
			return contextName;
		}

		public void setContextName(String contextName) {
			this.contextName = contextName;
		}

		/*
		 * 更新Properties.
		 */
		public void applyProperties(Properties properties) {
			properties.put(contextName, this.getText());
		}
	}
}