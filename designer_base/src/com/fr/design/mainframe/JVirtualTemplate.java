package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.DesignModelAdapter;
import com.fr.design.designer.TargetComponent;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.mainframe.templateinfo.TemplateProcessInfo;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.stable.OperatingSystem;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Author : MoMeak
 * Date: 17-11-20
 * 极简模式模板----for减少堆内存引用
 * 其他参数都去掉，只保留触发重新激活的文件路径
 */
public class JVirtualTemplate extends JTemplate {

    private FILE editingFILE = null;

    public JVirtualTemplate(FILE editingFILE) {
        setEditingFILE(editingFILE);
    }

    public String getFullPathName() {
        String editingFileName = getEditingFILE().getPath();
        if (editingFileName.startsWith(ProjectConstants.REPORTLETS_NAME)) {
            editingFileName = ((FileNodeFILE) getEditingFILE()).getEnvPath() + File.separator + editingFileName;
        }
        editingFileName = OperatingSystem.isWindows() ? editingFileName.replaceAll("/", "\\\\") : editingFileName.replaceAll("\\\\", "/");
        return editingFileName;
    }

    /**
     * 得到正在编辑的FILE
     *
     * @return
     */
    public FILE getEditingFILE() {
        return this.editingFILE;
    }

    /**
     * 正在编辑的FILE
     *
     * @return
     */
    public void setEditingFILE(FILE editingFILE) {
        this.editingFILE = editingFILE;
    }

    @Override
    public void refreshEastPropertiesPane() {

    }

    @Override
    public TargetComponent getCurrentElementCasePane() {
        return null;
    }

    public JComponent getCurrentReportComponentPane() {
        return null;
    }

    @Override
    public TemplateProcessInfo getProcessInfo() {
        return null;
    }

    @Override
    public void setJTemplateResolution(int resolution) {

    }

    @Override
    public int getJTemplateResolution() {
        return 0;
    }

    @Override
    protected JComponent createCenterPane() {
        return null;
    }

    @Override
    public void removeTemplateSelection() {

    }

    @Override
    public void refreshContainer() {

    }

    @Override
    public void removeParameterPaneSelection() {

    }

    @Override
    public void setScale(int resolution) {

    }

    @Override
    public int getScale() {
        return 0;
    }

    @Override
    public int selfAdaptUpdate() {
        return 0;
    }

    @Override
    protected DesignModelAdapter createDesignModel() {
        return null;
    }

    @Override
    public UIMenuItem[] createMenuItem4Preview() {
        return new UIMenuItem[0];
    }

    @Override
    protected BaseUndoState<?> createUndoState() {
        return null;
    }

    @Override
    public String suffix() {
        return null;
    }

    @Override
    public void copy() {

    }

    @Override
    public boolean paste() {
        return false;
    }

    @Override
    public boolean cut() {
        return false;
    }

    @Override
    public AuthorityEditPane createAuthorityEditPane() {
        return null;
    }

    @Override
    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        return null;
    }

    @Override
    public JPanel getEastUpPane() {
        return null;
    }

    @Override
    public JPanel getEastDownPane() {
        return null;
    }

    @Override
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    @Override
    public JPanel[] toolbarPanes4Form() {
        return new JPanel[0];
    }

    @Override
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    @Override
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];
    }

    @Override
    public JComponent[] toolBarButton4Form() {
        return new JComponent[0];
    }

    @Override
    public JComponent toolBar4Authority() {
        return null;
    }

    @Override
    public int getToolBarHeight() {
        return 0;
    }

    @Override
    public boolean isJWorkBook() {
        return false;
    }

    @Override
    public void activeJTemplate(int index, JTemplate jt) {
        List<JTemplate<?, ?>> historyList = HistoryTemplateListPane.getInstance().getHistoryList();
        historyList.set(index, jt);
        DesignerContext.getDesignerFrame().addAndActivateJTemplate(jt);
    }

    @Override
    public void activeOldJTemplate() {
        DesignerContext.getDesignerFrame().openTemplate(this.getEditingFILE());
    }

    @Override
    public void activeNewJTemplate() {
        DesignerContext.getDesignerFrame().openTemplate(this.getEditingFILE());
    }

    @Override
    public void closeOverLineTemplate(int index) {
    }

    @Override
    public HyperlinkGroupPane getHyperLinkPane(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        return null;
    }

    public HyperlinkGroupPane getHyperLinkPaneNoPop(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        return null;
    }

    @Override
    public void setAuthorityMode(boolean isUpMode) {

    }

    @Override
    public Icon getIcon() {
        if (getFullPathName().endsWith("cpt")) {
            return BaseUtils.readIcon("/com/fr/design/images/buttonicon/newcpts.png");
        } else {
            return BaseUtils.readIcon("/com/fr/web/images/form/new_form3.png");
        }
    }

    @Override
    protected void applyUndoState(BaseUndoState baseUndoState) {

    }
}
