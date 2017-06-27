package com.fr.design.mainframe.alphafine.component;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.cell.CellModelHelper;
import com.fr.design.mainframe.alphafine.cell.model.*;
import com.fr.design.mainframe.alphafine.cell.render.ContentCellRender;
import com.fr.design.mainframe.alphafine.listener.ComponentHandler;
import com.fr.design.mainframe.alphafine.listener.DocumentAdapter;
import com.fr.design.mainframe.alphafine.model.SearchListModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.preview.ActionPreviewPane;
import com.fr.design.mainframe.alphafine.preview.DocumentPreviewPane;
import com.fr.design.mainframe.alphafine.preview.FilePreviewPane;
import com.fr.design.mainframe.alphafine.preview.PluginPreviewPane;
import com.fr.design.mainframe.alphafine.search.manager.*;
import com.fr.form.main.Form;
import com.fr.form.main.FormIO;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.ImageExporter;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.main.impl.WorkBook;
import com.fr.stable.CodeUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

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

    private AlphaFineTextField searchTextField;
    private UIButton closeButton;
    private JPanel searchResultPane;
    private Point pressedPoint;
    private UIScrollPane leftSearchResultPane;
    private JPanel rightSearchResultPane;
    private AlphaFineList searchResultList;
    private SearchListModel searchListModel;
    private SwingWorker searchWorker;
    private SwingWorker showWorker;
    private String storeText;
    //是否强制打开，因为面板是否关闭绑定了全局鼠标事件，这里需要处理一下
    private boolean forceOpen;


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
                    if (ComparatorUtils.equals(keyStroke.toString(), storeKeyStroke.toString()) && AlphaFinePane.getAlphaFinePane().isVisible()) {
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
        searchTextField = new AlphaFineTextField("AlphaFine");
        searchTextField.setFont(AlphaFineConstants.GREATER_FONT);
        searchTextField.setBackground(Color.white);
        searchTextField.setBorderPainted(false);
        searchTextField.initKeyListener(this);
        JPanel topPane = new JPanel(new BorderLayout());
        UILabel iconLabel = new UILabel(new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/bigsearch.png")));
        iconLabel.setPreferredSize(AlphaFineConstants.ICON_LABEL_SIZE);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(Color.white);
        topPane.add(iconLabel, BorderLayout.WEST);
        topPane.add(searchTextField, BorderLayout.CENTER);
        closeButton = new UIButton() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 0, getSize().width, getSize().height);
                super.paintComponent(g);
            }
        };
        closeButton.setContentAreaFilled(false);
        closeButton.setPreferredSize(AlphaFineConstants.CLOSE_BUTTON_SIZE);
        closeButton.setIcon(new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/alphafine_close.png")));
        closeButton.set4ToolbarButton();
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        UILabel borderLabel = new UILabel();
        borderLabel.setBackground(AlphaFineConstants.GRAY);
        borderLabel.setPreferredSize(new Dimension(AlphaFineConstants.HEIGHT, 1));
        topPane.add(closeButton, BorderLayout.EAST);
        topPane.add(borderLabel, BorderLayout.SOUTH);
        add(topPane, BorderLayout.CENTER);
        searchTextField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent e) {
                doSearch(searchTextField.getText());
            }
        });
    }

    /**
     *
     */
    private void initProperties() {
        setUndecorated(true);
        addComponentListener(new ComponentHandler());
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

    // TODO: 2017/5/8  xiaxiang: 窗体圆角setShape()有毛边，重写paint方法可以解决毛边问题，但带来了别的问题,处理比较麻烦，暂用setShape();
//    public void paint(Graphics g){
//        Graphics2D g2 = (Graphics2D) g.create();
//        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2.setRenderingHints(qualityHints);
//        g2.setPaint(Color.WHITE);
//        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
//        g2.dispose();
//    }

    /**
     * 执行搜索
     *
     * @param text
     */
    private void doSearch(String text) {

        if (StringUtils.isBlank(text) || isNeedSearch(text)) {
            removeSearchResult();
        } else {
            showSearchResult();
        }
    }

    boolean isNeedSearch(String text) {
        return ComparatorUtils.equals("AlphaFine", text) || text.contains("'");
    }

    /**
     * 移除搜索结果
     */
    private void removeSearchResult() {
        if (searchResultPane != null) {
            remove(searchResultPane);
            searchResultPane = null;
        }
        setSize(AlphaFineConstants.FIELD_SIZE);
        repaint();
    }

    /**
     * 展示搜索结果
     */
    private void showSearchResult() {
        if (searchResultPane == null) {
            initSearchResultComponents();
            initTextFieldKeyListener();
        }
        initSearchWorker();
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
        searchResultList.setCellRenderer(new ContentCellRender());

        leftSearchResultPane = new UIScrollPane(searchResultList);
        leftSearchResultPane.setBackground(Color.white);
        leftSearchResultPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftSearchResultPane.setPreferredSize(new Dimension(AlphaFineConstants.LEFT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        rightSearchResultPane = new JPanel();
        rightSearchResultPane.setBackground(Color.white);
        searchResultPane.add(leftSearchResultPane, BorderLayout.WEST);
        rightSearchResultPane.setPreferredSize(new Dimension(AlphaFineConstants.RIGHT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        searchResultPane.add(rightSearchResultPane, BorderLayout.EAST);
        add(searchResultPane, BorderLayout.SOUTH);
        setSize(AlphaFineConstants.FULL_SIZE);
    }

    /**
     * 异步加载搜索结果
     */
    private void initSearchWorker() {
        if (this.searchWorker != null && !this.searchWorker.isDone()) {
            this.searchWorker.cancel(true);
            this.searchWorker = null;
        }
        this.searchWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                rebuildList(searchTextField.getText().toLowerCase());
                return null;
            }

            @Override
            protected void done() {
                if (!isCancelled() && getModel().getSize() > 0) {
                    searchResultList.setSelectedIndex(0);
                    showResult(searchResultList.getSelectedValue());
                }
            }
        };
        this.searchWorker.execute();
    }

    /**
     * 重新构建搜索结果列表
     * 先根据输入判断是不是隐藏的搜索功能
     *
     * @param searchText
     */
    private void rebuildList(String searchText) {
        resetContainer();
        if (searchText.startsWith(ADVANCED_SEARCH_MARK)) {
            if (searchText.startsWith(ACTION_MARK_SHORT) || searchText.startsWith(ACTION_MARK)) {
                storeText = searchText.substring(searchText.indexOf(StringUtils.BLANK) + 1, searchText.length());
                getActionList(storeText);
            } else if (searchText.startsWith(DOCUMENT_MARK_SHORT) || searchText.startsWith(DOCUMENT_MARK)) {
                storeText = searchText.substring(searchText.indexOf(StringUtils.BLANK) + 1, searchText.length());
                getDocumentList(storeText);
            } else if (searchText.startsWith(FILE_MARK_SHORT) || searchText.startsWith(FILE_MARK)) {
                storeText = searchText.substring(searchText.indexOf(StringUtils.BLANK) + 1, searchText.length());
                getFileList(storeText);
            } else if (searchText.startsWith(CPT_MARK) || searchText.startsWith(FRM_MARK)) {
                storeText = searchText.substring(searchText.indexOf(StringUtils.BLANK) + 1, searchText.length());
                getFileList(searchText);
            } else if (searchText.startsWith(DS_MARK)) {
                storeText = searchText.substring(searchText.indexOf(StringUtils.BLANK) + 1, searchText.length());
                getFileList(DS_NAME + storeText);
            } else if (searchText.startsWith(PLUGIN_MARK_SHORT) || searchText.startsWith(PLUGIN_MARK)) {
                storeText = searchText.substring(searchText.indexOf(StringUtils.BLANK) + 1, searchText.length());
                getPluginList(storeText);
            }
        } else {
            storeText = searchText.trim();
            doNormalSearch(storeText);
        }

    }

    /**
     * 重置面板
     */
    private void resetContainer() {
        searchResultList.resetSelectedIndex();
        searchListModel.removeAllElements();
        rightSearchResultPane.removeAll();
        rightSearchResultPane.validate();
        rightSearchResultPane.repaint();
    }

    /**
     * 普通搜索
     *
     * @param searchText
     */
    private void doNormalSearch(String searchText) {
        getRecentList(searchText);
        getRecommendList(searchText);
        getActionList(searchText);
        getFileList(searchText);
        getDocumentList(searchText);
        getPluginList(searchText);
    }

    private void getDocumentList(final String searchText) {
        SearchResult documentModelList = DocumentSearchManager.getDocumentSearchManager().getLessSearchResult(searchText);
        for (AlphaCellModel object : documentModelList) {
            AlphaFineHelper.checkCancel();
            searchListModel.addElement(object);
        }
    }

    private void getFileList(final String searchText) {
        SearchResult fileModelList = FileSearchManager.getFileSearchManager().getLessSearchResult(searchText);
        for (AlphaCellModel object : fileModelList) {
            AlphaFineHelper.checkCancel();
            searchListModel.addElement(object);
        }
    }

    private void getActionList(final String searchText) {
        SearchResult actionModelList = ActionSearchManager.getActionSearchManager().getLessSearchResult(searchText);
        for (AlphaCellModel object : actionModelList) {
            AlphaFineHelper.checkCancel();
            searchListModel.addElement(object);
        }
    }

    private void getPluginList(final String searchText) {
        SearchResult pluginModelList = PluginSearchManager.getPluginSearchManager().getLessSearchResult(searchText);
        for (AlphaCellModel object : pluginModelList) {
            AlphaFineHelper.checkCancel();
            searchListModel.addElement(object);
        }
    }

    private void getRecommendList(final String searchText) {
        SearchResult recommendModelList = RecommendSearchManager.getRecommendSearchManager().getLessSearchResult(searchText);
        for (AlphaCellModel object : recommendModelList) {
            AlphaFineHelper.checkCancel();
            searchListModel.addElement(object);
        }
    }

    private void getRecentList(final String searchText) {
        SearchResult recentModelList = RecentSearchManager.getRecentSearchManger().getLessSearchResult(searchText);
        for (AlphaCellModel object : recentModelList) {
            AlphaFineHelper.checkCancel();
            searchListModel.addElement(object);
        }

    }

    /**
     * 右侧面板展示搜索结果的内容
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
                                form = FormIO.readForm(FRContext.getCurrentEnv(), fileName);
                            } catch (Exception e) {
                                FRLogger.getLogger().error(e.getMessage());
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
                                    FRLogger.getLogger().error(e.getMessage());
                                } catch (ExecutionException e) {
                                    FRLogger.getLogger().error(e.getMessage());
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
                                workBook = (WorkBook) TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(), fileName);
                            } catch (Exception e) {
                                FRLogger.getLogger().error(e.getMessage());
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
                                    FRLogger.getLogger().error(e.getMessage());
                                } catch (ExecutionException e) {
                                    FRLogger.getLogger().error(e.getMessage());
                                }
                            }

                        }
                    };
                    this.showWorker.execute();
                }
                break;
            case ACTION:
                rightSearchResultPane.removeAll();
                rightSearchResultPane.add(new ActionPreviewPane());
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
                                FRLogger.getLogger().error(e.getMessage());
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
                        } catch (InterruptedException e) {
                            FRLogger.getLogger().error(e.getMessage());
                        } catch (ExecutionException e) {
                            FRLogger.getLogger().error(e.getMessage());
                        }

                    }
                };
                this.showWorker.execute();
                break;
            default:
                return;

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

    private void HandleMoreOrLessResult(int index, MoreModel selectedValue) {
        if (selectedValue.getContent().equals(Inter.getLocText("FR-Designer_AlphaFine_ShowAll"))) {
            selectedValue.setContent(Inter.getLocText("FR-Designer_AlphaFine_ShowLess"));
            rebuildShowMoreList(index, selectedValue);

        } else {
            selectedValue.setContent(Inter.getLocText("FR-Designer_AlphaFine_ShowAll"));
            rebuildShowMoreList(index, selectedValue);
        }
    }

    private void showDefaultPreviewPane() {
        rightSearchResultPane.removeAll();
        UILabel label = new UILabel(new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/opening.gif")));
        label.setBorder(BorderFactory.createEmptyBorder(120, 0, 0, 0));
        rightSearchResultPane.add(label, BorderLayout.CENTER);
        validate();
        repaint();
    }

    /**
     * 为面板添加全局监听器
     */
    private void initGlobalListener() {
        initAWTEventListener();
        initMouseListener();
    }

    private void initTextFieldKeyListener() {
        /**
         * 为textField添加键盘监听器，按上下方向键时把焦点给list,实现键盘操作
         */
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
                    searchResultList.requestFocus();
                }
            }
        });
    }

    /**
     * 窗口拖拽
     */
    private void initMouseListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                doMouseDragged(e);
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                doMousePressed(e);
            }

        });
    }

    private void doMousePressed(MouseEvent e) {

        pressedPoint = e.getPoint();

    }

    private void doMouseDragged(MouseEvent e) {

        Point point = e.getPoint();// 获取当前坐标

        Point locationPoint = getLocation();// 获取窗体坐标

        int x = locationPoint.x + point.x - pressedPoint.x;// 计算移动后的新坐标

        int y = locationPoint.y + point.y - pressedPoint.y;

        setLocation(x, y);// 改变窗体位置

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

    }

    private void doNavigate() {
        AlphaFineDialog.this.dispose();
        final AlphaCellModel model = searchResultList.getSelectedValue();
        model.doAction();
    }

    /**
     * 保存本地（本地常用）
     *
     * @param cellModel
     */
    private void saveHistory(AlphaCellModel cellModel) {
        RecentSearchManager recentSearchManager = RecentSearchManager.getRecentSearchManger();
        recentSearchManager.addRecentModel(storeText, cellModel);
        recentSearchManager.saveXMLFile();
        sendToServer(storeText, cellModel);

    }

    /**
     * 上传数据到服务器
     *
     * @param searchKey
     * @param cellModel
     */
    private void sendToServer(String searchKey, AlphaCellModel cellModel) {
        if (cellModel.isNeedToSendToServer()) {
            String username = DesignerEnvManager.getEnvManager().getBBSName();
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
                FRLogger.getLogger().error(e.getMessage());
            }
            final HashMap<String, String> para = new HashMap<>();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            para.put("token", CodeUtils.md5Encode(date, "", "MD5"));
            para.put("content", object.toString());
            HttpClient httpClient = new HttpClient(AlphaFineConstants.CLOUD_SERVER_URL,  para, true);
            httpClient.setTimeout(5000);
            httpClient.asGet();
            if (!httpClient.isServerAlive()) {
                FRLogger.getLogger().error("Failed to sent data to server!");
            }
        }



    }

    /**
     * 点击显示更多时，添加对应的model到list；点击收起是移除model
     *
     * @param index
     * @param selectedValue
     */
    private void rebuildShowMoreList(int index, MoreModel selectedValue) {
        SearchResult moreResult = getMoreResult(selectedValue);
        if ((selectedValue).getContent().equals(Inter.getLocText("FR-Designer_AlphaFine_ShowLess"))) {
            for (int i = 0; i < moreResult.size(); i++) {
                this.searchListModel.add(index + AlphaFineConstants.SHOW_SIZE + 1 + i, moreResult.get(i));
            }
        } else {
            for (int i = 0; i < moreResult.size(); i++) {
                this.searchListModel.remove(index + AlphaFineConstants.SHOW_SIZE + 1);

            }
        }
    }

    private SearchResult getMoreResult(MoreModel selectedValue) {
        SearchResult moreResult;
        switch (selectedValue.getContentType()) {
            case PLUGIN:
                moreResult = PluginSearchManager.getPluginSearchManager().getMoreSearchResult();
                break;
            case DOCUMENT:
                moreResult = DocumentSearchManager.getDocumentSearchManager().getMoreSearchResult();
                break;
            case FILE:
                moreResult = FileSearchManager.getFileSearchManager().getMoreSearchResult();
                break;
            case ACTION:
                moreResult = ActionSearchManager.getActionSearchManager().getMoreSearchResult();
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


    /**
     +-------------------------------------+
     |             自定义JList              |
     +-------------------------------------+
     */
    private class AlphaFineList extends JList<AlphaCellModel> {

        public AlphaFineList() {
            initListListener();
        }

        /**
         * 重置选项
         */
        public void resetSelectedIndex() {
            super.setSelectedIndex(0);
        }

        /**
         * 重写选中的方法
         * @param index
         */
        @Override
        public void setSelectedIndex(int index) {
            if (index >=0 && checkSelectedIndex(index)) {
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
            ensureIndexIsVisible(getSelectedIndex());
        }

        private boolean checkSelectedIndex(int index) {
            int size = getModel().getSize();
            return size > 0 && index < size;
        }

        private void initListListener() {

            /**
             * 为list添加键盘监听器
             */
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        doNavigate();
                        saveHistory(getSelectedValue());
                    }
                }
            });

            /**
             * 为list添加鼠标监听器
             */
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int selectedIndex = getSelectedIndex();
                    AlphaCellModel selectedValue = getSelectedValue();
                    if (e.getClickCount() == 2 && selectedValue.hasAction()) {
                        doNavigate();
                        saveHistory(selectedValue);
                    } else if (e.getClickCount() == 1) {
                        if (selectedValue instanceof MoreModel && ((MoreModel) selectedValue).isNeedMore()) {
                            HandleMoreOrLessResult(selectedIndex, (MoreModel) selectedValue);
                        }
                    }
                }
            });

            /**
             *单击时触发右侧面板展示搜索结果
             */
            addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        showResult(getSelectedValue());

                    }
                }
            });
        }


    }

}