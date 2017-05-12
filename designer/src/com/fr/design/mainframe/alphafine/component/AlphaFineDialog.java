package com.fr.design.mainframe.alphafine.component;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.alphafine.AlphaFineConstants;
import com.fr.design.mainframe.alphafine.AlphaFineHelper;
import com.fr.design.mainframe.alphafine.cell.cellRender.ContentCellRender;
import com.fr.design.mainframe.alphafine.cell.cellModel.*;
import com.fr.design.mainframe.alphafine.listener.ComponentHandler;
import com.fr.design.mainframe.alphafine.listener.DocumentAdapter;
import com.fr.design.mainframe.alphafine.model.SearchListModel;
import com.fr.design.mainframe.alphafine.model.SearchResult;
import com.fr.design.mainframe.alphafine.previewPane.DocumentPreviewPane;
import com.fr.design.mainframe.alphafine.previewPane.FilePreviewPane;
import com.fr.design.mainframe.alphafine.previewPane.PluginPreviewPane;
import com.fr.design.mainframe.alphafine.searchManager.*;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.form.main.Form;
import com.fr.form.main.FormIO;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.ImageExporter;
import com.fr.main.impl.WorkBook;
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
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by XiaXiang on 2017/3/21.
 */
public class AlphaFineDialog extends UIDialog {
    private AlphaFineTextField searchTextField;
    private UIButton closeButton;
    private JPanel searchResultPane;
    private Point pressedPoint;
    private UIScrollPane leftSearchResultPane;
    private JPanel rightSearchResultPane;
    private JList searchResultList;
    private SearchListModel searchListModel;
    private SwingWorker searchWorker;

    public AlphaFineDialog(Frame parent) {
        super(parent);
        initProperties();
        initListener();
        initComponents();
    }

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
                g.setColor( Color.white );
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

    private void initProperties() {
        setUndecorated(true);
        addComponentListener(new ComponentHandler());
        setSize(AlphaFineConstants.FIELD_SIZE);
        centerWindow(this);
    }

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
        win.setLocation((screenSize.width - winSize.width ) / 2, (screenSize.height - winSize.height) / AlphaFineConstants.SHOW_SIZE);
    }

    private void doSearch(String text) {
        if (text.length() < 2 || text.contains("'")) {
            return;
        }
        if (StringUtils.isBlank(text) || text.equals("AlphaFine")) {
            removeSearchResult();
        } else {
            showSearchResult(text);
        }

    }

    private void removeSearchResult() {
        if (searchResultPane != null) {
            remove(searchResultPane);
            searchResultPane = null;
        }
        setSize(AlphaFineConstants.FIELD_SIZE);
        repaint();
    }

    // TODO: 2017/5/8  xiaxiang: 窗体圆角setShape()有毛边，重写paint方法可以解决毛边问题，但带来了别的问题,处理比较麻烦，暂用setShape();
//    public void paint(Graphics g){
//
//        Graphics2D g2 = (Graphics2D) g.create();
//        RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2.setRenderingHints(qualityHints);
//        g2.setPaint(Color.WHITE);
//        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
//        g2.dispose();
//    }



    private void showSearchResult(String searchText) {
        if (searchResultPane == null) {
            initSearchResultComponents();
            initListListener();
        }
        initSearchWorker(searchText);
    }

    private void initSearchResultComponents() {
        searchResultList = new JList();
        searchResultPane = new JPanel();
        searchResultPane.setPreferredSize(AlphaFineConstants.CONTENT_SIZE);
        searchResultPane.setLayout(new BorderLayout());
        searchResultList.setCellRenderer(new ContentCellRender());
        searchResultList.setFixedCellHeight(AlphaFineConstants.CELL_HEIGHT);

        leftSearchResultPane = new UIScrollPane(searchResultList);
        leftSearchResultPane.setBackground(Color.white);
        leftSearchResultPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        leftSearchResultPane.setPreferredSize(new Dimension(AlphaFineConstants.LEFT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        rightSearchResultPane = new JPanel();
        rightSearchResultPane.setBackground(Color.white);
        searchResultPane.add(leftSearchResultPane, BorderLayout.WEST);
        rightSearchResultPane.setPreferredSize(new Dimension(AlphaFineConstants.RIGHT_WIDTH, AlphaFineConstants.CONTENT_HEIGHT));
        searchResultPane.add(rightSearchResultPane, BorderLayout.EAST);
        add(searchResultPane, BorderLayout.SOUTH);
        setSize(AlphaFineConstants.FULL_SIZE);
    }

    private void initSearchWorker(final String searchText) {
        searchResultList.setModel(new SearchListModel(AlphaSearchManager.getSearchManager().showDefaultSearchResult()));
        if (this.searchWorker != null && !this.searchWorker.isDone()) {
            this.searchWorker.cancel(true);
            this.searchWorker = null;
        }
        this.searchWorker = new SwingWorker<SearchListModel, String>() {

            @Override
            protected SearchListModel doInBackground() throws Exception {
                return setjListModel(new SearchListModel(AlphaSearchManager.getSearchManager().showLessSearchResult(searchText)));
            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        searchResultList.setModel(get());
                        searchResultList.validate();
                        searchResultList.repaint();
                        validate();
                        repaint();
                    }
                } catch (InterruptedException e) {
                    FRLogger.getLogger().error(e.getMessage());
                } catch (ExecutionException e) {
                    searchResultList.setModel(null);
                    FRLogger.getLogger().error(e.getMessage());
                }

            }

        };
        this.searchWorker.execute();
    }

    private void initListListener() {
        searchResultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = searchResultList.getSelectedIndex();
                Object selectedValue = searchResultList.getSelectedValue();
                if (e.getClickCount() == 2) {
                    final int i = searchResultList.locationToIndex(e.getPoint());
                    searchResultList.setSelectedIndex(i);
                    doNavigate(selectedIndex);
                    if (selectedValue instanceof AlphaCellModel) {
                        SearchResult originalResultList = LatestSearchManager.getLatestSearchManager().getLatestModelList();
                        originalResultList.add(searchResultList.getSelectedValue());
                        LatestSearchManager.getLatestSearchManager().setLatestModelList(originalResultList);
                        //保存最近搜索
                        saveHistory(originalResultList);
                    }
                } else if (e.getClickCount() == 1) {
                    if (selectedValue instanceof MoreModel && ((MoreModel) selectedValue).isNeedMore()) {
                        HandleMoreOrLessResult(selectedIndex, (MoreModel) selectedValue);
                    }
                }
            }
        });

        // TODO: 2017/5/8  xiaxiang: e.getClickCount() == 1 时，偶发性的不能触发，所以先放到valueChanged
        searchResultList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    showResult(searchResultList.getSelectedIndex(), searchResultList.getSelectedValue());

                }
            }
        });

        searchResultList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doNavigate(searchResultList.getSelectedIndex());
                }
            }
        });
    }

    private void showResult(int index, Object selectedValue) {
        if (selectedValue instanceof FileModel) {
            String fileName = ((FileModel) selectedValue).getFilePath().substring(ProjectConstants.REPORTLETS_NAME.length() + 1);
            showDefaultPreviewPane();
            if (fileName.endsWith("frm")) {
                if (this.searchWorker != null && !this.searchWorker.isDone()) {
                    this.searchWorker.cancel(true);
                    this.searchWorker = null;
                }
                this.searchWorker = new SwingWorker<BufferedImage, Void>() {
                    @Override
                    protected BufferedImage doInBackground() throws Exception {
                        Form form = FormIO.readForm(FRContext.getCurrentEnv(), fileName);
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
                this.searchWorker.execute();
            } else if (fileName.endsWith("cpt")) {
                if (this.searchWorker != null && !this.searchWorker.isDone()) {
                    this.searchWorker.cancel(true);
                    this.searchWorker = null;
                }
                this.searchWorker = new SwingWorker<BufferedImage, Void>() {
                    @Override
                    protected BufferedImage doInBackground() throws Exception {
                        WorkBook workBook = (WorkBook) TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(), fileName);
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
                this.searchWorker.execute();


            }

        } else if (selectedValue instanceof DocumentModel) {
            rightSearchResultPane.removeAll();
            rightSearchResultPane.add(new DocumentPreviewPane(((DocumentModel) selectedValue).getName(), ((DocumentModel) selectedValue).getContent()));
            validate();
            repaint();
        } else if (selectedValue instanceof PluginModel) {
            showDefaultPreviewPane();
            if (this.searchWorker != null && !this.searchWorker.isDone()) {
                this.searchWorker.cancel(true);
                this.searchWorker = null;
            }
            this.searchWorker = new SwingWorker<Image, Void>() {
                @Override
                protected Image doInBackground() throws Exception {
                    BufferedImage bufferedImage = ImageIO.read(new URL(((PluginModel) selectedValue).getImageUrl()));
                    return bufferedImage;
                }

                @Override
                protected void done() {
                    try {
                        rightSearchResultPane.removeAll();
                        rightSearchResultPane.add(new PluginPreviewPane(((PluginModel) selectedValue).getName(), get(), ((PluginModel) selectedValue).getVersion(), ((PluginModel) selectedValue).getJartime(), ((PluginModel) selectedValue).getType(), ((PluginModel) selectedValue).getPrice()));
                        validate();
                        repaint();
                    } catch (InterruptedException e) {
                        FRLogger.getLogger().error(e.getMessage());
                    } catch (ExecutionException e) {
                        FRLogger.getLogger().error(e.getMessage());
                    }

                }
            };
            this.searchWorker.execute();

        } else if (selectedValue instanceof ActionModel) {
            showDefaultPreviewPane();
        }

    }

    private void HandleMoreOrLessResult(int index, MoreModel selectedValue) {
        if (selectedValue.getContent().equals(Inter.getLocText("FR-Designer_AlphaFine_ShowAll"))) {
            selectedValue.setContent(Inter.getLocText("FR-Designer_AlphaFine_ShowLess"));
            rebuildList(index, selectedValue);

        } else {
            selectedValue.setContent(Inter.getLocText("FR-Designer_AlphaFine_ShowAll"));
            rebuildList(index, selectedValue);
        }
    }

    private void showDefaultPreviewPane() {
        rightSearchResultPane.removeAll();
        UILabel label = new UILabel(new ImageIcon(getClass().getResource("/com/fr/design/mainframe/alphafine/images/opening.gif")));
        label.setBorder(BorderFactory.createEmptyBorder(120,0,0,0));
        rightSearchResultPane.add(label, BorderLayout.CENTER);
        validate();
        repaint();
    }

    private void initListener() {
        initAWTEventListener();

        initMouseListener();

    }

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
                        Rectangle paneRectangle = new Rectangle(AlphaFinePane.createAlphaFinePane().getLocationOnScreen(), AlphaFinePane.createAlphaFinePane().getSize());
                        if (!dialogRectangle.contains(p) && !paneRectangle.contains(p)) {
                            AlphaFineDialog.this.dispose();
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK|AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * 全局快捷键
     * @return
     */
    public static AWTEventListener listener() {
        return new AWTEventListener() {

            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof KeyEvent) {
                    KeyEvent e = (KeyEvent) event;
                    KeyStroke keyStroke = (KeyStroke) KeyStroke.getAWTKeyStrokeForEvent(e);
                    KeyStroke storeKeyStroke = DesignerEnvManager.getEnvManager().getAlphafineConfigManager().getShortCutKeyStore();
                    if (ComparatorUtils.equals(keyStroke.toString(), storeKeyStroke.toString())) {
                        doClickAction();
                    }

                }
            }
        };
    }

    private static void doClickAction() {
        AlphaFineHelper.showAlphaFineDialog();
    }


    @Override
    public void checkValid() throws Exception {

    }

    private void doNavigate(int index) {
        AlphaFineDialog.this.dispose();
        final Object value = searchResultList.getSelectedValue();
        if (value instanceof ActionModel) {
            ((ActionModel) value).getAction().actionPerformed(null);
        } else if (value instanceof FileModel) {
            DesignerContext.getDesignerFrame().openTemplate(new FileNodeFILE(new FileNode(((FileModel)value).getFilePath(), false)));
        } else if (value instanceof PluginModel) {
            String url = ((PluginModel) value).getPluginUrl();
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e) {
                FRLogger.getLogger().error(e.getMessage());
            } catch (URISyntaxException e) {
                FRLogger.getLogger().error(e.getMessage());
            }
        } else if (value instanceof DocumentModel) {
            String url = ((DocumentModel) value).getDocumentUrl();
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e) {
                FRLogger.getLogger().error(e.getMessage());

            } catch (URISyntaxException e) {
                FRLogger.getLogger().error(e.getMessage());
            }
        }

    }

    /**
     * todo:保存到本地的逻辑待修改
     * @param searchResult
     */
    private void saveHistory(SearchResult searchResult) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(AlphaFineHelper.getInfoFile()));
            os.writeObject(searchResult);
            os.close();
        } catch (IOException e) {
            FRLogger.getLogger().error(e.getMessage());
        }
        
        sendToServer();

    }

    /**
     *todo:还没做上传服务器
     */
    private void sendToServer() {

    }

    private void rebuildList(int index, MoreModel selectedValue) {
        SearchResult moreResult = getMoreResult(selectedValue);
        if((selectedValue).getContent().equals(Inter.getLocText("FR-Designer_AlphaFine_ShowLess"))) {
            for (int i = 0; i < moreResult.size(); i++) {
                this.searchListModel.insertElementAt(moreResult.get(i), index + AlphaFineConstants.SHOW_SIZE -1 + i);
            }
        } else {
            for (int i = 0; i < moreResult.size(); i++) {
                this.searchListModel.removeElementAt(index + AlphaFineConstants.SHOW_SIZE - 1);

            }
        }
        this.searchResultList.validate();
        this.searchResultList.repaint();
        validate();
        repaint();

    }

    private SearchResult getMoreResult(MoreModel selectedValue) {
        SearchResult moreResult;
        switch (selectedValue.getType()) {
            case PLUGIN:
                moreResult = PluginSearchManager.getPluginSearchManager().showMoreSearchResult();
                break;
            case DOCUMENT:
                moreResult = DocumentSearchManager.getDocumentSearchManager().showMoreSearchResult();
                break;
            case FILE:
                moreResult = FileSearchManager.getFileSearchManager().showMoreSearchResult();
                break;
            case ACTION:
                moreResult = ActionSearchManager.getActionSearchManager().showMoreSearchResult();
                break;
            default:
                moreResult = AlphaSearchManager.getSearchManager().showMoreSearchResult();
        }
        return moreResult;
    }

    private SearchListModel getModel() {
        return (SearchListModel) searchResultList.getModel();
    }

    //测试
    public static void main(String[] args) {
        AlphaFineDialog alphaFineDialog = new AlphaFineDialog(DesignerContext.getDesignerFrame());
        alphaFineDialog.setSize(new Dimension(680,55));
        alphaFineDialog.setVisible(true);
    }


    public SearchListModel setjListModel(SearchListModel jListModel) {
        this.searchListModel = jListModel;
        return this.searchListModel;
    }

    public SwingWorker getSearchWorker() {
        return searchWorker;
    }

    public void setSearchWorker(SwingWorker searchWorker) {
        this.searchWorker = searchWorker;
    }

}