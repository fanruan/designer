package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ifilechooser.FRFileChooserFactory;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.form.share.SharableWidgetProvider;
import com.fr.form.share.ShareLoader;
import com.fr.general.CloudCenter;
import com.fr.log.FineLoggerFactory;
import com.fr.share.ShareConstants;
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
    private static final int LOCAL_WIDGET_LABEL_WIDTH = 90;

    private UIScrollPane downPane;
    private JPanel reuWidgetPanel;
    private UIComboBox comboBox;
    private SharableWidgetProvider[] elCaseBindInfoList;
    private UIButton deleteButton;
    private JPanel editPanel;
    private JPanel resetPanel;
    private JPanel menutPanelNorthPane;
    private SwingWorker sw;
    //组件面板是否可以编辑
    private boolean isEdit;

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
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Tree_And_Table");
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

        add(reuWidgetPanel, BorderLayout.CENTER);

    }

    /**
     * 初始化组件共享和复用面板
     */
    private void initReuWidgetPanel() {
        elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
        downPane = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList, false));
        downPane.setBorder(null);
        reuWidgetPanel.add(downPane);
    }

    /**
     * 初始化菜单栏面板
     */
    private void initMenuPanel() {
        JPanel menutPanel = new JPanel();
        menutPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        menutPanel.setBorder(BorderFactory.createEmptyBorder(3, 10, 10, 15));

        menutPanelNorthPane = new JPanel(new BorderLayout());
        UILabel localWidgetLabel = FRWidgetFactory.createLineWrapLabel(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Local_Widget"),
                LOCAL_WIDGET_LABEL_WIDTH);
        menutPanelNorthPane.add(localWidgetLabel, BorderLayout.WEST);
        menutPanelNorthPane.add(initEditButtonPane(), BorderLayout.EAST);
        menutPanelNorthPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        menutPanel.add(menutPanelNorthPane, BorderLayout.NORTH);
        comboBox = new UIComboBox(getFormCategories());
        comboBox.setPreferredSize(new Dimension(240, comboBox.getPreferredSize().height));
        initComboBoxSelectedListener();
        menutPanel.add(comboBox, BorderLayout.CENTER);
        reuWidgetPanel.add(menutPanel, BorderLayout.NORTH);

    }

    /**
     * 创建菜单栏按钮面板
     */
    private JPanel initEditButtonPane() {
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
    private JPanel initResetButtonPane() {
        resetPanel = new JPanel();
        UIButton resetButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Reset"));
        resetPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        resetButton.setBackground(Color.white);
        resetButton.setForeground(new Color(0x333334));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshDownPanel(false);
                replaceButtonPanel(false);
                reuWidgetPanel.remove(deleteButton);
            }
        });

        deleteButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Remove_Item"));
        deleteButton.setBackground(Color.white);
        deleteButton.setForeground(new Color(0xeb1d1f));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ShareLoader.getLoader().removeModulesFromList()) {
                    refreshShareMoudule();
                    reuWidgetPanel.remove(deleteButton);
                    elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                    JOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Share_Module_Removed_Successful"));
                    refreshDownPanel(false);
                    replaceButtonPanel(false);
                    refreshComboxData();
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

        refreshDownPanel(true);

        return resetPanel;

    }


    private void initComboBoxSelectedListener() {
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ShareLoader.getLoader().resetRemovedModuleList();
                int filterIndex = comboBox.getSelectedIndex();
                if (filterIndex == 0) {
                    elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                } else {
                    String filterName = comboBox.getSelectedItem().toString();
                    elCaseBindInfoList = ShareLoader.getLoader().getFilterBindInfoList(filterName);
                }
                refreshDownPanel(isEdit);

            }
        });
    }

    /**
     * 创建工具条按钮
     */
    private UIButton createToolButton(Icon icon, String toolTip, ActionListener actionListener) {
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
    private UIButton createRefreshButton() {
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
                                elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                                refreshComboxData();
                                refreshDownPanel(false);
                                return null;
                            }
                        };
                        sw.execute();
                    }
                }
        );
    }

    private void refreshComboxData() {
        comboBox.setSelectedIndex(0);
        comboBox.setModel(new DefaultComboBoxModel(getFormCategories()));
    }

    /**
     * 创建下载模板的按钮
     */
    private UIButton createDownloadButton() {
        UIButton downloadButton = new UIButton();
        downloadButton.setIcon(BaseUtils.readIcon("/com/fr/design/form/images/download icon.png"));
        downloadButton.set4ToolbarButton();
        downloadButton.setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Download_Template"));
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = CloudCenter.getInstance().acquireUrlByKind("reuse.url");
                if (StringUtils.isEmpty(url)) {
                    FRContext.getLogger().info("The URL is empty!");
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
    private UIButton createInstallButton() {
        return createToolButton(
                BaseUtils.readIcon("/com/fr/design/form/images/install icon.png"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Install_Template"),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = FRFileChooserFactory.createFileChooser();
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
    private UIButton createDeleteButton() {
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

    private void replaceButtonPanel(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            menutPanelNorthPane.remove(editPanel);
            menutPanelNorthPane.add(initResetButtonPane(), BorderLayout.EAST);
        } else {
            menutPanelNorthPane.remove(resetPanel);
            menutPanelNorthPane.add(initEditButtonPane(), BorderLayout.EAST);
            ShareLoader.getLoader().resetRemovedModuleList();
        }
    }

    private void installFromDiskZipFile(File chosenFile) {
        if (chosenFile != null && chosenFile.getName().endsWith(ShareConstants.SUFFIX_MODULE)) {
            try {
                if (ShareLoader.getLoader().installModuleFromDiskZipFile(chosenFile)) {
                    refreshShareMoudule();
                    elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                    refreshDownPanel(false);
                    refreshComboxData();
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

    private void refreshShareMoudule() {
        try {
            ShareLoader.getLoader().refreshModule();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 获取报表块组件分类
     */
    public String[] getFormCategories() {
        return ArrayUtils.addAll(new String[] {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_AllCategories")}, ShareLoader.getLoader().getModuleCategory());
    }

    public void refreshDownPanel(boolean isEdit) {
        reuWidgetPanel.remove(downPane);
        downPane = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList, isEdit));
        downPane.setBorder(null);
        reuWidgetPanel.add(downPane);
        repaintContainer();

    }

    public void repaintContainer() {
        validate();
        repaint();
        revalidate();
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
