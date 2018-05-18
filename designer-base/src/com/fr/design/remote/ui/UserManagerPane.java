package com.fr.design.remote.ui;

import com.fr.design.border.UITitledBorder;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.remote.RemoteMember;
import com.fr.design.remote.Utils;
import com.fr.design.remote.ui.list.AddedMemberList;
import com.fr.design.remote.ui.list.AddedMemberListCellRender;
import com.fr.design.remote.ui.list.AddingMemberList;
import com.fr.design.remote.ui.list.AddingMemberListCellRender;
import com.fr.design.remote.ui.list.MemberListSelectedChangeListener;
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
    private List<RemoteMember> addingMembers = new ArrayList<>();
    /**
     * 添加到设计的决策平台成员
     */
    private List<RemoteMember> addedMembers = new ArrayList<>();

    /**
     * 决策平台成员列表model
     */
    private DefaultListModel<RemoteMember> addingListModel = new DefaultListModel<>();
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
            // todo 对话款回车键绑定的是对话框的确定按钮
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
    private DefaultListModel<RemoteMember> addedListModel;


    private MemberListSelectedChangeListener addingListChangeListener = new MemberListSelectedChangeListener() {
        @Override
        public void selectedChange() {
            resetAddedMembers();
            sync2AddedMembersFromAdding();
            addToAddedMemberList();
        }
    };

    private MemberListSelectedChangeListener addedListChangeListener = new MemberListSelectedChangeListener() {
        @Override
        public void selectedChange() {
            addingList.revalidate();
            addingList.repaint();
            resetAddedMembers();
            sync2AddedMembersFormAdded();
            // 不需要重复更新右侧列表显示

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
        return "添加设计成员";
    }

    private JPanel createLeftPanel() {
        JPanel content = new JPanel(new BorderLayout());

        content.setBorder(
                BorderFactory.createCompoundBorder(
                        new EmptyBorder(6, 0, 0, 0),
                        UITitledBorder.createBorderWithTitle(Inter.getLocText("决策系统成员")))
        );

        // 搜索
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder());
        keyField.setPreferredSize(new Dimension(200, 20));
        keyField.requestFocus();
        keyField.addKeyListener(keyFieldKeyListener);
        keyButton.setText("搜索");
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
                        UITitledBorder.createBorderWithTitle(Inter.getLocText("已选择的设计成员")))
        );

        // 计数
        countLabel.setText(Inter.getLocText("已选择{R1}人", String.valueOf(addedMembers.size())));

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
        for (RemoteMember member : addingMembers) {
            addingListModel.addElement(member);
        }
        addingList.revalidate();
        addingList.repaint();
    }

    private void addToAddedMemberList() {
        addedListModel.clear();
        for (RemoteMember member : addedMembers) {
            addedListModel.addElement(member);
        }
        addedList.revalidate();
        addedList.repaint();
        countLabel.setText(Inter.getLocText("已选择{R1}人", String.valueOf(addedMembers.size())));
    }

    private void resetMembers() {
        addingMembers.clear();
        addingMembers.add(RemoteMember.DEFAULT_MEMBER);
    }

    private void resetAddedMembers() {
        addedMembers.clear();
    }


    private void searchAddingMembers(final String keyword) {

        final SwingWorker getMemberWorker = new SwingWorker<List<RemoteMember>, Void>() {
            @Override
            protected List<RemoteMember> doInBackground() {
                addingMembers.clear();
                addingMembers.addAll(Utils.getRemoteMember(keyword));
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        RemoteMember[] members = new RemoteMember[addingListModel.getSize()];
        // shallow copy
        addingListModel.copyInto(members);
        for (RemoteMember member : members) {
            if (member.isSelected()) {
                addedMembers.add(member);
            }
        }
    }

    private void sync2AddedMembersFormAdded() {
        RemoteMember[] members = new RemoteMember[addedListModel.getSize()];
        // shallow copy
        addedListModel.copyInto(members);
        addedMembers.addAll(Arrays.asList(members));
    }


    public ImmutableList<RemoteMember> update() {
        return ImmutableList.copyOf(addedMembers);
    }
}
