package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.base.ParameterHelper;
import com.fr.data.core.db.TableProcedure;
import com.fr.data.impl.Connection;
import com.fr.data.impl.DBTableData;
import com.fr.data.impl.JDBCDatabaseConnection;
import com.fr.data.impl.JNDIDatabaseConnection;
import com.fr.data.impl.NameDatabaseConnection;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.data.datapane.connect.ConnectionTableProcedurePane;
import com.fr.design.data.datapane.connect.ConnectionTableProcedurePane.DoubleClickSelectedNodeOnTreeListener;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.data.datapane.sqlpane.SQLEditPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.fun.DBTableDataMenuHandler;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.SyntaxConstants;
import com.fr.design.gui.syntax.ui.rtextarea.RTextScrollPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.script.Calculator;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

public class DBTableDataPane extends AbstractTableDataPane<DBTableData> {
	private static final int BOTTOM = 6;
	private static final String PREVIEW_BUTTON = Inter.getLocText("FR-Designer_Preview");
    private static final String REFRESH_BUTTON = Inter.getLocText("FR-Designer_Refresh");
	private ConnectionTableProcedurePane connectionTableProcedurePane;
	private UITableEditorPane<ParameterProvider> editorPane;

	private DBTableDataMenuHandler dbTableDataMenuHandler;
	private SQLEditPane sqlTextPane;
	private UICheckBox isShareCheckBox;
	private MaxMemRowCountPanel maxPanel;
	private String pageQuery = null;
	private DBTableData dbTableData;

	private void init() {
		this.setLayout(new BorderLayout(4, 4));

		sqlTextPane = new SQLEditPane();
		sqlTextPane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);

		ParameterTableModel model = new ParameterTableModel() {
			@Override
			public UITableEditAction[] createAction() {
				return (UITableEditAction[]) ArrayUtils.add(super.createDBTableAction(), new RefreshAction());
			}
		};
		editorPane = new UITableEditorPane<ParameterProvider>(model);

		// 左边的Panel,上面是选择DatabaseConnection的ComboBox,下面DatabaseConnection对应的Table
		connectionTableProcedurePane = new ConnectionTableProcedurePane() {
			@Override
			protected void filter(Connection connection, String conName, List<String> nameList) {
				connection.addConnection(nameList, conName, new Class[]{JDBCDatabaseConnection.class, JNDIDatabaseConnection.class});
			}
		};
		connectionTableProcedurePane.addDoubleClickListener(new DoubleClickSelectedNodeOnTreeListener() {

			@Override
			public void actionPerformed(TableProcedure target) {
				Document document = sqlTextPane.getDocument();
				try {
					document.insertString(sqlTextPane.getCaretPosition(), target.toString(), null);
				} catch (BadLocationException e) {
					FRContext.getLogger().error(e.getMessage(), e);
				}
				// 这里开始作色,本来可以给sqlTextPane添加DocumentListener来实现的，
				// 后来发现insertString的时候，锁定了JTextPane,不能调用setXXX来作色,先这样了.
//				sqlTextPane.syntaxTexts();
				sqlTextPane.requestFocus();
			}
		});
		sqlTextPane.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// unsupport
			}

			public void focusLost(FocusEvent e) {
				if (isPreviewOrRefreshButton(e)) {
					checkParameter();
				}
			}
		});
	}


	private void initMainSplitPane() {
		Box box = new Box(BoxLayout.Y_AXIS);


		JPanel northPane = new JPanel(new BorderLayout(4, 4));
		JToolBar editToolBar = createToolBar();
		northPane.add(editToolBar, BorderLayout.CENTER);
		northPane.setBorder(BorderFactory.createEmptyBorder(0, 0, BOTTOM, 0));

		RTextScrollPane sqlTextScrollPane = new RTextScrollPane(sqlTextPane);
		sqlTextScrollPane.setLineNumbersEnabled(true);
		sqlTextScrollPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));
		sqlTextScrollPane.setPreferredSize(new Dimension(680, 600));

		JPanel paraMeanPane = new JPanel(new BorderLayout());
		paraMeanPane.setPreferredSize(new Dimension(680, 90));
		UILabel paraMean = new UILabel(Inter.getLocText("FR-Designer-Datasource-Param_DES"));
		paraMeanPane.add(paraMean, BorderLayout.CENTER);


		box.add(northPane);
		box.add(sqlTextScrollPane);
		box.add(paraMeanPane);
		box.add(editorPane);

		JPanel sqlSplitPane = new JPanel(new BorderLayout(4, 4));
		sqlSplitPane.add(box, BorderLayout.CENTER);


		box.setMinimumSize(new Dimension(310, 400));
		// 使用SplitPane
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, connectionTableProcedurePane, sqlSplitPane);
		mainSplitPane.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
		mainSplitPane.setOneTouchExpandable(true);
		this.add(mainSplitPane, BorderLayout.CENTER);
	}

	public DBTableDataPane() {
		init();
		initMainSplitPane();
	}



	private boolean isPreviewOrRefreshButton (FocusEvent e) {
		if (e.getOppositeComponent() != null) {
			String name = e.getOppositeComponent().getName();
			return ComparatorUtils.equals(name, PREVIEW_BUTTON) || ComparatorUtils.equals(name, REFRESH_BUTTON);
		}
		return false;
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("FR-Designer-DS-Database_Query");
	}

	private void refresh() {
		String[] paramTexts = new String[2];
		paramTexts[0] = sqlTextPane.getText();
		paramTexts[1] = pageQuery;
		List<ParameterProvider> existParameterList = editorPane.update();
		Parameter[] ps = existParameterList == null ? new Parameter[0] : existParameterList.toArray(new Parameter[existParameterList.size()]);

		editorPane.populate(ParameterHelper.analyzeAndUnionSameParameters(paramTexts, ps));
	}

	private JToolBar createToolBar() {
		// p:工具栏.
		ToolBarDef toolBarDef = new ToolBarDef();
		toolBarDef.addShortCut(new PreviewAction());
		toolBarDef.addShortCut(SeparatorDef.DEFAULT);
		toolBarDef.addShortCut(new EditPageQueryAction());
		dbTableDataMenuHandler = ExtraDesignClassManager.getInstance().getSingle(DBTableDataMenuHandler.MARK_STRING);
		if (dbTableDataMenuHandler != null) {
			toolBarDef.addShortCut(SeparatorDef.DEFAULT);
			toolBarDef.addShortCut(dbTableDataMenuHandler.createQueryAction());
		}
		isShareCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Is_Share_DBTableData"));
		isShareCheckBox.setBackground(Color.WHITE);
		maxPanel = new MaxMemRowCountPanel();
		maxPanel.setBorder(null);
		UIToolbar editToolBar = ToolBarDef.createJToolBar();
		toolBarDef.updateToolBar(editToolBar);
		editToolBar.add(isShareCheckBox);
		editToolBar.add(maxPanel);
		return editToolBar;
	}

	private void checkParameter() {
		String[] paramTexts = new String[2];
		paramTexts[0] = sqlTextPane.getText();
		paramTexts[1] = pageQuery;
		Parameter[] parameters = ParameterHelper.analyze4Parameters(paramTexts, false);

		if (parameters.length < 1 && editorPane.update().size() < 1) {
			return;
		}
		boolean isIn = true;
		List<ParameterProvider> list = editorPane.update();
		List<String> name = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			name.add(list.get(i).getName());
		}
		for (int i = 0; i < parameters.length; i++) {
			if (!name.contains(parameters[i].getName())) {
				isIn = false;
				break;
			}
		}
		if (list.size() == parameters.length && isIn) {
			return;
		}
		// bug:34175  删了是否刷新对话框， 均直接刷新
		refresh();
	}

	@Override
	public void populateBean(DBTableData dbtabledata) {
		this.dbTableData = dbtabledata;
		if (dbTableDataMenuHandler != null) {
			dbTableDataMenuHandler.populate(dbtabledata);
		}
		ParameterProvider[] parameters = null;

		Calculator c = Calculator.createCalculator();

		parameters = dbtabledata.getParameters(c);
		editorPane.populate(parameters);

		com.fr.data.impl.Connection db = null;
		String query = null;
		boolean isShare = false;
		int maxMemeryRow = -1;

		db = dbtabledata.getDatabase();
		query = dbtabledata.getQuery();
		isShare = dbtabledata.isShare();
		maxMemeryRow = dbtabledata.getMaxMemRowCount();
		this.pageQuery = dbtabledata.getPageQuerySql();
		this.connectionTableProcedurePane.setSelectedDatabaseConnection(db);
		this.sqlTextPane.setText(query);
		this.sqlTextPane.requestFocus();
		this.sqlTextPane.moveCaretPosition(this.sqlTextPane.getCaretPosition());

		isShareCheckBox.setSelected(isShare);
		maxPanel.setValue(maxMemeryRow);
	}

	@Override
	public DBTableData updateBean() {
		String dbName = this.connectionTableProcedurePane.getSelectedDatabaseConnnectonName();
		if (StringUtils.isBlank(dbName) || StringUtils.isBlank(this.sqlTextPane.getText())) {
			try {
				throw new Exception(Inter.getLocText("FR-Designer_Connect_SQL_Cannot_Null") + ".");
			} catch (Exception e) {
				// JOptionPane.showMessageDialog(DBTableDataPane.this,
				// Inter.getLocText("Connect_SQL_Cannot_Null") + ".");
			}
		}

		List<ParameterProvider> parameterList = editorPane.update();
		Parameter[] parameters = parameterList.toArray(new Parameter[parameterList.size()]);

		dbTableData.setDatabase(new NameDatabaseConnection(dbName));

		// p:必须先设置Parameters数组，因为setQuery里面会自动设置的

		dbTableData.setParameters(parameters);
		dbTableData.setQuery(this.sqlTextPane.getText());

		dbTableData.setShare(isShareCheckBox.isSelected());
		dbTableData.setMaxMemRowCount(maxPanel.getValue());
		dbTableData.setPageQuerySql(this.pageQuery);
		if (dbTableDataMenuHandler != null) {
			dbTableDataMenuHandler.update();
		}
		return dbTableData;
	}

	protected class RefreshAction extends UITableEditAction {
		public RefreshAction() {
			this.setName(REFRESH_BUTTON);
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
		}

		public void actionPerformed(ActionEvent e) {
			refresh();
		}

		@Override
		public void checkEnabled() {
			// unsupport
		}
	}

	private class PreviewAction extends UpdateAction {
		public PreviewAction() {
			this.setName(PREVIEW_BUTTON);
			this.setMnemonic('P');
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/preview.png"));
		}

		public void actionPerformed(ActionEvent evt) {
			checkParameter();
			PreviewTablePane.previewTableData(DBTableDataPane.this.updateBean());
		}
	}

	private class EditPageQueryAction extends UpdateAction {
		public EditPageQueryAction() {
			this.setName(Inter.getLocText("FR-Designer-LayerPageReport_PageQuery"));
			this.setMnemonic('L');
			this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/text.png"));
		}

		public void actionPerformed(ActionEvent e) {
			final QueryPane pane = new QueryPane(Inter.getLocText("FR-Designer-LayerPageReport_Define_PageQuerySQL"));
			pane.populate(pageQuery);
			BasicDialog dialog = pane.showWindow(DesignerContext.getDesignerFrame());
			dialog.addDialogActionListener(new DialogActionAdapter() {
				public void doOk() {
					pageQuery = pane.update();
					checkParameter();
				}
			});
			dialog.setVisible(true);
		}
	}

	private class QueryPane extends BasicPane {
		private SQLEditPane pageQueryPane;
		private String title;

		public QueryPane(String title) {
			this.title = title;
			this.initComponents();
		}

		public void initComponents() {
			this.setLayout(new BorderLayout());
			pageQueryPane = new SQLEditPane();
			this.add(new JScrollPane(pageQueryPane));
		}

		public void populate(String text) {
			if (StringUtils.isBlank(text)) {
				return;
			}
			pageQueryPane.setText(text);
		}

		public String update() {
			String text = pageQueryPane.getText();
			if (StringUtils.isBlank(text)) {
				return null;
			} else {
				return text;
			}
		}

		@Override
		protected String title4PopupWindow() {
			return title;
		}
	}
}