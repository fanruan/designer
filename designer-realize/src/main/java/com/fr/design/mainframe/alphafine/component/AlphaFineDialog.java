package com.fr.design.mainframe.alphafine.component;

import com.bulenkov.iconloader.IconLoader;
import com.fr.config.MarketConfig;
import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.help.alphafine.AlphaFineConfigManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
import com.fr.design.mainframe.alphafine.cell.model.AlphaCellModel;
import com.fr.design.mainframe.alphafine.cell.model.BottomModel;
import com.fr.design.mainframe.alphafine.cell.model.FileModel;
import com.fr.design.mainframe.alphafine.cell.model.MoreModel;
import com.fr.design.mainframe.alphafine.cell.model.PluginModel;
import com.fr.design.mainframe.alphafine.cell.model.RobotModel;
import com.fr.design.mainframe.alphafine.cell.render.ContentCellRender;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.preview.DocumentPreviewPane;
import com.fr.design.mainframe.alphafine.preview.FilePreviewPane;
import com.fr.design.mainframe.alphafine.preview.NoResultPane;
import com.fr.design.mainframe.alphafine.preview.PluginPreviewPane;
import com.fr.design.mainframe.alphafine.preview.RobotPreviewPane;
import com.fr.design.mainframe.alphafine.search.manager.impl.ActionSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.DocumentSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.FileSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.HotIssuesManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.PluginSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.RecentSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.RecommendSearchManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.SegmentationManager;
import com.fr.design.mainframe.alphafine.search.manager.impl.SimilarSearchManager;
import com.fr.design.mainframe.errorinfo.ErrorInfoUploader;
import com.fr.design.mainframe.templateinfo.TemplateInfoCollector;
import com.fr.form.main.Form;
import com.fr.form.main.FormIO;
import com.fr.general.ComparatorUtils;
import com.fr.general.http.HttpClient;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.ImageExporter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.main.impl.WorkBook;
import com.fr.stable.CodeUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XiaXiang on 2017/3/21.
 */
public class AlphaFineDialog extends UIDialog {
    private static final String ADVANCED_SEARCH_MARK = "k:";
    private static final String ACTION_MARK_SHORT = "k:1 ";
    private static final String ACTION_MARK = "k:setting ";
    private static final String DOCUMENT_MARK_SHORT = "k:2 ";
    private static final String DOCUMENT_MARK = "k:help ";
    private static final String FILE_MARK_SHORT = "k:3 ";
    private static final String FILE_MARK = "k:reportlets ";
    private static final String CPT_MARK = "k:cpt ";
    private static final String FRM_MARK = "k:frm ";
    private static final String DS_MARK = "k:ds ";
    private static final String DS_NAME = "dsname=\"";
    private static final String PLUGIN_MARK_SHORT = "k:4 ";
    private static final String PLUGIN_MARK = "k:shop ";
    private static final String SIMILAR_MARK = "k:robot ";
    private static final String PLACE_HOLDER = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine");
    private static final int MAX_SHOW_SIZE = 12;
    private static final int TIMER_DELAY = 300;
    private static final int ONLY_ONE_AVAILABLE_MODEL = 2;

    private AlphaFineTextField searchTextField;
    private UIButton closeButton;
    private JPanel searchResultPane;
    private UIScrollPane leftSearchResultPane;

    private JPanel defaultPane;
    //分割线
    private UILabel splitLabel;
    private JPanel rightSearchResultPane;
    private AlphaFineList searchResultList;
    private SearchListModel searchListModel;
    private SwingWorker searchWorker;
    private SwingWorker showWorker;
    private String storeText;
    private String[] segmentationResult;
    //是否强制打开，因为面板是否关闭绑定了全局鼠标事件，这里需要处理一下
    private boolean forceOpen;

    private JPanel hotPane;
    private JPanel backPane;
    SearchResult modeList = null;
    private static String beforeSearchStr = "";
    private static boolean alreadySearch = false;
    private static boolean alreadyInitHot = false;
    private String[][] hotData;

    public String[][] getHotData() {
        return hotData;
    }

    public void setHotData(String[][] hotData) {
        this.hotData = hotData;
    }

    public AlphaFineDialog(Frame parent, boolean forceOpen) {
        super(parent);
        this.forceOpen = forceOpen;
        initProperties();
        initGlobalListener();
        initComponents();
    }

    /**
     * 全局快捷键
     *
     * @return
     */
    public static AWTEventListener listener() {
        return new AWTEventListener() {

            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof KeyEvent) {
                    KeyEvent e = (KeyEvent) event;
                    KeyStroke keyStroke = (KeyStroke) KeyStroke.getAWTKeyStrokeForEvent(e);
                    KeyStroke storeKeyStroke = DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().getShortCutKeyStore();
                    if (ComparatorUtils.equals(keyStroke.toString(), storeKeyStroke.toString()) && AlphaFineConfigManager.isALPHALicAvailable() && AlphaFinePane.getAlphaFinePane().isVisible()) {
                        doClickAction();
                    }

                }
            }
        };
    }

    /**
     * 打开搜索框
     */
    private static void doClickAction() {
        AlphaFineHelper.showAlphaFineDialog(false);
    }

    /**
     * 初始化全部组件
     */
    private void initComponents() {
        initSearchTextField();
        JPanel topPane = new JPanel(new BorderLayout());
        UILabel iconLabel = new UILabel(IconLoader.getIcon(AlphaFineConstants.IMAGE_URL + "bigsearch.png"));
        iconLabel.setPreferredSize(AlphaFineConstants.ICON_LABEL_SIZE);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(Color.WHITE);
        topPane.add(iconLabel, BorderLayout.WEST);
        topPane.add(searchTextField, BorderLayout.CENTER);
        closeButton = new UIButton() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getSize().width, getSize().height);
                super.paintComponent(g);
            }
        };
        closeButton.setPreferredSize(AlphaFineConstants.CLOSE_BUTTON_SIZE);
        closeButton.setIcon(IconLoader.getIcon(AlphaFineConstants.IMAGE_URL + "alphafine_close.png"));
        closeButton.set4ToolbarButton();
        closeButton.setBorderPainted(false);
        closeButton.setRolloverEnabled(false);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        topPane.add(closeButton, BorderLayout.EAST);
        add(topPane, BorderLayout.CENTER);

        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isNeedIntelligentCustomerService()) {
            initHotPane();
        }
    }

    /**
     * 初始化热门界面
     */
    private void initHotPane() {
        removeHotPane();
        hotPane = new JPanel();
        hotPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        hotPane.setPreferredSize(AlphaFineConstants.CONTENT_SIZE);
        hotPane.setLayout(new BorderLayout());

        UILabel uiLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Hot"));
        uiLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        uiLabel.setFont(AlphaFineConstants.SMALL_FONT);
        uiLabel.setForeground(AlphaFineConstants.DARK_GRAY);

        GridLayout gridLayout = new GridLayout(2, 3, 3, 3);
        JPanel panel = new JPanel();
        panel.setLayout(gridLayout);
        if (AlphaFineHelper.isNetworkOk()) {
            if (hotData == null) {
                hotData = HotIssuesManager.getInstance().getHotIssues();
            }
            for (int i = 0; i < hotData.length; i++) {
                panel.add(new HotIssueJpanel(hotData[i], i + 1));
            }
        } else {
            hotData = null;
            for (int i = 0; i < AlphaFineConstants.HOT_ITEMS; i++) {
                panel.add(new HotIssueJpanel(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Connection_Failed")}, i + 1));
            }
        }
        hotPane.add(uiLabel, BorderLayout.NORTH);
        hotPane.add(panel, BorderLayout.CENTER);
        add(hotPane, BorderLayout.SOUTH);
        setSize(AlphaFineConstants.FULL_SIZE);
    }

    /**
     * 初始化输入框
     */
    private void initSearchTextField() {
        searchTextField = new AlphaFineTextField(PLACE_HOLDER);
        initTextFieldListener();
        searchTextField.setFont(AlphaFineConstants.GREATER_FONT);
        searchTextField.setBackground(Color.WHITE);
        searchTextField.setBorderPainted(false);
    }

    /**
     *
     */
    private void initProperties() {
        setUndecorated(true);
//addComponentListener(new ComponentHandler());
        setSize(AlphaFineConstants.FIELD_SIZE);
        centerWindow(this);

    }

    /**
     * 设置面板位置
     *
     * @param win
     */
    private void centerWindow(Window win) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Dimension winSize = win.getSize();

        if (winSize.height > screenSize.height) {
            winSize.height = screenSize.height;
        }
        if (winSize.width > screenSize.width) {
            winSize.width = screenSize.width;
        }
//这里设置位置：水平居中，竖直偏上
        win.setLocation((screenSize.width - winSize.width) / 2, (screenSize.height - winSize.height) / AlphaFineConstants.SHOW_SIZE);
    }

    /**
     * 执行搜索
     *
     * @param text
     */
    private void doSearch(String text) {
        showSearchResult(text);
    }

    @Override
    public void setVisible(boolean isVisible) {
        if (!isVisible) {
            dispose();
            return;
        }
        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isNeedIntelligentCustomerService()) {
            initHotPane();
        } else {
            removeHotPane();
            setSize(AlphaFineConstants.FIELD_SIZE);
            refreshContainer();
        }
        super.setVisible(isVisible);
    }

    @Override
    public void dispose() {
        resetDialog();
        super.dispose();
    }

    /**
     * 重置搜索框
     */
    private void resetDialog() {
        removeSearchResult();
        searchTextField.setText(null);
    }

    /**
     * 移除搜索结果
     */
    private void removeSearchResult() {
        if (searchResultPane != null) {
            remove(searchResultPane);
            searchResultPane = null;
        }

    }

    /**
     * 展示搜索结果
     */
    private void showSearchResult(String text) {
        if (searchResultPane == null) {
            initSearchResultComponents();
        }
        initSearchWorker(text);
    }

    /**
     * 初始化搜索面板
     */
    private void initSearchResultComponents() {


        searchResultList = new AlphaFineList();
        searchResultList.setFixedCellHeight(AlphaFineConstants.CELL_HEIGHT);
        searchListModel = new SearchListModel(new SearchResult());
        searchResultList.setModel(searchListModel);
        searchResultPane = new JPanel();
        searchResultPane.setPreferredSize(AlphaFineConstants.CONTENT_SIZE);
        searchResultPane.setLayout(new BorderLayout());
        searchResultList.setCellRenderer(new ContentCellRender(storeText, segmentationResult));

        leftSearchResultPane = new UIScrollPane(searchResultList);
        leftSearchResultPane.setBorder(null);
        leftSearchResultPane.setBackground(Color.WHITE);
        leftSearchResultPane.setPreferredSize(new Dimension(AlphaFineConstants.LEFT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        rightSearchResultPane = new JPanel();
        rightSearchResultPane.setBackground(Color.WHITE);
        rightSearchResultPane.setPreferredSize(new Dimension(AlphaFineConstants.RIGHT_WIDTH - 1, AlphaFineConstants.CONTENT_HEIGHT));
        searchResultPane.add(leftSearchResultPane, BorderLayout.WEST);
        searchResultPane.add(rightSearchResultPane, BorderLayout.EAST);
        splitLabel = new UILabel();
        splitLabel.setPreferredSize(new Dimension(AlphaFineConstants.HEIGHT, 1));
        searchResultPane.add(splitLabel, BorderLayout.NORTH);
        add(searchResultPane, BorderLayout.SOUTH);
        setSize(AlphaFineConstants.FULL_SIZE);
    }

    /**
     * 异步加载搜索结果
     */
    private void initSearchWorker(final String text) {
        if (this.searchWorker != null && !this.searchWorker.isDone()) {
            this.searchWorker.cancel(true);
            this.searchWorker = null;
        }
        this.searchWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                rebuildList(text);
                return null;
            }

            @Override
            protected void done() {
                if (!isCancelled()) {
                    splitLabel.setIcon(null);
                    fireStopLoading();
                }
            }
        };
        this.searchWorker.execute();
    }

    /**
     * 恢复左侧列表面板
     */
    private void resumeLeftPane() {
        if (searchResultPane != null && defaultPane != null) {
            searchResultPane.remove(defaultPane);
            defaultPane = null;
            searchResultPane.add(leftSearchResultPane, BorderLayout.WEST);
        }
    }

    /**
     * 移除左侧列表面板
     */
    private void removeLeftPane() {
        if (searchListModel.isEmpty() && defaultPane == null) {
            defaultPane = new NoResultPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_NO_Result"), AlphaFineConstants.IMAGE_URL + "no_result.png");
            searchResultPane.remove(leftSearchResultPane);
            searchResultPane.add(defaultPane, BorderLayout.WEST);
            refreshContainer();
        }
    }

    /**
     * 停止加载状态
     */
    private void fireStopLoading() {
        if (searchResultPane != null) {
            removeLeftPane();
        }
    }

    /**
     * 刷新容器
     */
    private void refreshContainer() {
        validate();
        repaint();
        revalidate();
    }

    /**
     * 重新构建搜索结果列表
     * 先根据输入判断是不是隐藏的搜索功能
     *
     * @param searchText
     */
    private void rebuildList(String searchText) {
        resetContainer();
        setStoreText(searchText);
        if (searchText.startsWith(ADVANCED_SEARCH_MARK)) {
            dealWithSearchText(searchText);
        } else {
            storeText = searchText.trim();
            doNormalSearch(storeText);
        }

    }

    /**
     * 处理搜索字符串
     *
     * @param searchText
     */
    private void dealWithSearchText(String searchText) {
        if (searchText.startsWith(ACTION_MARK_SHORT) || searchText.startsWith(ACTION_MARK)) {
            buildActionList(new String[]{getStoreText(searchText)});
        } else if (searchText.startsWith(DOCUMENT_MARK_SHORT) || searchText.startsWith(DOCUMENT_MARK)) {
            buildDocumentList(new String[]{getStoreText(searchText)});
        } else if (searchText.startsWith(FILE_MARK_SHORT) || searchText.startsWith(FILE_MARK)) {
            buildFileList(getStoreText(searchText), new String[]{getStoreText(searchText)});
        } else if (searchText.startsWith(CPT_MARK) || searchText.startsWith(FRM_MARK)) {
            buildFileList(getStoreText(searchText), new String[]{searchText});
        } else if (searchText.startsWith(DS_MARK)) {
            buildFileList(getStoreText(searchText), new String[]{DS_NAME + getStoreText(searchText)});
        } else if (searchText.startsWith(PLUGIN_MARK_SHORT) || searchText.startsWith(PLUGIN_MARK)) {
            buildPluginList(new String[]{getStoreText(searchText)});
        } else if (searchText.startsWith(SIMILAR_MARK)) {
            buildSimilarList(new String[]{getStoreText(searchText)});
        }
    }

    /**
     * 截取字符串中关键词
     *
     * @param searchText
     * @return
     */
    private String getStoreText(String searchText) {
        setStoreText(searchText.substring(searchText.indexOf(StringUtils.BLANK) + 1, searchText.length()));
        return storeText;
    }

    /**
     * 重置面板
     */
    private void resetContainer() {
        rightSearchResultPane.removeAll();
        splitLabel.setIcon(new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/bigloading.gif")));
        resumeLeftPane();
        searchListModel.removeAllElements();
        searchListModel.resetSelectedState();
        refreshContainer();
    }

    /**
     * 普通搜索
     *
     * @param searchText
     */
    private void doNormalSearch(String searchText) {
        if (segmentationResult != null) {
            buildRecentList(segmentationResult);
            buildRecommendList(segmentationResult);
            buildActionList(segmentationResult);
            buildFileList(searchText, segmentationResult);
            buildDocumentList(segmentationResult);
            buildPluginList(segmentationResult);
            buildSimilarList(segmentationResult);
        }
        searchListModel.addElement(new BottomModel());
    }

    private void buildDocumentList(final String[] searchText) {
        addSearchResult(DocumentSearchManager.getInstance().getLessSearchResult(searchText));
    }

    private void buildFileList(String searchStr, final String[] searchText) {
        addSearchResult(FileSearchManager.getInstance().getLessSearchResult(searchStr, searchText));
    }

    private void buildActionList(final String[] searchText) {
        addSearchResult(ActionSearchManager.getInstance().getLessSearchResult(searchText));
    }

    private void buildPluginList(final String[] searchText) {
        addSearchResult(PluginSearchManager.getInstance().getLessSearchResult(searchText));
    }

    private void buildRecommendList(final String[] searchText) {
        addSearchResult(RecommendSearchManager.getInstance().getLessSearchResult(searchText));
    }

    private void buildRecentList(final String[] searchText) {
        addSearchResult(RecentSearchManager.getInstance().getLessSearchResult(searchText));

    }

    private void buildSimilarList(final String[] searchText) {
        addSearchResult(SimilarSearchManager.getInstance().getLessSearchResult(searchText));
    }

    private synchronized void addSearchResult(SearchResult searchResult) {
        for (AlphaCellModel object : searchResult) {
            AlphaFineHelper.checkCancel();
            searchListModel.addElement(object);
        }
    }

    /**
     * 右侧面板展示搜索结果的内容
     *
     * @param selectedValue
     */
    private void showResult(final AlphaCellModel selectedValue) {
        switch (selectedValue.getType()) {
            case FILE:
                final String fileName = ((FileModel) selectedValue).getFilePath().substring(ProjectConstants.REPORTLETS_NAME.length() + 1);
                showDefaultPreviewPane();
                if (fileName.endsWith(ProjectConstants.FRM_SUFFIX)) {
                    checkWorker();
                    this.showWorker = new SwingWorker<BufferedImage, Void>() {
                        @Override
                        protected BufferedImage doInBackground() {
                            Form form = null;
                            try {
                                form = FormIO.readForm(fileName);
                            } catch (Exception e) {
                                FineLoggerFactory.getLogger().error(e.getMessage(), e);
                            }
                            return FormIO.exportFormAsImage(form);
                        }

                        @Override
                        protected void done() {
                            if (!isCancelled()) {
                                rightSearchResultPane.removeAll();
                                try {
                                    rightSearchResultPane.add(new FilePreviewPane(get()));
                                } catch (InterruptedException e) {
                                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                                } catch (ExecutionException e) {
                                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                                }
                                validate();
                                repaint();
                            }

                        }
                    };
                    this.showWorker.execute();
                } else if (fileName.endsWith(ProjectConstants.CPT_SUFFIX)) {
                    checkWorker();
                    this.showWorker = new SwingWorker<BufferedImage, Void>() {
                        @Override
                        protected BufferedImage doInBackground() {
                            WorkBook workBook = null;
                            try {
                                workBook = (WorkBook) TemplateWorkBookIO.readTemplateWorkBook(fileName);
                            } catch (Exception e) {
                                FineLoggerFactory.getLogger().error(e.getMessage(), e);
                            }
                            BufferedImage bufferedImage = new ImageExporter().exportToImage(workBook);
                            return bufferedImage;
                        }

                        @Override
                        protected void done() {
                            if (!isCancelled()) {
                                rightSearchResultPane.removeAll();
                                try {
                                    rightSearchResultPane.add(new FilePreviewPane(get()));
                                    validate();
                                    repaint();
                                } catch (InterruptedException e) {
                                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                                } catch (ExecutionException e) {
                                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                                }
                            }

                        }
                    };
                    this.showWorker.execute();
                }
                break;
            case ACTION:
                rightSearchResultPane.removeAll();
                rightSearchResultPane.add(new NoResultPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_No_Result"), AlphaFineConstants.IMAGE_URL + "noresult.png"));
                validate();
                repaint();
                break;
            case DOCUMENT:
                rightSearchResultPane.removeAll();
                rightSearchResultPane.add(new DocumentPreviewPane((selectedValue).getName(), (selectedValue).getContent()));
                validate();
                repaint();
                break;
            case PLUGIN:
            case REUSE:
                showDefaultPreviewPane();
                checkWorker();
                this.showWorker = new SwingWorker<Image, Void>() {
                    @Override
                    protected Image doInBackground() {
                        BufferedImage bufferedImage = null;
                        try {
                            bufferedImage = ImageIO.read(new URL(((PluginModel) selectedValue).getImageUrl()));
                        } catch (IOException e) {
                            try {
                                bufferedImage = ImageIO.read(getClass().getResource("/com/fr/design/mainframe/alphafine/images/default_product.png"));
                            } catch (IOException e1) {
                                FineLoggerFactory.getLogger().error(e.getMessage(), e);
                            }
                        }
                        return bufferedImage;
                    }

                    @Override
                    protected void done() {
                        try {
                            if (!isCancelled()) {
                                rightSearchResultPane.removeAll();
                                rightSearchResultPane.add(new PluginPreviewPane((selectedValue).getName(), get(), ((PluginModel) selectedValue).getVersion(), ((PluginModel) selectedValue).getJartime(), ((PluginModel) selectedValue).getType(), ((PluginModel) selectedValue).getPrice()));
                                validate();
                                repaint();
                            }
                        } catch (Exception e) {
                        }
                    }
                };
                this.showWorker.execute();
                break;
            case ROBOT:
            case RECOMMEND_ROBOT:
                showDefaultPreviewPane();
                checkWorker();
                this.showWorker = new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() {
                        if (com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alpha_Hot_No_Item").equals((selectedValue).getName())) {
                            return StringUtils.EMPTY;
                        }
                        String content = RobotModel.getContent((selectedValue).getName());
                        if (StringUtils.isNotEmpty(content)) {
                            //1.去掉小帆底部的信息。2.修改链接标签，使点击能够正常跳转。
                            content = content.replaceAll(AlphaFineConstants.BOTTOM_REGEX_FIRST, StringUtils.EMPTY)
                                    .replaceAll(AlphaFineConstants.BOTTOM_REGEX_SECOND, StringUtils.EMPTY)
                                    .replaceAll(AlphaFineConstants.LINK_REGEX, StringUtils.EMPTY)
                                    .replaceAll("'\\)", StringUtils.EMPTY)
                                    .replaceAll(AlphaFineConstants.LINK_REGEX_ANOTHER, StringUtils.EMPTY);
                            return content;
                        } else {
                            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alpha_Hot_No_Item");
                        }
                    }

                    @Override
                    protected void done() {
                        if (!isCancelled() && rightSearchResultPane != null) {
                            rightSearchResultPane.removeAll();
                            try {
                                rightSearchResultPane.add(new RobotPreviewPane(selectedValue, get()));
                            } catch (InterruptedException e) {
                                FineLoggerFactory.getLogger().error("get hot item content error: " + e.getMessage());
                            } catch (ExecutionException e) {
                                FineLoggerFactory.getLogger().error("get hot item content execution error: " + e.getMessage());
                            }
                            validate();
                            repaint();
                        }
                    }
                };
                this.showWorker.execute();
                break;
            default:
                rightSearchResultPane.removeAll();

        }

    }

    /**
     * 检查
     */
    private void checkWorker() {
        if (this.showWorker != null && !this.showWorker.isDone()) {
            this.showWorker.cancel(true);
            this.showWorker = null;
        }
    }

    private void dealWithMoreOrLessResult(int index, MoreModel selectedValue) {
        if (ComparatorUtils.equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"), selectedValue.getContent())) {
            selectedValue.setContent(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowLess"));

            rebuildShowMoreList(index, selectedValue);
        } else {
            selectedValue.setContent(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowAll"));
            rebuildShowMoreList(index, selectedValue);
        }
    }

    private void showDefaultPreviewPane() {
        rightSearchResultPane.removeAll();
        UILabel label = new UILabel(new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/opening.gif")));
        label.setBorder(BorderFactory.createEmptyBorder(120, 0, 0, 0));
        rightSearchResultPane.add(label, BorderLayout.CENTER);
        refreshContainer();
    }

    /**
     * 为面板添加全局监听器
     */
    private void initGlobalListener() {
        initAWTEventListener();
    }

    /**
     * 为textfield添加监听器
     */
    private void initTextFieldListener() {
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (searchResultList.getModel().getSize() > 1) {
                        dealWithSearchResult(searchResultList.getSelectedValue());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (searchResultList.getSelectedIndex() == searchResultList.getModel().getSize() - 1) {
                        searchResultList.setSelectedIndex(0);
                    }
                    searchResultList.setSelectedIndex(searchResultList.getSelectedIndex() + 1);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    searchResultList.setSelectedIndex(searchResultList.getSelectedIndex() - 1);
                } else {
                    escAlphaFineDialog(e);
                }
            }
        });

        Timer timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!alreadyInitHot && StringUtils.isEmpty(searchTextField.getText())) {
                    alreadyInitHot = true;
                    removeSearchResult();
                    refreshContainer();
                    if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isNeedIntelligentCustomerService()) {
                        initHotPane();
                        setSize(AlphaFineConstants.FULL_SIZE);
                    } else {
                        setSize(AlphaFineConstants.FIELD_SIZE);
                    }
                    refreshContainer();
                    return;
                } else if (beforeSearchStr.equals(searchTextField.getText()) && StringUtils.isNotEmpty(beforeSearchStr)) {
                    if (alreadySearch) {
                        return;
                    } else {
                        removeHotPane();
                        removeSearchResult();
                        refreshContainer();
                        if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isNeedSegmentationCheckbox()) {
                            //是高级搜索
                            if (searchTextField.getText().toLowerCase().startsWith(ADVANCED_SEARCH_MARK)) {
                                segmentationResult = SegmentationManager.getInstance().startSegmentation(getStoreText(searchTextField.getText().toLowerCase()));
                            }
                            //是普通搜索
                            else {
                                segmentationResult = SegmentationManager.getInstance().startSegmentation(searchTextField.getText().toLowerCase());
                            }
                        } else {
                            if (StringUtils.isEmpty(getRealSearchText(searchTextField.getText()))) {
                                segmentationResult = null;
                            } else {
                                segmentationResult = new String[]{getRealSearchText(searchTextField.getText())};
                            }
                        }
                        doSearch(searchTextField.getText().toLowerCase());
                        alreadySearch = true;
                    }
                } else {
                    beforeSearchStr = searchTextField.getText();
                    alreadySearch = false;
                }
                if (beforeSearchStr.equals(searchTextField.getText()) && beforeSearchStr.length() != 0) {
                    alreadyInitHot = false;
                }
            }

        });
        timer.start();
    }

    /**
     * 去除特殊字符，空格等
     */
    private String getRealSearchText(String searchText) {
        searchText = searchText.toLowerCase();
        Pattern p = Pattern.compile(AlphaFineConstants.SPECIAL_CHARACTER_REGEX);
        Matcher m = p.matcher(searchText);
        searchText = m.replaceAll("").trim().replaceAll(" ", "");
        if (searchText.length() == 0) {
            return null;
        }
        return searchText;
    }

    /**
     * 处理搜索结果
     *
     * @param selectedValue
     */
    private void dealWithSearchResult(AlphaCellModel selectedValue) {
        doNavigate();
        saveLocalHistory(selectedValue);
    }

    /**
     * 当鼠标在搜索界面边界外点击时触发
     */
    private void initAWTEventListener() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (!AlphaFineDialog.this.isVisible()) {
                    return;
                }
                if (event instanceof MouseEvent) {
                    MouseEvent k = (MouseEvent) event;
                    if (SwingUtilities.isLeftMouseButton(k)) {
                        Point p = k.getLocationOnScreen();
                        Rectangle dialogRectangle = AlphaFineDialog.this.getBounds();
                        Rectangle paneRectangle = new Rectangle(AlphaFinePane.getAlphaFinePane().getLocationOnScreen(), AlphaFinePane.getAlphaFinePane().getSize());
                        if (!dialogRectangle.contains(p) && !paneRectangle.contains(p) && !forceOpen) {
                            AlphaFineDialog.this.dispose();
                            forceOpen = false;
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }

    @Override
    public void checkValid() throws Exception {
//不处理
    }

    /**
     * 导航到结果页面
     */
    private void doNavigate() {
        AlphaFineDialog.this.dispose();
        final AlphaCellModel model = searchResultList.getSelectedValue();
        if (model != null) {
            model.doAction();
        }
    }

    /**
     * 保存结果到本地（本地常用）
     *
     * @param cellModel
     */
    private void saveLocalHistory(final AlphaCellModel cellModel) {
        if (cellModel instanceof BottomModel) {
            return;
        }
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isNotEmpty(storeText)) {
                    RecentSearchManager searchManager = RecentSearchManager.getInstance();
                    searchManager.addModel(storeText, cellModel);
                    sendDataToServer(storeText, cellModel);
                    TemplateInfoCollector.getInstance().sendTemplateInfo();
                    ErrorInfoUploader.getInstance().sendErrorInfo();
                }
            }
        });
        sendThread.start();
    }

    /**
     * 上传数据到服务器
     *
     * @param searchKey
     * @param cellModel
     */
    private void sendDataToServer(String searchKey, AlphaCellModel cellModel) {
        if (cellModel.isNeedToSendToServer()) {
            String username = MarketConfig.getInstance().getBbsUsername();
            String uuid = DesignerEnvManager.getEnvManager().getUUID();
            String activityKey = DesignerEnvManager.getEnvManager().getActivationKey();
            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
            String key = searchKey;
            int resultKind = cellModel.getType().getTypeValue();
            String resultValue = CellModelHelper.getResultValueFromModel(cellModel);
            JSONObject object = JSONObject.create();
            try {
                object.put("uuid", uuid).put("activityKey", activityKey).put("username", username).put("createTime", createTime).put("key", key).put("resultKind", resultKind).put("resultValue", resultValue);
            } catch (JSONException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            final HashMap<String, String> para = new HashMap<>();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            para.put("token", CodeUtils.md5Encode(date, StringUtils.EMPTY, "MD5"));
            para.put("content", object.toString());
            HttpClient httpClient = new HttpClient(AlphaFineConstants.CLOUD_SERVER_URL, para, true);
            httpClient.asGet();
            if (!httpClient.isServerAlive()) {
                FineLoggerFactory.getLogger().error("Failed to sent data to server!");
            }
        }


    }

    /**
     * 点击显示更多时，添加对应的model到list；点击收起是移除model
     *
     * @param index
     * @param selectedValue
     */
    private void rebuildShowMoreList(final int index, final MoreModel selectedValue) {
        if ((selectedValue).getContent().equals(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_ShowLess"))) {
            splitLabel.setIcon(new ImageIcon(getClass().getResource(AlphaFineConstants.IMAGE_URL + "bigloading.gif")));
            if (this.searchWorker != null && !this.searchWorker.isDone()) {
                this.searchWorker.cancel(true);
                this.searchWorker = null;
            }
            this.searchWorker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    SearchResult moreResults = getMoreResult(selectedValue);
                    for (int i = 0; i < moreResults.size(); i++) {
                        searchListModel.add(index + AlphaFineConstants.SHOW_SIZE + 1 + i, moreResults.get(i));
                    }
                    return null;
                }

                @Override
                protected void done() {
                    if (!isCancelled()) {
                        splitLabel.setIcon(null);
                    }
                }
            };
            this.searchWorker.execute();

        } else {
            for (int i = 0; i < getMoreResult(selectedValue).size(); i++) {
                this.searchListModel.remove(index + AlphaFineConstants.SHOW_SIZE + 1);

            }
        }
    }

    private SearchResult getMoreResult(MoreModel selectedValue) {
        SearchResult moreResult;
        switch (selectedValue.getContentType()) {
            case PLUGIN:
                moreResult = PluginSearchManager.getInstance().getMoreSearchResult(searchTextField.getText());
                break;
            case DOCUMENT:
                moreResult = DocumentSearchManager.getInstance().getMoreSearchResult(searchTextField.getText());
                break;
            case FILE:
                moreResult = FileSearchManager.getInstance().getMoreSearchResult(searchTextField.getText());
                break;
            case ACTION:
                moreResult = ActionSearchManager.getInstance().getMoreSearchResult(searchTextField.getText());
                break;
            case ROBOT:
            case RECOMMEND_ROBOT:
                moreResult = SimilarSearchManager.getInstance().getMoreSearchResult(searchTextField.getText());
                break;
            case RECOMMEND:
                moreResult = RecommendSearchManager.getInstance().getMoreSearchResult(searchTextField.getText());
                break;
            default:
                moreResult = new SearchResult();
        }
        return moreResult;
    }

    private SearchListModel getModel() {
        return (SearchListModel) searchResultList.getModel();
    }

    public SearchListModel setListModel(SearchListModel jListModel) {
        this.searchListModel = jListModel;
        return this.searchListModel;
    }

    public SwingWorker getSearchWorker() {
        return searchWorker;
    }

    public void setSearchWorker(SwingWorker searchWorker) {
        this.searchWorker = searchWorker;
    }


    public boolean isForceOpen() {
        return forceOpen;
    }

    public void setForceOpen(boolean forceOpen) {
        this.forceOpen = forceOpen;
    }

    public String getStoreText() {
        return storeText;
    }

    public void setStoreText(String storeText) {
        this.storeText = storeText;
    }

    public UILabel getSplitLabel() {
        return splitLabel;
    }

    public void setSplitLabel(UILabel splitLabel) {
        this.splitLabel = splitLabel;
    }

    /**
     * 键盘退出AlphaFine
     *
     * @param e
     */
    private void escAlphaFineDialog(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (StringUtils.isBlank(searchTextField.getText()) || ComparatorUtils.equals(searchTextField.getText(), searchTextField.getPlaceHolder())) {
                AlphaFineDialog.this.setVisible(false);
            } else {
                searchTextField.setText(null);
                removeSearchResult();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (searchResultList.getModel().getSize() > 1) {
                dealWithSearchResult(searchResultList.getSelectedValue());
            }
        }
    }

    /**
     * +-------------------------------------+
     * | 自定义JList |
     * +-------------------------------------+
     */
    private class AlphaFineList extends JList<AlphaCellModel> {

        public AlphaFineList() {
            initListListener();
        }

        /**
         * 重写选中的方法
         *
         * @param index
         */
        @Override
        public void setSelectedIndex(int index) {
            if (index == 0 && getModel().getSize() > 1) {
                super.setSelectedIndex(1);
            } else if (index > 0 && checkSelectedIndex(index)) {
                int previousIndex = getSelectedIndex();
                super.setSelectedIndex(index);
                AlphaCellModel cellModel = getSelectedValue();
                if (cellModel != null && !cellModel.hasAction()) {
                    if (previousIndex <= getSelectedIndex()) {
                        setSelectedIndex(index + 1);
                    } else {
                        setSelectedIndex(index - 1);
                    }

                }
            }
            showResult(getSelectedValue());
            ensureIndexIsVisible(getSelectedIndex());
        }

        private boolean checkSelectedIndex(int index) {
            int size = getModel().getSize();
            return size > 0 && index < size;
        }

        private void initListListener() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedIndex = getSelectedIndex();
                    AlphaCellModel selectedValue = getSelectedValue();
                    if (e.getClickCount() == 2 && selectedValue.hasAction()) {
                        dealWithSearchResult(selectedValue);
                    } else if (e.getClickCount() == 1) {
                        if (selectedValue instanceof MoreModel && ((MoreModel) selectedValue).isNeedMore()) {
                            dealWithMoreOrLessResult(selectedIndex, (MoreModel) selectedValue);
                        } else if (selectedValue instanceof BottomModel) {
                            dealWithSearchResult(selectedValue);
                        }
                    }
                }
            });

            addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting() && getSelectedValue() != null) {
                        showResult(getSelectedValue());
                    }
                }
            });

            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    final int x = e.getX();
                    final int y = e.getY();
                    final Rectangle cellBounds = getCellBounds(getModel().getSize() - 1, getModel().getSize() - 1);
                    if (cellBounds != null && cellBounds.contains(x, y) && getModel().getElementAt(getModel().getSize() - 1) instanceof BottomModel) {
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    } else {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                }
            });

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    escAlphaFineDialog(e);

                }
            });
        }
    }

    /**
     * +-------------------------------------+
     * | 自定义ListModel |
     * +-------------------------------------+
     */
    private class SearchListModel extends DefaultListModel<AlphaCellModel> {
        SearchResult myDelegate;

        /**
         * 第一有效的项是否被选中
         */
        private boolean isValidSelected;

        public SearchListModel(SearchResult searchResult) {
            this.myDelegate = searchResult;
        }

        @Override
        public void addElement(AlphaCellModel element) {
            AlphaFineHelper.checkCancel();
            int index = myDelegate.size();
            myDelegate.add(element);
            fireContentsChanged(this, index, index);
            fireSelectedStateChanged(element, index);

        }

        @Override
        protected void fireContentsChanged(Object source, int index0, int index1) {
            if (myDelegate.size() > MAX_SHOW_SIZE) {
                leftSearchResultPane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
                leftSearchResultPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 2));
            } else {
                leftSearchResultPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            }
            super.fireContentsChanged(source, index0, index1);
        }

        /**
         * 触发选中第一有效的项
         *
         * @param element
         * @param index
         */
        private void fireSelectedStateChanged(AlphaCellModel element, int index) {
            if (element.hasAction() && !isValidSelected()) {
                searchResultList.setSelectedIndex(index);
                setValidSelected(true);
            }
        }

        @Override
        public AlphaCellModel getElementAt(int index) {
            return myDelegate.get(index);
        }

        @Override
        public void add(int index, AlphaCellModel element) {
            myDelegate.add(index, element);
            fireIntervalAdded(this, index, index);
        }

        @Override
        public AlphaCellModel remove(int index) {
            AlphaCellModel object = myDelegate.get(index);
            myDelegate.remove(object);
            fireContentsChanged(this, index, index);
            return object;
        }

        @Override
        public int getSize() {
            return this.myDelegate.size();
        }

        @Override
        public void removeAllElements() {
            this.myDelegate.clear();
        }

        /**
         * 重置选中状态
         */
        public void resetSelectedState() {
            setValidSelected(false);
        }

        private boolean isValidSelected() {
            return isValidSelected;
        }

        private void setValidSelected(boolean selected) {
            isValidSelected = selected;
        }

        @Override
        public boolean isEmpty() {
            return myDelegate.isEmpty();
        }

        public void resetState() {
            for (int i = 0; i < getSize(); i++) {
                getElementAt(i).resetState();
            }
        }
    }

    public void showIssuesList() {
        if (this.searchWorker != null && !this.searchWorker.isDone()) {
            this.searchWorker.cancel(true);
            this.searchWorker = null;
        }
        this.searchWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                resetContainer();
                if (modeList.size() == ONLY_ONE_AVAILABLE_MODEL && "".equals(modeList.get(1).getName())) {
                    RobotModel model = new RobotModel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alpha_Hot_No_Item"), null);
                    searchListModel.addElement(model);
                } else {
                    for (AlphaCellModel object : modeList) {
                        if (!searchListModel.contains(object)) {
                            searchListModel.addElement(object);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                if (!isCancelled()) {
                    splitLabel.setIcon(null);
                    fireStopLoading();
                }
            }
        };
        this.searchWorker.execute();
    }

    /**
     * 移除热门面板
     */
    private void removeHotPane() {
        if (hotPane != null) {
            remove(hotPane);
            hotPane = null;
        }
    }

    /**
     * 增加返回面板
     */
    private void initBackPane() {
        backPane = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_AlphaFine_Back"));
        jLabel.setIcon(IconLoader.getIcon(AlphaFineConstants.IMAGE_URL + AlphaFineConstants.BACK_ICON_NAME));
        jLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jLabel.setPreferredSize(new Dimension(80, 20));
        jLabel.setFont(AlphaFineConstants.SMALL_FONT);
        jLabel.setForeground(AlphaFineConstants.DARK_GRAY);
        jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backPane.add(jLabel, BorderLayout.WEST);

        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchResultPane != null) {
                    remove(searchResultPane);
                    searchResultPane = null;
                }
                if (DesignerEnvManager.getEnvManager().getAlphaFineConfigManager().isNeedIntelligentCustomerService()) {
                    initHotPane();
                    setSize(AlphaFineConstants.FULL_SIZE);
                } else {
                    setSize(AlphaFineConstants.FIELD_SIZE);
                }
                refreshContainer();
            }
        });
    }

    /**
     * +-------------------------------------+
     * | 自定义热门问题面板 |
     * +-------------------------------------+
     */
    private class HotIssueJpanel extends JPanel {

        public HotIssueJpanel(String[] str, int pngIndex) {
            this.setLayout(new BorderLayout());
            this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            this.setSize(AlphaFineConstants.HOT_ISSUES_JAPNEL_SIZE);

            JPanel pane1 = new JPanel(new BorderLayout());
            UILabel iconLabel = new UILabel(IconLoader.getIcon(AlphaFineConstants.IMAGE_URL + AlphaFineConstants.ALPHA_HOT_IMAGE_NAME + pngIndex + ".png"));
            iconLabel.setOpaque(true);
            iconLabel.setBackground(Color.WHITE);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            pane1.add(iconLabel, BorderLayout.NORTH);
            add(pane1, BorderLayout.NORTH);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setBackground(Color.white);
            UILabel title = new UILabel();
            if (StringUtils.isEmpty(str[0])) {
                title.setText(StringUtils.EMPTY);
            }
            title.setText(str[0]);
            title.setFont(AlphaFineConstants.MEDIUM_FONT_ANOTHER);
            title.setForeground(AlphaFineConstants.DARK_GRAY);
            title.setHorizontalAlignment(JTextField.CENTER);
            centerPanel.add(title);
            add(centerPanel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setBackground(Color.white);

            GridLayout gridLayout = new GridLayout(2, 2);


            for (int i = 1; i < str.length; i++) {
                final UILabel subTitle = new UILabel(str[i]);
                subTitle.setForeground(AlphaFineConstants.DARK_GRAY);
                subTitle.setFont(AlphaFineConstants.MEDIUM_FONT_ANOTHER);
                subTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
                subTitle.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        segmentationResult = null;
                        removeHotPane();
                        if (searchResultPane == null) {
                            initSearchResultComponents();
                        }
                        initBackPane();
                        searchResultPane.add(backPane, BorderLayout.NORTH);
                        refreshContainer();
                        modeList = HotIssuesManager.getInstance().getTitleSearchResult(subTitle.getText());
                        showIssuesList();
                    }
                });
                bottomPanel.add(subTitle);
            }
            bottomPanel.setLayout(gridLayout);
            bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 22, 0));
            add(bottomPanel, BorderLayout.SOUTH);
        }
    }

}
