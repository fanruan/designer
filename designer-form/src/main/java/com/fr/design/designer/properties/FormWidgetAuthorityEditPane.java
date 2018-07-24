package com.fr.design.designer.properties;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
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


/**
 * Author : daisy
 * Date: 13-9-16
 * Time: 上午10:45
 */
public class FormWidgetAuthorityEditPane extends AuthorityEditPane {
    private FormDesigner designer;
    private Widget[] widgets = null;
    private UICheckBox widgetVisible = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget_Visible"));
    private UICheckBox widgetAvailable = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget_Enabled"));
    private ItemListener visibleItemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (selectedRoles == null) {
                return;
            }
            if (widgets != null && widgets.length > 0) {
                for (int i = 0; i < widgets.length; i++) {
                    widgets[i].changeVisibleAuthorityState(selectedRoles, widgetVisible.isSelected());
                }
            }
            doAfterAuthority();
        }
    };


    private ItemListener usableItemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            String selectedRoles = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (ComparatorUtils.equals(selectedRoles, com.fr.design.i18n.Toolkit.i18nText("FR-Engine_Role"))) {
                return;
            }
            if (selectedRoles == null) {
                return;
            }
            if (widgets != null && widgets.length > 0) {
                for (int i = 0; i < widgets.length; i++) {
                    widgets[i].changeUsableAuthorityState(selectedRoles, widgetAvailable.isSelected());
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
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
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
    public void populateType() {
        type.setText(com.fr.design.i18n.Toolkit.i18nText("Widget-Form_Widget_Config"));
    }

    /**
     * 更新名称面板
     *
     * @date 2014-12-21-下午7:12:27
     */
    public void populateName() {
        String nameText = "";
        if (widgets == null || widgets.length <= 0) {
            return;
        }
        for (int i = 0; i < widgets.length; i++) {
            nameText += "," + widgets[i].getClass().getSimpleName();
        }
        name.setText(nameText.substring(1));
    }

    /**
     * 更新checkbox所在的面板
     *
     * @return 面板
     * @date 2014-12-21-下午6:19:03
     */
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
    public void populateDetials() {
        //更新说明要是JWorkBook的话，说明鼠标焦点又改变了
        HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().setAuthorityMode(true);
        signelSelection();

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

    private void signelSelection() {
        if (HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().isJWorkBook()) {
            //清工具栏
            JComponent component = DesignerContext.getDesignerFrame().getToolbarComponent();
            if (component instanceof AuthorityEditToolBarComponent) {
                ((AuthorityEditToolBarComponent) component).removeSelection();
            }

            //清空报表主体的单元格选择
            HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().removeTemplateSelection();
        }
    }

    private void refreshCreator() {
        int size = designer.getSelectionModel().getSelection().size();
        widgets = size == 0 ? null : designer.getSelectionModel().getSelection().getSelectedWidgets();
    }


}