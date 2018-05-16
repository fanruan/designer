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
import com.fr.design.remote.ui.list.MemberList;
import com.fr.design.remote.ui.list.MemberListCellRender;
import com.fr.general.Inter;

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
import java.util.List;


/**
 * @author yaohwu
 */
public class UserManagerPane extends BasicPane {

    /**
     * 获取的决策平台成员
     */
    private List<RemoteMember> members = new ArrayList<>();
    /**
     * 添加到设计的决策平台成员
     */
    private List<RemoteMember> addedMembers = new ArrayList<>();

    /**
     * 决策平台成员列表model
     */
    private DefaultListModel<RemoteMember> listModel = new DefaultListModel<>();
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
            searchMembers(keyField.getText());
        }
    };

    /**
     * 输入框绑定事件
     */
    private KeyAdapter keyFieldKeyListener = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            //判断按下的键是否是回车键
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                searchMembers(keyField.getText());
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
    private DefaultListModel<Object> addedListModel;


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
        listModel = new DefaultListModel<>();
        final MemberList list = new MemberList(listModel);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setCellRenderer(new MemberListCellRender());
        resetMembers();
        addToMemberList();
        UIScrollPane listPane = new UIScrollPane(list);
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
        countLabel.setText(Inter.getLocText("已选择{R1}人", "0"));

        addedListModel = new DefaultListModel<>();
        final AddedMemberList addedList = new AddedMemberList(listModel);
        addedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addedList.setCellRenderer(new MemberListCellRender());
        resetAddedMembers();
        addToAddedMemberList();
        UIScrollPane listPane = new UIScrollPane(addedList);
        listPane.setBorder(BorderFactory.createEmptyBorder());

        content.add(countLabel, BorderLayout.NORTH);
        content.add(listPane, BorderLayout.CENTER);
        return content;

    }


    private void addToMemberList() {
        listModel.clear();
        for (RemoteMember member : members) {
            listModel.addElement(member);
        }
    }

    private void addToAddedMemberList() {
        addedListModel.clear();
        for (RemoteMember member : addedMembers) {
            addedListModel.addElement(member);
        }
    }

    private void resetMembers() {
        members.clear();
        members.add(RemoteMember.DEFAULT_MEMBER);
    }

    private void resetAddedMembers() {
        addedMembers.clear();
    }


    private void searchMembers(final String keyword) {

        final SwingWorker getMemberWorker = new SwingWorker<List<RemoteMember>, Void>() {
            @Override
            protected List<RemoteMember> doInBackground() {
                members.clear();
                members.addAll(Utils.getRemoteMember(keyword));
                return members;
            }

            @Override
            protected void done() {
                addToMemberList();
            }
        };
        getMemberWorker.execute();
    }
}
