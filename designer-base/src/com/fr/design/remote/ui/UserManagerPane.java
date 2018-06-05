package com.fr.design.remote.ui;

import com.fr.core.env.EnvContext;
import com.fr.core.env.proxy.EnvProxy;
import com.fr.core.env.resource.EnvConfigUtils;
import com.fr.design.border.UITitledBorder;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.remote.ui.list.AddedMemberList;
import com.fr.design.remote.ui.list.AddedMemberListCellRender;
import com.fr.design.remote.ui.list.AddingMemberList;
import com.fr.design.remote.ui.list.AddingMemberListCellRender;
import com.fr.design.remote.ui.list.MemberListSelectedChangeListener;
import com.fr.env.RemoteDesignMember;
import com.fr.env.operator.decision.DecisionOperator;
import com.fr.env.operator.decision.DefaultDecisionOperator;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;
import com.fr.third.guava.collect.ImmutableList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author yaohwu
 */
public class UserManagerPane extends BasicPane {

    /**
     * 获取的决策平台成员
     */
    private List<RemoteDesignMember> addingMembers = new ArrayList<>();
    /**
     * 添加到设计的决策平台成员
     */
    private List<RemoteDesignMember> addedMembers = new ArrayList<>();

    /**
     * 决策平台成员列表model
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
            searchAddingMembers(keyField.getText());
        }
    };

    /**
     * 输入框绑定事件
     */
    private KeyAdapter keyFieldKeyListener = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            // 判断按下的键是否是回车键
            // todo 对话框回车键绑定的是对话框的确定按钮
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                searchAddingMembers(keyField.getText());
            }
        }
    };
    /**
     * 添加到设计的决策成员计数标签
     */
    private UILabel countLabel = new UILabel();
    /**
     * 添加到设计的决策成员计数标签
     */
    private DefaultListModel<RemoteDesignMember> addedListModel;


    /**
     * 左侧列表变动事件
     */
    private MemberListSelectedChangeListener addingListChangeListener = new MemberListSelectedChangeListener() {
        @Override
        public void selectedChange() {
            // 右侧列表发生变化后，将右侧列表中选中但是在左侧列表中没有的成员添加进来,同时移除取消选中的
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
                    Inter.getLocText("Fine-Designer_Remote_Design_Selected_Member_Count",
                            String.valueOf(addedMembers.size())
                    )
            );
            // 刷新左侧列表显示
            addToMemberList();

        }
    };
    private AddedMemberList addedList;
    private AddingMemberList addingList;


    public UserManagerPane() {
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout());
        this.add(
                TableLayoutHelper.createTableLayoutPane(
                        new Component[][]{
                                new Component[]{createLeftPanel(), createRightPanel()}
                        },
                        new double[]{TableLayout.FILL},
                        new double[]{TableLayout.FILL, TableLayout.FILL}
                ),
                BorderLayout.CENTER);
    }


    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Fine-Designer_Remote_Design_Add_Member");
    }

    private JPanel createLeftPanel() {
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 0, 0),
                        UITitledBorder.createBorderWithTitle(
                                Inter.getLocText("Fine-Designer_Remote_Design_Decision_Member")
                        )
                )
        );

        // 搜索
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder());
        keyField.setPreferredSize(new Dimension(250, 20));
        keyField.requestFocus();
        keyField.addKeyListener(keyFieldKeyListener);
        keyButton.setText(Inter.getLocText("Fine-Designer_Remote_Design_Search"));
        keyButton.addActionListener(keyButtonActionListener);
        searchPanel.add(keyField);
        searchPanel.add(keyButton);

        // 内容列表
        addingListModel = new DefaultListModel<>();
        addingList = new AddingMemberList(addingListModel);
        addingList.setCellRenderer(new AddingMemberListCellRender());
        addingList.addSelectedChangeListener(addingListChangeListener);
        resetMembers();
        addToMemberList();
        searchAddingMembers(StringUtils.EMPTY);
        UIScrollPane listPane = new UIScrollPane(addingList);
        listPane.setBorder(BorderFactory.createEmptyBorder());

        content.add(searchPanel, BorderLayout.NORTH);
        content.add(listPane, BorderLayout.CENTER);
        return content;
    }


    private JPanel createRightPanel() {
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 0, 0),
                        UITitledBorder.createBorderWithTitle(
                                Inter.getLocText("Fine-Designer_Remote_Design_Selected_Member")
                        )
                )
        );

        // 计数
        countLabel.setText(
                Inter.getLocText("Fine-Designer_Remote_Design_Selected_Member_Count",
                        String.valueOf(addedMembers.size()))
        );
        countLabel.setBorder(BorderFactory.createEmptyBorder(7, 12, 8, 0));
        countLabel.setForeground(new Color(0x8F8F92));

        addedListModel = new DefaultListModel<>();
        addedList = new AddedMemberList(addedListModel);
        addedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addedList.setCellRenderer(new AddedMemberListCellRender());
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
            } else {
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
                Inter.getLocText("Fine-Designer_Remote_Design_Selected_Member_Count",
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


    private void searchAddingMembers(final String keyword) {

        final SwingWorker getMemberWorker = new SwingWorker<List<RemoteDesignMember>, Void>() {
            @Override
            protected List<RemoteDesignMember> doInBackground() {
                addingMembers.clear();
                String username = EnvConfigUtils.getUsername(EnvContext.currentEnv());
                addingMembers.addAll(DefaultDecisionOperator.getInstance().getMembers(username, keyword));
                return addingMembers;
            }

            @Override
            protected void done() {
                addToMemberList();
            }
        };
        getMemberWorker.execute();
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
