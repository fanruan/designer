package com.fr.design.parameter;

import com.fr.base.BaseUtils;
import com.fr.base.Parameter;
import com.fr.base.parameter.ParameterUI;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignModelAdapter;
import com.fr.design.DesignState;
import com.fr.design.actions.AllowAuthorityEditAction;
import com.fr.design.actions.ExitAuthorityEditAction;
import com.fr.design.actions.edit.RedoAction;
import com.fr.design.actions.edit.UndoAction;
import com.fr.design.actions.file.SaveAsTemplateAction;
import com.fr.design.actions.file.SaveTemplateAction;
import com.fr.design.actions.report.ReportParameterAction;
import com.fr.design.designer.TargetComponent;
import com.fr.design.event.TargetModifiedEvent;
import com.fr.design.event.TargetModifiedListener;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.AuthorityToolBarPane;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.NameSeparator;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.style.background.BackgroundPane;
import com.fr.log.FineLoggerFactory;
import com.fr.main.parameter.ReportParameterAttr;
import com.fr.main.parameter.TemplateParameterAttr;
import com.fr.stable.ArrayUtils;
import com.fr.stable.bridge.StableFactory;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 整体的参数设计面板
 *
 * @editor zhou
 * @since 2012-3-23下午3:36:52
 */

//TODO:一切关于setbutton的删除掉
public class ParameterDefinitePane extends JPanel implements ToolBarMenuDockPlus, ParaDefinitePane {
    private static final int NUM_IN_A_LINE = 4;
    private Parameter[] parameterArray;
    //    private FormParaDesigner formParaDesignEditor;
    private ParameterDesignerProvider paraDesignEditor;
    private PropertyChangeAdapter propertyChangeListener;
    // 用于添加时记住位置,每行五组
    private int currentIndex;
    private Parameter[] allParameters;
    private UIButtonGroup<Integer> bg;
    private UIButton setButton;
    private JCheckBoxMenuItem isshowWindowItem;
    private JCheckBoxMenuItem isdelayItem;
    private JPopupMenu jPopupMenu;
    private BackgroundPane bgPane;

    private boolean isEditing;

    private static final int TOOLBARPANEDIMHEIGHT = 26;

    private JWorkBook workBook;

    public ParameterDefinitePane() {
        this.setBorder(null);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        setComponentBg(this);
        paraDesignEditor = DesignModuleFactory.getFormParaDesigner();
        if (paraDesignEditor == null) {
            return;
        }
        paraDesignEditor.initWidgetToolbarPane();

        this.add(paraDesignEditor.createWrapper(), BorderLayout.CENTER);

        setButton = new UIButton(BaseUtils.readIcon("com/fr/design/images/toolbarbtn/parametersetting.png"));
        setButton.set4ToolbarButton();
        isshowWindowItem = new JCheckBoxMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ParameterD_Show_Parameter_Window"));
        isdelayItem = new JCheckBoxMenuItem(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ParameterD_Delay_Playing"));

        initListeners();
    }

    private void initListeners() {
        ((TargetComponent) paraDesignEditor).addTargetModifiedListener(new TargetModifiedListener() {
            @Override
            public void targetModified(TargetModifiedEvent e) {
                if (isEditing) {
                    workBook.updateReportParameterAttr();
                    workBook.fireTargetModified();
                }
            }
        });
        paraDesignEditor.addListener(this);

        propertyChangeListener = new PropertyChangeAdapter() {
            @Override
            public void propertyChange() {
                if (isEditing) {
                    workBook.updateReportParameterAttr();
                    workBook.fireTargetModified();
                }
            }
        };

        isshowWindowItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (propertyChangeListener != null) {
                    propertyChangeListener.propertyChange();
                }
            }
        });

        isdelayItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (propertyChangeListener != null) {
                    propertyChangeListener.propertyChange();
                }

            }
        });

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jPopupMenu.show(setButton, 0, 20);
            }
        });
    }

    /**
     * 初始化
     */
    public void initBeforeUpEdit() {
        paraDesignEditor.initBeforeUpEdit();
    }

    /**
     * set Component Background
     *
     * @param cc
     */
    public void setComponentBg(Container cc) {
        for (Component com : cc.getComponents()) {
            com.setBackground(new Color(212, 212, 216));
            if (com instanceof Container) {
                setComponentBg((Container) com);
            }
        }
    }

    /**
     * get formParaDesignEditor
     *
     * @return
     */
    public ParameterDesignerProvider getParaDesigner() {
        return paraDesignEditor;
    }

    /**
     * 获取默认大小
     *
     * @return
     */
    public Dimension getPreferredSize() {
        return paraDesignEditor.getPreferredSize();
    }

    /**
     * set height
     *
     * @param height
     */
    public void setDesignHeight(int height) {
        paraDesignEditor.setDesignHeight(height);
    }

    public Dimension getDesignSize() {
        return paraDesignEditor.getDesignSize();
    }

    public void setParameterArray(Parameter[] ps) {
        parameterArray = ps;
    }

    public Parameter[] getParameterArray() {
        return parameterArray;
    }

    public int getToolBarHeight(){
        return TOOLBARPANEDIMHEIGHT;
    }

    /**
     * 导出菜单项，用于图表设计器
     * @return 菜单项
     */
    public ShortCut[] shortcut4ExportMenu() {
        return new ShortCut[0];
    }

    /**
     * populate
     *
     * @param workBook
     */
    public void populate(final JWorkBook workBook) {
        isEditing = false;
        this.workBook = workBook;
        ReportParameterAttr reportParameterAttr = workBook.getTarget().getReportParameterAttr();
        if (reportParameterAttr == null) {
            reportParameterAttr = new ReportParameterAttr();
            reportParameterAttr.setShowWindow(true);
        }

        // formParaDesignEditor.populate()需要在refreshParameter()之前执行,不然会使refreshParameter中已经添加的参数判定出问题
        ParameterUI parameterUI = reportParameterAttr.getParameterUI();
        if (parameterUI == null) {
            try {
                parameterUI = StableFactory.getMarkedInstanceObjectFromClass(ParameterUI.FORM_XML_TAG, ParameterUI.class);
                parameterUI.setDefaultSize();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }

        paraDesignEditor.populate(parameterUI);

        parameterArray = getNoRepeatParas(getTargetParameter(workBook));
        refreshParameter();
        allParameters = reportParameterAttr.getParameters();

        paraDesignEditor.populateParameterPropertyPane(this);
        isdelayItem.setSelected(reportParameterAttr.isDelayPlaying());
        isshowWindowItem.setSelected(reportParameterAttr.isShowWindow());
        isEditing = true;
        ParameterBridge bridge = paraDesignEditor.getParaComponent();
        if (parameterUI != null) {
            bridge.setDelayDisplayContent(reportParameterAttr.isDelayPlaying());
            bridge.setPosition(reportParameterAttr.getAlign());
            bridge.setDisplay(reportParameterAttr.isShowWindow());
            bridge.setBackground(reportParameterAttr.getBackground());
            bridge.setUseParamsTemplate(reportParameterAttr.isUseParamsTemplate());
        }
    }


    private Parameter[] getTargetParameter(JWorkBook workBook) {
        return workBook.getTarget().getParameters();
    }

    /**
     * 刷新所有的控件
     */
    public void refreshAllNameWidgets() {
        if (paraDesignEditor != null) {
            paraDesignEditor.refreshAllNameWidgets();
        }
    }


    /**
     * 刷新数据集
     *
     * @param oldName 旧的名字
     * @param newName 新的名字
     */
    public void refresh4TableData(String oldName, String newName) {
        if (paraDesignEditor != null) {
            paraDesignEditor.refresh4TableData(oldName, newName);
        }
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

    /**
     * 刷新参数
     */
    public void refreshParameter() {
        if (paraDesignEditor != null) {
            paraDesignEditor.refreshParameter(this, this.workBook);
        }
    }

    /**
     * // 获取参数面板里面所有控件的名字列表
     *
     * @return
     */
    public List<String> getAllXCreatorNameList() {
        return paraDesignEditor.getAllXCreatorNameList();
    }

    private boolean isWithoutParaXCreator() {
        // allParameters只包含全部的模板参数
        return paraDesignEditor.isWithoutParaXCreator(DesignModelAdapter.getCurrentModelAdapter().getParameters());    }

    /**
     * get allParameters
     *
     * @return
     */
    public Parameter[] getAllParameters() {
        return allParameters;
    }

    private boolean isBlank() {
        return paraDesignEditor.isBlank();
    }

    /**
     * 是否有查询按钮
     *
     * @return 有则返回true
     */
    public boolean isWithQueryButton() {
        return paraDesignEditor.isWithQueryButton();
    }

    /**
     * update
     *
     * @param reportParameterAttr
     * @return
     */
    public ReportParameterAttr update(ReportParameterAttr reportParameterAttr) {
        if (reportParameterAttr == null) {
            reportParameterAttr = new ReportParameterAttr();
        }

        ParameterUI parameterUI = (isBlank() ? null : paraDesignEditor.getParaTarget());
        ParameterBridge bridge = paraDesignEditor.getParaComponent();
        if (parameterUI != null) {
            reportParameterAttr.setWindowPosition(TemplateParameterAttr.EMBED);
            reportParameterAttr.setDelayPlaying(bridge.isDelayDisplayContent());
            reportParameterAttr.setShowWindow(bridge.isDisplay());
            reportParameterAttr.setAlign(bridge.getPosition());
            reportParameterAttr.setBackground(bridge.getDataBackground());
            reportParameterAttr.setUseParamsTemplate(bridge.isUseParamsTemplate());
        }
        //这里不用 parameterUI 的原因是考虑到没有控件的时候设置宽度有效果但不保存，只有含有控件才保存属性
        paraDesignEditor.getParaTarget().setDesignSize(new Dimension(bridge.getDesignWidth(),
                (int)paraDesignEditor.getParaTarget().getDesignSize().getHeight()));
        reportParameterAttr.setParameterUI(parameterUI);
        return reportParameterAttr;
    }

    /**
     * 将参数增加到编辑器中
     *
     * @param parameter 参数
     */
    public void addingParameter2Editor(Parameter parameter) {
        if (isWithoutParaXCreator()) {
            currentIndex = 0;
        }
        if (!paraDesignEditor.addingParameter2Editor(parameter, currentIndex)) {
            return;
        }
        currentIndex++;
        parameterArray = (Parameter[]) ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
//        FormHierarchyTreePane.getInstance().refreshDockingView();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if (propertyChangeListener != null) {
            propertyChangeListener.propertyChange();
        }
    }

    /**
     * 将参数增加到带查询按钮的编辑器中
     *
     * @param parameter 参数
     */
    public void addingParameter2EditorWithQueryButton(Parameter parameter) {
        currentIndex = (isWithoutParaXCreator()) ? 0 : (currentIndex + NUM_IN_A_LINE);
        if (!paraDesignEditor.addingParameter2EditorWithQueryButton(parameter, currentIndex)) {
            return;
        }
        currentIndex = currentIndex + NUM_IN_A_LINE - currentIndex % NUM_IN_A_LINE;
        parameterArray = (Parameter[]) ArrayUtils.removeElement(parameterArray, parameter);
        refreshParameter();
//        FormHierarchyTreePane.getInstance().refreshDockingView();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if (propertyChangeListener != null) {
            propertyChangeListener.propertyChange();
        }
    }

    /**
     * 将所有的参数增加到编辑器中
     */
    public void addingAllParameter2Editor() {
        if (isWithoutParaXCreator()) {
            currentIndex = 0;
        }
        if (parameterArray == null) {
            return;
        }

        paraDesignEditor.addingAllParameter2Editor(parameterArray, currentIndex);

        parameterArray = null;
        refreshParameter();
//        FormHierarchyTreePane.getInstance().refreshDockingView();
        DesignModuleFactory.getFormHierarchyPane().refreshDockingView();
        if (propertyChangeListener != null) {
            propertyChangeListener.propertyChange();
        }
        workBook.setAutoHeightForCenterPane();
    }

    /**
     * 检查提交按钮
     *
     * @return 返回true
     */
    public boolean checkSubmitButton() {
        return true;
    }


    /**
     * 针对对象的工具条
     *
     * @return 无工具
     */
    public ToolBarDef[] toolbars4Target() {
        return new ToolBarDef[0];
    }

    /**
     * 参数面板的文件菜单的子菜单
     *
     * @return 文件菜单的子菜单
     */
    public ShortCut[] shortcut4FileMenu() {
        return (ShortCut[]) ArrayUtils.addAll(DesignerMode.isAuthorityEditing() ?
                        new ShortCut[]{new SaveTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                                new UndoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                                new RedoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate())} :
                        new ShortCut[]{new SaveTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                                new SaveAsTemplateAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                                new UndoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate()),
                                new RedoAction(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate())},
                new ShortCut[0]
        );
    }

    /**
     * 参数面板的模板菜单
     *
     * @return 模板菜单
     */
    public MenuDef[] menus4Target() {
        MenuDef tplMenu = new MenuDef(KeySetUtils.TEMPLATE.getMenuKeySetName(),KeySetUtils.TEMPLATE.getMnemonic());
        if (!DesignerMode.isAuthorityEditing()) {
            tplMenu.addShortCut(new NameSeparator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Utils_WorkBook")));
            tplMenu.addShortCut(new ReportParameterAction(workBook));
            tplMenu.addShortCut(new NameSeparator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit_DashBoard_Potence")));
            tplMenu.addShortCut(new AllowAuthorityEditAction(workBook));
        } else {
            tplMenu.addShortCut(new ExitAuthorityEditAction(workBook));
        }


        return new MenuDef[]{tplMenu};
    }

    /**
     * 参数面板针对FROM的工具条
     *
     * @return 返回工具
     */
    public JPanel[] toolbarPanes4Form() {
        return paraDesignEditor.toolbarPanes4Form();
    }

    /**
     * 参数面板针对FORM的功能按钮
     *
     * @return 返回工具按钮
     */
    public JComponent[] toolBarButton4Form() {
        return paraDesignEditor.toolBarButton4Form();
    }

    /**
     * 参数面板针对权限细粒度的工具条
     *
     * @return 工具条
     */
    public JComponent toolBar4Authority() {
        return new AuthorityToolBarPane();
    }

    @Override
    public int getMenuState() {
        return DesignState.PARAMETER_PANE;
    }


}
