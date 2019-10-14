package com.fr.design.remote.ui;

import com.fr.base.BaseUtils;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.remote.ui.list.AddedMemberList;
import com.fr.design.remote.ui.list.AddedMemberListCellRender;
import com.fr.design.remote.ui.list.AddingMemberList;
import com.fr.design.remote.ui.list.AddingMemberListCellRender;
import com.fr.design.remote.ui.list.MemberListSelectedChangeListener;
import com.fr.stable.StringUtils;
import com.fr.third.guava.collect.ImmutableList;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.authority.RemoteDesignMember;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * 选择设计角色、用户面板的基类
 * @author yaohwu
 */
public abstract class AbstractManagerPane extends BasicPane {

    /**
     * 每页个数
     */
    private static final int DEFAULT_NUM_EACH_PAGE = 50;

    /**
     * 获取的决策平台用户
     */
    private final List<RemoteDesignMember> addingMembers = new ArrayList<>();
    /**
     * 添加到设计的决策平台用户
     */
    private List<RemoteDesignMember> addedMembers = new ArrayList<>();


    /**
     * 具有设计权限的角色/用户
     */
    private List<RemoteDesignMember> authorityMembers = new ArrayList<>();

    /**
     * 决策平台用户列表model
     */
    private DefaultListModel<RemoteDesignMember> addingListModel = new DefaultListModel<>();
    /**
     * 搜索输入框
     */
    private UITextField keyField = new UITextField();
    /**
     * 搜索按钮
     */
    private UIButton keyButton = new UIButton();

    /**
     * 搜索按钮绑定事件
     */
    private ActionListener keyButtonActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            searchAddingMembers(keyWord);
        }
    };

    /**
     * 输入框绑定事件
     */
    private KeyAdapter keyFieldKeyListener = new KeyAdapter() {

        @Override
        public void keyPressed(KeyEvent e) {
            // 判断按下的键是否是回车键
            // 对话框回车键绑定的是对话框的确定按钮
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                searchAddingMembers(keyWord);
                // has been processed
                e.consume();
            }
        }
    };
    /**
     * 添加到设计的决策用户计数标签
     */
    private UILabel countLabel = new UILabel();
    /**
     * 添加到设计的决策用户计数标签
     */
    private DefaultListModel<RemoteDesignMember> addedListModel;


    /**
     * 左侧列表变动事件
     */
    private MemberListSelectedChangeListener addingListChangeListener = new MemberListSelectedChangeListener() {
        @Override
        public void selectedChange() {
            sync2AddedMembersFromAdding();
            // 刷新右侧列表显示
            addToAddedMemberList();
        }
    };

    /**
     * 右侧列表变动事件
     */
    private MemberListSelectedChangeListener addedListChangeListener = new MemberListSelectedChangeListener() {
        @Override
        public void selectedChange() {
            addingList.revalidate();
            addingList.repaint();
            resetAddedMembers();
            sync2AddedMembersFormAdded();
            // 不需要重复更新右侧列表显示 但是更新一下计数显示
            countLabel.setText(
                    Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Selected_Member_Count",
                            String.valueOf(addedMembers.size())
                    )
            );
            // 刷新左侧列表显示
            addToMemberList();
        }
    };


    /**
     * 已经添加的用户列表
     */
    private AddedMemberList addedList;
    /**
     * 待添加的用户列表
     */
    private AddingMemberList addingList;

    /**
     * 搜索关键字
     */
    private String keyWord;

    /**
     * 搜索关键词变更监听
     */
    private transient DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            keyWord = keyField.getText();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            keyWord = keyField.getText();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            keyWord = keyField.getText();
        }
    };

    /**
     * 当前分页计数
     */
    private int pageNum = 1;


    public AbstractManagerPane() {
        this.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        this.setLayout(new BorderLayout());
        this.add(
                TableLayoutHelper.createCommonTableLayoutPane(
                        new Component[][]{
                                new Component[]{createLeftPanel(), createRightPanel()}
                        },
                        new double[]{TableLayout.FILL},
                        new double[]{TableLayout.FILL, TableLayout.FILL},
                        LayoutConstants.VGAP_LARGE
                ),
                BorderLayout.CENTER);
    }

    public void populate(List<RemoteDesignMember> addedMembers) {

        // 已选信息
        resetAddedMembers();
        this.addedMembers.addAll(addedMembers);

        resetAuthorityMembers();
        this.authorityMembers.addAll(addedMembers);

        // 刷新右侧面板
        addToAddedMemberList();

        // 刷新左侧展示信息
        addToMemberList();
    }


    @Override
    protected String title4PopupWindow() {
        // 选择设计用户
        return Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Choose_User");
    }

    protected abstract JPanel leftPanel();

    private JPanel createLeftPanel() {
        JPanel content = leftPanel();

        // 搜索
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        keyField.setPreferredSize(new Dimension(270, 20));
        keyField.requestFocus();
        keyField.addKeyListener(keyFieldKeyListener);
        keyField.getDocument().addDocumentListener(documentListener);
        keyButton.setIcon(BaseUtils.readIcon("com/fr/design/images/buttonicon/user_search_normal.png"));
        keyButton.setToolTipText(Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Search"));
        keyButton.addActionListener(keyButtonActionListener);
        searchPanel.add(keyField);
        searchPanel.add(keyButton);

        // 内容列表
        addingListModel = new DefaultListModel<>();
        addingList = new AddingMemberList(addingListModel);
        addingList.setCellRenderer(getAddingMemberListCellRender());
        addingList.addSelectedChangeListener(addingListChangeListener);
        resetMembers();
        addToMemberList();
        searchAddingMembers(StringUtils.EMPTY);
        final UIScrollPane listPane = new UIScrollPane(addingList);
        listPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                JViewport vp = listPane.getViewport();
                if (vp.getView().getHeight() <= vp.getHeight() + vp.getViewPosition().y) {
                    loadMoreAddingMembers(keyWord, DEFAULT_NUM_EACH_PAGE);
                }
            }
        });
        listPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                JViewport vp = listPane.getViewport();
                if (vp.getView().getHeight() <= vp.getHeight() + vp.getViewPosition().y && e.getValueIsAdjusting()) {
                    loadMoreAddingMembers(keyWord, DEFAULT_NUM_EACH_PAGE);
                }
            }
        });
        listPane.setBorder(BorderFactory.createEmptyBorder());

        content.add(searchPanel, BorderLayout.NORTH);
        content.add(listPane, BorderLayout.CENTER);
        return content;
    }

    protected abstract AddingMemberListCellRender getAddingMemberListCellRender();

    protected abstract AddedMemberListCellRender getAddedMemberListCellRender();

    protected abstract JPanel rightPanel();

    private JPanel createRightPanel() {
        JPanel content = rightPanel();

        // 计数
        countLabel.setText(
                // 已选择{}人
                Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Selected_Member_Count",
                        String.valueOf(addedMembers.size()))
        );
        countLabel.setBorder(BorderFactory.createEmptyBorder(7, 12, 8, 0));
        countLabel.setForeground(new Color(0x8F8F92));

        addedListModel = new DefaultListModel<>();
        addedList = new AddedMemberList(addedListModel);
        addedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addedList.setCellRenderer(getAddedMemberListCellRender());
        addedList.addSelectedChangeListener(addedListChangeListener);
        resetAddedMembers();
        addToAddedMemberList();
        UIScrollPane listPane = new UIScrollPane(addedList);
        listPane.setBorder(BorderFactory.createEmptyBorder());

        content.add(countLabel, BorderLayout.NORTH);
        content.add(listPane, BorderLayout.CENTER);
        return content;

    }


    private void addToMemberList() {
        addingListModel.clear();
        for (RemoteDesignMember member : addingMembers) {
            // 如果包含在右侧列表中，那么左侧列表默认选中
            if (addedMembers.contains(member)) {
                member.setSelected(true);
            }
            else {
                member.setSelected(false);
            }
            addingListModel.addElement(member);
        }
        addingList.revalidate();
        addingList.repaint();
    }

    private void addToAddedMemberList() {
        addedListModel.clear();
        for (RemoteDesignMember member : addedMembers) {
            addedListModel.addElement(member);
        }
        addedList.revalidate();
        addedList.repaint();
        countLabel.setText(
                Toolkit.i18nText("Fine-Design_Basic_Remote_Design_Selected_Member_Count",
                        String.valueOf(addedMembers.size())
                ));
    }

    private void resetMembers() {
        addingMembers.clear();
        addingMembers.add(RemoteDesignMember.DEFAULT_MEMBER);
    }

    private void resetAddedMembers() {
        addedMembers.clear();
    }

    private void resetAuthorityMembers() {
        authorityMembers.clear();
    }

    protected abstract Collection<RemoteDesignMember> getMembers(String userName, String keyWord);

    protected abstract Collection<RemoteDesignMember> getMembers(String userName, String keyWord, int pageNum, int count);

    private void searchAddingMembers(final String keyword) {

        final SwingWorker getMemberWorker = new SwingWorker<List<RemoteDesignMember>, Void>() {
            @Override
            protected List<RemoteDesignMember> doInBackground() {
                String username = WorkContext.getCurrent().getConnection().getUserName();
                synchronized (addingMembers) {
                    addingMembers.clear();
                    Collection<RemoteDesignMember> more = getMembers(username, keyword);
                    pageNum = 1;
                    if (!more.isEmpty()) {
                        addingMembers.addAll(more);
                        if (more.size() >= DEFAULT_NUM_EACH_PAGE) {
                            addingMembers.add(RemoteDesignMember.DEFAULT_MEMBER);
                        }
                    }
                }
                return addingMembers;
            }

            @Override
            protected void done() {
                referAddingMemberList();
                addToMemberList();
            }
        };
        getMemberWorker.execute();
    }

    private void loadMoreAddingMembers(final String keyword, final int count) {

        final SwingWorker loadMoreWorker = new SwingWorker<List<RemoteDesignMember>, Void>() {
            @Override
            protected List<RemoteDesignMember> doInBackground() {

                String username = WorkContext.getCurrent().getConnection().getUserName();
                synchronized (addingMembers) {
                    addingMembers.remove(RemoteDesignMember.DEFAULT_MEMBER);
                    Collection<RemoteDesignMember> more = getMembers(username, keyword, pageNum + 1, count);
                    if (!more.isEmpty()) {
                        pageNum += 1;
                        addingMembers.addAll(more);
                        addingMembers.add(RemoteDesignMember.DEFAULT_MEMBER);
                    }
                }
                return addingMembers;
            }

            @Override
            protected void done() {
                referAddingMemberList();
                addToMemberList();
            }
        };
        loadMoreWorker.execute();
    }

    // 检查左侧列表角色/用户是否有权限
    private void referAddingMemberList() {
        for (RemoteDesignMember member : addingMembers) {
            if (authorityMembers.contains(member)){
                member.setAuthority(true);
            }
        }
    }


    private void sync2AddedMembersFromAdding() {
        RemoteDesignMember[] members = new RemoteDesignMember[addingListModel.getSize()];
        // shallow copy
        addingListModel.copyInto(members);
        for (RemoteDesignMember member : members) {

            if (!member.isSelected()) {
                addedMembers.remove(member);
            }
            if (member.isSelected() && !addedMembers.contains(member)) {
                addedMembers.add(member);
            }
        }
    }

    private void sync2AddedMembersFormAdded() {
        RemoteDesignMember[] members = new RemoteDesignMember[addedListModel.getSize()];
        // shallow copy
        addedListModel.copyInto(members);
        addedMembers.addAll(Arrays.asList(members));
    }


    public ImmutableList<RemoteDesignMember> update() {
        return ImmutableList.copyOf(addedMembers);
    }
}
