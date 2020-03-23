package com.fr.design.mainframe.component.pane;

import com.fr.base.BaseUtils;
import com.fr.design.fun.impl.AbstractComponentLibraryPaneProcessor;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DockingView;
import com.fr.design.mainframe.ShareWidgetPane;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.form.share.ShareLoader;
import com.fr.form.ui.SharableWidgetBindInfo;
import com.fr.general.CloudCenter;
import com.fr.log.FineLoggerFactory;
import com.fr.share.ShareConstants;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * created by Harrison on 2020/03/16
 **/
public class ComponentLibraryPaneProcessorImpl extends AbstractComponentLibraryPaneProcessor {
    
    private static final int LOCAL_WIDGET_LABEL_WIDTH = 90;
    
    private DockingView parentView;
    
    private SwingWorker sw;
    
    private JPanel componentLibPane;
    private UIScrollPane showPane;
    
    private UIComboBox menuPanelComboBox;
    private JPanel menuPanelNorthPane;
    private SharableWidgetBindInfo[] bindInfoArray;
    
    private UIButton deleteButton;
    private JPanel editPanel;
    private JPanel resetPanel;
    /**
     * 组件面板是否可以编辑
     */
    private boolean isEdit;
    
    public ComponentLibraryPaneProcessorImpl() {
    
    }
    
    @Override
    public void parentView(DockingView dockingView) {
        
        parentView = dockingView;
        if (bindInfoArray == null) {
            if (sw != null) {
                sw.cancel(true);
            }
            sw = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    bindInfoArray = ShareLoader.getLoader().getAllBindInfoList();
                    refreshShowPanel(false);
                    return null;
                }
            };
            sw.execute();
        }
    }
    
    @Override
    public void parentPane(JPanel panel) {
        this.componentLibPane = panel;
    }
    
    @Override
    public void complete() {
    
    }
    
    /**
     * 初始化组件共享和复用面板
     */
    @Override
    public UIScrollPane createShowPanel(boolean isEdit) {
        
        showPane = new UIScrollPane(
                new ShareWidgetPane(bindInfoArray, isEdit)
        );
        showPane.setBorder(null);
        return showPane;
    }
    
    
    @Override
    public UIComboBox createMenuComBox() {
        
        menuPanelComboBox = new UIComboBox(getFormCategories());
        menuPanelComboBox.setPreferredSize(new Dimension(240, menuPanelComboBox.getPreferredSize().height));
        addComboBoxListener();
        return menuPanelComboBox;
    }
    
    private void addComboBoxListener() {
        
        menuPanelComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ShareLoader.getLoader().resetRemovedModuleList();
                int filterIndex = menuPanelComboBox.getSelectedIndex();
                if (filterIndex == 0) {
                    bindInfoArray = ShareLoader.getLoader().getAllBindInfoList();
                } else {
                    String filterName = menuPanelComboBox.getSelectedItem().toString();
                    bindInfoArray = ShareLoader.getLoader().getFilterBindInfoList(filterName);
                }
                refreshShowPanel(isEdit);
            }
        });
    }
    
    @Override
    public JPanel createMenuNorthPane() {
        
        menuPanelNorthPane = new JPanel(new BorderLayout());
        UILabel localWidgetLabel = FRWidgetFactory.createLineWrapLabel(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Local_Widget"),
                LOCAL_WIDGET_LABEL_WIDTH);
        menuPanelNorthPane.add(localWidgetLabel, BorderLayout.WEST);
        menuPanelNorthPane.add(initEditButtonPane(), BorderLayout.EAST);
        menuPanelNorthPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return menuPanelNorthPane;
        
    }
    
    /**
     * 创建菜单栏按钮面板
     */
    protected JPanel initEditButtonPane() {
        
        editPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    
        editPanel.add(createRefreshButton());
        editPanel.add(createDownloadButton());
        editPanel.add(createInstallButton());
        editPanel.add(createDeleteButton());
    
        return editPanel;
    }
    
    /**
     * 创建取消删除面板
     */
    protected JPanel initResetButtonPane() {
        
        resetPanel = new JPanel();
        UIButton resetButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Reset"));
        resetPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        resetButton.setBackground(Color.white);
        resetButton.setForeground(new Color(0x333334));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshShowPanel(false);
                replaceButtonPanel(false);
                componentLibPane.remove(deleteButton);
            }
        });
    
        deleteButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Remove_Item"));
        deleteButton.setBackground(Color.white);
        deleteButton.setForeground(new Color(0xeb1d1f));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ShareLoader.getLoader().removeModulesFromList()) {
                    refreshShareModule();
                    componentLibPane.remove(deleteButton);
                    bindInfoArray = ShareLoader.getLoader().getAllBindInfoList();
                    JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Share_Module_Removed_Successful"));
                    refreshShowPanel(false);
                    replaceButtonPanel(false);
                    refreshComboBoxData();
                } else {
                    JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Share_Module_Removed_Failed"));
                }
    
            }
        });
        JPanel deletePane = new JPanel(new BorderLayout());
        deletePane.add(deleteButton, BorderLayout.CENTER);
        deletePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    
        resetPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        resetPanel.add(resetButton, BorderLayout.CENTER);
        resetPanel.add(deletePane, BorderLayout.WEST);
    
        refreshShowPanel(true);
    
        return resetPanel;
    
    }
    
    
    /**
     * 创建工具条按钮
     */
    protected UIButton createToolButton(Icon icon, String toolTip, ActionListener actionListener) {
        
        UIButton toolButton = new UIButton();
        toolButton.setIcon(icon);
        toolButton.setToolTipText(toolTip);
        toolButton.set4ToolbarButton();
        toolButton.addActionListener(actionListener);
        return toolButton;
    
    }
    
    /**
     * 创建刷新按钮
     */
    protected UIButton createRefreshButton() {
        
        return createToolButton(
                BaseUtils.readIcon("/com/fr/design/form/images/refresh.png"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Refresh"),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (sw != null) {
                            sw.cancel(true);
                        }
                        sw = new SwingWorker() {
                            @Override
                            protected Object doInBackground() throws Exception {
                                ShareLoader.getLoader().refreshModule();
                                bindInfoArray = ShareLoader.getLoader().getAllBindInfoList();
                                refreshComboBoxData();
                                refreshShowPanel(false);
                                return null;
                            }
                        };
                        sw.execute();
                    }
                }
        );
    }
    
    /**
     * 创建下载模板的按钮
     */
    protected UIButton createDownloadButton() {
        UIButton downloadButton = new UIButton();
        downloadButton.setIcon(BaseUtils.readIcon("/com/fr/design/form/images/download icon.png"));
        downloadButton.set4ToolbarButton();
        downloadButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Download_Template"));
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = CloudCenter.getInstance().acquireUrlByKind("reuse.url");
                if (StringUtils.isEmpty(url)) {
                    FineLoggerFactory.getLogger().info("The URL is empty!");
                    return;
                }
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException exp) {
                    JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Set_Default_Browser_Duplicate"));
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                } catch (URISyntaxException exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                } catch (Exception exp) {
                    FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                    FineLoggerFactory.getLogger().error("Can not open the browser for URL:  " + url);
                }
            }
        });
        return downloadButton;
    }
    
    /**
     * 创建安装模板的按钮
     */
    protected UIButton createInstallButton() {
        return createToolButton(
                BaseUtils.readIcon("/com/fr/design/form/images/install icon.png"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Install_Template"),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        fileChooser.setFileFilter(new FileNameExtensionFilter(".reu", "reu"));
                        int returnValue = fileChooser.showDialog(new UILabel(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Select"));
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            final File chosenFile = fileChooser.getSelectedFile();
                            installFromDiskZipFile(chosenFile);
                        }
                    }
                }
        );
    }
    
    /**
     * 创建删除模板的按钮
     */
    protected UIButton createDeleteButton() {
        return createToolButton(
                BaseUtils.readIcon("/com/fr/design/form/images/delete icon.png"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Delete_Template"),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        replaceButtonPanel(true);
                    }
                }
        );
    }
    
    /**
     * 获取报表块组件分类
     */
    protected String[] getFormCategories() {
        
        return ArrayUtils.addAll(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_AllCategories")}, ShareLoader.getLoader().getModuleCategory());
    }
    
    protected void refreshShowPanel(boolean isEdit) {
        
        componentLibPane.remove(showPane);
        showPane = createShowPanel(isEdit);
        componentLibPane.add(showPane);
        repaint();
    }
    
    private void refreshComboBoxData() {
        
        menuPanelComboBox.setSelectedIndex(0);
        menuPanelComboBox.setModel(new DefaultComboBoxModel(getFormCategories()));
    }
    
    private void replaceButtonPanel(boolean isEdit) {
        
        this.isEdit = isEdit;
        if (isEdit) {
            menuPanelNorthPane.remove(editPanel);
            menuPanelNorthPane.add(initResetButtonPane(), BorderLayout.EAST);
        } else {
            menuPanelNorthPane.remove(resetPanel);
            menuPanelNorthPane.add(initEditButtonPane(), BorderLayout.EAST);
            ShareLoader.getLoader().resetRemovedModuleList();
        }
    }
    
    private void installFromDiskZipFile(File chosenFile) {
        
        if (chosenFile != null && chosenFile.getName().endsWith(ShareConstants.SUFFIX_MODULE)) {
            try {
                if (ShareLoader.getLoader().installModuleFromDiskZipFile(chosenFile)) {
                    refreshShareModule();
                    bindInfoArray = ShareLoader.getLoader().getAllBindInfoList();
                    refreshShowPanel(false);
                    refreshComboBoxData();
                    JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Share_Module_OK"));
                } else {
                    JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Share_Module_Error"));
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Share_Module_Error"));
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }
    
    private void refreshShareModule() {
        try {
            ShareLoader.getLoader().refreshModule();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
    
    public void repaint() {
        
        parentView.validate();
        parentView.repaint();
        parentView.revalidate();
    }
    
}
