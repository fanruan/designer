package com.fr.design.update.ui.dialog;

import com.fr.decision.update.info.UpdateCallBack;
import com.fr.decision.update.info.UpdateProgressCallBack;
import com.fr.design.RestartHelper;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.update.actions.FileProcess;
import com.fr.design.update.domain.UpdateConstants;
import com.fr.design.update.domain.UpdateInfoCachePropertyManager;
import com.fr.design.update.factory.DirectoryOperationFactory;
import com.fr.design.update.ui.widget.LoadingLabel;
import com.fr.design.update.ui.widget.UpdateActionLabel;
import com.fr.design.update.ui.widget.UpdateInfoTable;
import com.fr.design.update.ui.widget.UpdateInfoTableCellRender;
import com.fr.design.update.ui.widget.UpdateInfoTableModel;
import com.fr.design.update.ui.widget.UpdateInfoTextAreaCellRender;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.*;
import com.fr.general.http.HttpToolbox;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.third.org.apache.http.client.methods.CloseableHttpResponse;
import com.fr.third.org.apache.http.client.methods.HttpGet;
import com.fr.third.org.apache.http.impl.client.CloseableHttpClient;
import com.fr.workspace.WorkContext;
import com.sun.java.swing.plaf.motif.MotifProgressBarUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static java.nio.charset.StandardCharsets.*;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class UpdateMainDialog extends UIDialog {
    public static final Dimension DEFAULT = new Dimension(660, 620);

    private static final Dimension PROGRESSBAR = new Dimension(120, 15);
    private static final Dimension UPDATE_BUTTON = new Dimension(80, 24);
    private static final int UPDATE_PANE_ROW_SIZE = 30;
    private static final int UPDATE_CONTENT_PANE_ROW_SIZE = 10;
    private static final int UPDATE_CONTENT_PANE_COLUMN_SIZE = 10;
    private static final int UPDATE_CONTENT_PANE_LABEL_COLUMN_SIZE = 100;
    private static final int SEARCH_PANE_ROW_SIZE = 50;
    private static final int SEARCH_PANE_TEXT_COLUMN = 130;
    private static final int SEARCH_PANE_COLUMN_GAP = 3;
    private static final int UPDATE_INFO_TABLE_HEADER_TIME_WIDTH = 120;
    private static final int UPDATE_CONTENT_PANE_BORDER_COLOR = 0xCCCCCC;
    private static final int RESTORE_LABEL_COLOR = 0x3384F0;

    private static final String UPDATE_CACHE_STATE_FAIL = "fail";
    private static final String UPDATE_CACHE_STATE_SUCCESS = "success";

    private final SimpleDateFormat CHANGELOG_FORMAT = new SimpleDateFormat("M/d/y, h:m:s a", Locale.ENGLISH);
    private final SimpleDateFormat UPDATE_INFO_TABLE_FORMAT = new SimpleDateFormat("yyyy.MM.dd");

    private JSONObject downloadFileConfig;
    //最新版本标签
    private LoadingLabel loadingLabel;
    //更新按钮
    private UIButton updateButton;
    //有新版本提示标签
    private UILabel updateLabel;

    //jar包版本信息面板，包括当前版本和最新版本
    private JPanel jarVersionInfoPane;
    //jar包更新信息面板，包括每个版本更新的信息
    private JPanel jarUpdateInfoPane;
    //jar包更新操作面板，包括更新重启按钮和进度条
    private JPanel updateActionPane;
    //进度条
    private JProgressBar progressBar;
    //更新版本提示面板
    private JPanel updateVersionReminderPane;
    //jar包版本标签
    private UILabel jarCurrentLabel;
    //jar包还原标签
    private UILabel jarRestoreLabel;
    //更新信息搜索按钮
    private UIButton searchUpdateInfoBtn;
    //搜索更新信息关键词文本框
    private UITextField searchUpdateInfoKeyword;

    private UpdateInfoTable updateInfoTable;

    private ArrayList<Object[]> updateInfoList;

    private boolean getUpdateInfoSuccess;

    private UpdateInfoCachePropertyManager cacheProperty;
    private String lastUpdateCacheTime;
    private String lastUpdateCacheState = UPDATE_CACHE_STATE_FAIL;

    private boolean autoUpdateAfterInit = false;  // 是否在加载结束后，自动开始更新

    public UpdateMainDialog(Dialog parent) {
        super(parent);
        initComponents();
    }

    public UpdateMainDialog(Frame parent) {
        super(parent);
        setModal(true);
        initComponents();
    }

    /**
     * 等待面板初始化结束后，点击"更新"按钮。
     */
    public void setAutoUpdateAfterInit() {
        autoUpdateAfterInit = true;
    }
    private void initUpdateActionPane() {
        double[] rowUpdateSubContentPaneSize = {UPDATE_CONTENT_PANE_ROW_SIZE, TableLayout.PREFERRED, UPDATE_CONTENT_PANE_ROW_SIZE};
        double[] rowUpdateContentPaneSize = {TableLayout.PREFERRED};
        double[] columnUpdateSubContentPaneProgressSize = {TableLayout.FILL, TableLayout.PREFERRED};
        double[] columnUpdateSubContentPaneSize = {UPDATE_CONTENT_PANE_COLUMN_SIZE, TableLayout.FILL, TableLayout.PREFERRED};
        JPanel progressBarPane = new JPanel(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setUI(new MotifProgressBarUI());
        progressBar.setForeground(UpdateConstants.BAR_COLOR);
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(PROGRESSBAR);

        updateLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_New_Version_Available"));
        updateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        updateLabel.setVisible(false);

        progressBarPane.add(GUICoreUtils.createBorderLayoutPane(
                progressBar, BorderLayout.CENTER,
                updateLabel, BorderLayout.EAST
        ), BorderLayout.CENTER);

        updateActionPane = TableLayoutHelper.createCommonTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(), new UILabel(), new UILabel()},
                new Component[]{new UILabel(), initPaneContent(getBackground(), rowUpdateContentPaneSize, columnUpdateSubContentPaneProgressSize, progressBarPane, updateButton), new UILabel()},
                new Component[]{new UILabel(), new UILabel(), new UILabel()}
        }, rowUpdateSubContentPaneSize, columnUpdateSubContentPaneSize, LayoutConstants.VGAP_LARGE);
    }

    private JPanel initPaneContent(Color color, double[] row, double[] column, Component... var) {
        JPanel paneContent = TableLayoutHelper.createTableLayoutPane(new Component[][]{var}, row, column);
        paneContent.setBackground(color);
        return paneContent;
    }

    private void initJarVersionInfoPane() {
        double[] rowUpdatePaneSize = {UPDATE_PANE_ROW_SIZE, TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] columnUpdatePaneSize = {TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED};
        double[] rowUpdateContentPaneSize = {TableLayout.PREFERRED};
        double[] columnUpdateContentPaneSize = {TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED};
        double[] rowUpdateSubContentPaneSize = {UPDATE_CONTENT_PANE_ROW_SIZE, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, UPDATE_CONTENT_PANE_ROW_SIZE};
        double[] columnUpdateSubContentPaneSize = {UPDATE_CONTENT_PANE_COLUMN_SIZE, TableLayout.FILL, TableLayout.PREFERRED};
        double[] columnUpdateSubContentPaneLabelSize = {UPDATE_CONTENT_PANE_LABEL_COLUMN_SIZE, TableLayout.PREFERRED};

        JPanel jarUpdateContentPane = new JPanel();
        jarUpdateContentPane.setLayout(new BorderLayout());
        jarUpdateContentPane.setBorder(BorderFactory.createLineBorder(new Color(UPDATE_CONTENT_PANE_BORDER_COLOR)));

        JPanel jarUpdateContentPane2 = TableLayoutHelper.createCommonTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(), new UILabel(), new UILabel()},
                new Component[]{new UILabel(), updateVersionReminderPane, new UILabel()},
                new Component[]{new UILabel(), initPaneContent(Color.WHITE, rowUpdateContentPaneSize, columnUpdateSubContentPaneLabelSize, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_JAR_Version")), jarCurrentLabel), new UILabel()},
                new Component[]{new UILabel(), initPaneContent(Color.WHITE, rowUpdateContentPaneSize, columnUpdateSubContentPaneLabelSize, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Latest_JAR")), loadingLabel), new UILabel()},
                new Component[]{new UILabel(), new UILabel(), new UILabel()}
        }, rowUpdateSubContentPaneSize, columnUpdateSubContentPaneSize, LayoutConstants.VGAP_LARGE);
        jarUpdateContentPane2.setBackground(Color.WHITE);
        jarUpdateContentPane.add(jarUpdateContentPane2);
        jarVersionInfoPane = TableLayoutHelper.createCommonTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(), new UILabel(), new UILabel()},
                new Component[]{new UILabel(), initPaneContent(getBackground(), rowUpdateContentPaneSize, columnUpdateContentPaneSize, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_JarUpdate")), new UILabel(), jarRestoreLabel), new UILabel()},
                new Component[]{new UILabel(), jarUpdateContentPane, new UILabel()}
        }, rowUpdatePaneSize, columnUpdatePaneSize, LayoutConstants.VGAP_LARGE);
    }

    private void initJarUpdateInfoPane() {
        double[] rowUpdatePaneSize = {SEARCH_PANE_ROW_SIZE, TableLayout.FILL};
        double[] columnUpdatePaneSize = {TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED};

        double[] searchRow = {UPDATE_CONTENT_PANE_ROW_SIZE, TableLayout.PREFERRED, SEARCH_PANE_COLUMN_GAP * 2};
        double[] searchColumn = {TableLayout.FILL, SEARCH_PANE_TEXT_COLUMN, TableLayout.PREFERRED};
        initUpdateInfoSearchPane();
        JPanel searchPane = TableLayoutHelper.createCommonTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(), new UILabel(), new UILabel()},
                new Component[]{new UILabel(), searchUpdateInfoKeyword, searchUpdateInfoBtn},
                new Component[]{new UILabel(), new UILabel(), new UILabel()}
        }, searchRow, searchColumn, LayoutConstants.VGAP_LARGE);

        String[] columnNames = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Date"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Content"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_SignHeader")};
        initUpdateInfoTable(columnNames);

        UIScrollPane uiScrollPane = new UIScrollPane(updateInfoTable);
        jarUpdateInfoPane = TableLayoutHelper.createCommonTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(), searchPane, new UILabel()},
                new Component[]{new UILabel(), uiScrollPane, new UILabel()}
        }, rowUpdatePaneSize, columnUpdatePaneSize, LayoutConstants.VGAP_LARGE);
    }

    private void initUpdateInfoTable(String[] columnNames) {
        int updateTimeColIndex = 0;
        int updateTitleColIndex = 1;
        int updateSignColIndex = 2;

        updateInfoTable = new UpdateInfoTable(columnNames);

        updateInfoTable.setShowGrid(false);
        updateInfoTable.setCellSelectionEnabled(false);
        TableRowSorter<UpdateInfoTableModel> sorter = new TableRowSorter<>(updateInfoTable.getDataModel());
        sorter.setSortable(updateTimeColIndex, true);
        sorter.setSortable(updateTitleColIndex, false);
        sorter.setSortable(updateSignColIndex, false);
        updateInfoTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(updateTimeColIndex, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);

        updateInfoTable.getTableHeader().setReorderingAllowed(false);
        updateInfoTable.getColumnModel().getColumn(updateTimeColIndex).setMaxWidth(UPDATE_INFO_TABLE_HEADER_TIME_WIDTH);
        updateInfoTable.getColumnModel().getColumn(updateTimeColIndex).setMinWidth(UPDATE_INFO_TABLE_HEADER_TIME_WIDTH);
        updateInfoTable.getColumnModel().getColumn(updateSignColIndex).setMaxWidth(0);
        updateInfoTable.getColumnModel().getColumn(updateSignColIndex).setMinWidth(0);
        updateInfoTable.getTableHeader().getColumnModel().getColumn(updateSignColIndex).setMaxWidth(0);
        updateInfoTable.getTableHeader().getColumnModel().getColumn(updateSignColIndex).setMinWidth(0);
        updateInfoTable.getColumn(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Date")).setCellRenderer(new UpdateInfoTableCellRender());
        updateInfoTable.getColumn(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Content")).setCellRenderer(new UpdateInfoTextAreaCellRender());
    }

    private void initUpdateInfoSearchPane() {
        searchUpdateInfoKeyword = new UITextField();
        searchUpdateInfoKeyword.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String keyword = searchUpdateInfoKeyword.getText();
                if (ComparatorUtils.equals(keyword, StringUtils.EMPTY) && getUpdateInfoSuccess) {
                    updateInfoList.clear();
                    getUpdateInfo(keyword).execute();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        searchUpdateInfoBtn = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Search"));
        searchUpdateInfoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getUpdateInfoSuccess) {
                    updateInfoList.clear();
                    getUpdateInfo(searchUpdateInfoKeyword.getText()).execute();
                }
            }
        });
    }

    private void initButtonAndLabel() {
        loadingLabel = new LoadingLabel();
        loadingLabel.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Checking_Jar_Update"));
        updateButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Update"));
        updateButton.setPreferredSize(UPDATE_BUTTON);
        updateButton.setEnabled(false);

        double[] rowSize = {TableLayout.PREFERRED};

        double[] colSize = {UPDATE_CONTENT_PANE_LABEL_COLUMN_SIZE, TableLayout.PREFERRED};
        updateVersionReminderPane = initPaneContent(
                Color.WHITE, rowSize, colSize,
                new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Designer_Version")),
                new UILabel(UpdateConstants.DEFAULT_APP_NAME + StringUtils.BLANK + ProductConstants.VERSION)
        );


        jarCurrentLabel = new UILabel(StringUtils.isEmpty(GeneralUtils.readBuildNO()) ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Not_Install_Version") : GeneralUtils.readBuildNO(), SwingConstants.CENTER);
        UILabel noJarPreviousRevision = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_No_Previous_Version"));
        UpdateActionLabel jarRestorePreviousRevision = new UpdateActionLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Restore"), false);
        jarRestorePreviousRevision.setForeground(new Color(RESTORE_LABEL_COLOR));
        jarRestorePreviousRevision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RestoreDialog dialog = new RestoreDialog(DesignerContext.getDesignerFrame(), true);
                dialog.showDialog();
            }
        });
        //choose RestoreLabel to show
        boolean isNeedRestore = ArrayUtils.isNotEmpty(DirectoryOperationFactory.listFilteredFiles(StableUtils.getInstallHome(), getBackupDirectory()));
        jarRestoreLabel = isNeedRestore ? jarRestorePreviousRevision : noJarPreviousRevision;
    }

    private void initComponents() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());


        initButtonAndLabel();

        initJarVersionInfoPane();
        initJarUpdateInfoPane();
        initUpdateActionPane();

        add(jarVersionInfoPane, BorderLayout.NORTH);

        //海外版本不显示更新信息
        if (GeneralContext.getLocale().equals(Locale.CHINA) || GeneralContext.getLocale().equals(Locale.TAIWAN)) {
            add(jarUpdateInfoPane, BorderLayout.CENTER);
            add(updateActionPane, BorderLayout.SOUTH);
        } else {
            add(updateActionPane, BorderLayout.CENTER);
        }

        addActionListenerForUpdateBtn();

        new SwingWorker<JSONObject, Void>() {
            @Override
            protected JSONObject doInBackground() throws Exception {
                return new JSONObject(HttpToolbox.get(CloudCenter.getInstance().acquireUrlByKind("jar10.update")));
            }

            @Override
            protected void done() {
                try {
                    downloadFileConfig = get();
                    showDownLoadInfo();
                    afterInit();
                } catch (InterruptedException e) {
                    stopLoading();
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    stopLoading();
                } finally {
                    getUpdateInfo(StringUtils.EMPTY).execute();
                }
            }
        }.execute();
    }

    private SwingWorker<JSONArray, Void> getUpdateInfo(final String keyword) {
        updateInfoList = new ArrayList<>();
        lastUpdateCacheTime = UpdateConstants.CHANGELOG_X_START;
        String cacheConfigPath = getUpdateCacheConfig();
        cacheProperty = new UpdateInfoCachePropertyManager(StableUtils.pathJoin(WorkContext.getCurrent().getPath(), "resources", "offlineres", cacheConfigPath));
        String recordUpdateTime = cacheProperty.readProperty("updateTime");
        if (StringUtils.isNotEmpty(recordUpdateTime)) {
            lastUpdateCacheTime = recordUpdateTime;
        }
        String recordUpdateState = cacheProperty.readProperty("updateState");
        if (StringUtils.isNotEmpty(recordUpdateState)) {
            lastUpdateCacheState = recordUpdateState;
        }
        return new SwingWorker<JSONArray, Void>() {
            @Override
            protected JSONArray doInBackground() {
                CloseableHttpClient httpClient;
                CloseableHttpResponse response;
                try {
                    getUpdateInfoSuccess = false;
                    //step1:read from cache file
                    getCachedUpdateInfo(keyword);
                    //step2:read from website,start from cacheRecordTime
                    if (downloadFileConfig == null) {
                        throw new Exception("network error.");
                    }
                    HttpGet get = new HttpGet(CloudCenter.getInstance().acquireUrlByKind("changelog10") + "&start=" + lastUpdateCacheTime + "&end=" + getLatestJARTimeStr());
                    httpClient = HttpToolbox.getHttpClient(CloudCenter.getInstance().acquireUrlByKind("changelog10") + "&start=" + lastUpdateCacheTime + "&end=" + getLatestJARTimeStr());
                    response = httpClient.execute(get);
                    String responseText = CommonIOUtils.inputStream2String(response.getEntity().getContent(),EncodeConstants.ENCODING_UTF_8).trim();
                    JSONArray array = JSONArray.create();
                    //假如返回"-1"，说明socket出错了
                    if (!ComparatorUtils.equals(responseText, "-1")) {
                        array = new JSONArray(responseText);
                    }
                    return array;
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage());
                }
                return JSONArray.create();
            }

            @Override
            protected void done() {
                try {
                    JSONArray jsonArray = get();
                    //step3:generateInfoTableList
                    updateInfoTable.getDataModel().populateBean(generateUpdateInfoList(jsonArray, keyword));

                    getUpdateInfoSuccess = true;
                    //step4:update cache file,start from cacheRecordTime,end latest server jartime
                    updateCachedInfoFile(jsonArray);
                } catch (Exception e) {
                    getUpdateInfoSuccess = true;
                    FineLoggerFactory.getLogger().error(e.getMessage());
                }
            }
        };
    }

    private void afterInit() {
        if (autoUpdateAfterInit) {
            updateButton.doClick();
        }
    }

    //从文件中读取缓存的更新信息
    private void getCachedUpdateInfo(String keyword) throws Exception {
        String cacheInfoPath = getUpdateCacheInfo();
        File cacheFile = new File(StableUtils.pathJoin(WorkContext.getCurrent().getPath(), "resources", "offlineres", cacheInfoPath));
        if (!ComparatorUtils.equals(lastUpdateCacheState, "success")) {
            cacheFile.delete();
            return;
        }
        if (cacheFile.exists()) {
            try (InputStreamReader streamReader = new InputStreamReader(new FileInputStream(cacheFile), StandardCharsets.UTF_8);
                 BufferedReader br = new BufferedReader(streamReader)) {
                String readStr, updateTimeStr;
                while ((readStr = br.readLine()) != null) {
                    String[] updateInfo = readStr.split("\\t");
                    if (updateInfo.length == 2) {
                        updateTimeStr = updateInfo[0];
                        Date updateTime = CHANGELOG_FORMAT.parse(updateTimeStr);
                        //形如 Build#release-2018.07.31.03.03.52.80
                        String currentNO = GeneralUtils.readBuildNO();
                        Date curJarDate = UPDATE_INFO_TABLE_FORMAT.parse(currentNO, new ParsePosition(currentNO.indexOf("-") + 1));
                        if (!ComparatorUtils.equals(keyword, StringUtils.EMPTY)) {
                            if (!containsKeyword(UPDATE_INFO_TABLE_FORMAT.format(updateTime), keyword) && !containsKeyword(updateInfo[1], keyword)) {
                                continue;
                            }
                        }
                        if (isValidLogInfo(updateInfo[1])) {
                            updateInfoList.add(new Object[]{UPDATE_INFO_TABLE_FORMAT.format(updateTime), updateInfo[1], updateTime.after(curJarDate)});
                        }
                    }
                }
            }
        }
    }

    private void updateCachedInfoFile(JSONArray jsonArray) throws Exception {
        String cacheDirPath = StableUtils.pathJoin(WorkContext.getCurrent().getPath(), "resources", "offlineres");
        File cacheFileDir = new File(cacheDirPath);
        if (!StableUtils.mkdirs(cacheFileDir)) {
            FineLoggerFactory.getLogger().error("make dir error.");
            return;
        }
        final File cacheFile = new File(StableUtils.pathJoin(cacheDirPath, getUpdateCacheInfo()));
        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
            lastUpdateCacheTime = UpdateConstants.CHANGELOG_X_START;
            lastUpdateCacheState = UPDATE_CACHE_STATE_FAIL;
        }
        if (downloadFileConfig == null) {
            return;
        }
        String endTime = getLatestJARTimeStr();
        if (endTime.equals(lastUpdateCacheTime) || jsonArray.length() == 0 || ComparatorUtils.compare(endTime, lastUpdateCacheTime) <= 0) {
            return;
        }
        try (OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(cacheFile), UTF_8)) {
            try (BufferedWriter bufferWriter = new BufferedWriter(writerStream)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = (JSONObject) jsonArray.get(i);
                    bufferWriter.write((String) jo.get("update") + '\t' + jo.get("title"));
                    bufferWriter.newLine();
                    bufferWriter.flush();
                }
            }
        }
        lastUpdateCacheState = UPDATE_CACHE_STATE_SUCCESS;
        lastUpdateCacheTime = endTime;
        cacheProperty.updateProperty("updateTime", lastUpdateCacheTime);
        cacheProperty.updateProperty("updateState", lastUpdateCacheState);
    }

    private ArrayList<Object[]> generateUpdateInfoList(JSONArray jsonArray, String keyword) throws Exception {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jo = (JSONObject) jsonArray.get(i);
            String updateTitle = (String) jo.get("title");
            String updateTimeStr = (String) jo.get("update");
            Date updateTime = CHANGELOG_FORMAT.parse(updateTimeStr);
            //形如 Build#release-2018.07.31.03.03.52.80
            String currentNO = GeneralUtils.readBuildNO();
            Date curJarDate = UPDATE_INFO_TABLE_FORMAT.parse(currentNO, new ParsePosition(currentNO.indexOf("-") + 1));
            if (curJarDate == null) {
                curJarDate = updateTime;
            }
            if (!ComparatorUtils.equals(keyword, StringUtils.EMPTY)) {
                if (!containsKeyword(UPDATE_INFO_TABLE_FORMAT.format(updateTime), keyword) && !containsKeyword(updateTitle, keyword)) {
                    continue;
                }
            }
            if (isValidLogInfo(updateTitle)) {
                updateInfoList.add(new Object[]{UPDATE_INFO_TABLE_FORMAT.format(updateTime), updateTitle, updateTime.after(curJarDate)});
            }
        }
        return new ArrayList<>(updateInfoList);
    }

    private boolean containsKeyword(String str, String keyword) {
        return str.toUpperCase().contains(keyword.toUpperCase());
    }

    private void stopLoading() {
        loadingLabel.stopLoading(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Connect_VersionUpdateServer_Failed"));
    }


    private void showDownLoadInfo() {
        //形如 Build#release-2018.07.31.03.03.52.80
        String buildNO = downloadFileConfig.optString("buildNO");
        Date jarDate = (new SimpleDateFormat("yyyy.MM.dd")).parse(buildNO, new ParsePosition(buildNO.indexOf("-") + 1));
        String serverVersionNO = downloadFileConfig.optString("versionNO");
        String currentVersionNO = ProductConstants.RELEASE_VERSION;
        String[] serverVersionSplitStr = serverVersionNO.split("\\.");
        String[] currentVersionSplitStr = currentVersionNO.split("\\.");
        int index = 0;
        int compareResult;
        int versionLength = Math.min(serverVersionSplitStr.length, currentVersionSplitStr.length);

        //形如 Build#release-2018.07.31.03.03.52.80
        String currentNO = GeneralUtils.readBuildNO();
        if (!".".equals(StableUtils.getInstallHome())) {
            Date currentDate = (new SimpleDateFormat("yyyy.MM.dd")).parse(currentNO, new ParsePosition(currentNO.indexOf("-") + 1));
            if (DateUtils.subtractDate(jarDate, currentDate, DateUtils.DAY) > 0) {
                updateButton.setEnabled(true);
                updateLabel.setVisible(true);
                loadingLabel.stopLoading(buildNO.contains("-") ? buildNO.substring(buildNO.lastIndexOf("-") + 1) : buildNO);
            } else {
                loadingLabel.stopLoading(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Already_Latest_Version"));
            }
        } else {
            updateButton.setEnabled(true);
            updateLabel.setVisible(true);
            loadingLabel.stopLoading(buildNO.contains("-") ? buildNO.substring(buildNO.lastIndexOf("-") + 1) : buildNO);
        }

        while (index < versionLength) {
            compareResult = serverVersionSplitStr[index].length() - currentVersionSplitStr[index].length();
            if (0 == compareResult) {
                compareResult = serverVersionSplitStr[index].compareTo(currentVersionSplitStr[index]);
                if (0 == compareResult) {
                    ++index;
                    continue;
                }
                break;
            }
            break;
        }
    }

    /**
     * jar包更新按钮监听器
     */
    private void addActionListenerForUpdateBtn() {
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backup();
                int a = JOptionPane.showConfirmDialog(getParent(), Toolkit.i18nText("Fine-Design_Update_Info_Information"),Toolkit.i18nText("Fine-Design_Update_Info_Title"), JOptionPane.OK_CANCEL_OPTION);
                if (a == 0) {
                    progressBar.setVisible(true);
                    progressBar.setString(Toolkit.i18nText("Fine-Design_Update_Info_Wait_Message"));
                    UpdateCallBack callBack = new UpdateProgressCallBack(progressBar);
                    updateButton.setEnabled(false);
                    updateLabel.setVisible(false);
                    new FileProcess(callBack) {
                        @Override
                        public void onDownloadSuccess() {
                            updateButton.setEnabled(true);
                            progressBar.setVisible(false);
                            RestartHelper.restart();
                        }
                        @Override
                        public void onDownloadFailed() {
                            progressBar.setVisible(false);
                            JOptionPane.showMessageDialog(getParent(),"Update Failed");
                            RestartHelper.restart();
                        }
                    }.execute();
                }
            }
        });
    }

    /**
     * Jar还原按钮兼容
     */
    private void backup() {
        String installHome = StableUtils.getInstallHome();
        //jar包备份文件的目录为"backup/"+jar包当前版本号
        String todayBackupDir = StableUtils.pathJoin(installHome, getBackupDirectory(), (GeneralUtils.readBuildNO()));
        backupFilesFromInstallEnv(installHome, todayBackupDir);
        backupFilesFromInstallLib(installHome, todayBackupDir);
        jarCurrentLabel.setText(downloadFileConfig.optString("buildNO"));
    }

    private void backupFilesFromInstallEnv(String installHome, String todayBackupDir) {
        for (String file : UpdateConstants.JARS_FOR_SERVER_X) {
            try {
                IOUtils.copy(
                        new File(StableUtils.pathJoin(installHome, UpdateConstants.APPS_FOLDER_NAME, ProductConstants.getAppFolderName(), ProjectConstants.WEBINF_NAME, ProjectConstants.LIB_NAME, file)),
                        new File(StableUtils.pathJoin(todayBackupDir)));
            } catch (IOException e) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        }
    }

    private void backupFilesFromInstallLib(String installHome, String todayBackupDir) {
        for (String file : UpdateConstants.JARS_FOR_DESIGNER_X) {
            try {
                IOUtils.copy(
                        new File(StableUtils.pathJoin(installHome, ProjectConstants.LIB_NAME, file)),
                        new File(StableUtils.pathJoin(todayBackupDir)));
            } catch (IOException e) {
                FineLoggerFactory.getLogger().error(e.getMessage());
            }
        }
    }

    //获取备份目录
    private String getBackupDirectory() {
        return UpdateConstants.DESIGNER_BACKUP_DIR;
    }

    //获取更新日志缓存配置文件名
    private String getUpdateCacheConfig() {
        return UpdateConstants.UPDATE_CACHE_CONFIG_X;
    }

    //获取更新日志缓存内容文件名
    private String getUpdateCacheInfo() {
        return UpdateConstants.UPDATE_CACHE_INFO_X;
    }

    //获取最新的jar包时间字符串
    private String getLatestJARTimeStr() {
        if (downloadFileConfig == null) {
            return StringUtils.EMPTY;
        }
        String buildNO = downloadFileConfig.optString("buildNO");
        Date jarDate = (new SimpleDateFormat("yyyy.MM.dd")).parse(buildNO, new ParsePosition(buildNO.indexOf("-") + 1));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(jarDate);
    }

    //判断是否是有效的日志内容
    private boolean isValidLogInfo(String logContent) {
        String log = logContent.toUpperCase();
        List<String> logType = UpdateConstants.LOG_TYPE;
        for (String s : logType) {
            if (log.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 显示窗口
     */
    public void showDialog() {
        setSize(DEFAULT);
        setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_UpdateAndUpgrade"));
        GUICoreUtils.centerWindow(this);
        setVisible(true);
    }

    /**
     * 检查有效性
     *
     */
    @Override
    public void checkValid() throws Exception {
    }
}