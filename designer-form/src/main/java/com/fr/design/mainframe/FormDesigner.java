package com.fr.design.mainframe;

import com.fr.base.Parameter;
import com.fr.base.ScreenResolution;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignState;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.designer.TargetComponent;
import com.fr.design.designer.beans.AdapterBus;
import com.fr.design.designer.beans.Painter;
import com.fr.design.designer.beans.actions.CopyAction;
import com.fr.design.designer.beans.actions.CutAction;
import com.fr.design.designer.beans.actions.FormDeleteAction;
import com.fr.design.designer.beans.actions.MoveDownAction;
import com.fr.design.designer.beans.actions.MoveToBottomAction;
import com.fr.design.designer.beans.actions.MoveToTopAction;
import com.fr.design.designer.beans.actions.MoveUpAction;
import com.fr.design.designer.beans.actions.PasteAction;
import com.fr.design.designer.beans.adapters.layout.FRParameterLayoutAdapter;
import com.fr.design.designer.beans.events.CreatorEventListenerTable;
import com.fr.design.designer.beans.events.DesignerEditListener;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.beans.location.Location;
import com.fr.design.designer.beans.location.RootResizeDirection;
import com.fr.design.designer.beans.models.AddingModel;
import com.fr.design.designer.beans.models.SelectionModel;
import com.fr.design.designer.beans.models.StateModel;
import com.fr.design.designer.creator.XChartEditor;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWAbsoluteBodyLayout;
import com.fr.design.designer.creator.XWAbsoluteLayout;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWParameterLayout;
import com.fr.design.designer.properties.FormWidgetAuthorityEditPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.event.DesignerOpenedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.design.fun.RightSelectionHandlerProvider;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.parameter.ParaDefinitePane;
import com.fr.design.parameter.ParameterPropertyPane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.FormElementCaseContainerProvider;
import com.fr.form.FormElementCaseProvider;
import com.fr.form.main.Form;
import com.fr.form.parameter.FormSubmitButton;
import com.fr.form.ui.EditorHolder;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.form.ui.container.WFitLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.third.javax.annotation.Nullable;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 设计界面组件。该组件是界面设计工具的核心，主要负责的是被设计界面的显示，界面设计操作状态的 显示，编辑状态的显示等等。
 */
public class FormDesigner extends TargetComponent<Form> implements TreeSelectionListener, InvocationHandler, BaseFormDesigner, ParaDefinitePane {
    protected static final ArrayList<String> NAME_ARRAY_LIST = new ArrayList<String>(
            Arrays.asList(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_M_Edit_Cut"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_M_Edit_Copy"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_M_Edit_Delete")})
    );
    //底层容器的默认大小
    protected static final Dimension LARGE_PREFERRED_SIZE = new Dimension(WBorderLayout.DEFAULT_WIDTH, WBorderLayout.DEFAULT_HEIGHT);
    private int paraHeight = 0;
    /**
     * 当前正在设计的组件树的根节点。目前只支持JPanel作为根节点。可以很容易的修改使其支持其他
     * 容器。被设计的组件其name属性都不为空，其值为该组件的变量名称。
     */
    private XLayoutContainer rootComponent;
    private XLayoutContainer paraComponent;
    private boolean drawLineMode;
    private FormArea formArea;
    private ConnectorHelper connectorHelper;
    private boolean isReportBlockEditing = false;

    //组件重叠
    private boolean isWidgetsIntersect = false;

    /**
     * 下面的变量都是非序列化成员，不记录设计状态，只作为设计时临时状态使用。
     */
    // 编辑状态时鼠标处理器
    private transient EditingMouseListener editingMouseListener;
    // 编辑状态下的model，存储编辑状态下的临时状态，比如拖拽区域、鼠标热点等等
    private transient StateModel stateModel;
    // 添加状态下的model，存储添加状态下的临时状态，比如要添加的组件、当前鼠标位置等等
    private transient AddingModel addingModel;
    // 当前负责额外渲染的painter，主要目的用来渲染添加组件的位置提示，它通常由外部类设置，在
    // 设计器渲染时被调用渲染这些位置提示。
    private transient Painter painter;
    // 存储被选择组件和剪切板的model
    private transient SelectionModel selectionModel;

    private int resolution = ScreenResolution.getScreenResolution();
    // 编辑状态的事件表
    private CreatorEventListenerTable edit;
    protected List<UpdateAction> designerActions;
    private FormDesignerModeForSpecial<?> desigerMode;
    private Action switchAction;
    private FormElementCaseContainerProvider elementCaseContainer;
    private Parameter[] parameterArray;
    //控制添加参数位置
    private int currentIndex;
    private static final int NUM_IN_A_LINE = 4;
    private static final int H_COMPONENT_GAP = 165;
    private static final int V_COMPONENT_GAP = 25;
    private static final int FIRST_V_LOCATION = 35;
    private static final int FIRST_H_LOCATION = 90;
    private static final int SECOND_H_LOCATION = 170;
    private static final int ADD_HEIGHT = 20;
    private static final int H_GAP = 105;
    private static final int SUBMIT_BUTTON_H_LOCATION = 270;

    public FormDesigner(Form form) {
        this(form, null);
    }

    public FormDesigner(Form form, Action switchAction) {
        super(form);
        setDoubleBuffered(true);
        // 为了处理键盘事件，需要FormDesigner能够获取焦点
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.setOpaque(true);
        this.setBackground(Color.WHITE);

        // 初始化
        edit = new CreatorEventListenerTable();
        selectionModel = new SelectionModel(this);
        stateModel = new StateModel(this);
        desigerMode = createFormDesignerTargetMode();
        updateUI();// 初始化界面设计工具的UI实例
        refreshRoot();// 初始化缺省的设计组件
        initializeListener();// 初始化事件处理器

        new FormDesignerDropTarget(this);// 添加Drag and Drop.

        this.switchAction = switchAction;

        // 必须刷新"参数/控件树"面板，否则，若最近一次打开模版为 cpt，重启设计器，打开 frm，控件树消失
        populateParameterPropertyPane();
    }

    /**
     * 刷新控件树面板
     */
    public void populateParameterPropertyPane() {
        //参数
        setParameterArray(getNoRepeatParas(getTarget().getParameters()));
        refreshParameter();
        //容器
        ParameterPropertyPane.getInstance().populateBean(this);
    }

    public Parameter[] getNoRepeatParas(Parameter[] paras) {
        List<Parameter> paraList = new ArrayList<Parameter>();
        java.util.Set set = new java.util.HashSet();
        for (Parameter p : paras) {
            if (!set.contains(p.getName().toLowerCase())) {
                paraList.add(p);
                set.add(p.getName().toLowerCase());
            }
        }
        return paraList.toArray(new Parameter[paraList.size()]);
    }

    public void setParameterArray(Parameter[] ps) {
        parameterArray = ps;
    }


    public Parameter[] getParameterArray() {
        return parameterArray;
    }

    /**
     * 刷新参数
     */
    public void refreshParameter() {
        XLayoutContainer rootContainer = this.getParaComponent();
        if (rootContainer != null) {
            java.util.List<String> namelist = new ArrayList<String>();
            rootContainer.getAllXCreatorNameList(rootContainer, namelist);
            // parameterArray是报表的所有参数, nameList是已经在参数面板添加过控件的参数名
            // 与已有的参数列表比较 如果已经存在 就除去
            Parameter[] ps = getParameterArray();
            if (ps != null) {
                removeSame(ps, namelist);
            }
        }
        ParameterPropertyPane.getInstance().getParameterToolbarPane().populateBean(
                getParameterArray() == null ? new Parameter[0] : getParameterArray());
        ParameterPropertyPane.getInstance(this).repaintContainer();  // 传入this的同时会更新参数面板高度
    }

    private void removeSame(Parameter[] parameters, List<String> namelist) {
        for (Parameter parameter : parameters) {
            for (String name : namelist) {
                if (name.equalsIgnoreCase(parameter.getName())) {
                    setParameterArray((Parameter[]) ArrayUtils.removeElement(getParameterArray(), parameter));
                }
            }
        }
    }

    /**
     * 是否有查询按钮
     *
     * @return 有无查询按钮
     */
    public boolean isWithQueryButton() {
        XLayoutContainer rootContainer = this.getParaComponent();
        return rootContainer != null && rootContainer.SearchQueryCreators(rootContainer);
    }

    /**
     * 加入参数到参数面板
     *
     * @param parameter 参数
     * @return 是否加入
     */
    public void addingParameter2Editor(Parameter parameter) {
        if (getParaComponent() == null) {
            addParaPaneTooltips();
            return;
        }
        com.fr.form.ui.Label label = new com.fr.form.ui.Label();
        String name = parameter.getName();
        label.setWidgetName("Label" + name);
        label.setWidgetValue(new WidgetValue(name + ":"));
        XCreator xLabel = XCreatorUtils.createXCreator(label);

        EditorHolder editor = new EditorHolder(parameter);
        XCreator xCreator = XCreatorUtils.createXCreator(editor);

        if (!(this.autoAddComponent(xLabel, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                + FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
            return;
        }
        if (!(this.autoAddComponent(xCreator, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                + SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
            return;
        }
        currentIndex++;
        parameterArray = (Parameter[]) ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
        fireTargetModified();
    }

    /**
     * 加入参数到参数面板，有查询按钮
     *
     * @param parameter 参数
     * @return 是否加入
     */
    public void addingParameter2EditorWithQueryButton(Parameter parameter) {
        if (getParaComponent() == null) {
            addParaPaneTooltips();
            return;
        }
        com.fr.form.ui.Label label = new com.fr.form.ui.Label();
        String name = parameter.getName();
        label.setWidgetName("Label" + name);
        label.setWidgetValue(new WidgetValue(name + ":"));
        XCreator xLabel = XCreatorUtils.createXCreator(label);

        EditorHolder editor = new EditorHolder(parameter);
        editor.setWidgetName(name);
        XCreator xCreator = XCreatorUtils.createXCreator(editor);

        if (!(this.autoAddComponent(xLabel, FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return;
        }

        if (!(this.autoAddComponent(xCreator, SECOND_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return;
        }
        FormSubmitButton formSubmitButton = new FormSubmitButton();
        formSubmitButton.setWidgetName("Search");
        formSubmitButton.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Query"));
        xCreator = XCreatorUtils.createXCreator(formSubmitButton);
        if (!(this.autoAddComponent(xCreator, SUBMIT_BUTTON_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP
                * (currentIndex / NUM_IN_A_LINE)))) {
            return;
        }
        currentIndex = currentIndex + NUM_IN_A_LINE - currentIndex % NUM_IN_A_LINE;
        parameterArray = (Parameter[]) ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
        fireTargetModified();
    }

    /**
     * 一键添加所有参数
     */
    public void addingAllParameter2Editor() {
        if (getParaComponent() == null) {
            addParaPaneTooltips();
            return;
        }
        if (parameterArray == null) {
            return;
        }

        for (int i = 0; i < parameterArray.length; i++) {
            com.fr.form.ui.Label label = new com.fr.form.ui.Label();
            label.setWidgetName("Label" + parameterArray[i].getName());
            label.setWidgetValue(new WidgetValue(parameterArray[i].getName() + ":"));
            XCreator xLabel = XCreatorUtils.createXCreator(label);

            // 每行显示5组
            EditorHolder editor = new EditorHolder(parameterArray[i]);
            editor.setWidgetName(parameterArray[i].getName());
            XCreator xCreator = XCreatorUtils.createXCreator(editor);

            if (!(this.autoAddComponent(xLabel, H_COMPONENT_GAP * (currentIndex % NUM_IN_A_LINE)
                    + FIRST_H_LOCATION, FIRST_V_LOCATION + V_COMPONENT_GAP * (currentIndex / NUM_IN_A_LINE)))) {
                break;
            }

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

        parameterArray = null;

        //parameter多的时候，不刷新会出现控件边界交叉
        refreshRoot();
        // 最后刷新"添加参数面板"和控件树
        refreshParameter();
    }

    private void addParaPaneTooltips() {
        FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Please_Drag_ParaPane"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Alert"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 自动添加组件
     *
     * @param xCreator 组件
     * @param x        横坐标
     * @param y        纵坐标
     * @return 是否添加成功
     */
    public boolean autoAddComponent(XCreator xCreator, int x, int y) {
        XWParameterLayout layout = (XWParameterLayout) this.getParaComponent();
        FRParameterLayoutAdapter adapter = (FRParameterLayoutAdapter) layout.getLayoutAdapter();
        if (prepareForAdd(xCreator, x, y, layout)) {
            adapter.addBean(xCreator, x, y);
        }
        this.getSelectionModel().setSelectedCreator(xCreator);
        repaint();
        return true;
    }

    /**
     * 在参数很多时，全部添加的时候，可以向下一次排版，若去掉就会在参数面板堆到一起
     *
     * @param creator 组件
     * @param x       长度
     * @param y       长度
     * @param layout  布局
     * @return 是否扩展
     */
    public boolean prepareForAdd(XCreator creator, int x, int y, XWParameterLayout layout) {
        // 参数界面，自动扩展
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
            setParaHeight(size.height);
        }
        return true;
    }

    /**
     * 加入参数面板
     */
    public void addParaComponent() {
        if (paraComponent != null) {
            return;
        }
        paraHeight = WBorderLayout.DEFAULT_SIZE;
        paraComponent = new XWParameterLayout();
        paraComponent.toData().setWidgetName("para");
        paraComponent.setSize(paraComponent.initEditorSize());
        XWBorderLayout formLayoutContainer = (XWBorderLayout) rootComponent.getParent();
        formLayoutContainer.toData().setNorthSize(paraHeight);
        formLayoutContainer.add(paraComponent, WBorderLayout.NORTH);
        //设下northSize，增加para后，重置border大小，这时候para和root的大小会自适应调整
        formLayoutContainer.setSize(formLayoutContainer.getWidth(), formLayoutContainer.getHeight() + paraHeight);
        selectionModel.reset();
        selectionModel.setSelectedCreator(paraComponent);
        invalidateLayout();
        populateParameterPropertyPane();
    }

    /**
     * 返回根节点父容器
     *
     * @return 父容器
     */
    public Component getTopContainer() {
        if (rootComponent != null) {
            // 返回root所在的父容器，非designer
            return LayoutUtils.getTopContainer(rootComponent);
        }
        return XCreatorUtils.createXCreator(this.getTarget().getContainer());
    }

    /**
     * 返回参数界面高度
     *
     * @return para高度
     */
    public int getParaHeight() {
        return paraHeight;
    }

    /**
     * 重置para的高度
     *
     * @param height 高度
     */
    public void setParaHeight(int height) {
        XWBorderLayout container = (XWBorderLayout) getTopContainer();
        container.toData().setNorthSize(height);
        container.setSize(container.getWidth(), container.getHeight() + height - getParaHeight());
        paraHeight = height;
    }

    /**
     * 删除参数界面
     */
    public void removeParaComponent() {
        XWBorderLayout formLayoutContainer = (XWBorderLayout) getTopContainer();
        formLayoutContainer.toData().removeWidget(paraComponent.toData());
        paraHeight = 0;
        paraComponent = null;
        formLayoutContainer.setSize(rootComponent.getWidth(), rootComponent.getHeight());
        //atat
        //EastRegionContainerPane.getInstance().addTitlePane(ParameterPropertyPane.getInstance(FormDesigner.this));
        //删除后重绘下
        invalidateLayout();
    }

    /**
     * 切换
     *
     * @param elementCaseContainer 容器
     */
    public void switchTab(FormElementCaseContainerProvider elementCaseContainer) {
        if (this.switchAction == null) {
            return;
        }
        this.elementCaseContainer = elementCaseContainer;
        this.switchAction.actionPerformed(null);
    }

    public void setElementCaseContainer(FormElementCaseContainerProvider elementCaseContainer) {
        this.elementCaseContainer = elementCaseContainer;
    }

    public FormElementCaseProvider getElementCase() {
        return this.elementCaseContainer.getElementCase();
    }

    public String getElementCaseContainerName() {
        return this.elementCaseContainer.getElementCaseContainerName();
    }

    public void setElementCase(FormElementCaseProvider elementCase) {
        this.elementCaseContainer.setElementCase(elementCase);
    }

    public void setElementCaseBackground(BufferedImage image) {
        this.elementCaseContainer.setBackground(image);
    }

    public Dimension getElementCaseContainerSize() {
        return this.elementCaseContainer.getSize();
    }

    public FormElementCaseContainerProvider getElementCaseContainer() {
        return this.elementCaseContainer;
    }

    protected FormDesignerModeForSpecial<?> createFormDesignerTargetMode() {
        return new FormTargetMode(this);
    }

    public FormDesignerModeForSpecial<?> getDesignerMode() {
        return this.desigerMode;
    }

    public CreatorEventListenerTable getEditListenerTable() {
        return edit;
    }

    /**
     * 增加监听事件
     *
     * @param listener 界面组件编辑事件
     */
    public void addDesignerEditListener(DesignerEditListener listener) {
        getEditListenerTable().addListener(listener);
    }

    /**
     * 取消格式
     */
    public void cancelFormat() {
        return;
    }

    /**
     * 表单则判断参数面板是否为绝对布局
     *
     * @return 是则返回true
     */
    public boolean hasWAbsoluteLayout() {
        if (paraComponent != null && paraComponent.acceptType(XWParameterLayout.class)) {
            return true;
        } else {
            if (this.getSelectionModel().getSelection().getSelectedCreator().getParent() != null
                    && ((XLayoutContainer) this.getSelectionModel().getSelection().getSelectedCreator().getParent()).acceptType(XWAbsoluteLayout.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置是否为报表块编辑
     *
     * @param isEditing 是否为报表块编辑
     */
    public void setReportBlockEditing(boolean isEditing) {
        this.isReportBlockEditing = isEditing;
    }

    /**
     * 是否为报表块编辑
     *
     * @return 是否为报表块编辑
     */
    public boolean isReportBlockEditing() {
        return this.isReportBlockEditing;
    }

    public void setWidgetsIntersect(boolean isWidgetsIntersect) {
        this.isWidgetsIntersect = isWidgetsIntersect;
    }

    public boolean isWidgetsIntersect() {
        return this.isWidgetsIntersect;
    }

    /**
     * 是否重命名控件
     *
     * @param creator 组件
     * @param newName 新的组件名
     * @return 组件名有变化，且不和其他一样返回true
     */
    public boolean renameCreator(XCreator creator, String newName) {
        if (ComparatorUtils.equals(creator.toData().getWidgetName(), newName)) {
            return false;
        }
        if (this.getTarget().isNameExist(newName)) {
            FineJOptionPane.showMessageDialog(this, "\"" + newName + "\"" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_Has_Been_Existed")
                    + "!", com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Alert"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
        creator.resetCreatorName(newName);
        getEditListenerTable().fireCreatorModified(creator, DesignerEvent.CREATOR_EDITED);
        return true;
    }

    public void showMessageDialog(String message) {
        FineJOptionPane.showMessageDialog(this, message, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Alert"), JOptionPane.WARNING_MESSAGE);
        FormSelectionUtils.rebuildSelection(this);
        repaint();
    }

    /**
     * 保存参数界面的宽度
     *
     * @param width 指定的宽度
     */
    public void updateWidth(int width) {
        //TODO
    }

    /**
     * 更新界面布局，重绘
     *
     * @param proxy  动态代理类
     * @param method 接口方法
     * @param args   参数
     * @return 不返回任何对象
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (rootComponent != null) {
            LayoutUtils.layoutRootContainer(rootComponent);
        }
        if (paraComponent != null) {
            LayoutUtils.layoutRootContainer(paraComponent);
        }
        repaint();
        return null;
    }

    /**
     * 初始化事件处理器，初始状态为编辑状态，所以下初始化并添加编辑类的事件处理器
     */
    private void initializeListener() {
        addKeyListener(new FormEditorKeyListener(this));
        // 点击
        editingMouseListener = new EditingMouseListener(this);
        addMouseMotionListener(editingMouseListener);
        addMouseListener(editingMouseListener);
        addInvocationHandler(this);

        getEditListenerTable().addListener(new DesignerEditListener() {

            @Override
            public void fireCreatorModified(DesignerEvent evt) {
                // 只有选择组件时不触发模版更新，其他都要触发
                if (evt.getCreatorEventID() != DesignerEvent.CREATOR_SELECTED) {
                    FormDesigner.this.fireTargetModified();
                    if (evt.getCreatorEventID() == DesignerEvent.CREATOR_DELETED) {
                        setParameterArray(getNoRepeatParas(getTarget().getParameters()));
                        refreshParameter();
                    }
                } else {
                    for (UpdateAction action : getActions()) {
                        action.update();
                    }
                }
            }

        });

        DesignerContext.getDesignerFrame().addDesignerOpenedListener(new DesignerOpenedListener() {
            @Override
            public void designerOpened() {
                setToolbarButtons();
            }
        });
    }

    /**
     * 增加组件事件
     *
     * @param h 动态代理
     */
    public void addInvocationHandler(InvocationHandler h) {
        ClassLoader loader = getClass().getClassLoader();
        Class[] interfaces = new Class[]{DesignerEditListener.class};
        Object proxyListener = Proxy.newProxyInstance(loader, interfaces, h);
        addDesignerEditListener((DesignerEditListener) proxyListener);
        addDesignerEditListener(new FormWidgetAuthorityEditAdapter());
    }

    /**
     * 停止添加模式、返回编辑模式
     */
    public void stopAddingState() {
        // 恢复为空，UI类根据addingModel是否空决定是否停止渲染要添加的组件
        addingModel = null;
        painter = null;
        // DropTarget依然是addingMouseListener,改成这个,数据源拖拽用
        new FormDesignerDropTarget(this);
        // 触发停止添加模式的事件
        repaint();
    }

    /**
     * 设置其UI类为DesignerUI，负责渲染
     */
    @Override
    public void updateUI() {
        setUI(new FormDesignerUI());
    }

    /**
     * 在拖拽区域选择方式鼠标释放时调用此函数来更新所选择的组件
     *
     * @param e 当前鼠标事件，用来和起始点构成选择框，计算被圈入的组件
     */
    public void selectComponents(MouseEvent e) {
        // 调用stateModel的selectComponent更新被选择的组件，stateModel定义了拖拽起始点
        stateModel.selectCreators(e);
        // 清除stateModel为非拖拽状态
        stateModel.reset();
        repaint();
    }

    /**
     * 从root组件递归查找x,y所在的组件，注意是正在被设计的组件，因此其name属性必须不为空
     */
    private XCreator xCreatorAt(int x, int y, XCreator root, XCreator[] except) {
        if (root == null || !root.isVisible()) {
            return null;
        }
        x -= root.getX();
        y -= root.getY();

        if (root instanceof XLayoutContainer) {
            XLayoutContainer rootContainer = (XLayoutContainer) root;
            int count = rootContainer.getXCreatorCount();
            for (int i = 0; i < count; i++) {
                XCreator child = rootContainer.getXCreator(i);
                if (ArrayUtils.contains(except, child)) {
                    continue;
                }
                // 只有name不为空的组件才是搜索范围，这儿递归下溯调用
                XCreator dest = xCreatorAt(x, y, child, except);

                if (dest != null && !ArrayUtils.contains(except, child)) {
                    return dest;
                }
            }
        }

        Rectangle rect = ComponentUtils.computeVisibleRect(root);
        if (isIntersectArea(x, y, rect)) {
            // 判断是否处于交叉区域
            return root;
        }

        return null;
    }

    private boolean isIntersectArea(int x, int y, Rectangle rect) {
        return x >= rect.getX() && (x <= (rect.getX() + rect.getWidth())) && (y >= rect.getY())
                && (y <= (rect.getY() + rect.getHeight()));
    }

    /**
     * 更新边框线状态
     *
     * @param e 鼠标事件
     */
    public void updateDrawLineMode(MouseEvent e) {
        Point p = connectorHelper.getNearWidgetPoint(e);
        if (p == null && getComponentAt(e) == rootComponent) {
            p = new Point(e.getX() + formArea.getHorizontalValue(), e.getY() + formArea.getVerticalValue());
        }
        stateModel.startDrawLine(p);
    }

    /**
     * 刷新底层容器
     */
    public void refreshRoot() {
        // 撤销恢复操作都会refreshRoot，这时候的target.getContainer里的widget会和之前不一样，所以不用root判断来取
        XLayoutContainer formLayoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(this.getTarget().getContainer());
        // 布局默认都是1，底层的border改为0，不然没意义
        this.getTarget().getContainer().setMargin(new PaddingMargin(0, 0, 0, 0));
        formLayoutContainer.setBorder(null);
        if (formLayoutContainer.acceptType(XWBorderLayout.class)) {
            WBorderLayout borderLayout = (WBorderLayout) formLayoutContainer.toData();

            Widget northWidget = borderLayout.getLayoutWidget(WBorderLayout.NORTH);
            Widget centerWidget = borderLayout.getLayoutWidget(WBorderLayout.CENTER);
            //本身含有，这儿得先清空再加
            formLayoutContainer.removeAll();

            refreshNorth(northWidget, formLayoutContainer);
            refreshCenter(centerWidget, formLayoutContainer);

        } else {
            formLayoutContainer.setSize(LARGE_PREFERRED_SIZE);
            setRootComponent(formLayoutContainer);
        }
    }

    private void refreshNorth(Widget northWidget, XLayoutContainer formLayoutContainer) {
        // 如果没有参数界面, 那么就处理下高度以及参数界面的按钮要点亮
        if (northWidget == null) {
            paraComponent = null;
            paraHeight = 0;
            FormParaWidgetPane.getInstance(this);
            return;
        }

        XLayoutContainer northContainer = (XLayoutContainer) XCreatorUtils.createXCreator(northWidget);
        paraHeight = ((XWBorderLayout) formLayoutContainer).toData().getNorthSize();
        paraComponent = northContainer;
        northContainer.setSize(0, paraHeight);
        formLayoutContainer.add(northContainer, WBorderLayout.NORTH);
    }

    private void refreshCenter(Widget centerWidget, XLayoutContainer formLayoutContainer) {
        // 不存在center块, 说明是新建的模板
        if (centerWidget == null) {
            XLayoutContainer layoutContainer = (XLayoutContainer) XCreatorUtils.createXCreator(new WFitLayout("body"));
            layoutContainer.setSize(LARGE_PREFERRED_SIZE);
            setRootComponent(layoutContainer);
            formLayoutContainer.add(rootComponent, WBorderLayout.CENTER);
            return;
        }

        XLayoutContainer centerContainer = (XLayoutContainer) XCreatorUtils.createXCreator(centerWidget);
        Dimension d = new Dimension();
        d.width = ((WFitLayout) centerWidget).getContainerWidth();
        d.height = ((WFitLayout) centerWidget).getContainerHeight();
        centerContainer.setSize(d);
        formLayoutContainer.setSize(d.width, d.height + paraHeight);
        setRootComponent(centerContainer);
        // 再次打开时，layout下root，有内边距的话组件加上
        LayoutUtils.layoutContainer(centerContainer);
        formLayoutContainer.add(rootComponent, WBorderLayout.CENTER);
    }


    protected Insets getOutlineInsets() {
        return new Insets(10, 10, 10, 10);
    }

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(Painter p) {
        painter = p;
    }

    public XLayoutContainer getRootComponent() {
        return rootComponent;
    }

    public XLayoutContainer getParaComponent() {
        return paraComponent;
    }

    /**
     * 是否是报表的参数面板
     *
     * @return 否(表单的)
     */
    public boolean isFormParaDesigner() {
        return false;
    }

    /**
     * 是否为底层容器
     *
     * @param comp 组件
     * @return 是则返回true
     */
    public boolean isRoot(XCreator comp) {
        return comp != null && (comp == rootComponent || comp.acceptType(XWAbsoluteBodyLayout.class));
    }

    // 计算鼠标事件e所发生的位置相对根组件的位置关系
    public Direction getLoc2Root(MouseEvent e) {
        int x = e.getX() + formArea.getHorizontalValue();
        int y = e.getY() + formArea.getVerticalValue();
        int width = rootComponent.getWidth();
        int height = rootComponent.getHeight();

        Insets insets = getOutlineInsets();
        if (x < width) {
            if ((y >= height) && (y <= (height + insets.bottom))) {
                return RootResizeDirection.BOTTOM_RESIZE;
            } else {
                return Location.outer;
            }
        } else if (x <= (width + insets.right)) {
            if ((y >= 0) && (y < height)) {
                return RootResizeDirection.RIGHT_RESIZE;
            } else if ((y >= height) && (y <= (height + insets.bottom))) {
                return RootResizeDirection.RIGHT_BOTTOM_RESIZE;
            } else {
                return Location.outer;
            }
        } else {
            return Location.outer;
        }
    }

    protected void setRootComponent(XLayoutContainer component) {
        this.rootComponent = component;
        component.setOpaque(true);
        component.setBackground(XCreatorConstants.FORM_BG);
        selectionModel.reset();
        // 默认还是选中RootPane吧
        selectionModel.setSelectedCreator(rootComponent);
        invalidateLayout();
    }

    public StateModel getStateModel() {
        return stateModel;
    }

    public AddingModel getAddingModel() {
        return addingModel;
    }

    public XCreator getComponentAt(MouseEvent e) {
        return getComponentAt(e.getX(), e.getY());
    }

    @Override
    public XCreator getComponentAt(Point p) {
        return getComponentAt(p.x, p.y);
    }

    @Override
    public XCreator getComponentAt(int x, int y) {
        return getComponentAt(x, y, null);
    }

    @Nullable
    public XCreator getComponentAt(int x, int y, XCreator[] except) {
        XLayoutContainer container = y < paraHeight - formArea.getVerticalValue() ? paraComponent : rootComponent;
        XCreator comp = xCreatorAt(x + formArea.getHorizontalValue(), y + formArea.getVerticalValue(), container,
                except);
        return comp == null ? container : comp;
    }

    public SelectionModel getSelectionModel() {
        if (paraComponent != null) {
            paraComponent.setSize(paraComponent.getWidth(), getParaHeight());
            Rectangle rec = rootComponent.getBounds();
            rootComponent.setBounds(rec.x, getParaHeight(), rec.width, rec.height);
        }
        return selectionModel;
    }

    /**
     * 移除选中状态
     */
    public void removeSelection() {
        selectionModel.reset();
        this.repaint();
    }

    /**
     * 拖拽准备
     *
     * @param xCreator 组件
     */
    public void startDraggingBean(XCreator xCreator) {
        // 根据所选择的组件的BeanInfo生成相应的AddingModel
        // AddingModel和StateModel不一样，适合当前选择的组件相关的
        addingModel = new AddingModel(this, xCreator);
        this.setDropTarget(new FormCreatorDropTarget(this));
        // 触发状态添加模式事件
        repaint();
    }

    /**
     * 拖拽时相关处理
     *
     * @param xCreator       组件
     * @param lastPressEvent 鼠标事件
     * @param x              坐标x
     * @param y              坐标y
     */
    public void startDraggingComponent(XCreator xCreator, MouseEvent lastPressEvent, int x, int y) {
        // 根据所选择的组件的BeanInfo生成相应的AddingModel
        // AddingModel和StateModel不一样，适合当前选择的组件相关的
        int creatorWidth = xCreator.getWidth();
        int creatorHeight = xCreator.getHeight();
        this.addingModel = new AddingModel(xCreator, x, y);
        TransferHandler handler = new DesignerTransferHandler(this, addingModel);
        setTransferHandler(handler);
        handler.exportAsDrag(this, lastPressEvent, TransferHandler.COPY);
        XCreator parent = XCreatorUtils.getParentXLayoutContainer(xCreator);
        selectionModel.removeCreator(xCreator, creatorWidth, creatorHeight);
        selectionModel.setSelectedCreator(parent);
        this.setDropTarget(new FormCreatorDropTarget(this));
        // 触发状态添加模式事件
        repaint();
    }

    /**
     * 改变组件值
     *
     * @param e 组件选择事件
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (DesignerContext.getDesignerFrame().getSelectedJTemplate() == null) {  // 初始化完成前，不响应事件
            return;
        }
        ComponentTree tree = (ComponentTree) e.getSource();
        TreePath[] paths = tree.getSelectionPaths();

        if (paths != null) {
            ArrayList<XCreator> selected = new ArrayList<XCreator>();

            for (TreePath path : paths) {
                selected.add((XCreator) path.getLastPathComponent());
            }

            if (!DesignerMode.isAuthorityEditing()) {
                selectionModel.setSelectedCreators(selected);

                if (formArea != null) {
                    TreePath path = e.getNewLeadSelectionPath();
                    XCreator comp = (XCreator) path.getLastPathComponent();
                    formArea.scrollPathToVisible(comp);
                }
            } else {
                showAuthorityEditPane();
            }
            //先选中再检查
            setToolbarButtons();
        }
    }

    /**
     * 是否选中了自适应布局或底层form
     */
    public boolean isRootSelected() {
        ComponentTree tree = FormHierarchyTreePane.getInstance().getComponentTree();
        TreePath[] paths = tree.getSelectionPaths();
        if (paths == null) {
            return true;
        }
        boolean isForm = paths.length == 1 && tree.getSelectionPath().getParentPath() == null;
        return isForm || isRoot(getSelectionModel().getSelection().getSelectedCreator());
    }

    /**
     * 显示权限编辑界面
     */
    public void showAuthorityEditPane() {
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(true);
        if (isSupportAuthority()) {
            AuthorityPropertyPane authorityPropertyPane = new AuthorityPropertyPane(this);
            authorityPropertyPane.populate();
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION);
            EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(authorityPropertyPane);
        } else {
            EastRegionContainerPane.getInstance().switchMode(EastRegionContainerPane.PropertyMode.AUTHORITY_EDITION_DISABLED);
            EastRegionContainerPane.getInstance().replaceAuthorityEditionPane(new NoSupportAuthorityEdit());
        }
        EastRegionContainerPane.getInstance().replaceConfiguredRolesPane(RolesAlreadyEditedPane.getInstance());
    }

    /**
     * 是否支持权限编辑
     *
     * @return 是则返回true
     */
    public boolean isSupportAuthority() {
        int size = getSelectionModel().getSelection().size();
        XCreator creator = size == 0 ? getRootComponent() : getSelectionModel().getSelection()
                .getSelectedCreator();
        return !(creator instanceof XLayoutContainer) && !(creator instanceof XChartEditor);
    }


    protected void setToolbarButtons() {
        //自适应布局和底层都不能删除
        DesignerContext.getDesignerFrame().checkCombineUp(!isRootSelected(), NAME_ARRAY_LIST);
    }

    private void invalidateLayout() {
        Container parent = this.getArea();
        if (parent != null) {
            parent.doLayout();
            parent.repaint();
        }
    }

    /**
     * 是否含有action名
     *
     * @param name action名
     * @return 有则返回true
     */
    public boolean isRootRelatedAction(String name) {
        return NAME_ARRAY_LIST.contains(name);
    }

    /**
     * 显示组件
     *
     * @param comp 组件
     */
    public void makeVisible(XCreator comp) {
        XCreator parent = AdapterBus.getFirstInvisibleParent(comp);
        if (isRoot(parent)) {
            return;
        }
        while (parent != null) {
            XLayoutContainer parentContainer = XCreatorUtils.getParentXLayoutContainer(parent);

            if (parentContainer == null) {
                // parent.setVisible(true);
                break;
            } else {
                parentContainer.getLayoutAdapter().showComponent(parent);
                parent = AdapterBus.getFirstInvisibleParent(parent);
            }
        }
    }

    /**
     * 刷新界面渲染容器
     */
    public void refreshDesignerUI() {
        LayoutUtils.layoutRootContainer(getRootComponent());
        repaint();
    }

    /**
     * 返回复制粘贴删除等动作
     * 鼠标右键菜单
     *
     * @return 同上
     */
    public UpdateAction[] getActions() {
        if (designerActions == null) {
            designerActions = new ArrayList<UpdateAction>(Arrays.asList(new UpdateAction[]{new CutAction(this), new CopyAction(this), new PasteAction(this),
                    new FormDeleteAction(this), new MoveToTopAction(this), new MoveToBottomAction(this),
                    new MoveUpAction(this), new MoveDownAction(this)}));
            dmlActions(designerActions);
        }
        return designerActions.toArray(new UpdateAction[designerActions.size()]);
    }

    /**
     * 扩展菜单项
     *
     * @param actions
     */
    public void dmlActions(List<UpdateAction> actions) {
        try {
            Set<RightSelectionHandlerProvider> selectionHandlerProviders = ExtraDesignClassManager.getInstance().getArray(RightSelectionHandlerProvider.XML_TAG);
            for (RightSelectionHandlerProvider handler : selectionHandlerProviders) {
                if (handler.accept(this)) {
                    handler.dmlUpdateActions(this, actions);
                }
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    // 当前选中控件可以上移一层吗？
    public boolean isCurrentComponentMovableUp() {
        XCreator creator = getSelectionModel().getSelection().getSelectedCreator();
        XLayoutContainer container = (XLayoutContainer) creator.getParent();
        if (container == null || !container.supportInnerOrderChangeActions()) {
            return false;
        }
        return creator.isMovable() && container.getComponentZOrder(creator) > 0;
    }

    // 当前选中控件可以下移一层吗？
    public boolean isCurrentComponentMovableDown() {
        XCreator creator = getSelectionModel().getSelection().getSelectedCreator();
        XLayoutContainer container = (XLayoutContainer) creator.getParent();
        if (container == null || !container.supportInnerOrderChangeActions()) {
            return false;
        }
        return creator.isMovable() && container.getComponentZOrder(creator) < container.getComponentCount() - 1;
    }

    protected Border getOuterBorder() {
        return XCreatorConstants.AREA_BORDER;
    }

    protected Rectangle getOutlineBounds() {
        Insets insets = getOuterBorder().getBorderInsets(this);
        int w = rootComponent.getWidth() + insets.left + insets.right;
        int h = rootComponent.getHeight() + insets.top + insets.bottom;
        return new Rectangle(0, 0, w, h);
    }

    /**
     * 同步
     */
    public void populateRootSize() {
        // do nothing
    }

    /**
     * 返回表单区域
     *
     * @return 表单区域
     */
    public FormArea getArea() {
        return formArea;
    }

    /**
     * 设置上层区域
     *
     * @param formArea 表单区域
     */
    public void setParent(FormArea formArea) {
        this.formArea = formArea;
    }

    /**
     * 绘制组件根节点
     *
     * @param clipg 图形
     */
    public void paintContent(Graphics clipg) {
        rootComponent.paint(clipg);
    }

    public void paintPara(Graphics clipg) {
        if (paraComponent != null) {
            paraComponent.paint(clipg);
        }
    }

    /**
     * 重置组件边界
     */
    public void resetEditorComponentBounds() {
        editingMouseListener.resetEditorComponentBounds();
    }

    /**
     * 返回连线类
     *
     * @return ConnectorHelper类
     */
    public ConnectorHelper getDrawLineHelper() {
        return connectorHelper;
    }

    /**
     * 是否画线模式
     *
     * @return 是则返回true
     */
    public boolean isDrawLineMode() {
        return this.drawLineMode;
    }

    /**
     * 设置DrawLineMode
     *
     * @param mode 是or或
     */
    public void setDrawLineMode(boolean mode) {
        this.drawLineMode = mode;
    }

    /**
     * 鼠标按定位置(evtX, evtY).
     *
     * @param evtX event x position 坐标
     * @param evtY event y position 坐标
     */
    public void doMousePress(double evtX, double evtY) {
        dispatchEvent(new MouseEvent(this, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, (int) evtX,
                (int) evtY, 1, false));
    }

    /**
     * TODO ALEX_SEP 暂时不做什么事
     */
    @Override
    public void stopEditing() {
        // do nothing
    }

    public void stopEditing(TreePath path) {
        // do nothing
        XCreator comp = (XCreator) path.getLastPathComponent();
        editingMouseListener.stopEditing();
        editingMouseListener.stopEditTopLayout(comp);

        editingMouseListener.getSelectionModel().reset();
        editingMouseListener.getSelectionModel().selectACreator(comp);
    }

    /**
     * 返回表单控件权限编辑pane
     *
     * @return 同上
     */
    public AuthorityEditPane createAuthorityEditPane() {
        return new FormWidgetAuthorityEditPane(this);
    }

    public JPanel getEastUpPane() {
        return WidgetPropertyPane.getInstance(this);
    }

    public JPanel getEastDownPane() {
        final JPanel pane = new JPanel();
        if (EastRegionContainerPane.getInstance().getDownPane() == null) {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        FRLogger.getLogger().error(e.getMessage(), e);
                        Thread.currentThread().interrupt();
                    }

                    pane.setLayout(new BorderLayout());
                    pane.add(FormWidgetDetailPane.getInstance(FormDesigner.this), BorderLayout.CENTER);
                    EastRegionContainerPane.getInstance().replaceConfiguredRolesPane(pane);
                }
            }.start();
        } else {
            pane.setLayout(new BorderLayout());
            pane.add(FormWidgetDetailPane.getInstance(this), BorderLayout.CENTER);
            EastRegionContainerPane.getInstance().replaceConfiguredRolesPane(pane);
        }

        return pane;
    }


    public ToolBarMenuDockPlus getToolBarMenuDockPlus() {
        return StableFactory.getMarkedInstanceObjectFromClass(BaseJForm.XML_TAG, ToolBarMenuDockPlus.class);
    }

    /**
     * 复制
     */
    @Override
    public void copy() {
        if (DesignModeContext.isBanCopyAndCut()) {
            return;
        }
        selectionModel.copySelectedCreator2ClipBoard();
    }

    /**
     * 粘贴
     *
     * @return 否
     */
    @Override
    public boolean paste() {
        selectionModel.pasteFromClipBoard();
        return false;
    }

    /**
     * 剪切
     *
     * @return 否
     */
    @Override
    public boolean cut() {
        if (DesignModeContext.isBanCopyAndCut()) {
            return false;
        }
        selectionModel.cutSelectedCreator2ClipBoard();
        return false;
    }

    // ////////////////////////////////////////////////////////////////////
    // ////////////////for toolbarMenuAdapter//////////////////////////////
    // ////////////////////////////////////////////////////////////////////

    /**
     * 工具栏菜单
     *
     * @return 同上
     */
    @Override
    public MenuDef[] menus4Target() {
        return new MenuDef[0];
    }

    public int getMenuState() {
        return DesignState.JFORM;
    }

    /**
     * 模版菜单
     *
     * @return 同上
     */
    @Override
    public ShortCut[] shortcut4TemplateMenu() {
        return new ShortCut[0];
    }

    /**
     * 权限菜单
     *
     * @return 同上
     */
    public ShortCut[] shortCuts4Authority() {
        return new ShortCut[0];

    }

    /**
     * 返回ToolBarDef
     *
     * @return 同上
     */
    @Override
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * 返回工具栏按钮组件
     *
     * @return 同上
     */
    public JComponent[] toolBarButton4Form() {
        return new JComponent[]{new CutAction(this).createToolBarComponent(), new CopyAction(this).createToolBarComponent(), new PasteAction(this).createToolBarComponent(),
                new FormDeleteAction(this).createToolBarComponent()};
    }


    private class FormWidgetAuthorityEditAdapter implements DesignerEditListener {

        @Override
        public void fireCreatorModified(DesignerEvent evt) {
            if (!DesignerMode.isAuthorityEditing()) {
                return;
            }
            if (evt.getCreatorEventID() == DesignerEvent.CREATOR_EDITED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_DELETED
                    || evt.getCreatorEventID() == DesignerEvent.CREATOR_SELECTED) {

                TreePath paths = getSelectedTreePath();

                if (paths == null) {
                    return;
                }
                if (DesignerMode.isAuthorityEditing()) {
                    showAuthorityEditPane();
                }

            } else if (evt.getCreatorEventID() == DesignerEvent.CREATOR_RESIZED) {
                repaint();
            }
        }


        public TreePath getSelectedTreePath() {
            XCreator creator = selectionModel.getSelection().getSelectedCreator();
            return buildTreePath(creator);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof FormWidgetAuthorityEditAdapter;
        }

        private TreePath buildTreePath(Component comp) {
            ArrayList<Component> path = new ArrayList<Component>();
            Component parent = comp;

            while (parent != null) {
                path.add(0, parent);
                parent = parent.getParent();
            }

            Object[] components = (Object[]) path.toArray();
            if (components.length == 0) {
                return null;
            }

            return new TreePath(components);
        }

    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getResolution() {
        return this.resolution;
    }
}
