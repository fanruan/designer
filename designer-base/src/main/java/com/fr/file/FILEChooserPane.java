package com.fr.file;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.extension.FileExtension;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.env.DesignerWorkspaceType;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.fun.ReportSupportedFileUIProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonUI;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.DefaultCompletionFilter;
import com.fr.design.gui.itextfield.UIAutoCompletionField;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itree.filetree.FileTreeIcon;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.filetree.FileNode;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.file.filter.FILEFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.log.FineLoggerFactory;
import com.fr.report.ExtraReportClassManager;
import com.fr.report.fun.ReportSupportedFileProvider;
import com.fr.stable.CoreConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.os.windows.WindowsDetector;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * FileChooserPane要高亮显示某Button,以显示当前路径
 * 边距要调整
 * postfix还没有处理
 * 该文件选择器，整理行为如下：
 */
public class FILEChooserPane extends BasicPane {

    /**
     * Return value if OK is chosen.
     */
    public static final int OK_OPTION = 0;
    /**
     * Return value if CANCEL is chosen.
     */
    public static final int CANCEL_OPTION = 1;

    public static final int JOPTIONPANE_OK_OPTION = 2;

    public static final int JOPTIONPANE_CANCEL_OPTION = 3;


    /**
     * alex:之所以在Pattern那里加个+,是因为有些路径会有两个甚至多个分隔符放在一起
     */
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile("[/\\\\]+");

    private static final FILEChooserPane INSTANCE = new FILEChooserPane();

    private FILE currentDirectory; // 当前路径,在subFileList中显示这个路径下所有的文件

    private List<FILEFilter> filterList = new ArrayList<>();
    private FILEFilter filter;

    private LocationButtonPane locationBtnPane; // 显示location的Panel
    private UIButton mkdirButton;

    private JList placesList; // File.listRoots() + Env + Favourite
    private JList subFileList; // 当前选中目录下的文件夹及文件

    private JScrollPane scrollPane;
    private UIAutoCompletionField fileNameTextField; // 文件名的文本框
    private UIComboBox postfixComboBox; // 文件后缀名的下拉列表框

    private UIButton okButton;


    protected int type;
    protected boolean showEnv;
    protected boolean showLoc;
    protected boolean showWebReport = false;

    private UIDialog dialog;

    private int option = CANCEL_OPTION;

    protected String suffix;

    /**
     * @return
     */
    public static FILEChooserPane getInstance() {
        return getInstance(true, true);
    }

    /**
     * @param showEnv
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv) {
        return getInstance(showEnv, true);
    }

    /**
     * @param showEnv
     * @param showLoc
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv, boolean showLoc) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = false;
        INSTANCE.setPlaceListModel();
        INSTANCE.removeAllFilter();
        return INSTANCE;
    }

    public static FILEChooserPane getMultiEnvInstance(boolean showLoc, boolean showWebReport, FILEFilter filter) {
        INSTANCE.showEnv = true;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = showWebReport;
        // 替换掉 PlaceListModel
        INSTANCE.setMultiPlaceListModel();
        INSTANCE.removeAllFilter();
        INSTANCE.addChooseFILEFilter(filter, 0);
        return INSTANCE;
    }

    public static FILEChooserPane getMultiEnvInstance(boolean showLoc, boolean showWebReport) {
        INSTANCE.showEnv = true;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = showWebReport;
        // 替换掉 PlaceListModel
        INSTANCE.setMultiPlaceListModel();
        INSTANCE.removeAllFilter();
        return INSTANCE;
    }


    /**
     * @param showEnv
     * @param filter
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv, FILEFilter filter) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.setPlaceListModel();
        INSTANCE.removeAllFilter();
        INSTANCE.addChooseFILEFilter(filter, 0);
        return INSTANCE;
    }

    /**
     * @param showEnv
     * @param showLoc
     * @param filter
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv, boolean showLoc, FILEFilter filter) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = false;
        INSTANCE.setPlaceListModel();
        INSTANCE.removeAllFilter();
        INSTANCE.addChooseFILEFilter(filter, 0);
        return INSTANCE;
    }

    /**
     * @param showEnv
     * @param showLoc
     * @param showWebReport
     * @param filter
     * @return
     */
    public static FILEChooserPane getInstance(boolean showEnv, boolean showLoc, boolean showWebReport, FILEFilter filter) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = showWebReport;
        INSTANCE.setPlaceListModel();
        INSTANCE.removeAllFilter();
        INSTANCE.addChooseFILEFilterToFist(filter, 0);
        return INSTANCE;
    }

    private FILEChooserPane() {
        this.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        // kel:support Esc key.
        InputMap inputMapAncestor = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = this.getActionMap();

        // transfer focus to CurrentEditor
        inputMapAncestor.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "dialogExit");
        actionMap.put("dialogExit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                option = CANCEL_OPTION;
                dialogExit();
            }
        });

        JPanel locationPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // locationPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        locationPane.add(locationBtnPane = new LocationButtonPane(), BorderLayout.CENTER);

        mkdirButton = initMkdirButton();
        locationPane.add(mkdirButton, BorderLayout.EAST);

        JPanel centerLeftPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // centerLeftPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        // Richie:placesList includes C,D,E,F and DeskTop etc.
        placesList = new JList();
        centerLeftPanel.add(placesList, BorderLayout.CENTER);
        centerLeftPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        centerLeftPanel.setPreferredSize(new Dimension(120, 1));
        placesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        placesList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof FILE) {
                    FILE dir = (FILE) value;

                    String name = dir.getName();
                    if (name != null && !StringUtils.isBlank(name)) {
                        this.setText(name);
                    } else {
                        this.setText(GeneralContext.getCurrentAppNameOfEnv());
                    }
                    Icon icon = dir.getIcon();
                    if (icon != null) {
                        this.setIcon(icon);
                    }
                }

                return this;
            }

        });
        // placeList listener
        placesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object selValue = placesList.getSelectedValue();
                if (selValue instanceof FILE) {
                    setSelectedDirectory((FILE) selValue);
                }
            }
        });
        /*
         * placeList mouseListener
         */
        placesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Object selValue = placesList.getSelectedValue();
                if (selValue instanceof FILE) {
                    setSelectedDirectory((FILE) selValue);
                }
            }
        });

        // centerRightPane
        JPanel centerRightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel subFilePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // 以后rightPanel要用JTable
        subFileList = new JList(new DefaultListModel<>());
        /*
         * JList的CellRenderer
         */
        subFileList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof FILE) {
                    FILE dir = (FILE) value;

                    String name = dir.getName();
                    if (name != null) {
                        this.setText(name);
                    }
                    Icon icon = dir.getIcon();
                    if (icon != null) {
                        this.setIcon(icon);
                    }
                }

                return this;
            }

        });
        /*
         * 鼠标点击JList时的listener
         */
        subFileList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Object source = e.getSource();
                if (!(source instanceof JList)) {
                    return;
                }

                if (e.getClickCount() < 1) {
                    return;
                }

                JList list = (JList) source;

                Object selValue = list.getSelectedValue();
                if (selValue instanceof FILE) {
                    if (e.getClickCount() == 1) {
                        fileNameTextField.removeDocumentListener();
                        setFileTextField((FILE) selValue);
                        fileNameTextField.addDocumentListener();
                    } else {
                        setSelectedDirectory((FILE) selValue);
                        if (!((FILE) selValue).isDirectory()) {
                            doOK();
                        }
                    }
                }
            }
        });
        /*
         * right list.
         */
        // Richie:按下Enter的时候打开文件夹或者打开文件
        subFileList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                Object source = e.getSource();
                if (!(source instanceof JList)) {
                    return;
                }

                JList list = (JList) source;

                Object selValue = list.getSelectedValue();
                if (selValue instanceof FILE) {
                    setFileTextField((FILE) selValue);
                    // if (((FILE)selValue).isDirectory()) {
                    // fileNameTextField.setText("");
                    // }
                    // Richie:按下Enter的时候打开文件夹或者打开文件
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        setSelectedDirectory((FILE) selValue);
                        if (!((FILE) selValue).isDirectory()) {
                            doOK();
                        }
                    }
                }

            }
        });
        scrollPane = new JScrollPane(subFileList);
        subFilePanel.add(scrollPane, BorderLayout.CENTER);
        centerRightPane.add(subFilePanel, BorderLayout.CENTER);

        // 用createTableLayoutPane布局下fileNamePane
        UIButton cancelButton;
        Component[][] coms = new Component[][]{
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Toolkit.i18nText("Fine-Design_Basic_Utils_File_Name") + ":"), BorderLayout.WEST),
                        fileNameTextField = new UIAutoCompletionField(), okButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Utils_Design_File_Open"))
                },
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Toolkit.i18nText("Fine-Design_Report_Utils_File_Type") + ":"), BorderLayout.WEST),
                        postfixComboBox = new UIComboBox(), cancelButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Utils_Design_Action_Cancel"))
                }};

        JPanel fileNamePane = TableLayoutHelper.createGapTableLayoutPane(coms, new double[]{TableLayout.PREFERRED, TableLayout.PREFERRED,
                TableLayout.PREFERRED}, new double[]{TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED}, 24, 4);
        centerRightPane.add(fileNamePane, BorderLayout.SOUTH);

        Component[][] outComponents = new Component[][]{
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Toolkit.i18nText("Fine-Design_Basic_App_File_Lookup_range") + ":"), BorderLayout.WEST), locationPane},
                new Component[]{centerLeftPanel, centerRightPane}};
        JPanel contentPane = TableLayoutHelper.createTableLayoutPane(outComponents, new double[]{TableLayout.PREFERRED, TableLayout.FILL},
                new double[]{TableLayout.PREFERRED, TableLayout.FILL});
        this.add(contentPane, BorderLayout.CENTER);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                doOK();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                option = CANCEL_OPTION;
                doCancel();
            }
        });
        fileNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    option = CANCEL_OPTION;
                    doOK();
                }
            }
        });
        postfixComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                Object ss = postfixComboBox.getSelectedItem();
                if (ss instanceof ChooseFileFilter) {
                    setFILEFilter((ChooseFileFilter) ss);
                    if (fileNameTextField.isShowing()) {
                        fileNameTextField.setText(calProperFileName(fileNameTextField.getText(), (ChooseFileFilter) ss));
                    }
                } else {
                    setFILEFilter(null);
                }
            }
        });
    }

    private String calProperFileName(String fileName, ChooseFileFilter fileFilter) {
        if(fileFilter == null){
            return fileName;
        }
        String filterExtension = fileFilter.getExtensionString();
        int lastDotIndex = fileName.lastIndexOf(".") != -1 ? fileName.lastIndexOf(".") : fileName.length();
        String fileNameWithOutExtension = fileName.substring(0, lastDotIndex);
        String fileNameExtension = fileName.substring(lastDotIndex);
        FileExtension fileExtension = FileExtension.parse(fileNameExtension);
        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(filterExtension) || fileFilter.containsExtension(fileExtension.getExtension())) {
            return fileName;
        }
        return fileNameWithOutExtension + filterExtension;
    }

    private void doCancel() {
        this.locationBtnPane.setPopDir(null);
        dialogExit();
    }

    /**
     * @param showEnv
     * @param showLoc
     */
    public FILEChooserPane(boolean showEnv, boolean showLoc) {
        this();
        this.showEnv = showEnv;
        this.showLoc = showLoc;
        this.setPlaceListModel();
    }


    /**
     * 返回选中的FILE
     *
     * @return
     */
    public FILE getSelectedFILE() {
        String fileName = fileNameTextField.getText().trim();
        if (!fileName.endsWith(suffix) && !fileName.contains(CoreConstants.DOT)) {
            fileName += this.suffix;
        }
        if (currentDirectory == null) {
            return null;
        }
        if (currentDirectory instanceof FileFILE) {
            return new FileFILE((FileFILE) currentDirectory, fileName);
        } else {
            return new FileNodeFILE((FileNodeFILE) currentDirectory, fileName, false);
        }
    }

    protected String getEnvProjectName() {
        return Toolkit.i18nText("Fine-Design_Basic_Utils_Report_Env_Directory");
    }

    /**
     * 增加文件过滤器
     *
     * @param filter 过滤器
     */
    public void addChooseFILEFilter(FILEFilter filter) {
        addChooseFILEFilter(filter, filterList.size());
    }


    /**
     * 在指定index增加过滤器
     *
     * @param filter 过滤器
     * @param index  序号
     */
    public void addChooseFILEFilter(FILEFilter filter, int index) {
        if (filterList.contains(filter)) {
            return;
        }
        this.filterList.add(index, filter);
    }

    /**
     * 若是已经存在，则将之删去之后，在指定的位置增加
     *
     * @param filter 过滤
     * @param index  序号
     */
    public void addChooseFILEFilterToFist(FILEFilter filter, int index) {
        if (filterList.contains(filter)) {
            filterList.remove(filter);
            filterList.add(index, filter);
            return;
        }
        this.filterList.add(index, filter);
    }

    /**
     * 删除文件过滤器
     * 这命名太乱了，完全是误导
     *
     * @param filter 过滤
     */
    public void removeFILEFilter(FILEFilter filter) {
        if (filterList.contains(filter)) {
            this.filterList.remove(filter);
        }
    }

    /**
     * 删掉全部的过滤器
     */
    public void removeAllFilter() {
        this.filterList.clear();
    }


    /**
     * 设置filter,刷新右侧subFileList中的items
     *
     * @param filter 过滤
     */
    public void setFILEFilter(FILEFilter filter) {
        this.filter = filter;

        refreshSubFileListModel();
    }

    /**
     * richer:默认的话就使用.cpt作为后缀名
     *
     * @param text   文本
     * @param suffix 后缀
     */
    public void setFileNameTextField(String text, String suffix) {
        if (StringUtils.isEmpty(suffix)) {
            suffix = FileExtension.CPT.getSuffix();
        }
        this.suffix = suffix;

        if (!text.endsWith(suffix)) {
            text = text + suffix;
        }
        fileNameTextField.removeDocumentListener();
        fileNameTextField.setText(text);
        fileNameTextField.addDocumentListener();
        if (currentDirectory == null) {
            return;
        }
        FILE[] files = currentDirectory.listFiles();
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }
        fileNameTextField.setFilter(new DefaultCompletionFilter(names));
    }


    /**
     * 打开对话框
     *
     * @param parent 父类
     * @return 类型
     */
    public int showOpenDialog(Component parent) {
        return showOpenDialog(parent, FileExtension.CPT.getSuffix());
    }

    /**
     * 打开对话框
     *
     * @param parent 父类
     * @param suffix 后缀
     * @return 类型
     */
    public int showOpenDialog(Component parent, String suffix) {
        return showDialog(parent, JFileChooser.OPEN_DIALOG, suffix);
    }

    /**
     * 打开对话框
     *
     * @param parent 父类
     * @return 类型
     */
    public int showSaveDialog(Component parent) {
        return showSaveDialog(parent, FileExtension.CPT.getSuffix());
    }

    /**
     * 打开对话框
     *
     * @param parent 父类
     * @param suffix 后缀
     * @return 类型
     */
    public int showSaveDialog(Component parent, String suffix) {
        return showDialog(parent, JFileChooser.SAVE_DIALOG, suffix);
    }

    /**
     * august:控件的事件都在这里面添加的 那么我每次showDialog，不都要重复添加一次事件吗？无语了
     *
     * @param parent 父类
     * @param type   类型
     * @param suffix 后缀
     * @return 类型
     */
    public int showDialog(Component parent, int type, String suffix) {
        this.type = type;
        this.suffix = suffix;

        dialog = showWindow(parent instanceof DesignerFrame ? (Window) parent : SwingUtilities.getWindowAncestor(parent), false);
        JPanel contentPane = (JPanel) dialog.getContentPane();
        contentPane.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        okButton.setText(dialogName());
        // kel:打开界面的时候让文本域获得焦点，支持enter打开或保存。
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                fileNameTextField.requestFocusInWindow();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                option = CANCEL_OPTION;
                dialogExit();
            }
        });

        // neil:默认打开pane里显示所有支持的格式
        // daniel 从templateFileTree中取
        if (!showWebReport && filterList.isEmpty()) {
            fileType();
        }
        chooseType();
        // richer:当文件类型被选定时,显示的文件就仅仅显示被选定的类型
        // 如果是保存对话框,给个默认名字
        if (type != JFileChooser.SAVE_DIALOG) {
            fileNameTextField.removeDocumentListener();
            fileNameTextField.setText("");
            fileNameTextField.addDocumentListener();
        }
        dialog.setVisible(true);
        return option;
    }

    protected void fileType() {
        String appName = ProductConstants.APP_NAME;
        JTemplate editing = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (editing == null || !editing.isChartBook()) {

            if (type == JFileChooser.OPEN_DIALOG) {
                ChooseFileFilter supportedTypes = new ChooseFileFilter(FRContext.getFileNodes().getSupportedTypes(), appName + Toolkit.i18nText("Fine-Design_Report_Template_File"));
                Set<ReportSupportedFileProvider> providers = ExtraReportClassManager.getInstance().getArray(ReportSupportedFileProvider.XML_TAG);
                for (ReportSupportedFileProvider provider : providers) {
                    for (FileExtension fileExtension : provider.getFileExtensions()){
                        supportedTypes.addExtension(fileExtension.getExtension());
                    }
                }
                this.addChooseFILEFilter(supportedTypes);
            }

            // ben:filefilter设置初值为cpt过滤
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.CPT, appName + Toolkit.i18nText("Fine-Design_Report_Template_File")));

            // richer:form文件 daniel 改成三个字
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.FRM, appName + Toolkit.i18nText("Fine-Design_Report_Template_File")));

            Set<ReportSupportedFileUIProvider> providers = ExtraDesignClassManager.getInstance().getArray(ReportSupportedFileUIProvider.XML_TAG);
            for (ReportSupportedFileUIProvider provider : providers) {
                provider.addChooseFileFilter(this, StringUtils.EMPTY);
            }
        } else {
            if (type == JFileChooser.OPEN_DIALOG) {
                this.addChooseFILEFilter(new ChooseFileFilter(EnumSet.of(FileExtension.XLS, FileExtension.XLSX), Toolkit.i18nText("Fine-Design_Basic_Import_Excel_Source")));
            }
        }

        // 添加 xls 文件类型过滤 kt
        if (WorkContext.getCurrent().isLocal()) {  //本地连接
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.XLS, Toolkit.i18nText("Fine-Design_Basic_Import_Excel_Source")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.XLSX, Toolkit.i18nText("Fine-Design_Basic_Import_Excel2007_Source")));
        }
        if (FileExtension.PNG.matchExtension(suffix)) {
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.PNG, Toolkit.i18nText("Fine-Design_Basic_App_Export_png")));
        }
        if (type == JFileChooser.SAVE_DIALOG) {
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.PDF, Toolkit.i18nText("Fine-Design_Basic_Import_Export_PDF")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.SVG, Toolkit.i18nText("Fine-Design_Basic_Import_Export_SVG")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.CSV, Toolkit.i18nText("Fine-Design_Basic_Import_Export_CSV")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.DOC, Toolkit.i18nText("Fine-Design_Basic_Import_Export_Word")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.TXT, Toolkit.i18nText("Fine-Design_Basic_Import_Export_Text")));
        }

    }

    private void chooseType() {
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) postfixComboBox.getModel();
        defaultComboBoxModel.removeAllElements();
        for (FILEFilter filter : filterList) {
            defaultComboBoxModel.addElement(filter);
        }
        if (WorkContext.getCurrent().isLocal()) {  //本地连接
            if (!showWebReport) {
                defaultComboBoxModel.addElement(Toolkit.i18nText("Fine-Design_Basic_Utils_App_AllFiles") + "(*.*)");
            }
        }
        // 默认选取的文件类型(.cpt)类型
        if (!filterList.isEmpty()) {
            setFILEFilter(filterList.get(0));
            defaultComboBoxModel.setSelectedItem(filterList.get(0));
        }
        // richer:根据不同的文件类型显示不同的后缀名
        EnumSet<FileExtension> fileExtensions = EnumSet.of(
                FileExtension.CPT, FileExtension.CPTX, FileExtension.FRM, FileExtension.FRMX,
                FileExtension.XLS, FileExtension.XLSX, FileExtension.PDF, FileExtension.SVG,
                FileExtension.CSV, FileExtension.DOC, FileExtension.TXT, FileExtension.PNG);
        for (FileExtension fileExtension : fileExtensions) {
            if (fileExtension.matchExtension(suffix)) {
                postfixComboBox.setSelectedIndex(suffixIndex(fileExtension.getExtension()));
                break;
            }
        }
        //jerry 26216 只保留.cpt .frm有用的格式，并且不可编辑
        postfixComboBox.setEnabled(true);
        //只有一个类型时不可下拉
        if (filterList.size() == 1) {
            postfixComboBox.setEnabled(false);
        }
    }

    private int suffixIndex(String extension) {
        for (int i = 0; i < filterList.size(); i++) {
            FILEFilter fileFilter = filterList.get(i);
            if (fileFilter.containsExtension(extension)) {
                return i;
            }
        }
        return 0;
    }


    private void doOK() {
        // 如果没有写文件名,不允许打开 or save
        if (fileNameTextField.getText().length() == 0) {
            return;
        }
        if (type == JFileChooser.OPEN_DIALOG) {
            if (this.subFileList.getSelectedValue() == null) {
                FILE file = this.getSelectedFILE();
                if (file.exists()) {
                    option = OK_OPTION;
                    saveDictionary();
                    dialogExit();
                } else {
                    FineJOptionPane.showMessageDialog(this, Toolkit.i18nText("Fine-Design_Basic_App_Template_Report_Not_Exist"));
                    return;
                }
            }

            // alex:NPE判断,因为可能没有选中子目录,直接在文本框中输入文件名
            FILE selectedSubFile = (FILE) this.subFileList.getSelectedValue();
            if (selectedSubFile != null && selectedSubFile.isDirectory()) {
                setSelectedDirectory((FILE) this.subFileList.getSelectedValue());
            } else {
                option = OK_OPTION;
                saveDictionary();
                dialogExit();
            }
        } else if (type == JFileChooser.SAVE_DIALOG) {
            saveDialog();

        }
    }


    private void saveDialog() {
        String filename = fileNameTextField.getText();
        filename = calProperFileName(filename, (ChooseFileFilter) (postfixComboBox.getSelectedItem()));
        fileNameTextField.setText(filename);
        option = OK_OPTION;
        FILE selectedFile = this.getSelectedFILE();

        if (access(selectedFile) && access(currentDirectory)) {
            if (selectedFile.exists()) {
                int selVal = FineJOptionPane.showConfirmDialog(dialog, Toolkit.i18nText("Fine-Design_Basic_Utils_Would_You_Like_To_Cover_The_Current_File") + " ?",
                        Toolkit.i18nText("Fine-Design_Basic_Confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (selVal == JOptionPane.YES_OPTION) {
                    option = JOPTIONPANE_OK_OPTION;
                    saveDictionary();
                    dialogExit();
                } else {
                    option = JOPTIONPANE_CANCEL_OPTION;
                }

            } else {
                dialogExit();
                saveDictionary();
            }
        } else {
            FineJOptionPane.showMessageDialog(FILEChooserPane.this, Toolkit.i18nText("Fine-Design_Basic_App_Privilege_No") + "!", Toolkit.i18nText("Fine-Design_Basic_App_File_Message"), JOptionPane.WARNING_MESSAGE);

        }
    }


    private boolean access(FILE selectedFile) {
        boolean access = false;
        try {
            access = FRContext.getOrganizationOperator().canAccess(selectedFile.getPath());
            if (selectedFile.isEnvFile() && selectedFile instanceof FileNodeFILE) {
                access = access && ((FileNodeFILE) selectedFile).hasFullAuth();
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return access;
    }

    private void saveDictionary() {
        FILE selectedFile = this.getSelectedFILE();
        if (selectedFile != null) {
            DesignerEnvManager designerEnvManager = DesignerEnvManager.getEnvManager();
            String prefix = currentDirectory.prefix();
            String path = currentDirectory.getPath();
            designerEnvManager.setCurrentDirectoryPrefix(prefix);
            designerEnvManager.setDialogCurrentDirectory(path);
            designerEnvManager.saveXMLFile();
        }
    }

    private void dialogExit() {

        if (dialog == null) {
            return;
        }
        dialog.setVisible(false);
        dialog.dispose();
        dialog = null;
    }

    /*
     * dialog的名字
     */
    private String dialogName() {
        return type == JFileChooser.OPEN_DIALOG ? Toolkit.i18nText("Fine-Design_Basic_Utils_Design_File_Open") : Toolkit.i18nText("Fine-Design_Basic_App_Template_Save");
    }

    /*
     * 在subFileList中选中文件
     */
    private void setSelectedFileName(String name) {
        if (name == null) {
            return;
        }

        ListModel model = subFileList.getModel();
        for (int i = 0, len = model.getSize(); i < len; i++) {
            if (ComparatorUtils.equals(name, ((FILE) model.getElementAt(i)).getName())) {
                subFileList.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    protected String title4PopupWindow() {
        return dialogName();
    }

    private void setPlaceListModel(AbstractPlaceListModel model) {
        if (placesList == null) {
            return;
        }
        placesList.setModel(model);
        String lastDirectoryPath = DesignerEnvManager.getEnvManager().getDialogCurrentDirectory();
        String prefix = DesignerEnvManager.getEnvManager().getCurrentDirectoryPrefix();
        FILE lastDirectory = FILEFactory.createFolder(prefix + lastDirectoryPath);

        model.setCD(lastDirectory);

        if (currentDirectory != null) {
            return;
        }
        // b:这里应该是currentDirectory为空时尝试从envmanager取，最后FileSystemView，和this.env有啥关系
        if (StringUtils.isNotBlank(DesignerEnvManager.getEnvManager().getDialogCurrentDirectory())) {
            currentDirectory = FILEFactory.createFolder(DesignerEnvManager.getEnvManager().getDialogCurrentDirectory());
        } else {
            currentDirectory = FILEFactory.createFolder(FileSystemView.getFileSystemView().getHomeDirectory().getPath());
        }
        if (currentDirectory != null && currentDirectory.exists()) {
            this.setCurrentDirectory(currentDirectory);
        }
    }

    private void setPlaceListModel() {
        if (placesList == null) {
            return;
        }
        setPlaceListModel(new PlaceListModel());
    }

    private void setMultiPlaceListModel() {
        if (placesList == null) {
            return;
        }
        setPlaceListModel(new MultiLocalEnvPlaceListModel());
    }

    /*
     * 选中文件
     */
    private void setFileTextField(FILE file) {
        if (file != null && !file.isDirectory()) {
            fileNameTextField.setText(file.getName());
            if (file instanceof FileFILE) {
                fileNameTextField.setText(((FileFILE) file).getTotalName());
            }
        }
    }

    /**
     * Sets the current directory. Passing in <code>null</code> sets the file
     * chooser to point to the user's default directory.
     *
     * @param dir the current directory to point to
     * @see #setSelectedDirectory
     */
    public void setCurrentDirectory(FILE dir) {
        if (dir == null && placesList != null) {
            dir = (FILE) placesList.getModel().getElementAt(0);
            placesList.setSelectedIndex(0);
        }

        if (placesList != null) {
            if (ComparatorUtils.equals(dir.prefix(), FILEFactory.ENV_PREFIX) || dir.prefix().endsWith(FILEFactory.WEBREPORT_PREFIX)) {
                placesList.setSelectedIndex(0);
            } else if (ComparatorUtils.equals(dir.prefix(), FILEFactory.FILE_PREFIX)) {
                AbstractPlaceListModel defaultListModel = (AbstractPlaceListModel) placesList.getModel();
                for (int i = 0; i < defaultListModel.getSize(); i++) {
                    if (defaultListModel.getElementAt(i) instanceof FileFILE) {
                        FileFILE popDir = (FileFILE) defaultListModel.getElementAt(i);
                        if (popDir != null && dir.getPath().indexOf(popDir.getPath()) == 0) {
                            placesList.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        }

        setSelectedDirectory(dir);
    }

    /*
     * Set Selected Directory 在右侧的subFileList中显示dir下面的所有文件和文件夹
     */
    private void setSelectedDirectory(FILE dir) {
        if (ComparatorUtils.equals(currentDirectory, dir) || dir == null || !dir.isDirectory()) {
            return;
        }

        currentDirectory = dir;

        this.locationBtnPane.populate(currentDirectory);
        this.mkdirButton.setEnabled(currentDirectory != null);

        refreshSubFileListModel();
    }

    /*
     * 刷新右侧的SubFileList
     */
    private void refreshSubFileListModel() {
        if (currentDirectory != null) {
            ((DefaultListModel) subFileList.getModel()).removeAllElements();

            FILE[] res_array = currentDirectory.listFiles();
            ((DefaultListModel) subFileList.getModel()).removeAllElements();
            for (int i = 0; i < res_array.length; i++) {
                if (filter == null || filter.accept(res_array[i])) {
                    ((DefaultListModel) subFileList.getModel()).addElement(res_array[i]);
                }
            }
            String[] name_array = new String[res_array.length];
            for (int i = 0; i < res_array.length; i++) {
                name_array[i] = res_array[i].getName();
            }
            fileNameTextField.setFilter(new DefaultCompletionFilter(name_array));
            subFileList.validate();
            subFileList.repaint(10);
        }
    }

    private abstract class AbstractPlaceListModel extends AbstractListModel<FILE> {

        protected List<FileFILE> filesOfSystem = new ArrayList<FileFILE>();

        protected void processSystemFile() {
            if (WindowsDetector.detect(true)) {
                // windows下展示桌面
                File[] desktop = FileSystemView.getFileSystemView().getRoots();
                if (desktop != null) {
                    for (int i = 0; i < desktop.length; i++) {
                        if (desktop[i].exists()) {
                            filesOfSystem.add(new FileFILE(desktop[i]));
                        }
                    }
                }
            } else {
                // *nix下展示家目录
                filesOfSystem.add(new FileFILE(FileSystemView.getFileSystemView().getDefaultDirectory()));
            }

            // C, D, E等盘符
            File[] roots = File.listRoots();
            if (roots != null) {
                for (int i = 0; i < roots.length; i++) {
                    if (roots[i].exists()) {
                        filesOfSystem.add(new FileFILE(roots[i]));
                    }
                }
            }
        }

        protected void setCD(final FILE lastDirectory) {
            for (int i = 0; i < this.getSize(); i++) {
                FILE file = this.getElementAt(i);
                if (ComparatorUtils.equals(lastDirectory.prefix(), file.prefix())) {
                    setCurrentDirectory(lastDirectory);
                    return;
                }
            }
            setCurrentDirectory(this.getElementAt(0));
        }
    }

    private class PlaceListModel extends AbstractPlaceListModel {
        private FileNodeFILE envFILE;
        private FileNodeFILE webReportFILE;

        PlaceListModel() {
            if (FILEChooserPane.this.showEnv) {
                envFILE = new FileNodeFILE(new FileNode(ProjectConstants.REPORTLETS_NAME, true)) {
                    @Override
                    public String getName() {
                        return getEnvProjectName();
                    }
                };
            }
            if (FILEChooserPane.this.showWebReport) {
                webReportFILE = new FileNodeFILE(FRContext.getCommonOperator().getWebRootPath());
            }
            if (FILEChooserPane.this.showLoc) {
                processSystemFile();
            }
        }

        @Override
        public FILE getElementAt(int index) {
            int n = FILEChooserPane.this.showEnv ? 1 : 0;
            int n2 = FILEChooserPane.this.showWebReport ? 1 : 0;

            if (index < n) {
                return envFILE;
            } else if (index < n + n2) {
                return webReportFILE;
            } else if (index < n + n2 + filesOfSystem.size()) {
                return filesOfSystem.get(index - n - n2);
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int getSize() {
            if (FILEChooserPane.this.showEnv && FILEChooserPane.this.showWebReport) {
                return 2 + filesOfSystem.size();
            } else if (FILEChooserPane.this.showEnv || FILEChooserPane.this.showWebReport) {
                return 1 + filesOfSystem.size();
            } else {
                return filesOfSystem.size();
            }
        }

    }

    private class MultiLocalEnvPlaceListModel extends AbstractPlaceListModel {

        private List<FileFILE> envFiles = new ArrayList<FileFILE>();
        private FileNodeFILE webReportFILE;

        MultiLocalEnvPlaceListModel() {
            Iterator<String> iterator = DesignerEnvManager.getEnvManager().getEnvNameIterator();

            while (iterator.hasNext()) {
                final String envName = iterator.next();
                DesignerWorkspaceInfo info = DesignerEnvManager.getEnvManager().getWorkspaceInfo(envName);
                if (info.getType() == DesignerWorkspaceType.Local) {
                    FileFILE fileFILE =
                            new FileFILE(new File(info.getPath() + CoreConstants.SEPARATOR + ProjectConstants.REPORTLETS_NAME)) {
                                @Override
                                public String getName() {
                                    return envName;
                                }
                            };
                    if (fileFILE.exists() && fileFILE.isDirectory()) {
                        envFiles.add(fileFILE);
                    }
                }
            }

            if (FILEChooserPane.this.showWebReport) {
                webReportFILE = new FileNodeFILE(FRContext.getCommonOperator().getWebRootPath());
            }
            if (FILEChooserPane.this.showLoc) {
                processSystemFile();
            }
        }

        @Override
        public FILE getElementAt(int index) {
            int envCount = envFiles.size();
            int webReportCount = FILEChooserPane.this.showWebReport ? 1 : 0;

            if (index < envCount) {
                return envFiles.get(index);
            } else if (index < envCount + webReportCount) {
                return webReportFILE;
            } else if (index < envCount + webReportCount + filesOfSystem.size()) {
                return filesOfSystem.get(index - envCount - webReportCount);
            }
            throw new IndexOutOfBoundsException();
        }

        @Override
        public int getSize() {
            int webReportCount = FILEChooserPane.this.showWebReport ? 1 : 0;
            return envFiles.size() + filesOfSystem.size() + webReportCount;
        }

        private class FileFILE extends com.fr.file.FileFILE {

            public FileFILE(File file) {
                super(file);
            }

            @Override
            public Icon getIcon() {
                if (ComparatorUtils.equals(getTotalName(), ProjectConstants.REPORTLETS_NAME)) {
                    return BaseUtils.readIcon("/com/fr/base/images/oem/logo.png");
                } else {
                    return FileTreeIcon.getIcon(new File(this.getPath()), false);
                }
            }

            @Override
            public FILE[] listFiles() {
                FILE[] fileFILES = super.listFiles();

                List<FileFILE> results = new ArrayList<>();

                for (FILE fileFILE : fileFILES) {
                    results.add(new FileFILE(new File(fileFILE.getPath())));
                }

                return results.toArray(new FILE[results.size()]);
            }
        }
    }

    /*
     * 上面的LocationButtonPane
     */
    private class LocationButtonPane extends JPanel {
        private FILE popDir;

        private BasicArrowButton leftArrowButton;
        private BasicArrowButton rightArrowButton;

        private List<UIButton> buttonList = new ArrayList<>();
        private int pathIndex = 0;
        private int maxPathIndex = 0;

        public LocationButtonPane() {
            this.setLayout(FRGUIPaneFactory.createBoxFlowLayout());

            leftArrowButton = new BasicArrowButton(BasicArrowButton.WEST) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(21, 21);
                }
            };
            leftArrowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (pathIndex > 0) {
                        pathIndex--;
                    }
                    LocationButtonPane.this.doLayout();
                    LocationButtonPane.this.repaint(10);
                }
            });

            rightArrowButton = new BasicArrowButton(BasicArrowButton.EAST) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(21, 21);
                }
            };
            rightArrowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (pathIndex < maxPathIndex) {
                        pathIndex++;
                    }
                    LocationButtonPane.this.doLayout();
                    LocationButtonPane.this.repaint(10);
                }
            });
        }

        public void highLightButton(FILE dir) {
            for (int i = 0; i < this.buttonList.size(); i++) {
                this.buttonList.get(i).setForeground(null);
                if (((SetDirectoryAction) this.buttonList.get(i).getAction()).getDir() != null
                        && this.buttonList.get(i).getAction() instanceof SetDirectoryAction
                        && (ComparatorUtils.equals(((SetDirectoryAction) this.buttonList.get(i).getAction()).getDir().getPath(), dir.getPath()))) {

                    this.buttonList.get(i).setForeground(Color.BLUE);
                }
            }
        }

        public void setPopDir(FILE file) {
            popDir = file;
        }

        public void populate(FILE dir) {
            if (popDir != null && dir != null && popDir.toString().indexOf(dir.toString()) == 0) {
                highLightButton(dir);
                return;
            }

            boolean isWebAppNamePath;
            popDir = dir;
            this.buttonList.clear();

            if (dir == null) {
                return;
            }
            String path = dir.getPath();
            isWebAppNamePath = ComparatorUtils.equals(dir.prefix(), FILEFactory.WEBREPORT_PREFIX);
            // 确保最后一个字符是分隔符
            if (!path.endsWith("/") && !path.endsWith("\\") && !StringUtils.isBlank(path)) {
                path = path + "/";
            }
            String webAppName = GeneralContext.getCurrentAppNameOfEnv();
            if (StringUtils.isBlank(path) && isWebAppNamePath) {
                this.buttonList.add(createBlankButton(new SetDirectoryAction(webAppName + '/')));
            }
            Matcher matcher = SEPARATOR_PATTERN.matcher(path);
            int node_start = 0;
            while (matcher.find()) {
                int start = matcher.start();
                String btn_text = path.substring(node_start, start);
                String btn_path = path.substring(0, start);
                if (StringUtils.isBlank(btn_text) && isWebAppNamePath) {
                    btn_text = webAppName;
                }
                this.buttonList.add(createBlankButton((new SetDirectoryAction(btn_text + '/',
                        // alex:dir.prefix不和btn_path一起参与pathJoin,因为btn_path是否以/打头在unix,linux
                        // OS中意义很不一样
                        FILEFactory.createFolder(dir.prefix() + StableUtils.pathJoin(btn_path, "/"))))));
                node_start = matcher.end();
            }
            maxPathIndex = calculateMaxPathIndex();

            pathIndex = maxPathIndex;
            this.doLayout();
            this.repaint(10);

            // 高亮显示当前目录
            highLightButton(dir);
        }

        // doLayout
        @Override
        public void doLayout() {
            this.removeAll();

            if (popDir == null) {
                return;
            }

            int pathWidth = 0;
            // 前缀
            // UILabel prefixLabel = new UILabel(popDir.prefix());
            // this.add(prefixLabel);
            // pathWidth += prefixLabel.getPreferredSize().width;

            // pathWidth
            for (int i = 0; i < buttonList.size(); i++) {
                pathWidth += buttonList.get(i).getPreferredSize().width;
            }

            // 当path按钮的长度超出显示范围时，隐藏
            if (this.getWidth() < pathWidth) {
                int tmpWidth = leftArrowButton.getPreferredSize().width + leftArrowButton.getPreferredSize().width;
                int oldTmpWidth = tmpWidth;
                for (int i = pathIndex; i < buttonList.size(); i++) {
                    tmpWidth += buttonList.get(i).getPreferredSize().width;
                    if (tmpWidth > this.getWidth()) {
                        break;
                    }


                    this.add(buttonList.get(i));
                    oldTmpWidth = tmpWidth;
                }
                UILabel blankLabel = new UILabel("");
                blankLabel.setPreferredSize(new Dimension(this.getWidth() - oldTmpWidth - 1, blankLabel.getPreferredSize().height));
                this.add(blankLabel);
                // leftArrowButton
                this.add(leftArrowButton, 1);
                // rightArrowButton
                this.add(rightArrowButton);
            } else {
                pathIndex = 0;
                for (int i = 0; i < buttonList.size(); i++) {
                    this.add(buttonList.get(i));
                }
            }

            super.doLayout();
        }

        private int calculateMaxPathIndex() {
            if (popDir == null) {
                return 0;
            }

            int pathWidth = 0;
            // 前缀
            UILabel prefixLabel = new UILabel(popDir.prefix());
            pathWidth += prefixLabel.getPreferredSize().width;

            for (int i = 0; i < buttonList.size(); i++) {
                pathWidth += buttonList.get(i).getPreferredSize().width;
            }

            if (this.getWidth() < pathWidth) {
                int index = 0;

                int tmpWidth = prefixLabel.getPreferredSize().width + leftArrowButton.getPreferredSize().width
                        + leftArrowButton.getPreferredSize().width;
                for (int i = buttonList.size() - 1; i >= 0; i--) {
                    tmpWidth += buttonList.get(i).getPreferredSize().width;
                    if (tmpWidth > this.getWidth()) {
                        break;
                    }
                    index = i;
                }

                return index;
            }
            // 当长度足够时，不出现左右的按钮，皆从0开始显示
            else {
                return 0;
            }
        }
    }

    private class SetDirectoryAction extends UpdateAction {
        private FILE dir;

        public SetDirectoryAction(String name) {
            this.setName(name);
            // this.dir = file;
        }

        public SetDirectoryAction(String name, FILE file) {
            this.setName(name);
            this.dir = file;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            if (dir != null) {
                setSelectedDirectory(dir);
            }
        }

        public FILE getDir() {
            return dir;
        }
    }


    private UIButton createBlankButton(SetDirectoryAction setDirectoryAction) {

        final Color brighter = this.getBackground().brighter();
        final Color darker = this.getBackground().darker();

        final UIButton blankButton = new UIButton(setDirectoryAction);
        blankButton.setMargin(new Insets(0, 0, 0, 0));
        blankButton.setUI(new BasicButtonUI());
        blankButton.setHorizontalTextPosition(SwingConstants.CENTER);
        blankButton.setBorderPainted(false);
        blankButton.setBorder(BorderFactory.createRaisedBevelBorder());
        blankButton.setBackground(darker);
        blankButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                blankButton.setBackground(brighter);
                blankButton.setBorderPainted(true);
                blankButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                blankButton.setBackground(darker);
                blankButton.setBorderPainted(false);
                blankButton.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                blankButton.setBackground(brighter);
                blankButton.setBorderPainted(false);
                blankButton.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                blankButton.setBackground(darker);
                blankButton.setBorderPainted(true);
                blankButton.repaint();
            }


        });
        return blankButton;
    }

    /*
     * 新建文件夹的Button
     */
    private UIButton initMkdirButton() {
        UIButton folderButton = new UIButton();
        folderButton.setIcon(BaseUtils.readIcon("com/fr/design/images/icon_NewFolderIcon_normal.png"));
        folderButton.setUI(new UIButtonUI());
        folderButton.setToolTipText(Toolkit.i18nText("Fine-Design_Basic_Utils_New_Folder"));
        folderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (currentDirectory == null) {
                    return;
                }

                if (access(currentDirectory)) {
                    new MkdirDialog();
                } else {
                    FineJOptionPane.showMessageDialog(
                            FILEChooserPane.this,
                            Toolkit.i18nText("Fine-Design_Basic_App_Privilege_No") + "!",
                            Toolkit.i18nText("Fine-Design_Basic_App_File_Message"),
                            JOptionPane.WARNING_MESSAGE);
                }


            }
        });
        return folderButton;
    }


    /**
     * 新建文件夹对话框
     * 支持快捷键Enter，ESC
     */
    private class MkdirDialog extends JDialog {

        private UITextField nameField;

        private UILabel warnLabel;

        private UIButton confirmButton;


        private MkdirDialog() {

            this.setLayout(new BorderLayout());
            this.setModal(true);

            // 输入框前提示
            UILabel newNameLabel = new UILabel(Toolkit.i18nText(
                    "Fine-Design_Basic_Enter_New_Folder_Name")
            );
            newNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            newNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            newNameLabel.setPreferredSize(new Dimension(118, 15));

            // 文件名输入框
            nameField = new UITextField();
            nameField.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    validInput();
                }

                public void insertUpdate(DocumentEvent e) {
                    validInput();
                }

                public void removeUpdate(DocumentEvent e) {
                    validInput();
                }
            });
            nameField.selectAll();
            nameField.setPreferredSize(new Dimension(180, 20));

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
            topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
            topPanel.add(newNameLabel);
            topPanel.add(nameField);

            // 增加enter以及esc快捷键的支持
            nameField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        dispose();
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (confirmButton.isEnabled()) {
                            confirmClose();
                        }
                    }
                }
            });
            // 重名提示
            warnLabel = new UILabel();
            warnLabel.setPreferredSize(new Dimension(300, 30));
            warnLabel.setHorizontalAlignment(SwingConstants.LEFT);
            warnLabel.setForeground(Color.RED);
            warnLabel.setVisible(false);

            JPanel midPanel = new JPanel(new BorderLayout());
            midPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            midPanel.add(warnLabel, BorderLayout.WEST);

            // 确认按钮
            confirmButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Confirm"));
            confirmButton.setPreferredSize(new Dimension(60, 25));
            confirmButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    confirmClose();
                }
            });

            // 取消按钮
            UIButton cancelButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Cancel"));
            cancelButton.setPreferredSize(new Dimension(60, 25));

            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            bottomPanel.add(confirmButton);
            bottomPanel.add(cancelButton);

            this.add(
                    TableLayoutHelper.createTableLayoutPane(
                            new Component[][]{
                                    new Component[]{topPanel},
                                    new Component[]{midPanel},
                                    new Component[]{bottomPanel}
                            },
                            new double[]{TableLayout.FILL, TableLayout.FILL, TableLayout.FILL},
                            new double[]{TableLayout.FILL}
                    ),
                    BorderLayout.CENTER);


            this.setSize(340, 180);
            this.setTitle(Toolkit.i18nText("Fine-Design_Basic_Mkdir"));
            this.setResizable(false);
            this.setAlwaysOnTop(true);
            this.setIconImage(BaseUtils.readImage("/com/fr/base/images/oem/logo.png"));
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            GUICoreUtils.centerWindow(this);
            this.setVisible(true);
        }

        private void confirmClose() {

            String userInput = nameField.getText().trim();

            // 处理不合法的文件夹名称
            userInput = userInput.replaceAll("[\\\\/:*?\"<>|]", StringUtils.EMPTY);

            if (currentDirectory.createFolder(userInput)) {
                refreshSubFileListModel();
                setSelectedFileName(userInput);
                // ben:这里处理有些不妥，取文件时没有考虑filefilter，不过效果一样，取的时候应该用subfilelist得data
                FILE[] allFiles = currentDirectory.listFiles();
                int place = 0;
                for (int i = 0; i < allFiles.length; i++) {
                    if (ComparatorUtils.equals(allFiles[i].getName(), userInput) && allFiles[i].isDirectory()) {
                        place = i;
                        break;
                    }
                }
                scrollPane.revalidate();
                scrollPane.repaint();
                int total = scrollPane.getVerticalScrollBar().getMaximum();
                int value = total * place / subFileList.getModel().getSize();
                scrollPane.getVerticalScrollBar().setValue(value);

            } else {
                FineJOptionPane.showConfirmDialog(FILEChooserPane.this,
                        Toolkit.i18nText("Fine-Design_Basic_Make_Failure"),
                        Toolkit.i18nText("Fine-Design_Basic_Error"),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE);
            }
            this.dispose();
        }


        private void validInput() {

            String userInput = nameField.getText().trim();

            if (StringUtils.isEmpty(userInput)) {
                confirmButton.setEnabled(false);
            }

            boolean duplicate = false;
            // ben:这里处理有些不妥，取文件时没有考虑filefilter，不过效果一样，取的时候应该用subfilelist得data
            FILE[] allFiles = currentDirectory.listFiles();
            for (int i = 0; i < allFiles.length; i++) {
                if (ComparatorUtils.equals(allFiles[i].getName(), userInput) && allFiles[i].isDirectory()) {
                    duplicate = true;
                    break;
                }
            }

            if (duplicate) {
                nameField.selectAll();
                // 如果文件名已存在，则灰掉确认按钮
                warnLabel.setText(
                        Toolkit.i18nText(
                                "Fine-Design_Basic_Folder_Name_Duplicate",
                                userInput));
                warnLabel.setVisible(true);
                confirmButton.setEnabled(false);
            } else {
                warnLabel.setVisible(false);
                confirmButton.setEnabled(true);
            }
        }
    }
}
