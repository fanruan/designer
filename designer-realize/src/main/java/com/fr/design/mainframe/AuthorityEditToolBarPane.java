package com.fr.design.mainframe;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.roleAuthority.ReportAndFSManagePane;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.design.webattr.ToolBarButton;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StringUtils;


import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * 用于在权限编辑状态对工具栏的元素进行权限编辑的面板
 * Author : daisy
 * Date: 13-9-18
 * Time: 下午2:15
 */
public class AuthorityEditToolBarPane extends AuthorityPropertyPane {

    private static final int TITLE_HEIGHT = 19;
    private static final int RIGHT_GAP = 10;
    private AuthorityEditPane authorityEditPane = null;
    private AuthorityToolBarPane authorityToolBarPane;
    private String[] selectedPathArray;

    public AuthorityEditToolBarPane(List<ToolBarButton> buttonlists) {
        super(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
        this.setLayout(new BorderLayout());
        this.setBorder(null);
        UILabel authorityTitle = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Permissions_Edit")) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TITLE_HEIGHT);
            }
        };
        authorityTitle.setHorizontalAlignment(SwingConstants.CENTER);
        authorityTitle.setVerticalAlignment(SwingConstants.CENTER);
        JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        northPane.add(authorityTitle, BorderLayout.CENTER);
        northPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LINE_COLOR));
//        this.add(northPane, BorderLayout.NORTH);
        authorityEditPane = new AuthorityEditPane(buttonlists);
        this.add(authorityEditPane, BorderLayout.CENTER);
    }

    public void setAuthorityToolBarPane(AuthorityToolBarPane authorityToolBarPane) {
        this.authorityToolBarPane = authorityToolBarPane;
    }


    public void populate() {
        authorityToolBarPane.populateAuthority();
        signelSelection();
        authorityEditPane.populateDetials();
    }


    private void signelSelection() {
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jTemplate.isJWorkBook()) {
            //清参数面板
            jTemplate.removeParameterPaneSelection();
            //清报表主体
            jTemplate.removeTemplateSelection();
        }
    }


    private class AuthorityEditPane extends JPanel {
        private static final int TOP_GAP = 11;
        private static final int LEFT_GAP = 4;
        private static final int LEFT_CHECKPANE = 3;
        private JPanel typePane;
        private JPanel namePane;
        private UILabel type = null;
        private UILabel name = null;
        private JPanel checkPane = null;
        private List<ToolBarButton> buttonlists;
        private UICheckBox buttonVisible = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Visible"));
        private ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String selectedRole = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
                initSelectedPathArray();
                if (ComparatorUtils.equals(selectedRole, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Role")) || selectedRole ==
                        null || selectedPathArray == null) {
                    return;
                }
                for (int t = 0; t < selectedPathArray.length; t++) {
                    for (int i = 0; i < buttonlists.size(); i++) {
                        if (buttonlists.get(i).isSelected()) {
                            buttonlists.get(i).changeAuthorityState(selectedPathArray[t], buttonVisible.isSelected());
                            authorityToolBarPane.repaint();
                        }
                    }
                    HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().fireTargetModified();
                    RolesAlreadyEditedPane.getInstance().refreshDockingView();
                    UICheckBox checkbox = (UICheckBox) e.getSource();
                    List<ToolBarButton> btns = AuthorityEditPane.this.buttonlists;
                    for (int j = 0; j < btns.size(); j++) {
                        if (btns.get(j).isSelected()) {
                            //由引擎实现保存进模板报表
                            authorityToolBarPane.setAuthorityWebAttr(btns.get(j).getWidget(), checkbox.isSelected(), selectedPathArray[t]);
                        }
                    }
                }
            }
        };

        /**
         * @see ElementCasePaneAuthorityEditPane initSelectedPathArray()
         */
        private void initSelectedPathArray() {
            TreePath[] selectionPaths = ReportAndFSManagePane.getInstance().getRoleTree().getCheckBoxTreeSelectionModel().getSelectionPaths();
            if (selectionPaths.length == 1) {
                if (((ExpandMutableTreeNode) (selectionPaths[0].getLastPathComponent())).getChildCount() > 0) {
                    ExpandMutableTreeNode node = (ExpandMutableTreeNode) ((ExpandMutableTreeNode) (selectionPaths[0].getLastPathComponent())).getLastChild();
                    selectedPathArray = new String[node.getChildCount()];
                    for (int i = 0; i < node.getChildCount(); i++) {
                        ExpandMutableTreeNode n = (ExpandMutableTreeNode) node.getChildAt(i);
                        String nodeName = n.getUserObject().toString();
                        selectedPathArray[i] = nodeName;
                    }
                } else {
                    selectedPathArray = ElementCasePaneAuthorityEditPane.pathToString(selectionPaths);
                }
            } else {
                selectedPathArray = ElementCasePaneAuthorityEditPane.pathToString(selectionPaths);
            }

        }

        public AuthorityEditPane(List<ToolBarButton> buttonlists) {
            setLayout(new BorderLayout());
            type = new UILabel();
            typePane = new JPanel(new BorderLayout());
            typePane.add(type, BorderLayout.CENTER);
            type.setBorder(BorderFactory.createEmptyBorder(0, LEFT_GAP, 0, 0));
            typePane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            name = new UILabel();
            namePane = new JPanel(new BorderLayout());
            namePane.add(name, BorderLayout.CENTER);
            name.setBorder(BorderFactory.createEmptyBorder(0, LEFT_GAP, 0, 0));
            namePane.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            checkPane = new JPanel();
            checkPane.setLayout(new BorderLayout());
            this.add(centerPane(), BorderLayout.NORTH);
            this.setBorder(BorderFactory.createEmptyBorder(TOP_GAP, LEFT_GAP, 0, RIGHT_GAP));
            this.buttonlists = buttonlists;
            buttonVisible.addItemListener(itemListener);
        }

        private JPanel centerPane() {
            double f = TableLayout.FILL;
            double p = TableLayout.PREFERRED;
            double[] rowSize = {p, p, p};
            double[] columnSize = {p, f};
            int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}};
            Component[][] components = new Component[][]{
                    new Component[]{new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Type") + "        ", SwingConstants.LEFT), typePane},
                    new Component[]{new UILabel(" " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_WF_Name") + "        ", SwingConstants.LEFT), namePane},
                    new Component[]{checkPane, null},
            };

            return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_MEDIUM);
        }

        /**
         * 更新权限编辑面板的具体内容：类型、名称、权限面板
         */
        public void populateDetials() {
            populateName();
            populateType();
            populateCheckPane();
            checkVisibleCheckBoxes();
        }

        private void checkVisibleCheckBoxes() {
            buttonVisible.removeItemListener(itemListener);
            String selected = ReportAndFSManagePane.getInstance().getRoleTree().getSelectedRoleName();
            if (selected == null) {
                buttonVisible.setSelected(true);
            }
            for (int i = 0; i < buttonlists.size(); i++) {
                if (buttonlists.get(i).isSelected()) {
                    buttonVisible.setSelected(!buttonlists.get(i).isDoneAuthorityEdited(selected));
                    break;
                }
            }
            buttonVisible.addItemListener(itemListener);
        }


        public void populateType() {
            if (StringUtils.isEmpty(name.getText())) {
                type.setText("");
            } else {
                type.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Toolbar_Button"));
            }
        }

        public void populateName() {
            String names = "";
            for (int i = 0; i < buttonlists.size(); i++) {
                if (buttonlists.get(i).isSelected()) {
                    names += "," + buttonlists.get(i).getNameOption().optionName();
                }
            }
            if (StringUtils.isNotEmpty(names)) {
                names = names.substring(1);
            }
            name.setText(names);
        }

        public void populateCheckPane() {
            checkPane.removeAll();
            if (StringUtils.isEmpty(name.getText())) {
                return;
            }
            double f = TableLayout.FILL;
            double p = TableLayout.PREFERRED;
            Component[][] components = new Component[][]{
                    new Component[]{buttonVisible},
            };
            double[] rowSize = {p};
            double[] columnSize = {p};
            int[][] rowCount = {{1}};
            JPanel check = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_SMALL, LayoutConstants.VGAP_SMALL);
            checkPane.add(check, BorderLayout.CENTER);
            checkPane.setBorder(BorderFactory.createEmptyBorder(0, LEFT_CHECKPANE, 0, 0));
        }
    }
}
