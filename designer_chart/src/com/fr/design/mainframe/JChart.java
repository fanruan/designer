/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 * 图表类型文件
 */

package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.file.FILEChooserPane;
import com.fr.file.FILEChooserPane4Chart;
import com.fr.form.ui.ChartBook;
import com.fr.design.DesignModelAdapter;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.actions.ExcelExportAction4Chart;
import com.fr.design.mainframe.actions.PDFExportAction4Chart;
import com.fr.design.mainframe.actions.PNGExportAction4Chart;
import com.fr.design.mainframe.form.FormECCompositeProvider;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.file.FILE;
import com.fr.general.Inter;
import com.fr.json.JSONArray;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * 图表crt文件
 * <p/>
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: 下午2:28
 */
public class JChart extends JTemplate<ChartBook, ChartUndoState> {
    public static final String XML_TAG = "JChart";
    private static final String CHART_CARD = "FORM";
    private static final String ELEMENTCASE_CARD = "ELEMENTCASE";

    private static final String[] CARDNAME = new String[]{CHART_CARD, ELEMENTCASE_CARD};
    private static final int TOOLBARPANEDIMHEIGHT_FORM = 60;
    //图表设计器
    ChartDesigner chartDesigner;

    //中间编辑区域, carllayout布局
    private JPanel tabCenterPane;
    private CardLayout cardLayout;
    //当前编辑的组件对象
    private JComponent editingComponent;
    private FormECCompositeProvider reportComposite;

    public JChart() {
        super(new ChartBook(), "Chart");
    }

    public JChart(ChartBook chartFile, FILE file) {
        super(chartFile, file);
    }

    @Override
    protected JPanel createCenterPane() {
        tabCenterPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(85, 85, 85)));
        chartDesigner = new ChartDesigner(this.getTarget());
        ChartArea area = new ChartArea(chartDesigner);
        centerPane.add(area, BorderLayout.CENTER);
        tabCenterPane.add(centerPane, CHART_CARD, 0);
        chartDesigner.addTargetModifiedListener(new TargetModifiedListener() {
                    public void targetModified(TargetModifiedEvent e) {
                        JChart.this.fireTargetModified();// 调用保存*, 调用刷新界面, 刷新工具栏按钮
                    }
                });

        this.add(tabCenterPane, BorderLayout.CENTER);
        return tabCenterPane;
    }

    /**
     * 移除选择
     */
    public void removeTemplateSelection() {

    }

    /**
     * 刷新容器
     */
    public void refreshContainer() {

    }

    /**
     * 移除参数面板选择
     */
    public void removeParameterPaneSelection() {

    }

    /**
     * 创建设计模式
     *
     * @return 返回模式
     */
    protected DesignModelAdapter<ChartBook, ?> createDesignModel() {
        return null;
    }

    /**
     * 创建预览得菜单
     *
     * @return 菜单
     */
    public UIMenuItem[] createMenuItem4Preview() {
        return new UIMenuItem[0];
    }

    /**
     * 创建撤销状态
     *
     * @return 状态
     */
    protected ChartUndoState createUndoState() {
        return new ChartUndoState(this,chartDesigner.getArea());
    }

    /**
     * 应用撤销状态
     *
     * @param chartUndoState 撤销状态
     */
    protected void applyUndoState(ChartUndoState chartUndoState) {
        try {
            this.setTarget((ChartBook)chartUndoState.getChartBook().clone());
            chartDesigner.setTarget(this.getTarget());
            chartDesigner.populate();
        }catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 后缀
     *
     * @return 后缀
     */
    public String suffix() {
        return ".crt";
    }

    /**
     * 复制
     */
    public void copy() {

    }

    /**
     * 黏贴
     *
     * @return 是否鸟贴成功
     */
    public boolean paste() {
        return false;
    }

    /**
     * 是否剪切
     *
     * @return 剪切成功
     */
    public boolean cut() {
        return false;
    }

    /**
     * 创建权限编辑面板
     *
     * @return 面板
     */
    public AuthorityEditPane createAuthorityEditPane() {
        return null;
    }

    /**
     * 工具栏
     *
     * @return 工具栏
     */
    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        return null;
    }

    /**
     * 东上面板
     *
     * @return 面板
     */
    public JPanel getEastUpPane() {
        return null;
    }

    /**
     * 东下面板
     *
     * @return 面板
     */
    public JPanel getEastDownPane() {
        return null;
    }

    /**
     * 工具栏菜单
     *
     * @return 菜单
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * 表单面板
     *
     * @return 面板
     */
    public JPanel[] toolbarPanes4Form() {
        return new JPanel[0];
    }

    /**
     * 模版菜单
     *
     * @return 模版菜单
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    /**
     * 权限编辑菜单
     *
     * @return 菜单
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];
    }

    /**
     * 工具条表单
     *
     * @return 表单
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[0];
    }

    /**
     * 权限编辑工具栏,但是图表设计器里面用于正常工具栏不是全县编辑
     *
     * @return 工具条
     */
    public JComponent toolBar4Authority() {
        return chartDesigner.getChartToolBarPane();
    }

    /**
     * 工具条高度
     *
     * @return 工具条高度
     */
    public int getToolBarHeight() {
        return 0;
    }

    /**
     * 是否是报表
     *
     * @return 不是
     */
    public boolean isJWorkBook() {
        return false;
    }

    /**
     * 是否是图表
     *
     * @return 是则返回true
     */
    public boolean isChartBook() {
        return true;
    }

    /**
     * 设置权限编辑模式
     *
     * @param isUpMode 没有权限编辑
     */
    public void setAuthorityMode(boolean isUpMode) {

    }

    /**
     * 刷新工具区域
     */
    public void refreshToolArea() {
        DesignerContext.getDesignerFrame().resetToolkitByPlus(JChart.this);
        chartDesigner.populate();
        ChartDesignerPropertyPane.getInstance().populateChartPropertyPane(getTarget().getChartCollection(), chartDesigner);
        EastRegionContainerPane.getInstance().replaceUpPane(ChartDesignerPropertyPane.getInstance());
    }

    /**
     * 导出菜单的子菜单 ，目前用于图表设计器
     *
     * @return 子菜单
     */
    public ShortCut[] shortcut4ExportMenu() {
        return new ShortCut[]{new PNGExportAction4Chart(this), new ExcelExportAction4Chart(this), new PDFExportAction4Chart(this)};
    }

    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/design/images/chart.png");
    }

    public ChartDesigner getChartDesigner(){
        return chartDesigner;
    }

    /**
     * 复制JS代码
     */
    public void copyJS(){
        JSONObject jsonObject =this.getTarget().createExportConfig();
        String jsonString = StringUtils.EMPTY;
        if(jsonObject != null){
            try{
                if(jsonObject.has("charts")){
                    JSONArray charts = jsonObject.getJSONArray("charts");
                    jsonString = charts.toString(2);
                }else{
                    jsonString = jsonObject.toString(2);
                }
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Chart-CopyJS_Message"), Inter.getLocText("FR-Chart-Action_Copy")+"JS", JOptionPane.INFORMATION_MESSAGE);
            }catch (JSONException ex){
                FRContext.getLogger().error(ex.getMessage());
                JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Chart-CopyJS_Failed")+"!", Inter.getLocText("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Chart-CopyJS_Failed")+"!", Inter.getLocText("Error"), JOptionPane.ERROR_MESSAGE);
        }
        StringSelection stringSelection = new StringSelection(jsonString);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    };

    /**
     * 系列风格改动
     */
    public void styleChange(){
          chartDesigner.clearToolBarStyleChoose();
    }

    protected FILEChooserPane getFILEChooserPane(boolean isShowLoc){
        return new FILEChooserPane4Chart(true, isShowLoc);
    }

}