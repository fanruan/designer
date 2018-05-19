package com.fr.design.remote.ui;

import com.fr.base.FRContext;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.remote.RemoteDesignAuthority;
import com.fr.design.remote.ui.tree.FileAuthorityTree;
import com.fr.file.filetree.IOFileNodeFilter;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.util.Arrays;

public class AuthorityEditorPane extends BasicBeanPane<RemoteDesignAuthority> {

    private UILabel label = new UILabel();

    private FileAuthorityTree tree = new FileAuthorityTree();


    public AuthorityEditorPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder());
        this.add(label, BorderLayout.NORTH);
        IOFileNodeFilter filter = new IOFileNodeFilter(new String[]{".cpt", ".class", ".frm", ".form"});
        tree.setDigIn(true);
        tree.setFileNodeFilter(filter);
        this.add(new UIScrollPane(tree), BorderLayout.CENTER);
        tree.refreshEnv(FRContext.getCurrentEnv());
    }

    @Override
    protected String title4PopupWindow() {
        return "编辑文件权限";
    }

    @Override
    public void populateBean(RemoteDesignAuthority ob) {
        label.setText(ob.getName());
        // todo 选中树的结点
    }

    @Override
    public RemoteDesignAuthority updateBean() {
        // todo 更新权限信息
        System.out.println(Arrays.toString(tree.getCheckBoxTreeSelectionModel().getSelectionPaths()));
        return new RemoteDesignAuthority();
    }
}
