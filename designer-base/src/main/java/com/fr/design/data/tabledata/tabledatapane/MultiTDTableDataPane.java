package com.fr.design.data.tabledata.tabledatapane;

import com.fr.base.*;
import com.fr.data.impl.ConditionTableData;
import com.fr.data.impl.MultiTDTableData;
import com.fr.data.impl.UnionTableData;
import com.fr.design.data.DesignTableDataManager;
import com.fr.design.data.tabledata.wrapper.TableDataWrapper;
import com.fr.design.data.tabledata.wrapper.TemplateTableDataWrapper;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.formula.FormulaFactory;
import com.fr.design.formula.UIFormula;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itableeditorpane.ParameterTableModel;
import com.fr.design.gui.itableeditorpane.UITableEditAction;
import com.fr.design.gui.itableeditorpane.UITableEditorPane;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.ComparatorUtils;

import com.fr.script.Calculator;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.Icon;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class MultiTDTableDataPane extends AbstractTableDataPane<MultiTDTableData> {

    private static final int MAX_LENTH_OF_DATASET = 143; //关联数据集面板最大显示的数据集长度，超出这个长度显示数据集名称+“...”
    private static final int MIN_BAR_NUMBER = 10;
    private static final int SUB_LENGTH = 4;
    private JPanel centerPanel;
    private UITableEditorPane<ParameterProvider> editorPane;
    // key = name ; value = formula
    private HashMap<String, String> choosenTableData = new HashMap<String, String>();
    protected java.util.Map<String, TableDataWrapper> resMap;

    public MultiTDTableDataPane() {
        this(StringUtils.EMPTY);

    }

    public MultiTDTableDataPane(String multiName) {
        this.setLayout(new BorderLayout());
        this.add(initNorthPane(multiName), BorderLayout.CENTER);
        this.add(initSouthPanel(), BorderLayout.SOUTH);

    }

    private JPanel initNorthPane(String multiName) {
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        UILabel chooseTableData = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tabledata_Select_To_Merge"));
        UIButton previewButton = new UIButton();
        previewButton.setIcon(BaseUtils.readIcon("/com/fr/web/images/preview.png"));
        previewButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        previewButton.addActionListener(getPreviewActionListener());
        northPanel.add(chooseTableData);
        northPanel.add(previewButton);

        centerPanel = new JPanel();
        JScrollPane js = new JScrollPane(centerPanel);
        js.getVerticalScrollBar().setUnitIncrement(20);// 鼠标滚动大小
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        initAllBarPane(multiName);

        jpanel.add(northPanel, BorderLayout.NORTH);
        jpanel.add(js, BorderLayout.CENTER);

        return jpanel;
    }

    private void initAllBarPane(String multiName) {
        UILabel headLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Multi_Nam_Formula"));
        setResMap();
        int len = resMap.size();
        // 10个正好不会出现滚动条
        centerPanel.setLayout(new GridLayout(len < MIN_BAR_NUMBER ? MIN_BAR_NUMBER : len + 1, 1));
        centerPanel.add(headLabel);

        Iterator<Entry<String, TableDataWrapper>> entryIt = resMap.entrySet().iterator();
        while (entryIt.hasNext()) {
            TableDataWrapper tableDataWrappe = entryIt.next().getValue();
            String tmp = tableDataWrappe.getTableDataName();
            if (!ComparatorUtils.equals(tableDataWrappe.getTableDataName(), multiName)) {
                centerPanel.add(new BarPanel(tmp, tableDataWrappe.getIcon()));
            }
        }

    }

    protected void setResMap() {
        resMap = DesignTableDataManager.getAllEditingDataSet(DesignTableDataManager.getEditingTableDataSource());
    }

    private ActionListener getPreviewActionListener() {
        return new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MultiTDTableData td = MultiTDTableDataPane.this.updateBean();
                td.setTableDataSource(DesignTableDataManager.getEditingTableDataSource());
                new TemplateTableDataWrapper(td).previewData();
            }
        };
    }

    private JPanel initSouthPanel() {
        JPanel jpanel = new JPanel();
        jpanel.setPreferredSize(new Dimension(-1, 150));
        jpanel.setLayout(new BorderLayout());

        editorPane = new UITableEditorPane<ParameterProvider>(new ParameterTableModel() {
            @Override
            public UITableEditAction[] createAction() {
                return new UITableEditAction[]{new RefreshAction()};
            }
        }, " " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Tabledata_Default_Parameter"));

        jpanel.add(editorPane, BorderLayout.CENTER);

        return jpanel;
    }

    private class RefreshAction extends UITableEditAction {
        public RefreshAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Refresh"));
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/control/refresh.png"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }

        @Override
        public void checkEnabled() {
            //do nothing
        }
    }

    private void refresh() {
        int size = choosenTableData.size();
        String[] paramTexts = new String[size];
        Object[] object = choosenTableData.values().toArray();
        for (int i = 0; i < size; i++) {
            paramTexts[i] = (String) object[i];
        }
        List<ParameterProvider> existParameterList = new ArrayList<ParameterProvider>();
        Iterator<Entry<String, String>> dataItera = choosenTableData.entrySet().iterator();
        List<String> parameterName = new ArrayList<String>();
        while (dataItera.hasNext()) {
        	Entry<String, String> entry = dataItera.next();
        	TableData td = resMap.get(entry.getKey()).getTableData();
        	ParameterProvider[] currentparameters = td.getParameters(Calculator.createCalculator());
        	for (int i=0; i<currentparameters.length; i++){
        		if (parameterName.contains(currentparameters[i].getName())){
        			continue;
        		}
        		parameterName.add(currentparameters[i].getName());
        		existParameterList.add(currentparameters[i]);
        	}
        }
        ParameterProvider[] texts = ParameterHelper.analyze4Parameters(paramTexts, true);
        for (int i=0; i<texts.length; i++) {
        	if (parameterName.contains(texts[i].getName())){
        		continue;
        	}
        	existParameterList.add(texts[i]);
        }
        Parameter[] ps = existParameterList.toArray(new Parameter[existParameterList.size()]);
        editorPane.populate(ps);
        existParameterList.clear();
        parameterName.clear();
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Relation_TableData");
    }

    @Override
    public void populateBean(MultiTDTableData ob) {
        choosenTableData.clear();
        if (ob != null) {
            if (ob instanceof ConditionTableData) {
                String name;
                String formula;
                for (int i = 0; i < ob.getTableDataCount(); i++) {
                    name = ob.getTableDataName(i);
                    formula = ((ConditionTableData) ob).getConditionContent(i);
                    if (StringUtils.isNotEmpty(formula)) {
                        formula = formula.startsWith("=") ? formula.substring(1) : formula;
                    }
                    choosenTableData.put(name, formula);
                }

                editorPane.populate(((ConditionTableData) ob).getDefineParameters());
            } else if (ob instanceof UnionTableData) {
                // UnionTableData暂时不删, 留着做兼容
                String name;
                for (int i = 0; i < ob.getTableDataCount(); i++) {
                    name = ob.getTableDataName(i);
                    choosenTableData.put(name, "");
                }
            }
        }
        populateCenterPanel();
    }

    private void populateCenterPanel() {
        for (int i = 0, len = centerPanel.getComponentCount(); i < len; i++) {
            Component c = centerPanel.getComponent(i);
            if (c instanceof BarPanel) {
                ((BarPanel) c).populate();
            }
        }
    }

    @Override
    public MultiTDTableData updateBean() {
        ConditionTableData td = new ConditionTableData();
        Iterator<Entry<String, String>> it = choosenTableData.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            td.addTableData(entry.getKey(), entry.getValue());
        }

        List<ParameterProvider> paramList = editorPane.update();
        if (paramList != null) {
            td.setDefineParameters(paramList.toArray(new Parameter[paramList.size()]));
        }

        return td;
    }

    class BarPanel extends JPanel {

        private static final int BAR_HEIGHT = 33;

        private String name;
        private Icon icon;
        private UICheckBox chekbox;
        private UITextField formulaContentTextField;
        private UIButton formulaButton;
        private DocumentListener documentListener = new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFormula(formulaContentTextField.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFormula(formulaContentTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFormula(formulaContentTextField.getText());
            }
        };

        public BarPanel(String name, Icon icon) {
            this.name = name;
            this.icon = icon;
            initUI();
        }

        private void initUI() {
            String tmp = name;
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.setPreferredSize(new Dimension(190, BAR_HEIGHT));
            chekbox = new UICheckBox();
            UILabel tabledataName = new UILabel(tmp);
            while (tabledataName.getPreferredSize().width > MAX_LENTH_OF_DATASET) {
                tmp = tmp.substring(0, tmp.length() - SUB_LENGTH);
                tmp = tmp + "...";
                tabledataName = new UILabel(tmp);
            }
            UILabel iconLabel = new UILabel(icon);
            chekbox.addActionListener(tableDataCheckboxListener);
            iconLabel.addMouseListener(chooseTableDataListener);
            tabledataName.addMouseListener(chooseTableDataListener);
            leftPanel.add(chekbox);
            leftPanel.add(iconLabel);
            leftPanel.add(tabledataName);

            JPanel rightPanel = new JPanel();
            formulaContentTextField = new UITextField(BAR_HEIGHT);
            formulaContentTextField.setEnabled(false);
            formulaContentTextField.getDocument().addDocumentListener(documentListener);
            formulaButton = new UIButton("...");
            formulaButton.setEnabled(false);
            formulaButton.setPreferredSize(new Dimension(25, 23));
            formulaButton.addActionListener(getFormulaActionListener());
            rightPanel.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Conditions_Formula") + " ="));
            rightPanel.add(formulaContentTextField);
            rightPanel.add(formulaButton);

            this.setPreferredSize(new Dimension(-1, BAR_HEIGHT));
            this.add(leftPanel, BorderLayout.WEST);
            this.add(rightPanel, BorderLayout.CENTER);
        }

        public void populate() {
            Iterator<Entry<String, String>> it = choosenTableData.entrySet().iterator();
            chekbox.setSelected(false);
            formulaContentTextField.setEnabled(false);
            formulaButton.setEnabled(false);
            formulaContentTextField.getDocument().removeDocumentListener(documentListener);
            formulaContentTextField.setText("");
            while (it.hasNext()) {
                Entry<String, String> entry = it.next();
                if (ComparatorUtils.equals(name, entry.getKey())) {
                    chekbox.setSelected(true);
                    formulaContentTextField.setEnabled(true);
                    formulaButton.setEnabled(true);
                    formulaContentTextField.setText(entry.getValue());
                }
            }
            formulaContentTextField.getDocument().addDocumentListener(documentListener);
        }

        private MouseAdapter chooseTableDataListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String formula = formulaContentTextField.getText();
                if (chekbox.isSelected()) {
                    chekbox.setSelected(false);
                    formulaContentTextField.setEnabled(false);
                    formulaButton.setEnabled(false);
                    if (choosenTableData.containsKey(name)) {
                        choosenTableData.remove(name);
                    }
                } else {
                    formulaButton.setEnabled(true);
                    formulaContentTextField.setEnabled(true);
                    chekbox.setSelected(true);
                    choosenTableData.put(name, formula);
                }
            }
        };

        private ActionListener tableDataCheckboxListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String formula = formulaContentTextField.getText();
                if (chekbox.isSelected()) {
                    formulaButton.setEnabled(true);
                    formulaContentTextField.setEnabled(true);
                    choosenTableData.put(name, formula);
                } else {
                    formulaButton.setEnabled(false);
                    formulaContentTextField.setEnabled(false);
                    if (choosenTableData.containsKey(name)) {
                        choosenTableData.remove(name);
                    }
                }

            }
        };

        private void updateFormula(String newFormula) {
            if (!chekbox.isSelected() && StringUtils.isNotEmpty(newFormula)) {
                chekbox.setSelected(true);
            }
            choosenTableData.put(name, newFormula);
        }

        private ActionListener getFormulaActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final UIFormula formulaPane = FormulaFactory.createFormulaPane();
                    formulaPane.populate(BaseFormula.createFormulaBuilder().build(formulaContentTextField.getText()));
                    formulaPane.showLargeWindow(SwingUtilities.getWindowAncestor(MultiTDTableDataPane.this), new DialogActionAdapter() {
                        public void doOk() {
                            BaseFormula formula = formulaPane.update();
                            if (formula == null) {
                                formulaContentTextField.setText("");
                            } else {
                                formulaContentTextField.setText(formula.getContent().substring(1));
                            }
                            MultiTDTableDataPane.this.refresh();
                        }
                    }).setVisible(true);
                }
            };
        }
    }
}
