package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
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
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-7-8
 * Time: 下午8:18
 */
public class FormWidgetDetailPane extends FormDockView{

    private UITabbedPane tabbedPane;
    private UIScrollPane downPanel;
    private JPanel reuWidgetPanel;
    private UIComboBox comboBox;
    private ElCaseBindInfo[] elCaseBindInfoList;
    private UIButton downloadButton;
    private UIButton refreshButton;
    private UIButton deleteButton;
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
        createRefreshButton();
        createDownloadButton();
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
        downPanel = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList, false));
        reuWidgetPanel.add(downPanel);
    }

    /**
     * 初始化菜单栏面板
     */
    private void initMenuPanel() {
        JPanel menutPane = new JPanel();
        menutPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        menutPane.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 3));
        menutPane.setPreferredSize(new Dimension(240, 48));
        menutPane.add(new UILabel(Inter.getLocText("FR-Designer_LocalWidget"),
                SwingConstants.HORIZONTAL), BorderLayout.WEST);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(FRGUIPaneFactory.createBorderLayout());
        buttonPanel.add(refreshButton, BorderLayout.WEST);
        buttonPanel.add(downloadButton, BorderLayout.EAST);
        menutPane.add(buttonPanel, BorderLayout.EAST);
        menutPane.add(new JPanel(), BorderLayout.CENTER);
        comboBox = new UIComboBox(getFormCategories());
        comboBox.setPreferredSize(new Dimension(240, 30));
        initComboBoxSelectedListener();
        menutPane.add(comboBox, BorderLayout.SOUTH);
        reuWidgetPanel.add(menutPane, BorderLayout.NORTH);

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

    private void createRefreshButton() {
        refreshButton = new UIButton();
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
                        refreshDownPanel(false);
                        return null;
                    }
                };
                sw.execute();
            }
        });

    }

    /**
     * 创建下载模板的按钮
     */
    private void createDownloadButton() {
        downloadButton = new UIButton();
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
                        BasicPane managerPane = new WebManagerPaneFactory().createReusePane();
                        UIDialog dlg = new ShopDialog(DesignerContext.getDesignerFrame(), managerPane);
                        PluginWebBridge.getHelper().setDialogHandle(dlg);
                        dlg.setVisible(true);
                    }
                });

                installItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                        fileChooser.setFileFilter(new FileNameExtensionFilter(".reu", "reu"));
                        int returnValue = fileChooser.showDialog(new JLabel(), Inter.getLocText("FR-Designer_Select"));
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            final File chosenFile = fileChooser.getSelectedFile();
                            installFromDiskZipFile(chosenFile);

                        }
                    }
                });

                deleteItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        deleteFromDiskZipFile();
                    }
                });

                GUICoreUtils.showPopupMenu(menu, tabbedPane, tabbedPane.getX() + OFFSET_X, OFFSET_Y);

            }
        });
    }

    private void deleteFromDiskZipFile() {
        deleteButton = new UIButton(Inter.getLocText("FR-Designer-CommitTab_Remove"));
        deleteButton.setOpaque(true);
        deleteButton.setBackground(Color.red);
        deleteButton.setPreferredSize(new Dimension(240, 40));
        reuWidgetPanel.add(deleteButton, BorderLayout.SOUTH);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ShareLoader.getLoader().removeModulesFromList()) {
                    refreshShareMoudule();
                    reuWidgetPanel.remove(deleteButton);
                    elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Share_Module_Removed_Successful"));
                    refreshDownPanel(false);
                } else {
                    reuWidgetPanel.remove(deleteButton);
                    JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Share_Module_Removed_Failed"));
                    refreshDownPanel(false);
                }

            }
        });
        refreshDownPanel(true);

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
            e.printStackTrace();
        }

    }

    private void refreshShareMoudule() {
        try {
            ShareLoader.getLoader().refreshModule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取报表块组件分类
     */
    public String[] getFormCategories() {
        return ShareConstants.WIDGET_CATEGORIES;
    }



    public void refreshDownPanel(boolean isEdit) {
        reuWidgetPanel.remove(downPanel);
        elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
        downPanel = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList, isEdit));
        reuWidgetPanel.add(downPanel);
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