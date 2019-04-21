package com.fr.design.mainframe.vcs.ui;

import com.fr.base.GraphHelper;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.ToolBarNewTemplatePane;
import com.fr.design.mainframe.WestRegionContainerPane;
import com.fr.design.menu.ToolBarDef;
import com.fr.file.FileNodeFILE;
import com.fr.file.filetree.FileNode;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.server.vcs.common.Constants;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FileVersionsPanel extends BasicPane {
    private static final String ELLIPSIS = "...";

    private final FileVersionTablePanel fileVersionsTablePane;
    private UILabel titleLabel;
    private String templatePath;
    private UIButton filterBtn;
    private FileVersionDialog versionDialog;


    public FileVersionsPanel(FileVersionTablePanel fileVersionTablePanel) {
        this.fileVersionsTablePane = fileVersionTablePanel;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        UIToolbar toolbar = ToolBarDef.createJToolBar();
        toolbar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        toolbar.setBorderPainted(true);
        Box upPane = Box.createHorizontalBox();
        UIButton backBtn = new UIButton(Constants.VCS_BACK_PNG);
        backBtn.set4ToolbarButton();
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitVcs(templatePath);
            }
        });
        toolbar.add(backBtn);
        filterBtn = new UIButton(Constants.VCS_FILTER_PNG);
        filterBtn.set4ToolbarButton();
        filterBtn.setHorizontalAlignment(SwingConstants.RIGHT);
        filterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFilterPane();
            }
        });
        titleLabel = new UILabel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(257, 21);
            }
        };
        upPane.add(titleLabel);
        upPane.add(Box.createHorizontalGlue());
        upPane.add(filterBtn);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(upPane);
        add(toolbar, BorderLayout.NORTH);

        UIScrollPane jScrollPane = new UIScrollPane(fileVersionsTablePane);
        add(jScrollPane, BorderLayout.CENTER);
    }

    private void showFilterPane() {
        versionDialog = new FileVersionDialog(DesignerContext.getDesignerFrame());
        versionDialog.setVisible(true);
    }


    /**
     * 退出版本管理，并且打开模板
     *
     * @param path 被管理的模板的名字
     */
    public void exitVcs(String path) {

        // 关闭当前打开的版本
        JTemplate<?, ?> jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
        MutilTempalteTabPane.getInstance().setIsCloseCurrent(true);
        MutilTempalteTabPane.getInstance().closeFormat(jt);
        MutilTempalteTabPane.getInstance().closeSpecifiedTemplate(jt);

        udpateDesignerFrame(true);

        final String selectedFilePath = StableUtils.pathJoin(ProjectConstants.REPORTLETS_NAME, path);
        DesignerContext.getDesignerFrame().openTemplate(new FileNodeFILE(new FileNode(selectedFilePath, false)));
    }

    private void refreshVersionTablePane() {
        templatePath = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
        String[] paths = StableUtils.pathSplit(templatePath);
        String filename = paths[paths.length - 1];
        int width = fileVersionsTablePane.getWidth() - 40;
        if (getStringWidth(filename) > width) {
            filename = getEllipsisName(filename, width);
        }
        titleLabel.setText(filename);
        fileVersionsTablePane.updateModel(1);
    }

    public void showFileVersionsPane() {
        udpateDesignerFrame(false);
        refreshVersionTablePane();
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }


    private void udpateDesignerFrame(boolean isExit) {
        // 左上侧面板
        WestRegionContainerPane.getInstance().replaceUpPane(
                isExit ? DesignerFrameFileDealerPane.getInstance() : this);

        DesignModeContext.switchTo(isExit ? com.fr.design.base.mode.DesignerMode.NORMAL : com.fr.design.base.mode.DesignerMode.VCS);
        // MutilTempalteTabPane & NewTemplatePane 是否可点
        ToolBarNewTemplatePane.getInstance().setButtonGray(!isExit);

        JTemplate<?, ?> currentEditingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (currentEditingTemplate.isJWorkBook()) {
            DesignerContext.getDesignerFrame().resetToolkitByPlus(currentEditingTemplate);
        }
    }


    private int getStringWidth(String str) {
        return GraphHelper.getFontMetrics(this.getFont()).stringWidth(str);
    }


    private String getEllipsisName(String name, int maxStringlength) {

        //若是名字长度大于能显示的长度，那能显示的文字的最大长度还要减去省略号的最大长度
//        int maxellipsislength = maxStringlength - ELLIPSIS.length();
        int ellipsisWidth = getStringWidth(ELLIPSIS);
        int leftkeyPoint = 0;
        int rightKeyPoint = name.length() - 1;
        int leftStrWidth = 0;
        int rightStrWidth = 0;
        while (leftStrWidth + rightStrWidth + ellipsisWidth < maxStringlength) {
            if (leftStrWidth <= rightStrWidth) {
                leftkeyPoint++;
            } else {
                rightKeyPoint--;
            }
            leftStrWidth = getStringWidth(name.substring(0, leftkeyPoint));
            rightStrWidth = getStringWidth(name.substring(rightKeyPoint));

            if (leftStrWidth + rightStrWidth + ellipsisWidth > maxStringlength) {
                if (leftStrWidth <= rightStrWidth) {
                    rightKeyPoint++;
                }
                break;
            }
        }

        return name.substring(0, leftkeyPoint) + ELLIPSIS + name.substring(rightKeyPoint);
    }
}
