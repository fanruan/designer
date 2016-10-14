package com.fr.design.mainframe;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.fr.base.BaseUtils;
import com.fr.design.gui.frpane.UITabbedPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
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
    private JScrollPane downPanel;
    private JPanel reuWidgetPanel;
    private ArrayList<ElCaseBindInfo> elCaseBindInfoList;

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
        downPanel = new UIScrollPane(new ShareWidgetPane(elCaseBindInfoList));
        downPanel.setPreferredSize(new Dimension(240, 400));
        reuWidgetPanel = FRGUIPaneFactory.createCenterFlowInnerContainer_S_Pane();
        UIComboBox comboBox = new UIComboBox(getCategories());
        comboBox.setPreferredSize(new Dimension(240, 30));
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String filterName = (String) e.getItem();
                elCaseBindInfoList = ShareLoader.getLoader().getFilterBindInfoList(filterName);
                refreshUI();
                reuWidgetPanel.repaint();
            }
        });
        reuWidgetPanel.add(comboBox, BorderLayout.NORTH);
        reuWidgetPanel.add(downPanel, BorderLayout.SOUTH);
        reuWidgetPanel.setBorder(new LineBorder(Color.gray));
        esp.add(reuWidgetPanel, BorderLayout.CENTER);
        UIButton button = new UIButton();
        button.setIcon(BaseUtils.readIcon("/com/fr/design/images/control/download.png"));
        button.set4ToolbarButton();
        JPanel widgetPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        widgetPane.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 3));
        widgetPane.add(new UILabel(Inter.getLocText("FR-Designer_LocalWidget"),
                SwingConstants.HORIZONTAL), BorderLayout.WEST);
        widgetPane.add(button, BorderLayout.EAST);
        esp.add(widgetPane,BorderLayout.NORTH);
        tabbedPane = new UITabbedPane();
        tabbedPane.setOpaque(true);
        tabbedPane.setBorder(null);
        tabbedPane.setTabPlacement(SwingConstants.BOTTOM);
        tabbedPane.addTab(Inter.getLocText("FR-Engine_Report"), esp);
        tabbedPane.addTab(Inter.getLocText("FR-Designer-Form-ToolBar_Chart"), new JPanel());
        add(tabbedPane, BorderLayout.CENTER);

    }

    public String[] getCategories() {
        return new String[]{Inter.getLocText("FR-Designer_AllCategories"), "" };
    }

    public void refreshUI() {
        reuWidgetPanel.remove(downPanel);
        downPanel = new JScrollPane(new ShareWidgetPane(elCaseBindInfoList), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        reuWidgetPanel.add(downPanel);

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