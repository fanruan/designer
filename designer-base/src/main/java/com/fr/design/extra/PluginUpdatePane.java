package com.fr.design.extra;

import com.fr.config.MarketConfig;
import com.fr.design.extra.tradition.callback.UpdateOnlineCallback;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.json.JSONObject;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginMarker;
import com.fr.plugin.manage.PluginManager;
import com.fr.plugin.manage.control.PluginTaskResult;
import com.fr.plugin.manage.control.ProgressCallback;
import com.fr.plugin.view.PluginView;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginUpdatePane extends PluginAbstractLoadingViewPane<List<PluginView>, Void> {

    private static final int PERSENT = 100;
    private PluginControlPane controlPane;
    private JLabel errorMsgLabel;
    private UITabbedPane tabbedPane;

    public PluginUpdatePane(UITabbedPane tabbedPane) {
        super(tabbedPane);
        this.tabbedPane = tabbedPane;
    }

    /**
     * 更新pane
     *
     * @return 同上
     */
    public JPanel createSuccessPane() {
        return new PluginStatusCheckCompletePane() {

            @Override
            public void pressInstallButton() {
                doUpdateOnline(this);
            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Normal_Update");
            }


            @Override
            public String textForInstallFromDiskButton() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Normal_Update_From_Local");
            }

            @Override
            public JPanel centerPane() {
                controlPane = new PluginControlPane();
                final PluginStatusCheckCompletePane s = this;
                controlPane.addPluginSelectionListener(new PluginSelectListener() {
                    @Override
                    public void valueChanged(PluginView plugin) {
                        s.setInstallButtonEnable(true);
                    }
                });
                return controlPane;
            }
        };
    }

    /**
     * 出错pane
     *
     * @return 同上
     */
    @Override
    public JPanel createErrorPane() {
        errorMsgLabel = new UILabel();
        errorMsgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return new PluginStatusCheckCompletePane() {

            @Override
            public void pressInstallButton() {
                doUpdateOnline(this);
            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Normal_Update");
            }


            @Override
            public String textForInstallFromDiskButton() {
                return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Normal_Update_From_Local");
            }

            @Override
            public JComponent centerPane() {
                return errorMsgLabel;
            }
        };
    }

    /**
     * 加载插件
     *
     * @return 所有插件
     */
    public List<PluginView> loadData() throws Exception {
        return PluginsReaderFromStore.readPluginsForUpdate();
    }

    /**
     * 加载成功处理
     *
     * @param plugins 插件
     */
    public void loadOnSuccess(List<PluginView> plugins) {
        controlPane.loadPlugins(plugins);
        tabbedPane.setTitleAt(1, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Update") + "(" + plugins.size() + ")");
    }

    /**
     * 加载失败处理
     *
     * @param e 异常
     */
    public void loadOnFailed(Exception e) {
        errorMsgLabel.setText(e.getCause().getMessage());
    }

    /**
     * 略
     *
     * @return 略
     */
    @Override
    public String textForLoadingLabel() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Detecting_Update");
    }

    protected void installFromDiskFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("zip", "zip"));
        int returnValue = fileChooser.showOpenDialog(PluginUpdatePane.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            final File chosenFile = fileChooser.getSelectedFile();
            doUpdateFromFile(chosenFile);
        }
    }

    private void doUpdateOnline(final PluginStatusCheckCompletePane pane) {
        if (!StringUtils.isNotEmpty(MarketConfig.getInstance().getBbsUsername())) {
            LoginCheckContext.fireLoginCheckListener();
        }
        if (StringUtils.isNotEmpty(MarketConfig.getInstance().getBbsUsername())) {
            try {
                PluginView plugin = controlPane.getSelectedPlugin();
                PluginMarker pluginMarker = PluginMarker.create(plugin.getID(), plugin.getVersion());
                JSONObject latestPluginInfo = PluginUtils.getLatestPluginInfo(pluginMarker.getPluginID());
                String latestPluginVersion = (String) latestPluginInfo.get("version");
                PluginMarker toPluginMarker = PluginMarker.create(pluginMarker.getPluginID(), latestPluginVersion);
                PluginManager.getController().download(pluginMarker, new UpdateOnlineCallback(pluginMarker, toPluginMarker, pane));
            } catch (Exception e) {
            }

        }

    }

    private void doUpdateFromFile(File chosenFile) {
        PluginManager.getController().update(chosenFile, new ProgressCallback() {
            @Override
            public void updateProgress(String description, double progress) {
                // do nothing
            }

            @Override
            public void done(PluginTaskResult result) {
                if (result.isSuccess()) {
                    FineLoggerFactory.getLogger().info(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Plugin_Update_Success"));
                    JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Install_Successful"));
                } else {
                    JOptionPane.showMessageDialog(null, PluginUtils.getMessageByErrorCode(result.errorCode()), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * 从磁盘安装按钮的提示
     *
     * @return 按钮标题字符串
     */
    @Override
    public String textForInstallFromDiskFileButton() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Plugin_Normal_Update_From_Local");
    }

    @Override
    protected String title4PopupWindow() {
        return "Update";
    }
}
