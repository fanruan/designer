package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteMember;

import javax.swing.JList;
import javax.swing.ListModel;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class MemberList extends JList<RemoteMember> {


    private boolean[] selects;

    public MemberList() {
        super();
        init(this.getModel().getSize());
    }

    public MemberList(ListModel<RemoteMember> dataModel) {
        super(dataModel);
        init(dataModel.getSize());
    }

    public MemberList(RemoteMember[] listData) {
        super(listData);
        init(listData.length);
    }

    public MemberList(Vector<? extends RemoteMember> listData) {
        super(listData);
        init(listData.size());
    }


    @Override
    public void setModel(ListModel<RemoteMember> model) {
        super.setModel(model);
        selects = new boolean[this.getModel().getSize()];
    }

    private void init(int size) {
        selects = new boolean[size];
        setBackground(new Color(0xF5F5F7));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                displaySelected();
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    displaySelected();
                }
            }
        });
    }

    private void displaySelected() {
        RemoteMember member = getSelectedValue();
        member.setSelected(!member.isSelected());
        revalidate();
        repaint();
    }

}


