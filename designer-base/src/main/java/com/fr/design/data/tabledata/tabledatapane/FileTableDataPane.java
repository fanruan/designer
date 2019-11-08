package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.base.ParameterHelper;
import com.fr.base.Utils;
import com.fr.data.core.datasource.FileDataSource;
import com.fr.data.core.datasource.URLDataSource;
import com.fr.data.core.define.XMLColumnNameType;
import com.fr.data.impl.ExcelTableData;
import com.fr.data.impl.FileTableData;
import com.fr.data.impl.TextTableData;
import com.fr.data.impl.XMLTableData;
import com.fr.design.actions.UpdateAction;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.data.DataSource;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileTableDataPane extends AbstractTableDataPane<FileTableData> {
    private static final int TEXT = 0;
    private static final int EXCEL = 1;
    private static final int XML = 2;
    private static final double B = 20;
    private static final int EIGHT = 8;
    private static final String ROOTTAG = "XML";
    private static final String STARTTAG = "<XML>";
    private static final String ENDTAG = "</XML>";

    private UIComboBox fileTypeComboBox;
    private UITextField localText;
    private UITextField urlText;
    private UIRadioButton localFileRadioButton;
    private UIRadioButton urlFileRadioButton;
    private UITableEditorPane<ParameterProvider> editorPane;
    private UILabel tips;
    private UIComboBox xmlKyePoint;// xml关键节点
    private UIComboBox encodingComboBox;// xml编码
    private FileTableData fileTableData;
    private UIButton chooseFile;
    private UIButton testConnection;
    private XMLNodeTree xmlNodeTree;
    private Parameter[] params;
    private JPanel filePath;
    private XMLNodeTreePane nodeTreePane;

    private UICheckBox needColumnNameCheckBox;// 第一行是否作为标题
    private UIRadioButton tableDismemberRadioButton;// 制表符
    private UIRadioButton spaceDismenberRadioButton;// 空格符,也是默认的分隔符
    private UIRadioButton commaDismenberRadioButton;// 逗号
    private UIRadioButton otherDismenberRadioButton;// 其他
    private UITextField otherDismenberTextField;// 其他分隔符编辑
    private UICheckBox ignoreOneMoreDelimiterCheckBox;// 连续分隔符是否作为单一
    private UIComboBox charsetComboBox;
    private UILabel encodeLabel;
    private UILabel dismenberLabel;
    private UILabel keyPointLaber;
    private ExpandMutableTreeNode selectedNode = null;
    private ExpandMutableTreeNode finalSelectedNode = null;


    private ArrayList<String> xmlColumnsList = new ArrayList<String>();
    private static final int SETPANELWIDTH = 337;
    private static final int WIDTH = 317;
    private static final int HEIGHT = 453;
    private static final int GAP = 23;


    public FileTableDataPane() {
        this(SETPANELWIDTH, WIDTH, HEIGHT, GAP);
    }

    public FileTableDataPane(int setPanelWidth, int width, int height, int gap) {
        this.setLayout(new BorderLayout(gap, 0));
        JPanel northPanel = new JPanel(new BorderLayout());
        JPanel type = new JPanel();
        type.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_File_Type") + ":"));
        String[] item = {"TXT", "Excel", "XML"};
        fileTypeComboBox = new UIComboBox(item);
        fileTypeComboBox.setPreferredSize(new Dimension(100, 20));
        type.add(fileTypeComboBox);
        northPanel.add(type, BorderLayout.WEST);

        // 最上面的pane，文件选择
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(522, 200));
        centerPanel.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_File_Address")));
        addToCenterPanel(centerPanel);

        // 下面的pane，参数面板
        ParameterTableModel model = new ParameterTableModel() {
            @Override
            public UITableEditAction[] createAction() {
                return (UITableEditAction[]) ArrayUtils.add(null, new RefreshAction());
            }
        };
        editorPane = new UITableEditorPane<ParameterProvider>(model);
        editorPane.setPreferredSize(new Dimension(355, 130));
        centerPanel.add(editorPane, BorderLayout.SOUTH);

        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel setPanel = new JPanel();
        southPanel.add(setPanel, BorderLayout.CENTER);
        setPanel.setPreferredSize(new Dimension(setPanelWidth, 460));
        setPanel.setBorder(BorderFactory.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Set")));
        JPanel controlPane = textSetPanel(width, height);
        setPanel.add(controlPane, BorderLayout.NORTH);
        fileTypeComboBox.addActionListener(getFileTypeListener(setPanel, width, height));

        this.add(northPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.EAST);
    }

    private void addToCenterPanel(JPanel centerPanel) {
        localFileRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Local_File") + ":", true);
        urlFileRadioButton = new UIRadioButton("URL:", false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(localFileRadioButton);
        bg.add(urlFileRadioButton);
        localFileRadioButton.addActionListener(radioActionListener);
        urlFileRadioButton.addActionListener(radioActionListener);
        urlFileRadioButton.setForeground(new Color(143, 142, 139));
        localFileRadioButton.setForeground(Color.black);
        localText = new UITextField();
        localText.setPreferredSize(new Dimension(195, 20));
        urlText = new UITextField();
        urlText.setPreferredSize(new Dimension(195, 20));
        urlText.setEditable(false);
        chooseFile = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Selection"));
        chooseFile.addActionListener(chooseFileListener);

        testConnection = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Datasource_Test_Connection"));
        testConnection.setEnabled(false);
        testConnection.addActionListener(testConnectionListener);// 测试连接按钮

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        JPanel textFieldPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        textPanel.add(localFileRadioButton);
        textPanel.add(urlFileRadioButton);
        textFieldPanel.add(localText);
        textFieldPanel.add(urlText);
        buttonPanel.add(chooseFile);
        buttonPanel.add(testConnection);
        filePath = FRGUIPaneFactory.createBorderLayout_S_Pane();
        filePath.add(textPanel, BorderLayout.WEST);
        filePath.add(textFieldPanel, BorderLayout.CENTER);
        filePath.add(buttonPanel, BorderLayout.EAST);
        centerPanel.add(filePath, BorderLayout.NORTH);

        // 中间的pane，提示信息
        String tipContent = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Type_Parameter") + "reportlets/excel/FineReport${abc}." + "txt" + "<br>"
                + "http://192.168.100.120:8080/XXServer/Report/excel${abc}.jsp<br>" + "&nbsp</body> </html> ";
        tips = new UILabel(tipContent);
        centerPanel.add(tips, BorderLayout.CENTER);
    }

    private ActionListener testConnectionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            String uri = ParameterHelper.analyze4Templatee(urlText.getText(), params);
            if (!checkURL(uri)) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(FileTableDataPane.this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add_JS_warning"));
                return;
            }
            params = getEditorPaneParameter();
            URLDataSource url = new URLDataSource(urlText.getText().trim(), params);
            InputStream in = null;
            try {
                in = url.getSourceStream(params);
            } catch (Throwable e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            if (in == null) {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Database_Connection_Failed"),
                        null, 0, UIManager.getIcon("OptionPane.errorIcon"));
            } else {
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Datasource_Connection_Successfully"));
                try {
                    in.close();
                } catch (IOException e) {
                    in = null;
                }
            }
        }
    };

    private void previewPanel(JPanel jPanel) {
        JPanel previewPanel = new JPanel(new BorderLayout());
        UIButton preview = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"));
        preview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preview();
            }
        });
        previewPanel.add(preview, BorderLayout.EAST);
        jPanel.add(previewPanel, BorderLayout.SOUTH);
    }

    private JPanel xmlSetPanel(int width, int height) {
        // xml设置pane
        JPanel controlPane = new JPanel();
        JPanel northPane = new JPanel(new BorderLayout(8, 8));
        JPanel northTopPane = new JPanel(new BorderLayout(8, 8));
        JPanel southPane = new JPanel(new BorderLayout(8, 8));
        JPanel southTopPane = new JPanel(new BorderLayout(8, 8));
        controlPane.setLayout(new BorderLayout(8, 8));
        controlPane.setPreferredSize(new Dimension(width, height));
        JPanel comboboxPanel = new JPanel(new BorderLayout(8, 8));
        encodeLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Encoding_Type") + ":");
        encodingComboBox = new UIComboBox(EncodeConstants.ALL_ENCODING_ARRAY);
        encodingComboBox.setSelectedIndex(4);
        encodingComboBox.setPreferredSize(new Dimension(90, 20));

        JPanel treeContainerPane = new JPanel();
        treeContainerPane.setLayout(new BorderLayout(8, 8));
        nodeTreePane = new XMLNodeTreePane();
        treeContainerPane.add(nodeTreePane, BorderLayout.CENTER);


        comboboxPanel.add(encodeLabel, BorderLayout.WEST);
        comboboxPanel.add(encodingComboBox, BorderLayout.CENTER);

        northPane.add(comboboxPanel, BorderLayout.EAST);
        northTopPane.add(northPane, BorderLayout.WEST);
        southTopPane.add(southPane, BorderLayout.WEST);
        southTopPane.add(treeContainerPane, BorderLayout.CENTER);
        controlPane.add(northTopPane, BorderLayout.NORTH);
        controlPane.add(southTopPane, BorderLayout.CENTER);
        previewPanel(controlPane);
        return controlPane;
    }

    private JPanel excelSetPanel(int width, int height) {
        // excel设置pane
        int checkBoxWidth = width - EIGHT;
        JPanel controlPane = new JPanel();
        JPanel northPane = new JPanel(new BorderLayout(8, 8));
        controlPane.setLayout(new BorderLayout());
        controlPane.setPreferredSize(new Dimension(width, height));
        needColumnNameCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FirstRow_IS_Column_Name"), false);
        needColumnNameCheckBox.setPreferredSize(new Dimension(checkBoxWidth, 20));
        northPane.add(needColumnNameCheckBox, BorderLayout.EAST);
        controlPane.add(northPane, BorderLayout.NORTH);
        previewPanel(controlPane);
        return controlPane;
    }

    private String getFilePathFromUrlOrLocal() {
        if (StringUtils.isNotEmpty(localText.getText()) && localFileRadioButton.isSelected()) {
            return localText.getText().trim();
        } else if (StringUtils.isNotEmpty(urlText.getText()) && urlFileRadioButton.isSelected()) {
            return urlText.getText().trim();
        }
        return "";
    }

    /**
     * 检查链接是否可用
     *
     * @throws Exception
     */
    @Override
    public void checkValid() throws Exception {
        if (urlFileRadioButton.isSelected()) {
            String url = urlText.getText().trim();
            if (!checkURL(url)) {
                throw new Exception(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add_JS_warning"));
            }
        }

    }

    private boolean checkURL(String uri) {
        try {
            new URL(uri);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
        // return (uri.matches("https*://.+|\\$\\{.+\\}.*"));
    }

    private JPanel textSetPanel(int width, int height) {
        // text设置pane
        JPanel controlPane = new JPanel();
        controlPane.setLayout(new BorderLayout());
        controlPane.setPreferredSize(new Dimension(width, height));
        JPanel northPane = new JPanel(new BorderLayout(8, 8));
        addToNorthPane(northPane);
        controlPane.add(northPane, BorderLayout.WEST);
        previewPanel(controlPane);
        return controlPane;
    }

    private void addToNorthPane(JPanel northPane) {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] columnSize = {f, p, p};
        double[] rowSize = {B, B, B, B, B, B, B};
        needColumnNameCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FirstRow_IS_Column_Name"), true);
        dismenberLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Dismenber") + ":");
        tableDismemberRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Table_Dismember"), false);
        tableDismemberRadioButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Table_Dismember"));
        spaceDismenberRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Space"), true);
        spaceDismenberRadioButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Space"));
        commaDismenberRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Comma_Dismenber"), false);
        commaDismenberRadioButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Comma_Dismenber"));
        otherDismenberRadioButton = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Other") + ":", false);
        otherDismenberRadioButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Other"));
        otherDismenberTextField = new UITextField(8);
        otherDismenberTextField.setEditable(false);
        otherDismenberRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                if (otherDismenberRadioButton.isSelected()) {
                    otherDismenberTextField.setEditable(true);
                } else {
                    otherDismenberTextField.setEditable(false);
                }
            }
        });
        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(tableDismemberRadioButton);
        bg2.add(spaceDismenberRadioButton);
        bg2.add(commaDismenberRadioButton);
        bg2.add(otherDismenberRadioButton);
        ignoreOneMoreDelimiterCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Series_Dismenber_As_Single"), true);
        UIComponentUtils.setLineWrap(ignoreOneMoreDelimiterCheckBox);
        encodeLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Encoding_Type") + ":");
        charsetComboBox = new UIComboBox(EncodeConstants.ALL_ENCODING_ARRAY);
        Component[][] comps = {
                {encodeLabel, charsetComboBox, null},
                {needColumnNameCheckBox, null, null},
                {dismenberLabel, tableDismemberRadioButton, null},
                {null, spaceDismenberRadioButton, null},
                {null, commaDismenberRadioButton, null},
                {null, otherDismenberRadioButton, otherDismenberTextField},
                {ignoreOneMoreDelimiterCheckBox, null, null}
        };
        northPane.add(TableLayoutHelper.createTableLayoutPane(comps, rowSize, columnSize), BorderLayout.EAST);
    }

    private ActionListener radioActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (localFileRadioButton.isSelected()) {
                localRadioSelectAction();
                urlFileRadioButton.setForeground(new Color(143, 142, 139));
                localFileRadioButton.setForeground(Color.black);
            } else if (urlFileRadioButton.isSelected()) {
                urlRadioSelectAction();
                localFileRadioButton.setForeground(new Color(143, 142, 139));
                urlFileRadioButton.setForeground(Color.black);
            }
        }
    };

    private void localRadioSelectAction() {
        localFileRadioButton.setSelected(true);
        localText.setEditable(true);
        chooseFile.setEnabled(true);
        urlText.setEditable(false);
        urlText.setText("");
        testConnection.setEnabled(false);
    }

    private void urlRadioSelectAction() {
        urlFileRadioButton.setSelected(true);
        urlText.setEditable(true);
        testConnection.setEnabled(true);
        localText.setEditable(false);
        localText.setText("");
        chooseFile.setEnabled(false);
    }

    private ActionListener chooseFileListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            FILEChooserPane fileChooser = FILEChooserPane.getInstance(true, false, new ChooseFileFilter(getFileSuffix()));
            if (fileChooser.showOpenDialog(FileTableDataPane.this) == FILEChooserPane.OK_OPTION) {
                final FILE file = fileChooser.getSelectedFILE();
                if (file == null) {// 选择的文件不能是 null
                    return;
                }
                localText.setText(file.getPath());
            }

            fileChooser.removeFILEFilter(new ChooseFileFilter(getFileSuffix()));
            if (fileTypeComboBox.getSelectedIndex() == XML) {
                xmlNodeTree.waitRefresh();
                xmlNodeTree.refreshData();
            }
        }
    };

    private String[] getFileSuffix() {
        List<String> suffixList = new ArrayList<String>();
        String suffix = Objects.requireNonNull(fileTypeComboBox.getSelectedItem()).toString().toLowerCase();
        if ("excel".equalsIgnoreCase(suffix)) {
            suffixList.add("xls");
            suffixList.add("xlsx");
        } else {
            suffixList.add(suffix);
        }
        return suffixList.toArray(new String[suffixList.size()]);
    }

    private String getFileSuffixToString() {
        String suffixToString = Objects.requireNonNull(fileTypeComboBox.getSelectedItem()).toString().toLowerCase();
        if ("excel".equalsIgnoreCase(suffixToString)) {
            suffixToString = "xls";
        }
        return suffixToString;
    }

    private ActionListener getFileTypeListener(final JPanel setPanel, final int width, final int height) {
        ActionListener fileTypeListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPanel.removeAll();
                localText.setText("");
                urlText.setText("");
                if (fileTypeComboBox.getSelectedIndex() == XML) {
                    setPanel.add(xmlSetPanel(width, height), BorderLayout.NORTH);
                } else if (fileTypeComboBox.getSelectedIndex() == EXCEL) {
                    setPanel.add(excelSetPanel(width, height), BorderLayout.NORTH);
                } else {
                    setPanel.add(textSetPanel(width, height), BorderLayout.NORTH);
                }
                String tipContent = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Type_Parameter") + "reportlets/excel/FineReport${abc}." + getFileSuffixToString() + "<br>"
                        + "http://192.168.100.120:8080/XXServer/Report/excel${abc}.jsp<br>" + "&nbsp</body> </html> ";
                tips.setText(tipContent);
                setPanel.revalidate();
            }
        };

        return fileTypeListener;
    }

    private class RefreshAction extends UITableEditAction {
        public RefreshAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Refresh"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String[] paramTexts = new String[1];
            paramTexts[0] = getFilePathFromUrlOrLocal();

            List<ParameterProvider> existParameterList = editorPane.update();
            Parameter[] ps = existParameterList == null ? new Parameter[0] : existParameterList.toArray(new Parameter[existParameterList.size()]);

            editorPane.populate(ParameterHelper.analyzeAndUnionParameters(paramTexts, ps, false));
        }

        @Override
        public void checkEnabled() {
            //do nothing
        }
    }

    @Override
    public void populateBean(FileTableData ob) {
        fileTableData = ob;

        if (ob instanceof ExcelTableData) {
            fileTypeComboBox.setSelectedIndex(EXCEL);
            populate2EXCEL((ExcelTableData) ob);
        } else if (ob instanceof XMLTableData) {
            fileTypeComboBox.setSelectedIndex(XML);
            populate2XML((XMLTableData) ob);
        } else if (ob instanceof TextTableData) {
            fileTypeComboBox.setSelectedIndex(TEXT);
            populate2TEXT((TextTableData) ob);
        }
    }

    private void populate2EXCEL(ExcelTableData etd) {
        setTextField(etd);
        editorPane.populate(etd.getParams());
        needColumnNameCheckBox.setSelected(etd.needColumnName());
    }

    //wikky:为了使树能够正常展开，合法的xml文件把path中添加的XML节点去掉，而不合法的xml需要把根节点值还原为添加的ROOTTAG值。
    private void populate2XML(XMLTableData xtd) {
        setTextField(xtd);
        editorPane.populate(xtd.getParams());
        encodingComboBox.setSelectedItem(xtd.getCharSet());
        if (!ComparatorUtils.equals(xtd, new XMLTableData())) {
            xmlNodeTree.initData();
            String[] path = xtd.getXPath();
            String[] paths;
            if (path != null && path.length > 0) {
                DefaultTreeModel treeModel = (DefaultTreeModel) xmlNodeTree.getModel();
                ExpandMutableTreeNode root = (ExpandMutableTreeNode) treeModel.getRoot();
                if (treeModel != null) {
                    if (!ComparatorUtils.equals(treeModel.getRoot().toString(), "")) {
                        paths = new String[path.length - 1];
                        for (int i = 1; i < path.length; i++) {
                            paths[i - 1] = path[i];
                        }
                    } else {
                        paths = path;
                        root.setUserObject(ROOTTAG);
                    }
                    if (treeModel.getRoot() instanceof ExpandMutableTreeNode) {
                        selectNode((ExpandMutableTreeNode) treeModel.getRoot(), 0, paths);
                        if (selectedNode != null) {
                            TreePath treepath = new TreePath(treeModel.getPathToRoot(selectedNode));
                            xmlNodeTree.setSelectionPath(treepath);
                            xmlNodeTree.expandPath(treepath);
                        }
                    }
                    //防止某种操作导致添加的tag作为root出现。
                    if (ComparatorUtils.equals(root.toString(), ROOTTAG)) {
                        root.setUserObject(StringUtils.EMPTY);
                    }
                }
            }
        }
    }

    private void populate2TEXT(TextTableData ttd) {
        setTextField(ttd);
        String delimiter = ttd.getDelimiter();
        if (delimiter == null) {
            return;
        }
        if (ComparatorUtils.equals(delimiter, "\t")) {
            tableDismemberRadioButton.setSelected(true);
        } else if (ComparatorUtils.equals(delimiter, ",")) {
            commaDismenberRadioButton.setSelected(true);
        } else if (ComparatorUtils.equals(delimiter, " ")) {
            spaceDismenberRadioButton.setSelected(true);
        } else {
            otherDismenberRadioButton.setSelected(true);
            otherDismenberTextField.setEditable(true);
            otherDismenberTextField.setText(ttd.getDelimiter());
        }
        editorPane.populate(ttd.getParams());
        needColumnNameCheckBox.setSelected(ttd.needColumnName());
        ignoreOneMoreDelimiterCheckBox.setSelected(ttd.isIgnoreOneMoreDelimiter());
        charsetComboBox.setSelectedItem(ttd.getCharset());
    }

    private void setTextField(FileTableData ob) {
        if (ob.getFilePath() != null) {
            if (ob.getFilePath().indexOf("http") != -1) {
                urlRadioSelectAction();
                urlText.setText(ob.getFilePath());
            } else {
                localRadioSelectAction();
                localText.setText(ob.getFilePath());
            }
        }
    }

    @Override
    public FileTableData updateBean() {
        String filePath = getFilePathFromUrlOrLocal();
        if (StringUtils.isNotBlank(filePath)) {
            this.params = getEditorPaneParameter().length == 0 ? null : getEditorPaneParameter();
            if (fileTypeComboBox.getSelectedIndex() == EXCEL) {
                return update2EXCEL(filePath);
            } else if (fileTypeComboBox.getSelectedIndex() == TEXT) {
                return update2TEXT(filePath);
            } else if (fileTypeComboBox.getSelectedIndex() == XML) {
                return update2XML(filePath);

            }
        }
        return new FileTableData();
    }

    private TextTableData update2TEXT(String filePath) {
        TextTableData ttd = new TextTableData();
        ttd.setFilePath(filePath);
        ttd.setParams(this.params);
        ttd.setDelimiter(this.showDelimiter());
        ttd.setIgnoreOneMoreDelimiter(ignoreOneMoreDelimiterCheckBox.isSelected());
        ttd.setNeedColumnName(needColumnNameCheckBox.isSelected());
        ttd.setCharset((String) charsetComboBox.getSelectedItem());
        fileTableData = ttd;
        return ttd;
    }

    private ExcelTableData update2EXCEL(String filePath) {
        ExcelTableData etd = new ExcelTableData();
        etd.setFilePath(filePath);
        etd.setParams(this.params);
        etd.setNeedColumnName(needColumnNameCheckBox.isSelected());
        fileTableData = etd;
        return etd;
    }

    private XMLTableData update2XML(String filePath) {
        XMLTableData xtd = new XMLTableData();
        xtd.setFilePath(filePath);
        xtd.setParams(this.params);
        if (localFileRadioButton.isSelected()) {
            xtd.setDataSource(new FileDataSource(filePath, this.params));
        } else {
            xtd.setDataSource(new URLDataSource(filePath, this.params));
        }
        xtd.setCharSet((String) encodingComboBox.getSelectedItem());
        finalSelectedNode = selectedNode;
        if (selectedNode instanceof ExpandMutableTreeNode) {
            xmlColumnsList.clear();
            ExpandMutableTreeNode treeNode;
            boolean flag = true;
            for (int i = 0; i < selectedNode.getChildCount(); i++) {
                treeNode = (ExpandMutableTreeNode) selectedNode.getChildAt(i);
                if (treeNode.isLeaf()) {
                    xmlColumnsList.add(treeNode.toString());
                } else {
                    if (flag) {
                        flag = false;
                        finalSelectedNode = treeNode;
                        leafNode(treeNode);
                    }
                }
            }
        }
        String[] paths = getPaths();
        xtd.setXPath(paths);
        XMLColumnNameType[] nameTypes = new XMLColumnNameType[xmlColumnsList.size()];
        for (int i = 0; i < nameTypes.length; i++) {
            nameTypes[i] = new XMLColumnNameType(xmlColumnsList.get(i), 0);
        }
        xtd.setColumns(nameTypes);
        fileTableData = xtd;
        return xtd;
    }

    //wikky:构建树时为了美观把添加的根节点值赋为空显示，现在还得该回去使得预览时能够顺利取到数据。
    private String[] getPaths() {
        TreePath treePath = GUICoreUtils.getTreePath(finalSelectedNode);
        String path = StringUtils.EMPTY;
        if (treePath != null) {
            Object[] paths = treePath.getPath();
            for (int i = 0; i < paths.length; i++) {
                path += "/" + paths[i];
            }
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] paths = path.split("/");
        if (ComparatorUtils.equals(paths[0], StringUtils.EMPTY)) {
            paths[0] = ROOTTAG;
        }
        return paths;
    }

    private void leafNode(ExpandMutableTreeNode treeNode) {
        boolean flag = true;
        ExpandMutableTreeNode firstNode;
        for (int i = 0; i < treeNode.getChildCount(); i++) {
            firstNode = (ExpandMutableTreeNode) treeNode.getChildAt(i);
            if (firstNode.isLeaf()) {
                xmlColumnsList.add(firstNode.toString());
            } else {
                if (flag) {
                    flag = false;
                    finalSelectedNode = treeNode;
                    leafNode(firstNode);
                }
            }
        }
    }

    private void selectNode(ExpandMutableTreeNode node, int layer, String[] paths) {
        if (selectedNode != null || node == null) {
            return;
        }
        if (layer < paths.length && paths[layer] != null && ComparatorUtils.equals(paths[layer], node.getUserObject())) {
            if (layer == paths.length - 1) {
                selectedNode = node;
                return;
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                selectNode((ExpandMutableTreeNode) node.getChildAt(i), layer + 1, paths);
            }
        }
    }

    private Parameter[] getEditorPaneParameter() {
        String[] paramTexts = new String[1];
        paramTexts[0] = getFilePathFromUrlOrLocal();

        List<ParameterProvider> existParameterList = editorPane.update();
        Parameter[] ps = existParameterList == null ? new Parameter[0] : existParameterList.toArray(new Parameter[existParameterList.size()]);

        return ParameterHelper.analyzeAndUnionParameters(paramTexts, ps, false);
    }

    private String showDelimiter() {
        if (tableDismemberRadioButton.isSelected()) {
            return "\t";
        } else if (commaDismenberRadioButton.isSelected()) {
            return ",";
        } else if (spaceDismenberRadioButton.isSelected()) {
            return " ";
        } else {
            String other = otherDismenberTextField.getText();
            if (StringUtils.isEmpty(other)) {
                other = " ";
            }
            return other;
        }
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tabledata_Type_File");
    }

    private void preview() {
        if (this.fileTableData == null) {
            return;
        }
        PreviewTablePane.previewTableData(this.updateBean());
    }

    private class XMLNodeTreePane extends BasicPane {
        private RefreshParameterAction refreshAction;

        public XMLNodeTreePane() {
            this.initComponents();
        }

        protected void initComponents() {
            JPanel toolbarPanel = new JPanel(new BorderLayout());
            this.setLayout(new BorderLayout());
            xmlNodeTree = new XMLNodeTree();
            this.add(new JScrollPane(xmlNodeTree));

            keyPointLaber = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Key_Point") + ":");
            refreshAction = new RefreshParameterAction();
            ToolBarDef toolbarDef = new ToolBarDef();
            toolbarDef.addShortCut(refreshAction);
            UIToolbar toolBar = ToolBarDef.createJToolBar();
            toolbarDef.updateToolBar(toolBar);
            toolbarPanel.add(keyPointLaber, BorderLayout.WEST);
            toolbarPanel.add(toolBar, BorderLayout.EAST);
            this.add(toolbarPanel, BorderLayout.NORTH);
        }

        @Override
        protected String title4PopupWindow() {
            return null;
        }

        private class RefreshParameterAction extends UpdateAction {
            public RefreshParameterAction() {
                this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Refresh"));
                this.setMnemonic('r');
                this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                xmlNodeTree.waitRefresh();
                xmlNodeTree.refreshData();
            }
        }
    }

    private class XMLNodeTree extends JTree {
        private DefaultTreeModel xmlTreeModel;

        private DefaultTreeModel waitTreeModel = null;

        public XMLNodeTree() {
            this.initComponents();
            this.setModel(null);
        }

        protected void initComponents() {
            this.putClientProperty("JTree.lineStyle", "Angled");
            this.setShowsRootHandles(true);
            this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            this.addMouseListener(treeMouseListener);


            this.setEditable(false);
        }

        private MouseListener treeMouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (XMLNodeTree.this.getModel() != xmlTreeModel) {
                    return;
                }
                int selRow = XMLNodeTree.this.getRowForLocation(e.getX(), e.getY());
                if (selRow == -1) {//没有选中某个树节点，就直接返回啦
                    return;
                }
                TreePath selPath = XMLNodeTree.this.getPathForLocation(e.getX(), e.getY());
                if (selPath == null || selPath.getLastPathComponent() == null) {
                    return;//没有选中某个树节点，就直接返回啦
                }
                Object selObject = selPath.getLastPathComponent();
                if (selObject instanceof ExpandMutableTreeNode) {
                    ExpandMutableTreeNode expandMutableTreeNode = (ExpandMutableTreeNode) selObject;
                    if (!expandMutableTreeNode.isLeaf()) {
                        selectedNode = expandMutableTreeNode;
                    } else {
                        selectedNode = (ExpandMutableTreeNode) expandMutableTreeNode.getParent();
                    }
                }
            }
        };

        public void waitRefresh() {
            // 换个root，等待用户点击root节点后，展开刷新
            if (waitTreeModel == null) {
                ExpandMutableTreeNode rootTreeNode = new ExpandMutableTreeNode();
                rootTreeNode.setExpanded(false);
                rootTreeNode.setAllowsChildren(false);

                waitTreeModel = new DefaultTreeModel(rootTreeNode);
            }

            this.setModel(waitTreeModel);
        }

        public DefaultTreeModel getTreeModel() {
            return xmlTreeModel;
        }

        //防止界面卡死。
        public void refreshData() {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    XMLNodeTree.this.initData();
                }
            });
            thread.start();
        }

        //wikky:为满足706设计时对不合法的xml文件（有多个根节点）的处理，把拿到的InputStream强制在最外层添加<XML></XML>作为唯一根节点而将文件转换为合法的xml。
        private void initData() {
            params = getEditorPaneParameter();  // 生成tree结构放哪儿呢？放这里感觉不对撒
            xmlTreeModel = null;
            selectedNode = null;
            xmlColumnsList.clear();
            DataSource dataSource = null;
            if (localFileRadioButton.isSelected()) {
                String localTextString = StringUtils.trimToNull(localText.getText());
                if (StringUtils.isEmpty(localTextString)) {
                    FineLoggerFactory.getLogger().info("The file path is empty.");
                    loadedTreeModel();
                    return;
                }
                dataSource = new FileDataSource(localTextString, params);
            } else {
                String urlTextString = StringUtils.trimToNull(urlText.getText());
                if (StringUtils.isEmpty(urlTextString)) {
                    FineLoggerFactory.getLogger().info("The url path is empty.");
                    loadedTreeModel();
                    return;
                }
                dataSource = new URLDataSource(urlTextString, params);
            }
            try {
                InputStream in, input;
                if ((in = dataSource.getSourceStream(params)) != null) {
                    String xmlString = Utils.inputStream2String(in, (String) encodingComboBox.getSelectedItem());
                    String stringXml = addTag(xmlString);
                    input = new ByteArrayInputStream(stringXml.getBytes((String) encodingComboBox.getSelectedItem()));
                    InputStreamReader reader = new InputStreamReader(input, (String) encodingComboBox.getSelectedItem());
                    XMLableReader xmlReader = XMLableReader.createXMLableReader(reader);
                    if (xmlReader != null) {
                        xmlReader.readXMLObject(new XMLLayerReader(0));
                    } else {
                        FineLoggerFactory.getLogger().info("The file is wrong or bad, can not create the XMLReader.");
                        loadedTreeModel();
                    }
                    reader.close();
                }
            } catch (Throwable e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
                loadedTreeModel();
            }
            if (xmlTreeModel == null) {
                FineLoggerFactory.getLogger().info("The file is wrong or bad, can not create the XMLReader.");
                return;
            }
            if (xmlTreeModel.getChildCount(xmlTreeModel.getRoot()) == 1) {
                xmlTreeModel = new DefaultTreeModel((ExpandMutableTreeNode) xmlTreeModel.getChild(xmlTreeModel.getRoot(), 0));
            } else {
                ExpandMutableTreeNode root = (ExpandMutableTreeNode) xmlTreeModel.getRoot();
                root.setUserObject(StringUtils.EMPTY);
            }
            this.setModel(xmlTreeModel);
        }

        private void loadedTreeModel() {
            ExpandMutableTreeNode rootTreeNode = new ExpandMutableTreeNode(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Loaded_Tree_Model"));
            rootTreeNode.setExpanded(false);
            rootTreeNode.setAllowsChildren(false);
            DefaultTreeModel loadedTreeModel = new DefaultTreeModel(rootTreeNode);
            XMLNodeTree.this.setModel(loadedTreeModel);
        }

        private String addTag(String string) {
            String stringWithTag;
            int beginIndex = 0;
            int firstIndex = string.indexOf(">");
            int endIndex = string.length();
            String firstPart = string.substring(beginIndex, firstIndex + 1);
            String secondPart = STARTTAG;
            String thirdPart = string.substring(firstIndex + 1, endIndex);
            String lastPart = ENDTAG;
            stringWithTag = firstPart + secondPart + thirdPart + lastPart;
            return stringWithTag;
        }

        private class XMLLayerReader implements XMLReadable {
            private int layer = -1;
            private ExpandMutableTreeNode parentNode;
            private ExpandMutableTreeNode currentNode;

            public XMLLayerReader(int layer) {
                this.parentNode = null;
                this.layer = layer;
            }

            public XMLLayerReader(ExpandMutableTreeNode parentNode, int layer) {
                this.parentNode = parentNode;
                this.layer = layer;
            }

            @Override
            public void readXML(XMLableReader reader) {
                String nodeName;
                if (this.layer < 0) {
                    return;
                }
                if (reader.isAttr()) {
                    nodeName = reader.getTagName();
                    if (nodeName == null) {
                        return;
                    }
                    currentNode = new ExpandMutableTreeNode(nodeName);
                    if (layer == 0) {
                        xmlTreeModel = new DefaultTreeModel(currentNode);
                    } else {
                        boolean conflict = false;
                        for (int i = 0; i < parentNode.getChildCount(); i++) {
                            ExpandMutableTreeNode node = (ExpandMutableTreeNode) parentNode.getChildAt(i);
                            if (node != null && nodeName.equals(node.getUserObject())) {
                                conflict = true;
                                currentNode = node;
                            }
                        }
                        if (!conflict) {
                            parentNode.add(currentNode);
                        }
                    }
                }

                if (reader.isChildNode()) {
                    if (currentNode == null) {
                        return;
                    }
                    reader.readXMLObject(new XMLLayerReader(currentNode, layer + 1));
                }
            }
        }
    }

}
