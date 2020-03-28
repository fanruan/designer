package com.fr.design.data.datapane;

import com.fr.base.BaseFormula;
import com.fr.base.Parameter;
import com.fr.base.ParameterHelper;
import com.fr.base.ParameterMapNameSpace;
import com.fr.base.Utils;
import com.fr.data.impl.EmbeddedTableData;
import com.fr.design.data.datapane.preview.PreviewLabel;
import com.fr.design.data.datapane.preview.PreviewTablePane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.gui.itree.refreshabletree.ExpandMutableTreeNode;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.script.Calculator;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : Shockway
 * Date: 13-8-16
 * Time: 下午3:46
 */
public class ChoosePaneSupportFormula extends ChoosePane {
    private String ori_ds_formula;
    private String ori_ds_name;
    private String ori_table_formula;
    private String ori_table_name;

    public ChoosePaneSupportFormula() {
        this(null);
    }

    public ChoosePaneSupportFormula(PreviewLabel.Previewable parent) {
        this(parent, -1);
    }

    public ChoosePaneSupportFormula(PreviewLabel.Previewable parent, int labelSize) {
        super(parent, labelSize);
        this.dsNameComboBox.setEditable(true);
    }

    protected void addDSBoxListener() {
        dsNameComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                schemaBox.setSelectedIndex(-1);
                // 如果table是用公式编辑的 没必要联动清除
                if (Utils.objectToString(tableNameComboBox.getEditor().getItem()).startsWith("=")) {
                    return;
                }
                tableNameComboBox.setSelectedItem("");
                JTree tree = tableNameComboBox.getTree();
                if (tree == null) {
                    return;
                }
                DefaultMutableTreeNode rootTreeNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
                rootTreeNode.removeAllChildren();
                rootTreeNode.add(new ExpandMutableTreeNode("Loading..."));
                ((DefaultTreeModel) tree.getModel()).reload();
            }
        });
    }

    protected void addFocusListener() {
        // Do nothing
    }

    /**
     * 只弹出数据库框里的参数
     *
     * @return
     */
    protected String getDSName() {
        String selectedDSName = null;
        String item = Utils.objectToString(this.dsNameComboBox.getEditor().getItem());
        // 没有选中的列表项 那么看看是不是手输值
        if (item == null) {
            return null;
        }
        if (ComparatorUtils.equals(ori_ds_formula, item)) {
            return ori_ds_name;
        }
        ori_ds_formula = item;
        // 公式 需要解析 输入默认值
        if (item.startsWith("=")) {
            Calculator ca = Calculator.createCalculator();
            boolean isFormula = true;
            Parameter[] parameters = ParameterHelper.analyze4Parameters(new String[]{item}, isFormula);
            if (parameters.length > 0) {
                final Map paraMap = new HashMap();
                analyseParaDefaultValue(parameters);
                final ParameterInputPane pPane = new ParameterInputPane(parameters, false);
                pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                    public void doOk() {
                        paraMap.putAll(pPane.update());
                    }
                }).setVisible(true);
                ca.pushNameSpace(ParameterMapNameSpace.create(paraMap));
            }
            try {
                selectedDSName = Utils.objectToString(ca.eval(BaseFormula.createFormulaBuilder().build(item)));
//				selectedDSName = ParameterHelper.analyzeCurrentContextTableData4Templatee(item, parameters);
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        } else {
            selectedDSName = item;
        }
        ori_ds_name = selectedDSName;
        return selectedDSName;
    }

    /**
     * 在一个参数弹窗中弹出数据库框和表框的参数
     *
     * @return
     */
    protected String[] getDSAndTableName() {
        String dsName = "", tableName = "";
        String dsItem = Utils.objectToString(this.dsNameComboBox.getEditor().getItem());
        String tableItem = this.getTableName();
        if (ComparatorUtils.equals(ori_ds_formula, dsItem) && ComparatorUtils.equals(ori_table_formula, tableItem)) {
            return new String[]{ori_ds_name, ori_table_name};
        }
        ori_ds_formula = dsItem;
        ori_table_formula = tableItem;
        List paraList = new ArrayList();
        pushPara(dsItem, paraList);
        pushPara(tableItem, paraList);
        Calculator ca = Calculator.createCalculator();
        if (!paraList.isEmpty()) {
            Parameter[] parameters = (Parameter[]) paraList.toArray(new Parameter[paraList.size()]);
            final Map paraMap = new HashMap();
            analyseParaDefaultValue(parameters);
            final ParameterInputPane pPane = new ParameterInputPane(parameters, false);
            pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                public void doOk() {
                    paraMap.putAll(pPane.update());
                }
            }).setVisible(true);
            ca.pushNameSpace(ParameterMapNameSpace.create(paraMap));
        }
        try {
            dsName = dsItem.startsWith("=") ? Utils.objectToString(ca.eval(BaseFormula.createFormulaBuilder().build(dsItem))) : dsItem;
            tableName = tableItem.startsWith("=") ? Utils.objectToString(ca.eval(BaseFormula.createFormulaBuilder().build(tableItem))) : tableItem;
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        ori_ds_name = dsName;
        ori_table_name = tableName;
        return new String[]{dsName, tableName};
    }

    private void pushPara(String item, List paraList) {
        if (item.startsWith("=")) {
            Parameter[] parameters = ParameterHelper.analyze4Parameters(new String[]{item}, true);
            for (Parameter p : parameters) {
                if (!paraList.contains(p)) {
                    paraList.add(p);
                }
            }
        }
    }


	protected void failedToFindTable() {
		this.ori_ds_formula = null;
		this.ori_table_formula = null;
	}

    /**
     *  预览key value对应的数据
     * @param key   键
     * @param value    值
     */
	public void preview(int key, int value) {
		EmbeddedTableData tb = PreviewTablePane.previewTableData(createSelectTableData(), key, value);
		if (tb == null) {
			failedToFindTable();
		}
	}

    public DataBaseItems updateBean() {
        return updateBean(false);
    }

    /**
     * 这个分两种情况 设计的时候弹窗填入参数 需要返回计算后的值
     * 而当写入xml的时候 需要取他的原始formula
     *
     * @param getFormula
     * @return
     */
    public DataBaseItems updateBean(boolean getFormula) {
        String[] names;
        if (getFormula) {
            names = new String[]{Utils.objectToString(this.dsNameComboBox.getEditor().getItem()), this.getTableName()};
        } else {
            names = getDSAndTableName();
        }
        if (names != null && names.length == 2) {
            return new DataBaseItems(names[0], this.schemaBox.getSelectedItem(), names[1]);
        } else {
            return new DataBaseItems("", "", "");
        }
    }

    /**
     * 从模板参数和全局参数中取参数的默认值
     *
     * @param ps   参数
     */
    public void analyseParaDefaultValue(Parameter[] ps) {
        JTemplate jTemplate = DesignerContext.getDesignerFrame().getSelectedJTemplate();
        Parameter[] allParas = jTemplate.getParameters();
        for (int i = 0; i < ps.length; i++) {
            Parameter p = ps[i];
            for (int j = 0; j < allParas.length; j++) {
                if (ComparatorUtils.equals(p.getName(), allParas[j].getName())) {
                    p.setValue(allParas[j].getValue());
                }
            }
        }
    }

}