package com.fr.design.gui.controlpane;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by plough on 2017/7/21.
 */
public abstract class UIControlPane extends BasicPane implements UnrepeatedNameHelper {
    protected static final int SHORT_WIDTH = 30; //每加一个short Divider位置加30
    protected JPanel controlUpdatePane;

    private ShortCut4JControlPane[] shorts;
    private NameableCreator[] creators;
    private ToolBarDef toolbarDef;

    private UIToolbar toolBar;
    protected PopupEditPane popupEditPane;
    // peter:这是整体的一个cardLayout Pane
    protected CardLayout cardLayout;

    protected JPanel cardPane;

    public UIControlPane() {
        this.initComponentPane();
    }

    /**
     * 生成添加按钮的NameableCreator
     *
     * @return 按钮的NameableCreator
     */
    public abstract NameableCreator[] createNameableCreators();

    public ShortCut4JControlPane[] getShorts() {
        return shorts;
    }

    public void setShorts(ShortCut4JControlPane[] shorts) {
        this.shorts = shorts;
    }

    public void setCreators(NameableCreator[] creators) {
        this.creators = creators;
    }

    public ToolBarDef getToolbarDef() {
        return toolbarDef;
    }

    public void setToolbarDef(ToolBarDef toolbarDef) {
        this.toolbarDef = toolbarDef;
    }

    public UIToolbar getToolBar() {
        return toolBar;
    }

    public void setToolBar(UIToolbar toolBar) {
        this.toolBar = toolBar;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public JPanel getCardPane() {
        return cardPane;
    }

    public void setCardPane(JPanel cardPane) {
        this.cardPane = cardPane;
    }

    protected void initComponentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.creators = this.createNameableCreators();
        this.controlUpdatePane = createControlUpdatePane();

        // p: edit card layout
        this.cardLayout = new CardLayout();
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardPane.setLayout(this.cardLayout);
        // p:选择的Label
        UILabel selectLabel = new UILabel();
        cardPane.add(selectLabel, "SELECT");
        cardPane.add(controlUpdatePane, "EDIT");
        popupEditPane = new PopupEditPane(cardPane);
        // SplitPane
//        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, getLeftPane(), cardPane);
//        mainSplitPane.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
//        mainSplitPane.setOneTouchExpandable(true);

        this.add(getLeftPane(), BorderLayout.CENTER);
//        mainSplitPane.setDividerLocation(getLeftPreferredSize());
        this.checkButtonEnabled();
    }

    protected abstract JPanel createControlUpdatePane();

    protected JPanel getLeftPane() {
        // LeftPane
        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel leftContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        initLeftPane(leftContentPane);
        leftPane.add(leftContentPane, BorderLayout.CENTER);

        shorts = this.createShortcuts();
        if (ArrayUtils.isEmpty(shorts)) {
            return leftPane;
        }

        toolbarDef = new ToolBarDef();
        for (ShortCut4JControlPane sj : shorts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }
        toolBar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolBar);
        leftContentPane.add(toolBar, BorderLayout.NORTH);

        //  顶部标签及add按钮
        UIToolbar topToolBar = new UIToolbar();
        topToolBar.setLayout(new BorderLayout());
        ShortCut addItem = addItemShortCut().getShortCut();
        addItem.intoJToolBar(topToolBar);
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = { p, f };
        double[] rowSize = { p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel("add hyperlink "), topToolBar},
        };
        JPanel leftTopPane = TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
        leftPane.add(leftTopPane, BorderLayout.NORTH);

        return leftPane;
    }

    /**
     * 初始化左边面板
     */
    protected void initLeftPane(JPanel leftPane) {

    }

    protected int getLeftPreferredSize() {
        return shorts.length * SHORT_WIDTH;
    }


    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
//                addItemShortCut(),
                copyItemShortCut(),
                moveUpItemShortCut(),
                moveDownItemShortCut(),
                sortItemShortCut(),
                removeItemShortCut()
        };
    }

    protected abstract ShortCut4JControlPane addItemShortCut();

    protected abstract ShortCut4JControlPane removeItemShortCut();

    protected abstract ShortCut4JControlPane copyItemShortCut();

    protected abstract ShortCut4JControlPane moveUpItemShortCut();

    protected abstract ShortCut4JControlPane moveDownItemShortCut();

    protected abstract ShortCut4JControlPane sortItemShortCut();

    public abstract Nameable[] update();


    public void populate(Nameable[] nameableArray) {
    }

    /**
     * 检查按钮可用状态 Check button enabled.
     */
    public void checkButtonEnabled() {
    }

    protected void doBeforeRemove() {
    }

    protected void doAfterRemove() {
    }

    public NameableCreator[] creators() {
        return creators == null ? new NameableCreator[0] : creators;
    }

    protected abstract boolean hasInvalid(boolean isAdd);

    /**
     * 刷新 NameableCreator
     *
     * @param creators 生成器
     */
    public void refreshNameableCreator(NameableCreator[] creators) {
        this.creators = creators;
        shorts = this.createShortcuts();
        toolbarDef.clearShortCuts();
        for (ShortCut4JControlPane sj : shorts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }

        toolbarDef.updateToolBar(toolBar);
        toolBar.validate();
        toolBar.repaint();
        this.repaint();
    }

    // 点击"编辑"按钮，弹出面板
    protected class PopupEditPane extends JPopupMenu {
        private JComponent contentPane;
        private static final int WIDTH = 460;
        private static final int HEIGHT = 500;
        //        private PopupToolPane popupToolPane;
//        private int fixedHeight;

        PopupEditPane(JComponent pane) {
            contentPane = pane;
            this.setLayout(new BorderLayout());
            this.add(contentPane, BorderLayout.CENTER);
            this.setOpaque(false);
            contentPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//            fixedHeight = getPreferredSize().height - contentPane.getPreferredSize().height;
//            updateSize();
        }

//        private void updateSize() {
//            int newHeight = fixedHeight + contentPane.getPreferredSize().height;
//            this.setPreferredSize(new Dimension(CONTAINER_WIDTH - TAB_WIDTH, newHeight));
//        }

        public JComponent getContentPane() {
            return contentPane;
        }

        public void replaceContentPane(JComponent pane) {
//            remove(pane);
            this.remove(this.contentPane);
            this.add(this.contentPane = pane);
//            updateSize();
            refreshContainer();
        }

        private void refreshContainer() {
            validate();
            repaint();
            revalidate();
        }
    }
}