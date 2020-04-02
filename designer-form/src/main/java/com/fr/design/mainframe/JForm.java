package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.base.PaperSize;
import com.fr.base.Parameter;
import com.fr.base.extension.FileExtension;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignState;
import com.fr.design.actions.FormMobileAttrAction;
import com.fr.design.actions.TemplateParameterAction;
import com.fr.design.actions.core.WorkBookSupportable;
import com.fr.design.actions.file.export.EmbeddedFormExportExportAction;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.cell.FloatElementsProvider;
import com.fr.design.data.datapane.TableDataTreePane;
import com.fr.design.designer.TargetComponent;
import com.fr.design.designer.beans.actions.CopyAction;
import com.fr.design.designer.beans.actions.CutAction;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.actions.PasteAction;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XComponent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.FormWidgetAuthorityEditPane;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.gui.frpane.HyperlinkGroupPane;
import com.fr.design.gui.frpane.HyperlinkGroupPaneActionProvider;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.xpane.FormHyperlinkGroupPane;
import com.fr.design.gui.xpane.FormHyperlinkGroupPaneNoPop;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.form.FormECCompositeProvider;
import com.fr.design.mainframe.form.FormECDesignerProvider;
import com.fr.design.mainframe.template.info.JFormProcessInfo;
import com.fr.design.mainframe.template.info.TemplateProcessInfo;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.preview.FormPreview;
import com.fr.design.preview.MobilePreview;
import com.fr.design.report.fit.menupane.ReportFitAttrAction;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.file.FILE;
import com.fr.file.FILEChooserPane;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.main.Form;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.page.PaperSettingProvider;
import com.fr.report.cell.Elem;
import com.fr.report.cell.cellattr.CellImage;
import com.fr.report.worksheet.FormElementCase;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Constants;
import com.fr.stable.ProductConstants;
import com.fr.stable.bridge.StableFactory;
import com.fr.web.controller.ViewRequestConstants;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JForm extends JTemplate<Form, FormUndoState> implements BaseJForm<Form> {
    private static final String FORM_CARD = "FORM";
    private static final String ELEMENTCASE_CARD = "ELEMENTCASE";

    private static final String[] CARDNAME = new String[]{FORM_CARD, ELEMENTCASE_CARD};
    private static final int TOOLBARPANEDIMHEIGHT_FORM = 60;

    //表单设计器
    private FormDesigner formDesign;
    //格子设计器
    private FormECDesignerProvider elementCaseDesign;

    //中间编辑区域, carllayout布局
    private JPanel tabCenterPane;
    private CardLayout cardLayout;
    //当前编辑的组件对象
    private JComponent editingComponent;
    private FormECCompositeProvider reportComposite;

    //FORM_TAB代表是否点击编辑，用于点击编辑前后菜单的显示
    protected int index = FORM_TAB;

    public JForm() {
        super(new Form(new WBorderLayout("form")), "Form");
    }

    public JForm(Form form, FILE file) {
        super(form, file);
    }

    @Override
    public void refreshEastPropertiesPane() {
        // 暂时用不到，遇到的时候再加刷新右侧tab面板的代码
    }

    @Override
    public TargetComponent getCurrentElementCasePane() {
        if (elementCaseDesign == null) {
            return null;
        }
        return elementCaseDesign.getEditingElementCasePane();
    }

    @Override
    public JComponent getCurrentReportComponentPane() {
        return null;
    }

    public int getMenuState() {

        return DesignState.JFORM;
    }

    public TemplateProcessInfo<Form> getProcessInfo() {
        if (processInfo == null) {
            processInfo = new JFormProcessInfo(template);
        }
        return processInfo;
    }

    public FormECCompositeProvider getReportComposite() {
        return this.reportComposite;
    }

    @Override
    public void setJTemplateResolution(int resolution) {
    }

    @Override
    public int getJTemplateResolution() {
        return 0;
    }

    @Override
    public boolean accept(Object o) {
        return !(o instanceof FloatElementsProvider);
    }

    /**
     * 是否是报表
     *
     * @return 否
     */
    public boolean isJWorkBook() {
        return false;
    }

    /**
     * 返回当前支持的超链界面pane
     *
     * @return 超链连接界面
     */
    @Override
    public HyperlinkGroupPane getHyperLinkPane(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        return FormHyperlinkGroupPane.getInstance(hyperlinkGroupPaneActionProvider);
    }

    @Override
    public HyperlinkGroupPane getHyperLinkPaneNoPop(HyperlinkGroupPaneActionProvider hyperlinkGroupPaneActionProvider) {
        return FormHyperlinkGroupPaneNoPop.getInstance(hyperlinkGroupPaneActionProvider);
    }

    //表单返回 FORM_TAB or ELEMENTCASE_TAB
    public int getEditingReportIndex() {
        return this.index;
    }

    public void setAuthorityMode(boolean isUpMode) {
    }

    public int getToolBarHeight() {
        return TOOLBARPANEDIMHEIGHT_FORM;
    }

    /**
     * 菜单栏上的文件按钮
     *
     * @return 菜单数组
     */
    public ShortCut[] shortcut4FileMenu() {
        return ArrayUtils.addAll(
                super.shortcut4FileMenu(),
                DesignerMode.isVcsMode() ? new ShortCut[0] : new ShortCut[]{this.createWorkBookExportMenu()}
        );
    }

    private MenuDef createWorkBookExportMenu() {
        MenuDef exportMenuDef = new MenuDef(KeySetUtils.EXPORT.getMenuName());
        exportMenuDef.setIconPath("/com/fr/design/images/m_file/export.png");
        exportMenuDef.addShortCut(new EmbeddedFormExportExportAction(this));

        return exportMenuDef;
    }

    /**
     * 取消格式
     */
    public void cancelFormat() {
    }

    /**
     * 重新计算大小
     */
    public void doResize() {
        formDesign.getRootComponent().setSize(formDesign.getSize());
        LayoutUtils.layoutRootContainer(formDesign.getRootComponent());
    }

    @Override
    protected JPanel createCenterPane() {
        tabCenterPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardLayout = (CardLayout) tabCenterPane.getLayout();

        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(85, 85, 85)));
        formDesign = new FormDesigner(this.getTarget(), new TabChangeAction(BaseJForm.ELEMENTCASE_TAB, this));
        WidgetToolBarPane.getInstance(formDesign);
        FormArea area = new FormArea(formDesign);
        JPanel areaWrapper = new JPanel(new BorderLayout());
        areaWrapper.add(area, BorderLayout.CENTER);
        areaWrapper.setBackground(Color.white);
        centerPane.add(areaWrapper, BorderLayout.CENTER);
        tabCenterPane.add(centerPane, FORM_CARD, FORM_TAB);
        this.add(tabCenterPane, BorderLayout.CENTER);

        formDesign.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                JForm.this.fireTargetModified();// 调用保存*, 调用刷新界面, 刷新工具栏按钮
            }
        });
        formDesign.addDesignerEditListener(new DesignerEditListener() {
            private XComponent lastAffectedCreator;

            @Override
            public void fireCreatorModified(DesignerEvent evt) {
                if (evt.getCreatorEventID() == DesignerEvent.CREATOR_CUTED) {
                    setPropertyPaneChange(formDesign.getRootComponent());
                } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_DELETED) {
                    // 在 delete 之前，会先 select 父组件。这里直接传入 lastAffectedCreator 就好了
                    setPropertyPaneChange(lastAffectedCreator);
                } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {
                    lastAffectedCreator = evt.getAffectedCreator();
                    setPropertyPaneChange(lastAffectedCreator);
                }
            }
        });
        return tabCenterPane;
    }

    public FormDesigner getFormDesign() {
        return formDesign;
    }

    public void setFormDesign(FormDesigner formDesign) {
        this.formDesign = formDesign;
    }

    /**
     * 去除选择
     */
    public void removeTemplateSelection() {
    }

    public void setSheetCovered(boolean isCovered) {
    }

    /**
     * 刷新容器
     */
    public void refreshContainer() {
    }

    /**
     * 去除参数面板选择
     */
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

    /**
     * 创建权限细粒度编辑面板
     *
     * @return 权限细粒度编辑面板
     */
    public AuthorityEditPane createAuthorityEditPane() {
        FormWidgetAuthorityEditPane formWidgetAuthorityEditPane = new FormWidgetAuthorityEditPane(formDesign);
        formWidgetAuthorityEditPane.populateDetials();
        return formWidgetAuthorityEditPane;
    }


    private void setPropertyPaneChange(XComponent comp) {
        if (comp == null) {
            ParameterPropertyPane.getInstance().setAddParaPaneVisible(false, this);
            return;
        }
        ParameterPropertyPane.getInstance().setAddParaPaneVisible(isAddParaPaneVisible(comp), this);
        editingComponent = comp.createToolPane(this, formDesign);
        if (DesignerMode.isAuthorityEditing()) {
            EastRegionContainerPane.getInstance().replaceWidgetSettingsPane(
                    ComparatorUtils.equals(editingComponent.getClass(), NoSupportAuthorityEdit.class) ? editingComponent : createAuthorityEditPane());
        } else {
            EastRegionContainerPane.getInstance().replaceWidgetSettingsPane(editingComponent);
        }
    }

    private boolean isAddParaPaneVisible(XComponent comp) {
        boolean isVisible = false;
        try {
            isVisible = comp instanceof XWParameterLayout || ((XCreator) comp).getParent() instanceof XWParameterLayout;
        } catch (Throwable throwable) {
            // 发生异常则返回 false
        }
        return isVisible;
    }

    public JComponent getEditingPane() {
        return editingComponent;
    }


    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        return this;
    }


    /**
     * 焦点放到JForm
     */
    @Override
    public void requestFocus() {
        super.requestFocus();
        formDesign.requestFocus();
    }

    /**
     * 焦点放到JForm
     */
    public void requestGridFocus() {
        super.requestFocus();
        formDesign.requestFocus();
    }

    /**
     * 保存文件的后缀名
     *
     * @return 返回后缀名
     */
    @Override
    public String suffix() {
        // daniel改成三个字
        return ".frm";
    }

    /**
     * 刷新
     */
    public void refreshRoot() {
        // formDesign子类的target重置
        this.formDesign.setTarget(this.getTarget());
        this.formDesign.refreshRoot();
        FormHierarchyTreePane.getInstance().refreshRoot();
    }

    /**
     * 刷新s
     */
    public void refreshAllNameWidgets() {
        if (formDesign.getParaComponent() != null) {
            XCreatorUtils.refreshAllNameWidgets(formDesign.getParaComponent());
        }
        XCreatorUtils.refreshAllNameWidgets(formDesign.getRootComponent());
    }

    /**
     * 刷新
     */
    public void refreshSelectedWidget() {
        formDesign.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_SELECTED);
    }


    /**
     * 复制
     */
    @Override
    public void copy() {
        DesignModeContext.doCopy(this.formDesign);
    }


    /**
     * 粘贴
     *
     * @return 是否成功
     */
    @Override
    public boolean paste() {
        return DesignModeContext.doPaste(this.formDesign);
    }


    /**
     * 剪切
     *
     * @return 是否成功
     */
    @Override
    public boolean cut() {
        return DesignModeContext.doCut(this.formDesign);
    }

    // ////////////////////////////////////////////////////////////////////
    // ////////////////for toolbarMenuAdapter//////////////////////////////
    // ////////////////////////////////////////////////////////////////////

    @Override
    public void setPictureElem(Elem elem, CellImage cellImage) {
        elem.setValue(cellImage.getImage());
    }

    /**
     * 目标菜单
     *
     * @return 菜单
     */
    @Override
    public MenuDef[] menus4Target() {
        return this.index == FORM_TAB ?
                ArrayUtils.addAll(super.menus4Target(), this.formDesign.menus4Target()) :
                ArrayUtils.addAll(super.menus4Target(), this.elementCaseDesign.menus4Target());
    }


    /**
     * 模板的工具
     *
     * @return 工具
     */
    @Override
    public ToolBarDef[] toolbars4Target() {
        return this.index == FORM_TAB ?
                this.formDesign.toolbars4Target() :
                this.elementCaseDesign.toolbars4Target();
    }


    /**
     * 模板菜单
     *
     * @return 返回菜单
     */
    @Override
    public ShortCut[] shortcut4TemplateMenu() {
        if (this.index == FORM_TAB) {
            return ArrayUtils.addAll(new ShortCut[]{new TemplateParameterAction(this), new FormMobileAttrAction(this), new ReportFitAttrAction(this)}, new ShortCut[0]);
        } else {
            return ArrayUtils.addAll(new ShortCut[]{new TemplateParameterAction(this), new FormMobileAttrAction(this), new ReportFitAttrAction(this)}, this.elementCaseDesign.shortcut4TemplateMenu());
        }
    }

    /**
     * 权限细粒度模板菜单
     * 表单中去掉此菜单项
     *
     * @return 菜单
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];
    }

    @Override
    protected FormUndoState createUndoState() {
        FormUndoState cur = new FormUndoState(this, this.formDesign.getArea());
        if (this.formDesign.isReportBlockEditing()) {
            cur.setFormReportType(BaseUndoState.STATE_FORM_REPORT);
        }
        return cur;
    }

    /**
     * 应用UndoState
     *
     * @param o undo的状态
     */
    public void applyUndoState4Form(BaseUndoState o) {
        this.applyUndoState((FormUndoState) o);
    }

    /**
     * 可以撤销
     *
     * @return 是则返回true
     */
    public boolean canUndo() {
        //报表块最多撤销至编辑报表块的第一步，不能撤销表单中的操作
        boolean inECUndoForm = undoState != null
                && undoState.getFormReportType() == BaseUndoState.STATE_BEFORE_FORM_REPORT
                && formDesign.isReportBlockEditing();
        return !inECUndoForm
                && this.getUndoManager() != null
                && this.getUndoManager().canUndo();
    }

    // 返回当前的body，
    // 假如当前body是自适应的话就沿用，
    // 假如当前body是绝对布局的话就返回绝对布局body
    private XLayoutContainer selectedBodyLayout() {
        XLayoutContainer rootLayout = formDesign.getRootComponent();
        for (int i = 0; i < rootLayout.getComponentCount(); i++) {
            if (rootLayout.getXCreator(i).acceptType(XWAbsoluteBodyLayout.class)) {
                rootLayout = (XWAbsoluteBodyLayout) rootLayout.getXCreator(i);
            }
        }
        return rootLayout;
    }

    @Override
    protected void applyUndoState(FormUndoState u) {
        try {
            if (this.index == FORM_TAB) {
                //JForm的target重置
                this.setTarget((Form) u.getForm().clone());
                JForm.this.refreshRoot();
                this.formDesign.getArea().setAreaSize(u.getAreaSize(), u.getHorizontalValue(), u.getVerticalValue(), u.getWidthValue(), u.getHeightValue(), u.getSlideValue());
                //撤销的时候要重新选择的body布局
                this.formDesign.getSelectionModel().setSelectedCreators(FormSelectionUtils.rebuildSelection(formDesign.getRootComponent(),
                        formDesign.getRootComponent() == selectedBodyLayout() ? u.getSelectWidgets() : new Widget[]{selectedBodyLayout().toData()}));
                refreshToolArea();
            } else {
                // 只在报表块里撤销是不需要修改外部form对象的, 因为编辑的是当前报表块.
                // 修改了JForm的Target需要同步修改formDesign的Target.
                Form undoForm = (Form) u.getForm().clone();
                String widgetName = this.formDesign.getElementCaseContainerName();
                //这儿太坑了，u.getForm() 与 getTarget内容不一样
                FormElementCaseProvider dataTable = undoForm.getElementCaseByName(widgetName);
                this.reportComposite.setSelectedWidget(dataTable);
                //下面这句话是防止撤销之后直接退出编辑再编辑撤销的东西会回来,因为撤销不会保存EC
                formDesign.setElementCase(dataTable);
            }
            TableDataTreePane.getInstance(DesignModelAdapter.getCurrentModelAdapter()).refreshDockingView();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        if (DesignerMode.isAuthorityEditing()) {
            this.authorityUndoState = u;
        } else {
            this.undoState = u;
        }
    }

    @Override
    public void setTarget(Form form) {
        if (this.formDesign == null) {
            super.setTarget(form);
            return;
        }
        this.formDesign.setTarget(form);
    }

    @Override
    public Form getTarget() {
        if (this.formDesign == null) {
            return super.getTarget();
        }

        return this.formDesign.getTarget();
    }

    @Override
    protected FormModelAdapter createDesignModel() {
        return new FormModelAdapter(this);
    }

    @Override
    public JPanel[] toolbarPanes4Form() {
        return this.index == FORM_TAB ?
                new JPanel[]{FormParaWidgetPane.getInstance(formDesign)} :
                new JPanel[0];
    }

    /**
     * 表单的工具按钮
     *
     * @return 工具按钮
     */
    public JComponent[] toolBarButton4Form() {
        return this.index == FORM_TAB ?
                new JComponent[]{
                        new CutAction(formDesign).createToolBarComponent(),
                        new CopyAction(formDesign).createToolBarComponent(),
                        new PasteAction(formDesign).createToolBarComponent(),
                        new FormDeleteAction(formDesign).createToolBarComponent()} :
                elementCaseDesign.toolBarButton4Form();
    }

    /**
     * 权限细粒度状态下的工具面板
     *
     * @return 工具面板
     */
    public JComponent toolBar4Authority() {
        JPanel panel = new JPanel(new BorderLayout()) {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = ToolBarMenuDock.PANLE_HEIGNT;
                return dim;
            }
        };
        UILabel uiLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Authority_Edit"));
        uiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        uiLabel.setFont(new Font(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_All_MSBold"), 0, 14));
        uiLabel.setForeground(new Color(150, 150, 150));
        panel.add(uiLabel, BorderLayout.CENTER);
        return panel;
    }


    public JPanel getEastUpPane() {
        if (DesignerMode.isAuthorityEditing()) {
            if (formDesign.isSupportAuthority()) {
                return new AuthorityPropertyPane(this);
            } else {
                return new NoSupportAuthorityEdit();
            }
        } else {
            if (editingComponent == null) {
                editingComponent = formDesign.getRootComponent().createToolPane(this, formDesign);
            }
            return (JPanel) editingComponent;
        }
    }

    public JPanel getEastDownPane() {
        return formDesign.getEastDownPane();
    }

    @Override
    /**
     *
     */
    public Icon getPreviewLargeIcon() {
        return super.getPreviewLargeIcon();
    }

    @Override
    public Parameter[] getJTemplateParameters() {
        return this.getTarget().getTemplateParameters();
    }

    @Override
    /**
     * 创建菜单项Preview
     *
     * @return 菜单
     */
    public UIMenuItem[] createMenuItem4Preview() {
        List<UIMenuItem> menuItems = new ArrayList<UIMenuItem>();
        PreviewProvider[] previewProviders = supportPreview();
        for (final PreviewProvider provider : previewProviders) {
            UIMenuItem item = new UIMenuItem(provider.nameForPopupItem(), BaseUtils.readIcon(provider.iconPathForPopupItem()));
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    provider.onClick(JForm.this);
                }
            });
            menuItems.add(item);
        }
        return menuItems.toArray(new UIMenuItem[menuItems.size()]);
    }

    /**
     * 刷新参数
     */
    public void populateParameter() {
        formDesign.populateParameterPropertyPane();
    }

    @Override
    /**
     * 刷新工具区域
     */
    public void refreshToolArea() {
        populateParameter();
        DesignerContext.getDesignerFrame().resetToolkitByPlus(JForm.this);
        //表单切换后拖不进去组件是因为找不到designer
        WidgetToolBarPane.getInstance(formDesign);
        if (DesignerMode.isAuthorityEditing()) {
            if (formDesign.isSupportAuthority()) {
                EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION);
                EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(new AuthorityPropertyPane(this));
            } else {
                EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION_DISABLED);
                EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(new NoSupportAuthorityEdit());
            }
            EastRegionContainerPane.getInstance().replaceConfiguredRolesPane(RolesAlreadyEditedPane.getInstance());
            return;
        }

        if (formDesign.isReportBlockEditing() && elementCaseDesign != null) {
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.FORM_REPORT);
            EastRegionContainerPane.getInstance().removeParameterPane();
            EastRegionContainerPane.getInstance().replaceCellAttrPane(elementCaseDesign.getEastDownPane());
            EastRegionContainerPane.getInstance().replaceCellElementPane(elementCaseDesign.getEastUpPane());
            EastRegionContainerPane.getInstance().replaceConditionAttrPane(elementCaseDesign.getConditionAttrPane());
            EastRegionContainerPane.getInstance().replaceHyperlinkPane(elementCaseDesign.getHyperlinkPane(this));
            return;
        }

        EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.FORM);
        EastRegionContainerPane.getInstance().replaceWidgetSettingsPane(WidgetPropertyPane.getInstance(formDesign));
        ParameterPropertyPane parameterPropertyPane = ParameterPropertyPane.getInstance(formDesign);
        parameterPropertyPane.refreshState(this);
        EastRegionContainerPane.getInstance().addParameterPane(parameterPropertyPane);

        refreshWidgetLibPane();
    }

    private void refreshWidgetLibPane() {
        if (EastRegionContainerPane.getInstance().getWidgetLibPane() == null) {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        FineLoggerFactory.getLogger().error(e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }
                    JPanel pane = new JPanel();
                    pane.setLayout(new BorderLayout());
                    pane.add(FormWidgetDetailPane.getInstance(formDesign), BorderLayout.CENTER);
                    EastRegionContainerPane.getInstance().replaceWidgetLibPane(pane);
                }
            }.start();
        } else {
            JPanel pane = new JPanel();
            pane.setLayout(new BorderLayout());
            pane.add(FormWidgetDetailPane.getInstance(formDesign), BorderLayout.CENTER);
            EastRegionContainerPane.getInstance().replaceWidgetLibPane(pane);
        }
    }

    public String getEditingCreatorName() {
        return formDesign.getSelectionModel().getSelection().getSelectedCreator().toData().getWidgetName();
    }

    public WLayout getRootLayout() {
        return formDesign.getRootComponent().toData();
    }

    /**
     * 选择的是否是表单主体
     *
     * @return 是则返回true
     */
    public boolean isSelectRootPane() {
        return (formDesign.getRootComponent() == formDesign.getSelectionModel().getSelection().getSelectedCreator())
                || (formDesign.getSelectionModel().getSelection().getSelectedCreator().acceptType(XWAbsoluteBodyLayout.class));

    }

    /**
     * 只在Form和ElementCase之间切换
     *
     * @param index 切换位置
     */
    public void tabChanged(int index) {
        this.index = index;
        if (index == ELEMENTCASE_TAB) {
            formDesign.setReportBlockEditing(true);
            ecTabAction();
        } else {
            formDesign.setReportBlockEditing(false);
            formTabAction();
        }
        refreshToolArea();
        this.cardLayout.show(tabCenterPane, CARDNAME[index]);
        if (elementCaseDesign != null && index == ELEMENTCASE_TAB) {
            //报表块编辑失焦，进入报表块可直接编辑A1
            elementCaseDesign.requestFocus();
            //进入编辑报表块，触发一次保存，记住编辑报表块前的表单状态
            //防止报表块中撤销到表单
            JForm.this.fireTargetModified();
        }
    }

    /**
     * 在Form和ElementCase, 以及ElementCase和ElementCase之间切换
     *
     * @param index       切换位置
     * @param ecContainer ElementCase所在container
     */
    public void tabChanged(int index, FormElementCaseContainerProvider ecContainer) {
        if (index == ELEMENTCASE_CHANGE_TAB) {
            saveImage();
            //更新FormDesign中的控件容器
            formDesign.setElementCaseContainer(ecContainer);
            //如果只是内部ElementCase之间的切换, 那么不需要下面的界面变动
            return;
        }

        tabChanged(index);
    }

    /**
     * 格子编辑组件
     */
    private FormECDesignerProvider initElementCaseDesign() {
        HashMap<String, Class> designerClass = new HashMap<String, Class>();
        designerClass.put(Constants.ARG_0, FormElementCaseProvider.class);

        Object[] designerArg = new Object[]{formDesign.getElementCase(), getTarget()};
        FormECDesignerProvider formECDesigner = StableFactory.getMarkedInstanceObjectFromClass(FormECDesignerProvider.XML_TAG, designerArg, designerClass, FormECDesignerProvider.class);
        // 如果是移动端专属模版，需要修改页面大小并显示边缘线
        PaperSettingProvider paperSetting = ((FormElementCase) formECDesigner.getEditingElementCase()).getReportSettings().getPaperSetting();
        paperSetting.setPaperSize(getTarget().getFormMobileAttr().isMobileOnly() ? PaperSize.PAPERSIZE_MOBILE : new PaperSize());

        return formECDesigner;
    }

    /**
     * 整个报表块编辑区域
     */
    private FormECCompositeProvider initComposite() {
        Object[] compositeArg = new Object[]{this, elementCaseDesign, formDesign.getElementCaseContainer()};
        HashMap<String, Class> compoClass = new HashMap<String, Class>();
        compoClass.put(Constants.ARG_0, BaseJForm.class);
        compoClass.put(Constants.ARG_2, FormElementCaseContainerProvider.class);
        return StableFactory.getMarkedInstanceObjectFromClass(FormECCompositeProvider.XML_TAG, compositeArg, compoClass, FormECCompositeProvider.class);
    }

    /**
     * 切换格子编辑
     */
    private void ecTabAction() {
        elementCaseDesign = initElementCaseDesign();
        reportComposite = initComposite();

        tabCenterPane.add((Component) reportComposite, ELEMENTCASE_CARD, 1);
        reportComposite.addTargetModifiedListener(new TargetModifiedListener() {

            @Override
            public void targetModified(TargetModifiedEvent e) {
                JForm.this.fireTargetModified();
                FormElementCaseProvider te = elementCaseDesign.getEditingElementCase();
                formDesign.setElementCase(te);
            }
        });
    }

    private void saveImage() {
        //触发一次保存, 把缩略图保存起来
        JForm.this.fireTargetModified();
        //用formDesign的size是为了当报表块被拉伸时, 它对应的背景图片需要足够大才不会显示成空白
        BufferedImage image = elementCaseDesign.getElementCaseImage(formDesign.getSize());
        formDesign.setElementCaseBackground(image);
    }

    /**
     * 切换form编辑
     */
    private void formTabAction() {
        saveImage();
    }

    /**
     * 取小图标，主要用于多TAB标签栏
     *
     * @return 图表
     */
    public Icon getIcon() {
        return BaseUtils.readIcon("/com/fr/web/images/form/new_form3.png");
    }

    @Override
    public boolean acceptToolbarItem(Class clazz) {
        return WorkBookSupportable.class.isAssignableFrom(clazz);
    }

    @Override
    public Widget getSelectElementCase() {
        FormSelection selection = formDesign.getSelectionModel().getSelection();
        XCreator creator = selection.getSelectedCreator();
        return creator.toData();
    }

    /**
     * 支持的预览模式
     *
     * @return 预览模式
     */
    @Override
    public PreviewProvider[] supportPreview() {
        PreviewProvider[] templatePreviews = super.supportPreview();
        return ArrayUtils.addAll(new PreviewProvider[]{new FormPreview(), new MobilePreview()}, templatePreviews);
    }

    /**
     * 预览按钮点击事件
     *
     * @param provider 预览接口
     */
    @Override
    public void previewMenuActionPerformed(PreviewProvider provider) {
        super.previewMenuActionPerformed(provider);
    }

    @Override
    public String route() {
        return ViewRequestConstants.FORM_VIEW_PATH;
    }

    protected void addChooseFILEFilter(FILEChooserPane fileChooser){
        String appName = ProductConstants.APP_NAME;
        // richer:form文件 daniel 改成三个字
        fileChooser.addChooseFILEFilter(new ChooseFileFilter(FileExtension.FRM, appName + Toolkit.i18nText("Fine-Design_Report_Template_File")));
    }
}
