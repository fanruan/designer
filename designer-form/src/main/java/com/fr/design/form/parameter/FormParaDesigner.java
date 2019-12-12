/*
 * Copyright(c) 2001-2011, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.form.parameter;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.base.parameter.ParameterUI;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignModelAdapter;
import com.fr.design.actions.UpdateAction;
import com.fr.design.designer.beans.actions.CopyAction;
import com.fr.design.designer.beans.actions.CutAction;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.actions.PasteAction;
import com.fr.design.designer.beans.adapters.layout.FRAbsoluteLayoutAdapter;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.FormWidgetAuthorityEditPane;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.EastRegionContainerPane;
import com.fr.design.mainframe.FormArea;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.FormDesignerModeForSpecial;
import com.fr.design.mainframe.FormParaPane;
import com.fr.design.mainframe.FormWidgetDetailPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.WidgetToolBarPane;
import com.fr.design.parameter.ParaDefinitePane;
import com.fr.design.parameter.ParameterDesignerProvider;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.form.main.Form;
import com.fr.form.main.parameter.FormParameterUI;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.EditorHolder;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.container.WAbsoluteLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WParameterLayout;
import com.fr.stable.ArrayUtils;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User   : Richer
 * Version: 6.5.5
 * Date   : 11-7-5
 * Time   : 下午7:46
 * 表单类型的参数设计器
 */
// TODO ALEX_SEP FormDesigner和FormParaDesignEditor应该共用Form的编辑,但是FormParaDesignEditor不应该直接就是FormDesigner
public class FormParaDesigner extends FormDesigner implements ParameterDesignerProvider {
    private static final int NUM_IN_A_LINE = 4;
    private static final int H_COMPONENT_GAP = 165;
    private static final int V_COMPONENT_GAP = 25;
    private static final int FIRST_V_LOCATION = 35;
    private static final int FIRST_H_LOCATION = 90;
    private static final int SECOND_H_LOCATION = 170;
    private static final int ADD_HEIGHT = 20;
    private static final int H_GAP = 105;
    private static final int SUBMIT_BUTTON_H_LOCATION = 270;
    private static final int PARA_IMAGE_SHIFT_X = -4;
    private static final int FORM_AREA_PADDING_LEFT = 13;

    private static Image paraImage = BaseUtils.readImage("/com/fr/design/images/form/parameter.png");

    public FormParaDesigner() {
        this(new FormParameterUI());
    }

    public FormParaDesigner(FormParameterUI ui) {
        super(gen(ui));
    }

    private static Form gen(Form form) {
        WLayout container = form.getContainer();
        if (container == null) {
            container = new WParameterLayout();
        }
        container.setWidgetName("para");
        form.setContainer(container);
        return form;
    }

    protected FormDesignerModeForSpecial<?> createFormDesignerTargetMode() {
        return new FormParaTargetMode(this);
    }

    /**
     * 开始编辑参数面板的时候进行的初始化
     */
    public void initBeforeUpEdit() {
        WidgetToolBarPane.getInstance(this);
        EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.REPORT_PARA);
        EastRegionContainerPane.getInstance().replaceWidgetLibPane(
                FormWidgetDetailPane.getInstance(this));
        if (!DesignerMode.isAuthorityEditing()) {
            ParameterPropertyPane parameterPropertyPane = ParameterPropertyPane.getInstance(this);  // 传入this的同时会更新参数面板高度
            parameterPropertyPane.refreshState();
            EastRegionContainerPane.getInstance().addParameterPane(parameterPropertyPane);
            EastRegionContainerPane.getInstance().replaceWidgetSettingsPane(
                    WidgetPropertyPane.getInstance(this));
        } else {
            EastRegionContainerPane.getInstance().removeParameterPane();
            showAuthorityEditPane();
        }

    }

    /**
     * 创建权限编辑面板
     *
     * @return 面板
     */
    public AuthorityEditPane createAuthorityEditPane() {
        return new FormWidgetAuthorityEditPane(this);
    }

    /**
     * 内容属性表面板
     *
     * @return 内容属性表面板
     */
    public JPanel getEastUpPane() {
        return WidgetPropertyPane.getInstance(this);
    }

    /**
     * 参数属性表
     *
     * @return 参数属性表
     */

    public JPanel getEastDownPane() {
        return FormWidgetDetailPane.getInstance(this);
    }

    /**
     * 权限编辑面板
     *
     * @return 权限编辑面板
     */
    public AuthorityEditPane getAuthorityEditPane() {
        FormWidgetAuthorityEditPane formWidgetAuthorityEditPane = new FormWidgetAuthorityEditPane(this);
        formWidgetAuthorityEditPane.populateDetials();
        return formWidgetAuthorityEditPane;
    }

    /**
     * 给包含此FormParaDesigner的ParameterDefinitePane添加事件
     *
     * @param paraDefinitePane 面板
     */
    public void addListener(final ParaDefinitePane paraDefinitePane) {
        this.getEditListenerTable().addListener(new DesignerEditListener() {

            @Override
            public void fireCreatorModified(final DesignerEvent evt) {
                if (evt.getCreatorEventID() != DesignerEvent.CREATOR_SELECTED) {
                    paraDefinitePane.setParameterArray(
                            paraDefinitePane.getNoRepeatParas(DesignModelAdapter.getCurrentModelAdapter().getParameters()));
                    paraDefinitePane.refreshParameter();
                }
            }
        });
    }

    /**
     * 包裹一层FormArea
     *
     * @return 区域
     */
    public Component createWrapper() {
        FormArea area = new FormArea(this, false);
        area.setBorder(BorderFactory.createEmptyBorder(0, FORM_AREA_PADDING_LEFT, 0, 0));
        return area;
    }

    /**
     * 刷新控件
     */
    public void refreshAllNameWidgets() {
        XCreatorUtils.refreshAllNameWidgets(this.getRootComponent());
    }

    /**
     * 刷新tableData
     *
     * @param oldName 旧名称f
     * @param newName 新名称
     */
    public void refresh4TableData(String oldName, String newName) {
        this.getTarget().renameTableData(this.getTarget().getContainer(), oldName, newName);
        this.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_SELECTED);
    }

    /**
     * 刷新参数
     *
     * @param p 参数面板
     */
    public void refreshParameter(ParaDefinitePane p) {
        refreshParameter(p, DesignerContext.getDesignerFrame().getSelectedJTemplate());
    }

    /**
     * 刷新参数
     *
     * @param p  参数面板
     * @param jt 当前模版
     */
    public void refreshParameter(ParaDefinitePane p, JTemplate jt) {
        XLayoutContainer rootContainer = this.getRootComponent();
        java.util.List<String> namelist = getAllXCreatorNameList(rootContainer);
        // parameterArray是报表的所有参数, nameList是已经在参数面板添加过控件的参数名
        // 与已有的参数列表比较 如果已经存在 就除去
        Parameter[] ps = p.getParameterArray();
        if (ps != null) {
            for (Parameter parameter : ps) {
                for (String name : namelist) {
                    if (name.equalsIgnoreCase(parameter.getName())) {
                        p.setParameterArray((Parameter[]) ArrayUtils.removeElement(p.getParameterArray(), parameter));
                    }
                }
            }
        }
        ParameterPropertyPane.getInstance().getParameterToolbarPane().populateBean(
                p.getParameterArray() == null ? new Parameter[0] : p.getParameterArray());
        ParameterPropertyPane.getInstance().refreshState(jt);
    }

    /**
     * 判断这个参数面板是否没有控件
     *
     * @return 参数面板是否没有控件
     */
    public boolean isBlank() {
        XLayoutContainer rootContainer = this.getRootComponent();
        List<String> xx = getAllXCreatorNameList(rootContainer);
        return xx.isEmpty();
    }

    protected void setToolbarButtons(boolean flag) {
        DesignerContext.getDesignerFrame().checkCombineUp(!flag, NAME_ARRAY_LIST);
    }

    /**
     * 看看参数面板中的控件是否有和模板参数同名的
     *
     * @param allParameters 参数
     * @return 是否有同名
     */
    public boolean isWithoutParaXCreator(Parameter[] allParameters) {
        XLayoutContainer rootContainer = this.getRootComponent();
        List<String> xx = getAllXCreatorNameList(rootContainer);
        for (Parameter parameter : allParameters) {
            for (String name : xx) {
                if (name.equalsIgnoreCase(parameter.getName())) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 参数面板控件的名字
     *
     * @return 名字
     */
    public List<String> getAllXCreatorNameList() {
        XLayoutContainer rootContainer = this.getRootComponent();
        List<String> namelist = new ArrayList<String>();
        for (int i = 0; i < rootContainer.getXCreatorCount(); i++) {
            if (rootContainer.getXCreator(i) instanceof XLayoutContainer) {
                namelist.addAll(getAllXCreatorNameList((XLayoutContainer) rootContainer.getXCreator(i)));
            } else {
                namelist.add(rootContainer.getXCreator(i).toData().getWidgetName());
            }
        }
        return namelist;
    }

    private List<String> getAllXCreatorNameList(XLayoutContainer rootContainer) {
        List<String> namelist = new ArrayList<String>();
        for (int i = 0; i < rootContainer.getXCreatorCount(); i++) {
            if (rootContainer.getXCreator(i) instanceof XLayoutContainer) {
                namelist.addAll(getAllXCreatorNameList((XLayoutContainer) rootContainer.getXCreator(i)));
            } else {
                namelist.add(rootContainer.getXCreator(i).toData().getWidgetName());
            }
        }
        return namelist;
    }

    /**
     * 是否有查询按钮
     *
     * @return 有无查询按钮
     */
    public boolean isWithQueryButton() {
        XLayoutContainer rootContainer = this.getRootComponent();
        return searchQueryCreators(rootContainer);
    }

    /**
     * 返回复制粘贴删除等动作
     *
     * @return 同上
     */
    public UpdateAction[] getActions() {
        if (designerActions == null) {
            designerActions = new ArrayList<UpdateAction>(Arrays.asList(new UpdateAction[]{new CutAction(this), new CopyAction(this), new PasteAction(this),
                    new FormDeleteAction(this)}));
            dmlActions(designerActions);
        }
        return designerActions.toArray(new UpdateAction[designerActions.size()]);
    }

    private boolean searchQueryCreators(XLayoutContainer rootContainer) {
        boolean b = false;
        for (int i = 0; i < rootContainer.getXCreatorCount(); i++) {
            if (rootContainer.getXCreator(i) instanceof XLayoutContainer) {
                b = searchQueryCreators((XLayoutContainer) rootContainer.getXCreator(i));
            } else if (rootContainer.getXCreator(i) instanceof XFormSubmit) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 就是getTarget 为了返回ParameterUI接口而不冲突另写个
     *
     * @return
     */
    public ParameterUI getParaTarget() {
        return (FormParameterUI) super.getTarget();
    }

    /**
     * ParameterDefinitePane通过ParaDesigner来调用ParameterPropertyPane
     *
     * @param p 面板
     */
    public void populateParameterPropertyPane(ParaDefinitePane p) {
        ParameterPropertyPane.getInstance().populateBean(p);
    }

    /**
     * 初始化
     */
    public void initWidgetToolbarPane() {
        WidgetToolBarPane.getInstance(this);
    }

    /**
     * populate
     *
     * @param ui
     */
    public void populate(ParameterUI ui) {
        if (ui == null) {
            return;
        }
        if (this.getTarget() == ui) {
            repaint();
            return;
        }
        this.setTarget((FormParameterUI) ui.convert());
        this.refreshRoot();
    }

    /**
     * 报表直接判断底层是否是绝对布局
     *
     * @return 是则返回true
     */
    public boolean hasWAbsoluteLayout() {
        return this.getTarget().getContainer() instanceof WAbsoluteLayout;
    }

    /**
     * 刷新底层容器
     */
    public void refreshRoot() {
        XLayoutContainer layoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(this.getTarget()
                .getContainer());
        if (layoutContainer == null) {
            layoutContainer = new XWParameterLayout();
        }
        layoutContainer.setSize(LARGE_PREFERRED_SIZE);
        setRootComponent(layoutContainer);
    }

    /**
     * 是否是报表的参数面板
     *
     * @return 是
     */
    public boolean isFormParaDesigner() {
        return true;
    }

    public XLayoutContainer getParaComponent() {
        return getRootComponent();
    }

    private void paintLinkParameters(Graphics clipg) {
        Parameter[] paras = DesignModelAdapter.getCurrentModelAdapter().getParameters();
        if (paras == null || paras.length == 0) {
            return;
        }
        Graphics g = clipg.create();
        g.setColor(Color.RED);
        if (!(this.getRootComponent() instanceof XWAbsoluteLayout)) {
            return;
        }
        XWAbsoluteLayout layout = (XWAbsoluteLayout) this.getRootComponent();
        for (int i = 0; i < layout.getXCreatorCount(); i++) {
            XCreator creator = layout.getXCreator(i);
            if (!creator.isVisible()) {
                continue;
            }
            for (Parameter p : paras) {
                if (p.getName().equalsIgnoreCase(creator.toData().getWidgetName())) {
                    g.drawImage(paraImage, creator.getX() + PARA_IMAGE_SHIFT_X, creator.getY() + 2, null);
                    break;
                }
            }
        }
    }

    /**
     * 得到合适的大小
     *
     * @return
     */
    public Dimension getPreferredSize() {
        return getDesignSize();
    }

    public Dimension getDesignSize() {
        return ((FormParameterUI) getTarget()).getDesignSize();
    }

    /**
     * 设置高度
     *
     * @param height
     */
    public void setDesignHeight(int height) {
        Dimension dim = getPreferredSize();
        dim.height = height;
        ((FormParameterUI) getTarget()).setDesignSize(dim);
    }

    /**
     * paintContent
     *
     * @param clipg
     */
    public void paintContent(Graphics clipg) {
        Dimension dim;
        dim = ((FormParameterUI) getTarget()).getDesignSize();
        getRootComponent().setSize(dim);
        getRootComponent().paint(clipg);
        paintLinkParameters(clipg);
        paintOp(clipg, getOutlineBounds());
    }

    private void paintOp(Graphics offg, Rectangle bounds) {
        Color oldColor = offg.getColor();
        Insets insets = getOutlineInsets();
        offg.setColor(XCreatorConstants.OP_COLOR);
        offg.fillRect(bounds.x, bounds.y + bounds.height, bounds.width + insets.right, insets.bottom);
        offg.fillRect(bounds.x + bounds.width, bounds.y, insets.right, bounds.height);
        offg.setColor(oldColor);
    }

    protected void setRootComponent(XLayoutContainer component) {
        component.setDirections(new int[]{Direction.BOTTOM, Direction.RIGHT});
        super.setRootComponent(component);
    }

    /**
     * 刷新尺寸
     */
    public void populateRootSize() {
        ((FormParameterUI) getTarget()).setDesignSize(getRootComponent().getSize());
        if (getParaComponent().acceptType(XWParameterLayout.class)) {
            WParameterLayout layout = (WParameterLayout) getParaComponent().toData();
            layout.setDesignWidth(getRootComponent().getWidth());
        }
    }

    /**
     * 保存参数界面的宽度
     *
     * @param width 指定的宽度
     */
    public void updateWidth(int width) {
        FormParameterUI parameterUI = ((FormParameterUI) getTarget());
        parameterUI.setDesignSize(new Dimension(width, parameterUI.getDesignSize().height));
    }

    /**
     * 保存参数界面的高度
     *
     * @param height 指定的高度
     */
    public void updateHeight(int height) {
        FormParameterUI parameterUI = ((FormParameterUI) getTarget());
        parameterUI.setDesignSize(new Dimension(parameterUI.getDesignSize().width, height));
    }

    /**
     * 在参数很多时，全部添加的时候，可以向下一次排版，若去掉就会在参数面板堆到一起
     *
     * @param creator 组件   z
     * @param x       长度
     * @param y       长度     c
     * @param layout  布局
     * @return 是否扩展
     */
    public boolean prepareForAdd(XCreator creator, int x, int y, XWAbsoluteLayout layout) {
        // 参数界面，自动扩展
        if (!isRoot(layout)) {
            return false;
        }

        Dimension size = layout.getSize();
        Boolean needResize = false;

        if (creator.getWidth() / 2 + x > layout.getWidth()) {
            size.width = creator.getWidth() / 2 + x + ADD_HEIGHT;
            needResize = true;
        }
        if (creator.getHeight() / 2 + y > layout.getHeight()) {
            size.height = creator.getHeight() / 2 + y + ADD_HEIGHT;
            needResize = true;
        }
        if (needResize) {
            layout.setSize(size);
            populateRootSize();
        }
        return true;
    }

    /**
     * 加入参数
     *
     * @param parameter    参数        c
     * @param currentIndex 位置   w
     * @return 是否加入   s
     */
    public boolean addingParameter2Editor(Parameter parameter, int currentIndex) {
        com.fr.form.ui.Label label = new com.fr.form.ui.Label();
        String name = parameter.getName();
        label.setWidgetName("Label" + name);
        label.setWidgetValue(new WidgetValue(name + ":"));
        XCreator xCreator = XCreatorUtils.createXCreator(label);
        if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                + FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
            return false;
        }
        EditorHolder editor = new EditorHolder(parameter);
        xCreator = XCreatorUtils.createXCreator(editor);
        if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                + SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
            return false;
        }
        return true;
    }


    /**
     * 加入参数
     *
     * @param parameter    参数        c
     * @param currentIndex 位置   w
     * @return 是否加入   s
     */
    public boolean addingParameter2EditorWithQueryButton(Parameter parameter, int currentIndex) {
        com.fr.form.ui.Label label = new com.fr.form.ui.Label();
        String name = parameter.getName();
        label.setWidgetName("Label" + name);
        label.setWidgetValue(new WidgetValue(name + ":"));
        XCreator xCreator = XCreatorUtils.createXCreator(label);
        if (!(this.autoAddComponent(xCreator, FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return false;
        }
        EditorHolder editor = new EditorHolder(parameter);
        editor.setWidgetName(name);
        xCreator = XCreatorUtils.createXCreator(editor);
        if (!(this.autoAddComponent(xCreator, SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return false;
        }
        FormSubmitButton formSubmitButton = new FormSubmitButton();
        formSubmitButton.setWidgetName("Search");
        formSubmitButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Query"));
        xCreator = XCreatorUtils.createXCreator(formSubmitButton);
        if (!(this.autoAddComponent(xCreator, SUBMIT_BUTTON_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return false;
        }
        return true;
    }

    /**
     * 加入参数
     *
     * @param parameterArray 参数        c
     * @param currentIndex   位置   w
     * @return 是否加入   s
     */
    public void addingAllParameter2Editor(Parameter[] parameterArray, int currentIndex) {
        for (int i = 0; i < parameterArray.length; i++) {
            com.fr.form.ui.Label label = new com.fr.form.ui.Label();
            label.setWidgetName("Label" + parameterArray[i].getName());
            label.setWidgetValue(new WidgetValue(parameterArray[i].getName() + ":"));
            XCreator xCreator = XCreatorUtils.createXCreator(label);

            if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                    + FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
                break;
            }
            // 每行显示5组
            EditorHolder editor = new EditorHolder(parameterArray[i]);
            editor.setWidgetName(parameterArray[i].getName());
            xCreator = XCreatorUtils.createXCreator(editor);
            if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                    + SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
                break;
            }
            currentIndex++;
        }
        if (!isWithQueryButton()) {
            FormSubmitButton formSubmitButton = new FormSubmitButton();
            formSubmitButton.setWidgetName("Search");
            formSubmitButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Query"));
            XCreator xCreator = XCreatorUtils.createXCreator(formSubmitButton);
            if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * 3 + H_GAP, FIRST_V_LOCATION
                    + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
                return;
            }
        }
    }

    /**
     * 自动添加
     *
     * @param xCreator 组件       z
     * @param x        位置    w
     * @param y        位置
     * @return 是否添加   s
     */
    public boolean autoAddComponent(XCreator xCreator, int x, int y) {
        XWAbsoluteLayout layout = (XWAbsoluteLayout) this.getRootComponent();
        FRAbsoluteLayoutAdapter adapter = (FRAbsoluteLayoutAdapter) layout.getLayoutAdapter();
        if (prepareForAdd(xCreator, x, y, layout)) {
            adapter.addBean(xCreator, x, y);
        }
        this.getSelectionModel().setSelectedCreator(xCreator);
        repaint();
        return true;
    }

    /**
     * 工具栏
     *
     * @return 工具栏面板      g
     */
    public JPanel[] toolbarPanes4Form() {
        return new JPanel[]{FormParaPane.getInstance(this)};
    }

    /**
     * 复制等按钮
     *
     * @return 按钮组 a
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[]{new CutAction(this).createToolBarComponent(), new CopyAction(this).createToolBarComponent(), new PasteAction(this).createToolBarComponent(),
                new FormDeleteAction(this).createToolBarComponent()};
    }
}
