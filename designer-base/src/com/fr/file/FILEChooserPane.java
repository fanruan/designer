package com.fr.file;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.base.extension.FileExtension;
import com.fr.file.filetree.LocalFileNodes;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.DefaultCompletionFilter;
import com.fr.design.gui.itextfield.UIAutoCompletionField;
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
import com.fr.general.Inter;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CoreConstants;
import com.fr.stable.OperatingSystem;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
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

    private static final FILEChooserPane INSTANCE = new FILEChooserPane();

    public FILE currentDirectory; // 当前路径,在subFileList中显示这个路径下所有的文件

    private List<FILEFilter> filterList = new ArrayList<FILEFilter>();
    private FILEFilter filter;

    private LocationButtonPane locationBtnPane; // 显示location的Panel
    private UIButton createFolderButton;

    private PlaceListModel model;
    private JList placesList; // File.listRoots() + Env + Favourite
    private JList subFileList; // 当前选中目录下的文件夹及文件

    private JScrollPane scrollPane;
    private UIAutoCompletionField fileNameTextField; // 文件名的文本框
    private UIComboBox postfixComboBox; // 文件后缀名的下拉列表框

    private UIButton okButton;
    private UIButton cancelButton;


    protected int type;
    protected boolean showEnv;
    protected boolean showLoc;
    protected boolean showWebReport = false;

    private UIDialog dialog;

    private int returnValue = CANCEL_OPTION;

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
        INSTANCE.setModelOfPlaceList();
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
        INSTANCE.setModelOfPlaceList();
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
        INSTANCE.setModelOfPlaceList();
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
        INSTANCE.setModelOfPlaceList();
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
                returnValue = CANCEL_OPTION;
                dialogExit();
            }
        });

        JPanel locationPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // locationPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        locationPane.add(locationBtnPane = new LocationButtonPane(), BorderLayout.CENTER);

        createFolderButton = createFolderButton();
        locationPane.add(createFolderButton, BorderLayout.EAST);

        JPanel centerLeftPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // centerLeftPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        // Richie:placesList includes C,D,E,F and DeskTop etc.
        placesList = new JList();
        centerLeftPanel.add(placesList, BorderLayout.CENTER);
        centerLeftPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        centerLeftPanel.setPreferredSize(new Dimension(120, 1));
        placesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        placesList.setCellRenderer(placelistRenderer);
        placesList.addListSelectionListener(placeListener);
        placesList.addMouseListener(placeMouseListener);

        // centerRightPane
        JPanel centerRightPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel subFilePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // subFilePanel.setLayout(FRGUIPaneFactory.createBorderLayout()); //
        // TODO alex_GUI
        // 以后rightPanel要用JTable
        subFileList = new JList(new DefaultListModel());
        subFileList.setCellRenderer(listRenderer);
        subFileList.addMouseListener(subFileListMouseListener);
        subFileList.addKeyListener(subFileListKeyListener);
        scrollPane = new JScrollPane(subFileList);
        subFilePanel.add(scrollPane, BorderLayout.CENTER);
        centerRightPane.add(subFilePanel, BorderLayout.CENTER);

        // 用createTableLayoutPane布局下fileNamePane
        Component[][] coms = new Component[][]{
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Inter.getLocText("Utils-File_name") + ":"), BorderLayout.WEST),
                        fileNameTextField = new UIAutoCompletionField(), okButton = new UIButton(Inter.getLocText("Utils-Design-File_Open"))
                },
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Inter.getLocText("Utils-File_type") + ":"), BorderLayout.WEST),
                        postfixComboBox = new UIComboBox(), cancelButton = new UIButton(Inter.getLocText("Utils-Design-Action_Cancel"))
                }};

        JPanel fileNamePane = TableLayoutHelper.createGapTableLayoutPane(coms, new double[]{TableLayout.PREFERRED, TableLayout.PREFERRED,
                TableLayout.PREFERRED}, new double[]{TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED}, 24, 4);
        centerRightPane.add(fileNamePane, BorderLayout.SOUTH);

        Component[][] outComponents = new Component[][]{
                new Component[]{GUICoreUtils.createBorderPane(new UILabel(Inter.getLocText("FR-App-File_Lookup_range") + ":"), BorderLayout.WEST), locationPane},
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
                returnValue = CANCEL_OPTION;
                doCancel();
            }
        });
        fileNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    returnValue = CANCEL_OPTION;
                    doOK();
                }
            }
        });
        postfixComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                Object ss = postfixComboBox.getSelectedItem();
                if (ss instanceof FILEFilter) {
                    setFILEFilter((FILEFilter) ss);
                } else {
                    setFILEFilter(null);
                }
            }
        });
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
        this.setModelOfPlaceList();
    }


    /**
     * 返回选中的FILE
     *
     * @return
     */
    public FILE getSelectedFILE() {
        String fileName = fileNameTextField.getText().trim();
        if (!fileName.endsWith(suffix) && fileName.indexOf(CoreConstants.DOT) == -1) {
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
        return Inter.getLocText("Utils-Report-Env_Directory");
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

    // August:上面的方法在包含时直接return了，应该把改filter放在第一个
    // 不然在多次打开两个FILEchooserpane时，filter会出错

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
        if (currentDirectory != null) {
            return;
        }
        FILE[] res_array = currentDirectory.listFiles();
        String[] name_array = new String[res_array.length];
        for (int i = 0; i < res_array.length; i++) {
            name_array[i] = res_array[i].getName();
        }
        fileNameTextField.setFilter(new DefaultCompletionFilter(name_array));
    }

    /**
     * @return
     */
    public String getFileNameTextField() {
        return this.fileNameTextField.getText();
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
                returnValue = CANCEL_OPTION;
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
        if (type == JFileChooser.SAVE_DIALOG) {
            this.getFileNameTextField();
        } else {
            fileNameTextField.removeDocumentListener();
            fileNameTextField.setText("");
            fileNameTextField.addDocumentListener();
        }
        dialog.setVisible(true);
        return returnValue;
    }

    protected void fileType() {
        String appName = ProductConstants.APP_NAME;
        JTemplate editing = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (FileExtension.CRT.matchExtension(suffix)) {
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.CRT, appName + Inter.getLocText(new String[]{"Utils-The-Chart", "FR-App-All_File"})));
            return;
        }
        if (editing == null || !editing.isChartBook()) {
            String[] fileSuffix_local = LocalFileNodes.FILE_TYPE;
            EnumSet<FileExtension> fileExtensions = EnumSet.of(FileExtension.CPT, FileExtension.CPTX, FileExtension.FRM, FileExtension.FRMX, FileExtension.CHT);
            if (type == JFileChooser.OPEN_DIALOG) {
                if (FRContext.getFileNodes().isSupportLocalFileOperate()) { //本地连接
                    this.addChooseFILEFilter(new ChooseFileFilter(fileSuffix_local, appName + Inter.getLocText(new String[]{"FR-App-Report_Template", "FR-App-All_File"})));
                } else {
                    this.addChooseFILEFilter(new ChooseFileFilter(fileExtensions, appName + Inter.getLocText(new String[]{"FR-App-Report_Template", "FR-App-All_File"})));
                }
            }

            // ben:filefilter设置初值为cpt过滤
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.CPT, appName + Inter.getLocText(new String[]{"FR-App-Report_Template", "FR-App-All_File"})));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.CPTX, appName + Inter.getLocText(new String[]{"FR-App-Report_Template", "FR-App-All_File"})));

            // richer:form文件 daniel 改成三个字
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.FRM, appName + Inter.getLocText(new String[]{"FR-App-Template_Form", "FR-App-All_File"})));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.FRMX, appName + Inter.getLocText(new String[]{"FR-App-Template_Form", "FR-App-All_File"})));
        } else {
            if (type == JFileChooser.OPEN_DIALOG) {
                this.addChooseFILEFilter(new ChooseFileFilter(EnumSet.of(FileExtension.XLS, FileExtension.XLSX), Inter.getLocText("Import-Excel_Source")));
            }
        }

        // 添加 xls 文件类型过滤 kt
        if (FRContext.getFileNodes().isSupportLocalFileOperate()) {  //本地连接
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.XLS, Inter.getLocText("Import-Excel_Source")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.XLSX, Inter.getLocText("Import-Excel2007_Source")));
        }
        if (FileExtension.PNG.matchExtension(suffix)) {
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.PNG, Inter.getLocText("FR-App-Export_png")));
        }
        if (type == JFileChooser.SAVE_DIALOG) {
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.PDF, Inter.getLocText("FR-Import-Export_PDF")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.SVG, Inter.getLocText("FR-Import-Export_SVG")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.CSV, Inter.getLocText("FR-Import-Export_CSV")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.DOC, Inter.getLocText("FR-Import-Export_Word")));
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.TXT, Inter.getLocText("FR-Import-Export_Text")));
        }

    }

    private void chooseType() {
        DefaultComboBoxModel defaultComboBoxModel = (DefaultComboBoxModel) postfixComboBox.getModel();
        defaultComboBoxModel.removeAllElements();
        for (FILEFilter aFilterList : filterList) {
            defaultComboBoxModel.addElement(aFilterList);
        }
        if (FRContext.getFileNodes().isSupportLocalFileOperate()) {  //本地连接
            if (!showWebReport) {
                defaultComboBoxModel.addElement(Inter.getLocText("FR-Utils-App_AllFiles") + "(*.*)");
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
        if (type == JFileChooser.OPEN_DIALOG) {
            postfixComboBox.setEnabled(true);
        } else {
            postfixComboBox.setEnabled(false);
        }

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
                    returnValue = OK_OPTION;
                    saveDictionary();
                    dialogExit();
                } else {
                    JOptionPane.showMessageDialog(this, Inter.getLocText("FR-App-Template_Report_Not_Exist"));
                    return;
                }
            }

            // alex:NPE判断,因为可能没有选中子目录,直接在文本框中输入文件名
            FILE selectedSubFile = (FILE) this.subFileList.getSelectedValue();
            if (selectedSubFile != null && selectedSubFile.isDirectory()) {
                setSelectedDirectory((FILE) this.subFileList.getSelectedValue());
            } else {
                returnValue = OK_OPTION;
                saveDictionary();
                dialogExit();
            }
        } else if (type == JFileChooser.SAVE_DIALOG) {
            saveDialog();

        }
    }


    private void saveDialog() {
        String filename = fileNameTextField.getText();
        if (!filename.endsWith(suffix) && !filename.contains(CoreConstants.DOT)) {
            ChooseFileFilter chooseFileFilter = (ChooseFileFilter) (postfixComboBox.getSelectedItem());
            if (chooseFileFilter != null && StringUtils.isNotEmpty(chooseFileFilter.getExtensionString())) {
                fileNameTextField.setText(filename + chooseFileFilter.getExtensionString());
            } else {
                fileNameTextField.setText(filename + this.suffix);
            }
        }
        returnValue = OK_OPTION;
        FILE selectedFile = this.getSelectedFILE();
        boolean access = false;

        try {
            access = FRContext.getOrganizationOperator().canAccess(selectedFile.getPath());
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        if (!access) {
            JOptionPane.showMessageDialog(FILEChooserPane.this, Inter.getLocText("FR-App-Privilege_No") + "!", Inter.getLocText("FR-App-File_Message"), JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedFile.exists()) {
            int selVal = JOptionPane.showConfirmDialog(dialog, Inter.getLocText("FR-Utils-Would_you_like_to_cover_the_current_file") + " ?",
                    ProductConstants.PRODUCT_NAME, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (selVal == JOptionPane.YES_OPTION) {
                returnValue = JOPTIONPANE_OK_OPTION;
                saveDictionary();
                dialogExit();
            } else {
                returnValue = JOPTIONPANE_CANCEL_OPTION;
            }

        } else {
            dialogExit();
            saveDictionary();
        }
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

    /**
     * @return
     */
    public int getReturnValue() {
        return this.returnValue;
    }

    /*
     * dialog的名字
     */
    private String dialogName() {
        return type == JFileChooser.OPEN_DIALOG ? Inter.getLocText("Utils-Design-File_Open") : Inter.getLocText("FR-App-Template_Save");
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
                // TODO alex_GUI 怎么ScrollIntoView?
                break;
            }
        }
    }

    @Override
    protected String title4PopupWindow() {
        return dialogName();
    }

    private class PlaceListModel extends AbstractListModel {
        private FileNodeFILE envFILE;
        private FileNodeFILE webReportFILE;
        private List<FileFILE> filesOfSystem = new ArrayList<FileFILE>();

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
                // webReportFILE = new FileFILE(new
                // File(FRContext.getCommonOperator().getWebReportPath()));
                webReportFILE = new FileNodeFILE(FRContext.getCommonOperator().getWebReportPath());
                // String webReportPath =
                // FRContext.getCommonOperator().getWebReportPath();
                // String webReportParentPath = new
                // File(webReportPath).getParent();
                // webReportFILE = new FileNodeFILE(new FileNode("WebReport",
                // true),webReportParentPath);
            }
            if (FILEChooserPane.this.showLoc) {


                if (OperatingSystem.isWindows()) {
                    // windows下展示桌面
                    File[] desktop = FileSystemView.getFileSystemView().getRoots();
                    if (desktop != null) {
                        for (int i = 0; i < desktop.length; i++) {
                            if (desktop[i].exists()) {
                                filesOfSystem.add(new FileFILE(desktop[i]));
                            }
                        }
                    }
                } else { // *nix下展示家目录
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

        private void setCD(final FILE lastdirctory) {
            for (int i = 0; i < this.getSize(); i++) {
                FILE file = this.getElementAt(i);
                if (ComparatorUtils.equals(lastdirctory.prefix(), file.prefix())) {
                    setCurrentDirectory(lastdirctory);
                    return;
                }
            }
            setCurrentDirectory(this.getElementAt(0));
        }
    }

    protected void setModelOfPlaceList() {
        if (placesList == null) {
            return;
        }
        model = new PlaceListModel();
        placesList.setModel(model);
        String lastdirctorypath = DesignerEnvManager.getEnvManager().getDialogCurrentDirectory();
        String prefix = DesignerEnvManager.getEnvManager().getCurrentDirectoryPrefix();
        FILE lastdirctory = FILEFactory.createFolder(prefix + lastdirctorypath);
        model.setCD(lastdirctory);

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

    private ListCellRenderer placelistRenderer = new DefaultListCellRenderer() {

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

    };
    /*
     * JList的CellRenderer
     */
    private ListCellRenderer listRenderer = new DefaultListCellRenderer() {

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

    };
    // placeList listener
    ListSelectionListener placeListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            Object selValue = placesList.getSelectedValue();
            if (selValue instanceof FILE) {
                setSelectedDirectory((FILE) selValue);
            }
        }
    };

    /**
     * placeList mouseListener
     */
    private MouseListener placeMouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            Object selValue = placesList.getSelectedValue();
            if (selValue instanceof FILE) {
                setSelectedDirectory((FILE) selValue);
            }
        }
    };

    /**
     * right list.
     */
    private KeyListener subFileListKeyListener = new KeyAdapter() {
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
    };
    /*
     * 鼠标点击JList时的listener
     */
    private MouseListener subFileListMouseListener = new MouseAdapter() {
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
    };

    /*
     * 选中文件
     */
    private void setFileTextField(FILE file) {
        // clickedFILE = file;

        // String okButtonText;
        // if (file != null && file.isDirectory()
        // && this.fileNameTextField.getText().length() == 0) {
        // okButtonText = Inter.getLocText("Open");// + "(O)";
        // } else {
        // okButtonText = dialogName();
        // }
        // okButton.setText(okButtonText);

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
                PlaceListModel defaultListModel = (PlaceListModel) placesList.getModel();
                for (int i = 0; i < defaultListModel.getSize(); i++) {
                    if (defaultListModel.getElementAt(i) instanceof FileFILE) {
                        FileFILE popDir = (FileFILE) defaultListModel.getElementAt(i);
                        if (popDir != null && dir != null && dir.getPath().indexOf(popDir.getPath()) == 0) {
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
        this.createFolderButton.setEnabled(currentDirectory != null);

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

    /*
     * 上面的LocationButtonPane
     */
    private class LocationButtonPane extends JPanel {
        private FILE popDir;

        private BasicArrowButton leftArrowButton;
        private BasicArrowButton rightArrowButton;

        private List<UIButton> buttonList = new ArrayList<UIButton>();
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

            Pattern seperatorPattern = Pattern.compile("[/\\\\]+"); // alex:之所以在Pattern那里加个+,是因为有些路径会有两个甚至多个分隔符放在一起
            Matcher matcher = seperatorPattern.matcher(path);
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
                        FILEFactory.createFolder(dir.prefix() + StableUtils.pathJoin(new String[]{btn_path, "/"}))))));
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
        final UIButton blankButton = new UIButton(setDirectoryAction);
        blankButton.setMargin(new Insets(0, 0, 0, 0));
        blankButton.setUI(new BasicButtonUI());
        blankButton.setHorizontalTextPosition(SwingConstants.CENTER);
        blankButton.setBorderPainted(false);
        blankButton.setBorder(BorderFactory.createRaisedBevelBorder());
        blankButton.setBackground(FILEChooserPane.this.getBackground().darker());
        blankButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                blankButton.setBackground(FILEChooserPane.this.getBackground().brighter());
                blankButton.setBorderPainted(true);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                blankButton.setBackground(FILEChooserPane.this.getBackground().darker());
                blankButton.setBorderPainted(false);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                blankButton.setBackground(FILEChooserPane.this.getBackground().brighter());
                blankButton.setBorderPainted(false);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                blankButton.setBackground(FILEChooserPane.this.getBackground().brighter());
                blankButton.setBorderPainted(true);
                repaint();
            }


        });
        return blankButton;
    }


    private ActionListener createFolderActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            if (currentDirectory == null) {
                return;
            }

            boolean access = false;
            try {
                access = FRContext.getOrganizationOperator().canAccess(currentDirectory.getPath());
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            if (!access) {
                JOptionPane.showMessageDialog(FILEChooserPane.this, Inter.getLocText("FR-App-Privilege_No") + "!", Inter.getLocText("FR-App-File_Message"), JOptionPane.WARNING_MESSAGE);
                return;
            }

            String res = JOptionPane.showInputDialog(Inter.getLocText("FR-Utils-Please_Input_a_New_Name"));
            if (res != null) {
                currentDirectory.createFolder(res);

                refreshSubFileListModel();

                setSelectedFileName(res);
                // ben:这里处理有些不妥，取文件时没有考虑filefilter，不过效果一样，取的时候应该用subfilelist得data
                FILE[] allFiles = currentDirectory.listFiles();
                int place = 0;
                for (int i = 0; i < allFiles.length; i++) {
                    if (ComparatorUtils.equals(allFiles[i].getName(), res) && allFiles[i].isDirectory()) {
                        place = i;
                        break;
                    }
                }
                scrollPane.validate();
                int total = scrollPane.getVerticalScrollBar().getMaximum();
                int value = total * place / subFileList.getModel().getSize();
                scrollPane.getVerticalScrollBar().setValue(value);
            }
        }
    };


    /*
     * 新建文件夹的Button
     */
    private UIButton createFolderButton() {
        UIButton folderButton = new UIButton();
        folderButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/file/newfolder.png"));
        folderButton.setEnabled(false);
        folderButton.setMargin(new Insets(0, 0, 0, 0));
        folderButton.setUI(new BasicButtonUI());
        folderButton.setToolTipText(Inter.getLocText("FR-Utils-New_Folder"));
        folderButton.addActionListener(createFolderActionListener);
        return folderButton;
    }
}