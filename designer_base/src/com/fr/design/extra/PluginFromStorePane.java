package com.fr.design.extra;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;
import com.fr.plugin.Plugin;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginFromStorePane extends PluginAbstractLoadingViewPane<Plugin[], Void> {
    private UILabel errorMsgLabel;
    private UITabbedPane tabbedPane;
    private PluginControlPane controlPane;

    private static final int LISTNUM1 = 1;
    private static final int LISTNUM100 = 100;



    public PluginFromStorePane(final UITabbedPane tabbedPane) {
        super(tabbedPane);
        this.tabbedPane = tabbedPane;
    }

    /**
     * 创建成功页面
     * @return 创建的页面对象
     */
    public JPanel createSuccessPane() {
        return new PluginStatusCheckCompletePane(){


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
                return Inter.getLocText("FR-Designer-Plugin_Install");
            }


            @Override
            public String textForInstallFromDiskButton() {
                return Inter.getLocText("FR-Designer-Plugin_Install_From_Local");
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
     * 创建错误页面
     * @return 创建的页面对象
     */
    @Override
    public JPanel createErrorPane() {
        errorMsgLabel = new UILabel();
        errorMsgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return new PluginStatusCheckCompletePane(){

            @Override
            public void pressInstallButton() {

            }

            @Override
            public void pressInstallFromDiskButton() {
                installFromDiskFile();
            }

            @Override
            public String textForInstallButton() {
                return Inter.getLocText("FR-Designer-Plugin_Install");
            }


            @Override
            public String textForInstallFromDiskButton() {
                return Inter.getLocText("FR-Designer-Plugin_Install_From_Local");
            }

            @Override
            public JComponent centerPane() {
                return errorMsgLabel;
            }
        };
    }

    /**
     * 加载数据
     * @return 插件
     */
    public Plugin[] loadData() throws Exception {
        //Thread.sleep(3000);
        return PluginsReaderFromStore.readPlugins();
    }

    /**
     * 加载成功处理
     * @param plugins 插件
     */
    public void loadOnSuccess(Plugin[] plugins) {
        controlPane.loadPlugins(plugins);
        tabbedPane.setTitleAt(2, Inter.getLocText("FR-Designer-Plugin_All_Plugins") + "(" + plugins.length + ")");
    }

    /**
     * 加载失败
     * @param e 异常消息
     */
    public void loadOnFailed(Exception e) {
        errorMsgLabel.setText(e.getCause().getMessage());
    }

    protected void installFromDiskFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("zip", "zip"));
        int returnValue = fileChooser.showOpenDialog(PluginFromStorePane.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            final File chosenFile = fileChooser.getSelectedFile();
            installFromDiskZipFile(chosenFile);

        }
    }


    private void installFromDiskZipFile(File chosenFile) {
        try {
            PluginHelper.installPluginFromDisk(chosenFile, new After() {
                @Override
                public void done() {
                    int rv = JOptionPane.showOptionDialog(
                            PluginFromStorePane.this,
                            Inter.getLocText("FR-Designer-Plugin_Install_Successful"),
                            Inter.getLocText("FR-Designer-Plugin_Warning"),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            new String[]{Inter.getLocText("FR-Designer-Basic_Restart_Designer"), Inter.getLocText("FR-Designer-Basic_Restart_Designer_Later")},
                            null
                    );
                    if (rv == JOptionPane.OK_OPTION) {
                        RestartHelper.restart();
                    }
                }
            });

        } catch (Exception e1) {
            JOptionPane.showMessageDialog(PluginFromStorePane.this, e1.getMessage(), Inter.getLocText("FR-Designer-Plugin_Warning"), JOptionPane.ERROR_MESSAGE);
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
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    }
                    return null;
                }

                public void process(List<Double> list) {
                    pane.setProgress(list.get(list.size() - LISTNUM1) * LISTNUM100);
                }

                public void done() {
                    //下载完成，开始执行安装
                    try {
                        get();
                        pane.didTaskFinished();
                        installFromDiskZipFile(PluginHelper.getDownloadTempFile());
                    } catch (InterruptedException e) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    } catch (ExecutionException e) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    } catch (Exception e) {
                        FRContext.getLogger().error(e.getMessage(), e);
                    }
                }
            }.execute();
        }

    }

    /**
     * 正在加载页的标题
     * @return 标题字符串
     */
    public String textForLoadingLabel() {
        return Inter.getLocText("FR-Designer-Plugin_Load_Plugins_From_Server");
    }

    /**
     * 从磁盘安装按钮的提示
     * @return 按钮标题字符串
     */
    @Override
    public String textForInstallFromDiskFileButton() {
        return  Inter.getLocText("FR-Designer-Plugin_Install_From_Local");
    }

    @Override
    protected String title4PopupWindow() {
        return "All";
    }
}