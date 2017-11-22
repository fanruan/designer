package com.fr.design.dscolumn;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.JPanel;

import com.fr.design.data.DesignTableDataManager;
import com.fr.data.TableDataSource;
import com.fr.design.condition.DSColumnLiteConditionPane;
import com.fr.design.condition.DSColumnSimpleLiteConditionPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;
import com.fr.general.data.Condition;
import com.fr.report.cell.CellElement;
import com.fr.report.cell.cellattr.core.group.DSColumn;
import com.fr.design.utils.gui.GUICoreUtils;

public class DSColumnConditionsPane extends BasicPane {
    //peter: Lite Condition.

    private DSColumnLiteConditionPane liteConditionPane;
    //marks:是否继承父格条件
    private UICheckBox reselectExpandCheckBox;
    //marks:作为布局的空字符串
    private static final String INSET_TEXT = "      ";

    public DSColumnConditionsPane() {
        this(DSColumnPane.SETTING_ALL);
    }

    public DSColumnConditionsPane(int setting) {
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            liteConditionPane = new DSColumnLiteConditionPane() {
                protected boolean isNeedDoWithCondition(Condition liteCondition) {
                    return liteCondition != null;
                }
            };
        } else {
            liteConditionPane = new DSColumnSimpleLiteConditionPane();
        }
        this.add(liteConditionPane, BorderLayout.CENTER);

        if (setting > DSColumnPane.SETTING_DSRELATED) {
            //alex:ReSelect
            JPanel pane = FRGUIPaneFactory.createX_AXISBoxInnerContainer_S_Pane();
//            pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
            pane.add(new UILabel(INSET_TEXT));

            reselectExpandCheckBox = new UICheckBox(Inter.getLocText("BindColumn-Extend_the_conditions_of_fatherCell(Applied_to_the_data_contains_other_data)"), false);
            pane.add(reselectExpandCheckBox);
            reselectExpandCheckBox.setSelected(true);

            JPanel reSelectPane = GUICoreUtils.createFlowPane(pane, FlowLayout.LEFT);
            this.add(reSelectPane, BorderLayout.NORTH);
            reSelectPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("BindColumn-The_Conditions_of_FatherCell"), null));
        }
    }

    @Override
    public String title4PopupWindow() {
        return Inter.getLocText("Filter");
    }

    public void populate(TableDataSource tds, CellElement cellElement) {
        if (cellElement == null) {
            return;
        }
        Object value = cellElement.getValue();
        if (value == null || !(value instanceof DSColumn)) {
            return;
        }
        DSColumn linearDSColumn = (DSColumn) value;

        if (reselectExpandCheckBox != null) {
            reselectExpandCheckBox.setSelected(!(linearDSColumn.isReselect()));
        }

        String[] columnNames = DesignTableDataManager.getSelectedColumnNames(tds, linearDSColumn.getDSName());
        liteConditionPane.populateColumns(columnNames);

        this.liteConditionPane.populateBean(linearDSColumn.getCondition());
    }

    public void update(CellElement cellElement) {
        if (cellElement == null) {
            return;
        }
        Object value = cellElement.getValue();
        if (value == null || !(value instanceof DSColumn)) {
            return;
        }
        DSColumn linearDSColumn = (DSColumn) value;
        linearDSColumn.setCondition(this.liteConditionPane.updateBean());
        if (reselectExpandCheckBox != null) {
            linearDSColumn.setReselect(!(this.reselectExpandCheckBox.isSelected()));
        }
    }

}