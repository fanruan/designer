package com.fr.design.condition;

import com.fr.base.BaseUtils;
import com.fr.base.Formula;
import com.fr.data.DataConstants;
import com.fr.data.condition.*;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.formula.VariableResolver;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.scrollruler.ModLineBorder;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.Inter;
import com.fr.general.data.Condition;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * peter: LiteCondition Pane.
 */
public abstract class LiteConditionPane<T extends Condition> extends BasicBeanPane<Condition> {
    private static int MOVE_UP = 0;
    private static int MOVE_DOWN = 1;

    private static final long serialVersionUID = 1L;
    // peter:这两个变量在弹出公式编辑器的时候,需要用.
    private UIRadioButton commonRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_LiteCondition_Common"));
    private UIRadioButton formulaRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_LiteCondition_Formula"));
    private JPanel conditionCardPane;
    protected BasicBeanPane<T> defaultConditionPane;
    // card2
    private UITextArea formulaTextArea;
    private UIButton modifyButton;
    private UIButton addButton;
    private UIRadioButton andRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_LiteCondition_ConditionB-AND") + "  ");
    private UIRadioButton orRadioButton = new UIRadioButton(Inter.getLocText("FR-Designer_LiteCondition_ConditionB-OR"));
    protected JTree conditionsTree;// Conditions
    private UIButton removeButton;
    private UIButton moveUpButton;
    private UIButton moveDownButton;
    private UIButton bracketButton;
    private UIButton unBracketButton;

    private ActionListener actionListener1 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            Formula formula;

            String text = formulaTextArea.getText();
            if (text == null || text.length() <= 0) {
                formula = new Formula("");
            } else {
                formula = new Formula(text);
            }

            final UIFormula formulaPane = FormulaFactory.createFormulaPane();
            formulaPane.populate(formula, variableResolver4FormulaPane());
            formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(LiteConditionPane.this), new DialogActionAdapter() {

                @Override
                public void doOk() {
                    Formula formula = formulaPane.update();
                    if (formula.getContent().length() <= 1) {// 如果没有填任何字符，则是空白文本
                        formulaTextArea.setText("");
                    } else {
                        formulaTextArea.setText(formula.getContent().substring(1));
                    }
                }
            }).setVisible(true);
        }
    };


    private ActionListener actionListener2 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            // peter:先获得当前的LiteCondition.

            Condition liteCondition = null;
            if (commonRadioButton.isSelected()) {
                liteCondition = defaultConditionPane.updateBean();
            } else {
                liteCondition = new FormulaCondition(formulaTextArea.getText());
            }

            JoinCondition newJoinCondition = new JoinCondition(andRadioButton.isSelected() ? DataConstants.AND : DataConstants.OR, liteCondition);
            ExpandMutableTreeNode parentTreeNode = getParentTreeNode();
            boolean result = isExistedInParentTreeNode(parentTreeNode, newJoinCondition);
            if (result) {
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(LiteConditionPane.this),
                        Inter.getLocText("BindColumn-This_Condition_has_been_existed"));
                return;
            }

            ExpandMutableTreeNode newJionConditionTreeNode = new ExpandMutableTreeNode(newJoinCondition);
            parentTreeNode.add(newJionConditionTreeNode);
            DefaultTreeModel defaultTreeModel = (DefaultTreeModel) conditionsTree.getModel();
            defaultTreeModel.reload(parentTreeNode);
            parentTreeNode.expandCurrentTreeNode(conditionsTree);
            conditionsTree.setSelectionPath(GUICoreUtils.getTreePath(newJionConditionTreeNode));

            // peter:必须要检查Enabled.
            checkButtonEnabledForList();
        }
    };


    private MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent evt) {
            TreePath selectedTreePath = conditionsTree.getSelectionPath();
            // peter:当前的节点
            if (selectedTreePath != null) {
                ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
                JoinCondition oldJoinCondition = (JoinCondition) selectedTreeNode.getUserObject();
                oldJoinCondition.setJoin(andRadioButton.isSelected() ? DataConstants.AND : DataConstants.OR);

                Condition oldLiteCondition = oldJoinCondition.getCondition();
                // peter:如果当前选中的是ListCondition,只要改变Join为AND或者OR,直接返回.
                if (oldLiteCondition instanceof ListCondition) {
                    GUICoreUtils.setEnabled(conditionCardPane, false);
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent evt) {
        	GUICoreUtils.setEnabled(conditionCardPane, conditionCardPane.isEnabled());
        }
    };

    private TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {

        @Override
        public void valueChanged(TreeSelectionEvent evt) {
            checkButtonEnabledForList();

            TreePath selectedTreePath = conditionsTree.getSelectionPath();
            if (selectedTreePath == null) {
                return;
            }

            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            Object userObject = selectedTreeNode.getUserObject();
            if (userObject instanceof JoinCondition) {
                JoinCondition joinCondition = (JoinCondition) userObject;

                // peter:先弄join.
                int join = joinCondition.getJoin();
                if (join == DataConstants.AND) {
                    andRadioButton.setSelected(true);
                } else {
                    orRadioButton.setSelected(true);
                }

                // peter:当前的liteCondtion.
                Condition liteCondition = joinCondition.getCondition();
                // elake:两种Condition对应于数据列和高亮.
                if (liteCondition instanceof CommonCondition || liteCondition instanceof ObjectCondition) {
                    Condition commonCondition = liteCondition;
                    commonRadioButton.setSelected(true);
                    applyCardsPane();

                    defaultConditionPane.populateBean((T) commonCondition);

                } else if (liteCondition instanceof FormulaCondition) {
                    FormulaCondition formulaCondition = (FormulaCondition) liteCondition;

                    formulaRadioButton.setSelected(true);
                    applyCardsPane();

                    formulaTextArea.setText(formulaCondition.getFormula());
                }
            }
        }
    };


    private ActionListener actionListener3 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            TreePath selectedTreePath = conditionsTree.getSelectionPath();
            if (selectedTreePath == null) {
                return;
            }

            int returnVal = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(LiteConditionPane.this),
                    Inter.getLocText("Utils-Are_you_sure_to_remove_the_selected_item") + "?", Inter.getLocText("FR-Designer_Remove"),
                    JOptionPane.OK_CANCEL_OPTION);
            if (returnVal == JOptionPane.OK_OPTION) {
                DefaultTreeModel treeModel = (DefaultTreeModel) conditionsTree.getModel();

                TreePath[] selectedTreePaths = conditionsTree.getSelectionPaths();
                for (int i = selectedTreePaths.length - 1; i >= 0; i--) {
                    ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePaths[i].getLastPathComponent();
                    ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();

                    // peter:将要选择节点.
                    ExpandMutableTreeNode nextSelectTreeNode;
                    if (parentTreeNode.getChildAfter(selectedTreeNode) != null) {
                        nextSelectTreeNode = (ExpandMutableTreeNode) parentTreeNode.getChildAfter(selectedTreeNode);
                    } else if (parentTreeNode.getChildBefore(selectedTreeNode) != null) {
                        nextSelectTreeNode = (ExpandMutableTreeNode) parentTreeNode.getChildBefore(selectedTreeNode);
                    } else {
                        nextSelectTreeNode = parentTreeNode;
                    }

                    parentTreeNode.remove(selectedTreeNode);

                    if (!ComparatorUtils.equals(nextSelectTreeNode, treeModel.getRoot())) {
                        conditionsTree.setSelectionPath(GUICoreUtils.getTreePath(nextSelectTreeNode));
                    }
                    treeModel.reload(parentTreeNode);
                    parentTreeNode.expandCurrentTreeNode(conditionsTree);
                    if (!ComparatorUtils.equals(nextSelectTreeNode, treeModel.getRoot())) {
                        conditionsTree.setSelectionPath(GUICoreUtils.getTreePath(nextSelectTreeNode));
                    }
                }

                // peter:检查Button Enabled.
                checkButtonEnabledForList();
            }
        }
    };


    private ActionListener actionListener4 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            TreePath selectedTreePath = conditionsTree.getSelectionPath();
            if (selectedTreePath == null) {
                return;
            }
            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
            if (parentTreeNode.getChildBefore(selectedTreeNode) != null) {
                swapNodesOfConditionTree(parentTreeNode, (ExpandMutableTreeNode) parentTreeNode.getChildBefore(selectedTreeNode),
                        selectedTreeNode, MOVE_UP);
            }
        }
    };


    private ActionListener actionListener5 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            TreePath selectedTreePath = conditionsTree.getSelectionPath();
            if (selectedTreePath == null) {
                return;
            }
            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
            if (parentTreeNode.getChildAfter(selectedTreeNode) != null) {
                swapNodesOfConditionTree(parentTreeNode, selectedTreeNode, (ExpandMutableTreeNode) parentTreeNode.getChildAfter(selectedTreeNode), MOVE_DOWN);
            }
        }
    };


    private ActionListener actionListener6 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            TreePath[] selectedTreePaths = conditionsTree.getSelectionPaths();
            // peter:当前的节点
            if (selectedTreePaths == null || selectedTreePaths.length <= 1) {
                return;
            }

            // peter: 找到父亲节点,并且删除所有的节点.
            TreePath topTreePath = GUICoreUtils.getTopTreePath(conditionsTree, selectedTreePaths);
            ExpandMutableTreeNode leadTreeNode = (ExpandMutableTreeNode) topTreePath.getLastPathComponent();
            ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) leadTreeNode.getParent();
            int topIndex = parentTreeNode.getIndex(leadTreeNode);

            JoinCondition firstJionCondition = (JoinCondition) leadTreeNode.getUserObject();
            for (int i = 0; i < selectedTreePaths.length; i++) {
                ExpandMutableTreeNode tmpTreeNode = (ExpandMutableTreeNode) selectedTreePaths[i].getLastPathComponent();
                parentTreeNode.remove(tmpTreeNode);
            }

            // peter:建立新的节点.
            JoinCondition newJionCondition = new JoinCondition();
            newJionCondition.setJoin(firstJionCondition.getJoin());
            newJionCondition.setCondition(new ListCondition());
            ExpandMutableTreeNode newTreeNode = new ExpandMutableTreeNode(newJionCondition);
            for (int i = 0; i < selectedTreePaths.length; i++) {
                ExpandMutableTreeNode tmpTreeNode = (ExpandMutableTreeNode) selectedTreePaths[i].getLastPathComponent();
                newTreeNode.add(tmpTreeNode);
            }

            // peter:添加新的节点
            parentTreeNode.insert(newTreeNode, topIndex);

            // peter:需要reload
            DefaultTreeModel defaultTreeModel = (DefaultTreeModel) conditionsTree.getModel();
            defaultTreeModel.reload(parentTreeNode);
            parentTreeNode.expandCurrentTreeNode(conditionsTree);

            // peter:选择一个节点
            conditionsTree.setSelectionPath(GUICoreUtils.getTreePath(newTreeNode));
        }
    };


    ActionListener actionListener7 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            TreePath selectedTreePath = conditionsTree.getSelectionPath();
            // peter:当前的节点
            if (selectedTreePath == null) {
                return;
            }

            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            JoinCondition joinCondition = (JoinCondition) selectedTreeNode.getUserObject();
            Condition liteCondition = joinCondition.getCondition();
            if (liteCondition instanceof ListCondition) {
                ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
                int index = parentTreeNode.getIndex(selectedTreeNode);

                // peter:添加节点
                List<TreePath> treePathList = new ArrayList<TreePath>();
                for (int i = selectedTreeNode.getChildCount() - 1; i >= 0; i--) {
                    ExpandMutableTreeNode tmpTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getChildAt(i);
                    parentTreeNode.insert(tmpTreeNode, index);
                    treePathList.add(GUICoreUtils.getTreePath(tmpTreeNode));
                }

                // peter;删除这个List节点
                parentTreeNode.remove(selectedTreeNode);

                // peter:需要reload
                DefaultTreeModel defaultTreeModel = (DefaultTreeModel) conditionsTree.getModel();
                defaultTreeModel.reload(parentTreeNode);
                parentTreeNode.expandCurrentTreeNode(conditionsTree);

                // peter:选择所有选择的节点
                TreePath[] selectedTreePaths = new TreePath[treePathList.size()];
                treePathList.toArray(selectedTreePaths);
                conditionsTree.setSelectionPaths(selectedTreePaths);
            }
        }
    };


    private ActionListener actionListener8 = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            modify();
        }
    };


    // 图表条件高亮时没有公式选择
    protected JPanel conditonTypePane;

    public LiteConditionPane() {
        this.initComponents();
    }

    protected abstract BasicBeanPane<T> createUnFormulaConditionPane();

    protected abstract VariableResolver variableResolver4FormulaPane();

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        // north
        initNorth();

        //center
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(centerPane, BorderLayout.CENTER);
        centerPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        // Control
        JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.add(controlPane, BorderLayout.NORTH);
        // controlPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        // conditionCardPane
        initConditionCardPane(controlPane);

        // addControlPane, contains or,and Radio, add,modify Button
        initControlPane(controlPane);

        // Preview
        JPanel previewPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane.add(previewPane, BorderLayout.CENTER);
        previewPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 0));


        // conTreeScrollPane.setPreferredSize(new Dimension(400, 125));
        previewPane.add(iniTreeScrollPane(), BorderLayout.CENTER);
        conditionsTree.addTreeSelectionListener(treeSelectionListener);

        JPanel buttonPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        previewPane.add(GUICoreUtils.createBorderPane(buttonPane, BorderLayout.NORTH), BorderLayout.EAST);
        initButtonPane(buttonPane);

        // peter:必须要检查Enabled.
        checkButtonEnabledForList();
    }


    private void initButtonPane(JPanel buttonPane) {
        removeButton = new UIButton(Inter.getLocText("FR-Designer_Remove"));
        buttonPane.add(removeButton);
        removeButton.setIcon(BaseUtils.readIcon("com/fr/base/images/cell/control/remove.png"));
        removeButton.setEnabled(false);
        removeButton.addActionListener(actionListener3);

        moveUpButton = new UIButton(Inter.getLocText("Utils-Move_Up"));
        buttonPane.add(moveUpButton);
        moveUpButton.setIcon(BaseUtils.readIcon("com/fr/design/images/control/up.png"));
        moveUpButton.addActionListener(actionListener4);

        moveDownButton = new UIButton(Inter.getLocText("Utils-Move_Down"));
        buttonPane.add(moveDownButton);
        moveDownButton.setIcon(BaseUtils.readIcon("com/fr/design/images/control/down.png"));
        moveDownButton.addActionListener(actionListener5);

        // peter:加括号
        bracketButton = new UIButton(Inter.getLocText("ConditionB-Add_bracket"));
        buttonPane.add(bracketButton);
        bracketButton.setIcon(BaseUtils.readIcon("com/fr/design/images/condition/bracket.png"));
        bracketButton.addActionListener(actionListener6);

        // peter:去掉括号
        unBracketButton = new UIButton(Inter.getLocText("ConditionB-Remove_bracket"));
        buttonPane.add(unBracketButton);
        unBracketButton.setIcon(BaseUtils.readIcon("com/fr/design/images/condition/unBracket.png"));
        unBracketButton.addActionListener(actionListener7);
    }

    private JScrollPane iniTreeScrollPane() {
        conditionsTree = new JTree(new DefaultTreeModel(new ExpandMutableTreeNode(new JoinCondition(DataConstants.AND, new ListCondition()))));
        conditionsTree.setRootVisible(false);
        conditionsTree.setCellRenderer(conditionsTreeCellRenderer);
        conditionsTree.setSelectionModel(new ContinuousTreeSelectionModel());
        conditionsTree.addTreeExpansionListener(treeExpansionListener);
        conditionsTree.setShowsRootHandles(true);
        return new JScrollPane(conditionsTree);

    }

    private void initNorth() {
        conditonTypePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(conditonTypePane, BorderLayout.NORTH);
        conditonTypePane.setBorder(new ModLineBorder(ModLineBorder.BOTTOM));

        UILabel conditionTypeLabel = new UILabel(Inter.getLocText("FR-Designer_Type") + ":");
        conditonTypePane.add(conditionTypeLabel, BorderLayout.WEST);
        conditionTypeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        JPanel northPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        conditonTypePane.add(northPane, BorderLayout.CENTER);
        northPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        northPane.add(GUICoreUtils.createFlowPane(commonRadioButton, FlowLayout.CENTER));
        northPane.add(GUICoreUtils.createFlowPane(formulaRadioButton, FlowLayout.CENTER));
        commonRadioButton.addActionListener(radioActionListener);
        formulaRadioButton.addActionListener(radioActionListener);

        ButtonGroup mainBg = new ButtonGroup();
        mainBg.add(commonRadioButton);
        mainBg.add(formulaRadioButton);
        commonRadioButton.setSelected(true);
    }

    private void initConditionCardPane(JPanel controlPane) {
        conditionCardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        controlPane.add(conditionCardPane, BorderLayout.CENTER);
        conditionCardPane.setLayout(new CardLayout());
        conditionCardPane.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));

        // defaultConditionPane
        conditionCardPane.add(defaultConditionPane = createUnFormulaConditionPane(), "DEFAULT");

        // formulaConditionPane
        JPanel formulaConditionPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        conditionCardPane.add(formulaConditionPane, "FORMULA");
        // formulaConditionPane.setLayout(FRGUIPaneFactory.createBorderLayout());

        // formulaPane
        JPanel formulaPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // 95106 公式区域限定宽高, 显示两行即可, 在新窗口编辑.
        formulaPane.setPreferredSize(new Dimension(450, 40));
        formulaConditionPane.add(formulaPane, BorderLayout.CENTER);
        formulaPane.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 2));
        formulaPane.add(GUICoreUtils.createBorderPane(new UILabel(Inter.getLocText("FR-Designer_LiteCondition_Formula") + "="), BorderLayout.NORTH), BorderLayout.WEST);
        formulaTextArea = new UITextArea();
        formulaPane.add(new JScrollPane(formulaTextArea), BorderLayout.CENTER);
        UIButton editFormulaButton = new UIButton(Inter.getLocText("FR-Designer_LiteCondition_Define"));
        formulaPane.add(GUICoreUtils.createBorderPane(editFormulaButton, BorderLayout.NORTH), BorderLayout.EAST);
        editFormulaButton.addActionListener(actionListener1);
        applyCardsPane();
    }


    private void initControlPane(JPanel controlPane) {
        JPanel addControlPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        controlPane.add(addControlPane, BorderLayout.SOUTH);
        addControlPane.setBorder(new ModLineBorder(ModLineBorder.TOP));

        ButtonGroup bg = new ButtonGroup();
        bg.add(andRadioButton);
        bg.add(orRadioButton);

        andRadioButton.setSelected(true);

        JPanel radioPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(2);
        addControlPane.add(radioPane);
        radioPane.add(andRadioButton);
        radioPane.add(orRadioButton);

        addControlPane.add(Box.createHorizontalStrut(35));

        addButton = new UIButton(Inter.getLocText("FR-Designer_Add"), BaseUtils.readIcon("com/fr/base/images/cell/control/add.png"));
        addButton.setMnemonic('A');
        addControlPane.add(addButton);
        addButton.addActionListener(actionListener2);

        addControlPane.add(Box.createHorizontalStrut(4));

        modifyButton = new UIButton(Inter.getLocText("FR-Designer_Modify"), BaseUtils.readIcon("com/fr/base/images/cell/control/rename.png"));
        modifyButton.setMnemonic('M');
        addControlPane.add(modifyButton);
        modifyButton.addActionListener(actionListener8);

        // peter:当鼠标进入修改按钮的时候,如果是ListConditon内容编辑区域不可编辑
        modifyButton.addMouseListener(mouseAdapter);
    }


    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("FR-Designer-Submit_Condition");
    }

    // samuel:移出来，方便调用
    protected void modify() {
        TreePath selectedTreePath = conditionsTree.getSelectionPath();
        // peter:当前的节点
        if (selectedTreePath != null) {
            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            JoinCondition oldJoinCondition = (JoinCondition) selectedTreeNode.getUserObject();
            oldJoinCondition.setJoin(andRadioButton.isSelected() ? DataConstants.AND : DataConstants.OR);

            Condition oldLiteCondition = oldJoinCondition.getCondition();
            // peter:如果当前选中的是ListCondition,只要改变Join为AND或者OR,直接返回.
            if (oldLiteCondition != null && !(oldLiteCondition instanceof ListCondition)) {
                // peter:先获得当前的LiteCondition.
                Condition liteCondition;
                if (commonRadioButton.isSelected()) {
                    liteCondition = defaultConditionPane.updateBean();
                } else {
                    liteCondition = new FormulaCondition(formulaTextArea.getText());
                }

                oldJoinCondition.setCondition(liteCondition);
            }

            // peter:需要reload parent
            DefaultTreeModel defaultTreeModel = (DefaultTreeModel) conditionsTree.getModel();
            ExpandMutableTreeNode parentTreeNode = (ExpandMutableTreeNode) selectedTreeNode.getParent();
            defaultTreeModel.reload(parentTreeNode);
            parentTreeNode.expandCurrentTreeNode(conditionsTree);
            conditionsTree.setSelectionPath(GUICoreUtils.getTreePath(selectedTreeNode));
        }
    }

    protected void swapNodesOfConditionTree(ExpandMutableTreeNode parentTreeNode, ExpandMutableTreeNode firstSelectTreeNode,
                                            ExpandMutableTreeNode secondTreeNode, int type) {
        int nextIndex = parentTreeNode.getIndex(firstSelectTreeNode);
        parentTreeNode.remove(firstSelectTreeNode);
        parentTreeNode.remove(secondTreeNode);

        parentTreeNode.insert(firstSelectTreeNode, nextIndex);
        parentTreeNode.insert(secondTreeNode, nextIndex);

        DefaultTreeModel treeModel = (DefaultTreeModel) conditionsTree.getModel();
        treeModel.reload(parentTreeNode);

        parentTreeNode.expandCurrentTreeNode(conditionsTree);
        if (type == MOVE_UP) {
            conditionsTree.setSelectionPath(GUICoreUtils.getTreePath(secondTreeNode));
        } else if (type == MOVE_DOWN) {
            conditionsTree.setSelectionPath(GUICoreUtils.getTreePath(firstSelectTreeNode));
        }


        // peter:检查Button Enabled.
        checkButtonEnabledForList();
    }


    ActionListener radioActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent evt) {
            applyCardsPane();
        }
    };

    private void applyCardsPane() {
        CardLayout cl = (CardLayout) (conditionCardPane.getLayout());
        if (this.commonRadioButton.isSelected()) {
            this.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_LiteCondition_Common_Condition"), null));
            cl.show(conditionCardPane, "DEFAULT");
        } else {
            this.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("FR-Designer_LiteCondition_Formula_Condition"), null));
            cl.show(conditionCardPane, "FORMULA");
        }
    }

    /**
     * peter:检查Button是否可以编辑.
     */
    private void checkButtonEnabledForList() {
        modifyButton.setEnabled(false);
        removeButton.setEnabled(false);
        this.moveUpButton.setEnabled(false);
        this.moveDownButton.setEnabled(false);
        this.bracketButton.setEnabled(false);
        this.unBracketButton.setEnabled(false);

        TreePath selectedTreePath = conditionsTree.getSelectionPath();
        if (selectedTreePath != null) {
            modifyButton.setEnabled(true);
            removeButton.setEnabled(true);

            // peter:根据选中的节点的是否是第一个或者最后一个.
            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) selectedTreeNode.getParent();
            if (parentTreeNode.getChildBefore(selectedTreeNode) != null) {
                moveUpButton.setEnabled(true);
            }
            if (parentTreeNode.getChildAfter(selectedTreeNode) != null) {
                moveDownButton.setEnabled(true);
            }

            // peter: 连续选中了超过两个条件,同时没有选中所有的节点.
            int selectionCount = conditionsTree.getSelectionCount();
            if (selectionCount > 1 && parentTreeNode.getChildCount() > selectionCount) {
                this.bracketButton.setEnabled(true);
            }

            // peter:选中的节点必须是ListCondition,才可以删除括号
            JoinCondition jonCondition = (JoinCondition) selectedTreeNode.getUserObject();
            Condition liteCondtion = jonCondition.getCondition();
            if (liteCondtion instanceof ListCondition) {
                this.unBracketButton.setEnabled(true);
            }
        }
    }

    /**
     * 扩展事件.
     */
    TreeExpansionListener treeExpansionListener = new TreeExpansionListener() {

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            TreePath selectedTreePath = event.getPath();
            if (selectedTreePath == null) {
                return;
            }

            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            selectedTreeNode.setExpanded(true);
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            TreePath selectedTreePath = event.getPath();
            if (selectedTreePath == null) {
                return;
            }

            ExpandMutableTreeNode selectedTreeNode = (ExpandMutableTreeNode) selectedTreePath.getLastPathComponent();
            selectedTreeNode.setExpanded(false);
        }
    };

    private ExpandMutableTreeNode getParentTreeNode() {
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) conditionsTree.getModel();
        TreePath selectedTreePath = conditionsTree.getSelectionPath();
        // peter:如果没有选择的节点,直接添加到根节点.
        ExpandMutableTreeNode parentTreeNode;
        if (selectedTreePath == null) {
            parentTreeNode = (ExpandMutableTreeNode) defaultTreeModel.getRoot();
        } else {
            parentTreeNode = (ExpandMutableTreeNode) ((ExpandMutableTreeNode) selectedTreePath.getLastPathComponent()).getParent();
        }
        // peter:如果没有选中的节点,直接返回.
        return parentTreeNode;
    }

    private boolean isExistedInParentTreeNode(ExpandMutableTreeNode parentTreeNode, JoinCondition newJoinCondition) {

        if (parentTreeNode == null) {
            return false;
        }
        JoinCondition parentJoinCondition = (JoinCondition) parentTreeNode.getUserObject();
        Condition parentLiteCondition = parentJoinCondition.getCondition();
        if (parentLiteCondition instanceof ListCondition) {
            // peter:在添加UserObject的节点.

            for (int i = 0; i < parentTreeNode.getChildCount(); i++) {
                ExpandMutableTreeNode tempTreeNode = (ExpandMutableTreeNode) parentTreeNode.getChildAt(i);
                Object tempObject = tempTreeNode.getUserObject();
                if (tempObject instanceof JoinCondition) {
                    JoinCondition tempJoinCondition = (JoinCondition) tempObject;
                    if (ComparatorUtils.equals(tempJoinCondition, newJoinCondition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private DefaultTreeCellRenderer conditionsTreeCellRenderer = new DefaultTreeCellRenderer() {

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) value;
            adjustParentListCondition(currentTreeNode);
            DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) currentTreeNode.getParent();

            this.setIcon(null);
            JoinCondition joinCondition = (JoinCondition) currentTreeNode.getUserObject();
            StringBuilder sBuf = new StringBuilder();

            Condition liteCondition = joinCondition.getCondition();
            if (parentTreeNode != null && parentTreeNode.getFirstChild() != currentTreeNode) {
                if (joinCondition.getJoin() == DataConstants.AND) {
                    sBuf.append("and ");
                } else {
                    sBuf.append("or  ");
                }
            }

            if (liteCondition != null) {
                // TODO alex:这里得到的liteCondition为什么会是null呢?
                sBuf.append(liteCondition.toString());
            }
            this.setText(sBuf.toString());

            return this;
        }
    };

    // peter:根据孩子几点,调整当前节点的ListCondition的值.
    protected void adjustParentListCondition(DefaultMutableTreeNode currentTreeNode) {
        DefaultMutableTreeNode parentTreeNode = (DefaultMutableTreeNode) currentTreeNode.getParent();

        Object userObj = currentTreeNode.getUserObject();
        if (userObj instanceof JoinCondition) {
            StringBuilder sBuf = new StringBuilder();

            JoinCondition joinCondition = (JoinCondition) userObj;
            Condition liteCondition = joinCondition.getCondition();
            if (parentTreeNode != null && parentTreeNode.getFirstChild() != currentTreeNode) {
                if (joinCondition.getJoin() == DataConstants.AND) {
                    sBuf.append("and ");
                } else {
                    sBuf.append("or  ");
                }
            }

            // peter:这个地方动态产生ListCondition,因为ListCondition的节点会变化的,
            // 父亲节点的ListCondition这个UserObject需要跟着变化.
            if (liteCondition instanceof ListCondition) {
                ListCondition listCondition = (ListCondition) liteCondition;
                listCondition.clearJoinConditions();

                // peter:动态添加孩子节点
                int childCount = currentTreeNode.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    Object tmpUserObject = ((DefaultMutableTreeNode) currentTreeNode.getChildAt(i)).getUserObject();
                    if (tmpUserObject instanceof JoinCondition) {
                        listCondition.addJoinCondition((JoinCondition) tmpUserObject);
                    }
                }
            }
        }
    }

    /**
     * Sets whether or not this component is enabled.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        // checkenabled.
        checkButtonEnabledForList();
    }

    /**
     * Populate.
     *
     * @param liteCondition lite condition.
     */
    @Override
    public void populateBean(Condition liteCondition) {
        // liteCondition = null 时清空显示
//    	if (liteCondition == null){
//    		return;
//    	}
        // peter: 先删除所有的节点
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) this.conditionsTree.getModel();
        ExpandMutableTreeNode rootTreeNode = (ExpandMutableTreeNode) defaultTreeModel.getRoot();
        rootTreeNode.setUserObject(new JoinCondition(DataConstants.AND, new ListCondition()));
        rootTreeNode.removeAllChildren();

        // peter:需要构建成ListCondition,加入到里面.
        if (liteCondition instanceof ListCondition) {
            ListCondition listCondition = (ListCondition) liteCondition;

            int joinConditionCount = listCondition.getJoinConditionCount();
            if (joinConditionCount == 0) {
                commonRadioButton.setSelected(true);
                applyCardsPane();

            }
            for (int i = 0; i < joinConditionCount; i++) {
                addLiteConditionToListCondition(rootTreeNode, listCondition.getJoinCondition(i));
            }
        } else if (needDoWithCondition(liteCondition)) {
            // peter:直接添加
            ExpandMutableTreeNode newTreeNode = new ExpandMutableTreeNode(new JoinCondition(DataConstants.AND, liteCondition));
            rootTreeNode.add(newTreeNode);
        }

        // peter:需要reload
        defaultTreeModel.reload(rootTreeNode);
        rootTreeNode.expandCurrentTreeNode(conditionsTree);
        // marks:默认的选择第一行
        if (conditionsTree.getRowCount() > 0) {
            conditionsTree.setSelectionRow(0);
        }
        this.checkButtonEnabledForList();
        if (liteCondition == null) {
            try {
                defaultConditionPane.checkValid();
            } catch (Exception ignored) {
                //not need
            }
        }
    }

    protected boolean needDoWithCondition(Condition liteCondition) {
        return true;
    }

    // peter:运用递归方式,构建初始的节点
    private void addLiteConditionToListCondition(ExpandMutableTreeNode parentTreeNode, JoinCondition joinCondition) {
        ExpandMutableTreeNode newTreeNode = new ExpandMutableTreeNode(joinCondition);
        parentTreeNode.add(newTreeNode);

        // peter:继续添加.
        Condition liteCondition = joinCondition.getCondition();
        if (liteCondition instanceof ListCondition) {
            ListCondition listCondition = (ListCondition) liteCondition;

            int joinConditionCount = listCondition.getJoinConditionCount();
            for (int i = 0; i < joinConditionCount; i++) {
                addLiteConditionToListCondition(newTreeNode, listCondition.getJoinCondition(i));
            }
        }
    }


    /**
     * Update.
     *
     * @return the new lite condition.
     */
    @Override
    public Condition updateBean() {
        // Samuel：先按modifybutton
        modify();
        // peter: 先删除所有的节点
        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) this.conditionsTree.getModel();
        ExpandMutableTreeNode rootTreeNode = (ExpandMutableTreeNode) defaultTreeModel.getRoot();

        int childCount = rootTreeNode.getChildCount();
        // peter: 如果只有一个孩子节点, 返回空的 ListCondition
        if (childCount == 0) {
            return new ListCondition();
        } // peter: 如果roottreeNode只有一个孩子节点.
        else if (childCount == 1) {
            JoinCondition joinCondition = (JoinCondition) ((ExpandMutableTreeNode) rootTreeNode.getChildAt(0)).getUserObject();
            return joinCondition.getCondition();
        } // peter: 有好多的孩子节点.
        else {
            // peter:深度遍历所有的孩子节点
            Enumeration depthEnumeration = rootTreeNode.depthFirstEnumeration();
            while (depthEnumeration.hasMoreElements()) {
                this.adjustParentListCondition((ExpandMutableTreeNode) depthEnumeration.nextElement());
            }

            JoinCondition joinCondition = (JoinCondition) rootTreeNode.getUserObject();
            Condition newCondition = joinCondition.getCondition();
            //clone(),防止多个条件分组使用同一个condition对象
            try{
                newCondition = (Condition)joinCondition.getCondition().clone();
            }catch (CloneNotSupportedException e){
                FRLogger.getLogger().error(e.getMessage());
            }
            return newCondition;
        }
    }
}