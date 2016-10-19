package com.fr.design.mainframe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

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

        JPanel esp = FRGUIPaneFactory.createBorderLayout_S_Pane();
        esp.setBorder(null);
        if (elCaseBindInfoList == null) {
            elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
        }
        initReuWidgetPanel();
        esp.add(reuWidgetPanel, BorderLayout.CENTER);
        createDownloadButton();
        JPanel widgetPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        widgetPane.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 3));
        widgetPane.add(new UILabel(Inter.getLocText("FR-Designer_LocalWidget"),
                SwingConstants.HORIZONTAL), BorderLayout.WEST);
        widgetPane.add(downloadButton, BorderLayout.EAST);
        esp.add(widgetPane,BorderLayout.NORTH);
        tabbedPane = new UITabbedPane();
        tabbedPane.setOpaque(true);
        tabbedPane.setBorder(null);
        tabbedPane.setTabPlacement(SwingConstants.BOTTOM);
        tabbedPane.addTab(Inter.getLocText("FR-Engine_Report"), esp);
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Form-ToolBar_Chart"), new JPanel());
        add(tabbedPane, BorderLayout.CENTER);

    }

    /**
     * 初始化组件共享和复用面板
     */
    private void initReuWidgetPanel() {
        int rowCount = (elCaseBindInfoList.length + 1)/2;
        downPanel = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList));
        downPanel.setPreferredSize(new Dimension(236, rowCount * 82));
        reuWidgetPanel = new JPanel();
        comboBox = new UIComboBox(getCategories());
        comboBox.setPreferredSize(new Dimension(236, 30));
        initComboBoxSelectedListener();
        reuWidgetPanel.add(comboBox, BorderLayout.NORTH);
        reuWidgetPanel.add(downPanel, BorderLayout.CENTER);
        reuWidgetPanel.setBorder(new LineBorder(Color.gray));
    }

    private void initComboBoxSelectedListener() {
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int filterIndex = comboBox.getSelectedIndex();
                if (filterIndex == 0) {
                    elCaseBindInfoList = ShareLoader.getLoader().getAllBindInfoList();
                } else {
                    String filterName = (String) e.getItem();
                    elCaseBindInfoList = ShareLoader.getLoader().getFilterBindInfoList(filterName);
                }
                refreshDownPanel();

            }
        });
    }

    private void createDownloadButton() {
        downloadButton = new UIButton();
        downloadButton.setIcon(BaseUtils.readIcon("/com/fr/design/form/images/showmenu.png"));
        downloadButton.set4ToolbarButton();
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIPopupMenu menu = new UIPopupMenu();
                UIMenuItem downloadItem = new UIMenuItem(Inter.getLocText("FR-Designer_Download_Template"), BaseUtils.readIcon("/com/fr/design/form/images/download.png"));
                menu.add(downloadItem);
                downloadItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BasicPane managerPane = new WebManagerPaneFactory().createReusePane();
                        UIDialog dlg = new ShopDialog(DesignerContext.getDesignerFrame(), managerPane);
                        PluginWebBridge.getHelper().setDialogHandle(dlg);
                        dlg.setVisible(true);
                    }
                });
                GUICoreUtils.showPopupMenu(menu, tabbedPane, tabbedPane.getX() + 140, tabbedPane.getY() + 26);

            }
        });
    }
    public String[] getCategories() {
        return ShareConstants.WIDGET_CATEGORIES;
    }

    public void refreshDownPanel() {
        reuWidgetPanel.remove(downPanel);
        downPanel = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList));
        //todo:这个地方有问题
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