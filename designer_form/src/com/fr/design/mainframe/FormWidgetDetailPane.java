package com.fr.design.mainframe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.fr.base.BaseUtils;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.UIDialog;
import com.fr.design.extra.PluginWebBridge;
import com.fr.design.extra.ShopDialog;
import com.fr.design.extra.WebManagerPaneFactory;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIPreviewButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.form.share.ShareConstants;
import com.fr.form.share.ShareLoader;
import com.fr.form.ui.ElCaseBindInfo;
import com.fr.general.Inter;

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
    private static final int OFFSET_X = 140;
    private static final int OFFSET_Y = 26;

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
            elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
        }
        initReuWidgetPanel();
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
        downPanel = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList));
        reuWidgetPanel.add(downPanel);
    }

    /**
     * 初始化菜单栏面板
     */
    private void initMenuPanel() {
        JPanel menutPane = new JPanel();
        menutPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        menutPane.setBorder(BorderFactory.createEmptyBorder(5, 8, 3, 3));
        menutPane.add(new UILabel(Inter.getLocText("FR-Designer_LocalWidget"),
                SwingConstants.HORIZONTAL), BorderLayout.WEST);
        menutPane.add(downloadButton, BorderLayout.EAST);
        comboBox = new UIComboBox(getFormCategories());
        comboBox.setPreferredSize(new Dimension(240, 30));
        initComboBoxSelectedListener();
        menutPane.add(comboBox, BorderLayout.SOUTH);
        reuWidgetPanel.add(menutPane,BorderLayout.NORTH);
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
                refreshDownPanel();

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
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIPopupMenu menu = new UIPopupMenu();
                UIMenuItem downloadItem = new UIMenuItem(Inter.getLocText("FR-Designer_Download_Template"), BaseUtils.readIcon("/com/fr/design/form/images/download.png"));
                UIMenuItem installItem = new UIMenuItem(Inter.getLocText("FR-Designer_Download_Template"), BaseUtils.readIcon("/com/fr/design/form/images/download.png"));
                UIMenuItem deleteItem = new UIMenuItem(Inter.getLocText("FR-Designer_Download_Template"), BaseUtils.readIcon("/com/fr/design/form/images/download.png"));

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
                        fileChooser.setFileFilter(new FileNameExtensionFilter("zip", "zip"));
                        int returnValue = fileChooser.showDialog(new JLabel(), "选择");
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            final File chosenFile = fileChooser.getSelectedFile();
                            installFromDiskZipFile(chosenFile);

                        }
                    }
                });

                deleteItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                GUICoreUtils.showPopupMenu(menu, tabbedPane, tabbedPane.getX() + OFFSET_X, tabbedPane.getY() + OFFSET_Y);

            }
        });
    }

    private void installFromDiskZipFile(File chosenFile) {
    }

    /**
     * 获取报表块组件分类
     */
    public String[] getFormCategories() {
        return ShareConstants.WIDGET_CATEGORIES;
    }



    public void refreshDownPanel() {
        reuWidgetPanel.remove(downPanel);
        downPanel = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList));
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