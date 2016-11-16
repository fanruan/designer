package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.PluginWebBridge;
import com.fr.design.extra.ShopDialog;
import com.fr.design.extra.WebManagerPaneFactory;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.share.ShareConstants;
import com.fr.form.share.ShareLoader;
import com.fr.form.ui.ElCaseBindInfo;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-8
 * Time: 下午8:18
 */
public class FormWidgetDetailPane extends FormDockView{

    private UITabbedPane tabbedPane;
    private UIScrollPane downPane;
    private JPanel reuWidgetPanel;
    private UIComboBox comboBox;
    private ElCaseBindInfo[] elCaseBindInfoList;
    private UIButton deleteButton;
    private UIButton resetButton;
    private JPanel editPanel;
    private JPanel resetPanel;
    private JPanel menutPanel;
    private static final int OFFSET_X = 140;
    private static final int OFFSET_Y = 26;
    private SwingWorker sw;

    public static FormWidgetDetailPane getInstance() {
        if (HOLDER.singleton == null) {
            HOLDER.singleton = new FormWidgetDetailPane();
        }
        return HOLDER.singleton;
    }

    private  FormWidgetDetailPane(){
        setLayout(FRGUIPaneFactory.createBorderLayout());
    }


    public static FormWidgetDetailPane getInstance(FormDesigner formEditor) {
        HOLDER.singleton.setEditingFormDesigner(formEditor);
        HOLDER.singleton.refreshDockingView();
        return HOLDER.singleton;
    }

    private static class HOLDER {
        private static FormWidgetDetailPane singleton = new FormWidgetDetailPane();
    }

    public String getViewTitle() {
        return Inter.getLocText("FR-Widget_Tree_And_Table");
    }

    @Override
    public Icon getViewIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/m_report/attributes.png");
    }

    /**
     * 初始化
     */
    public void refreshDockingView(){
        FormDesigner designer = this.getEditingFormDesigner();
        removeAll();
        if(designer == null){
            clearDockingView();
            return;
        }
        reuWidgetPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        reuWidgetPanel.setBorder(null);
        if (elCaseBindInfoList == null) {
            elCaseBindInfoList = new ElCaseBindInfo[0];
            if (sw != null) {
                sw.cancel(true);
            }
            sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                    refreshDownPanel(false);
                    return null;
                }
            };
            sw.execute();
        }
        initReuWidgetPanel();
        initMenuPanel();
        tabbedPane = new UITabbedPane();
        tabbedPane.setOpaque(true);
        tabbedPane.setBorder(null);
        tabbedPane.setTabPlacement(SwingConstants.BOTTOM);
        tabbedPane.addTab(Inter.getLocText("FR-Engine_Report"), reuWidgetPanel);
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Form-ToolBar_Chart"), new JPanel());
        add(tabbedPane, BorderLayout.CENTER);

    }

    /**
     * 初始化组件共享和复用面板
     */
    private void initReuWidgetPanel() {
        downPane = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList, false));
        reuWidgetPanel.add(downPane);
    }

    /**
     * 初始化菜单栏面板
     */
    private void initMenuPanel() {
        menutPanel = new JPanel();
        menutPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        menutPanel.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 3));
        menutPanel.setPreferredSize(new Dimension(240, 48));
        menutPanel.add(new UILabel(Inter.getLocText("FR-Designer_LocalWidget"),
                SwingConstants.HORIZONTAL), BorderLayout.WEST);

        menutPanel.add(initEditButtonPane(), BorderLayout.EAST);
        menutPanel.add(new JPanel(), BorderLayout.CENTER);
        comboBox = new UIComboBox(getFormCategories());
        comboBox.setPreferredSize(new Dimension(240, 30));
        initComboBoxSelectedListener();
        menutPanel.add(comboBox, BorderLayout.SOUTH);
        reuWidgetPanel.add(menutPanel, BorderLayout.NORTH);

    }

    /**
     * 创建菜单栏按钮面板
     */
    private JPanel initEditButtonPane() {
        editPanel = new JPanel();
        editPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        editPanel.add(createRefreshButton(), BorderLayout.WEST);
        editPanel.add(createDownloadButton(), BorderLayout.EAST);
        return editPanel;
    }

    /**
     * 创建取消删除面板
     */
    private JPanel initResetButtonPane() {
        resetPanel = new JPanel();
        resetButton = new UIButton(Inter.getLocText("FR-Designer_Reset"));
        resetPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        resetButton.set4ToolbarButton();
        resetButton.setOpaque(true);
        resetButton.setBackground(new Color(184, 220, 242));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshDownPanel(false);
                replaceButtonPanel(false);
                reuWidgetPanel.remove(deleteButton);
            }
        });
        resetPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        resetPanel.add(resetButton, BorderLayout.CENTER);
        return resetPanel;

    }


    private void initComboBoxSelectedListener() {
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int filterIndex = comboBox.getSelectedIndex();
                if (filterIndex == 0) {
                    elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                } else {
                    String filterName = comboBox.getSelectedItem().toString();
                    elCaseBindInfoList = ShareLoader.getLoader().getFilterBindInfoList(filterName);
                }
                refreshDownPanel(false);

            }
        });
    }

    /**
     * 创建刷新按钮
     */
    private UIButton createRefreshButton() {
        UIButton refreshButton = new UIButton();
        refreshButton.setIcon(BaseUtils.readIcon("/com/fr/design/form/images/refresh.png"));
        refreshButton.setToolTipText(Inter.getLocText("FR-Designer_Refresh"));
        refreshButton.set4ToolbarButton();
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sw != null) {
                    sw.cancel(true);
                }
                sw = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        ShareLoader.getLoader().refreshModule();
                        elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                        comboBox.setSelectedIndex(0);
                        refreshDownPanel(false);
                        return null;
                    }
                };
                sw.execute();
            }
        });
        return refreshButton;

    }

    /**
     * 创建下载模板的按钮
     */
    private UIButton createDownloadButton() {
        UIButton downloadButton = new UIButton();
        downloadButton.setIcon(BaseUtils.readIcon("/com/fr/design/form/images/showmenu.png"));
        downloadButton.set4ToolbarButton();
        downloadButton.setToolTipText(Inter.getLocText("FR-Designer_Download_Template"));
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIPopupMenu menu = new UIPopupMenu();
                UIMenuItem downloadItem = new UIMenuItem(Inter.getLocText("FR-Designer_Download_Template"), BaseUtils.readIcon("/com/fr/design/form/images/download icon.png"));
                UIMenuItem installItem = new UIMenuItem(Inter.getLocText("FR-Designer_Install_Template"), BaseUtils.readIcon("/com/fr/design/form/images/install icon.png"));
                UIMenuItem deleteItem = new UIMenuItem(Inter.getLocText("FR-Designer_Delete_Template"), BaseUtils.readIcon("/com/fr/design/form/images/delete icon.png"));
                menu.add(downloadItem);
                menu.add(installItem);
                menu.add(deleteItem);
                downloadItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String url = SiteCenter.getInstance().acquireUrlByKind("reuse.url");
                        if (StringUtils.isEmpty(url)) {
                            FRContext.getLogger().info("The URL is empty!");
                            return;
                        }
                        try {
                            Desktop.getDesktop().browse(new URI(url));
                        } catch (IOException exp) {
                            JOptionPane.showMessageDialog(null, Inter.getLocText("Set_default_browser"));
                            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
                        } catch (URISyntaxException exp) {
                            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
                        } catch (Exception exp) {
                            FRContext.getLogger().errorWithServerLevel(exp.getMessage(), exp);
                            FRContext.getLogger().error("Can not open the browser for URL:  " + url);
                        }

                    }
                });

                installItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        fileChooser.setFileFilter(new FileNameExtensionFilter(".reu", "reu"));
                        int returnValue = fileChooser.showDialog(new UILabel(), Inter.getLocText("FR-Designer_Select"));
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            final File chosenFile = fileChooser.getSelectedFile();
                            installFromDiskZipFile(chosenFile);

                        }
                    }
                });

                deleteItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        replaceButtonPanel(true);
                        deleteFromDiskZipFile();
                    }
                });

                GUICoreUtils.showPopupMenu(menu, tabbedPane, tabbedPane.getX() + OFFSET_X, OFFSET_Y);

            }
        });
        return downloadButton;
    }

    private void deleteFromDiskZipFile() {
        deleteButton = new UIButton(Inter.getLocText("FR-Designer-CommitTab_Remove"));

        deleteButton.setBackground(Color.red);
        deleteButton.repaint();
        deleteButton.setPreferredSize(new Dimension(240, 40));
        reuWidgetPanel.add(deleteButton, BorderLayout.SOUTH);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ShareLoader.getLoader().removeModulesFromList()) {
                    refreshShareMoudule();
                    reuWidgetPanel.remove(deleteButton);
                    elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Share_Module_Removed_Successful"));
                    refreshDownPanel(false);
                    replaceButtonPanel(false);
                } else {
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Share_Module_Removed_Failed"));
                }

            }
        });
        refreshDownPanel(true);

    }

    private void replaceButtonPanel(boolean isEdit) {
        if (isEdit) {
            menutPanel.remove(editPanel);
            menutPanel.add(initResetButtonPane(), BorderLayout.EAST);
        } else {
            menutPanel.remove(resetPanel);
            menutPanel.add(initEditButtonPane(), BorderLayout.EAST);
            ShareLoader.getLoader().resetRemovedModuleList();
        }


    }

    private void installFromDiskZipFile(File chosenFile) {
        try {
            ShareLoader.getLoader().installModuleFromDiskZipFile(chosenFile);
            refreshShareMoudule();
            elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
            refreshDownPanel(false);
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Share_Module_OK"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Share_Module_Error"));
            FRLogger.getLogger().error(e.getMessage(), e);
        }

    }

    private void refreshShareMoudule() {
        try {
            ShareLoader.getLoader().refreshModule();
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 获取报表块组件分类
     */
    public String[] getFormCategories() {
        return ArrayUtils.addAll(new String[] {Inter.getLocText("FR-Designer_AllCategories")}, ShareLoader.getLoader().getModuleCategory());
    }



    public void refreshDownPanel(boolean isEdit) {
        reuWidgetPanel.remove(downPane);
        downPane = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList, isEdit));
        reuWidgetPanel.add(downPane);
        repaintContainer();

    }

    public void repaintContainer() {
        validate();
        repaint();
        revalidate();
    }


    public void setSelectedIndex(int index){
        tabbedPane.setSelectedIndex(index);
    }


    /**
     * 清除数据
     */
    public void clearDockingView() {
        JScrollPane psp = new JScrollPane();
        psp.setBorder(null);
        this.add(psp, BorderLayout.CENTER);
    }



    /**
     * 定位
     * @return  位置
     */
    public Location preferredLocation() {
        return Location.WEST_BELOW;
    }


}