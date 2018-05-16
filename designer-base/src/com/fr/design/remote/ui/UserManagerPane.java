package com.fr.design.remote.ui;

import com.fr.design.border.UITitledBorder;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.remote.RemoteMember;
import com.fr.design.remote.ui.list.MemberList;
import com.fr.design.remote.ui.list.MemberListCellRender;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yaohwu
 */
public class UserManagerPane extends BasicPane {

    private List<RemoteMember> members = new ArrayList<>();

    DefaultListModel<RemoteMember> listModel = new DefaultListModel<>();


    public UserManagerPane() {
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new BorderLayout());
        this.add(
                TableLayoutHelper.createTableLayoutPane(
                        new Component[][]{
                                new Component[]{createLeftPanel(), new JPanel()}
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
        UITextField searchKeyInput = new UITextField();
        UIButton searchButton = new UIButton("搜索");
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder());
        searchPanel.add(searchKeyInput);
        searchPanel.add(searchButton);
        // 内容列表
        listModel = new DefaultListModel<>();
        MemberList list = new MemberList(listModel);
        list.setCellRenderer(new MemberListCellRender());
        resetMembers();
        addContentToList();
        UIScrollPane listPane = new UIScrollPane(list);
        listPane.setBorder(BorderFactory.createEmptyBorder());

        content.add(searchPanel, BorderLayout.NORTH);
        content.add(listPane, BorderLayout.CENTER);
        return content;
    }


    private void addContentToList() {
        listModel.removeAllElements();
        for (RemoteMember member : members) {
            listModel.addElement(member);
        }
    }

    private void resetMembers() {
        members.clear();
        members.add(RemoteMember.DEFAULT_MEMBER);
    }
}
