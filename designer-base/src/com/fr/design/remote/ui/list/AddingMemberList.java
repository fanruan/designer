package com.fr.design.remote.ui.list;

import com.fr.design.remote.RemoteMember;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class AddingMemberList extends MemberList {


    public AddingMemberList() {
        super();
        init();
    }

    public AddingMemberList(ListModel<RemoteMember> dataModel) {
        super(dataModel);
        init();
    }

    public AddingMemberList(RemoteMember[] listData) {
        super(listData);
        init();
    }

    public AddingMemberList(Vector<? extends RemoteMember> listData) {
        super(listData);
        init();
    }


    private void init() {
        setBackground(new Color(0xF5F5F7));

        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                System.out.println(e.getX() + " " + e.getY());
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
}


