package com.fr.design.formula;

import com.fr.base.BaseFormula;
import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.actions.UpdateAction;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ilist.QuickList;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.RSyntaxTextArea;
import com.fr.design.gui.syntax.ui.rsyntaxtextarea.SyntaxConstants;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.parser.FRLexer;
import com.fr.parser.FRParser;
import com.fr.stable.EncodeConstants;
import com.fr.stable.ProductConstants;
import com.fr.stable.StringUtils;
import com.fr.stable.script.Expression;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;

/**
 * 公式编辑面板
 *
 * @editor zhou
 * @since 2012-3-29下午1:50:53
 */
public class FormulaPane extends BasicPane implements KeyListener, UIFormula {

    private VariableTreeAndDescriptionArea variableTreeAndDescriptionArea;
    private RSyntaxTextArea formulaTextArea;
    private UITextField keyWordTextField = new UITextField(18);
    private int currentPosition = 0;
    private int beginPosition = 0;
    private int insertPosition = 0;
    private JList tipsList;
    protected DefaultListModel listModel = new DefaultListModel();
    private int ifHasBeenWriten = 0;
    private DefaultListModel functionTypeListModel = new DefaultListModel();
    private QuickList functionTypeList;
    private DefaultListModel functionNameModel;
    private JList functionNameList;

    public FormulaPane() {
        initComponents();
    }

    private void initFormulaTextAreaKeyListener() {
        formulaTextArea.addKeyListener(this);
        formulaTextArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                formulaTextArea.setForeground(Color.black);
                String text = formulaTextArea.getText();
                // 判断在中文输入状态是否还包含提示符 要删掉
                //Tips:You_Can_Input_B1_To_Input_The_Data_Of_The_First_Row_Second_Column
                String tip = "\n\n\n" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaPane_Tips");
                if (text.contains(tip)) {
                    text = text.substring(0, text.indexOf(tip));
                    insertPosition = 0;
                    formulaTextArea.setText(text);
                }
            }
        });
    }

    private void initFormulaTextAreaMouseListener() {
        formulaTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                insertPosition = formulaTextArea.getCaretPosition();
                if (ifHasBeenWriten == 0) {
                    formulaTextArea.setText("");
                    ifHasBeenWriten = 1;
                    formulaTextArea.setForeground(Color.black);
                    insertPosition = 0;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentPosition = formulaTextArea.getCaretPosition();
                if (currentPosition == insertPosition) {
                    beginPosition = getBeginPosition();
                    insertPosition = beginPosition;
                    firstStepToFindTips(beginPosition);
                    fixFunctionNameList();
                }
            }
        });
    }

    private void initFormulaTextArea() {
        formulaTextArea = new RSyntaxTextArea();
        configFormulaArea();
        initFormulaTextAreaKeyListener();
        initFormulaTextAreaMouseListener();
    }

    private void initKeyWordTextFieldKeyListener() {
        keyWordTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String toFind = keyWordTextField.getText();
                    search(toFind, false);
                    fixFunctionNameList();
                    e.consume();
                }
            }
        });
    }

    private void initTipsPane() {
        // tipsPane
        JPanel tipsPane = new JPanel(new BorderLayout(4, 4));
        this.add(tipsPane, BorderLayout.EAST);

        JPanel searchPane = new JPanel(new BorderLayout(4, 4));
        searchPane.add(keyWordTextField, BorderLayout.CENTER);
        UIButton searchButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaPane_Search"));
        searchPane.add(searchButton, BorderLayout.EAST);
        tipsPane.add(searchPane, BorderLayout.NORTH);
        initKeyWordTextFieldKeyListener();
        tipsList = new JList(listModel);
        tipsList.addMouseListener(new DoubleClick());
        UIScrollPane tipsScrollPane = new UIScrollPane(tipsList);
        tipsScrollPane.setPreferredSize(new Dimension(170, 75));
        tipsScrollPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));
        tipsPane.add(tipsScrollPane, BorderLayout.CENTER);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String toFind = keyWordTextField.getText();
                search(toFind, false);
                formulaTextArea.requestFocusInWindow();
                fixFunctionNameList();
            }
        });
    }

    protected void initComponents() {
        this.setLayout(new BorderLayout(4, 4));

        initTextPane();
        initTipsPane();
        initVariableTreeAndDescriptionArea();
    }

    private void initVariableTreeAndDescriptionArea() {
        variableTreeAndDescriptionArea = new VariableTreeAndDescriptionArea();
        this.add(variableTreeAndDescriptionArea, BorderLayout.SOUTH);
    }

    private void initTextPane() {
        // text
        JPanel textPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.add(textPane, BorderLayout.CENTER);
        JPanel checkBoxandbuttonPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        UILabel formulaLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Input_Formula_In_The_Text_Area_Below") + ":"
                + "                         ");
        formulaLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        initFormulaTextArea();

        UIScrollPane formulaTextAreaScrollPane = new UIScrollPane(formulaTextArea);
        formulaTextAreaScrollPane.setBorder(null);
        textPane.add(formulaLabel, BorderLayout.NORTH);
        textPane.add(formulaTextAreaScrollPane, BorderLayout.CENTER);
        textPane.add(checkBoxandbuttonPane, BorderLayout.SOUTH);


        UIButton checkValidButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Check_Valid"));
        checkValidButton.addActionListener(checkValidActionListener);

        JPanel checkBoxPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        checkBoxPane.setPreferredSize(new Dimension(450, 30));
        checkBoxandbuttonPane.add(checkBoxPane, BorderLayout.WEST);
        checkBoxandbuttonPane.add(checkValidButton, BorderLayout.EAST);
        extendCheckBoxPane(checkBoxPane);
    }


    protected void extendCheckBoxPane(JPanel checkBoxPane) {
        // do nothing
    }

    private void configFormulaArea() {
        formulaTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_FORMULA);
        formulaTextArea.setAnimateBracketMatching(true);
        formulaTextArea.setAntiAliasingEnabled(true);
        formulaTextArea.setAutoIndentEnabled(true);
        formulaTextArea.setCodeFoldingEnabled(true);
        formulaTextArea.setUseSelectedTextColor(true);
        formulaTextArea.setCloseCurlyBraces(true);
        formulaTextArea.setBracketMatchingEnabled(true);
        formulaTextArea.setAntiAliasingEnabled(true);
        formulaTextArea.setCloseMarkupTags(true);
        formulaTextArea.setLineWrap(true);
    }

    public class DoubleClick extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int index = tipsList.getSelectedIndex();
            if (index != -1) {
                String currentLineContent = (String) listModel.getElementAt(index);
                if (e.getClickCount() == 2) {
                    if (ifHasBeenWriten == 0) {
                        formulaTextArea.setForeground(Color.black);
                        formulaTextArea.setText("");
                    }
                    formulaTextArea.setForeground(Color.black);
                    currentPosition = formulaTextArea.getCaretPosition();
                    String output = currentLineContent + "()";
                    String textAll = formulaTextArea.getText();
                    String textReplaced;
                    int position = 0;
                    if (insertPosition <= currentPosition) {
                        textReplaced = textAll.substring(0, insertPosition) + output + textAll.substring(currentPosition);
                        position = insertPosition + output.length() - 1;
                    } else {
                        textReplaced = textAll.substring(0, currentPosition) + output + textAll.substring(insertPosition);
                        position = currentPosition + output.length() - 1;
                    }
                    formulaTextArea.setText(textReplaced);
                    formulaTextArea.requestFocusInWindow();
                    formulaTextArea.setCaretPosition(position);
                    insertPosition = position;
                    ifHasBeenWriten = 1;
                    listModel.removeAllElements();
                } else if (e.getClickCount() == 1) {
                    refreshDescriptionTextArea(currentLineContent);

                    formulaTextArea.requestFocusInWindow();
                    fixFunctionNameList();
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (ifHasBeenWriten == 0) {
            this.formulaTextArea.setText(StringUtils.EMPTY);
        }
    }

    //hugh:为啥会是10呢？搞不懂~~~
    private static final int KEY_10 = 10;
    //上下左右
    private static final int KEY_37 = 37;
    private static final int KEY_38 = 38;
    private static final int KEY_39 = 39;
    private static final int KEY_40 = 40;

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KEY_38 || key == KEY_40 || key == KEY_37 || key == KEY_39 || key == KEY_10) //如果是删除符号  ，为了可读性 没有和其他按键的程序相融合
        {
            listModel.removeAllElements();
            currentPosition = formulaTextArea.getCaretPosition();
            insertPosition = currentPosition;
            beginPosition = getBeginPosition();
        } else {
            if (this.formulaTextArea.getText().trim().length() == 0) {
                insertPosition = 0;
                this.listModel.removeAllElements();
            } else {
                this.formulaTextArea.setForeground(Color.black);
                currentPosition = formulaTextArea.getCaretPosition();
                beginPosition = getBeginPosition();
                insertPosition = beginPosition;
                firstStepToFindTips(beginPosition);
                fixFunctionNameList();
                ifHasBeenWriten = 1;
            }
        }
    }

    private void fixFunctionNameList() {
        if (tipsList.getSelectedValue() != null) {
            int signOfContinue = 1;
            int indexOfFunction = 0;
            for (int i = 0; i < functionTypeListModel.size(); i++) {
                int signOfType = 0;
                FunctionGroup functionType = (FunctionGroup) functionTypeListModel.getElementAt(i);
                NameAndDescription[] nads = functionType.getDescriptions();
                if (signOfContinue == 1) {
                    functionNameModel.removeAllElements();
                    String functionName = ((String) tipsList.getSelectedValue());
                    for (int k = 0; k < nads.length; k++) {
                        functionNameModel.addElement(nads[k]);
                        if (functionName.equals(nads[k].getName()))//若相等，找出显示的函数的index，setSelectedIndex（）
                        {
                            signOfType = 1;
                            signOfContinue = 0;
                            indexOfFunction = k;
                        }
                    }

                    if (signOfType == 1) {
                        functionTypeList.setSelectedIndex(i);
                        signOfType = 0;
                    }
                }
            }
            functionNameList.setSelectedIndex(indexOfFunction);
            functionNameList.ensureIndexIsVisible(indexOfFunction);
        }

    }

    private int getBeginPosition() {
        int i = currentPosition;
        String textArea = formulaTextArea.getText();
        for (; i > 0; i--) {
            String tested = textArea.substring(i - 1, i).toUpperCase();
            char[] testedChar = tested.toCharArray();
            if (isChar(testedChar[0]) || isNum(testedChar[0])) {
                continue;
            } else {
                break;
            }
        }
        return i;
    }

    private void firstStepToFindTips(int theBeginPosition) {
        String textArea = formulaTextArea.getText();

        if (currentPosition > 0 && theBeginPosition < currentPosition) {
            String next = textArea.substring(theBeginPosition, theBeginPosition + 1);
            char[] nextChar = next.toCharArray();
            if (!isNum(nextChar[0])) {
                String toFind = textArea.substring(theBeginPosition, currentPosition);
                search(toFind, false);
                formulaTextArea.requestFocusInWindow();
            } else {
                listModel.removeAllElements();
            }
        } else {
            String toFind = textArea.substring(theBeginPosition, currentPosition);
            search(toFind, false);
            formulaTextArea.requestFocusInWindow();
        }
    }

    private static boolean isNum(char tested) {
        return tested >= '0' && tested <= '9';
    }

    private boolean isChar(char tested) {
        return tested >= 'A' && tested <= 'Z';
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    protected void search(String keyWord, boolean findDescription) {
        listModel.removeAllElements();

        keyWord = removeAllSpace(keyWord);
        if (keyWord.length() != 0) {
            NameAndDescription[] descriptions = FunctionConstants.ALL.getDescriptions();
            int lengthOfDes = descriptions.length;
            for (int i = 0; i < lengthOfDes; i++) {
                NameAndDescription and = descriptions[i];

                String functionName = and.searchResult(keyWord, findDescription);
                if (StringUtils.isNotBlank(functionName)) {
                    listModel.addElement(functionName);
                }
            }

            if (!listModel.isEmpty()) {
                tipsList.setSelectedIndex(0);
                refreshDescriptionTextArea((String) listModel.getElementAt(0));
            }
        }
    }

    private void refreshDescriptionTextArea(String line) {
        NameAndDescription[] descriptions = FunctionConstants.ALL.getDescriptions();
        int length = descriptions.length;
        for (int i = 0; i < length; i++) {
            NameAndDescription function = descriptions[i];
            String functionName = function.getName();
            if (functionName.equals(line)) {
                variableTreeAndDescriptionArea.descriptionTextArea.setText(function.getDesc());
                variableTreeAndDescriptionArea.descriptionTextArea.moveCaretPosition(0);
                break;
            }
        }
    }

    private String removeAllSpace(String toFind) {

        int index = toFind.indexOf(" ");
        while (index != -1) {
            toFind = toFind.substring(0, index) + toFind.substring(index + 1);
            index = toFind.indexOf(" ");
        }
        return toFind;
    }

    /**
     * Apply text.
     */
    private void applyText(String text) {
        if (text == null || text.length() <= 0) {
            return;
        }
        if (ifHasBeenWriten == 0) {
            formulaTextArea.setForeground(Color.black);
            formulaTextArea.setText("");
            ifHasBeenWriten = 1;
            insertPosition = 0;
        }
        String textAll = formulaTextArea.getText();
        currentPosition = formulaTextArea.getCaretPosition();
        int insert = 0;
        int current = 0;
        if (insertPosition <= currentPosition) {
            insert = insertPosition;
            current = currentPosition;
        } else {
            insert = currentPosition;
            current = insertPosition;
        }
        String beforeIndexOfInsertString = textAll.substring(0, insert);
        String afterIndexofInsertString = textAll.substring(current);
        formulaTextArea.setText(beforeIndexOfInsertString + text + afterIndexofInsertString);
        formulaTextArea.getText();
        if (text.indexOf("()") != -1) {
            formulaTextArea.setCaretPosition(insert + text.length() - 1);
        }
        formulaTextArea.requestFocus();
        insertPosition = formulaTextArea.getCaretPosition();
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Formula_Definition");
    }

    /**
     * Populate
     */
    public void populate(BaseFormula formula) {
        this.populate(formula, VariableResolver.DEFAULT);
    }

    public void populate(BaseFormula formula, VariableResolver variableResolver) {
        this.variableTreeAndDescriptionArea.populate(variableResolver);

        // set text
        if (formula != null) {
            String content = formula.getContent();
            if (content.trim().equals("=")) {
                this.formulaTextArea.setForeground(Color.gray);
                //Tips:You_Can_Input_B1_To_Input_The_Data_Of_The_First_Row_Second_Column
                this.formulaTextArea.setText("\n\n\n" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaPane_Tips"));
                this.formulaTextArea.setCaretPosition(0);
                ifHasBeenWriten = 0;
                this.listModel.removeAllElements();
            } else if (content.trim().charAt(0) == '=') {
                this.formulaTextArea.setText(content.trim().substring(1));
                currentPosition = formulaTextArea.getCaretPosition();
                beginPosition = getBeginPosition();
                insertPosition = beginPosition;
                firstStepToFindTips(beginPosition);
                fixFunctionNameList();
                ifHasBeenWriten = 1;
            } else {
                this.formulaTextArea.setText(content);
                currentPosition = formulaTextArea.getCaretPosition();
                beginPosition = getBeginPosition();
                insertPosition = beginPosition;
                firstStepToFindTips(beginPosition);
                fixFunctionNameList();
                ifHasBeenWriten = 1;
            }
        }
    }

    /**
     * update
     */
    public BaseFormula update() {
        BaseFormula formula;
        if (ifHasBeenWriten == 0) {
            formula = BaseFormula.createFormulaBuilder().build();
            return formula;
        } else {
            String content = this.formulaTextArea.getText();

            if (StringUtils.isEmpty(content) || content.trim().charAt(0) == '=') {
                formula = BaseFormula.createFormulaBuilder().build(content);
            } else {
                formula = BaseFormula.createFormulaBuilder().build("=" + content);
            }
            return formula;
        }
    }

    // check valid
    private ActionListener checkValidActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            // Execute Formula default cell element.
            String formulaText = formulaTextArea.getText().trim();

            if (formulaText != null && formulaText.length() > 0) {
                StringReader in = new StringReader(formulaText);

                FRLexer lexer = new FRLexer(in);
                FRParser parser = new FRParser(lexer);

                Expression expression = null;
                try {
                    expression = parser.parse();
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    // alex:继续往下面走,expression为null时告知不合法公式
                }

                JOptionPane.showMessageDialog(
                        FormulaPane.this,
                        /*
                        * alex:仅仅只需要根据expression是否为null作合法性判断
                        * 不需要eval
                        * TODO 但有个问题,有些函数的参数个数是有规定的,何以判别之
                        */
                        (expression != null ? com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Valid_Formula") : com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Invalid_Formula")) + ".", ProductConstants.PRODUCT_NAME,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    };

    public class VariableTreeAndDescriptionArea extends JPanel {

        private JTree variablesTree;
        private UITextArea descriptionTextArea;

        VariableTreeAndDescriptionArea() {
            this.initComponents();
        }

        private void initFunctionTypeList(JPanel functionPane) {
            functionTypeList = new QuickList(functionTypeListModel);
            UIScrollPane functionTypeScrollPane = new UIScrollPane(functionTypeList);
            functionTypeScrollPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));
            functionTypeScrollPane.setPreferredSize(new Dimension(140, 200));
            functionPane.add(this.createNamePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Function_Category") + ":", functionTypeScrollPane), BorderLayout.WEST);
            initTypeListCellRenderer();
            initGroupTypeModel();
            initTypeListSelectionListener();
        }

        private void initTypeListCellRenderer() {
            functionTypeList.setCellRenderer(
                    new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            if (value instanceof FunctionGroup) {
                                this.setText(((FunctionGroup) value).getGroupName());
                            }
                            return this;
                        }
                    });
        }

        private void initTypeListSelectionListener() {
            functionTypeList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent evt) {
                    Object selectedValue = ((JList) evt.getSource()).getSelectedValue();
                    if (!(selectedValue instanceof FunctionGroup)) {
                        return;
                    }
                    NameAndDescription[] nads = ((FunctionGroup) selectedValue).getDescriptions();
                    functionNameModel = (DefaultListModel) functionNameList.getModel();
                    functionNameModel.clear();
                    for (NameAndDescription nad : nads) {
                        functionNameModel.addElement(nad);
                    }
                    if (functionNameModel.size() > 0) {
                        functionNameList.setSelectedIndex(0);
                        functionNameList.ensureIndexIsVisible(0);
                    }
                }
            });
        }

        private void initGroupTypeModel() {
            functionTypeListModel.addElement(FunctionConstants.COMMON);
            for (int i = 0; i < FunctionConstants.EMBFUNCTIONS.length; i++) {
                functionTypeListModel.addElement(FunctionConstants.EMBFUNCTIONS[i]);
            }
            functionTypeListModel.addElement(FunctionConstants.ALL);
            functionTypeListModel.addElement(FunctionConstants.CUSTOM);
            functionTypeListModel.addElement(FunctionConstants.PLUGIN);

            //hugh: 从函数分组插件中添加分组
            FunctionConstants.addFunctionGroupFromPlugins(functionTypeListModel);
        }

        private void initFunctionNameListCellRenderer() {
            functionNameList.setCellRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof NameAndDescription) {
                        this.setText(((NameAndDescription) value).getName());
                    }
                    return this;
                }
            });
        }

        private void initFunctionNameListSelectionListener() {
            functionNameList.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent evt) {
                    Object selectedValue = functionNameList.getSelectedValue();
                    if (!(selectedValue instanceof NameAndDescription)) {
                        return;
                    }

                    String description = ((NameAndDescription) selectedValue).getDesc();
                    descriptionTextArea.setText(description);
                    setTextAreaText(description);
                    descriptionTextArea.moveCaretPosition(0);
                }
            });
        }

        private void initFunctionNameListMouseListener() {
            functionNameList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() >= 2) {
                        Object selectedValue = functionNameList.getSelectedValue();
                        if (!(selectedValue instanceof NameAndDescription)) {
                            return;
                        }
                        String insert = ((NameAndDescription) selectedValue).getName() + "()";
                        applyText(insert);

                    }
                    if (SwingUtilities.isRightMouseButton(evt)) {
                        JPopupMenu popupMenu = new JPopupMenu();
                        LookDetailAction lookDetailAction = new LookDetailAction();
                        popupMenu.add(lookDetailAction);

                        // peter: 只有弹出菜单有子菜单的时候,才需要弹出来.
                        GUICoreUtils.showPopupMenu(popupMenu, functionNameList, evt.getX() - 1, evt.getY() - 1);
                    }
                }
            });
        }

        private void initFunctionNameList(JPanel functionPane) {
            functionNameList = new JList(new DefaultListModel());
            UIScrollPane functionNameScrollPane = new UIScrollPane(functionNameList);
            functionNameScrollPane.setPreferredSize(new Dimension(140, 200));
            functionPane.add(
                    this.createNamePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Function_Name") + ":", functionNameScrollPane),
                    BorderLayout.CENTER);
            functionNameScrollPane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));
            initFunctionNameListCellRenderer();
            initFunctionNameListSelectionListener();
            initFunctionNameListMouseListener();
        }

        private void initDescriptionTextArea() {
            // Description
            descriptionTextArea = new UITextArea(16, 27);

            UIScrollPane desScrollPane = new UIScrollPane(descriptionTextArea);
            desScrollPane.setBorder(null);
            this.add(this.createNamePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaPane_Formula_Description") + ":", desScrollPane), BorderLayout.EAST);
            descriptionTextArea.setBackground(Color.white);
            descriptionTextArea.setLineWrap(true);
            descriptionTextArea.setWrapStyleWord(true);
            descriptionTextArea.setEditable(false);
            descriptionTextArea.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() >= 2) {
                        showPopupPane();
                    }
                }
            });

        }

        private StringBuilder getText(TextUserObject selectedValue, String path) throws IOException {
            Reader desReader;
            StringBuilder desBuf = new StringBuilder();
            InputStream desInputStream = BaseUtils.readResource(path + ((TextUserObject) selectedValue).displayText + ".txt");
            if (desInputStream == null) {
                String description = "";
                desReader = new StringReader(description);
            } else {
                desReader = new InputStreamReader(desInputStream, EncodeConstants.ENCODING_UTF_8);
            }
            BufferedReader reader = new BufferedReader(desReader);
            String lineText;
            while ((lineText = reader.readLine()) != null) {
                if (desBuf.length() > 0) {
                    desBuf.append('\n');
                }
                desBuf.append(lineText);
            }
            reader.close();
            desReader.close();
            return desBuf;
        }

        private void initVariablesTreeSelectionListener() {
            variablesTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    Object selectedValue = ((DefaultMutableTreeNode) variablesTree.getLastSelectedPathComponent()).getUserObject();
                    if (selectedValue == null) {
                        return;
                    }
                    StringBuilder desBuf = new StringBuilder();
                    try {
                        String path;
                        Locale locale = FRContext.getLocale();
                        if (locale.equals(Locale.CHINA)) {
                            path = "/com/fr/design/insert/formula/variable/cn/";
                        } else {
                            path = "/com/fr/design/insert/formula/variable/en/";
                        }
                        if (selectedValue instanceof TextUserObject) {
                            desBuf = getText((TextUserObject) selectedValue, path);
                        }
                    } catch (IOException exp) {
                        FineLoggerFactory.getLogger().error(exp.getMessage(), exp);
                    }
                    descriptionTextArea.setText(desBuf.toString());
                    descriptionTextArea.moveCaretPosition(0);
                }
            });
        }

        private void initVariablesTree() {
            // vairable.
            variablesTree = new JTree();
            UIScrollPane variablesTreePane = new UIScrollPane(variablesTree);
            variablesTreePane.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, UIConstants.ARC));
            this.add(this.createNamePane(
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaPane_Variables") + ":", variablesTreePane), BorderLayout.CENTER);
            variablesTree.setRootVisible(false);
            variablesTree.setShowsRootHandles(true);
            variablesTree.addMouseListener(applyTextMouseListener);
            variablesTree.setCellRenderer(applyTreeCellRenderer);

            initDescriptionTextArea();

            initVariablesTreeSelectionListener();
        }

        private void initComponents() {
            this.setLayout(new BorderLayout(4, 4));
            initVariablesTree();
            initFunctionPane();
        }

        private void initFunctionPane() {
            JPanel functionPane = new JPanel(new BorderLayout(4, 4));
            this.add(functionPane, BorderLayout.WEST);
            initFunctionTypeList(functionPane);
            initFunctionNameList(functionPane);
            functionTypeList.setSelectedIndex(0);
        }

        /*
         * 查看函数的详细信息
         */
        private class LookDetailAction extends UpdateAction {

            public LookDetailAction() {
                this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaPane_Function_Detail"));
                this.setMnemonic('L');
                this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_file/preview.png"));
            }

            // 弹出的窗口中显示函数的用法明细
            public void actionPerformed(ActionEvent evt) {
                showPopupPane();
            }
        }

        private void showPopupPane() {
            BasicPane basicPane = new BasicPane() {
                @Override
                protected String title4PopupWindow() {
                    return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaPane_Function_Detail");
                }
            };
            basicPane.setLayout(FRGUIPaneFactory.createBorderLayout());
            UITextArea desArea = new UITextArea();
//            desArea。setEnabled(false);
            desArea.setText(this.getTextAreaText());
            basicPane.add(new UIScrollPane(desArea), BorderLayout.CENTER);
            BasicDialog dialog = basicPane.showWindow(DesignerContext.getDesignerFrame());
            dialog.setVisible(true);
        }

        private String getTextAreaText() {
            return this.descriptionTextArea.getText();
        }

        private void setTextAreaText(String text) {
            this.descriptionTextArea.setText(text);
        }

        private JPanel createNamePane(String name, JComponent comp) {
            JPanel namePane = new JPanel(new BorderLayout(4, 4));
            namePane.add(new UILabel(name), BorderLayout.NORTH);
            namePane.add(comp, BorderLayout.CENTER);
            return namePane;
        }

        private MouseListener applyTextMouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() >= 2) {
                    Object source = evt.getSource();

                    if (source instanceof JTree) {
                        JTree tree = (JTree) source;
                        TreePath selectedTreePah = tree.getSelectionPath();
                        if (selectedTreePah != null) {
                            DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) selectedTreePah.getLastPathComponent();
                            Object userObject = selectedTreeNode.getUserObject();
                            if (userObject != null && userObject instanceof TextUserObject) {

                                applyText(((TextUserObject) userObject).getText());
                            }
                        }
                    }
                }
            }
        };
        private DefaultTreeCellRenderer applyTreeCellRenderer = new DefaultTreeCellRenderer() {

            public Component getTreeCellRendererComponent(JTree tree,
                                                          Object value, boolean selected, boolean expanded,
                                                          boolean leaf, int row, boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected,
                        expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
                Object userObj = treeNode.getUserObject();

                if (userObj instanceof TextUserObject) {
                    this.setIcon(null);
                    this.setText(((TextUserObject) userObj).getDisplayText());
                } else if (userObj instanceof TextFolderUserObject) {
                    TextFolderUserObject textUserObject = (TextFolderUserObject) userObj;
                    if (leaf) {
                        this.setText(textUserObject.getText());
                    } else {
                        this.setText(textUserObject.getText() + " - ["
                                + treeNode.getChildCount() + "]");
                    }

                    this.setIcon(textUserObject.getIcon());
                }

                return this;
            }
        };

        public void populate(VariableResolver variableResolver) {
            // varibale tree.
            DefaultTreeModel variableModel = (DefaultTreeModel) variablesTree.getModel();

            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) variableModel.getRoot();
            rootNode.removeAllChildren();

            if (variableResolver.isBindCell()) {
                // 加上当前值"$$$"
                DefaultMutableTreeNode bindCellNode = new DefaultMutableTreeNode(new TextUserObject("$$$"));
                rootNode.add(bindCellNode);
            }

            rootNode.add(new TextFolderUserObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaD_Data_Fields"),
                    BaseUtils.readIcon("/com/fr/design/images/dialog/table.png"),
                    variableResolver.resolveColumnNames()).createMutableTreeNode());

            // Set cutReport Variable
            rootNode.add(new TextFolderUserObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_FormulaPane_Variables"),
                    BaseUtils.readIcon("/com/fr/design/images/dialog/variable.png"),
                    variableResolver.resolveCurReportVariables()).createMutableTreeNode());

            rootNode.add(new TextFolderUserObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Parameter_Source_Type_Tabledata"),
                    BaseUtils.readIcon("/com/fr/design/images/dialog/parameter.gif"),
                    variableResolver.resolveTableDataParameterVariables()).createMutableTreeNode());

            rootNode.add(new TextFolderUserObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ParameterD_Report_Parameter"),
                    BaseUtils.readIcon("/com/fr/design/images/m_report/p.gif"),
                    variableResolver.resolveReportParameterVariables()).createMutableTreeNode());

            rootNode.add(new TextFolderUserObject(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_M_Server_Global_Parameters"),
                    BaseUtils.readIcon("/com/fr/design/images/dialog/parameter.gif"),
                    variableResolver.resolveGlobalParameterVariables()).createMutableTreeNode());

            variableModel.reload();
            // Expand
            for (int row = 0; row < this.variablesTree.getRowCount(); row++) {
                this.variablesTree.expandRow(row);
            }
        }
    }

    public static class TextFolderUserObject {

        private String text;
        private Icon icon;
        private String[] subNodes = new String[0];

        TextFolderUserObject(String text, Icon icon, String[] subNodes) {
            this.text = text;
            this.icon = icon;
            this.subNodes = subNodes;
        }

        public String getText() {
            return this.text;
        }

        public Icon getIcon() {
            return this.icon;
        }

        MutableTreeNode createMutableTreeNode() {
            DefaultMutableTreeNode variableTreeNode = new DefaultMutableTreeNode(this);

            for (String subNode : subNodes) {
                variableTreeNode.add(new DefaultMutableTreeNode(new TextUserObject(subNode)));
            }

            return variableTreeNode;
        }
    }

    public static class TextUserObject {

        TextUserObject(String text) {
            this(text, text);
        }

        TextUserObject(String text, String displayText) {
            this.text = text;
            this.displayText = displayText;
        }

        public String getText() {
            return this.text;
        }

        String getDisplayText() {
            return this.displayText;
        }

        private String text;
        private String displayText;
    }

    public static void main(String[] args) {
        FunctionGroup group = FunctionConstants.ALL;
        NameAndDescription[] nameAndDescriptions = group.getDescriptions();
        StringBuffer buffer = new StringBuffer();
        for (NameAndDescription d : nameAndDescriptions) {
            String name = d.getName();
            buffer.append("\"");
            buffer.append(name.toUpperCase());
            buffer.append("\"");
            buffer.append("|");
            buffer.append("\n");
            buffer.append("\"");
            buffer.append(name.toLowerCase());
            buffer.append("\"");
            buffer.append("|");
            buffer.append("\n");
        }
        FineLoggerFactory.getLogger().debug(buffer.toString());
    }
}
