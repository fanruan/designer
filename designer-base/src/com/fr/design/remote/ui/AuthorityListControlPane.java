package com.fr.design.remote.ui;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.remote.RemoteDesignAuthority;
import com.fr.design.remote.RemoteDesignAuthorityCreator;
import com.fr.design.remote.ui.list.AuthorityList;
import com.fr.design.remote.ui.list.AuthorityListCellRenderer;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class AuthorityListControlPane extends BasicPane {


    private static final String LIST_NAME = "AuthorityListControlPaneList";

    private static final String UNSELECTED_EDITOR_NAME = "UNSELECTED";
    private static final String SELECTED_EDITOR_NAME = "SELECTED";

    private AuthorityList authorityList;

    private static final int SHORT_WIDTH = 30;

    private ListEditorControlPane editorCtrl;


    private CardLayout cardLayout;

    private JPanel cardPane;

    private ShortCut4JControlPane[] shortCuts;

    private RemoteDesignAuthorityCreator[] authorityCreators;

    private ToolBarDef toolbarDef;

    private UIToolbar toolBar;


    public AuthorityListControlPane() {
        super();
        initComponentPane();
    }


    private void initComponentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.authorityCreators = new RemoteDesignAuthorityCreator[]{
                new RemoteDesignAuthorityCreator(
                        "远程设计用户",
                        BaseUtils.readIcon("com/fr/design/remote/images/icon_Member_normal@1x.png"),
                        RemoteDesignAuthority.class,
                        AuthorityEditorPane.class)
        };
        editorCtrl = new ListEditorControlPane();

        // 左侧列表面板
        JPanel leftPane = new JPanel(new BorderLayout());
        initLeftList(leftPane);
        initLeftToolbar(leftPane);

        // 右侧卡片布局
        cardLayout = new CardLayout();
        cardPane = new JPanel(cardLayout);
        UILabel selectLabel = new UILabel();
        cardPane.add(selectLabel, UNSELECTED_EDITOR_NAME);
        cardPane.add(editorCtrl, SELECTED_EDITOR_NAME);

        // 左右分割布局
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPane, cardPane);
        mainSplitPane.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
        mainSplitPane.setOneTouchExpandable(true);
        add(mainSplitPane, BorderLayout.CENTER);
        mainSplitPane.setDividerLocation(shortCuts.length * SHORT_WIDTH);

        checkButtonEnabled();
    }


    private void initLeftToolbar(JPanel leftPane) {
        shortCuts = createShortcuts();
        if (ArrayUtils.isEmpty(shortCuts)) {
            return;
        }
        toolbarDef = new ToolBarDef();
        for (ShortCut4JControlPane sj : shortCuts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }
        toolBar = ToolBarDef.createJToolBar();
        toolbarDef.updateToolBar(toolBar);
        leftPane.add(toolBar, BorderLayout.NORTH);
    }


    private void initLeftList(JPanel leftPane) {
        authorityList = createList();
        authorityList.setName(LIST_NAME);
        leftPane.add(new UIScrollPane(authorityList), BorderLayout.CENTER);


        authorityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        authorityList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                //避免多次update和populate大大降低效率
                if (!evt.getValueIsAdjusting()) {
                    //切换的时候加检验
                    if (hasInvalid()) {
                        return;
                    }
                    AuthorityListControlPane.this.editorCtrl.update();
                    AuthorityListControlPane.this.editorCtrl.populate();
                    AuthorityListControlPane.this.checkButtonEnabled();
                }
            }
        });
    }

    private AuthorityList createList() {
        AuthorityList list = new AuthorityList(new DefaultListModel<RemoteDesignAuthority>());
        list.setCellRenderer(new AuthorityListCellRenderer());
        return list;
    }


    private void doWhenPopulate() {

    }

    private void doBeforePopulate() {

    }


    public RemoteDesignAuthority[] update() {
        List<RemoteDesignAuthority> res = new ArrayList<>();
        this.editorCtrl.update();
        DefaultListModel listModel = (DefaultListModel) this.authorityList.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            res.add((RemoteDesignAuthority) listModel.getElementAt(i));
        }
        return res.toArray(new RemoteDesignAuthority[0]);
    }

    public void populate(RemoteDesignAuthority[] authorities) {
        DefaultListModel<RemoteDesignAuthority> listModel = (DefaultListModel<RemoteDesignAuthority>) this.authorityList.getModel();
        listModel.removeAllElements();
        if (ArrayUtils.isEmpty(authorities)) {
            return;
        }

        for (RemoteDesignAuthority authority : authorities) {
            listModel.addElement(authority);
        }

        if (listModel.size() > 0) {
            this.authorityList.setSelectedIndex(0);
        }
        this.checkButtonEnabled();
    }


    public void updateEditorCtrlPane() {
        editorCtrl.update();
    }

    /*
     * 刷新当前的选中的UpdatePane
     */
    public void populateEditorCtrlPane() {
        this.editorCtrl.populate();
    }


    public void setSelectedName(String name) {
        DefaultListModel listModel = (DefaultListModel) this.authorityList.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            RemoteDesignAuthority authority = (RemoteDesignAuthority) listModel.getElementAt(i);
            if (ComparatorUtils.equals(name, authority.getName())) {
                this.authorityList.setSelectedIndex(i);
                break;
            }
        }
    }


    /**
     * 获取选中的名字
     */
    public String getSelectedName() {
        RemoteDesignAuthority authority = (RemoteDesignAuthority) this.authorityList.getSelectedValue();
        return authority == null ? null : authority.getName();
    }

    /**
     * 添加 RemoteDesignAuthority
     *
     * @param authority authority
     * @param index     序号
     */
    public void addAuthority(RemoteDesignAuthority authority, int index) {
        DefaultListModel<RemoteDesignAuthority> model = (DefaultListModel<RemoteDesignAuthority>) authorityList.getModel();

        model.add(index, authority);
        authorityList.setSelectedIndex(index);
        authorityList.ensureIndexIsVisible(index);

        authorityList.revalidate();
        authorityList.repaint();
    }


    protected DefaultListModel<RemoteDesignAuthority> getModel() {
        return (DefaultListModel<RemoteDesignAuthority>) this.authorityList.getModel();
    }


    /**
     * 检查按钮可用状态 Check button enabled.
     */
    public void checkButtonEnabled() {

        if (authorityList.getSelectedIndex() == -1) {
            this.cardLayout.show(cardPane, UNSELECTED_EDITOR_NAME);
        } else {
            this.cardLayout.show(cardPane, SELECTED_EDITOR_NAME);
        }
        for (ShortCut4JControlPane shortCut : shortCuts) {
            shortCut.checkEnable();
        }
    }


    public class AbsoluteEnableShortCut extends ShortCut4JControlPane {
        AbsoluteEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(true);
        }
    }

    public class NormalEnableShortCut extends ShortCut4JControlPane {
        NormalEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(authorityList.getModel()
                    .getSize() > 0
                    && AuthorityListControlPane.this.authorityList.getSelectedIndex() != -1);
        }
    }


    private BasicBeanPane createPaneByCreators(RemoteDesignAuthorityCreator creator) {
        try {
            return creator.getEditorClazz().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 检查是否符合规范
     *
     * @throws Exception e
     */
    @Override
    public void checkValid() throws Exception {
        this.editorCtrl.checkValid();
    }

    private int getInValidIndex() {
        BasicBeanPane[] p = editorCtrl.editorPanes;
        if (p != null) {
            for (int i = 0; i < p.length; i++) {
                if (p[i] != null) {
                    try {
                        p[i].checkValid();
                    } catch (Exception e) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private boolean hasInvalid() {
        int idx = AuthorityListControlPane.this.getInValidIndex();
        if (authorityList.getSelectedIndex() != idx) {
            try {
                checkValid();
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(AuthorityListControlPane.this, exp.getMessage());
                authorityList.setSelectedIndex(idx);
                return true;
            }
        }
        return false;
    }

    /**
     * 设置选中项
     *
     * @param index 选中项的序列号
     */
    public void setSelectedIndex(int index) {
        authorityList.setSelectedIndex(index);
    }


    private ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                new AbsoluteEnableShortCut(new AddItemUpdateAction(authorityCreators)),
                new NormalEnableShortCut(new RemoveItemAction())
        };
    }


    private void doBeforeRemove() {
    }

    private void doAfterRemove() {
    }


    /**
     * 刷新 creators
     *
     * @param creators 生成器
     */
    public void refreshCreator(RemoteDesignAuthorityCreator[] creators) {
        this.authorityCreators = creators;
        shortCuts = this.createShortcuts();
        toolbarDef.clearShortCuts();
        for (ShortCut4JControlPane sj : shortCuts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }

        toolbarDef.updateToolBar(toolBar);
        toolBar.validate();
        toolBar.repaint();
        this.repaint();
    }


    @Override
    protected String title4PopupWindow() {
        return null;
    }


    private class ListEditorControlPane extends JPanel {
        private CardLayout card;
        private JPanel cardPane;
        private BasicBeanPane[] editorPanes;

        private RemoteDesignAuthority authority;

        ListEditorControlPane() {
            initUpdatePane();
        }

        private void initUpdatePane() {
            card = new CardLayout();
            cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
            cardPane.setLayout(card);
            this.setLayout(FRGUIPaneFactory.createBorderLayout());
            this.add(cardPane);
            editorPanes = new BasicBeanPane[authorityCreators.length];
        }

        public void populate() {
            authority = (RemoteDesignAuthority) AuthorityListControlPane.this.authorityList.getSelectedValue();

            if (authority == null) {
                return;
            }

            for (int i = 0, len = editorPanes.length; i < len; i++) {
                if (authorityCreators[i].accept(authority)) {
                    editorPanes[i] = createPaneByCreators(authorityCreators[i]);
                    cardPane.add(editorPanes[i], String.valueOf(i));
                    card.show(cardPane, String.valueOf(i));
                    doBeforePopulate();
                    editorPanes[i].populateBean(authority);
                    doWhenPopulate();
                    break;
                }
            }
        }


        public void update() {
            for (int i = 0; i < editorPanes.length; i++) {
                BasicBeanPane pane = editorPanes[i];
                if (pane != null && pane.isVisible()) {
                    Object bean = pane.updateBean();
                    if (i < authorityCreators.length) {
                        authorityCreators[i].saveUpdatedBean(authority, bean);
                    }
                }
            }
        }

        public void checkValid() throws Exception {
            if (editorPanes != null) {
                for (BasicBeanPane updatePane : editorPanes) {
                    if (updatePane != null) {
                        updatePane.checkValid();
                    }
                }
            }
        }
    }

    /**
     * 添加按钮
     */
    private class AddItemUpdateAction extends UpdateAction {
        private RemoteDesignAuthorityCreator creator;

        AddItemUpdateAction(RemoteDesignAuthorityCreator[] creators) {
            this.creator = creators[0];
            this.setName(Inter.getLocText("FR-Action_Add"));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final UserManagerPane userManagerPane = new UserManagerPane();
            BasicDialog dialog = userManagerPane.showWindow(SwingUtilities.getWindowAncestor(AuthorityListControlPane.this));

            dialog.addDialogActionListener(new DialogActionAdapter() {
                @Override
                public void doOk() {
                    // todo 获取 UserManagerPane 添加的用户
                    userManagerPane.title4PopupWindow();

                    RemoteDesignAuthority authority = new RemoteDesignAuthority();
                    authority.setName("new User");
                    AuthorityListControlPane.this.addAuthority(authority, getModel().getSize());
                }

                @Override
                public void doCancel() {
                    super.doCancel();
                }
            });
            dialog.setModal(true);
            dialog.setVisible(true);


        }
    }

    /*
     * 删除按钮
     */
    private class RemoveItemAction extends UpdateAction {
        RemoveItemAction() {
            this.setName(Inter.getLocText("FR-Action_Remove"));
            this.setMnemonic('R');
            this.setSmallIcon(BaseUtils
                    .readIcon(IconPathConstants.TD_REMOVE_ICON_PATH));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            doBeforeRemove();
            if (GUICoreUtils.removeJListSelectedNodes(SwingUtilities
                    .getWindowAncestor(AuthorityListControlPane.this), authorityList)) {
                checkButtonEnabled();
                doAfterRemove();
            }
        }
    }
}
