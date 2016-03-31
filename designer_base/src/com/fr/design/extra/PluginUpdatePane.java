package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
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
public class PluginUpdatePane extends PluginAbstractLoadingViewPane<Plugin[], Void> {

    private PluginControlPane controlPane;
    private JLabel errorMsgLabel;
    private UITabbedPane tabbedPane;
    
    private static final int PERSENT = 100;

    public PluginUpdatePane(UITabbedPane tabbedPane) {
        super(tabbedPane);
        this.tabbedPane = tabbedPane;
    }

    /**
     * 更新pane
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
                return Inter.getLocText("FR-Designer_Plugin_Normal_Update");
            }



            @Override
            public String textForInstallFromDiskButton() {
                return Inter.getLocText("FR-Designer_Plugin_Normal_Update_From_Local");
            }

            @Override
            public JPanel centerPane() {
                controlPane = new PluginControlPane();
                final PluginStatusCheckCompletePane s = this;
                controlPane.addPluginSelectionListener(new PluginSelectListener() {
                    @Override
                    public void valueChanged(Plugin plugin) {
                        s.setInstallButtonEnable(true);
                    }
                });
                return controlPane;
            }
        };
    }

    /**
     * 出错pane
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
                return Inter.getLocText("FR-Designer_Plugin_Normal_Update");
            }



            @Override
            public String textForInstallFromDiskButton() {
                return Inter.getLocText("FR-Designer_Plugin_Normal_Update_From_Local");
            }

            @Override
            public JComponent centerPane() {
                return errorMsgLabel;
            }
        };
    }

    /**
     * 加载插件
     * @return 所有插件
     */
    public Plugin[] loadData() throws Exception {
        return PluginsReaderFromStore.readPluginsForUpdate();
    }

    /**
     * 加载成功处理
     * 
     * @param plugins 插件
     */
    public void loadOnSuccess(Plugin[] plugins) {
        controlPane.loadPlugins(plugins);
        tabbedPane.setTitleAt(1, Inter.getLocText("FR-Designer-Plugin_Update") + "(" + plugins.length + ")");
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
     * @return 略
     */
    @Override
    public String textForLoadingLabel() {
        return Inter.getLocText("FR-Designer-Plugin_Detecting_Update");
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
        if (StringUtils.isEmpty(DesignerEnvManager.getEnvManager().getBBSName())){
            LoginCheckContext.fireLoginCheckListener();
        }
        if (StringUtils.isNotEmpty(DesignerEnvManager.getEnvManager().getBBSName())){
            new SwingWorker<Void, Double>(){

                @Override
                protected Void doInBackground() throws Exception {
                    Plugin plugin = controlPane.getSelectedPlugin();
                    String id = null;
                    if (plugin != null) {
                        id = plugin.getId();
                    }
                    String username = DesignerEnvManager.getEnvManager().getBBSName();
                    String password = DesignerEnvManager.getEnvManager().getBBSPassword();
                    try {
                        PluginHelper.downloadPluginFile(id,username,password, new Process<Double>() {
                            @Override
                            public void process(Double integer) {
                                publish(integer);
                            }
                        });
                    } catch (Exception e) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    }
                    return null;
                }

                public void process(List<Double> list) {
                    pane.setProgress(list.get(list.size() - 1) * PERSENT);
                }

                public void done() {
                    //下载完成，开始执行安装
                    try {
                        get();
                        pane.didTaskFinished();
                        doUpdateFromFile(PluginHelper.getDownloadTempFile());
                    } catch (Exception e) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    }
                }
            }.execute();
        }

    }

    private void doUpdateFromFile(File chosenFile) {
        try {
            Plugin plugin = PluginHelper.readPlugin(chosenFile);
            if (plugin == null) {
                JOptionPane.showMessageDialog(PluginUpdatePane.this, Inter.getLocText("FR-Designer-Plugin_Illegal_Plugin_Zip"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            Plugin oldPlugin = PluginLoader.getLoader().getPluginById(plugin.getId());
            if (oldPlugin != null) {
                // 说明安装了同ID的插件，再比较两个插件的版本
                if (PluginHelper.isNewThan(plugin, oldPlugin)) {
                    // 说明是新的插件，删除老的然后安装新的
                    final String[] files = PluginHelper.uninstallPlugin(FRContext.getCurrentEnv(), oldPlugin);
                    PluginHelper.installPluginFromUnzippedTempDir(FRContext.getCurrentEnv(), plugin, new After() {
                        @Override
                        public void done() {
                            int rv = JOptionPane.showOptionDialog(
                                    PluginUpdatePane.this,
                                    Inter.getLocText("FR-Designer-Plugin_Update_Successful"),
                                    Inter.getLocText("FR-Designer-Plugin_Warning"),
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null,
                                    new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"),
                                            Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")
                                    },
                                    null
                            );

                            if (rv == JOptionPane.OK_OPTION) {
                                RestartHelper.restart();
                            }

                            // 如果不是立即重启，就把要删除的文件存放起来
                            if (rv == JOptionPane.CANCEL_OPTION || rv == JOptionPane.CLOSED_OPTION) {
                                RestartHelper.saveFilesWhichToDelete(files);
                            }
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(PluginUpdatePane.this, Inter.getLocText("FR-Designer-Plugin_Version_Is_Lower_Than_Current"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(PluginUpdatePane.this, Inter.getLocText("FR-Designer-Plugin_Cannot_Update_Not_Install"), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(PluginUpdatePane.this, e1.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 从磁盘安装按钮的提示
     * @return 按钮标题字符串
     */
    @Override
    public String textForInstallFromDiskFileButton() {
        return Inter.getLocText("FR-Designer_Plugin_Normal_Update_From_Local");
    }
    @Override
    protected String title4PopupWindow() {
        return "Update";
    }
}