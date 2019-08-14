package com.fr.design.designer.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.AuthorityEditPane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.toolbar.AuthorityEditToolBarComponent;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.form.ui.Widget;
import com.fr.general.ComparatorUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;


/**
 * Author : daisy
 * Date: 13-9-16
 * Time: 上午10:45
 */
public class FormWidgetAuthorityEditPane extends AuthorityEditPane {
    private FormDesigner designer;
    private Widget[] widgets = null;
    private UICheckBox widgetVisible = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Visible"));
    private UICheckBox widgetAvailable = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Enabled"));
    private String[] selectedArray;
    private ItemListener visibleItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (selectedRoles == null) {
                return;
            }
            initSelectedArray();
            if (ArrayUtils.isEmpty(selectedArray)) {
                return;
            }
            for (String selectedRole : selectedArray) {
                for (Widget widget : widgets) {
                    widget.changeVisibleAuthorityState(selectedRole, widgetVisible.isSelected());
                    widgetAvailable.setEnabled(widgetVisible.isSelected());
                }
            }
            doAfterAuthority();
        }
    };


    private ItemListener usableItemListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (ComparatorUtils.equals(selectedRoles, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Role"))) {
                return;
            }
            if (selectedRoles == null) {
                return;
            }
            initSelectedArray();
            if (ArrayUtils.isEmpty(selectedArray)) {
                return;
            }
            for (String selectedRole : selectedArray) {
                for (Widget widget : widgets) {
                    widget.changeUsableAuthorityState(selectedRole, widgetAvailable.isSelected());
                }
            }
            doAfterAuthority();
        }
    };

    public FormWidgetAuthorityEditPane(FormDesigner designer) {
        super(designer);
        this.designer = designer;
        widgetAvailable.addItemListener(usableItemListener);
        widgetVisible.addItemListener(visibleItemListener);
    }


    private void doAfterAuthority() {
        designer.repaint();
        HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().fireTargetModified();
        RolesAlreadyEditedPane.getInstance().refreshDockingView();
        RolesAlreadyEditedPane.getInstance().setReportAndFSSelectedRoles();
        RolesAlreadyEditedPane.getInstance().repaint();
        checkCheckBoxes();
    }

    /**
     * 更新类型面板
     *
     * @date 2014-12-21-下午6:19:43
     */
    @Override
    public void populateType() {
        type.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Widget_Form_Widget_Config"));
    }

    /**
     * 更新名称面板
     *
     * @date 2014-12-21-下午7:12:27
     */
    @Override
    public void populateName() {
        if (widgets == null || widgets.length <= 0) {
            return;
        }
        List<String> widgetNames = new ArrayList<String>();
        for (Widget widget : widgets) {
            widgetNames.add(widget.getClass().getSimpleName());
        }
        name.setText(StableUtils.join(widgetNames, ","));
    }

    /**
     * 更新checkbox所在的面板
     *
     * @return 面板
     * @date 2014-12-21-下午6:19:03
     */
    @Override
    public JPanel populateCheckPane() {
        checkPane.add(populateWidgetCheckPane(), BorderLayout.CENTER);
        checkPane.setBorder(BorderFactory.createEmptyBorder(0, LEFT_CHECKPANE, 0, 0));
        return checkPane;
    }

    private JPanel populateWidgetCheckPane() {
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{widgetVisible},
                new Component[]{widgetAvailable}
        };
        double[] rowSize = {p, p};
        double[] columnSize = {p};
        int[][] rowCount = {{1},{1}};
        return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_SMALL);
    }


    /**
     * 对单元格区域进行操作时的权限编辑页面
     */
    @Override
    public void populateDetials() {
        //更新说明要是JWorkBook的话，说明鼠标焦点又改变了
        HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().setAuthorityMode(true);
        singleSelection();

        refreshCreator();
        //如果是布局选中不支持的元素则显示“该元素不支持权限控制”
        populateType();
        populateName();
        checkPane.removeAll();
        populateCheckPane();
        checkPane.setBorder(BorderFactory.createEmptyBorder(0, LEFT_CHECKPANE, 0, 0));
        checkCheckBoxes();
    }

    private void checkCheckBoxes() {
        String selected = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
        widgetVisible.removeItemListener(visibleItemListener);
        widgetAvailable.removeItemListener(usableItemListener);
        populateWidgetButton(selected);
        widgetVisible.addItemListener(visibleItemListener);
        widgetAvailable.addItemListener(usableItemListener);
    }

    private void populateWidgetButton(String selected) {
        if (widgets == null || widgets.length == 0) {
            return;
        }

        //选中多个, 界面上只取第一个
        Widget widget = widgets[0];

        if (widget.isVisible()) {
            widgetVisible.setSelected(!widget.isDoneVisibleAuthority(selected));
        } else {
            widgetVisible.setSelected(widget.isVisibleAuthority(selected));
        }

        if (widget.isEnabled()) {
            widgetAvailable.setSelected(!widget.isDoneUsableAuthority(selected));
        } else {
            widgetAvailable.setSelected(widget.isUsableAuthority(selected));
        }
    }


    //实现单选

    private void singleSelection() {
        if (HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().isJWorkBook()) {
            //清工具栏
            JComponent component = DesignerContext.getDesignerFrame().getToolbarComponent();
            if (component instanceof AuthorityEditToolBarComponent) {
                ((AuthorityEditToolBarComponent) component).removeSelection();
            }

            //清空报表主体的单元格选择
            HistoryTemplateListCache.getInstance().getCurrentEditingTemplate().removeTemplateSelection();
        }
    }

    private void refreshCreator() {
        int size = designer.getSelectionModel().getSelection().size();
        widgets = size == 0 ? null : designer.getSelectionModel().getSelection().getSelectedWidgets();
    }

    private void initSelectedArray() {
        TreePath[] selectionPaths = ReportAndFSManagePane.getInstance().getRoleTree().getCheckBoxTreeSelectionModel().getSelectionPaths();
        if (selectionPaths.length == 1) {
            if (((ExpandMutableTreeNode) (selectionPaths[0].getLastPathComponent())).getChildCount() > 0) {
                ExpandMutableTreeNode node = (ExpandMutableTreeNode) ((ExpandMutableTreeNode) (selectionPaths[0].getLastPathComponent())).getLastChild();
                selectedArray = new String[node.getChildCount()];
                for (int i = 0, len = node.getChildCount(); i < len; i++) {
                    ExpandMutableTreeNode treeNode = (ExpandMutableTreeNode) node.getChildAt(i);
                    String nodeName = treeNode.getUserObject().toString();
                    selectedArray[i] = nodeName;
                }
            } else {
                selectedArray = pathToString(selectionPaths);
            }
        } else {
            selectedArray = pathToString(selectionPaths);
        }
    }

    private String[] pathToString(TreePath[] path) {
        List<String> roles = new ArrayList<String>();
        if (!ArrayUtils.isEmpty(path)) {
            for (TreePath tempPath : path) {
                String temp = tempPath.toString();
                boolean exist = StringUtils.isNotEmpty(temp) && temp.startsWith("[") && temp.endsWith("]");
                if (exist) {
                    temp = temp.substring(1, temp.length() - 1);
                    String[] selectedRoles = temp.split("," + StringUtils.BLANK);
                    String role = selectedRoles[2].trim();
                    roles.add(role);
                }
            }
        }
        return roles.toArray(new String[0]);
    }

}
