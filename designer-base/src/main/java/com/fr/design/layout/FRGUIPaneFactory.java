package com.fr.design.layout;

import com.fr.design.border.UITitledBorder;
import com.fr.design.gui.ilable.UILabel;

import com.fr.stable.AssistUtils;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

public class FRGUIPaneFactory {

    private FRGUIPaneFactory() {
    }

    public static final float WIDTH_PARA_F = 80.0f;
    public static final int WIDTH_OFFSET_N = 60;
    public static final int WIDTH_OFFSET_M = 20;
    public static final int WIDTH_PARA_INT = 80;
    public static final float WIDTHABS_PARA_F = 2.0f;
    public static final int HEIGHT_PARA = 25;
    public static final int HEIGHT_OFFSET = 50;

    /**
     * 创建一个靠右靠左的水平间隙为2的流式布局
     *
     * @return FlowLayout对象
     */
    public static LayoutManager createBoxFlowLayout() { // createBoxFlowLayout 图表用到的比较多
        return new FlowLayout(FlowLayout.LEFT, 2, 0);
    }

    /**
     * 创建一个靠左的布局
     *
     * @return FlowLayout对象
     */
    public static LayoutManager createLeftZeroLayout() {
        return new FlowLayout(FlowLayout.LEFT, 0, 0);
    }

    /**
     * 创建一个靠右的布局
     *
     * @return FlowLayout对象
     */
    public static LayoutManager createRightZeroLayout() {
        return new FlowLayout(FlowLayout.RIGHT, 0, 0);
    }

    /**
     * 创建一个靠左的水平和垂直间隙均为5的流式布局
     *
     * @return FlowLayout对象
     */
    public static LayoutManager createLabelFlowLayout() { // createLabelFlowLayout
        return new FlowLayout(FlowLayout.LEFT); // 默认 5, 5
    }

    /**
     * 创建一个靠左流式布局，间距10,10
     *
     * @return FlowLayout对象
     */
    public static LayoutManager createL_FlowLayout() {
        return new FlowLayout(FlowLayout.LEFT, 10, 10);
    }

    /**
     * 创建一个居中流式布局
     *
     * @return FlowLayout对象
     */
    public static LayoutManager createCenterFlowLayout() {
        return new FlowLayout(FlowLayout.CENTER);
    }

    /**
     * 创建一个靠右流式布局
     *
     * @return FlowLayout对象
     */
    public static LayoutManager createRightFlowLayout() {
        return new FlowLayout(FlowLayout.RIGHT);
    }

    /**
     * 创建一个边框布局
     *
     * @return BorderLayout对象
     */
    public static LayoutManager createBorderLayout() {
        return new BorderLayout();
    }

    /**
     * 创建一个边框布局，间距4,4
     *
     * @return BorderLayout对象
     */
    public static LayoutManager createM_BorderLayout() {
        return new BorderLayout(4, 4);
    }


    /**
     * 创建一个1列的网格布局
     *
     * @return FRGridLayout对象
     */
    public static LayoutManager create1ColumnGridLayout() {
        return new FRGridLayout(1);
    }

    /**
     * 创建一个2列的网格布局
     *
     * @return FRGridLayout对象
     */
    public static LayoutManager create2ColumnGridLayout() {
        return new FRGridLayout(2);
    }

    /**
     * 创建一个n列的网格布局
     *
     * @param nColumn 列数
     * @return FRGridLayout对象
     */
    public static LayoutManager createNColumnGridLayout(int nColumn) {
        return new FRGridLayout(nColumn);
    }

    /**
     * 创建一个带标题边框面板
     *
     * @param string 边框标题
     * @return JPanel对象
     */
    public static JPanel createTitledBorderPane(String string) {
        JPanel jp = new JPanel();
        UITitledBorder explainBorder = UITitledBorder.createBorderWithTitle(string);
        jp.setBorder(explainBorder);
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        return jp;
    }

    /**
     * 创建一个带标题边框面板，垂直居左布局
     *
     * @param string 边框标题
     * @return JPanel对象
     */
    public static JPanel createVerticalTitledBorderPane(String string) {
        JPanel jp = new JPanel();
        UITitledBorder explainBorder = UITitledBorder.createBorderWithTitle(string);
        jp.setBorder(explainBorder);
        VerticalFlowLayout layout = new VerticalFlowLayout();
        layout.setAlignLeft(true);
        jp.setLayout(layout);
        return jp;
    }

    /**
     * 创建一个带标题边框面板并且居中显示
     *
     * @param borderTitle 边框标题
     * @return JPanel对象
     */
    public static JPanel createTitledBorderPaneCenter(String borderTitle) {
        JPanel jp = new JPanel();
        UITitledBorder explainBorder = UITitledBorder.createBorderWithTitle(borderTitle);
        jp.setBorder(explainBorder);
        jp.setLayout(new FlowLayout(FlowLayout.CENTER));
        return jp;
    }

    /**
     * 创建一个靠左空边框布局，间隔大
     *
     * @return JPanel对象
     */
    public static JPanel createBigHGapFlowInnerContainer_M_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jp.setLayout(new FRLeftFlowLayout(5, 60, 5));
        return jp;
    }

    /**
     * 创建一个靠左空边框面板，间隔中等
     *
     * @return JPanel对象
     */
    public static JPanel createMediumHGapFlowInnerContainer_M_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jp.setLayout(new FRLeftFlowLayout(5, 20, 5));
        return jp;
    }

    /**
     * 创建一个靠左空边框面板，间隔中等，firsthgap 为0
     *
     * @return JPanel对象
     */
    public static JPanel createMediumHGapFlowInnerContainer_M_Pane_First0() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jp.setLayout(new FRLeftFlowLayout(0, 20, 5));
        return jp;
    }

    /**
     * 创建一个靠左空边框面板，间隔小，firsthgap 为0
     *
     * @return JPanel对象
     */
    public static JPanel createTinyHGapFlowInnerContainer_M_Pane_First0() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jp.setLayout(new FRLeftFlowLayout(0, 5, 0));
        return jp;
    }

    /**
     * 创建一个靠左空边框面板，间隔中等
     *
     * @return JPanel对象
     */
    public static JPanel createMediumHGapHighTopFlowInnerContainer_M_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(50, 5, 0, 0));
        jp.setLayout(new FRLeftFlowLayout(5, 20, 5));
        return jp;
    }

    /**
     * 创建一个正常靠左空边框面板
     *
     * @return JPanel对象
     */
    public static JPanel createNormalFlowInnerContainer_M_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        return jp;
    }

    /**
     * 创建一个靠左0间距边框面板
     *
     * @return JPanel对象
     */
    public static JPanel createLeftFlowZeroGapBorderPane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jp.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        return jp;
    }

    /**
     * 创建一个靠左流式布局，正常流式内嵌
     *
     * @return JPanel对象
     */
    public static JPanel createNormalFlowInnerContainer_S_Pane() {
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        return jp;
    }

    /**
     * 创建一个靠左流式布局，流式内嵌
     *
     * @return JPanel对象
     */
    public static JPanel createBoxFlowInnerContainer_S_Pane() {
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        return jp;
    }

    /**
     * 创建一个靠左流式布局，流式内嵌，首元素距离左边0
     *
     * @return JPanel对象
     */
    public static JPanel createBoxFlowInnerContainer_S_Pane_First0() {
        JPanel jp = new JPanel();
        jp.setLayout(new FRLeftFlowLayout(0, 0, 5));
        return jp;
    }

    /**
     * 创建一个靠右面板
     *
     * @return JPanel对象
     */
    public static JPanel createRightFlowInnerContainer_S_Pane() {
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout(FlowLayout.RIGHT));
        return jp;
    }

    /**
     * 创建一个居中面板
     *
     * @return JPanel对象
     */
    public static JPanel createCenterFlowInnerContainer_S_Pane() {
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout(FlowLayout.CENTER));
        return jp;
    }

    /**
     * 创建一个居中0间距面板
     *
     * @return JPanel对象
     */
    public static JPanel createCenterFlowZeroGapBorderPane() {
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jp.setBorder(BorderFactory.createEmptyBorder());
        return jp;
    }

    /**
     * 创建纵向排列面板
     *
     * @return JPanel对象
     */
    public static JPanel createY_AXISBoxInnerContainer_L_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        return jp;
    }

    /**
     * 创建纵向边框面板
     *
     * @return JPanel对象
     */
    public static JPanel createYBoxEmptyBorderPane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        return jp;
    }

    /**
     * 创建横向面板
     *
     * @return JPanel对象
     */
    public static JPanel createX_AXISBoxInnerContainer_L_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
        return jp;
    }

    /**
     * 创建纵向面板M
     *
     * @return JPanel对象
     */
    public static JPanel createY_AXISBoxInnerContainer_M_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        return jp;
    }

    /**
     * 创建横向内置boxlayout的面板
     *
     * @return JPanel对象
     */
    public static JPanel createX_AXISBoxInnerContainer_M_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
        return jp;
    }

    /**
     * 创建纵向内置boxlayout的面板
     *
     * @return JPanel对象
     */
    public static JPanel createY_AXISBoxInnerContainer_S_Pane() {
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        return jp;
    }

    /**
     * 创建横向内置boxlayout的面板
     *
     * @return JPanel对象
     */
    public static JPanel createX_AXISBoxInnerContainer_S_Pane() {
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
        return jp;
    }

    /**
     * 创建n列网格面板
     *
     * @param nColumn 列数
     * @return JPanel对象
     */
    public static JPanel createNColumnGridInnerContainer_S_Pane(int nColumn) {
        JPanel jp = new JPanel();
        jp.setLayout(new FRGridLayout(nColumn));
        return jp;
    }

    /**
     * 创建n列网格面板
     *
     * @param nColumn 列数
     * @param h       水平间距
     * @param v       垂直间距
     * @return JPanel对象
     */
    public static JPanel createNColumnGridInnerContainer_Pane(int nColumn, int h, int v) {
        JPanel jp = new JPanel();
        jp.setLayout(new FRGridLayout(nColumn, h, v));
        return jp;
    }

    /**
     * 创建顶格n列网格面板
     *
     * @param nColumn 列数
     * @return JPanel对象
     */
    public static JPanel createFillColumnPane(int nColumn) {
        JPanel jp = new JPanel();
        jp.setLayout(new FRGridLayout(nColumn, 0, 0));
        return jp;
    }

    /**
     * 创建垂直流布局面板
     *
     * @param isAlignLeft 是否左对齐
     * @return JPanel对象
     */
    public static JPanel createVerticalFlowLayout_S_Pane(boolean isAlignLeft) {
        JPanel jp = new JPanel();
        VerticalFlowLayout layout = new VerticalFlowLayout();
        layout.setAlignLeft(isAlignLeft);
        jp.setLayout(layout);
        return jp;
    }

    /**
     * 创建垂直流布局面板
     *
     * @param isAlignLeft 是否左对齐
     * @param align the alignment value
     * @param hgap  the horizontal gap between components
     * @param vgap  the vertical gap between components
     * @return JPanel对象
     */
    public static JPanel createVerticalFlowLayout_Pane(boolean isAlignLeft, int align, int hgap, int vgap) {
        JPanel jp = new JPanel();
        VerticalFlowLayout layout = new VerticalFlowLayout(align, hgap, vgap);
        layout.setAlignLeft(isAlignLeft);
        jp.setLayout(layout);
        return jp;
    }

    /**
     * 创建边框面板L
     *
     * @return JPanel对象
     */
    public static JPanel createBorderLayout_L_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp.setLayout(FRGUIPaneFactory.createBorderLayout());
        return jp;
    }

    /**
     * 创建边框面板M
     *
     * @return JPanel对象
     */
    public static JPanel createBorderLayout_M_Pane() {
        JPanel jp = new JPanel();
        jp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jp.setLayout(FRGUIPaneFactory.createM_BorderLayout());
        return jp;
    }

    /**
     * 创建边框面板S
     *
     * @return JPanel对象
     */
    public static JPanel createBorderLayout_S_Pane() {
        JPanel jp = new JPanel();
        jp.setLayout(FRGUIPaneFactory.createBorderLayout());
        return jp;
    }

    /**
     * 创建卡片式布局
     *
     * @return JPanel对象
     */
    public static JPanel createCardLayout_S_Pane() {
        JPanel jp = new JPanel();
        jp.setLayout(new CardLayout());
        return jp;
    }

    /**
     * 创建图标IconRadio面板
     *
     * @param icon      图标
     * @param jradiobtn 按钮
     * @return JPanel对象
     */
    public static JPanel createIconRadio_S_Pane(Icon icon,
                                                JRadioButton jradiobtn) {
        jradiobtn.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        jradiobtn.setBackground(new Color(255, 255, 255));
        JPanel iconRadioPane = new JPanel();
        iconRadioPane.setLayout(new BoxLayout(iconRadioPane, BoxLayout.X_AXIS));

        iconRadioPane.add(new UILabel(icon));
        iconRadioPane.add(jradiobtn);

        return iconRadioPane;
    }

    /**
     * 计算宽度
     *
     * @param width 宽度输入值
     * @return w 宽度输出值
     */
    public static int caculateWidth(int width) {
        int w = 0;
        double m = (double)(width + WIDTH_OFFSET_M) / (double)WIDTH_PARA_F;
        double n = (double)(width + WIDTH_OFFSET_N ) / (double)WIDTH_PARA_F;
        double i = Math.abs(((double) ((int) m + (int) (m + 1)) / WIDTHABS_PARA_F) - m);
        double j = Math.abs(((double) ((int) n + (int) (n + 1)) / WIDTHABS_PARA_F) - n);
        double x = i > j ? i : j;
        if (AssistUtils.equals(x, i)) {
            w = (int) (Math.round(m) * WIDTH_PARA_INT - WIDTH_OFFSET_M);
        } else if (AssistUtils.equals(x, j)) {
            w = (int) (Math.round(n) * WIDTH_PARA_INT - WIDTH_OFFSET_N);
        }
        return w;
    }

    /**
     * 计算高度
     *
     * @param height 高度输入值
     * @return 高度输出值
     */
    public static int caculateHeight(int height) {
        int h = 0;
        double x = (double)(height + HEIGHT_OFFSET) / HEIGHT_PARA;
        h = ((int) x + 1) * HEIGHT_PARA;
        return h;
    }

}