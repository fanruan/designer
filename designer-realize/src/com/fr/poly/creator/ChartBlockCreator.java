package com.fr.poly.creator;

import com.fr.design.DesignState;
import com.fr.design.designer.TargetComponent;
import com.fr.design.gui.chart.MiddleChartComponent;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.selection.QuickEditor;
import com.fr.quickeditor.chartquick.PolyChartQuickEditor;
import com.fr.report.poly.PolyChartBlock;
import com.fr.stable.unit.FU;
import com.fr.stable.unit.UNIT;
import com.fr.stable.unit.UnitRectangle;

import javax.swing.*;
import java.awt.*;

/**
 * @author richer
 * @since 6.5.4 创建于2011-5-10
 */
public class ChartBlockCreator extends BlockCreator<PolyChartBlock> {
    private MiddleChartComponent cpm;
    private ChartBlockEditor editor;

    //图表默认宽高330*240
    private static final UNIT DEFAULT_WIDTH = FU.getInstance(12573000);
    private static final UNIT DEFAULT_HEIGHT = FU.getInstance(9144000);


    public ChartBlockCreator() {

    }

    public ChartBlockCreator(PolyChartBlock block) {
        super(block);
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    /**
     * 初始化
     *
     * @return 初始化的控件.
     */
    public JComponent initMonitor() {
        cpm = DesignModuleFactory.getChartComponent(getValue().getChartCollection());
        cpm.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        return cpm;
    }

    public UnitRectangle getDefaultBlockBounds() {
        return new UnitRectangle(UNIT.ZERO, UNIT.ZERO, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    @Override
    public BlockEditor getEditor() {
        if (editor == null) {
            editor = new ChartBlockEditor(designer, this);
        }
        return editor;
    }

    @Override
    public int getX(float time) {
        return (int) (this.getX() * time);
    }

    @Override
    public int getY(float time) {
        return (int) (this.getY() * time);
    }


    /**
     * 检测按钮状态
     *
     * @date 2015-2-5-上午11:33:46
     */
    public void checkButtonEnable() {
        if (editor == null) {
            editor = new ChartBlockEditor(designer, this);
        }
        editor.checkChartButtonsEnable();
    }

    @Override
    public PolyChartBlock getValue() {
        return block;
    }

    @Override
    public void setValue(PolyChartBlock block) {
        this.block = block;
        cpm.populate(this.block.getChartCollection());
    }


    /**
     * 获取当前工具栏组
     *
     * @return 工具栏组
     * @date 2015-2-5-上午11:29:07
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * 在Form的工具栏组
     *
     * @return 组件数组
     * @date 2015-2-5-上午11:31:46
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[0];
    }

    /**
     * 目标的列表
     *
     * @return 返回列表.
     */
    public MenuDef[] menus4Target() {
        return new MenuDef[0];
    }

    public int getMenuState() {
        return DesignState.POLY_SHEET;
    }

    /**
     * 模板的Menu
     *
     * @return 模板的menu
     */
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    @Override
    public PolyElementCasePane getEditingElementCasePane() {
        return null;
    }

    @Override
    public QuickEditor getQuickEditor(TargetComponent tc) {
        PolyChartQuickEditor quitEditor = new PolyChartQuickEditor();
        quitEditor.populate(tc);
        return quitEditor;
    }
}