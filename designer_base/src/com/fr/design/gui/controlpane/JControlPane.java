package com.fr.design.gui.controlpane;

import com.fr.data.TableDataSource;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.file.DatasourceManagerProvider;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Coder: zack
 * Date: 2016/5/17
 * Time: 15:07
 */
public abstract class JControlPane extends BasicPane implements UnrepeatedNameHelper {
    private static final int SHORT_WIDTH = 30; //每加一个short Divider位置加30

    private ShortCut4JControlPane[] shorts;
    private NameableCreator[] creators;
    protected JPanel controlUpdatePane;

    private ToolBarDef toolbarDef;
    private UIToolbar toolBar;

    // peter:这是整体的一个cardLayout Pane
    private CardLayout cardLayout;
    private JPanel cardPane;
    protected String selectedName;
    protected boolean isNamePermitted = true;
    protected Map<String, String> dsNameChangedMap = new HashMap<String, String>();
    private boolean isNameRepeated = false;

    public JControlPane() {
        this.initComponentPane();
    }

    /**
     * 生成添加按钮的NameableCreator
     *
     * @return 按钮的NameableCreator
     */
    public abstract NameableCreator[] createNameableCreators();

    protected void initComponentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.creators = this.createNameableCreators();
        this.controlUpdatePane = new JPanel();

        // p: edit card layout
        this.cardLayout = new CardLayout();
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardPane.setLayout(this.cardLayout);
        // p:选择的Label
        UILabel selectLabel = new UILabel();
        cardPane.add(selectLabel, "SELECT");
        cardPane.add(controlUpdatePane, "EDIT");
        // SplitPane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, getLeftPane(), cardPane);
        mainSplitPane.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
        mainSplitPane.setOneTouchExpandable(true);

        this.add(mainSplitPane, BorderLayout.CENTER);
        mainSplitPane.setDividerLocation(getLeftPreferredSize());
        this.checkButtonEnabled();
    }


    protected JPanel getLeftPane() {
        // LeftPane
        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        init(leftPane);

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
        leftPane.add(toolBar, BorderLayout.NORTH);
        return leftPane;
    }

    /**
     * 初始化
     */
    protected void init(JPanel leftPane) {

    }

    protected int getLeftPreferredSize() {
        return shorts.length * SHORT_WIDTH;
    }

    public Map<String, String> getDsNameChangedMap() {
        return dsNameChangedMap;
    }

    public void setDsNameChangedMap(Map<String, String> dsNameChangedMap) {
        this.dsNameChangedMap = dsNameChangedMap;
    }

    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                addItemShortCut(),
                removeItemShortCut(),
                copyItemShortCut(),
                moveUpItemShortCut(),
                moveDownItemShortCut(),
                sortItemShortCut()
        };
    }

    protected abstract ShortCut4JControlPane addItemShortCut();

    protected abstract ShortCut4JControlPane removeItemShortCut();

    protected abstract ShortCut4JControlPane copyItemShortCut();

    protected abstract ShortCut4JControlPane moveUpItemShortCut();

    protected abstract ShortCut4JControlPane moveDownItemShortCut();

    protected abstract ShortCut4JControlPane sortItemShortCut();

    public void setNameListEditable(boolean editable) {
    }

    public abstract Nameable[] update();


    public void populate(Nameable[] nameableArray) {
    }

    public void update(DatasourceManagerProvider datasourceManager) {
    }

    public void populate(DatasourceManagerProvider datasourceManager) {
    }

    public void populate(TableDataSource tds) {
    }

    public void update(TableDataSource tds) {
    }


    /**
     * 根据name,选中JNameEdList中的item
     */
    public void setSelectedName(String name) {
    }

    /**
     * 获取选中的名字
     */
    public abstract String getSelectedName();

    /**
     * 名字是否重复
     *
     * @return 重复则返回true
     */
    public boolean isNameRepeated() {
        return isNameRepeated;
    }

    /**
     * 名字是否允许
     *
     * @return 是则返回true
     */
    public boolean isNamePermitted() {
        return isNamePermitted;
    }

    /**
     * 是否重命名
     *
     * @return 是则true
     */
    public abstract boolean isContainsRename();

    /**
     * 生成不重复的名字
     *
     * @param prefix 名字前缀
     * @return 名字
     */
    public abstract String createUnrepeatedName(String prefix);


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

    /**
     * 检查是否符合规范
     *
     * @throws Exception
     */
    public void checkValid() throws Exception {
    }

    protected abstract boolean hasInvalid(boolean isAdd);

    /**
     * 设置选中项
     *
     * @param index 选中项的序列号
     */
    public void setSelectedIndex(int index) {
    }


    protected void rename(String oldName, String newName) {
        dsNameChangedMap.put(oldName, newName);
    }
}
