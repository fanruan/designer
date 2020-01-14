package com.fr.design.remote.ui;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.i18n.Toolkit;
import com.fr.design.icon.IconPathConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.remote.RemoteDesignAuthorityCreator;
import com.fr.design.remote.ui.list.AuthorityList;
import com.fr.design.remote.ui.list.AuthorityListCellRenderer;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.report.DesignAuthority;
import com.fr.stable.ArrayUtils;
import com.fr.workspace.server.authority.RemoteDesignMember;

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

/**
 * 左侧面板的基类
 * @author Lucian.Chen
 * @version 10.0
 * Created by Lucian.Chen on 2019/9/19
 */
public abstract class AbstractListControlPane extends BasicPane {

    private static final String LIST_NAME = "AbstractListControlPane";

    private static final String UNSELECTED_EDITOR_NAME = "UNSELECTED";
    private static final String SELECTED_EDITOR_NAME = "SELECTED";

    private AuthorityList authorityList;

    private static final int SHORT_WIDTH = 90;

    private ListEditorControlPane editorCtrl;


    private CardLayout cardLayout;

    private JPanel cardPane;

    private ShortCut4JControlPane[] shortCuts;

    private RemoteDesignAuthorityCreator[] authorityCreators;

    private ToolBarDef toolbarDef;

    private UIToolbar toolBar;


    public AbstractListControlPane() {
        super();
        initComponentPane();
    }


    private void initComponentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.authorityCreators = getAuthorityCreators();

        editorCtrl = new ListEditorControlPane();

        // 左侧列表面板
        JPanel leftPane = new JPanel(new BorderLayout());
        initLeftList(leftPane);
        initLeftToolbar(leftPane);

        // 右侧卡片布局
        cardLayout = new CardLayout();
        cardPane = new JPanel(cardLayout);
        UILabel emptyLabel = new UILabel();
        cardPane.add(emptyLabel, UNSELECTED_EDITOR_NAME);
        cardPane.add(editorCtrl, SELECTED_EDITOR_NAME);

        // 左右分割布局
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPane, cardPane);
        mainSplitPane.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
        mainSplitPane.setOneTouchExpandable(true);
        add(mainSplitPane, BorderLayout.CENTER);
        mainSplitPane.setDividerLocation(shortCuts.length * SHORT_WIDTH);

        checkButtonEnabled();
    }

    protected abstract RemoteDesignAuthorityCreator[] getAuthorityCreators();

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
                    AbstractListControlPane.this.editorCtrl.update();
                    AbstractListControlPane.this.editorCtrl.populate();
                    AbstractListControlPane.this.checkButtonEnabled();
                }
            }
        });
    }

    protected abstract AuthorityListCellRenderer getAuthorityListCellRender();

    private AuthorityList createList() {
        AuthorityList list = new AuthorityList(new DefaultListModel<DesignAuthority>());
        list.setCellRenderer(getAuthorityListCellRender());
        return list;
    }


    private void doWhenPopulate() {

    }

    private void doBeforePopulate() {

    }


    public DesignAuthority[] update() {
        List<DesignAuthority> res = new ArrayList<>();
        this.editorCtrl.update();
        DefaultListModel listModel = (DefaultListModel) this.authorityList.getModel();
        for (int i = 0, len = listModel.getSize(); i < len; i++) {
            DesignAuthority authority = (DesignAuthority) listModel.getElementAt(i);
//            authority.setRoleType(RoleType.USER);
            res.add(authority);
        }
        return res.toArray(new DesignAuthority[res.size()]);
    }

    public void populate(DesignAuthority[] authorities) {
        DefaultListModel<DesignAuthority> listModel = (DefaultListModel<DesignAuthority>) this.authorityList.getModel();
        listModel.removeAllElements();
        if (ArrayUtils.isEmpty(authorities)) {
            return;
        }

        for (DesignAuthority authority : authorities) {
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
            DesignAuthority authority = (DesignAuthority) listModel.getElementAt(i);
            if (ComparatorUtils.equals(name, authority.getUsername())) {
                this.authorityList.setSelectedIndex(i);
                break;
            }
        }
    }


    /**
     * 获取选中的名字
     */
    public String getSelectedName() {
        DesignAuthority authority = this.authorityList.getSelectedValue();
        return authority == null ? null : authority.getUsername();
    }

    /**
     * 添加 RemoteDesignAuthority
     *
     * @param authority authority
     * @param index     序号
     */
    public void addAuthority(DesignAuthority authority, int index) {
        DefaultListModel<DesignAuthority> model = (DefaultListModel<DesignAuthority>) authorityList.getModel();

        for (int i = 0; i < model.size(); i++) {
            if (model.get(i).getUserId().equals(authority.getUserId())) {
                return;
            }
        }

        model.add(index, authority);
        authorityList.setSelectedIndex(index);
        authorityList.ensureIndexIsVisible(index);

        authorityList.revalidate();
        authorityList.repaint();
    }


    /**
     * 添加 RemoteDesignAuthority
     *
     * @param authorities authority
     */
    public void setAuthority(List<DesignAuthority> authorities) {
        DefaultListModel<DesignAuthority> model = (DefaultListModel<DesignAuthority>) authorityList.getModel();
        model.clear();
        if (authorities != null && !authorities.isEmpty()) {
            for (DesignAuthority authority : authorities) {
                model.addElement(authority);
            }
            int size = model.getSize() - 1;
            authorityList.setSelectedIndex(size);
            authorityList.ensureIndexIsVisible(size);
        }
        authorityList.revalidate();
        authorityList.repaint();
    }


    protected DefaultListModel<DesignAuthority> getModel() {
        return (DefaultListModel<DesignAuthority>) this.authorityList.getModel();
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
                    && AbstractListControlPane.this.authorityList.getSelectedIndex() != -1);
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
                    } catch (Exception ignore) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private boolean hasInvalid() {
        int idx = AbstractListControlPane.this.getInValidIndex();
        if (authorityList.getSelectedIndex() != idx) {
            try {
                checkValid();
            } catch (Exception exp) {
                FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                FineJOptionPane.showMessageDialog(AbstractListControlPane.this, exp.getMessage());
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
                new AbsoluteEnableShortCut(new AddItemUpdateAction()),
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

        private DesignAuthority authority;

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
            authority = AbstractListControlPane.this.authorityList.getSelectedValue();

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

    protected abstract AbstractManagerPane getManagerPane();

    protected abstract String getKey();

    /**
     * 选择按钮
     */
    private class AddItemUpdateAction extends UpdateAction {

        AddItemUpdateAction() {
            this.setName(Toolkit.i18nText("Fine-Design_Basic_Action_Choose"));
            this.setMnemonic('A');
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/buttonicon/add.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final AbstractManagerPane managerPane = getManagerPane();
            BasicDialog dialog = managerPane.showWindow(SwingUtilities.getWindowAncestor(AbstractListControlPane.this));

            // 刷新用户管理面板展示信息
            final DesignAuthority[] authorities = AbstractListControlPane.this.update();

            dialog.addDialogActionListener(new DialogActionAdapter() {
                @Override
                public void doOk() {
                    // 获取添加的用户到权限编辑面板
                    List<RemoteDesignMember> members = managerPane.update();

                    List<DesignAuthority> oldAuthorities = new ArrayList<>();

                    // 已有的未修改的
                    for (RemoteDesignMember member : members) {
                        for (DesignAuthority authority : authorities) {
                            if (member.getUserId().equals(authority.getUserId())) {
                                oldAuthorities.add(authority);
                            }
                        }
                    }
                    // 保留已有且仍选择的，删除不再选择的
                    setAuthority(oldAuthorities);

                    // 新增的
                    for (RemoteDesignMember member : members) {
                        DesignAuthority authority = new DesignAuthority();
                        authority.setUsername(member.getUsername());
                        authority.setUserId(member.getUserId());
                        authority.setRealName(member.getRealName());
                        authority.setRoleType(member.getRoleType());
                        addAuthority(authority, getModel().getSize());
                    }

                }
            });

            List<RemoteDesignMember> members = new ArrayList<>();

            for (DesignAuthority authority : authorities) {
                RemoteDesignMember m = new RemoteDesignMember();
                m.setUsername(authority.getUsername());
                m.setUserId(authority.getUserId());
                m.setRealName(authority.getRealName());
                m.setSelected(true);
                m.setRoleType(authority.getRoleType());
                m.setAuthority(true);
                members.add(m);
            }
            managerPane.populate(members);

            dialog.setModal(true);
            dialog.setVisible(true);
        }
    }

    /*
     * 删除按钮
     */
    private class RemoveItemAction extends UpdateAction {
        RemoveItemAction() {
            this.setName(Toolkit.i18nText("Fine-Design_Basic_Action_Remove"));
            this.setMnemonic('R');
            this.setSmallIcon(BaseUtils
                    .readIcon(IconPathConstants.TD_REMOVE_ICON_PATH));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            doBeforeRemove();
            if (GUICoreUtils.removeJListSelectedNodes(SwingUtilities
                    .getWindowAncestor(AbstractListControlPane.this), authorityList, getKey())) {
                checkButtonEnabled();
                doAfterRemove();
            }
        }
    }
}
