package com.fr.env;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.env.LocalDesignerWorkspaceInfo;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itree.filetree.JFileTree;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.file.filter.OnlyShowDirectoryFileFilter;

import com.fr.stable.StringUtils;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.BorderLayout;
import java.io.File;

/**
 * @author yaohwu
 */
public class LocalEnvPane extends BasicBeanPane<LocalDesignerWorkspaceInfo> {

    private UITextField pathTextField;
    private JFileTree localEnvTree;

    public LocalEnvPane() {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());

        // northPane
        JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(northPane, BorderLayout.NORTH);

        northPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Location") + ":"), BorderLayout.WEST);
        northPane.add(pathTextField = new UITextField(), BorderLayout.CENTER);

        // 删除选择文件按钮 添加JFileTree

        // centerPane
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(centerPane, BorderLayout.CENTER);

        // 添加JFileTree
        localEnvTree = new JFileTree();
        JScrollPane localEnvPane = new JScrollPane(localEnvTree);
        centerPane.add(localEnvPane, BorderLayout.CENTER);

        // 设置根路径File 和 文件过滤类型
        localEnvTree.setFileFilter(new OnlyShowDirectoryFileFilter());
        localEnvTree.setRootFiles(File.listRoots());
        localEnvTree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                pathTextField.setText(localEnvTree.getSelectedFile().getPath());
            }
        });

        UITextArea description = new UITextArea();
        centerPane.add(description, BorderLayout.SOUTH);
        description.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Env_Des1"));
        description.setEditable(false);
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Location");
    }

    @Override
    public LocalDesignerWorkspaceInfo updateBean() {
        String path = pathTextField.getText();
        return LocalDesignerWorkspaceInfo.create(StringUtils.EMPTY, path);
    }

    public String getPath() {
        return pathTextField.getText();
    }

    @Override
    public void populateBean(LocalDesignerWorkspaceInfo ob) {
        if (StringUtils.isBlank(ob.getPath())) {
            return;
        }
        pathTextField.setText(ob.getPath());

        final File tmpFile = new File(ob.getPath());
        localEnvTree.selectFile(tmpFile);
        localEnvTree.setEnabled(true);
    }
}