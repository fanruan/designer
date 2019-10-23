package com.fr.design.data.datapane.connect;

import com.fr.design.constants.UIConstants;
import com.fr.data.impl.JDBCDatabaseConnection;
import com.fr.data.pool.DBCPConnectionPoolAttr;
import com.fr.design.border.UITitledBorder;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ipasswordfield.UIPassWordField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.ComparatorUtils;

import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class JDBCDefPane extends JPanel {
	public static final String DRIVER_TYPE = "driver_type";
	public static final String USER_NAME = "user_name";
    public static final int TIME_MULTIPLE = 1000;
	private static final String OTHER_DB = "Others";

	private static Map<String, DriverURLName[]> jdbcMap = new HashMap<String, DriverURLName[]>();

	static {
		jdbcMap.put(OTHER_DB, new DriverURLName[]{new DriverURLName("sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:"),
				new DriverURLName("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:[PATH_TO_DB_FILES]"), new DriverURLName("com.inet.tds.TdsDriver", "jdbc:inetdae7:localhost:1433/"),
				new DriverURLName("COM.cloudscape.JDBCDriver", "jdbc:cloudscape:/cloudscape/"),
				new DriverURLName("com.internetcds.jdbc.tds.Driver", "jdbc:freetds:sqlserver://localhost/"),
				new DriverURLName("com.fr.swift.jdbc.Driver", "jdbc:swift:emb://default")});
        jdbcMap.put("Inceptor",new DriverURLName[]{new DriverURLName("org.apache.hive.jdbc.HiveDriver","jdbc:inceptor2://localhost:10000/default"),
				new DriverURLName("org.apache.hadoop.hive.jdbc.HiveDriver","jdbc:inceptor://localhost:10000/default")});
		jdbcMap.put("Oracle", new DriverURLName[]{new DriverURLName("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@localhost:1521:databaseName")});
		jdbcMap.put("DB2", new DriverURLName[]{new DriverURLName("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://localhost:50000/")});
		jdbcMap.put("SQL Server", new DriverURLName[]{new DriverURLName("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://localhost:1433;" + "databaseName=")});
		jdbcMap.put("MySQL", new DriverURLName[]{new DriverURLName("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/"),
				new DriverURLName("org.gjt.mm.mysql.Driver", "jdbc:mysql://localhost/")});
		jdbcMap.put("Sybase", new DriverURLName[]{new DriverURLName("com.sybase.jdbc2.jdbc.SybDriver", "jdbc:sybase:Tds:localhost:5000/")});
		jdbcMap.put("Access", new DriverURLName[]{new DriverURLName("sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=")});
		jdbcMap.put("Derby", new DriverURLName[]{new DriverURLName("org.apache.derby.jdbc.ClientDriver", "jdbc:derby://localhost:1527/")});
		jdbcMap.put("Postgre", new DriverURLName[]{new DriverURLName("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/")});
		jdbcMap.put("SQLite", new DriverURLName[]{new DriverURLName("org.sqlite.JDBC", "jdbc:sqlite://${ENV_HOME}/../help/FRDemo.db")});
	}

	private UIButton dbtypeButton;
	private UIComboBox dbtypeComboBox;
	private UIComboBox driverComboBox;
	private UITextField urlTextField;
	private UITextField userNameTextField;
	private JPasswordField passwordTextField;
	// 请不要改动dbtype,只应该最后添加
	private final String[] dbtype = {"Oracle", "DB2", "SQL Server", "MySQL", "Sybase", "Access", "Derby", "Postgre","SQLite","Inceptor", OTHER_DB};

	// carl:DBCP的一些属性
	private IntegerEditor DBCP_INITIAL_SIZE = new IntegerEditor();
	private IntegerEditor DBCP_MAX_ACTIVE = new IntegerEditor();
	private IntegerEditor DBCP_MAX_IDLE = new IntegerEditor();
	private IntegerEditor DBCP_MIN_IDLE = new IntegerEditor();
	private IntegerEditor DBCP_MAX_WAIT = new IntegerEditor();
	private UITextField DBCP_VALIDATION_QUERY = new UITextField();

	private UIComboBox DBCP_TESTONBORROW = new UIComboBox(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Yes")});
	private UIComboBox DBCP_TESTONRETURN = new UIComboBox(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Yes")});
	private UIComboBox DBCP_TESTWHILEIDLE = new UIComboBox(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_No"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Yes")});

	private IntegerEditor DBCP_TIMEBETWEENEVICTIONRUNSMILLS = new IntegerEditor();
	private IntegerEditor DBCP_NUMTESTSPEREVICTIONRUN = new IntegerEditor();
	private IntegerEditor DBCP_MINEVICTABLEIDLETIMEMILLIS = new IntegerEditor();

	public JDBCDefPane() {
		this.setBorder(UITitledBorder.createBorderWithTitle("JDBC" + ":"));
		this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
		JPanel innerthis = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		innerthis.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.add(innerthis);
		dbtypeComboBox = new UIComboBox();
		dbtypeComboBox.setName(DRIVER_TYPE);
		for (int i = 0; i < dbtype.length; i++) {
			dbtypeComboBox.addItem(dbtype[i]);
		}
		dbtypeComboBox.addActionListener(dbtypeActionListener);
		dbtypeComboBox.setMaximumRowCount(10);

		driverComboBox = new UIComboBox();
		driverComboBox.setEditable(true);
		driverComboBox.addActionListener(driverListener);
		urlTextField = new UITextField(15);
		userNameTextField = new UITextField(15);
		userNameTextField.setName(USER_NAME);
		passwordTextField = new UIPassWordField(15);
		dbtypeButton = new UIButton(".");
		dbtypeButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Click_Get_Default_URL"));
		dbtypeButton.addActionListener(dbtypeButtonActionListener);

		double p = TableLayout.PREFERRED;
		double f = TableLayout.FILL;
		JPanel dbtypePane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
		dbtypePane.add(new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database") + ":")));
		JPanel dbtypeComPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		Component[][] dbtypeComComponents = {{dbtypeComboBox}};
		double[] dbtypeRowSize = {p};
		double[] dbtypeColumnSize = {p};
		dbtypeComPane = TableLayoutHelper.createTableLayoutPane(dbtypeComComponents, dbtypeRowSize, dbtypeColumnSize);

		JPanel driverPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
		driverPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Driver") + ":"));

		JPanel urlPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
		urlPane.add(new UILabel("URL:"));
		JPanel urlComPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		Component[][] urlComComponents = {{urlTextField, dbtypeButton}};
		double[] urlRowSize = {p};
		double[] urlColumnSize = {f, 21};
		urlComPane = TableLayoutHelper.createCommonTableLayoutPane(urlComComponents, urlRowSize, urlColumnSize, 4);

		JPanel userPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
		userPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_UserName") + ":"));
		JPanel userComPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		Component[][] userComComponents = {{userNameTextField, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Password") + ":"), passwordTextField}};
		double[] userRowSize = {p};
		double[] userColumnSize = {f, p, f};
		userComPane = TableLayoutHelper.createCommonTableLayoutPane(userComComponents, userRowSize, userColumnSize, 4);

		JPanel passwordPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
		passwordPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Password") + ":"));

		Component[][] components = {{dbtypePane, dbtypeComPane}, {driverPane, driverComboBox}, {urlPane, urlComPane}, {userPane, userComPane},};

		double[] rowSize = {p, p, p, p};
		double[] columnSize = {p, f, 22};
		JPanel centerPanel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 0, 6);
		innerthis.add(centerPanel);
		JPanel southPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		innerthis.add(southPanel);
		southPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 20));
		ActionLabel actionLabel = new ActionLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ConnectionPool_Attr"));
		southPanel.add(actionLabel, BorderLayout.EAST);
		actionLabel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JDialog wDialog = createJDialog();
				wDialog.setVisible(true);
			}
		});
	}

	public void populate(JDBCDatabaseConnection jdbcDatabase) {
		if (jdbcDatabase == null) {
			jdbcDatabase = new JDBCDatabaseConnection();
		}
		if (ComparatorUtils.equals(jdbcDatabase.getDriver(), "sun.jdbc.odbc.JdbcOdbcDriver")
				&& jdbcDatabase.getURL().startsWith("jdbc:odbc:Driver={Microsoft")) {
			this.dbtypeComboBox.setSelectedItem("Access");
		} else {
			Iterator<Entry<String, DriverURLName[]>> jdbc = jdbcMap.entrySet().iterator();
			boolean out = false;
			while (jdbc.hasNext()) {
				Entry<String, DriverURLName[]> entry = jdbc.next();
				DriverURLName[] dus = entry.getValue();
				for (int i = 0, len = dus.length; i < len; i++) {
					if (ComparatorUtils.equals(dus[i].getDriver(), jdbcDatabase.getDriver())) {
						this.dbtypeComboBox.setSelectedItem(entry.getKey());
						out = true;
						break;
					}
				}
				if (out) {
					break;
				}
			}
			if (!out) {
				this.dbtypeComboBox.setSelectedItem(OTHER_DB);
			}
		}
		this.driverComboBox.setSelectedItem(jdbcDatabase.getDriver());
		this.urlTextField.setText(jdbcDatabase.getURL());
		this.userNameTextField.setText(jdbcDatabase.getUser());
		this.passwordTextField.setText(jdbcDatabase.getPassword());

		DBCPConnectionPoolAttr dbcpAttr = jdbcDatabase.getDbcpAttr();
		if (dbcpAttr == null) {
			dbcpAttr = new DBCPConnectionPoolAttr();
			jdbcDatabase.setDbcpAttr(dbcpAttr);
		}
		this.DBCP_INITIAL_SIZE.setValue(dbcpAttr.getInitialSize());
		this.DBCP_MAX_ACTIVE.setValue(dbcpAttr.getMaxActive());
		this.DBCP_MAX_IDLE.setValue(dbcpAttr.getMaxIdle());
		this.DBCP_MAX_WAIT.setValue(dbcpAttr.getMaxWait());
		this.DBCP_MIN_IDLE.setValue(dbcpAttr.getMinIdle());
		this.DBCP_VALIDATION_QUERY.setText(dbcpAttr.getValidationQuery());
		this.DBCP_TESTONBORROW.setSelectedIndex(dbcpAttr.isTestOnBorrow() ? 1 : 0);
		this.DBCP_TESTONRETURN.setSelectedIndex(dbcpAttr.isTestOnReturn() ? 1 : 0);
		this.DBCP_TESTWHILEIDLE.setSelectedIndex(dbcpAttr.isTestWhileIdle() ? 1 : 0);
		this.DBCP_MINEVICTABLEIDLETIMEMILLIS.setValue(dbcpAttr.getMinEvictableIdleTimeMillis() / TIME_MULTIPLE);
		this.DBCP_NUMTESTSPEREVICTIONRUN.setValue(dbcpAttr.getNumTestsPerEvictionRun());
		this.DBCP_TIMEBETWEENEVICTIONRUNSMILLS.setValue(dbcpAttr.getTimeBetweenEvictionRunsMillis());
	}

	public JDBCDatabaseConnection update() {
		JDBCDatabaseConnection jdbcDatabase = new JDBCDatabaseConnection();
		Object driveItem = this.driverComboBox.getSelectedItem();
		jdbcDatabase.setDriver(driveItem == null ? null : driveItem.toString());
		jdbcDatabase.setURL(this.urlTextField.getText().trim());
		jdbcDatabase.setUser(this.userNameTextField.getText().trim());
		jdbcDatabase.setPassword(new String(this.passwordTextField.getPassword()).trim());

		DBCPConnectionPoolAttr dbcpAttr = jdbcDatabase.getDbcpAttr();
		if (dbcpAttr == null) {
			dbcpAttr = new DBCPConnectionPoolAttr();
			jdbcDatabase.setDbcpAttr(dbcpAttr);
		}
		dbcpAttr.setInitialSize(this.DBCP_INITIAL_SIZE.getValue().intValue());
		dbcpAttr.setMaxActive(this.DBCP_MAX_ACTIVE.getValue().intValue());
		dbcpAttr.setMaxIdle(this.DBCP_MAX_IDLE.getValue().intValue());
		dbcpAttr.setMaxWait(this.DBCP_MAX_WAIT.getValue().intValue());
		dbcpAttr.setMinIdle(this.DBCP_MIN_IDLE.getValue().intValue());
		dbcpAttr.setValidationQuery(this.DBCP_VALIDATION_QUERY.getText());
		dbcpAttr.setTestOnBorrow(this.DBCP_TESTONBORROW.getSelectedIndex() == 0 ? false : true);
		dbcpAttr.setTestOnReturn(this.DBCP_TESTONRETURN.getSelectedIndex() == 0 ? false : true);
		dbcpAttr.setTestWhileIdle(this.DBCP_TESTWHILEIDLE.getSelectedIndex() == 0 ? false : true);
		dbcpAttr.setMinEvictableIdleTimeMillis(((Number) this.DBCP_MINEVICTABLEIDLETIMEMILLIS.getValue()).intValue() * TIME_MULTIPLE);
		dbcpAttr.setNumTestsPerEvictionRun(((Number) this.DBCP_NUMTESTSPEREVICTIONRUN.getValue()).intValue());
		dbcpAttr.setTimeBetweenEvictionRunsMillis(((Number) this.DBCP_TIMEBETWEENEVICTIONRUNSMILLS.getValue()).intValue());

		return jdbcDatabase;
	}

	ActionListener dbtypeActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {

			urlTextField.setText(StringUtils.EMPTY);
			driverComboBox.removeAllItems();
			if (ComparatorUtils.equals(dbtypeComboBox.getSelectedItem(), StringUtils.EMPTY)) {
				driverComboBox.setSelectedItem(StringUtils.EMPTY);
				return;
			}

			DriverURLName[] dus = jdbcMap.get(dbtypeComboBox.getSelectedItem());
			for (int i = 0, len = dus.length; i < len; i++) {
				driverComboBox.addItem(dus[i].getDriver());
				if (i == 0) {
					driverComboBox.setSelectedItem(dus[i].getDriver());
					urlTextField.setText(dus[i].getURL());
				}
			}
		}
	};

	ActionListener driverListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (driverComboBox.getSelectedItem() == null ||ComparatorUtils.equals(driverComboBox.getSelectedItem(), StringUtils.EMPTY)) {
				return;
			}
			Iterator<Entry<String, DriverURLName[]>> jdbc = jdbcMap.entrySet().iterator();
			while (jdbc.hasNext()) {
				Entry<String, DriverURLName[]> entry = jdbc.next();
				DriverURLName[] dus = entry.getValue();
				for (int i = 0, len = dus.length; i < len; i++) {
					if (ComparatorUtils.equals(dus[i].getDriver(), (driverComboBox.getSelectedItem()))) {
						urlTextField.setText(dus[i].getURL());
						return;
					}
				}
			}
		}

	};

	ActionListener dbtypeButtonActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			if (ComparatorUtils.equals(dbtypeComboBox.getSelectedItem(), StringUtils.EMPTY)) {
				return;
			}
			DriverURLName[] dus = jdbcMap.get(dbtypeComboBox.getSelectedItem());
			for (int i = 0, len = dus.length; i < len; i++) {
				if (ComparatorUtils.equals(driverComboBox.getSelectedItem(), (dus[i].getDriver()))) {
					urlTextField.setText(dus[i].getURL());
					if (ComparatorUtils.equals(dbtypeComboBox.getSelectedItem(), ("Access"))) {
						// ben:这个能不能换种处理方案- -
						JFileChooser filechooser = new JFileChooser();
						filechooser.setDialogTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Open"));
						filechooser.setMultiSelectionEnabled(false);
						filechooser.addChoosableFileFilter(new ChooseFileFilter(new String[]{"accdb", "mdb"}, "Microsoft Office Access"));
						int result = filechooser.showOpenDialog(DesignerContext.getDesignerFrame());
						File selectedfile = null;

						if (result == JFileChooser.APPROVE_OPTION) {
							selectedfile = filechooser.getSelectedFile();
							if (selectedfile != null) {
								String selectedName = selectedfile.getPath().substring(selectedfile.getPath().lastIndexOf('.') + 1);
								if (selectedName.equalsIgnoreCase("mdb") || selectedName.equalsIgnoreCase("accdb")) {
									urlTextField.setText(urlTextField.getText() + selectedfile.getPath());
								}
							}
						}
					}
					break;
				}
			}
		}
	};

	private JDialog createJDialog() {
		return new DBCPAttrPane().showWindow(SwingUtilities.getWindowAncestor(JDBCDefPane.this));
	}

	class DBCPAttrPane extends BasicPane {
		public DBCPAttrPane() {
			JPanel defaultPane = this;

			// JPanel northFlowPane
			JPanel northFlowPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
			defaultPane.add(northFlowPane, BorderLayout.NORTH);

			DBCP_VALIDATION_QUERY.setColumns(15);
			// ContextPane

			double f = TableLayout.FILL;
			// double p = TableLayout.PREFERRED;
			double[] rowSize = {f, f, f, f, f, f, f, f, f, f, f, f};
			double[] columnSize = {f, f};
			Component[][] comps = {
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Initial_Size") + ":", SwingConstants.RIGHT), DBCP_INITIAL_SIZE},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Max_Active") + ":", SwingConstants.RIGHT), DBCP_MAX_ACTIVE},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Max_Idle") + ":", SwingConstants.RIGHT), DBCP_MAX_IDLE},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Min_Idle") + ":", SwingConstants.RIGHT), DBCP_MIN_IDLE},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Connection_Pool_Max_Wait_Time") + ":" , SwingConstants.RIGHT), DBCP_MAX_WAIT},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Validation_Query") + ":", SwingConstants.RIGHT), DBCP_VALIDATION_QUERY},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Test_On_Borrow") + ":", SwingConstants.RIGHT), DBCP_TESTONBORROW},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Test_On_Return") + ":", SwingConstants.RIGHT), DBCP_TESTONRETURN},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Test_While_Idle") + ":", SwingConstants.RIGHT), DBCP_TESTWHILEIDLE},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Connection_Pool_Evictionruns_millis") + ":", SwingConstants.RIGHT),
							DBCP_TIMEBETWEENEVICTIONRUNSMILLS},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dbcp_Num_Test_Per_Evction_Run") + ":", SwingConstants.RIGHT), DBCP_NUMTESTSPEREVICTIONRUN},
					{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Connection_Pool_Mix_Evictable_Idle_Time_Millis") + ":" , SwingConstants.RIGHT),
							DBCP_MINEVICTABLEIDLETIMEMILLIS}};

			JPanel contextPane = TableLayoutHelper.createGapTableLayoutPane(comps, rowSize, columnSize, 10, 4);
			contextPane.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, UIConstants.LINE_COLOR));
			northFlowPane.add(contextPane);
		}

		@Override
		protected String title4PopupWindow() {
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ConnectionPool_Attr");
		}
	}

	private static class DriverURLName {
		public DriverURLName(String driver, String url) {
			this.driver = driver;
			this.url = url;
		}

		public String getDriver() {
			return this.driver;
		}

		public String getURL() {
			return this.url;
		}

		private String driver;
		private String url;
	}
}
